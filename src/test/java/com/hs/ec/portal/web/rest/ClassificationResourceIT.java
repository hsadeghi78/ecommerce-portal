package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.ClassType;
import com.hs.ec.portal.domain.Classification;
import com.hs.ec.portal.repository.ClassTypeRepository;
import com.hs.ec.portal.repository.ClassificationRepository;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.service.ClassificationService;
import com.hs.ec.portal.service.dto.ClassificationDTO;
import com.hs.ec.portal.service.mapper.ClassificationMapper;
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
 * Integration tests for the {@link ClassificationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ClassificationResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CLASS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_LANGUAGE_CLASS_ID = 1L;
    private static final Long UPDATED_LANGUAGE_CLASS_ID = 2L;
    private static final Long SMALLER_LANGUAGE_CLASS_ID = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/classifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClassificationRepository classificationRepository;

    @Mock
    private ClassificationRepository classificationRepositoryMock;

    @Autowired
    private ClassificationMapper classificationMapper;

    @Mock
    private ClassificationService classificationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Classification classification;

    @Autowired
    private ClassTypeRepository classTypeRepository;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classification createEntity(EntityManager em) {
        Classification classification = new Classification()
            .title(DEFAULT_TITLE)
            .classCode(DEFAULT_CLASS_CODE)
            .description(DEFAULT_DESCRIPTION)
            .languageClassId(DEFAULT_LANGUAGE_CLASS_ID);
        // Add required entity
        ClassType classType;
        classType = em.insert(ClassTypeResourceIT.createEntity(em)).block();
        classification.setClassType(classType);
        return classification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classification createUpdatedEntity(EntityManager em) {
        Classification classification = new Classification()
            .title(UPDATED_TITLE)
            .classCode(UPDATED_CLASS_CODE)
            .description(UPDATED_DESCRIPTION)
            .languageClassId(UPDATED_LANGUAGE_CLASS_ID);
        // Add required entity
        ClassType classType;
        classType = em.insert(ClassTypeResourceIT.createUpdatedEntity(em)).block();
        classification.setClassType(classType);
        return classification;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Classification.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        ClassTypeResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        classification = createEntity(em);
    }

    @Test
    void createClassification() throws Exception {
        int databaseSizeBeforeCreate = classificationRepository.findAll().collectList().block().size();
        // Create the Classification
        ClassificationDTO classificationDTO = classificationMapper.toDto(classification);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classificationDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeCreate + 1);
        Classification testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testClassification.getClassCode()).isEqualTo(DEFAULT_CLASS_CODE);
        assertThat(testClassification.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testClassification.getLanguageClassId()).isEqualTo(DEFAULT_LANGUAGE_CLASS_ID);
    }

    @Test
    void createClassificationWithExistingId() throws Exception {
        // Create the Classification with an existing ID
        classification.setId(1L);
        ClassificationDTO classificationDTO = classificationMapper.toDto(classification);

        int databaseSizeBeforeCreate = classificationRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = classificationRepository.findAll().collectList().block().size();
        // set the field null
        classification.setTitle(null);

        // Create the Classification, which fails.
        ClassificationDTO classificationDTO = classificationMapper.toDto(classification);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkClassCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = classificationRepository.findAll().collectList().block().size();
        // set the field null
        classification.setClassCode(null);

        // Create the Classification, which fails.
        ClassificationDTO classificationDTO = classificationMapper.toDto(classification);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLanguageClassIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = classificationRepository.findAll().collectList().block().size();
        // set the field null
        classification.setLanguageClassId(null);

        // Create the Classification, which fails.
        ClassificationDTO classificationDTO = classificationMapper.toDto(classification);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllClassifications() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList
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
            .value(hasItem(classification.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].classCode")
            .value(hasItem(DEFAULT_CLASS_CODE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].languageClassId")
            .value(hasItem(DEFAULT_LANGUAGE_CLASS_ID.intValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClassificationsWithEagerRelationshipsIsEnabled() {
        when(classificationServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(classificationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClassificationsWithEagerRelationshipsIsNotEnabled() {
        when(classificationServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(classificationRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getClassification() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get the classification
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, classification.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(classification.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.classCode")
            .value(is(DEFAULT_CLASS_CODE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.languageClassId")
            .value(is(DEFAULT_LANGUAGE_CLASS_ID.intValue()));
    }

    @Test
    void getClassificationsByIdFiltering() {
        // Initialize the database
        classificationRepository.save(classification).block();

        Long id = classification.getId();

        defaultClassificationShouldBeFound("id.equals=" + id);
        defaultClassificationShouldNotBeFound("id.notEquals=" + id);

        defaultClassificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClassificationShouldNotBeFound("id.greaterThan=" + id);

        defaultClassificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClassificationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllClassificationsByTitleIsEqualToSomething() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where title equals to DEFAULT_TITLE
        defaultClassificationShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the classificationList where title equals to UPDATED_TITLE
        defaultClassificationShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    void getAllClassificationsByTitleIsInShouldWork() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultClassificationShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the classificationList where title equals to UPDATED_TITLE
        defaultClassificationShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    void getAllClassificationsByTitleIsNullOrNotNull() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where title is not null
        defaultClassificationShouldBeFound("title.specified=true");

        // Get all the classificationList where title is null
        defaultClassificationShouldNotBeFound("title.specified=false");
    }

    @Test
    void getAllClassificationsByTitleContainsSomething() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where title contains DEFAULT_TITLE
        defaultClassificationShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the classificationList where title contains UPDATED_TITLE
        defaultClassificationShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    void getAllClassificationsByTitleNotContainsSomething() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where title does not contain DEFAULT_TITLE
        defaultClassificationShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the classificationList where title does not contain UPDATED_TITLE
        defaultClassificationShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    void getAllClassificationsByClassCodeIsEqualToSomething() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where classCode equals to DEFAULT_CLASS_CODE
        defaultClassificationShouldBeFound("classCode.equals=" + DEFAULT_CLASS_CODE);

        // Get all the classificationList where classCode equals to UPDATED_CLASS_CODE
        defaultClassificationShouldNotBeFound("classCode.equals=" + UPDATED_CLASS_CODE);
    }

    @Test
    void getAllClassificationsByClassCodeIsInShouldWork() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where classCode in DEFAULT_CLASS_CODE or UPDATED_CLASS_CODE
        defaultClassificationShouldBeFound("classCode.in=" + DEFAULT_CLASS_CODE + "," + UPDATED_CLASS_CODE);

        // Get all the classificationList where classCode equals to UPDATED_CLASS_CODE
        defaultClassificationShouldNotBeFound("classCode.in=" + UPDATED_CLASS_CODE);
    }

    @Test
    void getAllClassificationsByClassCodeIsNullOrNotNull() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where classCode is not null
        defaultClassificationShouldBeFound("classCode.specified=true");

        // Get all the classificationList where classCode is null
        defaultClassificationShouldNotBeFound("classCode.specified=false");
    }

    @Test
    void getAllClassificationsByClassCodeContainsSomething() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where classCode contains DEFAULT_CLASS_CODE
        defaultClassificationShouldBeFound("classCode.contains=" + DEFAULT_CLASS_CODE);

        // Get all the classificationList where classCode contains UPDATED_CLASS_CODE
        defaultClassificationShouldNotBeFound("classCode.contains=" + UPDATED_CLASS_CODE);
    }

    @Test
    void getAllClassificationsByClassCodeNotContainsSomething() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where classCode does not contain DEFAULT_CLASS_CODE
        defaultClassificationShouldNotBeFound("classCode.doesNotContain=" + DEFAULT_CLASS_CODE);

        // Get all the classificationList where classCode does not contain UPDATED_CLASS_CODE
        defaultClassificationShouldBeFound("classCode.doesNotContain=" + UPDATED_CLASS_CODE);
    }

    @Test
    void getAllClassificationsByDescriptionIsEqualToSomething() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where description equals to DEFAULT_DESCRIPTION
        defaultClassificationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the classificationList where description equals to UPDATED_DESCRIPTION
        defaultClassificationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllClassificationsByDescriptionIsInShouldWork() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultClassificationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the classificationList where description equals to UPDATED_DESCRIPTION
        defaultClassificationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllClassificationsByDescriptionIsNullOrNotNull() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where description is not null
        defaultClassificationShouldBeFound("description.specified=true");

        // Get all the classificationList where description is null
        defaultClassificationShouldNotBeFound("description.specified=false");
    }

    @Test
    void getAllClassificationsByDescriptionContainsSomething() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where description contains DEFAULT_DESCRIPTION
        defaultClassificationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the classificationList where description contains UPDATED_DESCRIPTION
        defaultClassificationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllClassificationsByDescriptionNotContainsSomething() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where description does not contain DEFAULT_DESCRIPTION
        defaultClassificationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the classificationList where description does not contain UPDATED_DESCRIPTION
        defaultClassificationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllClassificationsByLanguageClassIdIsEqualToSomething() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where languageClassId equals to DEFAULT_LANGUAGE_CLASS_ID
        defaultClassificationShouldBeFound("languageClassId.equals=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the classificationList where languageClassId equals to UPDATED_LANGUAGE_CLASS_ID
        defaultClassificationShouldNotBeFound("languageClassId.equals=" + UPDATED_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllClassificationsByLanguageClassIdIsInShouldWork() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where languageClassId in DEFAULT_LANGUAGE_CLASS_ID or UPDATED_LANGUAGE_CLASS_ID
        defaultClassificationShouldBeFound("languageClassId.in=" + DEFAULT_LANGUAGE_CLASS_ID + "," + UPDATED_LANGUAGE_CLASS_ID);

        // Get all the classificationList where languageClassId equals to UPDATED_LANGUAGE_CLASS_ID
        defaultClassificationShouldNotBeFound("languageClassId.in=" + UPDATED_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllClassificationsByLanguageClassIdIsNullOrNotNull() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where languageClassId is not null
        defaultClassificationShouldBeFound("languageClassId.specified=true");

        // Get all the classificationList where languageClassId is null
        defaultClassificationShouldNotBeFound("languageClassId.specified=false");
    }

    @Test
    void getAllClassificationsByLanguageClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where languageClassId is greater than or equal to DEFAULT_LANGUAGE_CLASS_ID
        defaultClassificationShouldBeFound("languageClassId.greaterThanOrEqual=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the classificationList where languageClassId is greater than or equal to UPDATED_LANGUAGE_CLASS_ID
        defaultClassificationShouldNotBeFound("languageClassId.greaterThanOrEqual=" + UPDATED_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllClassificationsByLanguageClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where languageClassId is less than or equal to DEFAULT_LANGUAGE_CLASS_ID
        defaultClassificationShouldBeFound("languageClassId.lessThanOrEqual=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the classificationList where languageClassId is less than or equal to SMALLER_LANGUAGE_CLASS_ID
        defaultClassificationShouldNotBeFound("languageClassId.lessThanOrEqual=" + SMALLER_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllClassificationsByLanguageClassIdIsLessThanSomething() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where languageClassId is less than DEFAULT_LANGUAGE_CLASS_ID
        defaultClassificationShouldNotBeFound("languageClassId.lessThan=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the classificationList where languageClassId is less than UPDATED_LANGUAGE_CLASS_ID
        defaultClassificationShouldBeFound("languageClassId.lessThan=" + UPDATED_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllClassificationsByLanguageClassIdIsGreaterThanSomething() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList where languageClassId is greater than DEFAULT_LANGUAGE_CLASS_ID
        defaultClassificationShouldNotBeFound("languageClassId.greaterThan=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the classificationList where languageClassId is greater than SMALLER_LANGUAGE_CLASS_ID
        defaultClassificationShouldBeFound("languageClassId.greaterThan=" + SMALLER_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllClassificationsByClassTypeIsEqualToSomething() {
        ClassType classType = ClassTypeResourceIT.createEntity(em);
        classTypeRepository.save(classType).block();
        Long classTypeId = classType.getId();
        classification.setClassTypeId(classTypeId);
        classificationRepository.save(classification).block();
        // Get all the classificationList where classType equals to classTypeId
        defaultClassificationShouldBeFound("classTypeId.equals=" + classTypeId);

        // Get all the classificationList where classType equals to (classTypeId + 1)
        defaultClassificationShouldNotBeFound("classTypeId.equals=" + (classTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassificationShouldBeFound(String filter) {
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
            .value(hasItem(classification.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].classCode")
            .value(hasItem(DEFAULT_CLASS_CODE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].languageClassId")
            .value(hasItem(DEFAULT_LANGUAGE_CLASS_ID.intValue()));

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
    private void defaultClassificationShouldNotBeFound(String filter) {
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
    void getNonExistingClassification() {
        // Get the classification
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingClassification() throws Exception {
        // Initialize the database
        classificationRepository.save(classification).block();

        int databaseSizeBeforeUpdate = classificationRepository.findAll().collectList().block().size();

        // Update the classification
        Classification updatedClassification = classificationRepository.findById(classification.getId()).block();
        updatedClassification
            .title(UPDATED_TITLE)
            .classCode(UPDATED_CLASS_CODE)
            .description(UPDATED_DESCRIPTION)
            .languageClassId(UPDATED_LANGUAGE_CLASS_ID);
        ClassificationDTO classificationDTO = classificationMapper.toDto(updatedClassification);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, classificationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classificationDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
        Classification testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testClassification.getClassCode()).isEqualTo(UPDATED_CLASS_CODE);
        assertThat(testClassification.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testClassification.getLanguageClassId()).isEqualTo(UPDATED_LANGUAGE_CLASS_ID);
    }

    @Test
    void putNonExistingClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().collectList().block().size();
        classification.setId(longCount.incrementAndGet());

        // Create the Classification
        ClassificationDTO classificationDTO = classificationMapper.toDto(classification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, classificationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().collectList().block().size();
        classification.setId(longCount.incrementAndGet());

        // Create the Classification
        ClassificationDTO classificationDTO = classificationMapper.toDto(classification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().collectList().block().size();
        classification.setId(longCount.incrementAndGet());

        // Create the Classification
        ClassificationDTO classificationDTO = classificationMapper.toDto(classification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classificationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateClassificationWithPatch() throws Exception {
        // Initialize the database
        classificationRepository.save(classification).block();

        int databaseSizeBeforeUpdate = classificationRepository.findAll().collectList().block().size();

        // Update the classification using partial update
        Classification partialUpdatedClassification = new Classification();
        partialUpdatedClassification.setId(classification.getId());

        partialUpdatedClassification.title(UPDATED_TITLE).classCode(UPDATED_CLASS_CODE).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedClassification.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedClassification))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
        Classification testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testClassification.getClassCode()).isEqualTo(UPDATED_CLASS_CODE);
        assertThat(testClassification.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testClassification.getLanguageClassId()).isEqualTo(DEFAULT_LANGUAGE_CLASS_ID);
    }

    @Test
    void fullUpdateClassificationWithPatch() throws Exception {
        // Initialize the database
        classificationRepository.save(classification).block();

        int databaseSizeBeforeUpdate = classificationRepository.findAll().collectList().block().size();

        // Update the classification using partial update
        Classification partialUpdatedClassification = new Classification();
        partialUpdatedClassification.setId(classification.getId());

        partialUpdatedClassification
            .title(UPDATED_TITLE)
            .classCode(UPDATED_CLASS_CODE)
            .description(UPDATED_DESCRIPTION)
            .languageClassId(UPDATED_LANGUAGE_CLASS_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedClassification.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedClassification))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
        Classification testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testClassification.getClassCode()).isEqualTo(UPDATED_CLASS_CODE);
        assertThat(testClassification.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testClassification.getLanguageClassId()).isEqualTo(UPDATED_LANGUAGE_CLASS_ID);
    }

    @Test
    void patchNonExistingClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().collectList().block().size();
        classification.setId(longCount.incrementAndGet());

        // Create the Classification
        ClassificationDTO classificationDTO = classificationMapper.toDto(classification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, classificationDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(classificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().collectList().block().size();
        classification.setId(longCount.incrementAndGet());

        // Create the Classification
        ClassificationDTO classificationDTO = classificationMapper.toDto(classification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(classificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().collectList().block().size();
        classification.setId(longCount.incrementAndGet());

        // Create the Classification
        ClassificationDTO classificationDTO = classificationMapper.toDto(classification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(classificationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteClassification() {
        // Initialize the database
        classificationRepository.save(classification).block();

        int databaseSizeBeforeDelete = classificationRepository.findAll().collectList().block().size();

        // Delete the classification
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, classification.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
