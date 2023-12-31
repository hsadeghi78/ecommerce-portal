package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.CriticismTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CriticismTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Criticism.class);
        Criticism criticism1 = getCriticismSample1();
        Criticism criticism2 = new Criticism();
        assertThat(criticism1).isNotEqualTo(criticism2);

        criticism2.setId(criticism1.getId());
        assertThat(criticism1).isEqualTo(criticism2);

        criticism2 = getCriticismSample2();
        assertThat(criticism1).isNotEqualTo(criticism2);
    }
}
