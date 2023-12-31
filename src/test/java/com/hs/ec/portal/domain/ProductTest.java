package com.hs.ec.portal.domain;

import static com.hs.ec.portal.domain.CampaignTestSamples.*;
import static com.hs.ec.portal.domain.CategoryTestSamples.*;
import static com.hs.ec.portal.domain.ConsumeMaterialTestSamples.*;
import static com.hs.ec.portal.domain.FactorItemTestSamples.*;
import static com.hs.ec.portal.domain.FileDocumentTestSamples.*;
import static com.hs.ec.portal.domain.PartyTestSamples.*;
import static com.hs.ec.portal.domain.ProductItemTestSamples.*;
import static com.hs.ec.portal.domain.ProductTestSamples.*;
import static com.hs.ec.portal.domain.ProductTestSamples.*;
import static com.hs.ec.portal.domain.UserCommentTestSamples.*;
import static com.hs.ec.portal.domain.UserFavoriteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hs.ec.portal.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void productItemsTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        ProductItem productItemBack = getProductItemRandomSampleGenerator();

        product.addProductItems(productItemBack);
        assertThat(product.getProductItems()).containsOnly(productItemBack);
        assertThat(productItemBack.getProduct()).isEqualTo(product);

        product.removeProductItems(productItemBack);
        assertThat(product.getProductItems()).doesNotContain(productItemBack);
        assertThat(productItemBack.getProduct()).isNull();

        product.productItems(new HashSet<>(Set.of(productItemBack)));
        assertThat(product.getProductItems()).containsOnly(productItemBack);
        assertThat(productItemBack.getProduct()).isEqualTo(product);

        product.setProductItems(new HashSet<>());
        assertThat(product.getProductItems()).doesNotContain(productItemBack);
        assertThat(productItemBack.getProduct()).isNull();
    }

    @Test
    void userCommentsTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        UserComment userCommentBack = getUserCommentRandomSampleGenerator();

        product.addUserComments(userCommentBack);
        assertThat(product.getUserComments()).containsOnly(userCommentBack);
        assertThat(userCommentBack.getProduct()).isEqualTo(product);

        product.removeUserComments(userCommentBack);
        assertThat(product.getUserComments()).doesNotContain(userCommentBack);
        assertThat(userCommentBack.getProduct()).isNull();

        product.userComments(new HashSet<>(Set.of(userCommentBack)));
        assertThat(product.getUserComments()).containsOnly(userCommentBack);
        assertThat(userCommentBack.getProduct()).isEqualTo(product);

        product.setUserComments(new HashSet<>());
        assertThat(product.getUserComments()).doesNotContain(userCommentBack);
        assertThat(userCommentBack.getProduct()).isNull();
    }

    @Test
    void childrenTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        product.addChildren(productBack);
        assertThat(product.getChildren()).containsOnly(productBack);
        assertThat(productBack.getParent()).isEqualTo(product);

        product.removeChildren(productBack);
        assertThat(product.getChildren()).doesNotContain(productBack);
        assertThat(productBack.getParent()).isNull();

        product.children(new HashSet<>(Set.of(productBack)));
        assertThat(product.getChildren()).containsOnly(productBack);
        assertThat(productBack.getParent()).isEqualTo(product);

        product.setChildren(new HashSet<>());
        assertThat(product.getChildren()).doesNotContain(productBack);
        assertThat(productBack.getParent()).isNull();
    }

    @Test
    void favoritesTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        UserFavorite userFavoriteBack = getUserFavoriteRandomSampleGenerator();

        product.addFavorites(userFavoriteBack);
        assertThat(product.getFavorites()).containsOnly(userFavoriteBack);
        assertThat(userFavoriteBack.getProduct()).isEqualTo(product);

        product.removeFavorites(userFavoriteBack);
        assertThat(product.getFavorites()).doesNotContain(userFavoriteBack);
        assertThat(userFavoriteBack.getProduct()).isNull();

        product.favorites(new HashSet<>(Set.of(userFavoriteBack)));
        assertThat(product.getFavorites()).containsOnly(userFavoriteBack);
        assertThat(userFavoriteBack.getProduct()).isEqualTo(product);

        product.setFavorites(new HashSet<>());
        assertThat(product.getFavorites()).doesNotContain(userFavoriteBack);
        assertThat(userFavoriteBack.getProduct()).isNull();
    }

    @Test
    void materialsTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        ConsumeMaterial consumeMaterialBack = getConsumeMaterialRandomSampleGenerator();

        product.addMaterials(consumeMaterialBack);
        assertThat(product.getMaterials()).containsOnly(consumeMaterialBack);
        assertThat(consumeMaterialBack.getProduct()).isEqualTo(product);

        product.removeMaterials(consumeMaterialBack);
        assertThat(product.getMaterials()).doesNotContain(consumeMaterialBack);
        assertThat(consumeMaterialBack.getProduct()).isNull();

        product.materials(new HashSet<>(Set.of(consumeMaterialBack)));
        assertThat(product.getMaterials()).containsOnly(consumeMaterialBack);
        assertThat(consumeMaterialBack.getProduct()).isEqualTo(product);

        product.setMaterials(new HashSet<>());
        assertThat(product.getMaterials()).doesNotContain(consumeMaterialBack);
        assertThat(consumeMaterialBack.getProduct()).isNull();
    }

    @Test
    void factorItemsTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        FactorItem factorItemBack = getFactorItemRandomSampleGenerator();

        product.addFactorItems(factorItemBack);
        assertThat(product.getFactorItems()).containsOnly(factorItemBack);
        assertThat(factorItemBack.getProduct()).isEqualTo(product);

        product.removeFactorItems(factorItemBack);
        assertThat(product.getFactorItems()).doesNotContain(factorItemBack);
        assertThat(factorItemBack.getProduct()).isNull();

        product.factorItems(new HashSet<>(Set.of(factorItemBack)));
        assertThat(product.getFactorItems()).containsOnly(factorItemBack);
        assertThat(factorItemBack.getProduct()).isEqualTo(product);

        product.setFactorItems(new HashSet<>());
        assertThat(product.getFactorItems()).doesNotContain(factorItemBack);
        assertThat(factorItemBack.getProduct()).isNull();
    }

    @Test
    void documentsTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        FileDocument fileDocumentBack = getFileDocumentRandomSampleGenerator();

        product.addDocuments(fileDocumentBack);
        assertThat(product.getDocuments()).containsOnly(fileDocumentBack);

        product.removeDocuments(fileDocumentBack);
        assertThat(product.getDocuments()).doesNotContain(fileDocumentBack);

        product.documents(new HashSet<>(Set.of(fileDocumentBack)));
        assertThat(product.getDocuments()).containsOnly(fileDocumentBack);

        product.setDocuments(new HashSet<>());
        assertThat(product.getDocuments()).doesNotContain(fileDocumentBack);
    }

    @Test
    void categoryTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        product.setCategory(categoryBack);
        assertThat(product.getCategory()).isEqualTo(categoryBack);

        product.category(null);
        assertThat(product.getCategory()).isNull();
    }

    @Test
    void partyTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Party partyBack = getPartyRandomSampleGenerator();

        product.setParty(partyBack);
        assertThat(product.getParty()).isEqualTo(partyBack);

        product.party(null);
        assertThat(product.getParty()).isNull();
    }

    @Test
    void parentTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        product.setParent(productBack);
        assertThat(product.getParent()).isEqualTo(productBack);

        product.parent(null);
        assertThat(product.getParent()).isNull();
    }

    @Test
    void campaignsTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Campaign campaignBack = getCampaignRandomSampleGenerator();

        product.addCampaigns(campaignBack);
        assertThat(product.getCampaigns()).containsOnly(campaignBack);
        assertThat(campaignBack.getProducts()).containsOnly(product);

        product.removeCampaigns(campaignBack);
        assertThat(product.getCampaigns()).doesNotContain(campaignBack);
        assertThat(campaignBack.getProducts()).doesNotContain(product);

        product.campaigns(new HashSet<>(Set.of(campaignBack)));
        assertThat(product.getCampaigns()).containsOnly(campaignBack);
        assertThat(campaignBack.getProducts()).containsOnly(product);

        product.setCampaigns(new HashSet<>());
        assertThat(product.getCampaigns()).doesNotContain(campaignBack);
        assertThat(campaignBack.getProducts()).doesNotContain(product);
    }
}
