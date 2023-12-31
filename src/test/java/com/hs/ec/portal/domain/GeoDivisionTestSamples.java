package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class GeoDivisionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static GeoDivision getGeoDivisionSample1() {
        return new GeoDivision().id(1L).name("name1").code(1L).level(1);
    }

    public static GeoDivision getGeoDivisionSample2() {
        return new GeoDivision().id(2L).name("name2").code(2L).level(2);
    }

    public static GeoDivision getGeoDivisionRandomSampleGenerator() {
        return new GeoDivision()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .code(longCount.incrementAndGet())
            .level(intCount.incrementAndGet());
    }
}
