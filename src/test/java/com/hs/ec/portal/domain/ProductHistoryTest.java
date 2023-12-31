package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.ProductHistoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductHistory.class);
        ProductHistory productHistory1 = getProductHistorySample1();
        ProductHistory productHistory2 = new ProductHistory();
        assertThat(productHistory1).isNotEqualTo(productHistory2);

        productHistory2.setId(productHistory1.getId());
        assertThat(productHistory1).isEqualTo(productHistory2);

        productHistory2 = getProductHistorySample2();
        assertThat(productHistory1).isNotEqualTo(productHistory2);
    }
}
