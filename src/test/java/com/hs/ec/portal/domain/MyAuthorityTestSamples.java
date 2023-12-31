package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MyAuthorityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MyAuthority getMyAuthoritySample1() {
        return new MyAuthority().id(1L).name("name1").displayName("displayName1");
    }

    public static MyAuthority getMyAuthoritySample2() {
        return new MyAuthority().id(2L).name("name2").displayName("displayName2");
    }

    public static MyAuthority getMyAuthorityRandomSampleGenerator() {
        return new MyAuthority()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .displayName(UUID.randomUUID().toString());
    }
}
