package integration.com.better.auctionsniper;

import com.better.ApplicationRunner;
import com.better.FakeAuctionServer;
import org.jivesoftware.smack.XMPPException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuctionSniperEndToEndTest {

    private FakeAuctionServer auction = new FakeAuctionServer();
    private ApplicationRunner application = new ApplicationRunner();


    @Test
    public void sniperJoinsAndLosesWithoutBid() throws XMPPException, InterruptedException {
        auction.start();
        application.startBiddingIn(auction);
        auction.hasReceivedJoinFromSinper(ApplicationRunner.SNIPER_XMPP_ID);
        auction.announceClosed();
        application.showsSniperHasLostAuction();
    }

    @Test
    public void sniperMakesHigherBidButLoses() throws XMPPException, InterruptedException {
        auction.start();
        application.startBiddingIn(auction);
        auction.hasReceivedJoinFromSinper(ApplicationRunner.SNIPER_XMPP_ID);
        auction.reportPrice(1000, 98, "other bidder");

        application.hasShownSniperIsBidding(1000, 1098);
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
        auction.announceClosed();
        application.showsSniperHasLostAuction();
    }

    @Test
    public void sniperWinsAuctionByBiddingHigher() throws XMPPException, InterruptedException {
        auction.start();
        application.startBiddingIn(auction);
        auction.hasReceivedJoinFromSinper(ApplicationRunner.SNIPER_XMPP_ID);

        auction.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding(1000, 1098);

        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
        auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
        application.hasShownSniperIsWinning(1098);

        auction.announceClosed();
        application.showsSniperHaWonAuction(1098);
    }

    @AfterEach
    public void tearDown() {
        auction.stop();
        application.stop();
    }

}
