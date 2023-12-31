package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContactTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Contact getContactSample1() {
        return new Contact().id(1L).contactValue("contactValue1").typeClassId(1L).prefix("prefix1").description("description1");
    }

    public static Contact getContactSample2() {
        return new Contact().id(2L).contactValue("contactValue2").typeClassId(2L).prefix("prefix2").description("description2");
    }

    public static Contact getContactRandomSampleGenerator() {
        return new Contact()
            .id(longCount.incrementAndGet())
            .contactValue(UUID.randomUUID().toString())
            .typeClassId(longCount.incrementAndGet())
            .prefix(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
