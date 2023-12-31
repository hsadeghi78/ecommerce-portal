package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProductHistory getProductHistorySample1() {
        return new ProductHistory()
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
            .originalityClassId(1L)
            .categoryId(1L)
            .partyId(1L)
            .productId(1L)
            .priceId(1L)
            .campaignId(1L);
    }

    public static ProductHistory getProductHistorySample2() {
        return new ProductHistory()
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
            .originalityClassId(2L)
            .categoryId(2L)
            .partyId(2L)
            .productId(2L)
            .priceId(2L)
            .campaignId(2L);
    }

    public static ProductHistory getProductHistoryRandomSampleGenerator() {
        return new ProductHistory()
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
            .originalityClassId(longCount.incrementAndGet())
            .categoryId(longCount.incrementAndGet())
            .partyId(longCount.incrementAndGet())
            .productId(longCount.incrementAndGet())
            .priceId(longCount.incrementAndGet())
            .campaignId(longCount.incrementAndGet());
    }
}
