package com.hs.ec.portal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassificationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassificationDTO.class);
        ClassificationDTO classificationDTO1 = new ClassificationDTO();
        classificationDTO1.setId(1L);
        ClassificationDTO classificationDTO2 = new ClassificationDTO();
        assertThat(classificationDTO1).isNotEqualTo(classificationDTO2);
        classificationDTO2.setId(classificationDTO1.getId());
        assertThat(classificationDTO1).isEqualTo(classificationDTO2);
        classificationDTO2.setId(2L);
        assertThat(classificationDTO1).isNotEqualTo(classificationDTO2);
        classificationDTO1.setId(null);
        assertThat(classificationDTO1).isNotEqualTo(classificationDTO2);
    }
}
