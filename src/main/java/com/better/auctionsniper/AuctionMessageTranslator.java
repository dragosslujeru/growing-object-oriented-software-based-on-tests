package com.better.auctionsniper;

import com.better.auctionsniper.AuctionEventListener.PriceSource;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.HashMap;
import java.util.Map;

public class AuctionMessageTranslator implements MessageListener {

    private final AuctionEventListener listener;

    private final String sniperId;

    public AuctionMessageTranslator(AuctionEventListener listener, String sniper_id) {
        this.listener = listener;
        sniperId = sniper_id;
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        AuctionEvent event = AuctionEvent.from(message);
        String type = event.type();
        if ("CLOSE".equals(type)) {
            listener.auctionClosed();
        } else if ("PRICE".equals(type)) {
            listener.currentPrice(event.currentPrice(), event.increment(), event.isFrom(sniperId));
        }
    }

    private static class AuctionEvent {
        Map<String, String> fields = new HashMap<>();

        public int increment() {
            return getInt("Increment");
        }

        public String type() {
            return get("Event");
        }

        public int currentPrice() {
            return getInt("CurrentPrice");
        }

        public String bidder() {
            return get("Bidder");
        }

        public PriceSource isFrom(String sniperId) {
            return bidder().equals(sniperId) ? PriceSource.FromSniper : PriceSource.FromOtherBidder;
        }

        static AuctionEvent from(Message message) {
            AuctionEvent auctionEvent = new AuctionEvent();
            for (String field : fieldsIn(message.getBody())) {
                auctionEvent.addField(field);
            }
            return auctionEvent;
        }

        private String get(String fieldName) {
            return fields.get(fieldName);
        }

        private Integer getInt(String fieldName) {
            return Integer.parseInt(fields.get(fieldName));
        }

        private void addField(String field) {
            String[] pair = field.split(":");
            fields.put(pair[0].trim(), pair[1].trim());
        }

        static String[] fieldsIn(String messageBody) {
            return messageBody.split(";");
        }
    }
}
