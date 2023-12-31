package com.hs.ec.portal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassTypeDTO.class);
        ClassTypeDTO classTypeDTO1 = new ClassTypeDTO();
        classTypeDTO1.setId(1L);
        ClassTypeDTO classTypeDTO2 = new ClassTypeDTO();
        assertThat(classTypeDTO1).isNotEqualTo(classTypeDTO2);
        classTypeDTO2.setId(classTypeDTO1.getId());
        assertThat(classTypeDTO1).isEqualTo(classTypeDTO2);
        classTypeDTO2.setId(2L);
        assertThat(classTypeDTO1).isNotEqualTo(classTypeDTO2);
        classTypeDTO1.setId(null);
        assertThat(classTypeDTO1).isNotEqualTo(classTypeDTO2);
    }
}
