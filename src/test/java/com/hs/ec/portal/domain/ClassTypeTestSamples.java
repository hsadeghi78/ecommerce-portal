package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ClassTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ClassType getClassTypeSample1() {
        return new ClassType().id(1L).title("title1").typeCode(1).description("description1");
    }

    public static ClassType getClassTypeSample2() {
        return new ClassType().id(2L).title("title2").typeCode(2).description("description2");
    }

    public static ClassType getClassTypeRandomSampleGenerator() {
        return new ClassType()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .typeCode(intCount.incrementAndGet())
            .description(UUID.randomUUID().toString());
    }
}
