package com.hs.ec.portal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VwClassificationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VwClassificationDTO.class);
        VwClassificationDTO vwClassificationDTO1 = new VwClassificationDTO();
        vwClassificationDTO1.setId(1L);
        VwClassificationDTO vwClassificationDTO2 = new VwClassificationDTO();
        assertThat(vwClassificationDTO1).isNotEqualTo(vwClassificationDTO2);
        vwClassificationDTO2.setId(vwClassificationDTO1.getId());
        assertThat(vwClassificationDTO1).isEqualTo(vwClassificationDTO2);
        vwClassificationDTO2.setId(2L);
        assertThat(vwClassificationDTO1).isNotEqualTo(vwClassificationDTO2);
        vwClassificationDTO1.setId(null);
        assertThat(vwClassificationDTO1).isNotEqualTo(vwClassificationDTO2);
    }
}
