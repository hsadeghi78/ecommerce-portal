package com.hs.ec.portal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactorItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactorItemDTO.class);
        FactorItemDTO factorItemDTO1 = new FactorItemDTO();
        factorItemDTO1.setId(1L);
        FactorItemDTO factorItemDTO2 = new FactorItemDTO();
        assertThat(factorItemDTO1).isNotEqualTo(factorItemDTO2);
        factorItemDTO2.setId(factorItemDTO1.getId());
        assertThat(factorItemDTO1).isEqualTo(factorItemDTO2);
        factorItemDTO2.setId(2L);
        assertThat(factorItemDTO1).isNotEqualTo(factorItemDTO2);
        factorItemDTO1.setId(null);
        assertThat(factorItemDTO1).isNotEqualTo(factorItemDTO2);
    }
}
