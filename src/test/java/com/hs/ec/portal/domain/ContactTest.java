package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.ContactTestSamples.*;
import static com.hs.ec.portal.domain.PartyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contact.class);
        Contact contact1 = getContactSample1();
        Contact contact2 = new Contact();
        assertThat(contact1).isNotEqualTo(contact2);

        contact2.setId(contact1.getId());
        assertThat(contact1).isEqualTo(contact2);

        contact2 = getContactSample2();
        assertThat(contact1).isNotEqualTo(contact2);
    }

    @Test
    void partyTest() throws Exception {
        Contact contact = getContactRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        contact.setParty(partyBack);
        assertThat(contact.getParty()).isEqualTo(partyBack);

        contact.party(null);
        assertThat(contact.getParty()).isNull();
    }
}
