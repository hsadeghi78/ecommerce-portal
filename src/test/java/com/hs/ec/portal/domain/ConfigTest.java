package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.ConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Config.class);
        Config config1 = getConfigSample1();
        Config config2 = new Config();
        assertThat(config1).isNotEqualTo(config2);

        config2.setId(config1.getId());
        assertThat(config1).isEqualTo(config2);

        config2 = getConfigSample2();
        assertThat(config1).isNotEqualTo(config2);
    }
}
