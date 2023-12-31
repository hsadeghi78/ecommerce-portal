package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.VwClassificationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VwClassificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VwClassification.class);
        VwClassification vwClassification1 = getVwClassificationSample1();
        VwClassification vwClassification2 = new VwClassification();
        assertThat(vwClassification1).isNotEqualTo(vwClassification2);

        vwClassification2.setId(vwClassification1.getId());
        assertThat(vwClassification1).isEqualTo(vwClassification2);

        vwClassification2 = getVwClassificationSample2();
        assertThat(vwClassification1).isNotEqualTo(vwClassification2);
    }
}
