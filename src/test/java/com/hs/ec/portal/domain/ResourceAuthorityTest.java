package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.MyAuthorityTestSamples.*;
import static com.hs.ec.portal.domain.ResourceAuthorityTestSamples.*;
import static com.hs.ec.portal.domain.ResourceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResourceAuthorityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceAuthority.class);
        ResourceAuthority resourceAuthority1 = getResourceAuthoritySample1();
        ResourceAuthority resourceAuthority2 = new ResourceAuthority();
        assertThat(resourceAuthority1).isNotEqualTo(resourceAuthority2);

        resourceAuthority2.setId(resourceAuthority1.getId());
        assertThat(resourceAuthority1).isEqualTo(resourceAuthority2);

        resourceAuthority2 = getResourceAuthoritySample2();
        assertThat(resourceAuthority1).isNotEqualTo(resourceAuthority2);
    }

    @Test
    void resourceTest() throws Exception {
        ResourceAuthority resourceAuthority = getResourceAuthorityRandomSampleGenerator();
        Resource resourceBack = getResourceRandomSampleGenerator();

        resourceAuthority.setResource(resourceBack);
        assertThat(resourceAuthority.getResource()).isEqualTo(resourceBack);

        resourceAuthority.resource(null);
        assertThat(resourceAuthority.getResource()).isNull();
    }

    @Test
    void myAuthorityTest() throws Exception {
        ResourceAuthority resourceAuthority = getResourceAuthorityRandomSampleGenerator();
        MyAuthority myAuthorityBack = getMyAuthorityRandomSampleGenerator();

        resourceAuthority.setMyAuthority(myAuthorityBack);
        assertThat(resourceAuthority.getMyAuthority()).isEqualTo(myAuthorityBack);

        resourceAuthority.myAuthority(null);
        assertThat(resourceAuthority.getMyAuthority()).isNull();
    }
}
