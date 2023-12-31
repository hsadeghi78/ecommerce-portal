package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Category getCategorySample1() {
        return new Category().id(1L).title("title1").code("code1").level(1).keywords("keywords1").description("description1");
    }

    public static Category getCategorySample2() {
        return new Category().id(2L).title("title2").code("code2").level(2).keywords("keywords2").description("description2");
    }

    public static Category getCategoryRandomSampleGenerator() {
        return new Category()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .level(intCount.incrementAndGet())
            .keywords(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
