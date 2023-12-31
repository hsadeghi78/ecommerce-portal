package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.Agreement;
import com.hs.ec.portal.domain.Party;
import com.hs.ec.portal.repository.AgreementRepository;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.repository.PartyRepository;
import com.hs.ec.portal.repository.PartyRepository;
import com.hs.ec.portal.service.AgreementService;
import com.hs.ec.portal.service.dto.AgreementDTO;
import com.hs.ec.portal.service.mapper.AgreementMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AgreementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AgreementResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Long DEFAULT_ACTIVATION_STATUS_CLASS_ID = 1L;
    private static final Long UPDATED_ACTIVATION_STATUS_CLASS_ID = 2L;
    private static final Long SMALLER_ACTIVATION_STATUS_CLASS_ID = 1L - 1L;

    private static final Double DEFAULT_INFRASTRUCTURE_BENEFIT = 1D;
    private static final Double UPDATED_INFRASTRUCTURE_BENEFIT = 2D;
    private static final Double SMALLER_INFRASTRUCTURE_BENEFIT = 1D - 1D;

    private static final Double DEFAULT_EXTRA_BENEFIT = 1D;
    private static final Double UPDATED_EXTRA_BENEFIT = 2D;
    private static final Double SMALLER_EXTRA_BENEFIT = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/agreements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AgreementRepository agreementRepository;

    @Mock
    private AgreementRepository agreementRepositoryMock;

    @Autowired
    private AgreementMapper agreementMapper;

    @Mock
    private AgreementService agreementServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Agreement agreement;

    @Autowired
    private PartyRepository partyRepository;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agreement createEntity(EntityManager em) {
        Agreement agreement = new Agreement()
            .name(DEFAULT_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .activationStatusClassId(DEFAULT_ACTIVATION_STATUS_CLASS_ID)
            .infrastructureBenefit(DEFAULT_INFRASTRUCTURE_BENEFIT)
            .extraBenefit(DEFAULT_EXTRA_BENEFIT);
        return agreement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agreement createUpdatedEntity(EntityManager em) {
        Agreement agreement = new Agreement()
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .activationStatusClassId(UPDATED_ACTIVATION_STATUS_CLASS_ID)
            .infrastructureBenefit(UPDATED_INFRASTRUCTURE_BENEFIT)
            .extraBenefit(UPDATED_EXTRA_BENEFIT);
        return agreement;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Agreement.class).block();
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
        agreement = createEntity(em);
    }

    @Test
    void createAgreement() throws Exception {
        int databaseSizeBeforeCreate = agreementRepository.findAll().collectList().block().size();
        // Create the Agreement
        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agreementDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Agreement in the database
        List<Agreement> agreementList = agreementRepository.findAll().collectList().block();
        assertThat(agreementList).hasSize(databaseSizeBeforeCreate + 1);
        Agreement testAgreement = agreementList.get(agreementList.size() - 1);
        assertThat(testAgreement.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAgreement.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testAgreement.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testAgreement.getActivationStatusClassId()).isEqualTo(DEFAULT_ACTIVATION_STATUS_CLASS_ID);
        assertThat(testAgreement.getInfrastructureBenefit()).isEqualTo(DEFAULT_INFRASTRUCTURE_BENEFIT);
        assertThat(testAgreement.getExtraBenefit()).isEqualTo(DEFAULT_EXTRA_BENEFIT);
    }

    @Test
    void createAgreementWithExistingId() throws Exception {
        // Create the Agreement with an existing ID
        agreement.setId(1L);
        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);

        int databaseSizeBeforeCreate = agreementRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agreementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Agreement in the database
        List<Agreement> agreementList = agreementRepository.findAll().collectList().block();
        assertThat(agreementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = agreementRepository.findAll().collectList().block().size();
        // set the field null
        agreement.setName(null);

        // Create the Agreement, which fails.
        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agreementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Agreement> agreementList = agreementRepository.findAll().collectList().block();
        assertThat(agreementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = agreementRepository.findAll().collectList().block().size();
        // set the field null
        agreement.setStartDate(null);

        // Create the Agreement, which fails.
        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agreementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Agreement> agreementList = agreementRepository.findAll().collectList().block();
        assertThat(agreementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = agreementRepository.findAll().collectList().block().size();
        // set the field null
        agreement.setEndDate(null);

        // Create the Agreement, which fails.
        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agreementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Agreement> agreementList = agreementRepository.findAll().collectList().block();
        assertThat(agreementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkActivationStatusClassIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = agreementRepository.findAll().collectList().block().size();
        // set the field null
        agreement.setActivationStatusClassId(null);

        // Create the Agreement, which fails.
        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agreementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Agreement> agreementList = agreementRepository.findAll().collectList().block();
        assertThat(agreementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkInfrastructureBenefitIsRequired() throws Exception {
        int databaseSizeBeforeTest = agreementRepository.findAll().collectList().block().size();
        // set the field null
        agreement.setInfrastructureBenefit(null);

        // Create the Agreement, which fails.
        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agreementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Agreement> agreementList = agreementRepository.findAll().collectList().block();
        assertThat(agreementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAgreements() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList
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
            .value(hasItem(agreement.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].startDate")
            .value(hasItem(DEFAULT_START_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
            .jsonPath("$.[*].activationStatusClassId")
            .value(hasItem(DEFAULT_ACTIVATION_STATUS_CLASS_ID.intValue()))
            .jsonPath("$.[*].infrastructureBenefit")
            .value(hasItem(DEFAULT_INFRASTRUCTURE_BENEFIT.doubleValue()))
            .jsonPath("$.[*].extraBenefit")
            .value(hasItem(DEFAULT_EXTRA_BENEFIT.doubleValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAgreementsWithEagerRelationshipsIsEnabled() {
        when(agreementServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(agreementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAgreementsWithEagerRelationshipsIsNotEnabled() {
        when(agreementServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(agreementRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getAgreement() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get the agreement
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, agreement.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(agreement.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.startDate")
            .value(is(DEFAULT_START_DATE.toString()))
            .jsonPath("$.endDate")
            .value(is(DEFAULT_END_DATE.toString()))
            .jsonPath("$.activationStatusClassId")
            .value(is(DEFAULT_ACTIVATION_STATUS_CLASS_ID.intValue()))
            .jsonPath("$.infrastructureBenefit")
            .value(is(DEFAULT_INFRASTRUCTURE_BENEFIT.doubleValue()))
            .jsonPath("$.extraBenefit")
            .value(is(DEFAULT_EXTRA_BENEFIT.doubleValue()));
    }

    @Test
    void getAgreementsByIdFiltering() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        Long id = agreement.getId();

        defaultAgreementShouldBeFound("id.equals=" + id);
        defaultAgreementShouldNotBeFound("id.notEquals=" + id);

        defaultAgreementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAgreementShouldNotBeFound("id.greaterThan=" + id);

        defaultAgreementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAgreementShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllAgreementsByNameIsEqualToSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where name equals to DEFAULT_NAME
        defaultAgreementShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the agreementList where name equals to UPDATED_NAME
        defaultAgreementShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    void getAllAgreementsByNameIsInShouldWork() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAgreementShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the agreementList where name equals to UPDATED_NAME
        defaultAgreementShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    void getAllAgreementsByNameIsNullOrNotNull() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where name is not null
        defaultAgreementShouldBeFound("name.specified=true");

        // Get all the agreementList where name is null
        defaultAgreementShouldNotBeFound("name.specified=false");
    }

    @Test
    void getAllAgreementsByNameContainsSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where name contains DEFAULT_NAME
        defaultAgreementShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the agreementList where name contains UPDATED_NAME
        defaultAgreementShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    void getAllAgreementsByNameNotContainsSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where name does not contain DEFAULT_NAME
        defaultAgreementShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the agreementList where name does not contain UPDATED_NAME
        defaultAgreementShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    void getAllAgreementsByStartDateIsEqualToSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where startDate equals to DEFAULT_START_DATE
        defaultAgreementShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the agreementList where startDate equals to UPDATED_START_DATE
        defaultAgreementShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    void getAllAgreementsByStartDateIsInShouldWork() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultAgreementShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the agreementList where startDate equals to UPDATED_START_DATE
        defaultAgreementShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    void getAllAgreementsByStartDateIsNullOrNotNull() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where startDate is not null
        defaultAgreementShouldBeFound("startDate.specified=true");

        // Get all the agreementList where startDate is null
        defaultAgreementShouldNotBeFound("startDate.specified=false");
    }

    @Test
    void getAllAgreementsByStartDateIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultAgreementShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the agreementList where startDate is greater than or equal to UPDATED_START_DATE
        defaultAgreementShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    void getAllAgreementsByStartDateIsLessThanOrEqualToSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where startDate is less than or equal to DEFAULT_START_DATE
        defaultAgreementShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the agreementList where startDate is less than or equal to SMALLER_START_DATE
        defaultAgreementShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    void getAllAgreementsByStartDateIsLessThanSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where startDate is less than DEFAULT_START_DATE
        defaultAgreementShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the agreementList where startDate is less than UPDATED_START_DATE
        defaultAgreementShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    void getAllAgreementsByStartDateIsGreaterThanSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where startDate is greater than DEFAULT_START_DATE
        defaultAgreementShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the agreementList where startDate is greater than SMALLER_START_DATE
        defaultAgreementShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    void getAllAgreementsByEndDateIsEqualToSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where endDate equals to DEFAULT_END_DATE
        defaultAgreementShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the agreementList where endDate equals to UPDATED_END_DATE
        defaultAgreementShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    void getAllAgreementsByEndDateIsInShouldWork() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultAgreementShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the agreementList where endDate equals to UPDATED_END_DATE
        defaultAgreementShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    void getAllAgreementsByEndDateIsNullOrNotNull() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where endDate is not null
        defaultAgreementShouldBeFound("endDate.specified=true");

        // Get all the agreementList where endDate is null
        defaultAgreementShouldNotBeFound("endDate.specified=false");
    }

    @Test
    void getAllAgreementsByEndDateIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultAgreementShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the agreementList where endDate is greater than or equal to UPDATED_END_DATE
        defaultAgreementShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    void getAllAgreementsByEndDateIsLessThanOrEqualToSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where endDate is less than or equal to DEFAULT_END_DATE
        defaultAgreementShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the agreementList where endDate is less than or equal to SMALLER_END_DATE
        defaultAgreementShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    void getAllAgreementsByEndDateIsLessThanSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where endDate is less than DEFAULT_END_DATE
        defaultAgreementShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the agreementList where endDate is less than UPDATED_END_DATE
        defaultAgreementShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    void getAllAgreementsByEndDateIsGreaterThanSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where endDate is greater than DEFAULT_END_DATE
        defaultAgreementShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the agreementList where endDate is greater than SMALLER_END_DATE
        defaultAgreementShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    void getAllAgreementsByActivationStatusClassIdIsEqualToSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where activationStatusClassId equals to DEFAULT_ACTIVATION_STATUS_CLASS_ID
        defaultAgreementShouldBeFound("activationStatusClassId.equals=" + DEFAULT_ACTIVATION_STATUS_CLASS_ID);

        // Get all the agreementList where activationStatusClassId equals to UPDATED_ACTIVATION_STATUS_CLASS_ID
        defaultAgreementShouldNotBeFound("activationStatusClassId.equals=" + UPDATED_ACTIVATION_STATUS_CLASS_ID);
    }

    @Test
    void getAllAgreementsByActivationStatusClassIdIsInShouldWork() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where activationStatusClassId in DEFAULT_ACTIVATION_STATUS_CLASS_ID or UPDATED_ACTIVATION_STATUS_CLASS_ID
        defaultAgreementShouldBeFound(
            "activationStatusClassId.in=" + DEFAULT_ACTIVATION_STATUS_CLASS_ID + "," + UPDATED_ACTIVATION_STATUS_CLASS_ID
        );

        // Get all the agreementList where activationStatusClassId equals to UPDATED_ACTIVATION_STATUS_CLASS_ID
        defaultAgreementShouldNotBeFound("activationStatusClassId.in=" + UPDATED_ACTIVATION_STATUS_CLASS_ID);
    }

    @Test
    void getAllAgreementsByActivationStatusClassIdIsNullOrNotNull() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where activationStatusClassId is not null
        defaultAgreementShouldBeFound("activationStatusClassId.specified=true");

        // Get all the agreementList where activationStatusClassId is null
        defaultAgreementShouldNotBeFound("activationStatusClassId.specified=false");
    }

    @Test
    void getAllAgreementsByActivationStatusClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where activationStatusClassId is greater than or equal to DEFAULT_ACTIVATION_STATUS_CLASS_ID
        defaultAgreementShouldBeFound("activationStatusClassId.greaterThanOrEqual=" + DEFAULT_ACTIVATION_STATUS_CLASS_ID);

        // Get all the agreementList where activationStatusClassId is greater than or equal to UPDATED_ACTIVATION_STATUS_CLASS_ID
        defaultAgreementShouldNotBeFound("activationStatusClassId.greaterThanOrEqual=" + UPDATED_ACTIVATION_STATUS_CLASS_ID);
    }

    @Test
    void getAllAgreementsByActivationStatusClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where activationStatusClassId is less than or equal to DEFAULT_ACTIVATION_STATUS_CLASS_ID
        defaultAgreementShouldBeFound("activationStatusClassId.lessThanOrEqual=" + DEFAULT_ACTIVATION_STATUS_CLASS_ID);

        // Get all the agreementList where activationStatusClassId is less than or equal to SMALLER_ACTIVATION_STATUS_CLASS_ID
        defaultAgreementShouldNotBeFound("activationStatusClassId.lessThanOrEqual=" + SMALLER_ACTIVATION_STATUS_CLASS_ID);
    }

    @Test
    void getAllAgreementsByActivationStatusClassIdIsLessThanSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where activationStatusClassId is less than DEFAULT_ACTIVATION_STATUS_CLASS_ID
        defaultAgreementShouldNotBeFound("activationStatusClassId.lessThan=" + DEFAULT_ACTIVATION_STATUS_CLASS_ID);

        // Get all the agreementList where activationStatusClassId is less than UPDATED_ACTIVATION_STATUS_CLASS_ID
        defaultAgreementShouldBeFound("activationStatusClassId.lessThan=" + UPDATED_ACTIVATION_STATUS_CLASS_ID);
    }

    @Test
    void getAllAgreementsByActivationStatusClassIdIsGreaterThanSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where activationStatusClassId is greater than DEFAULT_ACTIVATION_STATUS_CLASS_ID
        defaultAgreementShouldNotBeFound("activationStatusClassId.greaterThan=" + DEFAULT_ACTIVATION_STATUS_CLASS_ID);

        // Get all the agreementList where activationStatusClassId is greater than SMALLER_ACTIVATION_STATUS_CLASS_ID
        defaultAgreementShouldBeFound("activationStatusClassId.greaterThan=" + SMALLER_ACTIVATION_STATUS_CLASS_ID);
    }

    @Test
    void getAllAgreementsByInfrastructureBenefitIsEqualToSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where infrastructureBenefit equals to DEFAULT_INFRASTRUCTURE_BENEFIT
        defaultAgreementShouldBeFound("infrastructureBenefit.equals=" + DEFAULT_INFRASTRUCTURE_BENEFIT);

        // Get all the agreementList where infrastructureBenefit equals to UPDATED_INFRASTRUCTURE_BENEFIT
        defaultAgreementShouldNotBeFound("infrastructureBenefit.equals=" + UPDATED_INFRASTRUCTURE_BENEFIT);
    }

    @Test
    void getAllAgreementsByInfrastructureBenefitIsInShouldWork() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where infrastructureBenefit in DEFAULT_INFRASTRUCTURE_BENEFIT or UPDATED_INFRASTRUCTURE_BENEFIT
        defaultAgreementShouldBeFound("infrastructureBenefit.in=" + DEFAULT_INFRASTRUCTURE_BENEFIT + "," + UPDATED_INFRASTRUCTURE_BENEFIT);

        // Get all the agreementList where infrastructureBenefit equals to UPDATED_INFRASTRUCTURE_BENEFIT
        defaultAgreementShouldNotBeFound("infrastructureBenefit.in=" + UPDATED_INFRASTRUCTURE_BENEFIT);
    }

    @Test
    void getAllAgreementsByInfrastructureBenefitIsNullOrNotNull() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where infrastructureBenefit is not null
        defaultAgreementShouldBeFound("infrastructureBenefit.specified=true");

        // Get all the agreementList where infrastructureBenefit is null
        defaultAgreementShouldNotBeFound("infrastructureBenefit.specified=false");
    }

    @Test
    void getAllAgreementsByInfrastructureBenefitIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where infrastructureBenefit is greater than or equal to DEFAULT_INFRASTRUCTURE_BENEFIT
        defaultAgreementShouldBeFound("infrastructureBenefit.greaterThanOrEqual=" + DEFAULT_INFRASTRUCTURE_BENEFIT);

        // Get all the agreementList where infrastructureBenefit is greater than or equal to UPDATED_INFRASTRUCTURE_BENEFIT
        defaultAgreementShouldNotBeFound("infrastructureBenefit.greaterThanOrEqual=" + UPDATED_INFRASTRUCTURE_BENEFIT);
    }

    @Test
    void getAllAgreementsByInfrastructureBenefitIsLessThanOrEqualToSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where infrastructureBenefit is less than or equal to DEFAULT_INFRASTRUCTURE_BENEFIT
        defaultAgreementShouldBeFound("infrastructureBenefit.lessThanOrEqual=" + DEFAULT_INFRASTRUCTURE_BENEFIT);

        // Get all the agreementList where infrastructureBenefit is less than or equal to SMALLER_INFRASTRUCTURE_BENEFIT
        defaultAgreementShouldNotBeFound("infrastructureBenefit.lessThanOrEqual=" + SMALLER_INFRASTRUCTURE_BENEFIT);
    }

    @Test
    void getAllAgreementsByInfrastructureBenefitIsLessThanSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where infrastructureBenefit is less than DEFAULT_INFRASTRUCTURE_BENEFIT
        defaultAgreementShouldNotBeFound("infrastructureBenefit.lessThan=" + DEFAULT_INFRASTRUCTURE_BENEFIT);

        // Get all the agreementList where infrastructureBenefit is less than UPDATED_INFRASTRUCTURE_BENEFIT
        defaultAgreementShouldBeFound("infrastructureBenefit.lessThan=" + UPDATED_INFRASTRUCTURE_BENEFIT);
    }

    @Test
    void getAllAgreementsByInfrastructureBenefitIsGreaterThanSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where infrastructureBenefit is greater than DEFAULT_INFRASTRUCTURE_BENEFIT
        defaultAgreementShouldNotBeFound("infrastructureBenefit.greaterThan=" + DEFAULT_INFRASTRUCTURE_BENEFIT);

        // Get all the agreementList where infrastructureBenefit is greater than SMALLER_INFRASTRUCTURE_BENEFIT
        defaultAgreementShouldBeFound("infrastructureBenefit.greaterThan=" + SMALLER_INFRASTRUCTURE_BENEFIT);
    }

    @Test
    void getAllAgreementsByExtraBenefitIsEqualToSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where extraBenefit equals to DEFAULT_EXTRA_BENEFIT
        defaultAgreementShouldBeFound("extraBenefit.equals=" + DEFAULT_EXTRA_BENEFIT);

        // Get all the agreementList where extraBenefit equals to UPDATED_EXTRA_BENEFIT
        defaultAgreementShouldNotBeFound("extraBenefit.equals=" + UPDATED_EXTRA_BENEFIT);
    }

    @Test
    void getAllAgreementsByExtraBenefitIsInShouldWork() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where extraBenefit in DEFAULT_EXTRA_BENEFIT or UPDATED_EXTRA_BENEFIT
        defaultAgreementShouldBeFound("extraBenefit.in=" + DEFAULT_EXTRA_BENEFIT + "," + UPDATED_EXTRA_BENEFIT);

        // Get all the agreementList where extraBenefit equals to UPDATED_EXTRA_BENEFIT
        defaultAgreementShouldNotBeFound("extraBenefit.in=" + UPDATED_EXTRA_BENEFIT);
    }

    @Test
    void getAllAgreementsByExtraBenefitIsNullOrNotNull() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where extraBenefit is not null
        defaultAgreementShouldBeFound("extraBenefit.specified=true");

        // Get all the agreementList where extraBenefit is null
        defaultAgreementShouldNotBeFound("extraBenefit.specified=false");
    }

    @Test
    void getAllAgreementsByExtraBenefitIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where extraBenefit is greater than or equal to DEFAULT_EXTRA_BENEFIT
        defaultAgreementShouldBeFound("extraBenefit.greaterThanOrEqual=" + DEFAULT_EXTRA_BENEFIT);

        // Get all the agreementList where extraBenefit is greater than or equal to UPDATED_EXTRA_BENEFIT
        defaultAgreementShouldNotBeFound("extraBenefit.greaterThanOrEqual=" + UPDATED_EXTRA_BENEFIT);
    }

    @Test
    void getAllAgreementsByExtraBenefitIsLessThanOrEqualToSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where extraBenefit is less than or equal to DEFAULT_EXTRA_BENEFIT
        defaultAgreementShouldBeFound("extraBenefit.lessThanOrEqual=" + DEFAULT_EXTRA_BENEFIT);

        // Get all the agreementList where extraBenefit is less than or equal to SMALLER_EXTRA_BENEFIT
        defaultAgreementShouldNotBeFound("extraBenefit.lessThanOrEqual=" + SMALLER_EXTRA_BENEFIT);
    }

    @Test
    void getAllAgreementsByExtraBenefitIsLessThanSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where extraBenefit is less than DEFAULT_EXTRA_BENEFIT
        defaultAgreementShouldNotBeFound("extraBenefit.lessThan=" + DEFAULT_EXTRA_BENEFIT);

        // Get all the agreementList where extraBenefit is less than UPDATED_EXTRA_BENEFIT
        defaultAgreementShouldBeFound("extraBenefit.lessThan=" + UPDATED_EXTRA_BENEFIT);
    }

    @Test
    void getAllAgreementsByExtraBenefitIsGreaterThanSomething() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        // Get all the agreementList where extraBenefit is greater than DEFAULT_EXTRA_BENEFIT
        defaultAgreementShouldNotBeFound("extraBenefit.greaterThan=" + DEFAULT_EXTRA_BENEFIT);

        // Get all the agreementList where extraBenefit is greater than SMALLER_EXTRA_BENEFIT
        defaultAgreementShouldBeFound("extraBenefit.greaterThan=" + SMALLER_EXTRA_BENEFIT);
    }

    @Test
    void getAllAgreementsByProviderIsEqualToSomething() {
        Party provider = PartyResourceIT.createEntity(em);
        partyRepository.save(provider).block();
        Long providerId = provider.getId();
        agreement.setProviderId(providerId);
        agreementRepository.save(agreement).block();
        // Get all the agreementList where provider equals to providerId
        defaultAgreementShouldBeFound("providerId.equals=" + providerId);

        // Get all the agreementList where provider equals to (providerId + 1)
        defaultAgreementShouldNotBeFound("providerId.equals=" + (providerId + 1));
    }

    @Test
    void getAllAgreementsByConsumerIsEqualToSomething() {
        Party consumer = PartyResourceIT.createEntity(em);
        partyRepository.save(consumer).block();
        Long consumerId = consumer.getId();
        agreement.setConsumerId(consumerId);
        agreementRepository.save(agreement).block();
        // Get all the agreementList where consumer equals to consumerId
        defaultAgreementShouldBeFound("consumerId.equals=" + consumerId);

        // Get all the agreementList where consumer equals to (consumerId + 1)
        defaultAgreementShouldNotBeFound("consumerId.equals=" + (consumerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAgreementShouldBeFound(String filter) {
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
            .value(hasItem(agreement.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].startDate")
            .value(hasItem(DEFAULT_START_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
            .jsonPath("$.[*].activationStatusClassId")
            .value(hasItem(DEFAULT_ACTIVATION_STATUS_CLASS_ID.intValue()))
            .jsonPath("$.[*].infrastructureBenefit")
            .value(hasItem(DEFAULT_INFRASTRUCTURE_BENEFIT.doubleValue()))
            .jsonPath("$.[*].extraBenefit")
            .value(hasItem(DEFAULT_EXTRA_BENEFIT.doubleValue()));

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
    private void defaultAgreementShouldNotBeFound(String filter) {
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
    void getNonExistingAgreement() {
        // Get the agreement
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAgreement() throws Exception {
        // Initialize the database
        agreementRepository.save(agreement).block();

        int databaseSizeBeforeUpdate = agreementRepository.findAll().collectList().block().size();

        // Update the agreement
        Agreement updatedAgreement = agreementRepository.findById(agreement.getId()).block();
        updatedAgreement
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .activationStatusClassId(UPDATED_ACTIVATION_STATUS_CLASS_ID)
            .infrastructureBenefit(UPDATED_INFRASTRUCTURE_BENEFIT)
            .extraBenefit(UPDATED_EXTRA_BENEFIT);
        AgreementDTO agreementDTO = agreementMapper.toDto(updatedAgreement);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, agreementDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agreementDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Agreement in the database
        List<Agreement> agreementList = agreementRepository.findAll().collectList().block();
        assertThat(agreementList).hasSize(databaseSizeBeforeUpdate);
        Agreement testAgreement = agreementList.get(agreementList.size() - 1);
        assertThat(testAgreement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAgreement.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAgreement.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testAgreement.getActivationStatusClassId()).isEqualTo(UPDATED_ACTIVATION_STATUS_CLASS_ID);
        assertThat(testAgreement.getInfrastructureBenefit()).isEqualTo(UPDATED_INFRASTRUCTURE_BENEFIT);
        assertThat(testAgreement.getExtraBenefit()).isEqualTo(UPDATED_EXTRA_BENEFIT);
    }

    @Test
    void putNonExistingAgreement() throws Exception {
        int databaseSizeBeforeUpdate = agreementRepository.findAll().collectList().block().size();
        agreement.setId(longCount.incrementAndGet());

        // Create the Agreement
        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, agreementDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agreementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Agreement in the database
        List<Agreement> agreementList = agreementRepository.findAll().collectList().block();
        assertThat(agreementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAgreement() throws Exception {
        int databaseSizeBeforeUpdate = agreementRepository.findAll().collectList().block().size();
        agreement.setId(longCount.incrementAndGet());

        // Create the Agreement
        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agreementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Agreement in the database
        List<Agreement> agreementList = agreementRepository.findAll().collectList().block();
        assertThat(agreementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAgreement() throws Exception {
        int databaseSizeBeforeUpdate = agreementRepository.findAll().collectList().block().size();
        agreement.setId(longCount.incrementAndGet());

        // Create the Agreement
        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agreementDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Agreement in the database
        List<Agreement> agreementList = agreementRepository.findAll().collectList().block();
        assertThat(agreementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAgreementWithPatch() throws Exception {
        // Initialize the database
        agreementRepository.save(agreement).block();

        int databaseSizeBeforeUpdate = agreementRepository.findAll().collectList().block().size();

        // Update the agreement using partial update
        Agreement partialUpdatedAgreement = new Agreement();
        partialUpdatedAgreement.setId(agreement.getId());

        partialUpdatedAgreement
            .startDate(UPDATED_START_DATE)
            .infrastructureBenefit(UPDATED_INFRASTRUCTURE_BENEFIT)
            .extraBenefit(UPDATED_EXTRA_BENEFIT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAgreement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAgreement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Agreement in the database
        List<Agreement> agreementList = agreementRepository.findAll().collectList().block();
        assertThat(agreementList).hasSize(databaseSizeBeforeUpdate);
        Agreement testAgreement = agreementList.get(agreementList.size() - 1);
        assertThat(testAgreement.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAgreement.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAgreement.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testAgreement.getActivationStatusClassId()).isEqualTo(DEFAULT_ACTIVATION_STATUS_CLASS_ID);
        assertThat(testAgreement.getInfrastructureBenefit()).isEqualTo(UPDATED_INFRASTRUCTURE_BENEFIT);
        assertThat(testAgreement.getExtraBenefit()).isEqualTo(UPDATED_EXTRA_BENEFIT);
    }

    @Test
    void fullUpdateAgreementWithPatch() throws Exception {
        // Initialize the database
        agreementRepository.save(agreement).block();

        int databaseSizeBeforeUpdate = agreementRepository.findAll().collectList().block().size();

        // Update the agreement using partial update
        Agreement partialUpdatedAgreement = new Agreement();
        partialUpdatedAgreement.setId(agreement.getId());

        partialUpdatedAgreement
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .activationStatusClassId(UPDATED_ACTIVATION_STATUS_CLASS_ID)
            .infrastructureBenefit(UPDATED_INFRASTRUCTURE_BENEFIT)
            .extraBenefit(UPDATED_EXTRA_BENEFIT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAgreement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAgreement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Agreement in the database
        List<Agreement> agreementList = agreementRepository.findAll().collectList().block();
        assertThat(agreementList).hasSize(databaseSizeBeforeUpdate);
        Agreement testAgreement = agreementList.get(agreementList.size() - 1);
        assertThat(testAgreement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAgreement.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAgreement.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testAgreement.getActivationStatusClassId()).isEqualTo(UPDATED_ACTIVATION_STATUS_CLASS_ID);
        assertThat(testAgreement.getInfrastructureBenefit()).isEqualTo(UPDATED_INFRASTRUCTURE_BENEFIT);
        assertThat(testAgreement.getExtraBenefit()).isEqualTo(UPDATED_EXTRA_BENEFIT);
    }

    @Test
    void patchNonExistingAgreement() throws Exception {
        int databaseSizeBeforeUpdate = agreementRepository.findAll().collectList().block().size();
        agreement.setId(longCount.incrementAndGet());

        // Create the Agreement
        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, agreementDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(agreementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Agreement in the database
        List<Agreement> agreementList = agreementRepository.findAll().collectList().block();
        assertThat(agreementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAgreement() throws Exception {
        int databaseSizeBeforeUpdate = agreementRepository.findAll().collectList().block().size();
        agreement.setId(longCount.incrementAndGet());

        // Create the Agreement
        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(agreementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Agreement in the database
        List<Agreement> agreementList = agreementRepository.findAll().collectList().block();
        assertThat(agreementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAgreement() throws Exception {
        int databaseSizeBeforeUpdate = agreementRepository.findAll().collectList().block().size();
        agreement.setId(longCount.incrementAndGet());

        // Create the Agreement
        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(agreementDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Agreement in the database
        List<Agreement> agreementList = agreementRepository.findAll().collectList().block();
        assertThat(agreementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAgreement() {
        // Initialize the database
        agreementRepository.save(agreement).block();

        int databaseSizeBeforeDelete = agreementRepository.findAll().collectList().block().size();

        // Delete the agreement
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, agreement.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Agreement> agreementList = agreementRepository.findAll().collectList().block();
        assertThat(agreementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
