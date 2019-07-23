package com.better;

import com.better.auctionsniper.Application;
import com.better.auctionsniper.MainWindow;

public class ApplicationRunner {

    private String itemId;

    private static final String AUCTION_HOSTNAME = "localhost";

    private static final String SNIPER_USERNAME = "sniper";
    private static final String SNIPER_PASSWORD = "sniper";

    public static final String SNIPER_XMPP_ID = "sniper@localhost/Auction";

    private AuctionSniperDriver driver;

    public void startBiddingIn(FakeAuctionServer auctionServer) {
        Thread thread = new Thread("Test application") {
            @Override
            public void run() {
                try {
                    Application.main(AUCTION_HOSTNAME, SNIPER_USERNAME, SNIPER_PASSWORD, auctionServer.getItemId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        driver = new AuctionSniperDriver(1000);
    }

    public void showsSniperHasLostAuction() {
        driver.showsSniperStatus(MainWindow.STAUS_LOST);
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

    public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
        driver.showsSniperStatus(itemId, lastPrice, lastBid, MainWindow.STATUS_BIDDING);
    }

    public void hasShownSniperIsWinning(int winningBid) {
        driver.showsSniperStatus(itemId, winningBid, winningBid, MainWindow.STATUS_WINNING);
    }

    public void showsSniperHaWonAuction(int lastBid) {
        driver.showsSniperStatus(itemId, lastBid, lastBid, MainWindow.STATUS_WON);
    }
}
