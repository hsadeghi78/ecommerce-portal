package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.ResourceAuthorityTestSamples.*;
import static com.hs.ec.portal.domain.ResourceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ResourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resource.class);
        Resource resource1 = getResourceSample1();
        Resource resource2 = new Resource();
        assertThat(resource1).isNotEqualTo(resource2);

        resource2.setId(resource1.getId());
        assertThat(resource1).isEqualTo(resource2);

        resource2 = getResourceSample2();
        assertThat(resource1).isNotEqualTo(resource2);
    }

    @Test
    void resourceAuthoritiesTest() throws Exception {
        Resource resource = getResourceRandomSampleGenerator();
        ResourceAuthority resourceAuthorityBack = getResourceAuthorityRandomSampleGenerator();

        resource.addResourceAuthorities(resourceAuthorityBack);
        assertThat(resource.getResourceAuthorities()).containsOnly(resourceAuthorityBack);
        assertThat(resourceAuthorityBack.getResource()).isEqualTo(resource);

        resource.removeResourceAuthorities(resourceAuthorityBack);
        assertThat(resource.getResourceAuthorities()).doesNotContain(resourceAuthorityBack);
        assertThat(resourceAuthorityBack.getResource()).isNull();

        resource.resourceAuthorities(new HashSet<>(Set.of(resourceAuthorityBack)));
        assertThat(resource.getResourceAuthorities()).containsOnly(resourceAuthorityBack);
        assertThat(resourceAuthorityBack.getResource()).isEqualTo(resource);

        resource.setResourceAuthorities(new HashSet<>());
        assertThat(resource.getResourceAuthorities()).doesNotContain(resourceAuthorityBack);
        assertThat(resourceAuthorityBack.getResource()).isNull();
    }
}
