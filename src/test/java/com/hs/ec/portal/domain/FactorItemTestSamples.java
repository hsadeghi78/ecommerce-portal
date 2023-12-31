package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FactorItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static FactorItem getFactorItemSample1() {
        return new FactorItem().id(1L).rowNum(1).title("title1").count(1).description("description1");
    }

    public static FactorItem getFactorItemSample2() {
        return new FactorItem().id(2L).rowNum(2).title("title2").count(2).description("description2");
    }

    public static FactorItem getFactorItemRandomSampleGenerator() {
        return new FactorItem()
            .id(longCount.incrementAndGet())
            .rowNum(intCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .count(intCount.incrementAndGet())
            .description(UUID.randomUUID().toString());
    }
}
