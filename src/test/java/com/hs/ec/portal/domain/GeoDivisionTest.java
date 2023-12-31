package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.GeoDivisionTestSamples.*;
import static com.hs.ec.portal.domain.GeoDivisionTestSamples.*;
import static com.hs.ec.portal.domain.LocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GeoDivisionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeoDivision.class);
        GeoDivision geoDivision1 = getGeoDivisionSample1();
        GeoDivision geoDivision2 = new GeoDivision();
        assertThat(geoDivision1).isNotEqualTo(geoDivision2);

        geoDivision2.setId(geoDivision1.getId());
        assertThat(geoDivision1).isEqualTo(geoDivision2);

        geoDivision2 = getGeoDivisionSample2();
        assertThat(geoDivision1).isNotEqualTo(geoDivision2);
    }

    @Test
    void childrenTest() throws Exception {
        GeoDivision geoDivision = getGeoDivisionRandomSampleGenerator();
        GeoDivision geoDivisionBack = getGeoDivisionRandomSampleGenerator();

        geoDivision.addChildren(geoDivisionBack);
        assertThat(geoDivision.getChildren()).containsOnly(geoDivisionBack);
        assertThat(geoDivisionBack.getParent()).isEqualTo(geoDivision);

        geoDivision.removeChildren(geoDivisionBack);
        assertThat(geoDivision.getChildren()).doesNotContain(geoDivisionBack);
        assertThat(geoDivisionBack.getParent()).isNull();

        geoDivision.children(new HashSet<>(Set.of(geoDivisionBack)));
        assertThat(geoDivision.getChildren()).containsOnly(geoDivisionBack);
        assertThat(geoDivisionBack.getParent()).isEqualTo(geoDivision);

        geoDivision.setChildren(new HashSet<>());
        assertThat(geoDivision.getChildren()).doesNotContain(geoDivisionBack);
        assertThat(geoDivisionBack.getParent()).isNull();
    }

    @Test
    void locationsTest() throws Exception {
        GeoDivision geoDivision = getGeoDivisionRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        geoDivision.addLocations(locationBack);
        assertThat(geoDivision.getLocations()).containsOnly(locationBack);
        assertThat(locationBack.getGeoDivision()).isEqualTo(geoDivision);

        geoDivision.removeLocations(locationBack);
        assertThat(geoDivision.getLocations()).doesNotContain(locationBack);
        assertThat(locationBack.getGeoDivision()).isNull();

        geoDivision.locations(new HashSet<>(Set.of(locationBack)));
        assertThat(geoDivision.getLocations()).containsOnly(locationBack);
        assertThat(locationBack.getGeoDivision()).isEqualTo(geoDivision);

        geoDivision.setLocations(new HashSet<>());
        assertThat(geoDivision.getLocations()).doesNotContain(locationBack);
        assertThat(locationBack.getGeoDivision()).isNull();
    }

    @Test
    void parentTest() throws Exception {
        GeoDivision geoDivision = getGeoDivisionRandomSampleGenerator();
        GeoDivision geoDivisionBack = getGeoDivisionRandomSampleGenerator();

        geoDivision.setParent(geoDivisionBack);
        assertThat(geoDivision.getParent()).isEqualTo(geoDivisionBack);

        geoDivision.parent(null);
        assertThat(geoDivision.getParent()).isNull();
    }
}
