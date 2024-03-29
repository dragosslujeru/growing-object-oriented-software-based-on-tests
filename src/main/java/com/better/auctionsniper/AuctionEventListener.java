package com.better.auctionsniper;

public interface AuctionEventListener {

    void auctionClosed();

    void currentPrice(int price, int increment, PriceSource priceSource);

    enum PriceSource {
        FromOtherBidder, FromSniper

    }
}
