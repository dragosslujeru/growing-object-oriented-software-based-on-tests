package com.better.auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import test.com.better.auctionsniper.SniperListener;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Application {

    public static final String MAIN_WINDOW_NAME = "Auction Sniper";
    public static final String SNIPER_STATUS_LABEL_NAME = "status";
    private static final String RESOURCE_NAME = "Auction";
    private static final String AUCTION_ID_FORMAT = "auction-%s@%s/" + RESOURCE_NAME;
    public static final String BID_COMMAND_FORMAT = "SOLVersion:1.1; Command: BID; Price: %d;";
    public static final String JOIN_COMMAND_FORMAT = "SOLVersion:1.1; Command: JOIN;";

    private Chat notToBeGCed;

    private MainWindow ui;

    public Application() {
        ui = new MainWindow();
    }

    public static void main(String... args) throws Exception {
        Application application = new Application();
        Connection connection = application.connection(args[0], args[1], args[2]);
        application.joinAuction(connection, args[3]);
    }

    private Connection connection(String hostname, String username, String password) throws XMPPException {
        Connection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, RESOURCE_NAME);
        return connection;
    }


    public void joinAuction(Connection connection, String itemId) {
        disconnectWhenUICloses(connection);
        Chat chat = connection.getChatManager().createChat(auctionId(itemId, connection), null);
        this.notToBeGCed = chat;

        Auction auction = new XMPPAuction(chat);
        chat.addMessageListener(new AuctionMessageTranslator(new AuctionSniper(auction, new SniperStateDisplayer()),
                connection.getUser()));
        auction.join();
    }

    private void disconnectWhenUICloses(Connection connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                connection.disconnect();
            }
        });
    }

    private String auctionId(String itemId, Connection connection) {
        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }

    public class SniperStateDisplayer implements SniperListener {

        @Override
        public void sniperLost() {
            showStatus("Lost");
        }

        @Override
        public void sniperBidding() {
            showStatus("Bidding");
        }

        @Override
        public void sniperWinning() {
            showStatus("Winning");

        }

        @Override
        public void sniperWon() {
            showStatus("Won");
        }

        private void showStatus(String status) {
            SwingUtilities.invokeLater(() -> ui.showStatus(status));
        }
    }
}
