package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.CampaignTestSamples.*;
import static com.hs.ec.portal.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CampaignTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Campaign.class);
        Campaign campaign1 = getCampaignSample1();
        Campaign campaign2 = new Campaign();
        assertThat(campaign1).isNotEqualTo(campaign2);

        campaign2.setId(campaign1.getId());
        assertThat(campaign1).isEqualTo(campaign2);

        campaign2 = getCampaignSample2();
        assertThat(campaign1).isNotEqualTo(campaign2);
    }

    @Test
    void productsTest() throws Exception {
        Campaign campaign = getCampaignRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        campaign.addProducts(productBack);
        assertThat(campaign.getProducts()).containsOnly(productBack);

        campaign.removeProducts(productBack);
        assertThat(campaign.getProducts()).doesNotContain(productBack);

        campaign.products(new HashSet<>(Set.of(productBack)));
        assertThat(campaign.getProducts()).containsOnly(productBack);

        campaign.setProducts(new HashSet<>());
        assertThat(campaign.getProducts()).doesNotContain(productBack);
    }
}
