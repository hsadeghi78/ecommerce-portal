package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ConsumeMaterialTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ConsumeMaterial getConsumeMaterialSample1() {
        return new ConsumeMaterial().id(1L).typeClassId(1L).name("name1").value("value1");
    }

    public static ConsumeMaterial getConsumeMaterialSample2() {
        return new ConsumeMaterial().id(2L).typeClassId(2L).name("name2").value("value2");
    }

    public static ConsumeMaterial getConsumeMaterialRandomSampleGenerator() {
        return new ConsumeMaterial()
            .id(longCount.incrementAndGet())
            .typeClassId(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .value(UUID.randomUUID().toString());
    }
}
