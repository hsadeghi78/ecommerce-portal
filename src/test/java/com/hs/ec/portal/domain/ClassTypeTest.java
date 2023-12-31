package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.ClassTypeTestSamples.*;
import static com.hs.ec.portal.domain.ClassificationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClassTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassType.class);
        ClassType classType1 = getClassTypeSample1();
        ClassType classType2 = new ClassType();
        assertThat(classType1).isNotEqualTo(classType2);

        classType2.setId(classType1.getId());
        assertThat(classType1).isEqualTo(classType2);

        classType2 = getClassTypeSample2();
        assertThat(classType1).isNotEqualTo(classType2);
    }

    @Test
    void classificationsTest() throws Exception {
        ClassType classType = getClassTypeRandomSampleGenerator();
        Classification classificationBack = getClassificationRandomSampleGenerator();

        classType.addClassifications(classificationBack);
        assertThat(classType.getClassifications()).containsOnly(classificationBack);
        assertThat(classificationBack.getClassType()).isEqualTo(classType);

        classType.removeClassifications(classificationBack);
        assertThat(classType.getClassifications()).doesNotContain(classificationBack);
        assertThat(classificationBack.getClassType()).isNull();

        classType.classifications(new HashSet<>(Set.of(classificationBack)));
        assertThat(classType.getClassifications()).containsOnly(classificationBack);
        assertThat(classificationBack.getClassType()).isEqualTo(classType);

        classType.setClassifications(new HashSet<>());
        assertThat(classType.getClassifications()).doesNotContain(classificationBack);
        assertThat(classificationBack.getClassType()).isNull();
    }
}
