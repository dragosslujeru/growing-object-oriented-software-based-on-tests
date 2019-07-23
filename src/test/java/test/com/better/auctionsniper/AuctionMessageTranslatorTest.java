package test.com.better.auctionsniper;

import com.better.auctionsniper.AuctionEventListener;
import com.better.auctionsniper.AuctionMessageTranslator;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.better.auctionsniper.AuctionEventListener.PriceSource.FromOtherBidder;
import static com.better.auctionsniper.AuctionEventListener.PriceSource.FromSniper;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuctionMessageTranslatorTest {


    private AuctionEventListener listener = Mockito.mock(AuctionEventListener.class);

    private final AuctionMessageTranslator translator = new AuctionMessageTranslator(listener, SNIPER_ID);

    private static final Chat UNUSED_CHAT = null;

    private static final String SNIPER_ID = "sniper";

    @Test
    public void notifiesAuctionClosedWhenCloseMessageReceived() {
        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: CLOSE;");
        translator.processMessage(UNUSED_CHAT, message);
        verify(listener, times(1)).auctionClosed();
    }

    @Test
    public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder() {
        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;");
        translator.processMessage(UNUSED_CHAT, message);
        verify(listener, times(1)).currentPrice(192, 7, FromOtherBidder);
    }

    @Test
    public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromSniper() {
        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: " + SNIPER_ID + ";");
        translator.processMessage(UNUSED_CHAT, message);
        verify(listener, times(1)).currentPrice(192, 7, FromSniper);
    }
}
