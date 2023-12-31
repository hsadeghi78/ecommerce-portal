package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.VwClassification;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.repository.VwClassificationRepository;
import com.hs.ec.portal.service.dto.VwClassificationDTO;
import com.hs.ec.portal.service.mapper.VwClassificationMapper;
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
 * Integration tests for the {@link VwClassificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class VwClassificationResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CLASS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_LANGUAGE_CLASS_ID = 1L;
    private static final Long UPDATED_LANGUAGE_CLASS_ID = 2L;
    private static final Long SMALLER_LANGUAGE_CLASS_ID = 1L - 1L;

    private static final String DEFAULT_TYPE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_TYPE_CODE = 1;
    private static final Integer UPDATED_TYPE_CODE = 2;
    private static final Integer SMALLER_TYPE_CODE = 1 - 1;

    private static final String DEFAULT_TYPE_DESC = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_DESC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vw-classifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VwClassificationRepository vwClassificationRepository;

    @Autowired
    private VwClassificationMapper vwClassificationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private VwClassification vwClassification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VwClassification createEntity(EntityManager em) {
        VwClassification vwClassification = new VwClassification()
            .title(DEFAULT_TITLE)
            .classCode(DEFAULT_CLASS_CODE)
            .description(DEFAULT_DESCRIPTION)
            .languageClassId(DEFAULT_LANGUAGE_CLASS_ID)
            .typeTitle(DEFAULT_TYPE_TITLE)
            .typeCode(DEFAULT_TYPE_CODE)
            .typeDesc(DEFAULT_TYPE_DESC);
        return vwClassification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VwClassification createUpdatedEntity(EntityManager em) {
        VwClassification vwClassification = new VwClassification()
            .title(UPDATED_TITLE)
            .classCode(UPDATED_CLASS_CODE)
            .description(UPDATED_DESCRIPTION)
            .languageClassId(UPDATED_LANGUAGE_CLASS_ID)
            .typeTitle(UPDATED_TYPE_TITLE)
            .typeCode(UPDATED_TYPE_CODE)
            .typeDesc(UPDATED_TYPE_DESC);
        return vwClassification;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(VwClassification.class).block();
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
        vwClassification = createEntity(em);
    }

    @Test
    void createVwClassification() throws Exception {
        int databaseSizeBeforeCreate = vwClassificationRepository.findAll().collectList().block().size();
        // Create the VwClassification
        VwClassificationDTO vwClassificationDTO = vwClassificationMapper.toDto(vwClassification);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vwClassificationDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the VwClassification in the database
        List<VwClassification> vwClassificationList = vwClassificationRepository.findAll().collectList().block();
        assertThat(vwClassificationList).hasSize(databaseSizeBeforeCreate + 1);
        VwClassification testVwClassification = vwClassificationList.get(vwClassificationList.size() - 1);
        assertThat(testVwClassification.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testVwClassification.getClassCode()).isEqualTo(DEFAULT_CLASS_CODE);
        assertThat(testVwClassification.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVwClassification.getLanguageClassId()).isEqualTo(DEFAULT_LANGUAGE_CLASS_ID);
        assertThat(testVwClassification.getTypeTitle()).isEqualTo(DEFAULT_TYPE_TITLE);
        assertThat(testVwClassification.getTypeCode()).isEqualTo(DEFAULT_TYPE_CODE);
        assertThat(testVwClassification.getTypeDesc()).isEqualTo(DEFAULT_TYPE_DESC);
    }

    @Test
    void createVwClassificationWithExistingId() throws Exception {
        // Create the VwClassification with an existing ID
        vwClassification.setId(1L);
        VwClassificationDTO vwClassificationDTO = vwClassificationMapper.toDto(vwClassification);

        int databaseSizeBeforeCreate = vwClassificationRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vwClassificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VwClassification in the database
        List<VwClassification> vwClassificationList = vwClassificationRepository.findAll().collectList().block();
        assertThat(vwClassificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = vwClassificationRepository.findAll().collectList().block().size();
        // set the field null
        vwClassification.setTitle(null);

        // Create the VwClassification, which fails.
        VwClassificationDTO vwClassificationDTO = vwClassificationMapper.toDto(vwClassification);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vwClassificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<VwClassification> vwClassificationList = vwClassificationRepository.findAll().collectList().block();
        assertThat(vwClassificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkClassCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vwClassificationRepository.findAll().collectList().block().size();
        // set the field null
        vwClassification.setClassCode(null);

        // Create the VwClassification, which fails.
        VwClassificationDTO vwClassificationDTO = vwClassificationMapper.toDto(vwClassification);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vwClassificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<VwClassification> vwClassificationList = vwClassificationRepository.findAll().collectList().block();
        assertThat(vwClassificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLanguageClassIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = vwClassificationRepository.findAll().collectList().block().size();
        // set the field null
        vwClassification.setLanguageClassId(null);

        // Create the VwClassification, which fails.
        VwClassificationDTO vwClassificationDTO = vwClassificationMapper.toDto(vwClassification);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vwClassificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<VwClassification> vwClassificationList = vwClassificationRepository.findAll().collectList().block();
        assertThat(vwClassificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = vwClassificationRepository.findAll().collectList().block().size();
        // set the field null
        vwClassification.setTypeTitle(null);

        // Create the VwClassification, which fails.
        VwClassificationDTO vwClassificationDTO = vwClassificationMapper.toDto(vwClassification);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vwClassificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<VwClassification> vwClassificationList = vwClassificationRepository.findAll().collectList().block();
        assertThat(vwClassificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vwClassificationRepository.findAll().collectList().block().size();
        // set the field null
        vwClassification.setTypeCode(null);

        // Create the VwClassification, which fails.
        VwClassificationDTO vwClassificationDTO = vwClassificationMapper.toDto(vwClassification);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vwClassificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<VwClassification> vwClassificationList = vwClassificationRepository.findAll().collectList().block();
        assertThat(vwClassificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllVwClassifications() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList
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
            .value(hasItem(vwClassification.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].classCode")
            .value(hasItem(DEFAULT_CLASS_CODE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].languageClassId")
            .value(hasItem(DEFAULT_LANGUAGE_CLASS_ID.intValue()))
            .jsonPath("$.[*].typeTitle")
            .value(hasItem(DEFAULT_TYPE_TITLE))
            .jsonPath("$.[*].typeCode")
            .value(hasItem(DEFAULT_TYPE_CODE))
            .jsonPath("$.[*].typeDesc")
            .value(hasItem(DEFAULT_TYPE_DESC));
    }

    @Test
    void getVwClassification() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get the vwClassification
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, vwClassification.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(vwClassification.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.classCode")
            .value(is(DEFAULT_CLASS_CODE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.languageClassId")
            .value(is(DEFAULT_LANGUAGE_CLASS_ID.intValue()))
            .jsonPath("$.typeTitle")
            .value(is(DEFAULT_TYPE_TITLE))
            .jsonPath("$.typeCode")
            .value(is(DEFAULT_TYPE_CODE))
            .jsonPath("$.typeDesc")
            .value(is(DEFAULT_TYPE_DESC));
    }

    @Test
    void getVwClassificationsByIdFiltering() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        Long id = vwClassification.getId();

        defaultVwClassificationShouldBeFound("id.equals=" + id);
        defaultVwClassificationShouldNotBeFound("id.notEquals=" + id);

        defaultVwClassificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVwClassificationShouldNotBeFound("id.greaterThan=" + id);

        defaultVwClassificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVwClassificationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllVwClassificationsByTitleIsEqualToSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where title equals to DEFAULT_TITLE
        defaultVwClassificationShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the vwClassificationList where title equals to UPDATED_TITLE
        defaultVwClassificationShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    void getAllVwClassificationsByTitleIsInShouldWork() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultVwClassificationShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the vwClassificationList where title equals to UPDATED_TITLE
        defaultVwClassificationShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    void getAllVwClassificationsByTitleIsNullOrNotNull() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where title is not null
        defaultVwClassificationShouldBeFound("title.specified=true");

        // Get all the vwClassificationList where title is null
        defaultVwClassificationShouldNotBeFound("title.specified=false");
    }

    @Test
    void getAllVwClassificationsByTitleContainsSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where title contains DEFAULT_TITLE
        defaultVwClassificationShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the vwClassificationList where title contains UPDATED_TITLE
        defaultVwClassificationShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    void getAllVwClassificationsByTitleNotContainsSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where title does not contain DEFAULT_TITLE
        defaultVwClassificationShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the vwClassificationList where title does not contain UPDATED_TITLE
        defaultVwClassificationShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    void getAllVwClassificationsByClassCodeIsEqualToSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where classCode equals to DEFAULT_CLASS_CODE
        defaultVwClassificationShouldBeFound("classCode.equals=" + DEFAULT_CLASS_CODE);

        // Get all the vwClassificationList where classCode equals to UPDATED_CLASS_CODE
        defaultVwClassificationShouldNotBeFound("classCode.equals=" + UPDATED_CLASS_CODE);
    }

    @Test
    void getAllVwClassificationsByClassCodeIsInShouldWork() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where classCode in DEFAULT_CLASS_CODE or UPDATED_CLASS_CODE
        defaultVwClassificationShouldBeFound("classCode.in=" + DEFAULT_CLASS_CODE + "," + UPDATED_CLASS_CODE);

        // Get all the vwClassificationList where classCode equals to UPDATED_CLASS_CODE
        defaultVwClassificationShouldNotBeFound("classCode.in=" + UPDATED_CLASS_CODE);
    }

    @Test
    void getAllVwClassificationsByClassCodeIsNullOrNotNull() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where classCode is not null
        defaultVwClassificationShouldBeFound("classCode.specified=true");

        // Get all the vwClassificationList where classCode is null
        defaultVwClassificationShouldNotBeFound("classCode.specified=false");
    }

    @Test
    void getAllVwClassificationsByClassCodeContainsSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where classCode contains DEFAULT_CLASS_CODE
        defaultVwClassificationShouldBeFound("classCode.contains=" + DEFAULT_CLASS_CODE);

        // Get all the vwClassificationList where classCode contains UPDATED_CLASS_CODE
        defaultVwClassificationShouldNotBeFound("classCode.contains=" + UPDATED_CLASS_CODE);
    }

    @Test
    void getAllVwClassificationsByClassCodeNotContainsSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where classCode does not contain DEFAULT_CLASS_CODE
        defaultVwClassificationShouldNotBeFound("classCode.doesNotContain=" + DEFAULT_CLASS_CODE);

        // Get all the vwClassificationList where classCode does not contain UPDATED_CLASS_CODE
        defaultVwClassificationShouldBeFound("classCode.doesNotContain=" + UPDATED_CLASS_CODE);
    }

    @Test
    void getAllVwClassificationsByDescriptionIsEqualToSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where description equals to DEFAULT_DESCRIPTION
        defaultVwClassificationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the vwClassificationList where description equals to UPDATED_DESCRIPTION
        defaultVwClassificationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllVwClassificationsByDescriptionIsInShouldWork() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultVwClassificationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the vwClassificationList where description equals to UPDATED_DESCRIPTION
        defaultVwClassificationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllVwClassificationsByDescriptionIsNullOrNotNull() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where description is not null
        defaultVwClassificationShouldBeFound("description.specified=true");

        // Get all the vwClassificationList where description is null
        defaultVwClassificationShouldNotBeFound("description.specified=false");
    }

    @Test
    void getAllVwClassificationsByDescriptionContainsSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where description contains DEFAULT_DESCRIPTION
        defaultVwClassificationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the vwClassificationList where description contains UPDATED_DESCRIPTION
        defaultVwClassificationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllVwClassificationsByDescriptionNotContainsSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where description does not contain DEFAULT_DESCRIPTION
        defaultVwClassificationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the vwClassificationList where description does not contain UPDATED_DESCRIPTION
        defaultVwClassificationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllVwClassificationsByLanguageClassIdIsEqualToSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where languageClassId equals to DEFAULT_LANGUAGE_CLASS_ID
        defaultVwClassificationShouldBeFound("languageClassId.equals=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the vwClassificationList where languageClassId equals to UPDATED_LANGUAGE_CLASS_ID
        defaultVwClassificationShouldNotBeFound("languageClassId.equals=" + UPDATED_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllVwClassificationsByLanguageClassIdIsInShouldWork() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where languageClassId in DEFAULT_LANGUAGE_CLASS_ID or UPDATED_LANGUAGE_CLASS_ID
        defaultVwClassificationShouldBeFound("languageClassId.in=" + DEFAULT_LANGUAGE_CLASS_ID + "," + UPDATED_LANGUAGE_CLASS_ID);

        // Get all the vwClassificationList where languageClassId equals to UPDATED_LANGUAGE_CLASS_ID
        defaultVwClassificationShouldNotBeFound("languageClassId.in=" + UPDATED_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllVwClassificationsByLanguageClassIdIsNullOrNotNull() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where languageClassId is not null
        defaultVwClassificationShouldBeFound("languageClassId.specified=true");

        // Get all the vwClassificationList where languageClassId is null
        defaultVwClassificationShouldNotBeFound("languageClassId.specified=false");
    }

    @Test
    void getAllVwClassificationsByLanguageClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where languageClassId is greater than or equal to DEFAULT_LANGUAGE_CLASS_ID
        defaultVwClassificationShouldBeFound("languageClassId.greaterThanOrEqual=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the vwClassificationList where languageClassId is greater than or equal to UPDATED_LANGUAGE_CLASS_ID
        defaultVwClassificationShouldNotBeFound("languageClassId.greaterThanOrEqual=" + UPDATED_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllVwClassificationsByLanguageClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where languageClassId is less than or equal to DEFAULT_LANGUAGE_CLASS_ID
        defaultVwClassificationShouldBeFound("languageClassId.lessThanOrEqual=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the vwClassificationList where languageClassId is less than or equal to SMALLER_LANGUAGE_CLASS_ID
        defaultVwClassificationShouldNotBeFound("languageClassId.lessThanOrEqual=" + SMALLER_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllVwClassificationsByLanguageClassIdIsLessThanSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where languageClassId is less than DEFAULT_LANGUAGE_CLASS_ID
        defaultVwClassificationShouldNotBeFound("languageClassId.lessThan=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the vwClassificationList where languageClassId is less than UPDATED_LANGUAGE_CLASS_ID
        defaultVwClassificationShouldBeFound("languageClassId.lessThan=" + UPDATED_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllVwClassificationsByLanguageClassIdIsGreaterThanSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where languageClassId is greater than DEFAULT_LANGUAGE_CLASS_ID
        defaultVwClassificationShouldNotBeFound("languageClassId.greaterThan=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the vwClassificationList where languageClassId is greater than SMALLER_LANGUAGE_CLASS_ID
        defaultVwClassificationShouldBeFound("languageClassId.greaterThan=" + SMALLER_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllVwClassificationsByTypeTitleIsEqualToSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where typeTitle equals to DEFAULT_TYPE_TITLE
        defaultVwClassificationShouldBeFound("typeTitle.equals=" + DEFAULT_TYPE_TITLE);

        // Get all the vwClassificationList where typeTitle equals to UPDATED_TYPE_TITLE
        defaultVwClassificationShouldNotBeFound("typeTitle.equals=" + UPDATED_TYPE_TITLE);
    }

    @Test
    void getAllVwClassificationsByTypeTitleIsInShouldWork() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where typeTitle in DEFAULT_TYPE_TITLE or UPDATED_TYPE_TITLE
        defaultVwClassificationShouldBeFound("typeTitle.in=" + DEFAULT_TYPE_TITLE + "," + UPDATED_TYPE_TITLE);

        // Get all the vwClassificationList where typeTitle equals to UPDATED_TYPE_TITLE
        defaultVwClassificationShouldNotBeFound("typeTitle.in=" + UPDATED_TYPE_TITLE);
    }

    @Test
    void getAllVwClassificationsByTypeTitleIsNullOrNotNull() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where typeTitle is not null
        defaultVwClassificationShouldBeFound("typeTitle.specified=true");

        // Get all the vwClassificationList where typeTitle is null
        defaultVwClassificationShouldNotBeFound("typeTitle.specified=false");
    }

    @Test
    void getAllVwClassificationsByTypeTitleContainsSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where typeTitle contains DEFAULT_TYPE_TITLE
        defaultVwClassificationShouldBeFound("typeTitle.contains=" + DEFAULT_TYPE_TITLE);

        // Get all the vwClassificationList where typeTitle contains UPDATED_TYPE_TITLE
        defaultVwClassificationShouldNotBeFound("typeTitle.contains=" + UPDATED_TYPE_TITLE);
    }

    @Test
    void getAllVwClassificationsByTypeTitleNotContainsSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where typeTitle does not contain DEFAULT_TYPE_TITLE
        defaultVwClassificationShouldNotBeFound("typeTitle.doesNotContain=" + DEFAULT_TYPE_TITLE);

        // Get all the vwClassificationList where typeTitle does not contain UPDATED_TYPE_TITLE
        defaultVwClassificationShouldBeFound("typeTitle.doesNotContain=" + UPDATED_TYPE_TITLE);
    }

    @Test
    void getAllVwClassificationsByTypeCodeIsEqualToSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where typeCode equals to DEFAULT_TYPE_CODE
        defaultVwClassificationShouldBeFound("typeCode.equals=" + DEFAULT_TYPE_CODE);

        // Get all the vwClassificationList where typeCode equals to UPDATED_TYPE_CODE
        defaultVwClassificationShouldNotBeFound("typeCode.equals=" + UPDATED_TYPE_CODE);
    }

    @Test
    void getAllVwClassificationsByTypeCodeIsInShouldWork() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where typeCode in DEFAULT_TYPE_CODE or UPDATED_TYPE_CODE
        defaultVwClassificationShouldBeFound("typeCode.in=" + DEFAULT_TYPE_CODE + "," + UPDATED_TYPE_CODE);

        // Get all the vwClassificationList where typeCode equals to UPDATED_TYPE_CODE
        defaultVwClassificationShouldNotBeFound("typeCode.in=" + UPDATED_TYPE_CODE);
    }

    @Test
    void getAllVwClassificationsByTypeCodeIsNullOrNotNull() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where typeCode is not null
        defaultVwClassificationShouldBeFound("typeCode.specified=true");

        // Get all the vwClassificationList where typeCode is null
        defaultVwClassificationShouldNotBeFound("typeCode.specified=false");
    }

    @Test
    void getAllVwClassificationsByTypeCodeIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where typeCode is greater than or equal to DEFAULT_TYPE_CODE
        defaultVwClassificationShouldBeFound("typeCode.greaterThanOrEqual=" + DEFAULT_TYPE_CODE);

        // Get all the vwClassificationList where typeCode is greater than or equal to UPDATED_TYPE_CODE
        defaultVwClassificationShouldNotBeFound("typeCode.greaterThanOrEqual=" + UPDATED_TYPE_CODE);
    }

    @Test
    void getAllVwClassificationsByTypeCodeIsLessThanOrEqualToSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where typeCode is less than or equal to DEFAULT_TYPE_CODE
        defaultVwClassificationShouldBeFound("typeCode.lessThanOrEqual=" + DEFAULT_TYPE_CODE);

        // Get all the vwClassificationList where typeCode is less than or equal to SMALLER_TYPE_CODE
        defaultVwClassificationShouldNotBeFound("typeCode.lessThanOrEqual=" + SMALLER_TYPE_CODE);
    }

    @Test
    void getAllVwClassificationsByTypeCodeIsLessThanSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where typeCode is less than DEFAULT_TYPE_CODE
        defaultVwClassificationShouldNotBeFound("typeCode.lessThan=" + DEFAULT_TYPE_CODE);

        // Get all the vwClassificationList where typeCode is less than UPDATED_TYPE_CODE
        defaultVwClassificationShouldBeFound("typeCode.lessThan=" + UPDATED_TYPE_CODE);
    }

    @Test
    void getAllVwClassificationsByTypeCodeIsGreaterThanSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where typeCode is greater than DEFAULT_TYPE_CODE
        defaultVwClassificationShouldNotBeFound("typeCode.greaterThan=" + DEFAULT_TYPE_CODE);

        // Get all the vwClassificationList where typeCode is greater than SMALLER_TYPE_CODE
        defaultVwClassificationShouldBeFound("typeCode.greaterThan=" + SMALLER_TYPE_CODE);
    }

    @Test
    void getAllVwClassificationsByTypeDescIsEqualToSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where typeDesc equals to DEFAULT_TYPE_DESC
        defaultVwClassificationShouldBeFound("typeDesc.equals=" + DEFAULT_TYPE_DESC);

        // Get all the vwClassificationList where typeDesc equals to UPDATED_TYPE_DESC
        defaultVwClassificationShouldNotBeFound("typeDesc.equals=" + UPDATED_TYPE_DESC);
    }

    @Test
    void getAllVwClassificationsByTypeDescIsInShouldWork() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where typeDesc in DEFAULT_TYPE_DESC or UPDATED_TYPE_DESC
        defaultVwClassificationShouldBeFound("typeDesc.in=" + DEFAULT_TYPE_DESC + "," + UPDATED_TYPE_DESC);

        // Get all the vwClassificationList where typeDesc equals to UPDATED_TYPE_DESC
        defaultVwClassificationShouldNotBeFound("typeDesc.in=" + UPDATED_TYPE_DESC);
    }

    @Test
    void getAllVwClassificationsByTypeDescIsNullOrNotNull() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where typeDesc is not null
        defaultVwClassificationShouldBeFound("typeDesc.specified=true");

        // Get all the vwClassificationList where typeDesc is null
        defaultVwClassificationShouldNotBeFound("typeDesc.specified=false");
    }

    @Test
    void getAllVwClassificationsByTypeDescContainsSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where typeDesc contains DEFAULT_TYPE_DESC
        defaultVwClassificationShouldBeFound("typeDesc.contains=" + DEFAULT_TYPE_DESC);

        // Get all the vwClassificationList where typeDesc contains UPDATED_TYPE_DESC
        defaultVwClassificationShouldNotBeFound("typeDesc.contains=" + UPDATED_TYPE_DESC);
    }

    @Test
    void getAllVwClassificationsByTypeDescNotContainsSomething() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        // Get all the vwClassificationList where typeDesc does not contain DEFAULT_TYPE_DESC
        defaultVwClassificationShouldNotBeFound("typeDesc.doesNotContain=" + DEFAULT_TYPE_DESC);

        // Get all the vwClassificationList where typeDesc does not contain UPDATED_TYPE_DESC
        defaultVwClassificationShouldBeFound("typeDesc.doesNotContain=" + UPDATED_TYPE_DESC);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVwClassificationShouldBeFound(String filter) {
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
            .value(hasItem(vwClassification.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].classCode")
            .value(hasItem(DEFAULT_CLASS_CODE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].languageClassId")
            .value(hasItem(DEFAULT_LANGUAGE_CLASS_ID.intValue()))
            .jsonPath("$.[*].typeTitle")
            .value(hasItem(DEFAULT_TYPE_TITLE))
            .jsonPath("$.[*].typeCode")
            .value(hasItem(DEFAULT_TYPE_CODE))
            .jsonPath("$.[*].typeDesc")
            .value(hasItem(DEFAULT_TYPE_DESC));

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
    private void defaultVwClassificationShouldNotBeFound(String filter) {
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
    void getNonExistingVwClassification() {
        // Get the vwClassification
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingVwClassification() throws Exception {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        int databaseSizeBeforeUpdate = vwClassificationRepository.findAll().collectList().block().size();

        // Update the vwClassification
        VwClassification updatedVwClassification = vwClassificationRepository.findById(vwClassification.getId()).block();
        updatedVwClassification
            .title(UPDATED_TITLE)
            .classCode(UPDATED_CLASS_CODE)
            .description(UPDATED_DESCRIPTION)
            .languageClassId(UPDATED_LANGUAGE_CLASS_ID)
            .typeTitle(UPDATED_TYPE_TITLE)
            .typeCode(UPDATED_TYPE_CODE)
            .typeDesc(UPDATED_TYPE_DESC);
        VwClassificationDTO vwClassificationDTO = vwClassificationMapper.toDto(updatedVwClassification);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, vwClassificationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vwClassificationDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the VwClassification in the database
        List<VwClassification> vwClassificationList = vwClassificationRepository.findAll().collectList().block();
        assertThat(vwClassificationList).hasSize(databaseSizeBeforeUpdate);
        VwClassification testVwClassification = vwClassificationList.get(vwClassificationList.size() - 1);
        assertThat(testVwClassification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVwClassification.getClassCode()).isEqualTo(UPDATED_CLASS_CODE);
        assertThat(testVwClassification.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVwClassification.getLanguageClassId()).isEqualTo(UPDATED_LANGUAGE_CLASS_ID);
        assertThat(testVwClassification.getTypeTitle()).isEqualTo(UPDATED_TYPE_TITLE);
        assertThat(testVwClassification.getTypeCode()).isEqualTo(UPDATED_TYPE_CODE);
        assertThat(testVwClassification.getTypeDesc()).isEqualTo(UPDATED_TYPE_DESC);
    }

    @Test
    void putNonExistingVwClassification() throws Exception {
        int databaseSizeBeforeUpdate = vwClassificationRepository.findAll().collectList().block().size();
        vwClassification.setId(longCount.incrementAndGet());

        // Create the VwClassification
        VwClassificationDTO vwClassificationDTO = vwClassificationMapper.toDto(vwClassification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, vwClassificationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vwClassificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VwClassification in the database
        List<VwClassification> vwClassificationList = vwClassificationRepository.findAll().collectList().block();
        assertThat(vwClassificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchVwClassification() throws Exception {
        int databaseSizeBeforeUpdate = vwClassificationRepository.findAll().collectList().block().size();
        vwClassification.setId(longCount.incrementAndGet());

        // Create the VwClassification
        VwClassificationDTO vwClassificationDTO = vwClassificationMapper.toDto(vwClassification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vwClassificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VwClassification in the database
        List<VwClassification> vwClassificationList = vwClassificationRepository.findAll().collectList().block();
        assertThat(vwClassificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamVwClassification() throws Exception {
        int databaseSizeBeforeUpdate = vwClassificationRepository.findAll().collectList().block().size();
        vwClassification.setId(longCount.incrementAndGet());

        // Create the VwClassification
        VwClassificationDTO vwClassificationDTO = vwClassificationMapper.toDto(vwClassification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(vwClassificationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the VwClassification in the database
        List<VwClassification> vwClassificationList = vwClassificationRepository.findAll().collectList().block();
        assertThat(vwClassificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateVwClassificationWithPatch() throws Exception {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        int databaseSizeBeforeUpdate = vwClassificationRepository.findAll().collectList().block().size();

        // Update the vwClassification using partial update
        VwClassification partialUpdatedVwClassification = new VwClassification();
        partialUpdatedVwClassification.setId(vwClassification.getId());

        partialUpdatedVwClassification
            .title(UPDATED_TITLE)
            .classCode(UPDATED_CLASS_CODE)
            .description(UPDATED_DESCRIPTION)
            .languageClassId(UPDATED_LANGUAGE_CLASS_ID)
            .typeDesc(UPDATED_TYPE_DESC);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedVwClassification.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedVwClassification))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the VwClassification in the database
        List<VwClassification> vwClassificationList = vwClassificationRepository.findAll().collectList().block();
        assertThat(vwClassificationList).hasSize(databaseSizeBeforeUpdate);
        VwClassification testVwClassification = vwClassificationList.get(vwClassificationList.size() - 1);
        assertThat(testVwClassification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVwClassification.getClassCode()).isEqualTo(UPDATED_CLASS_CODE);
        assertThat(testVwClassification.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVwClassification.getLanguageClassId()).isEqualTo(UPDATED_LANGUAGE_CLASS_ID);
        assertThat(testVwClassification.getTypeTitle()).isEqualTo(DEFAULT_TYPE_TITLE);
        assertThat(testVwClassification.getTypeCode()).isEqualTo(DEFAULT_TYPE_CODE);
        assertThat(testVwClassification.getTypeDesc()).isEqualTo(UPDATED_TYPE_DESC);
    }

    @Test
    void fullUpdateVwClassificationWithPatch() throws Exception {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        int databaseSizeBeforeUpdate = vwClassificationRepository.findAll().collectList().block().size();

        // Update the vwClassification using partial update
        VwClassification partialUpdatedVwClassification = new VwClassification();
        partialUpdatedVwClassification.setId(vwClassification.getId());

        partialUpdatedVwClassification
            .title(UPDATED_TITLE)
            .classCode(UPDATED_CLASS_CODE)
            .description(UPDATED_DESCRIPTION)
            .languageClassId(UPDATED_LANGUAGE_CLASS_ID)
            .typeTitle(UPDATED_TYPE_TITLE)
            .typeCode(UPDATED_TYPE_CODE)
            .typeDesc(UPDATED_TYPE_DESC);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedVwClassification.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedVwClassification))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the VwClassification in the database
        List<VwClassification> vwClassificationList = vwClassificationRepository.findAll().collectList().block();
        assertThat(vwClassificationList).hasSize(databaseSizeBeforeUpdate);
        VwClassification testVwClassification = vwClassificationList.get(vwClassificationList.size() - 1);
        assertThat(testVwClassification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVwClassification.getClassCode()).isEqualTo(UPDATED_CLASS_CODE);
        assertThat(testVwClassification.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVwClassification.getLanguageClassId()).isEqualTo(UPDATED_LANGUAGE_CLASS_ID);
        assertThat(testVwClassification.getTypeTitle()).isEqualTo(UPDATED_TYPE_TITLE);
        assertThat(testVwClassification.getTypeCode()).isEqualTo(UPDATED_TYPE_CODE);
        assertThat(testVwClassification.getTypeDesc()).isEqualTo(UPDATED_TYPE_DESC);
    }

    @Test
    void patchNonExistingVwClassification() throws Exception {
        int databaseSizeBeforeUpdate = vwClassificationRepository.findAll().collectList().block().size();
        vwClassification.setId(longCount.incrementAndGet());

        // Create the VwClassification
        VwClassificationDTO vwClassificationDTO = vwClassificationMapper.toDto(vwClassification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, vwClassificationDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(vwClassificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VwClassification in the database
        List<VwClassification> vwClassificationList = vwClassificationRepository.findAll().collectList().block();
        assertThat(vwClassificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchVwClassification() throws Exception {
        int databaseSizeBeforeUpdate = vwClassificationRepository.findAll().collectList().block().size();
        vwClassification.setId(longCount.incrementAndGet());

        // Create the VwClassification
        VwClassificationDTO vwClassificationDTO = vwClassificationMapper.toDto(vwClassification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(vwClassificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VwClassification in the database
        List<VwClassification> vwClassificationList = vwClassificationRepository.findAll().collectList().block();
        assertThat(vwClassificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamVwClassification() throws Exception {
        int databaseSizeBeforeUpdate = vwClassificationRepository.findAll().collectList().block().size();
        vwClassification.setId(longCount.incrementAndGet());

        // Create the VwClassification
        VwClassificationDTO vwClassificationDTO = vwClassificationMapper.toDto(vwClassification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(vwClassificationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the VwClassification in the database
        List<VwClassification> vwClassificationList = vwClassificationRepository.findAll().collectList().block();
        assertThat(vwClassificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteVwClassification() {
        // Initialize the database
        vwClassificationRepository.save(vwClassification).block();

        int databaseSizeBeforeDelete = vwClassificationRepository.findAll().collectList().block().size();

        // Delete the vwClassification
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, vwClassification.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<VwClassification> vwClassificationList = vwClassificationRepository.findAll().collectList().block();
        assertThat(vwClassificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
