package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Location getLocationSample1() {
        return new Location()
            .id(1L)
            .typeClassId(1L)
            .title("title1")
            .street1("street11")
            .street2("street21")
            .street3("street31")
            .buildingNo(1)
            .buildingName("buildingName1")
            .floor(1)
            .unit(1)
            .postalCode("postalCode1")
            .other("other1");
    }

    public static Location getLocationSample2() {
        return new Location()
            .id(2L)
            .typeClassId(2L)
            .title("title2")
            .street1("street12")
            .street2("street22")
            .street3("street32")
            .buildingNo(2)
            .buildingName("buildingName2")
            .floor(2)
            .unit(2)
            .postalCode("postalCode2")
            .other("other2");
    }

    public static Location getLocationRandomSampleGenerator() {
        return new Location()
            .id(longCount.incrementAndGet())
            .typeClassId(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .street1(UUID.randomUUID().toString())
            .street2(UUID.randomUUID().toString())
            .street3(UUID.randomUUID().toString())
            .buildingNo(intCount.incrementAndGet())
            .buildingName(UUID.randomUUID().toString())
            .floor(intCount.incrementAndGet())
            .unit(intCount.incrementAndGet())
            .postalCode(UUID.randomUUID().toString())
            .other(UUID.randomUUID().toString());
    }
}
