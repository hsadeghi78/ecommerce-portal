package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.FactorTestSamples.*;
import static com.hs.ec.portal.domain.GeoDivisionTestSamples.*;
import static com.hs.ec.portal.domain.LocationTestSamples.*;
import static com.hs.ec.portal.domain.PartyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Location.class);
        Location location1 = getLocationSample1();
        Location location2 = new Location();
        assertThat(location1).isNotEqualTo(location2);

        location2.setId(location1.getId());
        assertThat(location1).isEqualTo(location2);

        location2 = getLocationSample2();
        assertThat(location1).isNotEqualTo(location2);
    }

    @Test
    void factorTest() throws Exception {
        Location location = getLocationRandomSampleGenerator();
        Factor factorBack = getFactorRandomSampleGenerator();

        location.setFactor(factorBack);
        assertThat(location.getFactor()).isEqualTo(factorBack);
        assertThat(factorBack.getLocation()).isEqualTo(location);

        location.factor(null);
        assertThat(location.getFactor()).isNull();
        assertThat(factorBack.getLocation()).isNull();
    }

    @Test
    void geoDivisionTest() throws Exception {
        Location location = getLocationRandomSampleGenerator();
        GeoDivision geoDivisionBack = getGeoDivisionRandomSampleGenerator();

        location.setGeoDivision(geoDivisionBack);
        assertThat(location.getGeoDivision()).isEqualTo(geoDivisionBack);

        location.geoDivision(null);
        assertThat(location.getGeoDivision()).isNull();
    }

    @Test
    void partyTest() throws Exception {
        Location location = getLocationRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        location.setParty(partyBack);
        assertThat(location.getParty()).isEqualTo(partyBack);

        location.party(null);
        assertThat(location.getParty()).isNull();
    }
}
