package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.FactorTestSamples.*;
import static com.hs.ec.portal.domain.PartyTestSamples.*;
import static com.hs.ec.portal.domain.ProductTestSamples.*;
import static com.hs.ec.portal.domain.UserCommentTestSamples.*;
import static com.hs.ec.portal.domain.UserCommentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserCommentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserComment.class);
        UserComment userComment1 = getUserCommentSample1();
        UserComment userComment2 = new UserComment();
        assertThat(userComment1).isNotEqualTo(userComment2);

        userComment2.setId(userComment1.getId());
        assertThat(userComment1).isEqualTo(userComment2);

        userComment2 = getUserCommentSample2();
        assertThat(userComment1).isNotEqualTo(userComment2);
    }

    @Test
    void childrenTest() throws Exception {
        UserComment userComment = getUserCommentRandomSampleGenerator();
        UserComment userCommentBack = getUserCommentRandomSampleGenerator();

        userComment.addChildren(userCommentBack);
        assertThat(userComment.getChildren()).containsOnly(userCommentBack);
        assertThat(userCommentBack.getParent()).isEqualTo(userComment);

        userComment.removeChildren(userCommentBack);
        assertThat(userComment.getChildren()).doesNotContain(userCommentBack);
        assertThat(userCommentBack.getParent()).isNull();

        userComment.children(new HashSet<>(Set.of(userCommentBack)));
        assertThat(userComment.getChildren()).containsOnly(userCommentBack);
        assertThat(userCommentBack.getParent()).isEqualTo(userComment);

        userComment.setChildren(new HashSet<>());
        assertThat(userComment.getChildren()).doesNotContain(userCommentBack);
        assertThat(userCommentBack.getParent()).isNull();
    }

    @Test
    void partyTest() throws Exception {
        UserComment userComment = getUserCommentRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        userComment.setParty(partyBack);
        assertThat(userComment.getParty()).isEqualTo(partyBack);

        userComment.party(null);
        assertThat(userComment.getParty()).isNull();
    }

    @Test
    void productTest() throws Exception {
        UserComment userComment = getUserCommentRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        userComment.setProduct(productBack);
        assertThat(userComment.getProduct()).isEqualTo(productBack);

        userComment.product(null);
        assertThat(userComment.getProduct()).isNull();
    }

    @Test
    void factorTest() throws Exception {
        UserComment userComment = getUserCommentRandomSampleGenerator();
        Factor factorBack = getFactorRandomSampleGenerator();

        userComment.setFactor(factorBack);
        assertThat(userComment.getFactor()).isEqualTo(factorBack);

        userComment.factor(null);
        assertThat(userComment.getFactor()).isNull();
    }

    @Test
    void parentTest() throws Exception {
        UserComment userComment = getUserCommentRandomSampleGenerator();
        UserComment userCommentBack = getUserCommentRandomSampleGenerator();

        userComment.setParent(userCommentBack);
        assertThat(userComment.getParent()).isEqualTo(userCommentBack);

        userComment.parent(null);
        assertThat(userComment.getParent()).isNull();
    }
}
