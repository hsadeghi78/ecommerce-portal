package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.FactorItemTestSamples.*;
import static com.hs.ec.portal.domain.FactorTestSamples.*;
import static com.hs.ec.portal.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactorItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactorItem.class);
        FactorItem factorItem1 = getFactorItemSample1();
        FactorItem factorItem2 = new FactorItem();
        assertThat(factorItem1).isNotEqualTo(factorItem2);

        factorItem2.setId(factorItem1.getId());
        assertThat(factorItem1).isEqualTo(factorItem2);

        factorItem2 = getFactorItemSample2();
        assertThat(factorItem1).isNotEqualTo(factorItem2);
    }

    @Test
    void factorTest() throws Exception {
        FactorItem factorItem = getFactorItemRandomSampleGenerator();
        Factor factorBack = getFactorRandomSampleGenerator();

        factorItem.setFactor(factorBack);
        assertThat(factorItem.getFactor()).isEqualTo(factorBack);

        factorItem.factor(null);
        assertThat(factorItem.getFactor()).isNull();
    }

    @Test
    void productTest() throws Exception {
        FactorItem factorItem = getFactorItemRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        factorItem.setProduct(productBack);
        assertThat(factorItem.getProduct()).isEqualTo(productBack);

        factorItem.product(null);
        assertThat(factorItem.getProduct()).isNull();
    }
}
