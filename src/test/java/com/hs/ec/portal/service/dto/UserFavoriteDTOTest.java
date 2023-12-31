package com.hs.ec.portal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserFavoriteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserFavoriteDTO.class);
        UserFavoriteDTO userFavoriteDTO1 = new UserFavoriteDTO();
        userFavoriteDTO1.setId(1L);
        UserFavoriteDTO userFavoriteDTO2 = new UserFavoriteDTO();
        assertThat(userFavoriteDTO1).isNotEqualTo(userFavoriteDTO2);
        userFavoriteDTO2.setId(userFavoriteDTO1.getId());
        assertThat(userFavoriteDTO1).isEqualTo(userFavoriteDTO2);
        userFavoriteDTO2.setId(2L);
        assertThat(userFavoriteDTO1).isNotEqualTo(userFavoriteDTO2);
        userFavoriteDTO1.setId(null);
        assertThat(userFavoriteDTO1).isNotEqualTo(userFavoriteDTO2);
    }
}
