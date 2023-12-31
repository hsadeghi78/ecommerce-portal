package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.GeoDivision;
import com.hs.ec.portal.domain.GeoDivision;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.repository.GeoDivisionRepository;
import com.hs.ec.portal.repository.GeoDivisionRepository;
import com.hs.ec.portal.service.GeoDivisionService;
import com.hs.ec.portal.service.dto.GeoDivisionDTO;
import com.hs.ec.portal.service.mapper.GeoDivisionMapper;
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
 * Integration tests for the {@link GeoDivisionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class GeoDivisionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_CODE = 1L;
    private static final Long UPDATED_CODE = 2L;
    private static final Long SMALLER_CODE = 1L - 1L;

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;
    private static final Integer SMALLER_LEVEL = 1 - 1;

    private static final String ENTITY_API_URL = "/api/geo-divisions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GeoDivisionRepository geoDivisionRepository;

    @Mock
    private GeoDivisionRepository geoDivisionRepositoryMock;

    @Autowired
    private GeoDivisionMapper geoDivisionMapper;

    @Mock
    private GeoDivisionService geoDivisionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private GeoDivision geoDivision;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeoDivision createEntity(EntityManager em) {
        GeoDivision geoDivision = new GeoDivision().name(DEFAULT_NAME).code(DEFAULT_CODE).level(DEFAULT_LEVEL);
        return geoDivision;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeoDivision createUpdatedEntity(EntityManager em) {
        GeoDivision geoDivision = new GeoDivision().name(UPDATED_NAME).code(UPDATED_CODE).level(UPDATED_LEVEL);
        return geoDivision;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(GeoDivision.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        geoDivision = createEntity(em);
    }

    @Test
    void createGeoDivision() throws Exception {
        int databaseSizeBeforeCreate = geoDivisionRepository.findAll().collectList().block().size();
        // Create the GeoDivision
        GeoDivisionDTO geoDivisionDTO = geoDivisionMapper.toDto(geoDivision);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(geoDivisionDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the GeoDivision in the database
        List<GeoDivision> geoDivisionList = geoDivisionRepository.findAll().collectList().block();
        assertThat(geoDivisionList).hasSize(databaseSizeBeforeCreate + 1);
        GeoDivision testGeoDivision = geoDivisionList.get(geoDivisionList.size() - 1);
        assertThat(testGeoDivision.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGeoDivision.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testGeoDivision.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    void createGeoDivisionWithExistingId() throws Exception {
        // Create the GeoDivision with an existing ID
        geoDivision.setId(1L);
        GeoDivisionDTO geoDivisionDTO = geoDivisionMapper.toDto(geoDivision);

        int databaseSizeBeforeCreate = geoDivisionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(geoDivisionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GeoDivision in the database
        List<GeoDivision> geoDivisionList = geoDivisionRepository.findAll().collectList().block();
        assertThat(geoDivisionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = geoDivisionRepository.findAll().collectList().block().size();
        // set the field null
        geoDivision.setName(null);

        // Create the GeoDivision, which fails.
        GeoDivisionDTO geoDivisionDTO = geoDivisionMapper.toDto(geoDivision);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(geoDivisionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GeoDivision> geoDivisionList = geoDivisionRepository.findAll().collectList().block();
        assertThat(geoDivisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = geoDivisionRepository.findAll().collectList().block().size();
        // set the field null
        geoDivision.setCode(null);

        // Create the GeoDivision, which fails.
        GeoDivisionDTO geoDivisionDTO = geoDivisionMapper.toDto(geoDivision);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(geoDivisionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GeoDivision> geoDivisionList = geoDivisionRepository.findAll().collectList().block();
        assertThat(geoDivisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = geoDivisionRepository.findAll().collectList().block().size();
        // set the field null
        geoDivision.setLevel(null);

        // Create the GeoDivision, which fails.
        GeoDivisionDTO geoDivisionDTO = geoDivisionMapper.toDto(geoDivision);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(geoDivisionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GeoDivision> geoDivisionList = geoDivisionRepository.findAll().collectList().block();
        assertThat(geoDivisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllGeoDivisions() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList
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
            .value(hasItem(geoDivision.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE.intValue()))
            .jsonPath("$.[*].level")
            .value(hasItem(DEFAULT_LEVEL));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGeoDivisionsWithEagerRelationshipsIsEnabled() {
        when(geoDivisionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(geoDivisionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGeoDivisionsWithEagerRelationshipsIsNotEnabled() {
        when(geoDivisionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(geoDivisionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getGeoDivision() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get the geoDivision
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, geoDivision.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(geoDivision.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.code")
            .value(is(DEFAULT_CODE.intValue()))
            .jsonPath("$.level")
            .value(is(DEFAULT_LEVEL));
    }

    @Test
    void getGeoDivisionsByIdFiltering() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        Long id = geoDivision.getId();

        defaultGeoDivisionShouldBeFound("id.equals=" + id);
        defaultGeoDivisionShouldNotBeFound("id.notEquals=" + id);

        defaultGeoDivisionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGeoDivisionShouldNotBeFound("id.greaterThan=" + id);

        defaultGeoDivisionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGeoDivisionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllGeoDivisionsByNameIsEqualToSomething() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList where name equals to DEFAULT_NAME
        defaultGeoDivisionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the geoDivisionList where name equals to UPDATED_NAME
        defaultGeoDivisionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    void getAllGeoDivisionsByNameIsInShouldWork() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultGeoDivisionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the geoDivisionList where name equals to UPDATED_NAME
        defaultGeoDivisionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    void getAllGeoDivisionsByNameIsNullOrNotNull() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList where name is not null
        defaultGeoDivisionShouldBeFound("name.specified=true");

        // Get all the geoDivisionList where name is null
        defaultGeoDivisionShouldNotBeFound("name.specified=false");
    }

    @Test
    void getAllGeoDivisionsByNameContainsSomething() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList where name contains DEFAULT_NAME
        defaultGeoDivisionShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the geoDivisionList where name contains UPDATED_NAME
        defaultGeoDivisionShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    void getAllGeoDivisionsByNameNotContainsSomething() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList where name does not contain DEFAULT_NAME
        defaultGeoDivisionShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the geoDivisionList where name does not contain UPDATED_NAME
        defaultGeoDivisionShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    void getAllGeoDivisionsByCodeIsEqualToSomething() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList where code equals to DEFAULT_CODE
        defaultGeoDivisionShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the geoDivisionList where code equals to UPDATED_CODE
        defaultGeoDivisionShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    void getAllGeoDivisionsByCodeIsInShouldWork() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList where code in DEFAULT_CODE or UPDATED_CODE
        defaultGeoDivisionShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the geoDivisionList where code equals to UPDATED_CODE
        defaultGeoDivisionShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    void getAllGeoDivisionsByCodeIsNullOrNotNull() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList where code is not null
        defaultGeoDivisionShouldBeFound("code.specified=true");

        // Get all the geoDivisionList where code is null
        defaultGeoDivisionShouldNotBeFound("code.specified=false");
    }

    @Test
    void getAllGeoDivisionsByCodeIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList where code is greater than or equal to DEFAULT_CODE
        defaultGeoDivisionShouldBeFound("code.greaterThanOrEqual=" + DEFAULT_CODE);

        // Get all the geoDivisionList where code is greater than or equal to UPDATED_CODE
        defaultGeoDivisionShouldNotBeFound("code.greaterThanOrEqual=" + UPDATED_CODE);
    }

    @Test
    void getAllGeoDivisionsByCodeIsLessThanOrEqualToSomething() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList where code is less than or equal to DEFAULT_CODE
        defaultGeoDivisionShouldBeFound("code.lessThanOrEqual=" + DEFAULT_CODE);

        // Get all the geoDivisionList where code is less than or equal to SMALLER_CODE
        defaultGeoDivisionShouldNotBeFound("code.lessThanOrEqual=" + SMALLER_CODE);
    }

    @Test
    void getAllGeoDivisionsByCodeIsLessThanSomething() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList where code is less than DEFAULT_CODE
        defaultGeoDivisionShouldNotBeFound("code.lessThan=" + DEFAULT_CODE);

        // Get all the geoDivisionList where code is less than UPDATED_CODE
        defaultGeoDivisionShouldBeFound("code.lessThan=" + UPDATED_CODE);
    }

    @Test
    void getAllGeoDivisionsByCodeIsGreaterThanSomething() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList where code is greater than DEFAULT_CODE
        defaultGeoDivisionShouldNotBeFound("code.greaterThan=" + DEFAULT_CODE);

        // Get all the geoDivisionList where code is greater than SMALLER_CODE
        defaultGeoDivisionShouldBeFound("code.greaterThan=" + SMALLER_CODE);
    }

    @Test
    void getAllGeoDivisionsByLevelIsEqualToSomething() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList where level equals to DEFAULT_LEVEL
        defaultGeoDivisionShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the geoDivisionList where level equals to UPDATED_LEVEL
        defaultGeoDivisionShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    void getAllGeoDivisionsByLevelIsInShouldWork() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultGeoDivisionShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the geoDivisionList where level equals to UPDATED_LEVEL
        defaultGeoDivisionShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    void getAllGeoDivisionsByLevelIsNullOrNotNull() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList where level is not null
        defaultGeoDivisionShouldBeFound("level.specified=true");

        // Get all the geoDivisionList where level is null
        defaultGeoDivisionShouldNotBeFound("level.specified=false");
    }

    @Test
    void getAllGeoDivisionsByLevelIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList where level is greater than or equal to DEFAULT_LEVEL
        defaultGeoDivisionShouldBeFound("level.greaterThanOrEqual=" + DEFAULT_LEVEL);

        // Get all the geoDivisionList where level is greater than or equal to UPDATED_LEVEL
        defaultGeoDivisionShouldNotBeFound("level.greaterThanOrEqual=" + UPDATED_LEVEL);
    }

    @Test
    void getAllGeoDivisionsByLevelIsLessThanOrEqualToSomething() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList where level is less than or equal to DEFAULT_LEVEL
        defaultGeoDivisionShouldBeFound("level.lessThanOrEqual=" + DEFAULT_LEVEL);

        // Get all the geoDivisionList where level is less than or equal to SMALLER_LEVEL
        defaultGeoDivisionShouldNotBeFound("level.lessThanOrEqual=" + SMALLER_LEVEL);
    }

    @Test
    void getAllGeoDivisionsByLevelIsLessThanSomething() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList where level is less than DEFAULT_LEVEL
        defaultGeoDivisionShouldNotBeFound("level.lessThan=" + DEFAULT_LEVEL);

        // Get all the geoDivisionList where level is less than UPDATED_LEVEL
        defaultGeoDivisionShouldBeFound("level.lessThan=" + UPDATED_LEVEL);
    }

    @Test
    void getAllGeoDivisionsByLevelIsGreaterThanSomething() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        // Get all the geoDivisionList where level is greater than DEFAULT_LEVEL
        defaultGeoDivisionShouldNotBeFound("level.greaterThan=" + DEFAULT_LEVEL);

        // Get all the geoDivisionList where level is greater than SMALLER_LEVEL
        defaultGeoDivisionShouldBeFound("level.greaterThan=" + SMALLER_LEVEL);
    }

    @Test
    void getAllGeoDivisionsByParentIsEqualToSomething() {
        GeoDivision parent = GeoDivisionResourceIT.createEntity(em);
        geoDivisionRepository.save(parent).block();
        Long parentId = parent.getId();
        geoDivision.setParentId(parentId);
        geoDivisionRepository.save(geoDivision).block();
        // Get all the geoDivisionList where parent equals to parentId
        defaultGeoDivisionShouldBeFound("parentId.equals=" + parentId);

        // Get all the geoDivisionList where parent equals to (parentId + 1)
        defaultGeoDivisionShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGeoDivisionShouldBeFound(String filter) {
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
            .value(hasItem(geoDivision.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE.intValue()))
            .jsonPath("$.[*].level")
            .value(hasItem(DEFAULT_LEVEL));

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
    private void defaultGeoDivisionShouldNotBeFound(String filter) {
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
    void getNonExistingGeoDivision() {
        // Get the geoDivision
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingGeoDivision() throws Exception {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        int databaseSizeBeforeUpdate = geoDivisionRepository.findAll().collectList().block().size();

        // Update the geoDivision
        GeoDivision updatedGeoDivision = geoDivisionRepository.findById(geoDivision.getId()).block();
        updatedGeoDivision.name(UPDATED_NAME).code(UPDATED_CODE).level(UPDATED_LEVEL);
        GeoDivisionDTO geoDivisionDTO = geoDivisionMapper.toDto(updatedGeoDivision);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, geoDivisionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(geoDivisionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GeoDivision in the database
        List<GeoDivision> geoDivisionList = geoDivisionRepository.findAll().collectList().block();
        assertThat(geoDivisionList).hasSize(databaseSizeBeforeUpdate);
        GeoDivision testGeoDivision = geoDivisionList.get(geoDivisionList.size() - 1);
        assertThat(testGeoDivision.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGeoDivision.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testGeoDivision.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    void putNonExistingGeoDivision() throws Exception {
        int databaseSizeBeforeUpdate = geoDivisionRepository.findAll().collectList().block().size();
        geoDivision.setId(longCount.incrementAndGet());

        // Create the GeoDivision
        GeoDivisionDTO geoDivisionDTO = geoDivisionMapper.toDto(geoDivision);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, geoDivisionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(geoDivisionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GeoDivision in the database
        List<GeoDivision> geoDivisionList = geoDivisionRepository.findAll().collectList().block();
        assertThat(geoDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchGeoDivision() throws Exception {
        int databaseSizeBeforeUpdate = geoDivisionRepository.findAll().collectList().block().size();
        geoDivision.setId(longCount.incrementAndGet());

        // Create the GeoDivision
        GeoDivisionDTO geoDivisionDTO = geoDivisionMapper.toDto(geoDivision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(geoDivisionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GeoDivision in the database
        List<GeoDivision> geoDivisionList = geoDivisionRepository.findAll().collectList().block();
        assertThat(geoDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamGeoDivision() throws Exception {
        int databaseSizeBeforeUpdate = geoDivisionRepository.findAll().collectList().block().size();
        geoDivision.setId(longCount.incrementAndGet());

        // Create the GeoDivision
        GeoDivisionDTO geoDivisionDTO = geoDivisionMapper.toDto(geoDivision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(geoDivisionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the GeoDivision in the database
        List<GeoDivision> geoDivisionList = geoDivisionRepository.findAll().collectList().block();
        assertThat(geoDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateGeoDivisionWithPatch() throws Exception {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        int databaseSizeBeforeUpdate = geoDivisionRepository.findAll().collectList().block().size();

        // Update the geoDivision using partial update
        GeoDivision partialUpdatedGeoDivision = new GeoDivision();
        partialUpdatedGeoDivision.setId(geoDivision.getId());

        partialUpdatedGeoDivision.name(UPDATED_NAME).level(UPDATED_LEVEL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGeoDivision.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGeoDivision))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GeoDivision in the database
        List<GeoDivision> geoDivisionList = geoDivisionRepository.findAll().collectList().block();
        assertThat(geoDivisionList).hasSize(databaseSizeBeforeUpdate);
        GeoDivision testGeoDivision = geoDivisionList.get(geoDivisionList.size() - 1);
        assertThat(testGeoDivision.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGeoDivision.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testGeoDivision.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    void fullUpdateGeoDivisionWithPatch() throws Exception {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        int databaseSizeBeforeUpdate = geoDivisionRepository.findAll().collectList().block().size();

        // Update the geoDivision using partial update
        GeoDivision partialUpdatedGeoDivision = new GeoDivision();
        partialUpdatedGeoDivision.setId(geoDivision.getId());

        partialUpdatedGeoDivision.name(UPDATED_NAME).code(UPDATED_CODE).level(UPDATED_LEVEL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGeoDivision.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGeoDivision))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GeoDivision in the database
        List<GeoDivision> geoDivisionList = geoDivisionRepository.findAll().collectList().block();
        assertThat(geoDivisionList).hasSize(databaseSizeBeforeUpdate);
        GeoDivision testGeoDivision = geoDivisionList.get(geoDivisionList.size() - 1);
        assertThat(testGeoDivision.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGeoDivision.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testGeoDivision.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    void patchNonExistingGeoDivision() throws Exception {
        int databaseSizeBeforeUpdate = geoDivisionRepository.findAll().collectList().block().size();
        geoDivision.setId(longCount.incrementAndGet());

        // Create the GeoDivision
        GeoDivisionDTO geoDivisionDTO = geoDivisionMapper.toDto(geoDivision);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, geoDivisionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(geoDivisionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GeoDivision in the database
        List<GeoDivision> geoDivisionList = geoDivisionRepository.findAll().collectList().block();
        assertThat(geoDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchGeoDivision() throws Exception {
        int databaseSizeBeforeUpdate = geoDivisionRepository.findAll().collectList().block().size();
        geoDivision.setId(longCount.incrementAndGet());

        // Create the GeoDivision
        GeoDivisionDTO geoDivisionDTO = geoDivisionMapper.toDto(geoDivision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(geoDivisionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GeoDivision in the database
        List<GeoDivision> geoDivisionList = geoDivisionRepository.findAll().collectList().block();
        assertThat(geoDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamGeoDivision() throws Exception {
        int databaseSizeBeforeUpdate = geoDivisionRepository.findAll().collectList().block().size();
        geoDivision.setId(longCount.incrementAndGet());

        // Create the GeoDivision
        GeoDivisionDTO geoDivisionDTO = geoDivisionMapper.toDto(geoDivision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(geoDivisionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the GeoDivision in the database
        List<GeoDivision> geoDivisionList = geoDivisionRepository.findAll().collectList().block();
        assertThat(geoDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteGeoDivision() {
        // Initialize the database
        geoDivisionRepository.save(geoDivision).block();

        int databaseSizeBeforeDelete = geoDivisionRepository.findAll().collectList().block().size();

        // Delete the geoDivision
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, geoDivision.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<GeoDivision> geoDivisionList = geoDivisionRepository.findAll().collectList().block();
        assertThat(geoDivisionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
