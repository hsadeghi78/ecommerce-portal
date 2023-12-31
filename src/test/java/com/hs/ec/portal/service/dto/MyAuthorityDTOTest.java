package com.hs.ec.portal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MyAuthorityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyAuthorityDTO.class);
        MyAuthorityDTO myAuthorityDTO1 = new MyAuthorityDTO();
        myAuthorityDTO1.setId(1L);
        MyAuthorityDTO myAuthorityDTO2 = new MyAuthorityDTO();
        assertThat(myAuthorityDTO1).isNotEqualTo(myAuthorityDTO2);
        myAuthorityDTO2.setId(myAuthorityDTO1.getId());
        assertThat(myAuthorityDTO1).isEqualTo(myAuthorityDTO2);
        myAuthorityDTO2.setId(2L);
        assertThat(myAuthorityDTO1).isNotEqualTo(myAuthorityDTO2);
        myAuthorityDTO1.setId(null);
        assertThat(myAuthorityDTO1).isNotEqualTo(myAuthorityDTO2);
    }
}
