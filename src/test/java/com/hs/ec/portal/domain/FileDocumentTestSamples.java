package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FileDocumentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FileDocument getFileDocumentSample1() {
        return new FileDocument().id(1L).fileName("fileName1").filePath("filePath1").description("description1");
    }

    public static FileDocument getFileDocumentSample2() {
        return new FileDocument().id(2L).fileName("fileName2").filePath("filePath2").description("description2");
    }

    public static FileDocument getFileDocumentRandomSampleGenerator() {
        return new FileDocument()
            .id(longCount.incrementAndGet())
            .fileName(UUID.randomUUID().toString())
            .filePath(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
