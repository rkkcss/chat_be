package com.daniinc.chatapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MessageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Message getMessageSample1() {
        return new Message().id(1L).text("text1").mediaUrl("mediaUrl1");
    }

    public static Message getMessageSample2() {
        return new Message().id(2L).text("text2").mediaUrl("mediaUrl2");
    }

    public static Message getMessageRandomSampleGenerator() {
        return new Message().id(longCount.incrementAndGet()).text(UUID.randomUUID().toString()).mediaUrl(UUID.randomUUID().toString());
    }
}
