package com.daniinc.chatapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ChatRoomTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ChatRoom getChatRoomSample1() {
        return new ChatRoom().id(1L);
    }

    public static ChatRoom getChatRoomSample2() {
        return new ChatRoom().id(2L);
    }

    public static ChatRoom getChatRoomRandomSampleGenerator() {
        return new ChatRoom().id(longCount.incrementAndGet());
    }
}
