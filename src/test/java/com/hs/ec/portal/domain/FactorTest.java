package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.FactorItemTestSamples.*;
import static com.hs.ec.portal.domain.FactorTestSamples.*;
import static com.hs.ec.portal.domain.LocationTestSamples.*;
import static com.hs.ec.portal.domain.PartyTestSamples.*;
import static com.hs.ec.portal.domain.UserCommentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FactorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Factor.class);
        Factor factor1 = getFactorSample1();
        Factor factor2 = new Factor();
        assertThat(factor1).isNotEqualTo(factor2);

        factor2.setId(factor1.getId());
        assertThat(factor1).isEqualTo(factor2);

        factor2 = getFactorSample2();
        assertThat(factor1).isNotEqualTo(factor2);
    }

    @Test
    void locationTest() throws Exception {
        Factor factor = getFactorRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        factor.setLocation(locationBack);
        assertThat(factor.getLocation()).isEqualTo(locationBack);

        factor.location(null);
        assertThat(factor.getLocation()).isNull();
    }

    @Test
    void factorItemsTest() throws Exception {
        Factor factor = getFactorRandomSampleGenerator();
        FactorItem factorItemBack = getFactorItemRandomSampleGenerator();

        factor.addFactorItems(factorItemBack);
        assertThat(factor.getFactorItems()).containsOnly(factorItemBack);
        assertThat(factorItemBack.getFactor()).isEqualTo(factor);

        factor.removeFactorItems(factorItemBack);
        assertThat(factor.getFactorItems()).doesNotContain(factorItemBack);
        assertThat(factorItemBack.getFactor()).isNull();

        factor.factorItems(new HashSet<>(Set.of(factorItemBack)));
        assertThat(factor.getFactorItems()).containsOnly(factorItemBack);
        assertThat(factorItemBack.getFactor()).isEqualTo(factor);

        factor.setFactorItems(new HashSet<>());
        assertThat(factor.getFactorItems()).doesNotContain(factorItemBack);
        assertThat(factorItemBack.getFactor()).isNull();
    }

    @Test
    void userCommentsTest() throws Exception {
        Factor factor = getFactorRandomSampleGenerator();
        UserComment userCommentBack = getUserCommentRandomSampleGenerator();

        factor.addUserComments(userCommentBack);
        assertThat(factor.getUserComments()).containsOnly(userCommentBack);
        assertThat(userCommentBack.getFactor()).isEqualTo(factor);

        factor.removeUserComments(userCommentBack);
        assertThat(factor.getUserComments()).doesNotContain(userCommentBack);
        assertThat(userCommentBack.getFactor()).isNull();

        factor.userComments(new HashSet<>(Set.of(userCommentBack)));
        assertThat(factor.getUserComments()).containsOnly(userCommentBack);
        assertThat(userCommentBack.getFactor()).isEqualTo(factor);

        factor.setUserComments(new HashSet<>());
        assertThat(factor.getUserComments()).doesNotContain(userCommentBack);
        assertThat(userCommentBack.getFactor()).isNull();
    }

    @Test
    void buyerPartyTest() throws Exception {
        Factor factor = getFactorRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        factor.setBuyerParty(partyBack);
        assertThat(factor.getBuyerParty()).isEqualTo(partyBack);

        factor.buyerParty(null);
        assertThat(factor.getBuyerParty()).isNull();
    }

    @Test
    void sellerPartyTest() throws Exception {
        Factor factor = getFactorRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        factor.setSellerParty(partyBack);
        assertThat(factor.getSellerParty()).isEqualTo(partyBack);

        factor.sellerParty(null);
        assertThat(factor.getSellerParty()).isNull();
    }
}
