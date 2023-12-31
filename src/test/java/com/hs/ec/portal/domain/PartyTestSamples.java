package com.hs.ec.portal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PartyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Party getPartySample1() {
        return new Party().id(1L).title("title1").partyCode("partyCode1").tradeTitle("tradeTitle1");
    }

    public static Party getPartySample2() {
        return new Party().id(2L).title("title2").partyCode("partyCode2").tradeTitle("tradeTitle2");
    }

    public static Party getPartyRandomSampleGenerator() {
        return new Party()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .partyCode(UUID.randomUUID().toString())
            .tradeTitle(UUID.randomUUID().toString());
    }
}
