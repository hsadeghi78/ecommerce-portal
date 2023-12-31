package com.hs.ec.portal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgreementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgreementDTO.class);
        AgreementDTO agreementDTO1 = new AgreementDTO();
        agreementDTO1.setId(1L);
        AgreementDTO agreementDTO2 = new AgreementDTO();
        assertThat(agreementDTO1).isNotEqualTo(agreementDTO2);
        agreementDTO2.setId(agreementDTO1.getId());
        assertThat(agreementDTO1).isEqualTo(agreementDTO2);
        agreementDTO2.setId(2L);
        assertThat(agreementDTO1).isNotEqualTo(agreementDTO2);
        agreementDTO1.setId(null);
        assertThat(agreementDTO1).isNotEqualTo(agreementDTO2);
    }
}
