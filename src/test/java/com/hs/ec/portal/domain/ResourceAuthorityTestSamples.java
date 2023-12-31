package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ResourceAuthorityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ResourceAuthority getResourceAuthoritySample1() {
        return new ResourceAuthority().id(1L);
    }

    public static ResourceAuthority getResourceAuthoritySample2() {
        return new ResourceAuthority().id(2L);
    }

    public static ResourceAuthority getResourceAuthorityRandomSampleGenerator() {
        return new ResourceAuthority().id(longCount.incrementAndGet());
    }
}
