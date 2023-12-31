package com.hs.ec.portal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CriticismDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CriticismDTO.class);
        CriticismDTO criticismDTO1 = new CriticismDTO();
        criticismDTO1.setId(1L);
        CriticismDTO criticismDTO2 = new CriticismDTO();
        assertThat(criticismDTO1).isNotEqualTo(criticismDTO2);
        criticismDTO2.setId(criticismDTO1.getId());
        assertThat(criticismDTO1).isEqualTo(criticismDTO2);
        criticismDTO2.setId(2L);
        assertThat(criticismDTO1).isNotEqualTo(criticismDTO2);
        criticismDTO1.setId(null);
        assertThat(criticismDTO1).isNotEqualTo(criticismDTO2);
    }
}
