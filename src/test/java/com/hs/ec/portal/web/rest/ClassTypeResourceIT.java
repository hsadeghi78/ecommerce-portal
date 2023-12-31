package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.ClassType;
import com.hs.ec.portal.repository.ClassTypeRepository;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.service.dto.ClassTypeDTO;
import com.hs.ec.portal.service.mapper.ClassTypeMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ClassTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ClassTypeResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_TYPE_CODE = 1;
    private static final Integer UPDATED_TYPE_CODE = 2;
    private static final Integer SMALLER_TYPE_CODE = 1 - 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/class-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClassTypeRepository classTypeRepository;

    @Autowired
    private ClassTypeMapper classTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ClassType classType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassType createEntity(EntityManager em) {
        ClassType classType = new ClassType().title(DEFAULT_TITLE).typeCode(DEFAULT_TYPE_CODE).description(DEFAULT_DESCRIPTION);
        return classType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassType createUpdatedEntity(EntityManager em) {
        ClassType classType = new ClassType().title(UPDATED_TITLE).typeCode(UPDATED_TYPE_CODE).description(UPDATED_DESCRIPTION);
        return classType;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ClassType.class).block();
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
        classType = createEntity(em);
    }

    @Test
    void createClassType() throws Exception {
        int databaseSizeBeforeCreate = classTypeRepository.findAll().collectList().block().size();
        // Create the ClassType
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classType);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ClassType in the database
        List<ClassType> classTypeList = classTypeRepository.findAll().collectList().block();
        assertThat(classTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ClassType testClassType = classTypeList.get(classTypeList.size() - 1);
        assertThat(testClassType.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testClassType.getTypeCode()).isEqualTo(DEFAULT_TYPE_CODE);
        assertThat(testClassType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createClassTypeWithExistingId() throws Exception {
        // Create the ClassType with an existing ID
        classType.setId(1L);
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classType);

        int databaseSizeBeforeCreate = classTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ClassType in the database
        List<ClassType> classTypeList = classTypeRepository.findAll().collectList().block();
        assertThat(classTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = classTypeRepository.findAll().collectList().block().size();
        // set the field null
        classType.setTitle(null);

        // Create the ClassType, which fails.
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ClassType> classTypeList = classTypeRepository.findAll().collectList().block();
        assertThat(classTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = classTypeRepository.findAll().collectList().block().size();
        // set the field null
        classType.setTypeCode(null);

        // Create the ClassType, which fails.
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ClassType> classTypeList = classTypeRepository.findAll().collectList().block();
        assertThat(classTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllClassTypes() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        // Get all the classTypeList
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
            .value(hasItem(classType.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].typeCode")
            .value(hasItem(DEFAULT_TYPE_CODE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getClassType() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        // Get the classType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, classType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(classType.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.typeCode")
            .value(is(DEFAULT_TYPE_CODE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getClassTypesByIdFiltering() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        Long id = classType.getId();

        defaultClassTypeShouldBeFound("id.equals=" + id);
        defaultClassTypeShouldNotBeFound("id.notEquals=" + id);

        defaultClassTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClassTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultClassTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClassTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllClassTypesByTitleIsEqualToSomething() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        // Get all the classTypeList where title equals to DEFAULT_TITLE
        defaultClassTypeShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the classTypeList where title equals to UPDATED_TITLE
        defaultClassTypeShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    void getAllClassTypesByTitleIsInShouldWork() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        // Get all the classTypeList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultClassTypeShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the classTypeList where title equals to UPDATED_TITLE
        defaultClassTypeShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    void getAllClassTypesByTitleIsNullOrNotNull() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        // Get all the classTypeList where title is not null
        defaultClassTypeShouldBeFound("title.specified=true");

        // Get all the classTypeList where title is null
        defaultClassTypeShouldNotBeFound("title.specified=false");
    }

    @Test
    void getAllClassTypesByTitleContainsSomething() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        // Get all the classTypeList where title contains DEFAULT_TITLE
        defaultClassTypeShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the classTypeList where title contains UPDATED_TITLE
        defaultClassTypeShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    void getAllClassTypesByTitleNotContainsSomething() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        // Get all the classTypeList where title does not contain DEFAULT_TITLE
        defaultClassTypeShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the classTypeList where title does not contain UPDATED_TITLE
        defaultClassTypeShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    void getAllClassTypesByTypeCodeIsEqualToSomething() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        // Get all the classTypeList where typeCode equals to DEFAULT_TYPE_CODE
        defaultClassTypeShouldBeFound("typeCode.equals=" + DEFAULT_TYPE_CODE);

        // Get all the classTypeList where typeCode equals to UPDATED_TYPE_CODE
        defaultClassTypeShouldNotBeFound("typeCode.equals=" + UPDATED_TYPE_CODE);
    }

    @Test
    void getAllClassTypesByTypeCodeIsInShouldWork() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        // Get all the classTypeList where typeCode in DEFAULT_TYPE_CODE or UPDATED_TYPE_CODE
        defaultClassTypeShouldBeFound("typeCode.in=" + DEFAULT_TYPE_CODE + "," + UPDATED_TYPE_CODE);

        // Get all the classTypeList where typeCode equals to UPDATED_TYPE_CODE
        defaultClassTypeShouldNotBeFound("typeCode.in=" + UPDATED_TYPE_CODE);
    }

    @Test
    void getAllClassTypesByTypeCodeIsNullOrNotNull() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        // Get all the classTypeList where typeCode is not null
        defaultClassTypeShouldBeFound("typeCode.specified=true");

        // Get all the classTypeList where typeCode is null
        defaultClassTypeShouldNotBeFound("typeCode.specified=false");
    }

    @Test
    void getAllClassTypesByTypeCodeIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        // Get all the classTypeList where typeCode is greater than or equal to DEFAULT_TYPE_CODE
        defaultClassTypeShouldBeFound("typeCode.greaterThanOrEqual=" + DEFAULT_TYPE_CODE);

        // Get all the classTypeList where typeCode is greater than or equal to UPDATED_TYPE_CODE
        defaultClassTypeShouldNotBeFound("typeCode.greaterThanOrEqual=" + UPDATED_TYPE_CODE);
    }

    @Test
    void getAllClassTypesByTypeCodeIsLessThanOrEqualToSomething() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        // Get all the classTypeList where typeCode is less than or equal to DEFAULT_TYPE_CODE
        defaultClassTypeShouldBeFound("typeCode.lessThanOrEqual=" + DEFAULT_TYPE_CODE);

        // Get all the classTypeList where typeCode is less than or equal to SMALLER_TYPE_CODE
        defaultClassTypeShouldNotBeFound("typeCode.lessThanOrEqual=" + SMALLER_TYPE_CODE);
    }

    @Test
    void getAllClassTypesByTypeCodeIsLessThanSomething() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        // Get all the classTypeList where typeCode is less than DEFAULT_TYPE_CODE
        defaultClassTypeShouldNotBeFound("typeCode.lessThan=" + DEFAULT_TYPE_CODE);

        // Get all the classTypeList where typeCode is less than UPDATED_TYPE_CODE
        defaultClassTypeShouldBeFound("typeCode.lessThan=" + UPDATED_TYPE_CODE);
    }

    @Test
    void getAllClassTypesByTypeCodeIsGreaterThanSomething() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        // Get all the classTypeList where typeCode is greater than DEFAULT_TYPE_CODE
        defaultClassTypeShouldNotBeFound("typeCode.greaterThan=" + DEFAULT_TYPE_CODE);

        // Get all the classTypeList where typeCode is greater than SMALLER_TYPE_CODE
        defaultClassTypeShouldBeFound("typeCode.greaterThan=" + SMALLER_TYPE_CODE);
    }

    @Test
    void getAllClassTypesByDescriptionIsEqualToSomething() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        // Get all the classTypeList where description equals to DEFAULT_DESCRIPTION
        defaultClassTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the classTypeList where description equals to UPDATED_DESCRIPTION
        defaultClassTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllClassTypesByDescriptionIsInShouldWork() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        // Get all the classTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultClassTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the classTypeList where description equals to UPDATED_DESCRIPTION
        defaultClassTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllClassTypesByDescriptionIsNullOrNotNull() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        // Get all the classTypeList where description is not null
        defaultClassTypeShouldBeFound("description.specified=true");

        // Get all the classTypeList where description is null
        defaultClassTypeShouldNotBeFound("description.specified=false");
    }

    @Test
    void getAllClassTypesByDescriptionContainsSomething() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        // Get all the classTypeList where description contains DEFAULT_DESCRIPTION
        defaultClassTypeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the classTypeList where description contains UPDATED_DESCRIPTION
        defaultClassTypeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllClassTypesByDescriptionNotContainsSomething() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        // Get all the classTypeList where description does not contain DEFAULT_DESCRIPTION
        defaultClassTypeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the classTypeList where description does not contain UPDATED_DESCRIPTION
        defaultClassTypeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassTypeShouldBeFound(String filter) {
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
            .value(hasItem(classType.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].typeCode")
            .value(hasItem(DEFAULT_TYPE_CODE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));

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
    private void defaultClassTypeShouldNotBeFound(String filter) {
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
    void getNonExistingClassType() {
        // Get the classType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingClassType() throws Exception {
        // Initialize the database
        classTypeRepository.save(classType).block();

        int databaseSizeBeforeUpdate = classTypeRepository.findAll().collectList().block().size();

        // Update the classType
        ClassType updatedClassType = classTypeRepository.findById(classType.getId()).block();
        updatedClassType.title(UPDATED_TITLE).typeCode(UPDATED_TYPE_CODE).description(UPDATED_DESCRIPTION);
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(updatedClassType);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, classTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ClassType in the database
        List<ClassType> classTypeList = classTypeRepository.findAll().collectList().block();
        assertThat(classTypeList).hasSize(databaseSizeBeforeUpdate);
        ClassType testClassType = classTypeList.get(classTypeList.size() - 1);
        assertThat(testClassType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testClassType.getTypeCode()).isEqualTo(UPDATED_TYPE_CODE);
        assertThat(testClassType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingClassType() throws Exception {
        int databaseSizeBeforeUpdate = classTypeRepository.findAll().collectList().block().size();
        classType.setId(longCount.incrementAndGet());

        // Create the ClassType
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, classTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ClassType in the database
        List<ClassType> classTypeList = classTypeRepository.findAll().collectList().block();
        assertThat(classTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchClassType() throws Exception {
        int databaseSizeBeforeUpdate = classTypeRepository.findAll().collectList().block().size();
        classType.setId(longCount.incrementAndGet());

        // Create the ClassType
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ClassType in the database
        List<ClassType> classTypeList = classTypeRepository.findAll().collectList().block();
        assertThat(classTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamClassType() throws Exception {
        int databaseSizeBeforeUpdate = classTypeRepository.findAll().collectList().block().size();
        classType.setId(longCount.incrementAndGet());

        // Create the ClassType
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ClassType in the database
        List<ClassType> classTypeList = classTypeRepository.findAll().collectList().block();
        assertThat(classTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateClassTypeWithPatch() throws Exception {
        // Initialize the database
        classTypeRepository.save(classType).block();

        int databaseSizeBeforeUpdate = classTypeRepository.findAll().collectList().block().size();

        // Update the classType using partial update
        ClassType partialUpdatedClassType = new ClassType();
        partialUpdatedClassType.setId(classType.getId());

        partialUpdatedClassType.description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedClassType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedClassType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ClassType in the database
        List<ClassType> classTypeList = classTypeRepository.findAll().collectList().block();
        assertThat(classTypeList).hasSize(databaseSizeBeforeUpdate);
        ClassType testClassType = classTypeList.get(classTypeList.size() - 1);
        assertThat(testClassType.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testClassType.getTypeCode()).isEqualTo(DEFAULT_TYPE_CODE);
        assertThat(testClassType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void fullUpdateClassTypeWithPatch() throws Exception {
        // Initialize the database
        classTypeRepository.save(classType).block();

        int databaseSizeBeforeUpdate = classTypeRepository.findAll().collectList().block().size();

        // Update the classType using partial update
        ClassType partialUpdatedClassType = new ClassType();
        partialUpdatedClassType.setId(classType.getId());

        partialUpdatedClassType.title(UPDATED_TITLE).typeCode(UPDATED_TYPE_CODE).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedClassType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedClassType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ClassType in the database
        List<ClassType> classTypeList = classTypeRepository.findAll().collectList().block();
        assertThat(classTypeList).hasSize(databaseSizeBeforeUpdate);
        ClassType testClassType = classTypeList.get(classTypeList.size() - 1);
        assertThat(testClassType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testClassType.getTypeCode()).isEqualTo(UPDATED_TYPE_CODE);
        assertThat(testClassType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingClassType() throws Exception {
        int databaseSizeBeforeUpdate = classTypeRepository.findAll().collectList().block().size();
        classType.setId(longCount.incrementAndGet());

        // Create the ClassType
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, classTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(classTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ClassType in the database
        List<ClassType> classTypeList = classTypeRepository.findAll().collectList().block();
        assertThat(classTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchClassType() throws Exception {
        int databaseSizeBeforeUpdate = classTypeRepository.findAll().collectList().block().size();
        classType.setId(longCount.incrementAndGet());

        // Create the ClassType
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(classTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ClassType in the database
        List<ClassType> classTypeList = classTypeRepository.findAll().collectList().block();
        assertThat(classTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamClassType() throws Exception {
        int databaseSizeBeforeUpdate = classTypeRepository.findAll().collectList().block().size();
        classType.setId(longCount.incrementAndGet());

        // Create the ClassType
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(classTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ClassType in the database
        List<ClassType> classTypeList = classTypeRepository.findAll().collectList().block();
        assertThat(classTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteClassType() {
        // Initialize the database
        classTypeRepository.save(classType).block();

        int databaseSizeBeforeDelete = classTypeRepository.findAll().collectList().block().size();

        // Delete the classType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, classType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ClassType> classTypeList = classTypeRepository.findAll().collectList().block();
        assertThat(classTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
