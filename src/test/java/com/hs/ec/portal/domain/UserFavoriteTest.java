package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.ProductTestSamples.*;
import static com.hs.ec.portal.domain.UserFavoriteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserFavoriteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserFavorite.class);
        UserFavorite userFavorite1 = getUserFavoriteSample1();
        UserFavorite userFavorite2 = new UserFavorite();
        assertThat(userFavorite1).isNotEqualTo(userFavorite2);

        userFavorite2.setId(userFavorite1.getId());
        assertThat(userFavorite1).isEqualTo(userFavorite2);

        userFavorite2 = getUserFavoriteSample2();
        assertThat(userFavorite1).isNotEqualTo(userFavorite2);
    }

    @Test
    void productTest() throws Exception {
        UserFavorite userFavorite = getUserFavoriteRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        userFavorite.setProduct(productBack);
        assertThat(userFavorite.getProduct()).isEqualTo(productBack);

        userFavorite.product(null);
        assertThat(userFavorite.getProduct()).isNull();
    }
}
