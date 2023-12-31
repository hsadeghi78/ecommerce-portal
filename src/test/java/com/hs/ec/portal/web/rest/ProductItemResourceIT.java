package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.Product;
import com.hs.ec.portal.domain.ProductItem;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.repository.ProductItemRepository;
import com.hs.ec.portal.repository.ProductRepository;
import com.hs.ec.portal.service.ProductItemService;
import com.hs.ec.portal.service.dto.ProductItemDTO;
import com.hs.ec.portal.service.mapper.ProductItemMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link ProductItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProductItemResourceIT {

    private static final Long DEFAULT_TYPE_CLASS_ID = 1L;
    private static final Long UPDATED_TYPE_CLASS_ID = 2L;
    private static final Long SMALLER_TYPE_CLASS_ID = 1L - 1L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductItemRepository productItemRepository;

    @Mock
    private ProductItemRepository productItemRepositoryMock;

    @Autowired
    private ProductItemMapper productItemMapper;

    @Mock
    private ProductItemService productItemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ProductItem productItem;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductItem createEntity(EntityManager em) {
        ProductItem productItem = new ProductItem().typeClassId(DEFAULT_TYPE_CLASS_ID).name(DEFAULT_NAME).value(DEFAULT_VALUE);
        // Add required entity
        Product product;
        product = em.insert(ProductResourceIT.createEntity(em)).block();
        productItem.setProduct(product);
        return productItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductItem createUpdatedEntity(EntityManager em) {
        ProductItem productItem = new ProductItem().typeClassId(UPDATED_TYPE_CLASS_ID).name(UPDATED_NAME).value(UPDATED_VALUE);
        // Add required entity
        Product product;
        product = em.insert(ProductResourceIT.createUpdatedEntity(em)).block();
        productItem.setProduct(product);
        return productItem;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ProductItem.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        ProductResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        productItem = createEntity(em);
    }

    @Test
    void createProductItem() throws Exception {
        int databaseSizeBeforeCreate = productItemRepository.findAll().collectList().block().size();
        // Create the ProductItem
        ProductItemDTO productItemDTO = productItemMapper.toDto(productItem);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productItemDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ProductItem in the database
        List<ProductItem> productItemList = productItemRepository.findAll().collectList().block();
        assertThat(productItemList).hasSize(databaseSizeBeforeCreate + 1);
        ProductItem testProductItem = productItemList.get(productItemList.size() - 1);
        assertThat(testProductItem.getTypeClassId()).isEqualTo(DEFAULT_TYPE_CLASS_ID);
        assertThat(testProductItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProductItem.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    void createProductItemWithExistingId() throws Exception {
        // Create the ProductItem with an existing ID
        productItem.setId(1L);
        ProductItemDTO productItemDTO = productItemMapper.toDto(productItem);

        int databaseSizeBeforeCreate = productItemRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductItem in the database
        List<ProductItem> productItemList = productItemRepository.findAll().collectList().block();
        assertThat(productItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTypeClassIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productItemRepository.findAll().collectList().block().size();
        // set the field null
        productItem.setTypeClassId(null);

        // Create the ProductItem, which fails.
        ProductItemDTO productItemDTO = productItemMapper.toDto(productItem);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProductItem> productItemList = productItemRepository.findAll().collectList().block();
        assertThat(productItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productItemRepository.findAll().collectList().block().size();
        // set the field null
        productItem.setName(null);

        // Create the ProductItem, which fails.
        ProductItemDTO productItemDTO = productItemMapper.toDto(productItem);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProductItem> productItemList = productItemRepository.findAll().collectList().block();
        assertThat(productItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = productItemRepository.findAll().collectList().block().size();
        // set the field null
        productItem.setValue(null);

        // Create the ProductItem, which fails.
        ProductItemDTO productItemDTO = productItemMapper.toDto(productItem);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProductItem> productItemList = productItemRepository.findAll().collectList().block();
        assertThat(productItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProductItems() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        // Get all the productItemList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(productItem.getId().intValue()))
            .jsonPath("$.[*].typeClassId")
            .value(hasItem(DEFAULT_TYPE_CLASS_ID.intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].value")
            .value(hasItem(DEFAULT_VALUE));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductItemsWithEagerRelationshipsIsEnabled() {
        when(productItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(productItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductItemsWithEagerRelationshipsIsNotEnabled() {
        when(productItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(productItemRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getProductItem() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        // Get the productItem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, productItem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(productItem.getId().intValue()))
            .jsonPath("$.typeClassId")
            .value(is(DEFAULT_TYPE_CLASS_ID.intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.value")
            .value(is(DEFAULT_VALUE));
    }

    @Test
    void getProductItemsByIdFiltering() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        Long id = productItem.getId();

        defaultProductItemShouldBeFound("id.equals=" + id);
        defaultProductItemShouldNotBeFound("id.notEquals=" + id);

        defaultProductItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductItemShouldNotBeFound("id.greaterThan=" + id);

        defaultProductItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllProductItemsByTypeClassIdIsEqualToSomething() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        // Get all the productItemList where typeClassId equals to DEFAULT_TYPE_CLASS_ID
        defaultProductItemShouldBeFound("typeClassId.equals=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the productItemList where typeClassId equals to UPDATED_TYPE_CLASS_ID
        defaultProductItemShouldNotBeFound("typeClassId.equals=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllProductItemsByTypeClassIdIsInShouldWork() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        // Get all the productItemList where typeClassId in DEFAULT_TYPE_CLASS_ID or UPDATED_TYPE_CLASS_ID
        defaultProductItemShouldBeFound("typeClassId.in=" + DEFAULT_TYPE_CLASS_ID + "," + UPDATED_TYPE_CLASS_ID);

        // Get all the productItemList where typeClassId equals to UPDATED_TYPE_CLASS_ID
        defaultProductItemShouldNotBeFound("typeClassId.in=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllProductItemsByTypeClassIdIsNullOrNotNull() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        // Get all the productItemList where typeClassId is not null
        defaultProductItemShouldBeFound("typeClassId.specified=true");

        // Get all the productItemList where typeClassId is null
        defaultProductItemShouldNotBeFound("typeClassId.specified=false");
    }

    @Test
    void getAllProductItemsByTypeClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        // Get all the productItemList where typeClassId is greater than or equal to DEFAULT_TYPE_CLASS_ID
        defaultProductItemShouldBeFound("typeClassId.greaterThanOrEqual=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the productItemList where typeClassId is greater than or equal to UPDATED_TYPE_CLASS_ID
        defaultProductItemShouldNotBeFound("typeClassId.greaterThanOrEqual=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllProductItemsByTypeClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        // Get all the productItemList where typeClassId is less than or equal to DEFAULT_TYPE_CLASS_ID
        defaultProductItemShouldBeFound("typeClassId.lessThanOrEqual=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the productItemList where typeClassId is less than or equal to SMALLER_TYPE_CLASS_ID
        defaultProductItemShouldNotBeFound("typeClassId.lessThanOrEqual=" + SMALLER_TYPE_CLASS_ID);
    }

    @Test
    void getAllProductItemsByTypeClassIdIsLessThanSomething() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        // Get all the productItemList where typeClassId is less than DEFAULT_TYPE_CLASS_ID
        defaultProductItemShouldNotBeFound("typeClassId.lessThan=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the productItemList where typeClassId is less than UPDATED_TYPE_CLASS_ID
        defaultProductItemShouldBeFound("typeClassId.lessThan=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllProductItemsByTypeClassIdIsGreaterThanSomething() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        // Get all the productItemList where typeClassId is greater than DEFAULT_TYPE_CLASS_ID
        defaultProductItemShouldNotBeFound("typeClassId.greaterThan=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the productItemList where typeClassId is greater than SMALLER_TYPE_CLASS_ID
        defaultProductItemShouldBeFound("typeClassId.greaterThan=" + SMALLER_TYPE_CLASS_ID);
    }

    @Test
    void getAllProductItemsByNameIsEqualToSomething() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        // Get all the productItemList where name equals to DEFAULT_NAME
        defaultProductItemShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the productItemList where name equals to UPDATED_NAME
        defaultProductItemShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    void getAllProductItemsByNameIsInShouldWork() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        // Get all the productItemList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProductItemShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the productItemList where name equals to UPDATED_NAME
        defaultProductItemShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    void getAllProductItemsByNameIsNullOrNotNull() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        // Get all the productItemList where name is not null
        defaultProductItemShouldBeFound("name.specified=true");

        // Get all the productItemList where name is null
        defaultProductItemShouldNotBeFound("name.specified=false");
    }

    @Test
    void getAllProductItemsByNameContainsSomething() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        // Get all the productItemList where name contains DEFAULT_NAME
        defaultProductItemShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the productItemList where name contains UPDATED_NAME
        defaultProductItemShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    void getAllProductItemsByNameNotContainsSomething() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        // Get all the productItemList where name does not contain DEFAULT_NAME
        defaultProductItemShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the productItemList where name does not contain UPDATED_NAME
        defaultProductItemShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    void getAllProductItemsByValueIsEqualToSomething() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        // Get all the productItemList where value equals to DEFAULT_VALUE
        defaultProductItemShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the productItemList where value equals to UPDATED_VALUE
        defaultProductItemShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    void getAllProductItemsByValueIsInShouldWork() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        // Get all the productItemList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultProductItemShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the productItemList where value equals to UPDATED_VALUE
        defaultProductItemShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    void getAllProductItemsByValueIsNullOrNotNull() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        // Get all the productItemList where value is not null
        defaultProductItemShouldBeFound("value.specified=true");

        // Get all the productItemList where value is null
        defaultProductItemShouldNotBeFound("value.specified=false");
    }

    @Test
    void getAllProductItemsByValueContainsSomething() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        // Get all the productItemList where value contains DEFAULT_VALUE
        defaultProductItemShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the productItemList where value contains UPDATED_VALUE
        defaultProductItemShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    void getAllProductItemsByValueNotContainsSomething() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        // Get all the productItemList where value does not contain DEFAULT_VALUE
        defaultProductItemShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the productItemList where value does not contain UPDATED_VALUE
        defaultProductItemShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }

    @Test
    void getAllProductItemsByProductIsEqualToSomething() {
        Product product = ProductResourceIT.createEntity(em);
        productRepository.save(product).block();
        Long productId = product.getId();
        productItem.setProductId(productId);
        productItemRepository.save(productItem).block();
        // Get all the productItemList where product equals to productId
        defaultProductItemShouldBeFound("productId.equals=" + productId);

        // Get all the productItemList where product equals to (productId + 1)
        defaultProductItemShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductItemShouldBeFound(String filter) {
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(productItem.getId().intValue()))
            .jsonPath("$.[*].typeClassId")
            .value(hasItem(DEFAULT_TYPE_CLASS_ID.intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].value")
            .value(hasItem(DEFAULT_VALUE));

        // Check, that the count call also returns 1
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "/count?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .value(is(1));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductItemShouldNotBeFound(String filter) {
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .isArray()
            .jsonPath("$")
            .isEmpty();

        // Check, that the count call also returns 0
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "/count?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .value(is(0));
    }

    @Test
    void getNonExistingProductItem() {
        // Get the productItem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingProductItem() throws Exception {
        // Initialize the database
        productItemRepository.save(productItem).block();

        int databaseSizeBeforeUpdate = productItemRepository.findAll().collectList().block().size();

        // Update the productItem
        ProductItem updatedProductItem = productItemRepository.findById(productItem.getId()).block();
        updatedProductItem.typeClassId(UPDATED_TYPE_CLASS_ID).name(UPDATED_NAME).value(UPDATED_VALUE);
        ProductItemDTO productItemDTO = productItemMapper.toDto(updatedProductItem);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, productItemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productItemDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ProductItem in the database
        List<ProductItem> productItemList = productItemRepository.findAll().collectList().block();
        assertThat(productItemList).hasSize(databaseSizeBeforeUpdate);
        ProductItem testProductItem = productItemList.get(productItemList.size() - 1);
        assertThat(testProductItem.getTypeClassId()).isEqualTo(UPDATED_TYPE_CLASS_ID);
        assertThat(testProductItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProductItem.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void putNonExistingProductItem() throws Exception {
        int databaseSizeBeforeUpdate = productItemRepository.findAll().collectList().block().size();
        productItem.setId(longCount.incrementAndGet());

        // Create the ProductItem
        ProductItemDTO productItemDTO = productItemMapper.toDto(productItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, productItemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductItem in the database
        List<ProductItem> productItemList = productItemRepository.findAll().collectList().block();
        assertThat(productItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProductItem() throws Exception {
        int databaseSizeBeforeUpdate = productItemRepository.findAll().collectList().block().size();
        productItem.setId(longCount.incrementAndGet());

        // Create the ProductItem
        ProductItemDTO productItemDTO = productItemMapper.toDto(productItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductItem in the database
        List<ProductItem> productItemList = productItemRepository.findAll().collectList().block();
        assertThat(productItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProductItem() throws Exception {
        int databaseSizeBeforeUpdate = productItemRepository.findAll().collectList().block().size();
        productItem.setId(longCount.incrementAndGet());

        // Create the ProductItem
        ProductItemDTO productItemDTO = productItemMapper.toDto(productItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productItemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ProductItem in the database
        List<ProductItem> productItemList = productItemRepository.findAll().collectList().block();
        assertThat(productItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProductItemWithPatch() throws Exception {
        // Initialize the database
        productItemRepository.save(productItem).block();

        int databaseSizeBeforeUpdate = productItemRepository.findAll().collectList().block().size();

        // Update the productItem using partial update
        ProductItem partialUpdatedProductItem = new ProductItem();
        partialUpdatedProductItem.setId(productItem.getId());

        partialUpdatedProductItem.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProductItem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProductItem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ProductItem in the database
        List<ProductItem> productItemList = productItemRepository.findAll().collectList().block();
        assertThat(productItemList).hasSize(databaseSizeBeforeUpdate);
        ProductItem testProductItem = productItemList.get(productItemList.size() - 1);
        assertThat(testProductItem.getTypeClassId()).isEqualTo(DEFAULT_TYPE_CLASS_ID);
        assertThat(testProductItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProductItem.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    void fullUpdateProductItemWithPatch() throws Exception {
        // Initialize the database
        productItemRepository.save(productItem).block();

        int databaseSizeBeforeUpdate = productItemRepository.findAll().collectList().block().size();

        // Update the productItem using partial update
        ProductItem partialUpdatedProductItem = new ProductItem();
        partialUpdatedProductItem.setId(productItem.getId());

        partialUpdatedProductItem.typeClassId(UPDATED_TYPE_CLASS_ID).name(UPDATED_NAME).value(UPDATED_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProductItem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProductItem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ProductItem in the database
        List<ProductItem> productItemList = productItemRepository.findAll().collectList().block();
        assertThat(productItemList).hasSize(databaseSizeBeforeUpdate);
        ProductItem testProductItem = productItemList.get(productItemList.size() - 1);
        assertThat(testProductItem.getTypeClassId()).isEqualTo(UPDATED_TYPE_CLASS_ID);
        assertThat(testProductItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProductItem.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void patchNonExistingProductItem() throws Exception {
        int databaseSizeBeforeUpdate = productItemRepository.findAll().collectList().block().size();
        productItem.setId(longCount.incrementAndGet());

        // Create the ProductItem
        ProductItemDTO productItemDTO = productItemMapper.toDto(productItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, productItemDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(productItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductItem in the database
        List<ProductItem> productItemList = productItemRepository.findAll().collectList().block();
        assertThat(productItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProductItem() throws Exception {
        int databaseSizeBeforeUpdate = productItemRepository.findAll().collectList().block().size();
        productItem.setId(longCount.incrementAndGet());

        // Create the ProductItem
        ProductItemDTO productItemDTO = productItemMapper.toDto(productItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(productItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductItem in the database
        List<ProductItem> productItemList = productItemRepository.findAll().collectList().block();
        assertThat(productItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProductItem() throws Exception {
        int databaseSizeBeforeUpdate = productItemRepository.findAll().collectList().block().size();
        productItem.setId(longCount.incrementAndGet());

        // Create the ProductItem
        ProductItemDTO productItemDTO = productItemMapper.toDto(productItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(productItemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ProductItem in the database
        List<ProductItem> productItemList = productItemRepository.findAll().collectList().block();
        assertThat(productItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProductItem() {
        // Initialize the database
        productItemRepository.save(productItem).block();

        int databaseSizeBeforeDelete = productItemRepository.findAll().collectList().block().size();

        // Delete the productItem
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, productItem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ProductItem> productItemList = productItemRepository.findAll().collectList().block();
        assertThat(productItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
