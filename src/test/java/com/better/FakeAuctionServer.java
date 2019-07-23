package com.better;

import org.jivesoftware.smack.*;

import static com.better.auctionsniper.Application.BID_COMMAND_FORMAT;
import static com.better.auctionsniper.Application.JOIN_COMMAND_FORMAT;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class FakeAuctionServer {

    private static final String RESOURCE_NAME = "Auction";

    private Connection connection;

    public FakeAuctionServer() {
        this.connection = new XMPPConnection("localhost");
    }

    private SingleMessageListener messageListener = new SingleMessageListener(message -> System.err.println(
            "RECEIVED SOMETHING FROM SNIPER"));

    private Chat currentChat;

    public void start() throws XMPPException {
        connection.connect();
        connection.login("auction-item-123", "auction", RESOURCE_NAME);
        connection.getChatManager();

        connection.getChatManager().addChatListener((chat, createdLocally) -> {
            currentChat = chat;
            currentChat.addMessageListener(messageListener);
        });
    }

    public void announceClosed() throws XMPPException {
        currentChat.sendMessage("SOLVersion: 1.1; Event: CLOSE;");
    }

    public void hasReceivedJoinFromSinper(String sniperId) throws InterruptedException {
        messageListener.hadReceivedMessageMatching(equalTo(JOIN_COMMAND_FORMAT));
        assertThat(currentChat.getParticipant(), equalTo(sniperId));
    }

    public String getItemId() {
        return "item-123";
    }

    public void reportPrice(int price, int increment, String bidder) throws XMPPException {
        currentChat.sendMessage(String.format("SOLVersion: 1.1; Event: PRICE; "
                        + "CurrentPrice: %d; Increment: %d; Bidder: %s;",
                price, increment, bidder));
    }

    public void hasReceivedBid(int bid, String sniperId) throws InterruptedException {
        messageListener.hadReceivedMessageMatching(equalTo(String.format(BID_COMMAND_FORMAT, bid)));
        assertThat(currentChat.getParticipant(), equalTo(sniperId));
    }

    public void stop() {
        connection.disconnect();
    }

}
