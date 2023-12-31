package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProductItem getProductItemSample1() {
        return new ProductItem().id(1L).typeClassId(1L).name("name1").value("value1");
    }

    public static ProductItem getProductItemSample2() {
        return new ProductItem().id(2L).typeClassId(2L).name("name2").value("value2");
    }

    public static ProductItem getProductItemRandomSampleGenerator() {
        return new ProductItem()
            .id(longCount.incrementAndGet())
            .typeClassId(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .value(UUID.randomUUID().toString());
    }
}
