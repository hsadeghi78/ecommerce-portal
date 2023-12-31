package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CriticismTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Criticism getCriticismSample1() {
        return new Criticism().id(1L).fullName("fullName1").email("email1").contactNumber("contactNumber1").description("description1");
    }

    public static Criticism getCriticismSample2() {
        return new Criticism().id(2L).fullName("fullName2").email("email2").contactNumber("contactNumber2").description("description2");
    }

    public static Criticism getCriticismRandomSampleGenerator() {
        return new Criticism()
            .id(longCount.incrementAndGet())
            .fullName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .contactNumber(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
