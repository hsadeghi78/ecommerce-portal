package com.hs.ec.portal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductItemDTO.class);
        ProductItemDTO productItemDTO1 = new ProductItemDTO();
        productItemDTO1.setId(1L);
        ProductItemDTO productItemDTO2 = new ProductItemDTO();
        assertThat(productItemDTO1).isNotEqualTo(productItemDTO2);
        productItemDTO2.setId(productItemDTO1.getId());
        assertThat(productItemDTO1).isEqualTo(productItemDTO2);
        productItemDTO2.setId(2L);
        assertThat(productItemDTO1).isNotEqualTo(productItemDTO2);
        productItemDTO1.setId(null);
        assertThat(productItemDTO1).isNotEqualTo(productItemDTO2);
    }
}
