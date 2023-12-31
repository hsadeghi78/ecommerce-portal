package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FactorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Factor getFactorSample1() {
        return new Factor()
            .id(1L)
            .title("title1")
            .factorCode("factorCode1")
            .lastStatusClassId(1L)
            .paymentStateClassId(1L)
            .categoryClassId(1L)
            .discountCode("discountCode1")
            .description("description1");
    }

    public static Factor getFactorSample2() {
        return new Factor()
            .id(2L)
            .title("title2")
            .factorCode("factorCode2")
            .lastStatusClassId(2L)
            .paymentStateClassId(2L)
            .categoryClassId(2L)
            .discountCode("discountCode2")
            .description("description2");
    }

    public static Factor getFactorRandomSampleGenerator() {
        return new Factor()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .factorCode(UUID.randomUUID().toString())
            .lastStatusClassId(longCount.incrementAndGet())
            .paymentStateClassId(longCount.incrementAndGet())
            .categoryClassId(longCount.incrementAndGet())
            .discountCode(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
