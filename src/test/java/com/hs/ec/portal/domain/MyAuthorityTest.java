package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.MyAuthorityTestSamples.*;
import static com.hs.ec.portal.domain.MyAuthorityTestSamples.*;
import static com.hs.ec.portal.domain.ResourceAuthorityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MyAuthorityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyAuthority.class);
        MyAuthority myAuthority1 = getMyAuthoritySample1();
        MyAuthority myAuthority2 = new MyAuthority();
        assertThat(myAuthority1).isNotEqualTo(myAuthority2);

        myAuthority2.setId(myAuthority1.getId());
        assertThat(myAuthority1).isEqualTo(myAuthority2);

        myAuthority2 = getMyAuthoritySample2();
        assertThat(myAuthority1).isNotEqualTo(myAuthority2);
    }

    @Test
    void childrenTest() throws Exception {
        MyAuthority myAuthority = getMyAuthorityRandomSampleGenerator();
        MyAuthority myAuthorityBack = getMyAuthorityRandomSampleGenerator();

        myAuthority.addChildren(myAuthorityBack);
        assertThat(myAuthority.getChildren()).containsOnly(myAuthorityBack);
        assertThat(myAuthorityBack.getParent()).isEqualTo(myAuthority);

        myAuthority.removeChildren(myAuthorityBack);
        assertThat(myAuthority.getChildren()).doesNotContain(myAuthorityBack);
        assertThat(myAuthorityBack.getParent()).isNull();

        myAuthority.children(new HashSet<>(Set.of(myAuthorityBack)));
        assertThat(myAuthority.getChildren()).containsOnly(myAuthorityBack);
        assertThat(myAuthorityBack.getParent()).isEqualTo(myAuthority);

        myAuthority.setChildren(new HashSet<>());
        assertThat(myAuthority.getChildren()).doesNotContain(myAuthorityBack);
        assertThat(myAuthorityBack.getParent()).isNull();
    }

    @Test
    void resourceAuthoritiesTest() throws Exception {
        MyAuthority myAuthority = getMyAuthorityRandomSampleGenerator();
        ResourceAuthority resourceAuthorityBack = getResourceAuthorityRandomSampleGenerator();

        myAuthority.addResourceAuthorities(resourceAuthorityBack);
        assertThat(myAuthority.getResourceAuthorities()).containsOnly(resourceAuthorityBack);
        assertThat(resourceAuthorityBack.getMyAuthority()).isEqualTo(myAuthority);

        myAuthority.removeResourceAuthorities(resourceAuthorityBack);
        assertThat(myAuthority.getResourceAuthorities()).doesNotContain(resourceAuthorityBack);
        assertThat(resourceAuthorityBack.getMyAuthority()).isNull();

        myAuthority.resourceAuthorities(new HashSet<>(Set.of(resourceAuthorityBack)));
        assertThat(myAuthority.getResourceAuthorities()).containsOnly(resourceAuthorityBack);
        assertThat(resourceAuthorityBack.getMyAuthority()).isEqualTo(myAuthority);

        myAuthority.setResourceAuthorities(new HashSet<>());
        assertThat(myAuthority.getResourceAuthorities()).doesNotContain(resourceAuthorityBack);
        assertThat(resourceAuthorityBack.getMyAuthority()).isNull();
    }

    @Test
    void parentTest() throws Exception {
        MyAuthority myAuthority = getMyAuthorityRandomSampleGenerator();
        MyAuthority myAuthorityBack = getMyAuthorityRandomSampleGenerator();

        myAuthority.setParent(myAuthorityBack);
        assertThat(myAuthority.getParent()).isEqualTo(myAuthorityBack);

        myAuthority.parent(null);
        assertThat(myAuthority.getParent()).isNull();
    }
}
