package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.CategoryTestSamples.*;
import static com.hs.ec.portal.domain.CategoryTestSamples.*;
import static com.hs.ec.portal.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Category.class);
        Category category1 = getCategorySample1();
        Category category2 = new Category();
        assertThat(category1).isNotEqualTo(category2);

        category2.setId(category1.getId());
        assertThat(category1).isEqualTo(category2);

        category2 = getCategorySample2();
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    void productsTest() throws Exception {
        Category category = getCategoryRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        category.addProducts(productBack);
        assertThat(category.getProducts()).containsOnly(productBack);
        assertThat(productBack.getCategory()).isEqualTo(category);

        category.removeProducts(productBack);
        assertThat(category.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getCategory()).isNull();

        category.products(new HashSet<>(Set.of(productBack)));
        assertThat(category.getProducts()).containsOnly(productBack);
        assertThat(productBack.getCategory()).isEqualTo(category);

        category.setProducts(new HashSet<>());
        assertThat(category.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getCategory()).isNull();
    }

    @Test
    void childrenTest() throws Exception {
        Category category = getCategoryRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        category.addChildren(categoryBack);
        assertThat(category.getChildren()).containsOnly(categoryBack);
        assertThat(categoryBack.getParent()).isEqualTo(category);

        category.removeChildren(categoryBack);
        assertThat(category.getChildren()).doesNotContain(categoryBack);
        assertThat(categoryBack.getParent()).isNull();

        category.children(new HashSet<>(Set.of(categoryBack)));
        assertThat(category.getChildren()).containsOnly(categoryBack);
        assertThat(categoryBack.getParent()).isEqualTo(category);

        category.setChildren(new HashSet<>());
        assertThat(category.getChildren()).doesNotContain(categoryBack);
        assertThat(categoryBack.getParent()).isNull();
    }

    @Test
    void parentTest() throws Exception {
        Category category = getCategoryRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        category.setParent(categoryBack);
        assertThat(category.getParent()).isEqualTo(categoryBack);

        category.parent(null);
        assertThat(category.getParent()).isNull();
    }
}
