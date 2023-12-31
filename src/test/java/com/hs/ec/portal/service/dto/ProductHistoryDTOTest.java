package com.hs.ec.portal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductHistoryDTO.class);
        ProductHistoryDTO productHistoryDTO1 = new ProductHistoryDTO();
        productHistoryDTO1.setId(1L);
        ProductHistoryDTO productHistoryDTO2 = new ProductHistoryDTO();
        assertThat(productHistoryDTO1).isNotEqualTo(productHistoryDTO2);
        productHistoryDTO2.setId(productHistoryDTO1.getId());
        assertThat(productHistoryDTO1).isEqualTo(productHistoryDTO2);
        productHistoryDTO2.setId(2L);
        assertThat(productHistoryDTO1).isNotEqualTo(productHistoryDTO2);
        productHistoryDTO1.setId(null);
        assertThat(productHistoryDTO1).isNotEqualTo(productHistoryDTO2);
    }
}
