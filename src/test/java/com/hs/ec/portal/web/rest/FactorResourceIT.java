package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.Factor;
import com.hs.ec.portal.domain.Location;
import com.hs.ec.portal.domain.Party;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.repository.FactorRepository;
import com.hs.ec.portal.repository.LocationRepository;
import com.hs.ec.portal.repository.PartyRepository;
import com.hs.ec.portal.repository.PartyRepository;
import com.hs.ec.portal.service.FactorService;
import com.hs.ec.portal.service.dto.FactorDTO;
import com.hs.ec.portal.service.mapper.FactorMapper;
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
 * Integration tests for the {@link FactorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FactorResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_FACTOR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FACTOR_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_LAST_STATUS_CLASS_ID = 1L;
    private static final Long UPDATED_LAST_STATUS_CLASS_ID = 2L;
    private static final Long SMALLER_LAST_STATUS_CLASS_ID = 1L - 1L;

    private static final Long DEFAULT_PAYMENT_STATE_CLASS_ID = 1L;
    private static final Long UPDATED_PAYMENT_STATE_CLASS_ID = 2L;
    private static final Long SMALLER_PAYMENT_STATE_CLASS_ID = 1L - 1L;

    private static final Long DEFAULT_CATEGORY_CLASS_ID = 1L;
    private static final Long UPDATED_CATEGORY_CLASS_ID = 2L;
    private static final Long SMALLER_CATEGORY_CLASS_ID = 1L - 1L;

    private static final Double DEFAULT_TOTAL_PRICE = 1D;
    private static final Double UPDATED_TOTAL_PRICE = 2D;
    private static final Double SMALLER_TOTAL_PRICE = 1D - 1D;

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;
    private static final Double SMALLER_DISCOUNT = 1D - 1D;

    private static final String DEFAULT_DISCOUNT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DISCOUNT_CODE = "BBBBBBBBBB";

    private static final Double DEFAULT_FINAL_TAX = 1D;
    private static final Double UPDATED_FINAL_TAX = 2D;
    private static final Double SMALLER_FINAL_TAX = 1D - 1D;

    private static final Double DEFAULT_PAYABLE = 1D;
    private static final Double UPDATED_PAYABLE = 2D;
    private static final Double SMALLER_PAYABLE = 1D - 1D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/factors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FactorRepository factorRepository;

    @Mock
    private FactorRepository factorRepositoryMock;

    @Autowired
    private FactorMapper factorMapper;

    @Mock
    private FactorService factorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Factor factor;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PartyRepository partyRepository;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factor createEntity(EntityManager em) {
        Factor factor = new Factor()
            .title(DEFAULT_TITLE)
            .factorCode(DEFAULT_FACTOR_CODE)
            .lastStatusClassId(DEFAULT_LAST_STATUS_CLASS_ID)
            .paymentStateClassId(DEFAULT_PAYMENT_STATE_CLASS_ID)
            .categoryClassId(DEFAULT_CATEGORY_CLASS_ID)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .discount(DEFAULT_DISCOUNT)
            .discountCode(DEFAULT_DISCOUNT_CODE)
            .finalTax(DEFAULT_FINAL_TAX)
            .payable(DEFAULT_PAYABLE)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        Location location;
        location = em.insert(LocationResourceIT.createEntity(em)).block();
        factor.setLocation(location);
        return factor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factor createUpdatedEntity(EntityManager em) {
        Factor factor = new Factor()
            .title(UPDATED_TITLE)
            .factorCode(UPDATED_FACTOR_CODE)
            .lastStatusClassId(UPDATED_LAST_STATUS_CLASS_ID)
            .paymentStateClassId(UPDATED_PAYMENT_STATE_CLASS_ID)
            .categoryClassId(UPDATED_CATEGORY_CLASS_ID)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .discount(UPDATED_DISCOUNT)
            .discountCode(UPDATED_DISCOUNT_CODE)
            .finalTax(UPDATED_FINAL_TAX)
            .payable(UPDATED_PAYABLE)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        Location location;
        location = em.insert(LocationResourceIT.createUpdatedEntity(em)).block();
        factor.setLocation(location);
        return factor;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Factor.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        LocationResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        factor = createEntity(em);
    }

    @Test
    void createFactor() throws Exception {
        int databaseSizeBeforeCreate = factorRepository.findAll().collectList().block().size();
        // Create the Factor
        FactorDTO factorDTO = factorMapper.toDto(factor);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Factor in the database
        List<Factor> factorList = factorRepository.findAll().collectList().block();
        assertThat(factorList).hasSize(databaseSizeBeforeCreate + 1);
        Factor testFactor = factorList.get(factorList.size() - 1);
        assertThat(testFactor.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFactor.getFactorCode()).isEqualTo(DEFAULT_FACTOR_CODE);
        assertThat(testFactor.getLastStatusClassId()).isEqualTo(DEFAULT_LAST_STATUS_CLASS_ID);
        assertThat(testFactor.getPaymentStateClassId()).isEqualTo(DEFAULT_PAYMENT_STATE_CLASS_ID);
        assertThat(testFactor.getCategoryClassId()).isEqualTo(DEFAULT_CATEGORY_CLASS_ID);
        assertThat(testFactor.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testFactor.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testFactor.getDiscountCode()).isEqualTo(DEFAULT_DISCOUNT_CODE);
        assertThat(testFactor.getFinalTax()).isEqualTo(DEFAULT_FINAL_TAX);
        assertThat(testFactor.getPayable()).isEqualTo(DEFAULT_PAYABLE);
        assertThat(testFactor.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createFactorWithExistingId() throws Exception {
        // Create the Factor with an existing ID
        factor.setId(1L);
        FactorDTO factorDTO = factorMapper.toDto(factor);

        int databaseSizeBeforeCreate = factorRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Factor in the database
        List<Factor> factorList = factorRepository.findAll().collectList().block();
        assertThat(factorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorRepository.findAll().collectList().block().size();
        // set the field null
        factor.setTitle(null);

        // Create the Factor, which fails.
        FactorDTO factorDTO = factorMapper.toDto(factor);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Factor> factorList = factorRepository.findAll().collectList().block();
        assertThat(factorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFactorCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorRepository.findAll().collectList().block().size();
        // set the field null
        factor.setFactorCode(null);

        // Create the Factor, which fails.
        FactorDTO factorDTO = factorMapper.toDto(factor);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Factor> factorList = factorRepository.findAll().collectList().block();
        assertThat(factorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLastStatusClassIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorRepository.findAll().collectList().block().size();
        // set the field null
        factor.setLastStatusClassId(null);

        // Create the Factor, which fails.
        FactorDTO factorDTO = factorMapper.toDto(factor);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Factor> factorList = factorRepository.findAll().collectList().block();
        assertThat(factorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPaymentStateClassIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorRepository.findAll().collectList().block().size();
        // set the field null
        factor.setPaymentStateClassId(null);

        // Create the Factor, which fails.
        FactorDTO factorDTO = factorMapper.toDto(factor);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Factor> factorList = factorRepository.findAll().collectList().block();
        assertThat(factorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTotalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorRepository.findAll().collectList().block().size();
        // set the field null
        factor.setTotalPrice(null);

        // Create the Factor, which fails.
        FactorDTO factorDTO = factorMapper.toDto(factor);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Factor> factorList = factorRepository.findAll().collectList().block();
        assertThat(factorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPayableIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorRepository.findAll().collectList().block().size();
        // set the field null
        factor.setPayable(null);

        // Create the Factor, which fails.
        FactorDTO factorDTO = factorMapper.toDto(factor);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Factor> factorList = factorRepository.findAll().collectList().block();
        assertThat(factorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllFactors() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList
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
            .value(hasItem(factor.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].factorCode")
            .value(hasItem(DEFAULT_FACTOR_CODE))
            .jsonPath("$.[*].lastStatusClassId")
            .value(hasItem(DEFAULT_LAST_STATUS_CLASS_ID.intValue()))
            .jsonPath("$.[*].paymentStateClassId")
            .value(hasItem(DEFAULT_PAYMENT_STATE_CLASS_ID.intValue()))
            .jsonPath("$.[*].categoryClassId")
            .value(hasItem(DEFAULT_CATEGORY_CLASS_ID.intValue()))
            .jsonPath("$.[*].totalPrice")
            .value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue()))
            .jsonPath("$.[*].discount")
            .value(hasItem(DEFAULT_DISCOUNT.doubleValue()))
            .jsonPath("$.[*].discountCode")
            .value(hasItem(DEFAULT_DISCOUNT_CODE))
            .jsonPath("$.[*].finalTax")
            .value(hasItem(DEFAULT_FINAL_TAX.doubleValue()))
            .jsonPath("$.[*].payable")
            .value(hasItem(DEFAULT_PAYABLE.doubleValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFactorsWithEagerRelationshipsIsEnabled() {
        when(factorServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(factorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFactorsWithEagerRelationshipsIsNotEnabled() {
        when(factorServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(factorRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getFactor() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get the factor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, factor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(factor.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.factorCode")
            .value(is(DEFAULT_FACTOR_CODE))
            .jsonPath("$.lastStatusClassId")
            .value(is(DEFAULT_LAST_STATUS_CLASS_ID.intValue()))
            .jsonPath("$.paymentStateClassId")
            .value(is(DEFAULT_PAYMENT_STATE_CLASS_ID.intValue()))
            .jsonPath("$.categoryClassId")
            .value(is(DEFAULT_CATEGORY_CLASS_ID.intValue()))
            .jsonPath("$.totalPrice")
            .value(is(DEFAULT_TOTAL_PRICE.doubleValue()))
            .jsonPath("$.discount")
            .value(is(DEFAULT_DISCOUNT.doubleValue()))
            .jsonPath("$.discountCode")
            .value(is(DEFAULT_DISCOUNT_CODE))
            .jsonPath("$.finalTax")
            .value(is(DEFAULT_FINAL_TAX.doubleValue()))
            .jsonPath("$.payable")
            .value(is(DEFAULT_PAYABLE.doubleValue()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getFactorsByIdFiltering() {
        // Initialize the database
        factorRepository.save(factor).block();

        Long id = factor.getId();

        defaultFactorShouldBeFound("id.equals=" + id);
        defaultFactorShouldNotBeFound("id.notEquals=" + id);

        defaultFactorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFactorShouldNotBeFound("id.greaterThan=" + id);

        defaultFactorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFactorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllFactorsByTitleIsEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where title equals to DEFAULT_TITLE
        defaultFactorShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the factorList where title equals to UPDATED_TITLE
        defaultFactorShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    void getAllFactorsByTitleIsInShouldWork() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultFactorShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the factorList where title equals to UPDATED_TITLE
        defaultFactorShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    void getAllFactorsByTitleIsNullOrNotNull() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where title is not null
        defaultFactorShouldBeFound("title.specified=true");

        // Get all the factorList where title is null
        defaultFactorShouldNotBeFound("title.specified=false");
    }

    @Test
    void getAllFactorsByTitleContainsSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where title contains DEFAULT_TITLE
        defaultFactorShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the factorList where title contains UPDATED_TITLE
        defaultFactorShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    void getAllFactorsByTitleNotContainsSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where title does not contain DEFAULT_TITLE
        defaultFactorShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the factorList where title does not contain UPDATED_TITLE
        defaultFactorShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    void getAllFactorsByFactorCodeIsEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where factorCode equals to DEFAULT_FACTOR_CODE
        defaultFactorShouldBeFound("factorCode.equals=" + DEFAULT_FACTOR_CODE);

        // Get all the factorList where factorCode equals to UPDATED_FACTOR_CODE
        defaultFactorShouldNotBeFound("factorCode.equals=" + UPDATED_FACTOR_CODE);
    }

    @Test
    void getAllFactorsByFactorCodeIsInShouldWork() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where factorCode in DEFAULT_FACTOR_CODE or UPDATED_FACTOR_CODE
        defaultFactorShouldBeFound("factorCode.in=" + DEFAULT_FACTOR_CODE + "," + UPDATED_FACTOR_CODE);

        // Get all the factorList where factorCode equals to UPDATED_FACTOR_CODE
        defaultFactorShouldNotBeFound("factorCode.in=" + UPDATED_FACTOR_CODE);
    }

    @Test
    void getAllFactorsByFactorCodeIsNullOrNotNull() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where factorCode is not null
        defaultFactorShouldBeFound("factorCode.specified=true");

        // Get all the factorList where factorCode is null
        defaultFactorShouldNotBeFound("factorCode.specified=false");
    }

    @Test
    void getAllFactorsByFactorCodeContainsSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where factorCode contains DEFAULT_FACTOR_CODE
        defaultFactorShouldBeFound("factorCode.contains=" + DEFAULT_FACTOR_CODE);

        // Get all the factorList where factorCode contains UPDATED_FACTOR_CODE
        defaultFactorShouldNotBeFound("factorCode.contains=" + UPDATED_FACTOR_CODE);
    }

    @Test
    void getAllFactorsByFactorCodeNotContainsSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where factorCode does not contain DEFAULT_FACTOR_CODE
        defaultFactorShouldNotBeFound("factorCode.doesNotContain=" + DEFAULT_FACTOR_CODE);

        // Get all the factorList where factorCode does not contain UPDATED_FACTOR_CODE
        defaultFactorShouldBeFound("factorCode.doesNotContain=" + UPDATED_FACTOR_CODE);
    }

    @Test
    void getAllFactorsByLastStatusClassIdIsEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where lastStatusClassId equals to DEFAULT_LAST_STATUS_CLASS_ID
        defaultFactorShouldBeFound("lastStatusClassId.equals=" + DEFAULT_LAST_STATUS_CLASS_ID);

        // Get all the factorList where lastStatusClassId equals to UPDATED_LAST_STATUS_CLASS_ID
        defaultFactorShouldNotBeFound("lastStatusClassId.equals=" + UPDATED_LAST_STATUS_CLASS_ID);
    }

    @Test
    void getAllFactorsByLastStatusClassIdIsInShouldWork() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where lastStatusClassId in DEFAULT_LAST_STATUS_CLASS_ID or UPDATED_LAST_STATUS_CLASS_ID
        defaultFactorShouldBeFound("lastStatusClassId.in=" + DEFAULT_LAST_STATUS_CLASS_ID + "," + UPDATED_LAST_STATUS_CLASS_ID);

        // Get all the factorList where lastStatusClassId equals to UPDATED_LAST_STATUS_CLASS_ID
        defaultFactorShouldNotBeFound("lastStatusClassId.in=" + UPDATED_LAST_STATUS_CLASS_ID);
    }

    @Test
    void getAllFactorsByLastStatusClassIdIsNullOrNotNull() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where lastStatusClassId is not null
        defaultFactorShouldBeFound("lastStatusClassId.specified=true");

        // Get all the factorList where lastStatusClassId is null
        defaultFactorShouldNotBeFound("lastStatusClassId.specified=false");
    }

    @Test
    void getAllFactorsByLastStatusClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where lastStatusClassId is greater than or equal to DEFAULT_LAST_STATUS_CLASS_ID
        defaultFactorShouldBeFound("lastStatusClassId.greaterThanOrEqual=" + DEFAULT_LAST_STATUS_CLASS_ID);

        // Get all the factorList where lastStatusClassId is greater than or equal to UPDATED_LAST_STATUS_CLASS_ID
        defaultFactorShouldNotBeFound("lastStatusClassId.greaterThanOrEqual=" + UPDATED_LAST_STATUS_CLASS_ID);
    }

    @Test
    void getAllFactorsByLastStatusClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where lastStatusClassId is less than or equal to DEFAULT_LAST_STATUS_CLASS_ID
        defaultFactorShouldBeFound("lastStatusClassId.lessThanOrEqual=" + DEFAULT_LAST_STATUS_CLASS_ID);

        // Get all the factorList where lastStatusClassId is less than or equal to SMALLER_LAST_STATUS_CLASS_ID
        defaultFactorShouldNotBeFound("lastStatusClassId.lessThanOrEqual=" + SMALLER_LAST_STATUS_CLASS_ID);
    }

    @Test
    void getAllFactorsByLastStatusClassIdIsLessThanSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where lastStatusClassId is less than DEFAULT_LAST_STATUS_CLASS_ID
        defaultFactorShouldNotBeFound("lastStatusClassId.lessThan=" + DEFAULT_LAST_STATUS_CLASS_ID);

        // Get all the factorList where lastStatusClassId is less than UPDATED_LAST_STATUS_CLASS_ID
        defaultFactorShouldBeFound("lastStatusClassId.lessThan=" + UPDATED_LAST_STATUS_CLASS_ID);
    }

    @Test
    void getAllFactorsByLastStatusClassIdIsGreaterThanSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where lastStatusClassId is greater than DEFAULT_LAST_STATUS_CLASS_ID
        defaultFactorShouldNotBeFound("lastStatusClassId.greaterThan=" + DEFAULT_LAST_STATUS_CLASS_ID);

        // Get all the factorList where lastStatusClassId is greater than SMALLER_LAST_STATUS_CLASS_ID
        defaultFactorShouldBeFound("lastStatusClassId.greaterThan=" + SMALLER_LAST_STATUS_CLASS_ID);
    }

    @Test
    void getAllFactorsByPaymentStateClassIdIsEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where paymentStateClassId equals to DEFAULT_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldBeFound("paymentStateClassId.equals=" + DEFAULT_PAYMENT_STATE_CLASS_ID);

        // Get all the factorList where paymentStateClassId equals to UPDATED_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldNotBeFound("paymentStateClassId.equals=" + UPDATED_PAYMENT_STATE_CLASS_ID);
    }

    @Test
    void getAllFactorsByPaymentStateClassIdIsInShouldWork() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where paymentStateClassId in DEFAULT_PAYMENT_STATE_CLASS_ID or UPDATED_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldBeFound("paymentStateClassId.in=" + DEFAULT_PAYMENT_STATE_CLASS_ID + "," + UPDATED_PAYMENT_STATE_CLASS_ID);

        // Get all the factorList where paymentStateClassId equals to UPDATED_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldNotBeFound("paymentStateClassId.in=" + UPDATED_PAYMENT_STATE_CLASS_ID);
    }

    @Test
    void getAllFactorsByPaymentStateClassIdIsNullOrNotNull() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where paymentStateClassId is not null
        defaultFactorShouldBeFound("paymentStateClassId.specified=true");

        // Get all the factorList where paymentStateClassId is null
        defaultFactorShouldNotBeFound("paymentStateClassId.specified=false");
    }

    @Test
    void getAllFactorsByPaymentStateClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where paymentStateClassId is greater than or equal to DEFAULT_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldBeFound("paymentStateClassId.greaterThanOrEqual=" + DEFAULT_PAYMENT_STATE_CLASS_ID);

        // Get all the factorList where paymentStateClassId is greater than or equal to UPDATED_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldNotBeFound("paymentStateClassId.greaterThanOrEqual=" + UPDATED_PAYMENT_STATE_CLASS_ID);
    }

    @Test
    void getAllFactorsByPaymentStateClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where paymentStateClassId is less than or equal to DEFAULT_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldBeFound("paymentStateClassId.lessThanOrEqual=" + DEFAULT_PAYMENT_STATE_CLASS_ID);

        // Get all the factorList where paymentStateClassId is less than or equal to SMALLER_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldNotBeFound("paymentStateClassId.lessThanOrEqual=" + SMALLER_PAYMENT_STATE_CLASS_ID);
    }

    @Test
    void getAllFactorsByPaymentStateClassIdIsLessThanSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where paymentStateClassId is less than DEFAULT_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldNotBeFound("paymentStateClassId.lessThan=" + DEFAULT_PAYMENT_STATE_CLASS_ID);

        // Get all the factorList where paymentStateClassId is less than UPDATED_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldBeFound("paymentStateClassId.lessThan=" + UPDATED_PAYMENT_STATE_CLASS_ID);
    }

    @Test
    void getAllFactorsByPaymentStateClassIdIsGreaterThanSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where paymentStateClassId is greater than DEFAULT_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldNotBeFound("paymentStateClassId.greaterThan=" + DEFAULT_PAYMENT_STATE_CLASS_ID);

        // Get all the factorList where paymentStateClassId is greater than SMALLER_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldBeFound("paymentStateClassId.greaterThan=" + SMALLER_PAYMENT_STATE_CLASS_ID);
    }

    @Test
    void getAllFactorsByCategoryClassIdIsEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where categoryClassId equals to DEFAULT_CATEGORY_CLASS_ID
        defaultFactorShouldBeFound("categoryClassId.equals=" + DEFAULT_CATEGORY_CLASS_ID);

        // Get all the factorList where categoryClassId equals to UPDATED_CATEGORY_CLASS_ID
        defaultFactorShouldNotBeFound("categoryClassId.equals=" + UPDATED_CATEGORY_CLASS_ID);
    }

    @Test
    void getAllFactorsByCategoryClassIdIsInShouldWork() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where categoryClassId in DEFAULT_CATEGORY_CLASS_ID or UPDATED_CATEGORY_CLASS_ID
        defaultFactorShouldBeFound("categoryClassId.in=" + DEFAULT_CATEGORY_CLASS_ID + "," + UPDATED_CATEGORY_CLASS_ID);

        // Get all the factorList where categoryClassId equals to UPDATED_CATEGORY_CLASS_ID
        defaultFactorShouldNotBeFound("categoryClassId.in=" + UPDATED_CATEGORY_CLASS_ID);
    }

    @Test
    void getAllFactorsByCategoryClassIdIsNullOrNotNull() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where categoryClassId is not null
        defaultFactorShouldBeFound("categoryClassId.specified=true");

        // Get all the factorList where categoryClassId is null
        defaultFactorShouldNotBeFound("categoryClassId.specified=false");
    }

    @Test
    void getAllFactorsByCategoryClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where categoryClassId is greater than or equal to DEFAULT_CATEGORY_CLASS_ID
        defaultFactorShouldBeFound("categoryClassId.greaterThanOrEqual=" + DEFAULT_CATEGORY_CLASS_ID);

        // Get all the factorList where categoryClassId is greater than or equal to UPDATED_CATEGORY_CLASS_ID
        defaultFactorShouldNotBeFound("categoryClassId.greaterThanOrEqual=" + UPDATED_CATEGORY_CLASS_ID);
    }

    @Test
    void getAllFactorsByCategoryClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where categoryClassId is less than or equal to DEFAULT_CATEGORY_CLASS_ID
        defaultFactorShouldBeFound("categoryClassId.lessThanOrEqual=" + DEFAULT_CATEGORY_CLASS_ID);

        // Get all the factorList where categoryClassId is less than or equal to SMALLER_CATEGORY_CLASS_ID
        defaultFactorShouldNotBeFound("categoryClassId.lessThanOrEqual=" + SMALLER_CATEGORY_CLASS_ID);
    }

    @Test
    void getAllFactorsByCategoryClassIdIsLessThanSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where categoryClassId is less than DEFAULT_CATEGORY_CLASS_ID
        defaultFactorShouldNotBeFound("categoryClassId.lessThan=" + DEFAULT_CATEGORY_CLASS_ID);

        // Get all the factorList where categoryClassId is less than UPDATED_CATEGORY_CLASS_ID
        defaultFactorShouldBeFound("categoryClassId.lessThan=" + UPDATED_CATEGORY_CLASS_ID);
    }

    @Test
    void getAllFactorsByCategoryClassIdIsGreaterThanSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where categoryClassId is greater than DEFAULT_CATEGORY_CLASS_ID
        defaultFactorShouldNotBeFound("categoryClassId.greaterThan=" + DEFAULT_CATEGORY_CLASS_ID);

        // Get all the factorList where categoryClassId is greater than SMALLER_CATEGORY_CLASS_ID
        defaultFactorShouldBeFound("categoryClassId.greaterThan=" + SMALLER_CATEGORY_CLASS_ID);
    }

    @Test
    void getAllFactorsByTotalPriceIsEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where totalPrice equals to DEFAULT_TOTAL_PRICE
        defaultFactorShouldBeFound("totalPrice.equals=" + DEFAULT_TOTAL_PRICE);

        // Get all the factorList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultFactorShouldNotBeFound("totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    void getAllFactorsByTotalPriceIsInShouldWork() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where totalPrice in DEFAULT_TOTAL_PRICE or UPDATED_TOTAL_PRICE
        defaultFactorShouldBeFound("totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE);

        // Get all the factorList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultFactorShouldNotBeFound("totalPrice.in=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    void getAllFactorsByTotalPriceIsNullOrNotNull() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where totalPrice is not null
        defaultFactorShouldBeFound("totalPrice.specified=true");

        // Get all the factorList where totalPrice is null
        defaultFactorShouldNotBeFound("totalPrice.specified=false");
    }

    @Test
    void getAllFactorsByTotalPriceIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where totalPrice is greater than or equal to DEFAULT_TOTAL_PRICE
        defaultFactorShouldBeFound("totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the factorList where totalPrice is greater than or equal to UPDATED_TOTAL_PRICE
        defaultFactorShouldNotBeFound("totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    void getAllFactorsByTotalPriceIsLessThanOrEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where totalPrice is less than or equal to DEFAULT_TOTAL_PRICE
        defaultFactorShouldBeFound("totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the factorList where totalPrice is less than or equal to SMALLER_TOTAL_PRICE
        defaultFactorShouldNotBeFound("totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    void getAllFactorsByTotalPriceIsLessThanSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where totalPrice is less than DEFAULT_TOTAL_PRICE
        defaultFactorShouldNotBeFound("totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the factorList where totalPrice is less than UPDATED_TOTAL_PRICE
        defaultFactorShouldBeFound("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    void getAllFactorsByTotalPriceIsGreaterThanSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where totalPrice is greater than DEFAULT_TOTAL_PRICE
        defaultFactorShouldNotBeFound("totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the factorList where totalPrice is greater than SMALLER_TOTAL_PRICE
        defaultFactorShouldBeFound("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    void getAllFactorsByDiscountIsEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where discount equals to DEFAULT_DISCOUNT
        defaultFactorShouldBeFound("discount.equals=" + DEFAULT_DISCOUNT);

        // Get all the factorList where discount equals to UPDATED_DISCOUNT
        defaultFactorShouldNotBeFound("discount.equals=" + UPDATED_DISCOUNT);
    }

    @Test
    void getAllFactorsByDiscountIsInShouldWork() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where discount in DEFAULT_DISCOUNT or UPDATED_DISCOUNT
        defaultFactorShouldBeFound("discount.in=" + DEFAULT_DISCOUNT + "," + UPDATED_DISCOUNT);

        // Get all the factorList where discount equals to UPDATED_DISCOUNT
        defaultFactorShouldNotBeFound("discount.in=" + UPDATED_DISCOUNT);
    }

    @Test
    void getAllFactorsByDiscountIsNullOrNotNull() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where discount is not null
        defaultFactorShouldBeFound("discount.specified=true");

        // Get all the factorList where discount is null
        defaultFactorShouldNotBeFound("discount.specified=false");
    }

    @Test
    void getAllFactorsByDiscountIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where discount is greater than or equal to DEFAULT_DISCOUNT
        defaultFactorShouldBeFound("discount.greaterThanOrEqual=" + DEFAULT_DISCOUNT);

        // Get all the factorList where discount is greater than or equal to UPDATED_DISCOUNT
        defaultFactorShouldNotBeFound("discount.greaterThanOrEqual=" + UPDATED_DISCOUNT);
    }

    @Test
    void getAllFactorsByDiscountIsLessThanOrEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where discount is less than or equal to DEFAULT_DISCOUNT
        defaultFactorShouldBeFound("discount.lessThanOrEqual=" + DEFAULT_DISCOUNT);

        // Get all the factorList where discount is less than or equal to SMALLER_DISCOUNT
        defaultFactorShouldNotBeFound("discount.lessThanOrEqual=" + SMALLER_DISCOUNT);
    }

    @Test
    void getAllFactorsByDiscountIsLessThanSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where discount is less than DEFAULT_DISCOUNT
        defaultFactorShouldNotBeFound("discount.lessThan=" + DEFAULT_DISCOUNT);

        // Get all the factorList where discount is less than UPDATED_DISCOUNT
        defaultFactorShouldBeFound("discount.lessThan=" + UPDATED_DISCOUNT);
    }

    @Test
    void getAllFactorsByDiscountIsGreaterThanSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where discount is greater than DEFAULT_DISCOUNT
        defaultFactorShouldNotBeFound("discount.greaterThan=" + DEFAULT_DISCOUNT);

        // Get all the factorList where discount is greater than SMALLER_DISCOUNT
        defaultFactorShouldBeFound("discount.greaterThan=" + SMALLER_DISCOUNT);
    }

    @Test
    void getAllFactorsByDiscountCodeIsEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where discountCode equals to DEFAULT_DISCOUNT_CODE
        defaultFactorShouldBeFound("discountCode.equals=" + DEFAULT_DISCOUNT_CODE);

        // Get all the factorList where discountCode equals to UPDATED_DISCOUNT_CODE
        defaultFactorShouldNotBeFound("discountCode.equals=" + UPDATED_DISCOUNT_CODE);
    }

    @Test
    void getAllFactorsByDiscountCodeIsInShouldWork() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where discountCode in DEFAULT_DISCOUNT_CODE or UPDATED_DISCOUNT_CODE
        defaultFactorShouldBeFound("discountCode.in=" + DEFAULT_DISCOUNT_CODE + "," + UPDATED_DISCOUNT_CODE);

        // Get all the factorList where discountCode equals to UPDATED_DISCOUNT_CODE
        defaultFactorShouldNotBeFound("discountCode.in=" + UPDATED_DISCOUNT_CODE);
    }

    @Test
    void getAllFactorsByDiscountCodeIsNullOrNotNull() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where discountCode is not null
        defaultFactorShouldBeFound("discountCode.specified=true");

        // Get all the factorList where discountCode is null
        defaultFactorShouldNotBeFound("discountCode.specified=false");
    }

    @Test
    void getAllFactorsByDiscountCodeContainsSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where discountCode contains DEFAULT_DISCOUNT_CODE
        defaultFactorShouldBeFound("discountCode.contains=" + DEFAULT_DISCOUNT_CODE);

        // Get all the factorList where discountCode contains UPDATED_DISCOUNT_CODE
        defaultFactorShouldNotBeFound("discountCode.contains=" + UPDATED_DISCOUNT_CODE);
    }

    @Test
    void getAllFactorsByDiscountCodeNotContainsSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where discountCode does not contain DEFAULT_DISCOUNT_CODE
        defaultFactorShouldNotBeFound("discountCode.doesNotContain=" + DEFAULT_DISCOUNT_CODE);

        // Get all the factorList where discountCode does not contain UPDATED_DISCOUNT_CODE
        defaultFactorShouldBeFound("discountCode.doesNotContain=" + UPDATED_DISCOUNT_CODE);
    }

    @Test
    void getAllFactorsByFinalTaxIsEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where finalTax equals to DEFAULT_FINAL_TAX
        defaultFactorShouldBeFound("finalTax.equals=" + DEFAULT_FINAL_TAX);

        // Get all the factorList where finalTax equals to UPDATED_FINAL_TAX
        defaultFactorShouldNotBeFound("finalTax.equals=" + UPDATED_FINAL_TAX);
    }

    @Test
    void getAllFactorsByFinalTaxIsInShouldWork() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where finalTax in DEFAULT_FINAL_TAX or UPDATED_FINAL_TAX
        defaultFactorShouldBeFound("finalTax.in=" + DEFAULT_FINAL_TAX + "," + UPDATED_FINAL_TAX);

        // Get all the factorList where finalTax equals to UPDATED_FINAL_TAX
        defaultFactorShouldNotBeFound("finalTax.in=" + UPDATED_FINAL_TAX);
    }

    @Test
    void getAllFactorsByFinalTaxIsNullOrNotNull() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where finalTax is not null
        defaultFactorShouldBeFound("finalTax.specified=true");

        // Get all the factorList where finalTax is null
        defaultFactorShouldNotBeFound("finalTax.specified=false");
    }

    @Test
    void getAllFactorsByFinalTaxIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where finalTax is greater than or equal to DEFAULT_FINAL_TAX
        defaultFactorShouldBeFound("finalTax.greaterThanOrEqual=" + DEFAULT_FINAL_TAX);

        // Get all the factorList where finalTax is greater than or equal to UPDATED_FINAL_TAX
        defaultFactorShouldNotBeFound("finalTax.greaterThanOrEqual=" + UPDATED_FINAL_TAX);
    }

    @Test
    void getAllFactorsByFinalTaxIsLessThanOrEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where finalTax is less than or equal to DEFAULT_FINAL_TAX
        defaultFactorShouldBeFound("finalTax.lessThanOrEqual=" + DEFAULT_FINAL_TAX);

        // Get all the factorList where finalTax is less than or equal to SMALLER_FINAL_TAX
        defaultFactorShouldNotBeFound("finalTax.lessThanOrEqual=" + SMALLER_FINAL_TAX);
    }

    @Test
    void getAllFactorsByFinalTaxIsLessThanSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where finalTax is less than DEFAULT_FINAL_TAX
        defaultFactorShouldNotBeFound("finalTax.lessThan=" + DEFAULT_FINAL_TAX);

        // Get all the factorList where finalTax is less than UPDATED_FINAL_TAX
        defaultFactorShouldBeFound("finalTax.lessThan=" + UPDATED_FINAL_TAX);
    }

    @Test
    void getAllFactorsByFinalTaxIsGreaterThanSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where finalTax is greater than DEFAULT_FINAL_TAX
        defaultFactorShouldNotBeFound("finalTax.greaterThan=" + DEFAULT_FINAL_TAX);

        // Get all the factorList where finalTax is greater than SMALLER_FINAL_TAX
        defaultFactorShouldBeFound("finalTax.greaterThan=" + SMALLER_FINAL_TAX);
    }

    @Test
    void getAllFactorsByPayableIsEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where payable equals to DEFAULT_PAYABLE
        defaultFactorShouldBeFound("payable.equals=" + DEFAULT_PAYABLE);

        // Get all the factorList where payable equals to UPDATED_PAYABLE
        defaultFactorShouldNotBeFound("payable.equals=" + UPDATED_PAYABLE);
    }

    @Test
    void getAllFactorsByPayableIsInShouldWork() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where payable in DEFAULT_PAYABLE or UPDATED_PAYABLE
        defaultFactorShouldBeFound("payable.in=" + DEFAULT_PAYABLE + "," + UPDATED_PAYABLE);

        // Get all the factorList where payable equals to UPDATED_PAYABLE
        defaultFactorShouldNotBeFound("payable.in=" + UPDATED_PAYABLE);
    }

    @Test
    void getAllFactorsByPayableIsNullOrNotNull() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where payable is not null
        defaultFactorShouldBeFound("payable.specified=true");

        // Get all the factorList where payable is null
        defaultFactorShouldNotBeFound("payable.specified=false");
    }

    @Test
    void getAllFactorsByPayableIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where payable is greater than or equal to DEFAULT_PAYABLE
        defaultFactorShouldBeFound("payable.greaterThanOrEqual=" + DEFAULT_PAYABLE);

        // Get all the factorList where payable is greater than or equal to UPDATED_PAYABLE
        defaultFactorShouldNotBeFound("payable.greaterThanOrEqual=" + UPDATED_PAYABLE);
    }

    @Test
    void getAllFactorsByPayableIsLessThanOrEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where payable is less than or equal to DEFAULT_PAYABLE
        defaultFactorShouldBeFound("payable.lessThanOrEqual=" + DEFAULT_PAYABLE);

        // Get all the factorList where payable is less than or equal to SMALLER_PAYABLE
        defaultFactorShouldNotBeFound("payable.lessThanOrEqual=" + SMALLER_PAYABLE);
    }

    @Test
    void getAllFactorsByPayableIsLessThanSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where payable is less than DEFAULT_PAYABLE
        defaultFactorShouldNotBeFound("payable.lessThan=" + DEFAULT_PAYABLE);

        // Get all the factorList where payable is less than UPDATED_PAYABLE
        defaultFactorShouldBeFound("payable.lessThan=" + UPDATED_PAYABLE);
    }

    @Test
    void getAllFactorsByPayableIsGreaterThanSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where payable is greater than DEFAULT_PAYABLE
        defaultFactorShouldNotBeFound("payable.greaterThan=" + DEFAULT_PAYABLE);

        // Get all the factorList where payable is greater than SMALLER_PAYABLE
        defaultFactorShouldBeFound("payable.greaterThan=" + SMALLER_PAYABLE);
    }

    @Test
    void getAllFactorsByDescriptionIsEqualToSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where description equals to DEFAULT_DESCRIPTION
        defaultFactorShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the factorList where description equals to UPDATED_DESCRIPTION
        defaultFactorShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllFactorsByDescriptionIsInShouldWork() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFactorShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the factorList where description equals to UPDATED_DESCRIPTION
        defaultFactorShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllFactorsByDescriptionIsNullOrNotNull() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where description is not null
        defaultFactorShouldBeFound("description.specified=true");

        // Get all the factorList where description is null
        defaultFactorShouldNotBeFound("description.specified=false");
    }

    @Test
    void getAllFactorsByDescriptionContainsSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where description contains DEFAULT_DESCRIPTION
        defaultFactorShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the factorList where description contains UPDATED_DESCRIPTION
        defaultFactorShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllFactorsByDescriptionNotContainsSomething() {
        // Initialize the database
        factorRepository.save(factor).block();

        // Get all the factorList where description does not contain DEFAULT_DESCRIPTION
        defaultFactorShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the factorList where description does not contain UPDATED_DESCRIPTION
        defaultFactorShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllFactorsByBuyerPartyIsEqualToSomething() {
        Party buyerParty = PartyResourceIT.createEntity(em);
        partyRepository.save(buyerParty).block();
        Long buyerPartyId = buyerParty.getId();
        factor.setBuyerPartyId(buyerPartyId);
        factorRepository.save(factor).block();
        // Get all the factorList where buyerParty equals to buyerPartyId
        defaultFactorShouldBeFound("buyerPartyId.equals=" + buyerPartyId);

        // Get all the factorList where buyerParty equals to (buyerPartyId + 1)
        defaultFactorShouldNotBeFound("buyerPartyId.equals=" + (buyerPartyId + 1));
    }

    @Test
    void getAllFactorsBySellerPartyIsEqualToSomething() {
        Party sellerParty = PartyResourceIT.createEntity(em);
        partyRepository.save(sellerParty).block();
        Long sellerPartyId = sellerParty.getId();
        factor.setSellerPartyId(sellerPartyId);
        factorRepository.save(factor).block();
        // Get all the factorList where sellerParty equals to sellerPartyId
        defaultFactorShouldBeFound("sellerPartyId.equals=" + sellerPartyId);

        // Get all the factorList where sellerParty equals to (sellerPartyId + 1)
        defaultFactorShouldNotBeFound("sellerPartyId.equals=" + (sellerPartyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFactorShouldBeFound(String filter) {
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
            .value(hasItem(factor.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].factorCode")
            .value(hasItem(DEFAULT_FACTOR_CODE))
            .jsonPath("$.[*].lastStatusClassId")
            .value(hasItem(DEFAULT_LAST_STATUS_CLASS_ID.intValue()))
            .jsonPath("$.[*].paymentStateClassId")
            .value(hasItem(DEFAULT_PAYMENT_STATE_CLASS_ID.intValue()))
            .jsonPath("$.[*].categoryClassId")
            .value(hasItem(DEFAULT_CATEGORY_CLASS_ID.intValue()))
            .jsonPath("$.[*].totalPrice")
            .value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue()))
            .jsonPath("$.[*].discount")
            .value(hasItem(DEFAULT_DISCOUNT.doubleValue()))
            .jsonPath("$.[*].discountCode")
            .value(hasItem(DEFAULT_DISCOUNT_CODE))
            .jsonPath("$.[*].finalTax")
            .value(hasItem(DEFAULT_FINAL_TAX.doubleValue()))
            .jsonPath("$.[*].payable")
            .value(hasItem(DEFAULT_PAYABLE.doubleValue()))
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
    private void defaultFactorShouldNotBeFound(String filter) {
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
    void getNonExistingFactor() {
        // Get the factor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFactor() throws Exception {
        // Initialize the database
        factorRepository.save(factor).block();

        int databaseSizeBeforeUpdate = factorRepository.findAll().collectList().block().size();

        // Update the factor
        Factor updatedFactor = factorRepository.findById(factor.getId()).block();
        updatedFactor
            .title(UPDATED_TITLE)
            .factorCode(UPDATED_FACTOR_CODE)
            .lastStatusClassId(UPDATED_LAST_STATUS_CLASS_ID)
            .paymentStateClassId(UPDATED_PAYMENT_STATE_CLASS_ID)
            .categoryClassId(UPDATED_CATEGORY_CLASS_ID)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .discount(UPDATED_DISCOUNT)
            .discountCode(UPDATED_DISCOUNT_CODE)
            .finalTax(UPDATED_FINAL_TAX)
            .payable(UPDATED_PAYABLE)
            .description(UPDATED_DESCRIPTION);
        FactorDTO factorDTO = factorMapper.toDto(updatedFactor);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, factorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Factor in the database
        List<Factor> factorList = factorRepository.findAll().collectList().block();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate);
        Factor testFactor = factorList.get(factorList.size() - 1);
        assertThat(testFactor.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFactor.getFactorCode()).isEqualTo(UPDATED_FACTOR_CODE);
        assertThat(testFactor.getLastStatusClassId()).isEqualTo(UPDATED_LAST_STATUS_CLASS_ID);
        assertThat(testFactor.getPaymentStateClassId()).isEqualTo(UPDATED_PAYMENT_STATE_CLASS_ID);
        assertThat(testFactor.getCategoryClassId()).isEqualTo(UPDATED_CATEGORY_CLASS_ID);
        assertThat(testFactor.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testFactor.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testFactor.getDiscountCode()).isEqualTo(UPDATED_DISCOUNT_CODE);
        assertThat(testFactor.getFinalTax()).isEqualTo(UPDATED_FINAL_TAX);
        assertThat(testFactor.getPayable()).isEqualTo(UPDATED_PAYABLE);
        assertThat(testFactor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingFactor() throws Exception {
        int databaseSizeBeforeUpdate = factorRepository.findAll().collectList().block().size();
        factor.setId(longCount.incrementAndGet());

        // Create the Factor
        FactorDTO factorDTO = factorMapper.toDto(factor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, factorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Factor in the database
        List<Factor> factorList = factorRepository.findAll().collectList().block();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFactor() throws Exception {
        int databaseSizeBeforeUpdate = factorRepository.findAll().collectList().block().size();
        factor.setId(longCount.incrementAndGet());

        // Create the Factor
        FactorDTO factorDTO = factorMapper.toDto(factor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Factor in the database
        List<Factor> factorList = factorRepository.findAll().collectList().block();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFactor() throws Exception {
        int databaseSizeBeforeUpdate = factorRepository.findAll().collectList().block().size();
        factor.setId(longCount.incrementAndGet());

        // Create the Factor
        FactorDTO factorDTO = factorMapper.toDto(factor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Factor in the database
        List<Factor> factorList = factorRepository.findAll().collectList().block();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFactorWithPatch() throws Exception {
        // Initialize the database
        factorRepository.save(factor).block();

        int databaseSizeBeforeUpdate = factorRepository.findAll().collectList().block().size();

        // Update the factor using partial update
        Factor partialUpdatedFactor = new Factor();
        partialUpdatedFactor.setId(factor.getId());

        partialUpdatedFactor
            .title(UPDATED_TITLE)
            .factorCode(UPDATED_FACTOR_CODE)
            .lastStatusClassId(UPDATED_LAST_STATUS_CLASS_ID)
            .paymentStateClassId(UPDATED_PAYMENT_STATE_CLASS_ID)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .discount(UPDATED_DISCOUNT)
            .description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFactor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFactor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Factor in the database
        List<Factor> factorList = factorRepository.findAll().collectList().block();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate);
        Factor testFactor = factorList.get(factorList.size() - 1);
        assertThat(testFactor.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFactor.getFactorCode()).isEqualTo(UPDATED_FACTOR_CODE);
        assertThat(testFactor.getLastStatusClassId()).isEqualTo(UPDATED_LAST_STATUS_CLASS_ID);
        assertThat(testFactor.getPaymentStateClassId()).isEqualTo(UPDATED_PAYMENT_STATE_CLASS_ID);
        assertThat(testFactor.getCategoryClassId()).isEqualTo(DEFAULT_CATEGORY_CLASS_ID);
        assertThat(testFactor.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testFactor.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testFactor.getDiscountCode()).isEqualTo(DEFAULT_DISCOUNT_CODE);
        assertThat(testFactor.getFinalTax()).isEqualTo(DEFAULT_FINAL_TAX);
        assertThat(testFactor.getPayable()).isEqualTo(DEFAULT_PAYABLE);
        assertThat(testFactor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void fullUpdateFactorWithPatch() throws Exception {
        // Initialize the database
        factorRepository.save(factor).block();

        int databaseSizeBeforeUpdate = factorRepository.findAll().collectList().block().size();

        // Update the factor using partial update
        Factor partialUpdatedFactor = new Factor();
        partialUpdatedFactor.setId(factor.getId());

        partialUpdatedFactor
            .title(UPDATED_TITLE)
            .factorCode(UPDATED_FACTOR_CODE)
            .lastStatusClassId(UPDATED_LAST_STATUS_CLASS_ID)
            .paymentStateClassId(UPDATED_PAYMENT_STATE_CLASS_ID)
            .categoryClassId(UPDATED_CATEGORY_CLASS_ID)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .discount(UPDATED_DISCOUNT)
            .discountCode(UPDATED_DISCOUNT_CODE)
            .finalTax(UPDATED_FINAL_TAX)
            .payable(UPDATED_PAYABLE)
            .description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFactor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFactor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Factor in the database
        List<Factor> factorList = factorRepository.findAll().collectList().block();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate);
        Factor testFactor = factorList.get(factorList.size() - 1);
        assertThat(testFactor.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFactor.getFactorCode()).isEqualTo(UPDATED_FACTOR_CODE);
        assertThat(testFactor.getLastStatusClassId()).isEqualTo(UPDATED_LAST_STATUS_CLASS_ID);
        assertThat(testFactor.getPaymentStateClassId()).isEqualTo(UPDATED_PAYMENT_STATE_CLASS_ID);
        assertThat(testFactor.getCategoryClassId()).isEqualTo(UPDATED_CATEGORY_CLASS_ID);
        assertThat(testFactor.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testFactor.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testFactor.getDiscountCode()).isEqualTo(UPDATED_DISCOUNT_CODE);
        assertThat(testFactor.getFinalTax()).isEqualTo(UPDATED_FINAL_TAX);
        assertThat(testFactor.getPayable()).isEqualTo(UPDATED_PAYABLE);
        assertThat(testFactor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingFactor() throws Exception {
        int databaseSizeBeforeUpdate = factorRepository.findAll().collectList().block().size();
        factor.setId(longCount.incrementAndGet());

        // Create the Factor
        FactorDTO factorDTO = factorMapper.toDto(factor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, factorDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Factor in the database
        List<Factor> factorList = factorRepository.findAll().collectList().block();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFactor() throws Exception {
        int databaseSizeBeforeUpdate = factorRepository.findAll().collectList().block().size();
        factor.setId(longCount.incrementAndGet());

        // Create the Factor
        FactorDTO factorDTO = factorMapper.toDto(factor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Factor in the database
        List<Factor> factorList = factorRepository.findAll().collectList().block();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFactor() throws Exception {
        int databaseSizeBeforeUpdate = factorRepository.findAll().collectList().block().size();
        factor.setId(longCount.incrementAndGet());

        // Create the Factor
        FactorDTO factorDTO = factorMapper.toDto(factor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Factor in the database
        List<Factor> factorList = factorRepository.findAll().collectList().block();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFactor() {
        // Initialize the database
        factorRepository.save(factor).block();

        int databaseSizeBeforeDelete = factorRepository.findAll().collectList().block().size();

        // Delete the factor
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, factor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Factor> factorList = factorRepository.findAll().collectList().block();
        assertThat(factorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
