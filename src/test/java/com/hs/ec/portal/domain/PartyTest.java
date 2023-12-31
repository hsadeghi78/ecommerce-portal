package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.AgreementTestSamples.*;
import static com.hs.ec.portal.domain.ContactTestSamples.*;
import static com.hs.ec.portal.domain.FactorTestSamples.*;
import static com.hs.ec.portal.domain.LocationTestSamples.*;
import static com.hs.ec.portal.domain.PartyTestSamples.*;
import static com.hs.ec.portal.domain.ProductTestSamples.*;
import static com.hs.ec.portal.domain.UserCommentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PartyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Party.class);
        Party party1 = getPartySample1();
        Party party2 = new Party();
        assertThat(party1).isNotEqualTo(party2);

        party2.setId(party1.getId());
        assertThat(party1).isEqualTo(party2);

        party2 = getPartySample2();
        assertThat(party1).isNotEqualTo(party2);
    }

    @Test
    void buyerFactorsTest() throws Exception {
        Party party = getPartyRandomSampleGenerator();
        Factor factorBack = getFactorRandomSampleGenerator();

        party.addBuyerFactors(factorBack);
        assertThat(party.getBuyerFactors()).containsOnly(factorBack);
        assertThat(factorBack.getBuyerParty()).isEqualTo(party);

        party.removeBuyerFactors(factorBack);
        assertThat(party.getBuyerFactors()).doesNotContain(factorBack);
        assertThat(factorBack.getBuyerParty()).isNull();

        party.buyerFactors(new HashSet<>(Set.of(factorBack)));
        assertThat(party.getBuyerFactors()).containsOnly(factorBack);
        assertThat(factorBack.getBuyerParty()).isEqualTo(party);

        party.setBuyerFactors(new HashSet<>());
        assertThat(party.getBuyerFactors()).doesNotContain(factorBack);
        assertThat(factorBack.getBuyerParty()).isNull();
    }

    @Test
    void sellerFactorsTest() throws Exception {
        Party party = getPartyRandomSampleGenerator();
        Factor factorBack = getFactorRandomSampleGenerator();

        party.addSellerFactors(factorBack);
        assertThat(party.getSellerFactors()).containsOnly(factorBack);
        assertThat(factorBack.getSellerParty()).isEqualTo(party);

        party.removeSellerFactors(factorBack);
        assertThat(party.getSellerFactors()).doesNotContain(factorBack);
        assertThat(factorBack.getSellerParty()).isNull();

        party.sellerFactors(new HashSet<>(Set.of(factorBack)));
        assertThat(party.getSellerFactors()).containsOnly(factorBack);
        assertThat(factorBack.getSellerParty()).isEqualTo(party);

        party.setSellerFactors(new HashSet<>());
        assertThat(party.getSellerFactors()).doesNotContain(factorBack);
        assertThat(factorBack.getSellerParty()).isNull();
    }

    @Test
    void userCommentsTest() throws Exception {
        Party party = getPartyRandomSampleGenerator();
        UserComment userCommentBack = getUserCommentRandomSampleGenerator();

        party.addUserComments(userCommentBack);
        assertThat(party.getUserComments()).containsOnly(userCommentBack);
        assertThat(userCommentBack.getParty()).isEqualTo(party);

        party.removeUserComments(userCommentBack);
        assertThat(party.getUserComments()).doesNotContain(userCommentBack);
        assertThat(userCommentBack.getParty()).isNull();

        party.userComments(new HashSet<>(Set.of(userCommentBack)));
        assertThat(party.getUserComments()).containsOnly(userCommentBack);
        assertThat(userCommentBack.getParty()).isEqualTo(party);

        party.setUserComments(new HashSet<>());
        assertThat(party.getUserComments()).doesNotContain(userCommentBack);
        assertThat(userCommentBack.getParty()).isNull();
    }

    @Test
    void productsTest() throws Exception {
        Party party = getPartyRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        party.addProducts(productBack);
        assertThat(party.getProducts()).containsOnly(productBack);
        assertThat(productBack.getParty()).isEqualTo(party);

        party.removeProducts(productBack);
        assertThat(party.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getParty()).isNull();

        party.products(new HashSet<>(Set.of(productBack)));
        assertThat(party.getProducts()).containsOnly(productBack);
        assertThat(productBack.getParty()).isEqualTo(party);

        party.setProducts(new HashSet<>());
        assertThat(party.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getParty()).isNull();
    }

    @Test
    void providerAgreementsTest() throws Exception {
        Party party = getPartyRandomSampleGenerator();
        Agreement agreementBack = getAgreementRandomSampleGenerator();

        party.addProviderAgreements(agreementBack);
        assertThat(party.getProviderAgreements()).containsOnly(agreementBack);
        assertThat(agreementBack.getProvider()).isEqualTo(party);

        party.removeProviderAgreements(agreementBack);
        assertThat(party.getProviderAgreements()).doesNotContain(agreementBack);
        assertThat(agreementBack.getProvider()).isNull();

        party.providerAgreements(new HashSet<>(Set.of(agreementBack)));
        assertThat(party.getProviderAgreements()).containsOnly(agreementBack);
        assertThat(agreementBack.getProvider()).isEqualTo(party);

        party.setProviderAgreements(new HashSet<>());
        assertThat(party.getProviderAgreements()).doesNotContain(agreementBack);
        assertThat(agreementBack.getProvider()).isNull();
    }

    @Test
    void consumerAgreementsTest() throws Exception {
        Party party = getPartyRandomSampleGenerator();
        Agreement agreementBack = getAgreementRandomSampleGenerator();

        party.addConsumerAgreements(agreementBack);
        assertThat(party.getConsumerAgreements()).containsOnly(agreementBack);
        assertThat(agreementBack.getConsumer()).isEqualTo(party);

        party.removeConsumerAgreements(agreementBack);
        assertThat(party.getConsumerAgreements()).doesNotContain(agreementBack);
        assertThat(agreementBack.getConsumer()).isNull();

        party.consumerAgreements(new HashSet<>(Set.of(agreementBack)));
        assertThat(party.getConsumerAgreements()).containsOnly(agreementBack);
        assertThat(agreementBack.getConsumer()).isEqualTo(party);

        party.setConsumerAgreements(new HashSet<>());
        assertThat(party.getConsumerAgreements()).doesNotContain(agreementBack);
        assertThat(agreementBack.getConsumer()).isNull();
    }

    @Test
    void contactsTest() throws Exception {
        Party party = getPartyRandomSampleGenerator();
        Contact contactBack = getContactRandomSampleGenerator();

        party.addContacts(contactBack);
        assertThat(party.getContacts()).containsOnly(contactBack);
        assertThat(contactBack.getParty()).isEqualTo(party);

        party.removeContacts(contactBack);
        assertThat(party.getContacts()).doesNotContain(contactBack);
        assertThat(contactBack.getParty()).isNull();

        party.contacts(new HashSet<>(Set.of(contactBack)));
        assertThat(party.getContacts()).containsOnly(contactBack);
        assertThat(contactBack.getParty()).isEqualTo(party);

        party.setContacts(new HashSet<>());
        assertThat(party.getContacts()).doesNotContain(contactBack);
        assertThat(contactBack.getParty()).isNull();
    }

    @Test
    void locationsTest() throws Exception {
        Party party = getPartyRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        party.addLocations(locationBack);
        assertThat(party.getLocations()).containsOnly(locationBack);
        assertThat(locationBack.getParty()).isEqualTo(party);

        party.removeLocations(locationBack);
        assertThat(party.getLocations()).doesNotContain(locationBack);
        assertThat(locationBack.getParty()).isNull();

        party.locations(new HashSet<>(Set.of(locationBack)));
        assertThat(party.getLocations()).containsOnly(locationBack);
        assertThat(locationBack.getParty()).isEqualTo(party);

        party.setLocations(new HashSet<>());
        assertThat(party.getLocations()).doesNotContain(locationBack);
        assertThat(locationBack.getParty()).isNull();
    }
}
