package com.better;

import com.better.auctionsniper.Application;
import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;
import com.objogate.wl.swing.matcher.IterableComponentsMatcher;
import com.objogate.wl.swing.matcher.JLabelTextMatcher;
import org.hamcrest.Matchers;

import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.equalTo;


public class AuctionSniperDriver extends JFrameDriver {
    public AuctionSniperDriver(int milisecond) {
        super(new GesturePerformer(), JFrameDriver.topLevelFrame(named(Application.MAIN_WINDOW_NAME),
                showingOnScreen()), new AWTEventQueueProber(milisecond, 100));
    }

    public void showsSniperStatus(String status) {
        new JTableDriver(
                this).hasCell(withLabelText(equalTo(status)));
    }

    public void showsSniperStatus(String itemId, int lastPrice, int lastBid, String status) {
        new JTableDriver(this).hasRow(IterableComponentsMatcher.matching(withLabelText(itemId),
                withLabelText(valueOf(lastPrice)), withLabelText(valueOf(lastBid)), withLabelText(status)));
    }
}
