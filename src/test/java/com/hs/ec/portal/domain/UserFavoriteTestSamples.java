package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class UserFavoriteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserFavorite getUserFavoriteSample1() {
        return new UserFavorite().id(1L);
    }

    public static UserFavorite getUserFavoriteSample2() {
        return new UserFavorite().id(2L);
    }

    public static UserFavorite getUserFavoriteRandomSampleGenerator() {
        return new UserFavorite().id(longCount.incrementAndGet());
    }
}
