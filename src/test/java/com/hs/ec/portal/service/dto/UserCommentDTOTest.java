package com.hs.ec.portal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserCommentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserCommentDTO.class);
        UserCommentDTO userCommentDTO1 = new UserCommentDTO();
        userCommentDTO1.setId(1L);
        UserCommentDTO userCommentDTO2 = new UserCommentDTO();
        assertThat(userCommentDTO1).isNotEqualTo(userCommentDTO2);
        userCommentDTO2.setId(userCommentDTO1.getId());
        assertThat(userCommentDTO1).isEqualTo(userCommentDTO2);
        userCommentDTO2.setId(2L);
        assertThat(userCommentDTO1).isNotEqualTo(userCommentDTO2);
        userCommentDTO1.setId(null);
        assertThat(userCommentDTO1).isNotEqualTo(userCommentDTO2);
    }
}
