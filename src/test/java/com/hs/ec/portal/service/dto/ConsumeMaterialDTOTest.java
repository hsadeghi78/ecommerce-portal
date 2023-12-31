package com.hs.ec.portal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsumeMaterialDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsumeMaterialDTO.class);
        ConsumeMaterialDTO consumeMaterialDTO1 = new ConsumeMaterialDTO();
        consumeMaterialDTO1.setId(1L);
        ConsumeMaterialDTO consumeMaterialDTO2 = new ConsumeMaterialDTO();
        assertThat(consumeMaterialDTO1).isNotEqualTo(consumeMaterialDTO2);
        consumeMaterialDTO2.setId(consumeMaterialDTO1.getId());
        assertThat(consumeMaterialDTO1).isEqualTo(consumeMaterialDTO2);
        consumeMaterialDTO2.setId(2L);
        assertThat(consumeMaterialDTO1).isNotEqualTo(consumeMaterialDTO2);
        consumeMaterialDTO1.setId(null);
        assertThat(consumeMaterialDTO1).isNotEqualTo(consumeMaterialDTO2);
    }
}
