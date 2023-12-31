package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Product getProductSample1() {
        return new Product()
            .id(1L)
            .name("name1")
            .typeClassId(1L)
            .brandClassId(1L)
            .sizee("sizee1")
            .regularSizeClassId(1L)
            .languageClassId(1L)
            .description("description1")
            .keywords("keywords1")
            .currencyClassId(1L)
            .warrantyClassId(1L)
            .deliveryPlaceClassId(1L)
            .paymentPlaceClassId(1L)
            .originalityClassId(1L);
    }

    public static Product getProductSample2() {
        return new Product()
            .id(2L)
            .name("name2")
            .typeClassId(2L)
            .brandClassId(2L)
            .sizee("sizee2")
            .regularSizeClassId(2L)
            .languageClassId(2L)
            .description("description2")
            .keywords("keywords2")
            .currencyClassId(2L)
            .warrantyClassId(2L)
            .deliveryPlaceClassId(2L)
            .paymentPlaceClassId(2L)
            .originalityClassId(2L);
    }

    public static Product getProductRandomSampleGenerator() {
        return new Product()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .typeClassId(longCount.incrementAndGet())
            .brandClassId(longCount.incrementAndGet())
            .sizee(UUID.randomUUID().toString())
            .regularSizeClassId(longCount.incrementAndGet())
            .languageClassId(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .keywords(UUID.randomUUID().toString())
            .currencyClassId(longCount.incrementAndGet())
            .warrantyClassId(longCount.incrementAndGet())
            .deliveryPlaceClassId(longCount.incrementAndGet())
            .paymentPlaceClassId(longCount.incrementAndGet())
            .originalityClassId(longCount.incrementAndGet());
    }
}
