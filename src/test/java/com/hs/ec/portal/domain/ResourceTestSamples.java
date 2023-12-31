package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ResourceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Resource getResourceSample1() {
        return new Resource().id(1L).name("name1").displayName("displayName1").apiUri("apiUri1");
    }

    public static Resource getResourceSample2() {
        return new Resource().id(2L).name("name2").displayName("displayName2").apiUri("apiUri2");
    }

    public static Resource getResourceRandomSampleGenerator() {
        return new Resource()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .displayName(UUID.randomUUID().toString())
            .apiUri(UUID.randomUUID().toString());
    }
}
