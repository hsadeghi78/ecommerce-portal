package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.ProductItemTestSamples.*;
import static com.hs.ec.portal.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductItem.class);
        ProductItem productItem1 = getProductItemSample1();
        ProductItem productItem2 = new ProductItem();
        assertThat(productItem1).isNotEqualTo(productItem2);

        productItem2.setId(productItem1.getId());
        assertThat(productItem1).isEqualTo(productItem2);

        productItem2 = getProductItemSample2();
        assertThat(productItem1).isNotEqualTo(productItem2);
    }

    @Test
    void productTest() throws Exception {
        ProductItem productItem = getProductItemRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productItem.setProduct(productBack);
        assertThat(productItem.getProduct()).isEqualTo(productBack);

        productItem.product(null);
        assertThat(productItem.getProduct()).isNull();
    }
}
