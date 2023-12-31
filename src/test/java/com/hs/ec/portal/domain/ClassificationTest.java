package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.ClassTypeTestSamples.*;
import static com.hs.ec.portal.domain.ClassificationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Classification.class);
        Classification classification1 = getClassificationSample1();
        Classification classification2 = new Classification();
        assertThat(classification1).isNotEqualTo(classification2);

        classification2.setId(classification1.getId());
        assertThat(classification1).isEqualTo(classification2);

        classification2 = getClassificationSample2();
        assertThat(classification1).isNotEqualTo(classification2);
    }

    @Test
    void classTypeTest() throws Exception {
        Classification classification = getClassificationRandomSampleGenerator();
        ClassType classTypeBack = getClassTypeRandomSampleGenerator();

        classification.setClassType(classTypeBack);
        assertThat(classification.getClassType()).isEqualTo(classTypeBack);

        classification.classType(null);
        assertThat(classification.getClassType()).isNull();
    }
}
