package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.ConsumeMaterial;
import com.hs.ec.portal.domain.Product;
import com.hs.ec.portal.repository.ConsumeMaterialRepository;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.repository.ProductRepository;
import com.hs.ec.portal.service.ConsumeMaterialService;
import com.hs.ec.portal.service.dto.ConsumeMaterialDTO;
import com.hs.ec.portal.service.mapper.ConsumeMaterialMapper;
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
 * Integration tests for the {@link ConsumeMaterialResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ConsumeMaterialResourceIT {

    private static final Long DEFAULT_TYPE_CLASS_ID = 1L;
    private static final Long UPDATED_TYPE_CLASS_ID = 2L;
    private static final Long SMALLER_TYPE_CLASS_ID = 1L - 1L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/consume-materials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConsumeMaterialRepository consumeMaterialRepository;

    @Mock
    private ConsumeMaterialRepository consumeMaterialRepositoryMock;

    @Autowired
    private ConsumeMaterialMapper consumeMaterialMapper;

    @Mock
    private ConsumeMaterialService consumeMaterialServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ConsumeMaterial consumeMaterial;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConsumeMaterial createEntity(EntityManager em) {
        ConsumeMaterial consumeMaterial = new ConsumeMaterial().typeClassId(DEFAULT_TYPE_CLASS_ID).name(DEFAULT_NAME).value(DEFAULT_VALUE);
        // Add required entity
        Product product;
        product = em.insert(ProductResourceIT.createEntity(em)).block();
        consumeMaterial.setProduct(product);
        return consumeMaterial;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConsumeMaterial createUpdatedEntity(EntityManager em) {
        ConsumeMaterial consumeMaterial = new ConsumeMaterial().typeClassId(UPDATED_TYPE_CLASS_ID).name(UPDATED_NAME).value(UPDATED_VALUE);
        // Add required entity
        Product product;
        product = em.insert(ProductResourceIT.createUpdatedEntity(em)).block();
        consumeMaterial.setProduct(product);
        return consumeMaterial;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ConsumeMaterial.class).block();
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
        consumeMaterial = createEntity(em);
    }

    @Test
    void createConsumeMaterial() throws Exception {
        int databaseSizeBeforeCreate = consumeMaterialRepository.findAll().collectList().block().size();
        // Create the ConsumeMaterial
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterial);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterial> consumeMaterialList = consumeMaterialRepository.findAll().collectList().block();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeCreate + 1);
        ConsumeMaterial testConsumeMaterial = consumeMaterialList.get(consumeMaterialList.size() - 1);
        assertThat(testConsumeMaterial.getTypeClassId()).isEqualTo(DEFAULT_TYPE_CLASS_ID);
        assertThat(testConsumeMaterial.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testConsumeMaterial.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    void createConsumeMaterialWithExistingId() throws Exception {
        // Create the ConsumeMaterial with an existing ID
        consumeMaterial.setId(1L);
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterial);

        int databaseSizeBeforeCreate = consumeMaterialRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterial> consumeMaterialList = consumeMaterialRepository.findAll().collectList().block();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTypeClassIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = consumeMaterialRepository.findAll().collectList().block().size();
        // set the field null
        consumeMaterial.setTypeClassId(null);

        // Create the ConsumeMaterial, which fails.
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterial);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ConsumeMaterial> consumeMaterialList = consumeMaterialRepository.findAll().collectList().block();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = consumeMaterialRepository.findAll().collectList().block().size();
        // set the field null
        consumeMaterial.setName(null);

        // Create the ConsumeMaterial, which fails.
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterial);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ConsumeMaterial> consumeMaterialList = consumeMaterialRepository.findAll().collectList().block();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = consumeMaterialRepository.findAll().collectList().block().size();
        // set the field null
        consumeMaterial.setValue(null);

        // Create the ConsumeMaterial, which fails.
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterial);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ConsumeMaterial> consumeMaterialList = consumeMaterialRepository.findAll().collectList().block();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllConsumeMaterials() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        // Get all the consumeMaterialList
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
            .value(hasItem(consumeMaterial.getId().intValue()))
            .jsonPath("$.[*].typeClassId")
            .value(hasItem(DEFAULT_TYPE_CLASS_ID.intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].value")
            .value(hasItem(DEFAULT_VALUE));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConsumeMaterialsWithEagerRelationshipsIsEnabled() {
        when(consumeMaterialServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(consumeMaterialServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConsumeMaterialsWithEagerRelationshipsIsNotEnabled() {
        when(consumeMaterialServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(consumeMaterialRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getConsumeMaterial() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        // Get the consumeMaterial
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, consumeMaterial.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(consumeMaterial.getId().intValue()))
            .jsonPath("$.typeClassId")
            .value(is(DEFAULT_TYPE_CLASS_ID.intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.value")
            .value(is(DEFAULT_VALUE));
    }

    @Test
    void getConsumeMaterialsByIdFiltering() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        Long id = consumeMaterial.getId();

        defaultConsumeMaterialShouldBeFound("id.equals=" + id);
        defaultConsumeMaterialShouldNotBeFound("id.notEquals=" + id);

        defaultConsumeMaterialShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultConsumeMaterialShouldNotBeFound("id.greaterThan=" + id);

        defaultConsumeMaterialShouldBeFound("id.lessThanOrEqual=" + id);
        defaultConsumeMaterialShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllConsumeMaterialsByTypeClassIdIsEqualToSomething() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        // Get all the consumeMaterialList where typeClassId equals to DEFAULT_TYPE_CLASS_ID
        defaultConsumeMaterialShouldBeFound("typeClassId.equals=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the consumeMaterialList where typeClassId equals to UPDATED_TYPE_CLASS_ID
        defaultConsumeMaterialShouldNotBeFound("typeClassId.equals=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllConsumeMaterialsByTypeClassIdIsInShouldWork() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        // Get all the consumeMaterialList where typeClassId in DEFAULT_TYPE_CLASS_ID or UPDATED_TYPE_CLASS_ID
        defaultConsumeMaterialShouldBeFound("typeClassId.in=" + DEFAULT_TYPE_CLASS_ID + "," + UPDATED_TYPE_CLASS_ID);

        // Get all the consumeMaterialList where typeClassId equals to UPDATED_TYPE_CLASS_ID
        defaultConsumeMaterialShouldNotBeFound("typeClassId.in=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllConsumeMaterialsByTypeClassIdIsNullOrNotNull() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        // Get all the consumeMaterialList where typeClassId is not null
        defaultConsumeMaterialShouldBeFound("typeClassId.specified=true");

        // Get all the consumeMaterialList where typeClassId is null
        defaultConsumeMaterialShouldNotBeFound("typeClassId.specified=false");
    }

    @Test
    void getAllConsumeMaterialsByTypeClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        // Get all the consumeMaterialList where typeClassId is greater than or equal to DEFAULT_TYPE_CLASS_ID
        defaultConsumeMaterialShouldBeFound("typeClassId.greaterThanOrEqual=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the consumeMaterialList where typeClassId is greater than or equal to UPDATED_TYPE_CLASS_ID
        defaultConsumeMaterialShouldNotBeFound("typeClassId.greaterThanOrEqual=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllConsumeMaterialsByTypeClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        // Get all the consumeMaterialList where typeClassId is less than or equal to DEFAULT_TYPE_CLASS_ID
        defaultConsumeMaterialShouldBeFound("typeClassId.lessThanOrEqual=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the consumeMaterialList where typeClassId is less than or equal to SMALLER_TYPE_CLASS_ID
        defaultConsumeMaterialShouldNotBeFound("typeClassId.lessThanOrEqual=" + SMALLER_TYPE_CLASS_ID);
    }

    @Test
    void getAllConsumeMaterialsByTypeClassIdIsLessThanSomething() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        // Get all the consumeMaterialList where typeClassId is less than DEFAULT_TYPE_CLASS_ID
        defaultConsumeMaterialShouldNotBeFound("typeClassId.lessThan=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the consumeMaterialList where typeClassId is less than UPDATED_TYPE_CLASS_ID
        defaultConsumeMaterialShouldBeFound("typeClassId.lessThan=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllConsumeMaterialsByTypeClassIdIsGreaterThanSomething() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        // Get all the consumeMaterialList where typeClassId is greater than DEFAULT_TYPE_CLASS_ID
        defaultConsumeMaterialShouldNotBeFound("typeClassId.greaterThan=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the consumeMaterialList where typeClassId is greater than SMALLER_TYPE_CLASS_ID
        defaultConsumeMaterialShouldBeFound("typeClassId.greaterThan=" + SMALLER_TYPE_CLASS_ID);
    }

    @Test
    void getAllConsumeMaterialsByNameIsEqualToSomething() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        // Get all the consumeMaterialList where name equals to DEFAULT_NAME
        defaultConsumeMaterialShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the consumeMaterialList where name equals to UPDATED_NAME
        defaultConsumeMaterialShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    void getAllConsumeMaterialsByNameIsInShouldWork() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        // Get all the consumeMaterialList where name in DEFAULT_NAME or UPDATED_NAME
        defaultConsumeMaterialShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the consumeMaterialList where name equals to UPDATED_NAME
        defaultConsumeMaterialShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    void getAllConsumeMaterialsByNameIsNullOrNotNull() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        // Get all the consumeMaterialList where name is not null
        defaultConsumeMaterialShouldBeFound("name.specified=true");

        // Get all the consumeMaterialList where name is null
        defaultConsumeMaterialShouldNotBeFound("name.specified=false");
    }

    @Test
    void getAllConsumeMaterialsByNameContainsSomething() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        // Get all the consumeMaterialList where name contains DEFAULT_NAME
        defaultConsumeMaterialShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the consumeMaterialList where name contains UPDATED_NAME
        defaultConsumeMaterialShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    void getAllConsumeMaterialsByNameNotContainsSomething() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        // Get all the consumeMaterialList where name does not contain DEFAULT_NAME
        defaultConsumeMaterialShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the consumeMaterialList where name does not contain UPDATED_NAME
        defaultConsumeMaterialShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    void getAllConsumeMaterialsByValueIsEqualToSomething() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        // Get all the consumeMaterialList where value equals to DEFAULT_VALUE
        defaultConsumeMaterialShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the consumeMaterialList where value equals to UPDATED_VALUE
        defaultConsumeMaterialShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    void getAllConsumeMaterialsByValueIsInShouldWork() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        // Get all the consumeMaterialList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultConsumeMaterialShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the consumeMaterialList where value equals to UPDATED_VALUE
        defaultConsumeMaterialShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    void getAllConsumeMaterialsByValueIsNullOrNotNull() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        // Get all the consumeMaterialList where value is not null
        defaultConsumeMaterialShouldBeFound("value.specified=true");

        // Get all the consumeMaterialList where value is null
        defaultConsumeMaterialShouldNotBeFound("value.specified=false");
    }

    @Test
    void getAllConsumeMaterialsByValueContainsSomething() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        // Get all the consumeMaterialList where value contains DEFAULT_VALUE
        defaultConsumeMaterialShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the consumeMaterialList where value contains UPDATED_VALUE
        defaultConsumeMaterialShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    void getAllConsumeMaterialsByValueNotContainsSomething() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        // Get all the consumeMaterialList where value does not contain DEFAULT_VALUE
        defaultConsumeMaterialShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the consumeMaterialList where value does not contain UPDATED_VALUE
        defaultConsumeMaterialShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }

    @Test
    void getAllConsumeMaterialsByProductIsEqualToSomething() {
        Product product = ProductResourceIT.createEntity(em);
        productRepository.save(product).block();
        Long productId = product.getId();
        consumeMaterial.setProductId(productId);
        consumeMaterialRepository.save(consumeMaterial).block();
        // Get all the consumeMaterialList where product equals to productId
        defaultConsumeMaterialShouldBeFound("productId.equals=" + productId);

        // Get all the consumeMaterialList where product equals to (productId + 1)
        defaultConsumeMaterialShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConsumeMaterialShouldBeFound(String filter) {
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
            .value(hasItem(consumeMaterial.getId().intValue()))
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
    private void defaultConsumeMaterialShouldNotBeFound(String filter) {
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
    void getNonExistingConsumeMaterial() {
        // Get the consumeMaterial
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingConsumeMaterial() throws Exception {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        int databaseSizeBeforeUpdate = consumeMaterialRepository.findAll().collectList().block().size();

        // Update the consumeMaterial
        ConsumeMaterial updatedConsumeMaterial = consumeMaterialRepository.findById(consumeMaterial.getId()).block();
        updatedConsumeMaterial.typeClassId(UPDATED_TYPE_CLASS_ID).name(UPDATED_NAME).value(UPDATED_VALUE);
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(updatedConsumeMaterial);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, consumeMaterialDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterial> consumeMaterialList = consumeMaterialRepository.findAll().collectList().block();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeUpdate);
        ConsumeMaterial testConsumeMaterial = consumeMaterialList.get(consumeMaterialList.size() - 1);
        assertThat(testConsumeMaterial.getTypeClassId()).isEqualTo(UPDATED_TYPE_CLASS_ID);
        assertThat(testConsumeMaterial.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testConsumeMaterial.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void putNonExistingConsumeMaterial() throws Exception {
        int databaseSizeBeforeUpdate = consumeMaterialRepository.findAll().collectList().block().size();
        consumeMaterial.setId(longCount.incrementAndGet());

        // Create the ConsumeMaterial
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, consumeMaterialDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterial> consumeMaterialList = consumeMaterialRepository.findAll().collectList().block();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchConsumeMaterial() throws Exception {
        int databaseSizeBeforeUpdate = consumeMaterialRepository.findAll().collectList().block().size();
        consumeMaterial.setId(longCount.incrementAndGet());

        // Create the ConsumeMaterial
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterial> consumeMaterialList = consumeMaterialRepository.findAll().collectList().block();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamConsumeMaterial() throws Exception {
        int databaseSizeBeforeUpdate = consumeMaterialRepository.findAll().collectList().block().size();
        consumeMaterial.setId(longCount.incrementAndGet());

        // Create the ConsumeMaterial
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterial> consumeMaterialList = consumeMaterialRepository.findAll().collectList().block();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateConsumeMaterialWithPatch() throws Exception {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        int databaseSizeBeforeUpdate = consumeMaterialRepository.findAll().collectList().block().size();

        // Update the consumeMaterial using partial update
        ConsumeMaterial partialUpdatedConsumeMaterial = new ConsumeMaterial();
        partialUpdatedConsumeMaterial.setId(consumeMaterial.getId());

        partialUpdatedConsumeMaterial.value(UPDATED_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedConsumeMaterial.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedConsumeMaterial))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterial> consumeMaterialList = consumeMaterialRepository.findAll().collectList().block();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeUpdate);
        ConsumeMaterial testConsumeMaterial = consumeMaterialList.get(consumeMaterialList.size() - 1);
        assertThat(testConsumeMaterial.getTypeClassId()).isEqualTo(DEFAULT_TYPE_CLASS_ID);
        assertThat(testConsumeMaterial.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testConsumeMaterial.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void fullUpdateConsumeMaterialWithPatch() throws Exception {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        int databaseSizeBeforeUpdate = consumeMaterialRepository.findAll().collectList().block().size();

        // Update the consumeMaterial using partial update
        ConsumeMaterial partialUpdatedConsumeMaterial = new ConsumeMaterial();
        partialUpdatedConsumeMaterial.setId(consumeMaterial.getId());

        partialUpdatedConsumeMaterial.typeClassId(UPDATED_TYPE_CLASS_ID).name(UPDATED_NAME).value(UPDATED_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedConsumeMaterial.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedConsumeMaterial))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterial> consumeMaterialList = consumeMaterialRepository.findAll().collectList().block();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeUpdate);
        ConsumeMaterial testConsumeMaterial = consumeMaterialList.get(consumeMaterialList.size() - 1);
        assertThat(testConsumeMaterial.getTypeClassId()).isEqualTo(UPDATED_TYPE_CLASS_ID);
        assertThat(testConsumeMaterial.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testConsumeMaterial.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void patchNonExistingConsumeMaterial() throws Exception {
        int databaseSizeBeforeUpdate = consumeMaterialRepository.findAll().collectList().block().size();
        consumeMaterial.setId(longCount.incrementAndGet());

        // Create the ConsumeMaterial
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, consumeMaterialDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterial> consumeMaterialList = consumeMaterialRepository.findAll().collectList().block();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchConsumeMaterial() throws Exception {
        int databaseSizeBeforeUpdate = consumeMaterialRepository.findAll().collectList().block().size();
        consumeMaterial.setId(longCount.incrementAndGet());

        // Create the ConsumeMaterial
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterial> consumeMaterialList = consumeMaterialRepository.findAll().collectList().block();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamConsumeMaterial() throws Exception {
        int databaseSizeBeforeUpdate = consumeMaterialRepository.findAll().collectList().block().size();
        consumeMaterial.setId(longCount.incrementAndGet());

        // Create the ConsumeMaterial
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterial> consumeMaterialList = consumeMaterialRepository.findAll().collectList().block();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteConsumeMaterial() {
        // Initialize the database
        consumeMaterialRepository.save(consumeMaterial).block();

        int databaseSizeBeforeDelete = consumeMaterialRepository.findAll().collectList().block().size();

        // Delete the consumeMaterial
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, consumeMaterial.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ConsumeMaterial> consumeMaterialList = consumeMaterialRepository.findAll().collectList().block();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
