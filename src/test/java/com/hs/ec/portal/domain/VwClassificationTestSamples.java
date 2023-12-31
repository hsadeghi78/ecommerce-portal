package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VwClassificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static VwClassification getVwClassificationSample1() {
        return new VwClassification()
            .id(1L)
            .title("title1")
            .classCode("classCode1")
            .description("description1")
            .languageClassId(1L)
            .typeTitle("typeTitle1")
            .typeCode(1)
            .typeDesc("typeDesc1");
    }

    public static VwClassification getVwClassificationSample2() {
        return new VwClassification()
            .id(2L)
            .title("title2")
            .classCode("classCode2")
            .description("description2")
            .languageClassId(2L)
            .typeTitle("typeTitle2")
            .typeCode(2)
            .typeDesc("typeDesc2");
    }

    public static VwClassification getVwClassificationRandomSampleGenerator() {
        return new VwClassification()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .classCode(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .languageClassId(longCount.incrementAndGet())
            .typeTitle(UUID.randomUUID().toString())
            .typeCode(intCount.incrementAndGet())
            .typeDesc(UUID.randomUUID().toString());
    }
}
