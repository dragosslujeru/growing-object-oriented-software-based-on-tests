package com.better.auctionsniper;

import test.com.better.auctionsniper.SniperListener;

public class AuctionSniper implements AuctionEventListener {


    private final SniperListener sniperListener;
    private final Auction auction;

    boolean winning;

    public AuctionSniper(Auction auction, SniperListener sniperListener) {
        this.sniperListener = sniperListener;
        this.auction = auction;
    }

    @Override
    public void auctionClosed() {
        if (winning) {
            sniperListener.sniperWon();
        } else {
            sniperListener.sniperLost();
        }
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource priceSource) {
        winning = priceSource.equals(PriceSource.FromSniper);
        if (winning) {
            sniperListener.sniperWinning();
        } else {
            sniperListener.sniperBidding();
            auction.bid(price + increment);
        }
    }
}
