package com.better;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class SingleMessageListener implements MessageListener {

    private Consumer<Message> messageConsumer;

    public SingleMessageListener(Consumer<Message> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<>(1);

    @Override
    public void processMessage(Chat chat, Message message) {
        messages.add(message);
        messageConsumer.accept(message);
    }

    public void hadReceivedMessageMatching(Matcher<? super String> matcher) throws InterruptedException {
        Message message = getMessage();
        assertThat(message, is(notNullValue()));
        assertThat(message.getBody(), matcher);
    }

    private Message getMessage() throws InterruptedException {
        return messages.poll(5, TimeUnit.SECONDS);
    }
}
