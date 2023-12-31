package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.ConsumeMaterialTestSamples.*;
import static com.hs.ec.portal.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsumeMaterialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsumeMaterial.class);
        ConsumeMaterial consumeMaterial1 = getConsumeMaterialSample1();
        ConsumeMaterial consumeMaterial2 = new ConsumeMaterial();
        assertThat(consumeMaterial1).isNotEqualTo(consumeMaterial2);

        consumeMaterial2.setId(consumeMaterial1.getId());
        assertThat(consumeMaterial1).isEqualTo(consumeMaterial2);

        consumeMaterial2 = getConsumeMaterialSample2();
        assertThat(consumeMaterial1).isNotEqualTo(consumeMaterial2);
    }

    @Test
    void productTest() throws Exception {
        ConsumeMaterial consumeMaterial = getConsumeMaterialRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        consumeMaterial.setProduct(productBack);
        assertThat(consumeMaterial.getProduct()).isEqualTo(productBack);

        consumeMaterial.product(null);
        assertThat(consumeMaterial.getProduct()).isNull();
    }
}
