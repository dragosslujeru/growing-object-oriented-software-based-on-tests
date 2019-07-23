package test.com.better.auctionsniper;

import com.better.auctionsniper.Auction;
import com.better.auctionsniper.AuctionSniper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.better.auctionsniper.AuctionEventListener.PriceSource.FromOtherBidder;
import static com.better.auctionsniper.AuctionEventListener.PriceSource.FromSniper;

@ExtendWith(MockitoExtension.class)
public class AuctionSniperTest {

    private final SniperListener sniperListener = Mockito.mock(SniperListener.class);
    private final Auction auction = Mockito.mock(Auction.class);

    private final AuctionSniper sniper = new AuctionSniper(auction, sniperListener);

    @Test
    public void reportsLostWhenAuctionClosesImmediately() {
        sniper.auctionClosed();
        Mockito.verify(sniperListener, Mockito.times(1)).sniperLost();
    }

    @Test
    public void reportsLostWhenAuctionClosesWhileBidding() {
        sniper.currentPrice(755, 11, FromOtherBidder);
        sniper.auctionClosed();
        Mockito.verify(sniperListener, Mockito.atLeastOnce()).sniperLost();
    }

    @Test
    public void reportsWinningWhenCurrentPriceComesFromSniper() {
        sniper.currentPrice(125, 4, FromSniper);
        Mockito.verify(sniperListener, Mockito.atLeastOnce()).sniperWinning();
    }

    @Test
    public void reportsWonWhenAuctionClosedWhileWinning() {
        sniper.currentPrice(1025, 10, FromSniper);
        Mockito.verify(sniperListener, Mockito.atLeastOnce()).sniperWinning();
        sniper.auctionClosed();
        Mockito.verify(sniperListener, Mockito.times(1)).sniperWon();
    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        int price = 1001;
        int increment = 25;
        sniper.currentPrice(price, increment, FromOtherBidder);
        Mockito.verify(sniperListener, Mockito.atLeastOnce()).sniperBidding();
        Mockito.verify(auction, Mockito.times(1)).bid(price + increment);
    }
}
