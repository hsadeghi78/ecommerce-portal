package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClassificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Classification getClassificationSample1() {
        return new Classification().id(1L).title("title1").classCode("classCode1").description("description1").languageClassId(1L);
    }

    public static Classification getClassificationSample2() {
        return new Classification().id(2L).title("title2").classCode("classCode2").description("description2").languageClassId(2L);
    }

    public static Classification getClassificationRandomSampleGenerator() {
        return new Classification()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .classCode(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .languageClassId(longCount.incrementAndGet());
    }
}
