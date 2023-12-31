package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserCommentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserComment getUserCommentSample1() {
        return new UserComment().id(1L).description("description1");
    }

    public static UserComment getUserCommentSample2() {
        return new UserComment().id(2L).description("description2");
    }

    public static UserComment getUserCommentRandomSampleGenerator() {
        return new UserComment().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString());
    }
}
