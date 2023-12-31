package com.hs.ec.portal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GeoDivisionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeoDivisionDTO.class);
        GeoDivisionDTO geoDivisionDTO1 = new GeoDivisionDTO();
        geoDivisionDTO1.setId(1L);
        GeoDivisionDTO geoDivisionDTO2 = new GeoDivisionDTO();
        assertThat(geoDivisionDTO1).isNotEqualTo(geoDivisionDTO2);
        geoDivisionDTO2.setId(geoDivisionDTO1.getId());
        assertThat(geoDivisionDTO1).isEqualTo(geoDivisionDTO2);
        geoDivisionDTO2.setId(2L);
        assertThat(geoDivisionDTO1).isNotEqualTo(geoDivisionDTO2);
        geoDivisionDTO1.setId(null);
        assertThat(geoDivisionDTO1).isNotEqualTo(geoDivisionDTO2);
    }
}
