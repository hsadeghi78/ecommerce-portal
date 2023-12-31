package com.hs.ec.portal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactorDTO.class);
        FactorDTO factorDTO1 = new FactorDTO();
        factorDTO1.setId(1L);
        FactorDTO factorDTO2 = new FactorDTO();
        assertThat(factorDTO1).isNotEqualTo(factorDTO2);
        factorDTO2.setId(factorDTO1.getId());
        assertThat(factorDTO1).isEqualTo(factorDTO2);
        factorDTO2.setId(2L);
        assertThat(factorDTO1).isNotEqualTo(factorDTO2);
        factorDTO1.setId(null);
        assertThat(factorDTO1).isNotEqualTo(factorDTO2);
    }
}
