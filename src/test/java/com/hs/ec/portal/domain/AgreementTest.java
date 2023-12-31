package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.AgreementTestSamples.*;
import static com.hs.ec.portal.domain.PartyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgreementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agreement.class);
        Agreement agreement1 = getAgreementSample1();
        Agreement agreement2 = new Agreement();
        assertThat(agreement1).isNotEqualTo(agreement2);

        agreement2.setId(agreement1.getId());
        assertThat(agreement1).isEqualTo(agreement2);

        agreement2 = getAgreementSample2();
        assertThat(agreement1).isNotEqualTo(agreement2);
    }

    @Test
    void providerTest() throws Exception {
        Agreement agreement = getAgreementRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        agreement.setProvider(partyBack);
        assertThat(agreement.getProvider()).isEqualTo(partyBack);

        agreement.provider(null);
        assertThat(agreement.getProvider()).isNull();
    }

    @Test
    void consumerTest() throws Exception {
        Agreement agreement = getAgreementRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        agreement.setConsumer(partyBack);
        assertThat(agreement.getConsumer()).isEqualTo(partyBack);

        agreement.consumer(null);
        assertThat(agreement.getConsumer()).isNull();
    }
}
