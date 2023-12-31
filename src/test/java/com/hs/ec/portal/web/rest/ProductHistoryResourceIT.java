package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.ProductHistory;
import com.hs.ec.portal.domain.enumeration.Performance;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.repository.ProductHistoryRepository;
import com.hs.ec.portal.service.dto.ProductHistoryDTO;
import com.hs.ec.portal.service.mapper.ProductHistoryMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ProductHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProductHistoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_TYPE_CLASS_ID = 1L;
    private static final Long UPDATED_TYPE_CLASS_ID = 2L;
    private static final Long SMALLER_TYPE_CLASS_ID = 1L - 1L;

    private static final Long DEFAULT_BRAND_CLASS_ID = 1L;
    private static final Long UPDATED_BRAND_CLASS_ID = 2L;
    private static final Long SMALLER_BRAND_CLASS_ID = 1L - 1L;

    private static final String DEFAULT_SIZEE = "AAAAAAAAAA";
    private static final String UPDATED_SIZEE = "BBBBBBBBBB";

    private static final Long DEFAULT_REGULAR_SIZE_CLASS_ID = 1L;
    private static final Long UPDATED_REGULAR_SIZE_CLASS_ID = 2L;
    private static final Long SMALLER_REGULAR_SIZE_CLASS_ID = 1L - 1L;

    private static final Long DEFAULT_LANGUAGE_CLASS_ID = 1L;
    private static final Long UPDATED_LANGUAGE_CLASS_ID = 2L;
    private static final Long SMALLER_LANGUAGE_CLASS_ID = 1L - 1L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_KEYWORDS = "AAAAAAAAAA";
    private static final String UPDATED_KEYWORDS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PHOTO_1 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO_1 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_1_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_1_CONTENT_TYPE = "image/png";

    private static final Double DEFAULT_COUNT = 1D;
    private static final Double UPDATED_COUNT = 2D;
    private static final Double SMALLER_COUNT = 1D - 1D;

    private static final Float DEFAULT_DISCOUNT = 1F;
    private static final Float UPDATED_DISCOUNT = 2F;
    private static final Float SMALLER_DISCOUNT = 1F - 1F;

    private static final Double DEFAULT_ORIGINAL_PRICE = 1D;
    private static final Double UPDATED_ORIGINAL_PRICE = 2D;
    private static final Double SMALLER_ORIGINAL_PRICE = 1D - 1D;

    private static final Double DEFAULT_FINAL_PRICE = 1D;
    private static final Double UPDATED_FINAL_PRICE = 2D;
    private static final Double SMALLER_FINAL_PRICE = 1D - 1D;

    private static final LocalDate DEFAULT_PUBLISH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLISH_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PUBLISH_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_TRANSPORT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSPORT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRANSPORT_DATE = LocalDate.ofEpochDay(-1L);

    private static final Long DEFAULT_CURRENCY_CLASS_ID = 1L;
    private static final Long UPDATED_CURRENCY_CLASS_ID = 2L;
    private static final Long SMALLER_CURRENCY_CLASS_ID = 1L - 1L;

    private static final Float DEFAULT_BONUS = 1F;
    private static final Float UPDATED_BONUS = 2F;
    private static final Float SMALLER_BONUS = 1F - 1F;

    private static final Long DEFAULT_WARRANTY_CLASS_ID = 1L;
    private static final Long UPDATED_WARRANTY_CLASS_ID = 2L;
    private static final Long SMALLER_WARRANTY_CLASS_ID = 1L - 1L;

    private static final Long DEFAULT_DELIVERY_PLACE_CLASS_ID = 1L;
    private static final Long UPDATED_DELIVERY_PLACE_CLASS_ID = 2L;
    private static final Long SMALLER_DELIVERY_PLACE_CLASS_ID = 1L - 1L;

    private static final Long DEFAULT_PAYMENT_PLACE_CLASS_ID = 1L;
    private static final Long UPDATED_PAYMENT_PLACE_CLASS_ID = 2L;
    private static final Long SMALLER_PAYMENT_PLACE_CLASS_ID = 1L - 1L;

    private static final Performance DEFAULT_PERFORMANCE = Performance.VERY_WEAK;
    private static final Performance UPDATED_PERFORMANCE = Performance.WEAK;

    private static final Long DEFAULT_ORIGINALITY_CLASS_ID = 1L;
    private static final Long UPDATED_ORIGINALITY_CLASS_ID = 2L;
    private static final Long SMALLER_ORIGINALITY_CLASS_ID = 1L - 1L;

    private static final Float DEFAULT_SATISFACTION = 1F;
    private static final Float UPDATED_SATISFACTION = 2F;
    private static final Float SMALLER_SATISFACTION = 1F - 1F;

    private static final Boolean DEFAULT_USED = false;
    private static final Boolean UPDATED_USED = true;

    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final Long UPDATED_CATEGORY_ID = 2L;
    private static final Long SMALLER_CATEGORY_ID = 1L - 1L;

    private static final Long DEFAULT_PARTY_ID = 1L;
    private static final Long UPDATED_PARTY_ID = 2L;
    private static final Long SMALLER_PARTY_ID = 1L - 1L;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;
    private static final Long SMALLER_PRODUCT_ID = 1L - 1L;

    private static final Long DEFAULT_PRICE_ID = 1L;
    private static final Long UPDATED_PRICE_ID = 2L;
    private static final Long SMALLER_PRICE_ID = 1L - 1L;

    private static final Long DEFAULT_CAMPAIGN_ID = 1L;
    private static final Long UPDATED_CAMPAIGN_ID = 2L;
    private static final Long SMALLER_CAMPAIGN_ID = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/product-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    @Autowired
    private ProductHistoryMapper productHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ProductHistory productHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductHistory createEntity(EntityManager em) {
        ProductHistory productHistory = new ProductHistory()
            .name(DEFAULT_NAME)
            .typeClassId(DEFAULT_TYPE_CLASS_ID)
            .brandClassId(DEFAULT_BRAND_CLASS_ID)
            .sizee(DEFAULT_SIZEE)
            .regularSizeClassId(DEFAULT_REGULAR_SIZE_CLASS_ID)
            .languageClassId(DEFAULT_LANGUAGE_CLASS_ID)
            .description(DEFAULT_DESCRIPTION)
            .keywords(DEFAULT_KEYWORDS)
            .photo1(DEFAULT_PHOTO_1)
            .photo1ContentType(DEFAULT_PHOTO_1_CONTENT_TYPE)
            .count(DEFAULT_COUNT)
            .discount(DEFAULT_DISCOUNT)
            .originalPrice(DEFAULT_ORIGINAL_PRICE)
            .finalPrice(DEFAULT_FINAL_PRICE)
            .publishDate(DEFAULT_PUBLISH_DATE)
            .transportDate(DEFAULT_TRANSPORT_DATE)
            .currencyClassId(DEFAULT_CURRENCY_CLASS_ID)
            .bonus(DEFAULT_BONUS)
            .warrantyClassId(DEFAULT_WARRANTY_CLASS_ID)
            .deliveryPlaceClassId(DEFAULT_DELIVERY_PLACE_CLASS_ID)
            .paymentPlaceClassId(DEFAULT_PAYMENT_PLACE_CLASS_ID)
            .performance(DEFAULT_PERFORMANCE)
            .originalityClassId(DEFAULT_ORIGINALITY_CLASS_ID)
            .satisfaction(DEFAULT_SATISFACTION)
            .used(DEFAULT_USED)
            .categoryId(DEFAULT_CATEGORY_ID)
            .partyId(DEFAULT_PARTY_ID)
            .productId(DEFAULT_PRODUCT_ID)
            .priceId(DEFAULT_PRICE_ID)
            .campaignId(DEFAULT_CAMPAIGN_ID);
        return productHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductHistory createUpdatedEntity(EntityManager em) {
        ProductHistory productHistory = new ProductHistory()
            .name(UPDATED_NAME)
            .typeClassId(UPDATED_TYPE_CLASS_ID)
            .brandClassId(UPDATED_BRAND_CLASS_ID)
            .sizee(UPDATED_SIZEE)
            .regularSizeClassId(UPDATED_REGULAR_SIZE_CLASS_ID)
            .languageClassId(UPDATED_LANGUAGE_CLASS_ID)
            .description(UPDATED_DESCRIPTION)
            .keywords(UPDATED_KEYWORDS)
            .photo1(UPDATED_PHOTO_1)
            .photo1ContentType(UPDATED_PHOTO_1_CONTENT_TYPE)
            .count(UPDATED_COUNT)
            .discount(UPDATED_DISCOUNT)
            .originalPrice(UPDATED_ORIGINAL_PRICE)
            .finalPrice(UPDATED_FINAL_PRICE)
            .publishDate(UPDATED_PUBLISH_DATE)
            .transportDate(UPDATED_TRANSPORT_DATE)
            .currencyClassId(UPDATED_CURRENCY_CLASS_ID)
            .bonus(UPDATED_BONUS)
            .warrantyClassId(UPDATED_WARRANTY_CLASS_ID)
            .deliveryPlaceClassId(UPDATED_DELIVERY_PLACE_CLASS_ID)
            .paymentPlaceClassId(UPDATED_PAYMENT_PLACE_CLASS_ID)
            .performance(UPDATED_PERFORMANCE)
            .originalityClassId(UPDATED_ORIGINALITY_CLASS_ID)
            .satisfaction(UPDATED_SATISFACTION)
            .used(UPDATED_USED)
            .categoryId(UPDATED_CATEGORY_ID)
            .partyId(UPDATED_PARTY_ID)
            .productId(UPDATED_PRODUCT_ID)
            .priceId(UPDATED_PRICE_ID)
            .campaignId(UPDATED_CAMPAIGN_ID);
        return productHistory;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ProductHistory.class).block();
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
        productHistory = createEntity(em);
    }

    @Test
    void createProductHistory() throws Exception {
        int databaseSizeBeforeCreate = productHistoryRepository.findAll().collectList().block().size();
        // Create the ProductHistory
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ProductHistory in the database
        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        ProductHistory testProductHistory = productHistoryList.get(productHistoryList.size() - 1);
        assertThat(testProductHistory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProductHistory.getTypeClassId()).isEqualTo(DEFAULT_TYPE_CLASS_ID);
        assertThat(testProductHistory.getBrandClassId()).isEqualTo(DEFAULT_BRAND_CLASS_ID);
        assertThat(testProductHistory.getSizee()).isEqualTo(DEFAULT_SIZEE);
        assertThat(testProductHistory.getRegularSizeClassId()).isEqualTo(DEFAULT_REGULAR_SIZE_CLASS_ID);
        assertThat(testProductHistory.getLanguageClassId()).isEqualTo(DEFAULT_LANGUAGE_CLASS_ID);
        assertThat(testProductHistory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProductHistory.getKeywords()).isEqualTo(DEFAULT_KEYWORDS);
        assertThat(testProductHistory.getPhoto1()).isEqualTo(DEFAULT_PHOTO_1);
        assertThat(testProductHistory.getPhoto1ContentType()).isEqualTo(DEFAULT_PHOTO_1_CONTENT_TYPE);
        assertThat(testProductHistory.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testProductHistory.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testProductHistory.getOriginalPrice()).isEqualTo(DEFAULT_ORIGINAL_PRICE);
        assertThat(testProductHistory.getFinalPrice()).isEqualTo(DEFAULT_FINAL_PRICE);
        assertThat(testProductHistory.getPublishDate()).isEqualTo(DEFAULT_PUBLISH_DATE);
        assertThat(testProductHistory.getTransportDate()).isEqualTo(DEFAULT_TRANSPORT_DATE);
        assertThat(testProductHistory.getCurrencyClassId()).isEqualTo(DEFAULT_CURRENCY_CLASS_ID);
        assertThat(testProductHistory.getBonus()).isEqualTo(DEFAULT_BONUS);
        assertThat(testProductHistory.getWarrantyClassId()).isEqualTo(DEFAULT_WARRANTY_CLASS_ID);
        assertThat(testProductHistory.getDeliveryPlaceClassId()).isEqualTo(DEFAULT_DELIVERY_PLACE_CLASS_ID);
        assertThat(testProductHistory.getPaymentPlaceClassId()).isEqualTo(DEFAULT_PAYMENT_PLACE_CLASS_ID);
        assertThat(testProductHistory.getPerformance()).isEqualTo(DEFAULT_PERFORMANCE);
        assertThat(testProductHistory.getOriginalityClassId()).isEqualTo(DEFAULT_ORIGINALITY_CLASS_ID);
        assertThat(testProductHistory.getSatisfaction()).isEqualTo(DEFAULT_SATISFACTION);
        assertThat(testProductHistory.getUsed()).isEqualTo(DEFAULT_USED);
        assertThat(testProductHistory.getCategoryId()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testProductHistory.getPartyId()).isEqualTo(DEFAULT_PARTY_ID);
        assertThat(testProductHistory.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testProductHistory.getPriceId()).isEqualTo(DEFAULT_PRICE_ID);
        assertThat(testProductHistory.getCampaignId()).isEqualTo(DEFAULT_CAMPAIGN_ID);
    }

    @Test
    void createProductHistoryWithExistingId() throws Exception {
        // Create the ProductHistory with an existing ID
        productHistory.setId(1L);
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        int databaseSizeBeforeCreate = productHistoryRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductHistory in the database
        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productHistoryRepository.findAll().collectList().block().size();
        // set the field null
        productHistory.setName(null);

        // Create the ProductHistory, which fails.
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkRegularSizeClassIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productHistoryRepository.findAll().collectList().block().size();
        // set the field null
        productHistory.setRegularSizeClassId(null);

        // Create the ProductHistory, which fails.
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLanguageClassIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productHistoryRepository.findAll().collectList().block().size();
        // set the field null
        productHistory.setLanguageClassId(null);

        // Create the ProductHistory, which fails.
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = productHistoryRepository.findAll().collectList().block().size();
        // set the field null
        productHistory.setCount(null);

        // Create the ProductHistory, which fails.
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkOriginalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productHistoryRepository.findAll().collectList().block().size();
        // set the field null
        productHistory.setOriginalPrice(null);

        // Create the ProductHistory, which fails.
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFinalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productHistoryRepository.findAll().collectList().block().size();
        // set the field null
        productHistory.setFinalPrice(null);

        // Create the ProductHistory, which fails.
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPublishDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = productHistoryRepository.findAll().collectList().block().size();
        // set the field null
        productHistory.setPublishDate(null);

        // Create the ProductHistory, which fails.
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTransportDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = productHistoryRepository.findAll().collectList().block().size();
        // set the field null
        productHistory.setTransportDate(null);

        // Create the ProductHistory, which fails.
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCurrencyClassIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productHistoryRepository.findAll().collectList().block().size();
        // set the field null
        productHistory.setCurrencyClassId(null);

        // Create the ProductHistory, which fails.
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUsedIsRequired() throws Exception {
        int databaseSizeBeforeTest = productHistoryRepository.findAll().collectList().block().size();
        // set the field null
        productHistory.setUsed(null);

        // Create the ProductHistory, which fails.
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCategoryIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productHistoryRepository.findAll().collectList().block().size();
        // set the field null
        productHistory.setCategoryId(null);

        // Create the ProductHistory, which fails.
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPartyIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productHistoryRepository.findAll().collectList().block().size();
        // set the field null
        productHistory.setPartyId(null);

        // Create the ProductHistory, which fails.
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkProductIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productHistoryRepository.findAll().collectList().block().size();
        // set the field null
        productHistory.setProductId(null);

        // Create the ProductHistory, which fails.
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPriceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productHistoryRepository.findAll().collectList().block().size();
        // set the field null
        productHistory.setPriceId(null);

        // Create the ProductHistory, which fails.
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProductHistories() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList
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
            .value(hasItem(productHistory.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].typeClassId")
            .value(hasItem(DEFAULT_TYPE_CLASS_ID.intValue()))
            .jsonPath("$.[*].brandClassId")
            .value(hasItem(DEFAULT_BRAND_CLASS_ID.intValue()))
            .jsonPath("$.[*].sizee")
            .value(hasItem(DEFAULT_SIZEE))
            .jsonPath("$.[*].regularSizeClassId")
            .value(hasItem(DEFAULT_REGULAR_SIZE_CLASS_ID.intValue()))
            .jsonPath("$.[*].languageClassId")
            .value(hasItem(DEFAULT_LANGUAGE_CLASS_ID.intValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].keywords")
            .value(hasItem(DEFAULT_KEYWORDS))
            .jsonPath("$.[*].photo1ContentType")
            .value(hasItem(DEFAULT_PHOTO_1_CONTENT_TYPE))
            .jsonPath("$.[*].photo1")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO_1)))
            .jsonPath("$.[*].count")
            .value(hasItem(DEFAULT_COUNT.doubleValue()))
            .jsonPath("$.[*].discount")
            .value(hasItem(DEFAULT_DISCOUNT.doubleValue()))
            .jsonPath("$.[*].originalPrice")
            .value(hasItem(DEFAULT_ORIGINAL_PRICE.doubleValue()))
            .jsonPath("$.[*].finalPrice")
            .value(hasItem(DEFAULT_FINAL_PRICE.doubleValue()))
            .jsonPath("$.[*].publishDate")
            .value(hasItem(DEFAULT_PUBLISH_DATE.toString()))
            .jsonPath("$.[*].transportDate")
            .value(hasItem(DEFAULT_TRANSPORT_DATE.toString()))
            .jsonPath("$.[*].currencyClassId")
            .value(hasItem(DEFAULT_CURRENCY_CLASS_ID.intValue()))
            .jsonPath("$.[*].bonus")
            .value(hasItem(DEFAULT_BONUS.doubleValue()))
            .jsonPath("$.[*].warrantyClassId")
            .value(hasItem(DEFAULT_WARRANTY_CLASS_ID.intValue()))
            .jsonPath("$.[*].deliveryPlaceClassId")
            .value(hasItem(DEFAULT_DELIVERY_PLACE_CLASS_ID.intValue()))
            .jsonPath("$.[*].paymentPlaceClassId")
            .value(hasItem(DEFAULT_PAYMENT_PLACE_CLASS_ID.intValue()))
            .jsonPath("$.[*].performance")
            .value(hasItem(DEFAULT_PERFORMANCE.toString()))
            .jsonPath("$.[*].originalityClassId")
            .value(hasItem(DEFAULT_ORIGINALITY_CLASS_ID.intValue()))
            .jsonPath("$.[*].satisfaction")
            .value(hasItem(DEFAULT_SATISFACTION.doubleValue()))
            .jsonPath("$.[*].used")
            .value(hasItem(DEFAULT_USED.booleanValue()))
            .jsonPath("$.[*].categoryId")
            .value(hasItem(DEFAULT_CATEGORY_ID.intValue()))
            .jsonPath("$.[*].partyId")
            .value(hasItem(DEFAULT_PARTY_ID.intValue()))
            .jsonPath("$.[*].productId")
            .value(hasItem(DEFAULT_PRODUCT_ID.intValue()))
            .jsonPath("$.[*].priceId")
            .value(hasItem(DEFAULT_PRICE_ID.intValue()))
            .jsonPath("$.[*].campaignId")
            .value(hasItem(DEFAULT_CAMPAIGN_ID.intValue()));
    }

    @Test
    void getProductHistory() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get the productHistory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, productHistory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(productHistory.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.typeClassId")
            .value(is(DEFAULT_TYPE_CLASS_ID.intValue()))
            .jsonPath("$.brandClassId")
            .value(is(DEFAULT_BRAND_CLASS_ID.intValue()))
            .jsonPath("$.sizee")
            .value(is(DEFAULT_SIZEE))
            .jsonPath("$.regularSizeClassId")
            .value(is(DEFAULT_REGULAR_SIZE_CLASS_ID.intValue()))
            .jsonPath("$.languageClassId")
            .value(is(DEFAULT_LANGUAGE_CLASS_ID.intValue()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.keywords")
            .value(is(DEFAULT_KEYWORDS))
            .jsonPath("$.photo1ContentType")
            .value(is(DEFAULT_PHOTO_1_CONTENT_TYPE))
            .jsonPath("$.photo1")
            .value(is(Base64Utils.encodeToString(DEFAULT_PHOTO_1)))
            .jsonPath("$.count")
            .value(is(DEFAULT_COUNT.doubleValue()))
            .jsonPath("$.discount")
            .value(is(DEFAULT_DISCOUNT.doubleValue()))
            .jsonPath("$.originalPrice")
            .value(is(DEFAULT_ORIGINAL_PRICE.doubleValue()))
            .jsonPath("$.finalPrice")
            .value(is(DEFAULT_FINAL_PRICE.doubleValue()))
            .jsonPath("$.publishDate")
            .value(is(DEFAULT_PUBLISH_DATE.toString()))
            .jsonPath("$.transportDate")
            .value(is(DEFAULT_TRANSPORT_DATE.toString()))
            .jsonPath("$.currencyClassId")
            .value(is(DEFAULT_CURRENCY_CLASS_ID.intValue()))
            .jsonPath("$.bonus")
            .value(is(DEFAULT_BONUS.doubleValue()))
            .jsonPath("$.warrantyClassId")
            .value(is(DEFAULT_WARRANTY_CLASS_ID.intValue()))
            .jsonPath("$.deliveryPlaceClassId")
            .value(is(DEFAULT_DELIVERY_PLACE_CLASS_ID.intValue()))
            .jsonPath("$.paymentPlaceClassId")
            .value(is(DEFAULT_PAYMENT_PLACE_CLASS_ID.intValue()))
            .jsonPath("$.performance")
            .value(is(DEFAULT_PERFORMANCE.toString()))
            .jsonPath("$.originalityClassId")
            .value(is(DEFAULT_ORIGINALITY_CLASS_ID.intValue()))
            .jsonPath("$.satisfaction")
            .value(is(DEFAULT_SATISFACTION.doubleValue()))
            .jsonPath("$.used")
            .value(is(DEFAULT_USED.booleanValue()))
            .jsonPath("$.categoryId")
            .value(is(DEFAULT_CATEGORY_ID.intValue()))
            .jsonPath("$.partyId")
            .value(is(DEFAULT_PARTY_ID.intValue()))
            .jsonPath("$.productId")
            .value(is(DEFAULT_PRODUCT_ID.intValue()))
            .jsonPath("$.priceId")
            .value(is(DEFAULT_PRICE_ID.intValue()))
            .jsonPath("$.campaignId")
            .value(is(DEFAULT_CAMPAIGN_ID.intValue()));
    }

    @Test
    void getProductHistoriesByIdFiltering() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        Long id = productHistory.getId();

        defaultProductHistoryShouldBeFound("id.equals=" + id);
        defaultProductHistoryShouldNotBeFound("id.notEquals=" + id);

        defaultProductHistoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductHistoryShouldNotBeFound("id.greaterThan=" + id);

        defaultProductHistoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductHistoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllProductHistoriesByNameIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where name equals to DEFAULT_NAME
        defaultProductHistoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the productHistoryList where name equals to UPDATED_NAME
        defaultProductHistoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    void getAllProductHistoriesByNameIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProductHistoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the productHistoryList where name equals to UPDATED_NAME
        defaultProductHistoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    void getAllProductHistoriesByNameIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where name is not null
        defaultProductHistoryShouldBeFound("name.specified=true");

        // Get all the productHistoryList where name is null
        defaultProductHistoryShouldNotBeFound("name.specified=false");
    }

    @Test
    void getAllProductHistoriesByNameContainsSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where name contains DEFAULT_NAME
        defaultProductHistoryShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the productHistoryList where name contains UPDATED_NAME
        defaultProductHistoryShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    void getAllProductHistoriesByNameNotContainsSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where name does not contain DEFAULT_NAME
        defaultProductHistoryShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the productHistoryList where name does not contain UPDATED_NAME
        defaultProductHistoryShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    void getAllProductHistoriesByTypeClassIdIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where typeClassId equals to DEFAULT_TYPE_CLASS_ID
        defaultProductHistoryShouldBeFound("typeClassId.equals=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the productHistoryList where typeClassId equals to UPDATED_TYPE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("typeClassId.equals=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByTypeClassIdIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where typeClassId in DEFAULT_TYPE_CLASS_ID or UPDATED_TYPE_CLASS_ID
        defaultProductHistoryShouldBeFound("typeClassId.in=" + DEFAULT_TYPE_CLASS_ID + "," + UPDATED_TYPE_CLASS_ID);

        // Get all the productHistoryList where typeClassId equals to UPDATED_TYPE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("typeClassId.in=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByTypeClassIdIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where typeClassId is not null
        defaultProductHistoryShouldBeFound("typeClassId.specified=true");

        // Get all the productHistoryList where typeClassId is null
        defaultProductHistoryShouldNotBeFound("typeClassId.specified=false");
    }

    @Test
    void getAllProductHistoriesByTypeClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where typeClassId is greater than or equal to DEFAULT_TYPE_CLASS_ID
        defaultProductHistoryShouldBeFound("typeClassId.greaterThanOrEqual=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the productHistoryList where typeClassId is greater than or equal to UPDATED_TYPE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("typeClassId.greaterThanOrEqual=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByTypeClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where typeClassId is less than or equal to DEFAULT_TYPE_CLASS_ID
        defaultProductHistoryShouldBeFound("typeClassId.lessThanOrEqual=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the productHistoryList where typeClassId is less than or equal to SMALLER_TYPE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("typeClassId.lessThanOrEqual=" + SMALLER_TYPE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByTypeClassIdIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where typeClassId is less than DEFAULT_TYPE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("typeClassId.lessThan=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the productHistoryList where typeClassId is less than UPDATED_TYPE_CLASS_ID
        defaultProductHistoryShouldBeFound("typeClassId.lessThan=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByTypeClassIdIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where typeClassId is greater than DEFAULT_TYPE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("typeClassId.greaterThan=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the productHistoryList where typeClassId is greater than SMALLER_TYPE_CLASS_ID
        defaultProductHistoryShouldBeFound("typeClassId.greaterThan=" + SMALLER_TYPE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByBrandClassIdIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where brandClassId equals to DEFAULT_BRAND_CLASS_ID
        defaultProductHistoryShouldBeFound("brandClassId.equals=" + DEFAULT_BRAND_CLASS_ID);

        // Get all the productHistoryList where brandClassId equals to UPDATED_BRAND_CLASS_ID
        defaultProductHistoryShouldNotBeFound("brandClassId.equals=" + UPDATED_BRAND_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByBrandClassIdIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where brandClassId in DEFAULT_BRAND_CLASS_ID or UPDATED_BRAND_CLASS_ID
        defaultProductHistoryShouldBeFound("brandClassId.in=" + DEFAULT_BRAND_CLASS_ID + "," + UPDATED_BRAND_CLASS_ID);

        // Get all the productHistoryList where brandClassId equals to UPDATED_BRAND_CLASS_ID
        defaultProductHistoryShouldNotBeFound("brandClassId.in=" + UPDATED_BRAND_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByBrandClassIdIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where brandClassId is not null
        defaultProductHistoryShouldBeFound("brandClassId.specified=true");

        // Get all the productHistoryList where brandClassId is null
        defaultProductHistoryShouldNotBeFound("brandClassId.specified=false");
    }

    @Test
    void getAllProductHistoriesByBrandClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where brandClassId is greater than or equal to DEFAULT_BRAND_CLASS_ID
        defaultProductHistoryShouldBeFound("brandClassId.greaterThanOrEqual=" + DEFAULT_BRAND_CLASS_ID);

        // Get all the productHistoryList where brandClassId is greater than or equal to UPDATED_BRAND_CLASS_ID
        defaultProductHistoryShouldNotBeFound("brandClassId.greaterThanOrEqual=" + UPDATED_BRAND_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByBrandClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where brandClassId is less than or equal to DEFAULT_BRAND_CLASS_ID
        defaultProductHistoryShouldBeFound("brandClassId.lessThanOrEqual=" + DEFAULT_BRAND_CLASS_ID);

        // Get all the productHistoryList where brandClassId is less than or equal to SMALLER_BRAND_CLASS_ID
        defaultProductHistoryShouldNotBeFound("brandClassId.lessThanOrEqual=" + SMALLER_BRAND_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByBrandClassIdIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where brandClassId is less than DEFAULT_BRAND_CLASS_ID
        defaultProductHistoryShouldNotBeFound("brandClassId.lessThan=" + DEFAULT_BRAND_CLASS_ID);

        // Get all the productHistoryList where brandClassId is less than UPDATED_BRAND_CLASS_ID
        defaultProductHistoryShouldBeFound("brandClassId.lessThan=" + UPDATED_BRAND_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByBrandClassIdIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where brandClassId is greater than DEFAULT_BRAND_CLASS_ID
        defaultProductHistoryShouldNotBeFound("brandClassId.greaterThan=" + DEFAULT_BRAND_CLASS_ID);

        // Get all the productHistoryList where brandClassId is greater than SMALLER_BRAND_CLASS_ID
        defaultProductHistoryShouldBeFound("brandClassId.greaterThan=" + SMALLER_BRAND_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesBySizeeIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where sizee equals to DEFAULT_SIZEE
        defaultProductHistoryShouldBeFound("sizee.equals=" + DEFAULT_SIZEE);

        // Get all the productHistoryList where sizee equals to UPDATED_SIZEE
        defaultProductHistoryShouldNotBeFound("sizee.equals=" + UPDATED_SIZEE);
    }

    @Test
    void getAllProductHistoriesBySizeeIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where sizee in DEFAULT_SIZEE or UPDATED_SIZEE
        defaultProductHistoryShouldBeFound("sizee.in=" + DEFAULT_SIZEE + "," + UPDATED_SIZEE);

        // Get all the productHistoryList where sizee equals to UPDATED_SIZEE
        defaultProductHistoryShouldNotBeFound("sizee.in=" + UPDATED_SIZEE);
    }

    @Test
    void getAllProductHistoriesBySizeeIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where sizee is not null
        defaultProductHistoryShouldBeFound("sizee.specified=true");

        // Get all the productHistoryList where sizee is null
        defaultProductHistoryShouldNotBeFound("sizee.specified=false");
    }

    @Test
    void getAllProductHistoriesBySizeeContainsSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where sizee contains DEFAULT_SIZEE
        defaultProductHistoryShouldBeFound("sizee.contains=" + DEFAULT_SIZEE);

        // Get all the productHistoryList where sizee contains UPDATED_SIZEE
        defaultProductHistoryShouldNotBeFound("sizee.contains=" + UPDATED_SIZEE);
    }

    @Test
    void getAllProductHistoriesBySizeeNotContainsSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where sizee does not contain DEFAULT_SIZEE
        defaultProductHistoryShouldNotBeFound("sizee.doesNotContain=" + DEFAULT_SIZEE);

        // Get all the productHistoryList where sizee does not contain UPDATED_SIZEE
        defaultProductHistoryShouldBeFound("sizee.doesNotContain=" + UPDATED_SIZEE);
    }

    @Test
    void getAllProductHistoriesByRegularSizeClassIdIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where regularSizeClassId equals to DEFAULT_REGULAR_SIZE_CLASS_ID
        defaultProductHistoryShouldBeFound("regularSizeClassId.equals=" + DEFAULT_REGULAR_SIZE_CLASS_ID);

        // Get all the productHistoryList where regularSizeClassId equals to UPDATED_REGULAR_SIZE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("regularSizeClassId.equals=" + UPDATED_REGULAR_SIZE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByRegularSizeClassIdIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where regularSizeClassId in DEFAULT_REGULAR_SIZE_CLASS_ID or UPDATED_REGULAR_SIZE_CLASS_ID
        defaultProductHistoryShouldBeFound("regularSizeClassId.in=" + DEFAULT_REGULAR_SIZE_CLASS_ID + "," + UPDATED_REGULAR_SIZE_CLASS_ID);

        // Get all the productHistoryList where regularSizeClassId equals to UPDATED_REGULAR_SIZE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("regularSizeClassId.in=" + UPDATED_REGULAR_SIZE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByRegularSizeClassIdIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where regularSizeClassId is not null
        defaultProductHistoryShouldBeFound("regularSizeClassId.specified=true");

        // Get all the productHistoryList where regularSizeClassId is null
        defaultProductHistoryShouldNotBeFound("regularSizeClassId.specified=false");
    }

    @Test
    void getAllProductHistoriesByRegularSizeClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where regularSizeClassId is greater than or equal to DEFAULT_REGULAR_SIZE_CLASS_ID
        defaultProductHistoryShouldBeFound("regularSizeClassId.greaterThanOrEqual=" + DEFAULT_REGULAR_SIZE_CLASS_ID);

        // Get all the productHistoryList where regularSizeClassId is greater than or equal to UPDATED_REGULAR_SIZE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("regularSizeClassId.greaterThanOrEqual=" + UPDATED_REGULAR_SIZE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByRegularSizeClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where regularSizeClassId is less than or equal to DEFAULT_REGULAR_SIZE_CLASS_ID
        defaultProductHistoryShouldBeFound("regularSizeClassId.lessThanOrEqual=" + DEFAULT_REGULAR_SIZE_CLASS_ID);

        // Get all the productHistoryList where regularSizeClassId is less than or equal to SMALLER_REGULAR_SIZE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("regularSizeClassId.lessThanOrEqual=" + SMALLER_REGULAR_SIZE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByRegularSizeClassIdIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where regularSizeClassId is less than DEFAULT_REGULAR_SIZE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("regularSizeClassId.lessThan=" + DEFAULT_REGULAR_SIZE_CLASS_ID);

        // Get all the productHistoryList where regularSizeClassId is less than UPDATED_REGULAR_SIZE_CLASS_ID
        defaultProductHistoryShouldBeFound("regularSizeClassId.lessThan=" + UPDATED_REGULAR_SIZE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByRegularSizeClassIdIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where regularSizeClassId is greater than DEFAULT_REGULAR_SIZE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("regularSizeClassId.greaterThan=" + DEFAULT_REGULAR_SIZE_CLASS_ID);

        // Get all the productHistoryList where regularSizeClassId is greater than SMALLER_REGULAR_SIZE_CLASS_ID
        defaultProductHistoryShouldBeFound("regularSizeClassId.greaterThan=" + SMALLER_REGULAR_SIZE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByLanguageClassIdIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where languageClassId equals to DEFAULT_LANGUAGE_CLASS_ID
        defaultProductHistoryShouldBeFound("languageClassId.equals=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the productHistoryList where languageClassId equals to UPDATED_LANGUAGE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("languageClassId.equals=" + UPDATED_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByLanguageClassIdIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where languageClassId in DEFAULT_LANGUAGE_CLASS_ID or UPDATED_LANGUAGE_CLASS_ID
        defaultProductHistoryShouldBeFound("languageClassId.in=" + DEFAULT_LANGUAGE_CLASS_ID + "," + UPDATED_LANGUAGE_CLASS_ID);

        // Get all the productHistoryList where languageClassId equals to UPDATED_LANGUAGE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("languageClassId.in=" + UPDATED_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByLanguageClassIdIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where languageClassId is not null
        defaultProductHistoryShouldBeFound("languageClassId.specified=true");

        // Get all the productHistoryList where languageClassId is null
        defaultProductHistoryShouldNotBeFound("languageClassId.specified=false");
    }

    @Test
    void getAllProductHistoriesByLanguageClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where languageClassId is greater than or equal to DEFAULT_LANGUAGE_CLASS_ID
        defaultProductHistoryShouldBeFound("languageClassId.greaterThanOrEqual=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the productHistoryList where languageClassId is greater than or equal to UPDATED_LANGUAGE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("languageClassId.greaterThanOrEqual=" + UPDATED_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByLanguageClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where languageClassId is less than or equal to DEFAULT_LANGUAGE_CLASS_ID
        defaultProductHistoryShouldBeFound("languageClassId.lessThanOrEqual=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the productHistoryList where languageClassId is less than or equal to SMALLER_LANGUAGE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("languageClassId.lessThanOrEqual=" + SMALLER_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByLanguageClassIdIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where languageClassId is less than DEFAULT_LANGUAGE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("languageClassId.lessThan=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the productHistoryList where languageClassId is less than UPDATED_LANGUAGE_CLASS_ID
        defaultProductHistoryShouldBeFound("languageClassId.lessThan=" + UPDATED_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByLanguageClassIdIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where languageClassId is greater than DEFAULT_LANGUAGE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("languageClassId.greaterThan=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the productHistoryList where languageClassId is greater than SMALLER_LANGUAGE_CLASS_ID
        defaultProductHistoryShouldBeFound("languageClassId.greaterThan=" + SMALLER_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByDescriptionIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where description equals to DEFAULT_DESCRIPTION
        defaultProductHistoryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the productHistoryList where description equals to UPDATED_DESCRIPTION
        defaultProductHistoryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllProductHistoriesByDescriptionIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProductHistoryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the productHistoryList where description equals to UPDATED_DESCRIPTION
        defaultProductHistoryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllProductHistoriesByDescriptionIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where description is not null
        defaultProductHistoryShouldBeFound("description.specified=true");

        // Get all the productHistoryList where description is null
        defaultProductHistoryShouldNotBeFound("description.specified=false");
    }

    @Test
    void getAllProductHistoriesByDescriptionContainsSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where description contains DEFAULT_DESCRIPTION
        defaultProductHistoryShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the productHistoryList where description contains UPDATED_DESCRIPTION
        defaultProductHistoryShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllProductHistoriesByDescriptionNotContainsSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where description does not contain DEFAULT_DESCRIPTION
        defaultProductHistoryShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the productHistoryList where description does not contain UPDATED_DESCRIPTION
        defaultProductHistoryShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllProductHistoriesByKeywordsIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where keywords equals to DEFAULT_KEYWORDS
        defaultProductHistoryShouldBeFound("keywords.equals=" + DEFAULT_KEYWORDS);

        // Get all the productHistoryList where keywords equals to UPDATED_KEYWORDS
        defaultProductHistoryShouldNotBeFound("keywords.equals=" + UPDATED_KEYWORDS);
    }

    @Test
    void getAllProductHistoriesByKeywordsIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where keywords in DEFAULT_KEYWORDS or UPDATED_KEYWORDS
        defaultProductHistoryShouldBeFound("keywords.in=" + DEFAULT_KEYWORDS + "," + UPDATED_KEYWORDS);

        // Get all the productHistoryList where keywords equals to UPDATED_KEYWORDS
        defaultProductHistoryShouldNotBeFound("keywords.in=" + UPDATED_KEYWORDS);
    }

    @Test
    void getAllProductHistoriesByKeywordsIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where keywords is not null
        defaultProductHistoryShouldBeFound("keywords.specified=true");

        // Get all the productHistoryList where keywords is null
        defaultProductHistoryShouldNotBeFound("keywords.specified=false");
    }

    @Test
    void getAllProductHistoriesByKeywordsContainsSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where keywords contains DEFAULT_KEYWORDS
        defaultProductHistoryShouldBeFound("keywords.contains=" + DEFAULT_KEYWORDS);

        // Get all the productHistoryList where keywords contains UPDATED_KEYWORDS
        defaultProductHistoryShouldNotBeFound("keywords.contains=" + UPDATED_KEYWORDS);
    }

    @Test
    void getAllProductHistoriesByKeywordsNotContainsSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where keywords does not contain DEFAULT_KEYWORDS
        defaultProductHistoryShouldNotBeFound("keywords.doesNotContain=" + DEFAULT_KEYWORDS);

        // Get all the productHistoryList where keywords does not contain UPDATED_KEYWORDS
        defaultProductHistoryShouldBeFound("keywords.doesNotContain=" + UPDATED_KEYWORDS);
    }

    @Test
    void getAllProductHistoriesByCountIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where count equals to DEFAULT_COUNT
        defaultProductHistoryShouldBeFound("count.equals=" + DEFAULT_COUNT);

        // Get all the productHistoryList where count equals to UPDATED_COUNT
        defaultProductHistoryShouldNotBeFound("count.equals=" + UPDATED_COUNT);
    }

    @Test
    void getAllProductHistoriesByCountIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where count in DEFAULT_COUNT or UPDATED_COUNT
        defaultProductHistoryShouldBeFound("count.in=" + DEFAULT_COUNT + "," + UPDATED_COUNT);

        // Get all the productHistoryList where count equals to UPDATED_COUNT
        defaultProductHistoryShouldNotBeFound("count.in=" + UPDATED_COUNT);
    }

    @Test
    void getAllProductHistoriesByCountIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where count is not null
        defaultProductHistoryShouldBeFound("count.specified=true");

        // Get all the productHistoryList where count is null
        defaultProductHistoryShouldNotBeFound("count.specified=false");
    }

    @Test
    void getAllProductHistoriesByCountIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where count is greater than or equal to DEFAULT_COUNT
        defaultProductHistoryShouldBeFound("count.greaterThanOrEqual=" + DEFAULT_COUNT);

        // Get all the productHistoryList where count is greater than or equal to UPDATED_COUNT
        defaultProductHistoryShouldNotBeFound("count.greaterThanOrEqual=" + UPDATED_COUNT);
    }

    @Test
    void getAllProductHistoriesByCountIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where count is less than or equal to DEFAULT_COUNT
        defaultProductHistoryShouldBeFound("count.lessThanOrEqual=" + DEFAULT_COUNT);

        // Get all the productHistoryList where count is less than or equal to SMALLER_COUNT
        defaultProductHistoryShouldNotBeFound("count.lessThanOrEqual=" + SMALLER_COUNT);
    }

    @Test
    void getAllProductHistoriesByCountIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where count is less than DEFAULT_COUNT
        defaultProductHistoryShouldNotBeFound("count.lessThan=" + DEFAULT_COUNT);

        // Get all the productHistoryList where count is less than UPDATED_COUNT
        defaultProductHistoryShouldBeFound("count.lessThan=" + UPDATED_COUNT);
    }

    @Test
    void getAllProductHistoriesByCountIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where count is greater than DEFAULT_COUNT
        defaultProductHistoryShouldNotBeFound("count.greaterThan=" + DEFAULT_COUNT);

        // Get all the productHistoryList where count is greater than SMALLER_COUNT
        defaultProductHistoryShouldBeFound("count.greaterThan=" + SMALLER_COUNT);
    }

    @Test
    void getAllProductHistoriesByDiscountIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where discount equals to DEFAULT_DISCOUNT
        defaultProductHistoryShouldBeFound("discount.equals=" + DEFAULT_DISCOUNT);

        // Get all the productHistoryList where discount equals to UPDATED_DISCOUNT
        defaultProductHistoryShouldNotBeFound("discount.equals=" + UPDATED_DISCOUNT);
    }

    @Test
    void getAllProductHistoriesByDiscountIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where discount in DEFAULT_DISCOUNT or UPDATED_DISCOUNT
        defaultProductHistoryShouldBeFound("discount.in=" + DEFAULT_DISCOUNT + "," + UPDATED_DISCOUNT);

        // Get all the productHistoryList where discount equals to UPDATED_DISCOUNT
        defaultProductHistoryShouldNotBeFound("discount.in=" + UPDATED_DISCOUNT);
    }

    @Test
    void getAllProductHistoriesByDiscountIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where discount is not null
        defaultProductHistoryShouldBeFound("discount.specified=true");

        // Get all the productHistoryList where discount is null
        defaultProductHistoryShouldNotBeFound("discount.specified=false");
    }

    @Test
    void getAllProductHistoriesByDiscountIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where discount is greater than or equal to DEFAULT_DISCOUNT
        defaultProductHistoryShouldBeFound("discount.greaterThanOrEqual=" + DEFAULT_DISCOUNT);

        // Get all the productHistoryList where discount is greater than or equal to UPDATED_DISCOUNT
        defaultProductHistoryShouldNotBeFound("discount.greaterThanOrEqual=" + UPDATED_DISCOUNT);
    }

    @Test
    void getAllProductHistoriesByDiscountIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where discount is less than or equal to DEFAULT_DISCOUNT
        defaultProductHistoryShouldBeFound("discount.lessThanOrEqual=" + DEFAULT_DISCOUNT);

        // Get all the productHistoryList where discount is less than or equal to SMALLER_DISCOUNT
        defaultProductHistoryShouldNotBeFound("discount.lessThanOrEqual=" + SMALLER_DISCOUNT);
    }

    @Test
    void getAllProductHistoriesByDiscountIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where discount is less than DEFAULT_DISCOUNT
        defaultProductHistoryShouldNotBeFound("discount.lessThan=" + DEFAULT_DISCOUNT);

        // Get all the productHistoryList where discount is less than UPDATED_DISCOUNT
        defaultProductHistoryShouldBeFound("discount.lessThan=" + UPDATED_DISCOUNT);
    }

    @Test
    void getAllProductHistoriesByDiscountIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where discount is greater than DEFAULT_DISCOUNT
        defaultProductHistoryShouldNotBeFound("discount.greaterThan=" + DEFAULT_DISCOUNT);

        // Get all the productHistoryList where discount is greater than SMALLER_DISCOUNT
        defaultProductHistoryShouldBeFound("discount.greaterThan=" + SMALLER_DISCOUNT);
    }

    @Test
    void getAllProductHistoriesByOriginalPriceIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where originalPrice equals to DEFAULT_ORIGINAL_PRICE
        defaultProductHistoryShouldBeFound("originalPrice.equals=" + DEFAULT_ORIGINAL_PRICE);

        // Get all the productHistoryList where originalPrice equals to UPDATED_ORIGINAL_PRICE
        defaultProductHistoryShouldNotBeFound("originalPrice.equals=" + UPDATED_ORIGINAL_PRICE);
    }

    @Test
    void getAllProductHistoriesByOriginalPriceIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where originalPrice in DEFAULT_ORIGINAL_PRICE or UPDATED_ORIGINAL_PRICE
        defaultProductHistoryShouldBeFound("originalPrice.in=" + DEFAULT_ORIGINAL_PRICE + "," + UPDATED_ORIGINAL_PRICE);

        // Get all the productHistoryList where originalPrice equals to UPDATED_ORIGINAL_PRICE
        defaultProductHistoryShouldNotBeFound("originalPrice.in=" + UPDATED_ORIGINAL_PRICE);
    }

    @Test
    void getAllProductHistoriesByOriginalPriceIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where originalPrice is not null
        defaultProductHistoryShouldBeFound("originalPrice.specified=true");

        // Get all the productHistoryList where originalPrice is null
        defaultProductHistoryShouldNotBeFound("originalPrice.specified=false");
    }

    @Test
    void getAllProductHistoriesByOriginalPriceIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where originalPrice is greater than or equal to DEFAULT_ORIGINAL_PRICE
        defaultProductHistoryShouldBeFound("originalPrice.greaterThanOrEqual=" + DEFAULT_ORIGINAL_PRICE);

        // Get all the productHistoryList where originalPrice is greater than or equal to UPDATED_ORIGINAL_PRICE
        defaultProductHistoryShouldNotBeFound("originalPrice.greaterThanOrEqual=" + UPDATED_ORIGINAL_PRICE);
    }

    @Test
    void getAllProductHistoriesByOriginalPriceIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where originalPrice is less than or equal to DEFAULT_ORIGINAL_PRICE
        defaultProductHistoryShouldBeFound("originalPrice.lessThanOrEqual=" + DEFAULT_ORIGINAL_PRICE);

        // Get all the productHistoryList where originalPrice is less than or equal to SMALLER_ORIGINAL_PRICE
        defaultProductHistoryShouldNotBeFound("originalPrice.lessThanOrEqual=" + SMALLER_ORIGINAL_PRICE);
    }

    @Test
    void getAllProductHistoriesByOriginalPriceIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where originalPrice is less than DEFAULT_ORIGINAL_PRICE
        defaultProductHistoryShouldNotBeFound("originalPrice.lessThan=" + DEFAULT_ORIGINAL_PRICE);

        // Get all the productHistoryList where originalPrice is less than UPDATED_ORIGINAL_PRICE
        defaultProductHistoryShouldBeFound("originalPrice.lessThan=" + UPDATED_ORIGINAL_PRICE);
    }

    @Test
    void getAllProductHistoriesByOriginalPriceIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where originalPrice is greater than DEFAULT_ORIGINAL_PRICE
        defaultProductHistoryShouldNotBeFound("originalPrice.greaterThan=" + DEFAULT_ORIGINAL_PRICE);

        // Get all the productHistoryList where originalPrice is greater than SMALLER_ORIGINAL_PRICE
        defaultProductHistoryShouldBeFound("originalPrice.greaterThan=" + SMALLER_ORIGINAL_PRICE);
    }

    @Test
    void getAllProductHistoriesByFinalPriceIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where finalPrice equals to DEFAULT_FINAL_PRICE
        defaultProductHistoryShouldBeFound("finalPrice.equals=" + DEFAULT_FINAL_PRICE);

        // Get all the productHistoryList where finalPrice equals to UPDATED_FINAL_PRICE
        defaultProductHistoryShouldNotBeFound("finalPrice.equals=" + UPDATED_FINAL_PRICE);
    }

    @Test
    void getAllProductHistoriesByFinalPriceIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where finalPrice in DEFAULT_FINAL_PRICE or UPDATED_FINAL_PRICE
        defaultProductHistoryShouldBeFound("finalPrice.in=" + DEFAULT_FINAL_PRICE + "," + UPDATED_FINAL_PRICE);

        // Get all the productHistoryList where finalPrice equals to UPDATED_FINAL_PRICE
        defaultProductHistoryShouldNotBeFound("finalPrice.in=" + UPDATED_FINAL_PRICE);
    }

    @Test
    void getAllProductHistoriesByFinalPriceIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where finalPrice is not null
        defaultProductHistoryShouldBeFound("finalPrice.specified=true");

        // Get all the productHistoryList where finalPrice is null
        defaultProductHistoryShouldNotBeFound("finalPrice.specified=false");
    }

    @Test
    void getAllProductHistoriesByFinalPriceIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where finalPrice is greater than or equal to DEFAULT_FINAL_PRICE
        defaultProductHistoryShouldBeFound("finalPrice.greaterThanOrEqual=" + DEFAULT_FINAL_PRICE);

        // Get all the productHistoryList where finalPrice is greater than or equal to UPDATED_FINAL_PRICE
        defaultProductHistoryShouldNotBeFound("finalPrice.greaterThanOrEqual=" + UPDATED_FINAL_PRICE);
    }

    @Test
    void getAllProductHistoriesByFinalPriceIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where finalPrice is less than or equal to DEFAULT_FINAL_PRICE
        defaultProductHistoryShouldBeFound("finalPrice.lessThanOrEqual=" + DEFAULT_FINAL_PRICE);

        // Get all the productHistoryList where finalPrice is less than or equal to SMALLER_FINAL_PRICE
        defaultProductHistoryShouldNotBeFound("finalPrice.lessThanOrEqual=" + SMALLER_FINAL_PRICE);
    }

    @Test
    void getAllProductHistoriesByFinalPriceIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where finalPrice is less than DEFAULT_FINAL_PRICE
        defaultProductHistoryShouldNotBeFound("finalPrice.lessThan=" + DEFAULT_FINAL_PRICE);

        // Get all the productHistoryList where finalPrice is less than UPDATED_FINAL_PRICE
        defaultProductHistoryShouldBeFound("finalPrice.lessThan=" + UPDATED_FINAL_PRICE);
    }

    @Test
    void getAllProductHistoriesByFinalPriceIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where finalPrice is greater than DEFAULT_FINAL_PRICE
        defaultProductHistoryShouldNotBeFound("finalPrice.greaterThan=" + DEFAULT_FINAL_PRICE);

        // Get all the productHistoryList where finalPrice is greater than SMALLER_FINAL_PRICE
        defaultProductHistoryShouldBeFound("finalPrice.greaterThan=" + SMALLER_FINAL_PRICE);
    }

    @Test
    void getAllProductHistoriesByPublishDateIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where publishDate equals to DEFAULT_PUBLISH_DATE
        defaultProductHistoryShouldBeFound("publishDate.equals=" + DEFAULT_PUBLISH_DATE);

        // Get all the productHistoryList where publishDate equals to UPDATED_PUBLISH_DATE
        defaultProductHistoryShouldNotBeFound("publishDate.equals=" + UPDATED_PUBLISH_DATE);
    }

    @Test
    void getAllProductHistoriesByPublishDateIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where publishDate in DEFAULT_PUBLISH_DATE or UPDATED_PUBLISH_DATE
        defaultProductHistoryShouldBeFound("publishDate.in=" + DEFAULT_PUBLISH_DATE + "," + UPDATED_PUBLISH_DATE);

        // Get all the productHistoryList where publishDate equals to UPDATED_PUBLISH_DATE
        defaultProductHistoryShouldNotBeFound("publishDate.in=" + UPDATED_PUBLISH_DATE);
    }

    @Test
    void getAllProductHistoriesByPublishDateIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where publishDate is not null
        defaultProductHistoryShouldBeFound("publishDate.specified=true");

        // Get all the productHistoryList where publishDate is null
        defaultProductHistoryShouldNotBeFound("publishDate.specified=false");
    }

    @Test
    void getAllProductHistoriesByPublishDateIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where publishDate is greater than or equal to DEFAULT_PUBLISH_DATE
        defaultProductHistoryShouldBeFound("publishDate.greaterThanOrEqual=" + DEFAULT_PUBLISH_DATE);

        // Get all the productHistoryList where publishDate is greater than or equal to UPDATED_PUBLISH_DATE
        defaultProductHistoryShouldNotBeFound("publishDate.greaterThanOrEqual=" + UPDATED_PUBLISH_DATE);
    }

    @Test
    void getAllProductHistoriesByPublishDateIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where publishDate is less than or equal to DEFAULT_PUBLISH_DATE
        defaultProductHistoryShouldBeFound("publishDate.lessThanOrEqual=" + DEFAULT_PUBLISH_DATE);

        // Get all the productHistoryList where publishDate is less than or equal to SMALLER_PUBLISH_DATE
        defaultProductHistoryShouldNotBeFound("publishDate.lessThanOrEqual=" + SMALLER_PUBLISH_DATE);
    }

    @Test
    void getAllProductHistoriesByPublishDateIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where publishDate is less than DEFAULT_PUBLISH_DATE
        defaultProductHistoryShouldNotBeFound("publishDate.lessThan=" + DEFAULT_PUBLISH_DATE);

        // Get all the productHistoryList where publishDate is less than UPDATED_PUBLISH_DATE
        defaultProductHistoryShouldBeFound("publishDate.lessThan=" + UPDATED_PUBLISH_DATE);
    }

    @Test
    void getAllProductHistoriesByPublishDateIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where publishDate is greater than DEFAULT_PUBLISH_DATE
        defaultProductHistoryShouldNotBeFound("publishDate.greaterThan=" + DEFAULT_PUBLISH_DATE);

        // Get all the productHistoryList where publishDate is greater than SMALLER_PUBLISH_DATE
        defaultProductHistoryShouldBeFound("publishDate.greaterThan=" + SMALLER_PUBLISH_DATE);
    }

    @Test
    void getAllProductHistoriesByTransportDateIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where transportDate equals to DEFAULT_TRANSPORT_DATE
        defaultProductHistoryShouldBeFound("transportDate.equals=" + DEFAULT_TRANSPORT_DATE);

        // Get all the productHistoryList where transportDate equals to UPDATED_TRANSPORT_DATE
        defaultProductHistoryShouldNotBeFound("transportDate.equals=" + UPDATED_TRANSPORT_DATE);
    }

    @Test
    void getAllProductHistoriesByTransportDateIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where transportDate in DEFAULT_TRANSPORT_DATE or UPDATED_TRANSPORT_DATE
        defaultProductHistoryShouldBeFound("transportDate.in=" + DEFAULT_TRANSPORT_DATE + "," + UPDATED_TRANSPORT_DATE);

        // Get all the productHistoryList where transportDate equals to UPDATED_TRANSPORT_DATE
        defaultProductHistoryShouldNotBeFound("transportDate.in=" + UPDATED_TRANSPORT_DATE);
    }

    @Test
    void getAllProductHistoriesByTransportDateIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where transportDate is not null
        defaultProductHistoryShouldBeFound("transportDate.specified=true");

        // Get all the productHistoryList where transportDate is null
        defaultProductHistoryShouldNotBeFound("transportDate.specified=false");
    }

    @Test
    void getAllProductHistoriesByTransportDateIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where transportDate is greater than or equal to DEFAULT_TRANSPORT_DATE
        defaultProductHistoryShouldBeFound("transportDate.greaterThanOrEqual=" + DEFAULT_TRANSPORT_DATE);

        // Get all the productHistoryList where transportDate is greater than or equal to UPDATED_TRANSPORT_DATE
        defaultProductHistoryShouldNotBeFound("transportDate.greaterThanOrEqual=" + UPDATED_TRANSPORT_DATE);
    }

    @Test
    void getAllProductHistoriesByTransportDateIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where transportDate is less than or equal to DEFAULT_TRANSPORT_DATE
        defaultProductHistoryShouldBeFound("transportDate.lessThanOrEqual=" + DEFAULT_TRANSPORT_DATE);

        // Get all the productHistoryList where transportDate is less than or equal to SMALLER_TRANSPORT_DATE
        defaultProductHistoryShouldNotBeFound("transportDate.lessThanOrEqual=" + SMALLER_TRANSPORT_DATE);
    }

    @Test
    void getAllProductHistoriesByTransportDateIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where transportDate is less than DEFAULT_TRANSPORT_DATE
        defaultProductHistoryShouldNotBeFound("transportDate.lessThan=" + DEFAULT_TRANSPORT_DATE);

        // Get all the productHistoryList where transportDate is less than UPDATED_TRANSPORT_DATE
        defaultProductHistoryShouldBeFound("transportDate.lessThan=" + UPDATED_TRANSPORT_DATE);
    }

    @Test
    void getAllProductHistoriesByTransportDateIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where transportDate is greater than DEFAULT_TRANSPORT_DATE
        defaultProductHistoryShouldNotBeFound("transportDate.greaterThan=" + DEFAULT_TRANSPORT_DATE);

        // Get all the productHistoryList where transportDate is greater than SMALLER_TRANSPORT_DATE
        defaultProductHistoryShouldBeFound("transportDate.greaterThan=" + SMALLER_TRANSPORT_DATE);
    }

    @Test
    void getAllProductHistoriesByCurrencyClassIdIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where currencyClassId equals to DEFAULT_CURRENCY_CLASS_ID
        defaultProductHistoryShouldBeFound("currencyClassId.equals=" + DEFAULT_CURRENCY_CLASS_ID);

        // Get all the productHistoryList where currencyClassId equals to UPDATED_CURRENCY_CLASS_ID
        defaultProductHistoryShouldNotBeFound("currencyClassId.equals=" + UPDATED_CURRENCY_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByCurrencyClassIdIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where currencyClassId in DEFAULT_CURRENCY_CLASS_ID or UPDATED_CURRENCY_CLASS_ID
        defaultProductHistoryShouldBeFound("currencyClassId.in=" + DEFAULT_CURRENCY_CLASS_ID + "," + UPDATED_CURRENCY_CLASS_ID);

        // Get all the productHistoryList where currencyClassId equals to UPDATED_CURRENCY_CLASS_ID
        defaultProductHistoryShouldNotBeFound("currencyClassId.in=" + UPDATED_CURRENCY_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByCurrencyClassIdIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where currencyClassId is not null
        defaultProductHistoryShouldBeFound("currencyClassId.specified=true");

        // Get all the productHistoryList where currencyClassId is null
        defaultProductHistoryShouldNotBeFound("currencyClassId.specified=false");
    }

    @Test
    void getAllProductHistoriesByCurrencyClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where currencyClassId is greater than or equal to DEFAULT_CURRENCY_CLASS_ID
        defaultProductHistoryShouldBeFound("currencyClassId.greaterThanOrEqual=" + DEFAULT_CURRENCY_CLASS_ID);

        // Get all the productHistoryList where currencyClassId is greater than or equal to UPDATED_CURRENCY_CLASS_ID
        defaultProductHistoryShouldNotBeFound("currencyClassId.greaterThanOrEqual=" + UPDATED_CURRENCY_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByCurrencyClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where currencyClassId is less than or equal to DEFAULT_CURRENCY_CLASS_ID
        defaultProductHistoryShouldBeFound("currencyClassId.lessThanOrEqual=" + DEFAULT_CURRENCY_CLASS_ID);

        // Get all the productHistoryList where currencyClassId is less than or equal to SMALLER_CURRENCY_CLASS_ID
        defaultProductHistoryShouldNotBeFound("currencyClassId.lessThanOrEqual=" + SMALLER_CURRENCY_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByCurrencyClassIdIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where currencyClassId is less than DEFAULT_CURRENCY_CLASS_ID
        defaultProductHistoryShouldNotBeFound("currencyClassId.lessThan=" + DEFAULT_CURRENCY_CLASS_ID);

        // Get all the productHistoryList where currencyClassId is less than UPDATED_CURRENCY_CLASS_ID
        defaultProductHistoryShouldBeFound("currencyClassId.lessThan=" + UPDATED_CURRENCY_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByCurrencyClassIdIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where currencyClassId is greater than DEFAULT_CURRENCY_CLASS_ID
        defaultProductHistoryShouldNotBeFound("currencyClassId.greaterThan=" + DEFAULT_CURRENCY_CLASS_ID);

        // Get all the productHistoryList where currencyClassId is greater than SMALLER_CURRENCY_CLASS_ID
        defaultProductHistoryShouldBeFound("currencyClassId.greaterThan=" + SMALLER_CURRENCY_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByBonusIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where bonus equals to DEFAULT_BONUS
        defaultProductHistoryShouldBeFound("bonus.equals=" + DEFAULT_BONUS);

        // Get all the productHistoryList where bonus equals to UPDATED_BONUS
        defaultProductHistoryShouldNotBeFound("bonus.equals=" + UPDATED_BONUS);
    }

    @Test
    void getAllProductHistoriesByBonusIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where bonus in DEFAULT_BONUS or UPDATED_BONUS
        defaultProductHistoryShouldBeFound("bonus.in=" + DEFAULT_BONUS + "," + UPDATED_BONUS);

        // Get all the productHistoryList where bonus equals to UPDATED_BONUS
        defaultProductHistoryShouldNotBeFound("bonus.in=" + UPDATED_BONUS);
    }

    @Test
    void getAllProductHistoriesByBonusIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where bonus is not null
        defaultProductHistoryShouldBeFound("bonus.specified=true");

        // Get all the productHistoryList where bonus is null
        defaultProductHistoryShouldNotBeFound("bonus.specified=false");
    }

    @Test
    void getAllProductHistoriesByBonusIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where bonus is greater than or equal to DEFAULT_BONUS
        defaultProductHistoryShouldBeFound("bonus.greaterThanOrEqual=" + DEFAULT_BONUS);

        // Get all the productHistoryList where bonus is greater than or equal to UPDATED_BONUS
        defaultProductHistoryShouldNotBeFound("bonus.greaterThanOrEqual=" + UPDATED_BONUS);
    }

    @Test
    void getAllProductHistoriesByBonusIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where bonus is less than or equal to DEFAULT_BONUS
        defaultProductHistoryShouldBeFound("bonus.lessThanOrEqual=" + DEFAULT_BONUS);

        // Get all the productHistoryList where bonus is less than or equal to SMALLER_BONUS
        defaultProductHistoryShouldNotBeFound("bonus.lessThanOrEqual=" + SMALLER_BONUS);
    }

    @Test
    void getAllProductHistoriesByBonusIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where bonus is less than DEFAULT_BONUS
        defaultProductHistoryShouldNotBeFound("bonus.lessThan=" + DEFAULT_BONUS);

        // Get all the productHistoryList where bonus is less than UPDATED_BONUS
        defaultProductHistoryShouldBeFound("bonus.lessThan=" + UPDATED_BONUS);
    }

    @Test
    void getAllProductHistoriesByBonusIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where bonus is greater than DEFAULT_BONUS
        defaultProductHistoryShouldNotBeFound("bonus.greaterThan=" + DEFAULT_BONUS);

        // Get all the productHistoryList where bonus is greater than SMALLER_BONUS
        defaultProductHistoryShouldBeFound("bonus.greaterThan=" + SMALLER_BONUS);
    }

    @Test
    void getAllProductHistoriesByWarrantyClassIdIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where warrantyClassId equals to DEFAULT_WARRANTY_CLASS_ID
        defaultProductHistoryShouldBeFound("warrantyClassId.equals=" + DEFAULT_WARRANTY_CLASS_ID);

        // Get all the productHistoryList where warrantyClassId equals to UPDATED_WARRANTY_CLASS_ID
        defaultProductHistoryShouldNotBeFound("warrantyClassId.equals=" + UPDATED_WARRANTY_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByWarrantyClassIdIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where warrantyClassId in DEFAULT_WARRANTY_CLASS_ID or UPDATED_WARRANTY_CLASS_ID
        defaultProductHistoryShouldBeFound("warrantyClassId.in=" + DEFAULT_WARRANTY_CLASS_ID + "," + UPDATED_WARRANTY_CLASS_ID);

        // Get all the productHistoryList where warrantyClassId equals to UPDATED_WARRANTY_CLASS_ID
        defaultProductHistoryShouldNotBeFound("warrantyClassId.in=" + UPDATED_WARRANTY_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByWarrantyClassIdIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where warrantyClassId is not null
        defaultProductHistoryShouldBeFound("warrantyClassId.specified=true");

        // Get all the productHistoryList where warrantyClassId is null
        defaultProductHistoryShouldNotBeFound("warrantyClassId.specified=false");
    }

    @Test
    void getAllProductHistoriesByWarrantyClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where warrantyClassId is greater than or equal to DEFAULT_WARRANTY_CLASS_ID
        defaultProductHistoryShouldBeFound("warrantyClassId.greaterThanOrEqual=" + DEFAULT_WARRANTY_CLASS_ID);

        // Get all the productHistoryList where warrantyClassId is greater than or equal to UPDATED_WARRANTY_CLASS_ID
        defaultProductHistoryShouldNotBeFound("warrantyClassId.greaterThanOrEqual=" + UPDATED_WARRANTY_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByWarrantyClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where warrantyClassId is less than or equal to DEFAULT_WARRANTY_CLASS_ID
        defaultProductHistoryShouldBeFound("warrantyClassId.lessThanOrEqual=" + DEFAULT_WARRANTY_CLASS_ID);

        // Get all the productHistoryList where warrantyClassId is less than or equal to SMALLER_WARRANTY_CLASS_ID
        defaultProductHistoryShouldNotBeFound("warrantyClassId.lessThanOrEqual=" + SMALLER_WARRANTY_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByWarrantyClassIdIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where warrantyClassId is less than DEFAULT_WARRANTY_CLASS_ID
        defaultProductHistoryShouldNotBeFound("warrantyClassId.lessThan=" + DEFAULT_WARRANTY_CLASS_ID);

        // Get all the productHistoryList where warrantyClassId is less than UPDATED_WARRANTY_CLASS_ID
        defaultProductHistoryShouldBeFound("warrantyClassId.lessThan=" + UPDATED_WARRANTY_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByWarrantyClassIdIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where warrantyClassId is greater than DEFAULT_WARRANTY_CLASS_ID
        defaultProductHistoryShouldNotBeFound("warrantyClassId.greaterThan=" + DEFAULT_WARRANTY_CLASS_ID);

        // Get all the productHistoryList where warrantyClassId is greater than SMALLER_WARRANTY_CLASS_ID
        defaultProductHistoryShouldBeFound("warrantyClassId.greaterThan=" + SMALLER_WARRANTY_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByDeliveryPlaceClassIdIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where deliveryPlaceClassId equals to DEFAULT_DELIVERY_PLACE_CLASS_ID
        defaultProductHistoryShouldBeFound("deliveryPlaceClassId.equals=" + DEFAULT_DELIVERY_PLACE_CLASS_ID);

        // Get all the productHistoryList where deliveryPlaceClassId equals to UPDATED_DELIVERY_PLACE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("deliveryPlaceClassId.equals=" + UPDATED_DELIVERY_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByDeliveryPlaceClassIdIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where deliveryPlaceClassId in DEFAULT_DELIVERY_PLACE_CLASS_ID or UPDATED_DELIVERY_PLACE_CLASS_ID
        defaultProductHistoryShouldBeFound(
            "deliveryPlaceClassId.in=" + DEFAULT_DELIVERY_PLACE_CLASS_ID + "," + UPDATED_DELIVERY_PLACE_CLASS_ID
        );

        // Get all the productHistoryList where deliveryPlaceClassId equals to UPDATED_DELIVERY_PLACE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("deliveryPlaceClassId.in=" + UPDATED_DELIVERY_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByDeliveryPlaceClassIdIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where deliveryPlaceClassId is not null
        defaultProductHistoryShouldBeFound("deliveryPlaceClassId.specified=true");

        // Get all the productHistoryList where deliveryPlaceClassId is null
        defaultProductHistoryShouldNotBeFound("deliveryPlaceClassId.specified=false");
    }

    @Test
    void getAllProductHistoriesByDeliveryPlaceClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where deliveryPlaceClassId is greater than or equal to DEFAULT_DELIVERY_PLACE_CLASS_ID
        defaultProductHistoryShouldBeFound("deliveryPlaceClassId.greaterThanOrEqual=" + DEFAULT_DELIVERY_PLACE_CLASS_ID);

        // Get all the productHistoryList where deliveryPlaceClassId is greater than or equal to UPDATED_DELIVERY_PLACE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("deliveryPlaceClassId.greaterThanOrEqual=" + UPDATED_DELIVERY_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByDeliveryPlaceClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where deliveryPlaceClassId is less than or equal to DEFAULT_DELIVERY_PLACE_CLASS_ID
        defaultProductHistoryShouldBeFound("deliveryPlaceClassId.lessThanOrEqual=" + DEFAULT_DELIVERY_PLACE_CLASS_ID);

        // Get all the productHistoryList where deliveryPlaceClassId is less than or equal to SMALLER_DELIVERY_PLACE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("deliveryPlaceClassId.lessThanOrEqual=" + SMALLER_DELIVERY_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByDeliveryPlaceClassIdIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where deliveryPlaceClassId is less than DEFAULT_DELIVERY_PLACE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("deliveryPlaceClassId.lessThan=" + DEFAULT_DELIVERY_PLACE_CLASS_ID);

        // Get all the productHistoryList where deliveryPlaceClassId is less than UPDATED_DELIVERY_PLACE_CLASS_ID
        defaultProductHistoryShouldBeFound("deliveryPlaceClassId.lessThan=" + UPDATED_DELIVERY_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByDeliveryPlaceClassIdIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where deliveryPlaceClassId is greater than DEFAULT_DELIVERY_PLACE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("deliveryPlaceClassId.greaterThan=" + DEFAULT_DELIVERY_PLACE_CLASS_ID);

        // Get all the productHistoryList where deliveryPlaceClassId is greater than SMALLER_DELIVERY_PLACE_CLASS_ID
        defaultProductHistoryShouldBeFound("deliveryPlaceClassId.greaterThan=" + SMALLER_DELIVERY_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByPaymentPlaceClassIdIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where paymentPlaceClassId equals to DEFAULT_PAYMENT_PLACE_CLASS_ID
        defaultProductHistoryShouldBeFound("paymentPlaceClassId.equals=" + DEFAULT_PAYMENT_PLACE_CLASS_ID);

        // Get all the productHistoryList where paymentPlaceClassId equals to UPDATED_PAYMENT_PLACE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("paymentPlaceClassId.equals=" + UPDATED_PAYMENT_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByPaymentPlaceClassIdIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where paymentPlaceClassId in DEFAULT_PAYMENT_PLACE_CLASS_ID or UPDATED_PAYMENT_PLACE_CLASS_ID
        defaultProductHistoryShouldBeFound(
            "paymentPlaceClassId.in=" + DEFAULT_PAYMENT_PLACE_CLASS_ID + "," + UPDATED_PAYMENT_PLACE_CLASS_ID
        );

        // Get all the productHistoryList where paymentPlaceClassId equals to UPDATED_PAYMENT_PLACE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("paymentPlaceClassId.in=" + UPDATED_PAYMENT_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByPaymentPlaceClassIdIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where paymentPlaceClassId is not null
        defaultProductHistoryShouldBeFound("paymentPlaceClassId.specified=true");

        // Get all the productHistoryList where paymentPlaceClassId is null
        defaultProductHistoryShouldNotBeFound("paymentPlaceClassId.specified=false");
    }

    @Test
    void getAllProductHistoriesByPaymentPlaceClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where paymentPlaceClassId is greater than or equal to DEFAULT_PAYMENT_PLACE_CLASS_ID
        defaultProductHistoryShouldBeFound("paymentPlaceClassId.greaterThanOrEqual=" + DEFAULT_PAYMENT_PLACE_CLASS_ID);

        // Get all the productHistoryList where paymentPlaceClassId is greater than or equal to UPDATED_PAYMENT_PLACE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("paymentPlaceClassId.greaterThanOrEqual=" + UPDATED_PAYMENT_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByPaymentPlaceClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where paymentPlaceClassId is less than or equal to DEFAULT_PAYMENT_PLACE_CLASS_ID
        defaultProductHistoryShouldBeFound("paymentPlaceClassId.lessThanOrEqual=" + DEFAULT_PAYMENT_PLACE_CLASS_ID);

        // Get all the productHistoryList where paymentPlaceClassId is less than or equal to SMALLER_PAYMENT_PLACE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("paymentPlaceClassId.lessThanOrEqual=" + SMALLER_PAYMENT_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByPaymentPlaceClassIdIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where paymentPlaceClassId is less than DEFAULT_PAYMENT_PLACE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("paymentPlaceClassId.lessThan=" + DEFAULT_PAYMENT_PLACE_CLASS_ID);

        // Get all the productHistoryList where paymentPlaceClassId is less than UPDATED_PAYMENT_PLACE_CLASS_ID
        defaultProductHistoryShouldBeFound("paymentPlaceClassId.lessThan=" + UPDATED_PAYMENT_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByPaymentPlaceClassIdIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where paymentPlaceClassId is greater than DEFAULT_PAYMENT_PLACE_CLASS_ID
        defaultProductHistoryShouldNotBeFound("paymentPlaceClassId.greaterThan=" + DEFAULT_PAYMENT_PLACE_CLASS_ID);

        // Get all the productHistoryList where paymentPlaceClassId is greater than SMALLER_PAYMENT_PLACE_CLASS_ID
        defaultProductHistoryShouldBeFound("paymentPlaceClassId.greaterThan=" + SMALLER_PAYMENT_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByPerformanceIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where performance equals to DEFAULT_PERFORMANCE
        defaultProductHistoryShouldBeFound("performance.equals=" + DEFAULT_PERFORMANCE);

        // Get all the productHistoryList where performance equals to UPDATED_PERFORMANCE
        defaultProductHistoryShouldNotBeFound("performance.equals=" + UPDATED_PERFORMANCE);
    }

    @Test
    void getAllProductHistoriesByPerformanceIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where performance in DEFAULT_PERFORMANCE or UPDATED_PERFORMANCE
        defaultProductHistoryShouldBeFound("performance.in=" + DEFAULT_PERFORMANCE + "," + UPDATED_PERFORMANCE);

        // Get all the productHistoryList where performance equals to UPDATED_PERFORMANCE
        defaultProductHistoryShouldNotBeFound("performance.in=" + UPDATED_PERFORMANCE);
    }

    @Test
    void getAllProductHistoriesByPerformanceIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where performance is not null
        defaultProductHistoryShouldBeFound("performance.specified=true");

        // Get all the productHistoryList where performance is null
        defaultProductHistoryShouldNotBeFound("performance.specified=false");
    }

    @Test
    void getAllProductHistoriesByOriginalityClassIdIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where originalityClassId equals to DEFAULT_ORIGINALITY_CLASS_ID
        defaultProductHistoryShouldBeFound("originalityClassId.equals=" + DEFAULT_ORIGINALITY_CLASS_ID);

        // Get all the productHistoryList where originalityClassId equals to UPDATED_ORIGINALITY_CLASS_ID
        defaultProductHistoryShouldNotBeFound("originalityClassId.equals=" + UPDATED_ORIGINALITY_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByOriginalityClassIdIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where originalityClassId in DEFAULT_ORIGINALITY_CLASS_ID or UPDATED_ORIGINALITY_CLASS_ID
        defaultProductHistoryShouldBeFound("originalityClassId.in=" + DEFAULT_ORIGINALITY_CLASS_ID + "," + UPDATED_ORIGINALITY_CLASS_ID);

        // Get all the productHistoryList where originalityClassId equals to UPDATED_ORIGINALITY_CLASS_ID
        defaultProductHistoryShouldNotBeFound("originalityClassId.in=" + UPDATED_ORIGINALITY_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByOriginalityClassIdIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where originalityClassId is not null
        defaultProductHistoryShouldBeFound("originalityClassId.specified=true");

        // Get all the productHistoryList where originalityClassId is null
        defaultProductHistoryShouldNotBeFound("originalityClassId.specified=false");
    }

    @Test
    void getAllProductHistoriesByOriginalityClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where originalityClassId is greater than or equal to DEFAULT_ORIGINALITY_CLASS_ID
        defaultProductHistoryShouldBeFound("originalityClassId.greaterThanOrEqual=" + DEFAULT_ORIGINALITY_CLASS_ID);

        // Get all the productHistoryList where originalityClassId is greater than or equal to UPDATED_ORIGINALITY_CLASS_ID
        defaultProductHistoryShouldNotBeFound("originalityClassId.greaterThanOrEqual=" + UPDATED_ORIGINALITY_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByOriginalityClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where originalityClassId is less than or equal to DEFAULT_ORIGINALITY_CLASS_ID
        defaultProductHistoryShouldBeFound("originalityClassId.lessThanOrEqual=" + DEFAULT_ORIGINALITY_CLASS_ID);

        // Get all the productHistoryList where originalityClassId is less than or equal to SMALLER_ORIGINALITY_CLASS_ID
        defaultProductHistoryShouldNotBeFound("originalityClassId.lessThanOrEqual=" + SMALLER_ORIGINALITY_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByOriginalityClassIdIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where originalityClassId is less than DEFAULT_ORIGINALITY_CLASS_ID
        defaultProductHistoryShouldNotBeFound("originalityClassId.lessThan=" + DEFAULT_ORIGINALITY_CLASS_ID);

        // Get all the productHistoryList where originalityClassId is less than UPDATED_ORIGINALITY_CLASS_ID
        defaultProductHistoryShouldBeFound("originalityClassId.lessThan=" + UPDATED_ORIGINALITY_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesByOriginalityClassIdIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where originalityClassId is greater than DEFAULT_ORIGINALITY_CLASS_ID
        defaultProductHistoryShouldNotBeFound("originalityClassId.greaterThan=" + DEFAULT_ORIGINALITY_CLASS_ID);

        // Get all the productHistoryList where originalityClassId is greater than SMALLER_ORIGINALITY_CLASS_ID
        defaultProductHistoryShouldBeFound("originalityClassId.greaterThan=" + SMALLER_ORIGINALITY_CLASS_ID);
    }

    @Test
    void getAllProductHistoriesBySatisfactionIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where satisfaction equals to DEFAULT_SATISFACTION
        defaultProductHistoryShouldBeFound("satisfaction.equals=" + DEFAULT_SATISFACTION);

        // Get all the productHistoryList where satisfaction equals to UPDATED_SATISFACTION
        defaultProductHistoryShouldNotBeFound("satisfaction.equals=" + UPDATED_SATISFACTION);
    }

    @Test
    void getAllProductHistoriesBySatisfactionIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where satisfaction in DEFAULT_SATISFACTION or UPDATED_SATISFACTION
        defaultProductHistoryShouldBeFound("satisfaction.in=" + DEFAULT_SATISFACTION + "," + UPDATED_SATISFACTION);

        // Get all the productHistoryList where satisfaction equals to UPDATED_SATISFACTION
        defaultProductHistoryShouldNotBeFound("satisfaction.in=" + UPDATED_SATISFACTION);
    }

    @Test
    void getAllProductHistoriesBySatisfactionIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where satisfaction is not null
        defaultProductHistoryShouldBeFound("satisfaction.specified=true");

        // Get all the productHistoryList where satisfaction is null
        defaultProductHistoryShouldNotBeFound("satisfaction.specified=false");
    }

    @Test
    void getAllProductHistoriesBySatisfactionIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where satisfaction is greater than or equal to DEFAULT_SATISFACTION
        defaultProductHistoryShouldBeFound("satisfaction.greaterThanOrEqual=" + DEFAULT_SATISFACTION);

        // Get all the productHistoryList where satisfaction is greater than or equal to UPDATED_SATISFACTION
        defaultProductHistoryShouldNotBeFound("satisfaction.greaterThanOrEqual=" + UPDATED_SATISFACTION);
    }

    @Test
    void getAllProductHistoriesBySatisfactionIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where satisfaction is less than or equal to DEFAULT_SATISFACTION
        defaultProductHistoryShouldBeFound("satisfaction.lessThanOrEqual=" + DEFAULT_SATISFACTION);

        // Get all the productHistoryList where satisfaction is less than or equal to SMALLER_SATISFACTION
        defaultProductHistoryShouldNotBeFound("satisfaction.lessThanOrEqual=" + SMALLER_SATISFACTION);
    }

    @Test
    void getAllProductHistoriesBySatisfactionIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where satisfaction is less than DEFAULT_SATISFACTION
        defaultProductHistoryShouldNotBeFound("satisfaction.lessThan=" + DEFAULT_SATISFACTION);

        // Get all the productHistoryList where satisfaction is less than UPDATED_SATISFACTION
        defaultProductHistoryShouldBeFound("satisfaction.lessThan=" + UPDATED_SATISFACTION);
    }

    @Test
    void getAllProductHistoriesBySatisfactionIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where satisfaction is greater than DEFAULT_SATISFACTION
        defaultProductHistoryShouldNotBeFound("satisfaction.greaterThan=" + DEFAULT_SATISFACTION);

        // Get all the productHistoryList where satisfaction is greater than SMALLER_SATISFACTION
        defaultProductHistoryShouldBeFound("satisfaction.greaterThan=" + SMALLER_SATISFACTION);
    }

    @Test
    void getAllProductHistoriesByUsedIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where used equals to DEFAULT_USED
        defaultProductHistoryShouldBeFound("used.equals=" + DEFAULT_USED);

        // Get all the productHistoryList where used equals to UPDATED_USED
        defaultProductHistoryShouldNotBeFound("used.equals=" + UPDATED_USED);
    }

    @Test
    void getAllProductHistoriesByUsedIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where used in DEFAULT_USED or UPDATED_USED
        defaultProductHistoryShouldBeFound("used.in=" + DEFAULT_USED + "," + UPDATED_USED);

        // Get all the productHistoryList where used equals to UPDATED_USED
        defaultProductHistoryShouldNotBeFound("used.in=" + UPDATED_USED);
    }

    @Test
    void getAllProductHistoriesByUsedIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where used is not null
        defaultProductHistoryShouldBeFound("used.specified=true");

        // Get all the productHistoryList where used is null
        defaultProductHistoryShouldNotBeFound("used.specified=false");
    }

    @Test
    void getAllProductHistoriesByCategoryIdIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where categoryId equals to DEFAULT_CATEGORY_ID
        defaultProductHistoryShouldBeFound("categoryId.equals=" + DEFAULT_CATEGORY_ID);

        // Get all the productHistoryList where categoryId equals to UPDATED_CATEGORY_ID
        defaultProductHistoryShouldNotBeFound("categoryId.equals=" + UPDATED_CATEGORY_ID);
    }

    @Test
    void getAllProductHistoriesByCategoryIdIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where categoryId in DEFAULT_CATEGORY_ID or UPDATED_CATEGORY_ID
        defaultProductHistoryShouldBeFound("categoryId.in=" + DEFAULT_CATEGORY_ID + "," + UPDATED_CATEGORY_ID);

        // Get all the productHistoryList where categoryId equals to UPDATED_CATEGORY_ID
        defaultProductHistoryShouldNotBeFound("categoryId.in=" + UPDATED_CATEGORY_ID);
    }

    @Test
    void getAllProductHistoriesByCategoryIdIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where categoryId is not null
        defaultProductHistoryShouldBeFound("categoryId.specified=true");

        // Get all the productHistoryList where categoryId is null
        defaultProductHistoryShouldNotBeFound("categoryId.specified=false");
    }

    @Test
    void getAllProductHistoriesByCategoryIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where categoryId is greater than or equal to DEFAULT_CATEGORY_ID
        defaultProductHistoryShouldBeFound("categoryId.greaterThanOrEqual=" + DEFAULT_CATEGORY_ID);

        // Get all the productHistoryList where categoryId is greater than or equal to UPDATED_CATEGORY_ID
        defaultProductHistoryShouldNotBeFound("categoryId.greaterThanOrEqual=" + UPDATED_CATEGORY_ID);
    }

    @Test
    void getAllProductHistoriesByCategoryIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where categoryId is less than or equal to DEFAULT_CATEGORY_ID
        defaultProductHistoryShouldBeFound("categoryId.lessThanOrEqual=" + DEFAULT_CATEGORY_ID);

        // Get all the productHistoryList where categoryId is less than or equal to SMALLER_CATEGORY_ID
        defaultProductHistoryShouldNotBeFound("categoryId.lessThanOrEqual=" + SMALLER_CATEGORY_ID);
    }

    @Test
    void getAllProductHistoriesByCategoryIdIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where categoryId is less than DEFAULT_CATEGORY_ID
        defaultProductHistoryShouldNotBeFound("categoryId.lessThan=" + DEFAULT_CATEGORY_ID);

        // Get all the productHistoryList where categoryId is less than UPDATED_CATEGORY_ID
        defaultProductHistoryShouldBeFound("categoryId.lessThan=" + UPDATED_CATEGORY_ID);
    }

    @Test
    void getAllProductHistoriesByCategoryIdIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where categoryId is greater than DEFAULT_CATEGORY_ID
        defaultProductHistoryShouldNotBeFound("categoryId.greaterThan=" + DEFAULT_CATEGORY_ID);

        // Get all the productHistoryList where categoryId is greater than SMALLER_CATEGORY_ID
        defaultProductHistoryShouldBeFound("categoryId.greaterThan=" + SMALLER_CATEGORY_ID);
    }

    @Test
    void getAllProductHistoriesByPartyIdIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where partyId equals to DEFAULT_PARTY_ID
        defaultProductHistoryShouldBeFound("partyId.equals=" + DEFAULT_PARTY_ID);

        // Get all the productHistoryList where partyId equals to UPDATED_PARTY_ID
        defaultProductHistoryShouldNotBeFound("partyId.equals=" + UPDATED_PARTY_ID);
    }

    @Test
    void getAllProductHistoriesByPartyIdIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where partyId in DEFAULT_PARTY_ID or UPDATED_PARTY_ID
        defaultProductHistoryShouldBeFound("partyId.in=" + DEFAULT_PARTY_ID + "," + UPDATED_PARTY_ID);

        // Get all the productHistoryList where partyId equals to UPDATED_PARTY_ID
        defaultProductHistoryShouldNotBeFound("partyId.in=" + UPDATED_PARTY_ID);
    }

    @Test
    void getAllProductHistoriesByPartyIdIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where partyId is not null
        defaultProductHistoryShouldBeFound("partyId.specified=true");

        // Get all the productHistoryList where partyId is null
        defaultProductHistoryShouldNotBeFound("partyId.specified=false");
    }

    @Test
    void getAllProductHistoriesByPartyIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where partyId is greater than or equal to DEFAULT_PARTY_ID
        defaultProductHistoryShouldBeFound("partyId.greaterThanOrEqual=" + DEFAULT_PARTY_ID);

        // Get all the productHistoryList where partyId is greater than or equal to UPDATED_PARTY_ID
        defaultProductHistoryShouldNotBeFound("partyId.greaterThanOrEqual=" + UPDATED_PARTY_ID);
    }

    @Test
    void getAllProductHistoriesByPartyIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where partyId is less than or equal to DEFAULT_PARTY_ID
        defaultProductHistoryShouldBeFound("partyId.lessThanOrEqual=" + DEFAULT_PARTY_ID);

        // Get all the productHistoryList where partyId is less than or equal to SMALLER_PARTY_ID
        defaultProductHistoryShouldNotBeFound("partyId.lessThanOrEqual=" + SMALLER_PARTY_ID);
    }

    @Test
    void getAllProductHistoriesByPartyIdIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where partyId is less than DEFAULT_PARTY_ID
        defaultProductHistoryShouldNotBeFound("partyId.lessThan=" + DEFAULT_PARTY_ID);

        // Get all the productHistoryList where partyId is less than UPDATED_PARTY_ID
        defaultProductHistoryShouldBeFound("partyId.lessThan=" + UPDATED_PARTY_ID);
    }

    @Test
    void getAllProductHistoriesByPartyIdIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where partyId is greater than DEFAULT_PARTY_ID
        defaultProductHistoryShouldNotBeFound("partyId.greaterThan=" + DEFAULT_PARTY_ID);

        // Get all the productHistoryList where partyId is greater than SMALLER_PARTY_ID
        defaultProductHistoryShouldBeFound("partyId.greaterThan=" + SMALLER_PARTY_ID);
    }

    @Test
    void getAllProductHistoriesByProductIdIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where productId equals to DEFAULT_PRODUCT_ID
        defaultProductHistoryShouldBeFound("productId.equals=" + DEFAULT_PRODUCT_ID);

        // Get all the productHistoryList where productId equals to UPDATED_PRODUCT_ID
        defaultProductHistoryShouldNotBeFound("productId.equals=" + UPDATED_PRODUCT_ID);
    }

    @Test
    void getAllProductHistoriesByProductIdIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where productId in DEFAULT_PRODUCT_ID or UPDATED_PRODUCT_ID
        defaultProductHistoryShouldBeFound("productId.in=" + DEFAULT_PRODUCT_ID + "," + UPDATED_PRODUCT_ID);

        // Get all the productHistoryList where productId equals to UPDATED_PRODUCT_ID
        defaultProductHistoryShouldNotBeFound("productId.in=" + UPDATED_PRODUCT_ID);
    }

    @Test
    void getAllProductHistoriesByProductIdIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where productId is not null
        defaultProductHistoryShouldBeFound("productId.specified=true");

        // Get all the productHistoryList where productId is null
        defaultProductHistoryShouldNotBeFound("productId.specified=false");
    }

    @Test
    void getAllProductHistoriesByProductIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where productId is greater than or equal to DEFAULT_PRODUCT_ID
        defaultProductHistoryShouldBeFound("productId.greaterThanOrEqual=" + DEFAULT_PRODUCT_ID);

        // Get all the productHistoryList where productId is greater than or equal to UPDATED_PRODUCT_ID
        defaultProductHistoryShouldNotBeFound("productId.greaterThanOrEqual=" + UPDATED_PRODUCT_ID);
    }

    @Test
    void getAllProductHistoriesByProductIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where productId is less than or equal to DEFAULT_PRODUCT_ID
        defaultProductHistoryShouldBeFound("productId.lessThanOrEqual=" + DEFAULT_PRODUCT_ID);

        // Get all the productHistoryList where productId is less than or equal to SMALLER_PRODUCT_ID
        defaultProductHistoryShouldNotBeFound("productId.lessThanOrEqual=" + SMALLER_PRODUCT_ID);
    }

    @Test
    void getAllProductHistoriesByProductIdIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where productId is less than DEFAULT_PRODUCT_ID
        defaultProductHistoryShouldNotBeFound("productId.lessThan=" + DEFAULT_PRODUCT_ID);

        // Get all the productHistoryList where productId is less than UPDATED_PRODUCT_ID
        defaultProductHistoryShouldBeFound("productId.lessThan=" + UPDATED_PRODUCT_ID);
    }

    @Test
    void getAllProductHistoriesByProductIdIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where productId is greater than DEFAULT_PRODUCT_ID
        defaultProductHistoryShouldNotBeFound("productId.greaterThan=" + DEFAULT_PRODUCT_ID);

        // Get all the productHistoryList where productId is greater than SMALLER_PRODUCT_ID
        defaultProductHistoryShouldBeFound("productId.greaterThan=" + SMALLER_PRODUCT_ID);
    }

    @Test
    void getAllProductHistoriesByPriceIdIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where priceId equals to DEFAULT_PRICE_ID
        defaultProductHistoryShouldBeFound("priceId.equals=" + DEFAULT_PRICE_ID);

        // Get all the productHistoryList where priceId equals to UPDATED_PRICE_ID
        defaultProductHistoryShouldNotBeFound("priceId.equals=" + UPDATED_PRICE_ID);
    }

    @Test
    void getAllProductHistoriesByPriceIdIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where priceId in DEFAULT_PRICE_ID or UPDATED_PRICE_ID
        defaultProductHistoryShouldBeFound("priceId.in=" + DEFAULT_PRICE_ID + "," + UPDATED_PRICE_ID);

        // Get all the productHistoryList where priceId equals to UPDATED_PRICE_ID
        defaultProductHistoryShouldNotBeFound("priceId.in=" + UPDATED_PRICE_ID);
    }

    @Test
    void getAllProductHistoriesByPriceIdIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where priceId is not null
        defaultProductHistoryShouldBeFound("priceId.specified=true");

        // Get all the productHistoryList where priceId is null
        defaultProductHistoryShouldNotBeFound("priceId.specified=false");
    }

    @Test
    void getAllProductHistoriesByPriceIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where priceId is greater than or equal to DEFAULT_PRICE_ID
        defaultProductHistoryShouldBeFound("priceId.greaterThanOrEqual=" + DEFAULT_PRICE_ID);

        // Get all the productHistoryList where priceId is greater than or equal to UPDATED_PRICE_ID
        defaultProductHistoryShouldNotBeFound("priceId.greaterThanOrEqual=" + UPDATED_PRICE_ID);
    }

    @Test
    void getAllProductHistoriesByPriceIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where priceId is less than or equal to DEFAULT_PRICE_ID
        defaultProductHistoryShouldBeFound("priceId.lessThanOrEqual=" + DEFAULT_PRICE_ID);

        // Get all the productHistoryList where priceId is less than or equal to SMALLER_PRICE_ID
        defaultProductHistoryShouldNotBeFound("priceId.lessThanOrEqual=" + SMALLER_PRICE_ID);
    }

    @Test
    void getAllProductHistoriesByPriceIdIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where priceId is less than DEFAULT_PRICE_ID
        defaultProductHistoryShouldNotBeFound("priceId.lessThan=" + DEFAULT_PRICE_ID);

        // Get all the productHistoryList where priceId is less than UPDATED_PRICE_ID
        defaultProductHistoryShouldBeFound("priceId.lessThan=" + UPDATED_PRICE_ID);
    }

    @Test
    void getAllProductHistoriesByPriceIdIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where priceId is greater than DEFAULT_PRICE_ID
        defaultProductHistoryShouldNotBeFound("priceId.greaterThan=" + DEFAULT_PRICE_ID);

        // Get all the productHistoryList where priceId is greater than SMALLER_PRICE_ID
        defaultProductHistoryShouldBeFound("priceId.greaterThan=" + SMALLER_PRICE_ID);
    }

    @Test
    void getAllProductHistoriesByCampaignIdIsEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where campaignId equals to DEFAULT_CAMPAIGN_ID
        defaultProductHistoryShouldBeFound("campaignId.equals=" + DEFAULT_CAMPAIGN_ID);

        // Get all the productHistoryList where campaignId equals to UPDATED_CAMPAIGN_ID
        defaultProductHistoryShouldNotBeFound("campaignId.equals=" + UPDATED_CAMPAIGN_ID);
    }

    @Test
    void getAllProductHistoriesByCampaignIdIsInShouldWork() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where campaignId in DEFAULT_CAMPAIGN_ID or UPDATED_CAMPAIGN_ID
        defaultProductHistoryShouldBeFound("campaignId.in=" + DEFAULT_CAMPAIGN_ID + "," + UPDATED_CAMPAIGN_ID);

        // Get all the productHistoryList where campaignId equals to UPDATED_CAMPAIGN_ID
        defaultProductHistoryShouldNotBeFound("campaignId.in=" + UPDATED_CAMPAIGN_ID);
    }

    @Test
    void getAllProductHistoriesByCampaignIdIsNullOrNotNull() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where campaignId is not null
        defaultProductHistoryShouldBeFound("campaignId.specified=true");

        // Get all the productHistoryList where campaignId is null
        defaultProductHistoryShouldNotBeFound("campaignId.specified=false");
    }

    @Test
    void getAllProductHistoriesByCampaignIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where campaignId is greater than or equal to DEFAULT_CAMPAIGN_ID
        defaultProductHistoryShouldBeFound("campaignId.greaterThanOrEqual=" + DEFAULT_CAMPAIGN_ID);

        // Get all the productHistoryList where campaignId is greater than or equal to UPDATED_CAMPAIGN_ID
        defaultProductHistoryShouldNotBeFound("campaignId.greaterThanOrEqual=" + UPDATED_CAMPAIGN_ID);
    }

    @Test
    void getAllProductHistoriesByCampaignIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where campaignId is less than or equal to DEFAULT_CAMPAIGN_ID
        defaultProductHistoryShouldBeFound("campaignId.lessThanOrEqual=" + DEFAULT_CAMPAIGN_ID);

        // Get all the productHistoryList where campaignId is less than or equal to SMALLER_CAMPAIGN_ID
        defaultProductHistoryShouldNotBeFound("campaignId.lessThanOrEqual=" + SMALLER_CAMPAIGN_ID);
    }

    @Test
    void getAllProductHistoriesByCampaignIdIsLessThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where campaignId is less than DEFAULT_CAMPAIGN_ID
        defaultProductHistoryShouldNotBeFound("campaignId.lessThan=" + DEFAULT_CAMPAIGN_ID);

        // Get all the productHistoryList where campaignId is less than UPDATED_CAMPAIGN_ID
        defaultProductHistoryShouldBeFound("campaignId.lessThan=" + UPDATED_CAMPAIGN_ID);
    }

    @Test
    void getAllProductHistoriesByCampaignIdIsGreaterThanSomething() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        // Get all the productHistoryList where campaignId is greater than DEFAULT_CAMPAIGN_ID
        defaultProductHistoryShouldNotBeFound("campaignId.greaterThan=" + DEFAULT_CAMPAIGN_ID);

        // Get all the productHistoryList where campaignId is greater than SMALLER_CAMPAIGN_ID
        defaultProductHistoryShouldBeFound("campaignId.greaterThan=" + SMALLER_CAMPAIGN_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductHistoryShouldBeFound(String filter) {
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
            .value(hasItem(productHistory.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].typeClassId")
            .value(hasItem(DEFAULT_TYPE_CLASS_ID.intValue()))
            .jsonPath("$.[*].brandClassId")
            .value(hasItem(DEFAULT_BRAND_CLASS_ID.intValue()))
            .jsonPath("$.[*].sizee")
            .value(hasItem(DEFAULT_SIZEE))
            .jsonPath("$.[*].regularSizeClassId")
            .value(hasItem(DEFAULT_REGULAR_SIZE_CLASS_ID.intValue()))
            .jsonPath("$.[*].languageClassId")
            .value(hasItem(DEFAULT_LANGUAGE_CLASS_ID.intValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].keywords")
            .value(hasItem(DEFAULT_KEYWORDS))
            .jsonPath("$.[*].photo1ContentType")
            .value(hasItem(DEFAULT_PHOTO_1_CONTENT_TYPE))
            .jsonPath("$.[*].photo1")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO_1)))
            .jsonPath("$.[*].count")
            .value(hasItem(DEFAULT_COUNT.doubleValue()))
            .jsonPath("$.[*].discount")
            .value(hasItem(DEFAULT_DISCOUNT.doubleValue()))
            .jsonPath("$.[*].originalPrice")
            .value(hasItem(DEFAULT_ORIGINAL_PRICE.doubleValue()))
            .jsonPath("$.[*].finalPrice")
            .value(hasItem(DEFAULT_FINAL_PRICE.doubleValue()))
            .jsonPath("$.[*].publishDate")
            .value(hasItem(DEFAULT_PUBLISH_DATE.toString()))
            .jsonPath("$.[*].transportDate")
            .value(hasItem(DEFAULT_TRANSPORT_DATE.toString()))
            .jsonPath("$.[*].currencyClassId")
            .value(hasItem(DEFAULT_CURRENCY_CLASS_ID.intValue()))
            .jsonPath("$.[*].bonus")
            .value(hasItem(DEFAULT_BONUS.doubleValue()))
            .jsonPath("$.[*].warrantyClassId")
            .value(hasItem(DEFAULT_WARRANTY_CLASS_ID.intValue()))
            .jsonPath("$.[*].deliveryPlaceClassId")
            .value(hasItem(DEFAULT_DELIVERY_PLACE_CLASS_ID.intValue()))
            .jsonPath("$.[*].paymentPlaceClassId")
            .value(hasItem(DEFAULT_PAYMENT_PLACE_CLASS_ID.intValue()))
            .jsonPath("$.[*].performance")
            .value(hasItem(DEFAULT_PERFORMANCE.toString()))
            .jsonPath("$.[*].originalityClassId")
            .value(hasItem(DEFAULT_ORIGINALITY_CLASS_ID.intValue()))
            .jsonPath("$.[*].satisfaction")
            .value(hasItem(DEFAULT_SATISFACTION.doubleValue()))
            .jsonPath("$.[*].used")
            .value(hasItem(DEFAULT_USED.booleanValue()))
            .jsonPath("$.[*].categoryId")
            .value(hasItem(DEFAULT_CATEGORY_ID.intValue()))
            .jsonPath("$.[*].partyId")
            .value(hasItem(DEFAULT_PARTY_ID.intValue()))
            .jsonPath("$.[*].productId")
            .value(hasItem(DEFAULT_PRODUCT_ID.intValue()))
            .jsonPath("$.[*].priceId")
            .value(hasItem(DEFAULT_PRICE_ID.intValue()))
            .jsonPath("$.[*].campaignId")
            .value(hasItem(DEFAULT_CAMPAIGN_ID.intValue()));

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
    private void defaultProductHistoryShouldNotBeFound(String filter) {
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
    void getNonExistingProductHistory() {
        // Get the productHistory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingProductHistory() throws Exception {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        int databaseSizeBeforeUpdate = productHistoryRepository.findAll().collectList().block().size();

        // Update the productHistory
        ProductHistory updatedProductHistory = productHistoryRepository.findById(productHistory.getId()).block();
        updatedProductHistory
            .name(UPDATED_NAME)
            .typeClassId(UPDATED_TYPE_CLASS_ID)
            .brandClassId(UPDATED_BRAND_CLASS_ID)
            .sizee(UPDATED_SIZEE)
            .regularSizeClassId(UPDATED_REGULAR_SIZE_CLASS_ID)
            .languageClassId(UPDATED_LANGUAGE_CLASS_ID)
            .description(UPDATED_DESCRIPTION)
            .keywords(UPDATED_KEYWORDS)
            .photo1(UPDATED_PHOTO_1)
            .photo1ContentType(UPDATED_PHOTO_1_CONTENT_TYPE)
            .count(UPDATED_COUNT)
            .discount(UPDATED_DISCOUNT)
            .originalPrice(UPDATED_ORIGINAL_PRICE)
            .finalPrice(UPDATED_FINAL_PRICE)
            .publishDate(UPDATED_PUBLISH_DATE)
            .transportDate(UPDATED_TRANSPORT_DATE)
            .currencyClassId(UPDATED_CURRENCY_CLASS_ID)
            .bonus(UPDATED_BONUS)
            .warrantyClassId(UPDATED_WARRANTY_CLASS_ID)
            .deliveryPlaceClassId(UPDATED_DELIVERY_PLACE_CLASS_ID)
            .paymentPlaceClassId(UPDATED_PAYMENT_PLACE_CLASS_ID)
            .performance(UPDATED_PERFORMANCE)
            .originalityClassId(UPDATED_ORIGINALITY_CLASS_ID)
            .satisfaction(UPDATED_SATISFACTION)
            .used(UPDATED_USED)
            .categoryId(UPDATED_CATEGORY_ID)
            .partyId(UPDATED_PARTY_ID)
            .productId(UPDATED_PRODUCT_ID)
            .priceId(UPDATED_PRICE_ID)
            .campaignId(UPDATED_CAMPAIGN_ID);
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(updatedProductHistory);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, productHistoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ProductHistory in the database
        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeUpdate);
        ProductHistory testProductHistory = productHistoryList.get(productHistoryList.size() - 1);
        assertThat(testProductHistory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProductHistory.getTypeClassId()).isEqualTo(UPDATED_TYPE_CLASS_ID);
        assertThat(testProductHistory.getBrandClassId()).isEqualTo(UPDATED_BRAND_CLASS_ID);
        assertThat(testProductHistory.getSizee()).isEqualTo(UPDATED_SIZEE);
        assertThat(testProductHistory.getRegularSizeClassId()).isEqualTo(UPDATED_REGULAR_SIZE_CLASS_ID);
        assertThat(testProductHistory.getLanguageClassId()).isEqualTo(UPDATED_LANGUAGE_CLASS_ID);
        assertThat(testProductHistory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProductHistory.getKeywords()).isEqualTo(UPDATED_KEYWORDS);
        assertThat(testProductHistory.getPhoto1()).isEqualTo(UPDATED_PHOTO_1);
        assertThat(testProductHistory.getPhoto1ContentType()).isEqualTo(UPDATED_PHOTO_1_CONTENT_TYPE);
        assertThat(testProductHistory.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testProductHistory.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testProductHistory.getOriginalPrice()).isEqualTo(UPDATED_ORIGINAL_PRICE);
        assertThat(testProductHistory.getFinalPrice()).isEqualTo(UPDATED_FINAL_PRICE);
        assertThat(testProductHistory.getPublishDate()).isEqualTo(UPDATED_PUBLISH_DATE);
        assertThat(testProductHistory.getTransportDate()).isEqualTo(UPDATED_TRANSPORT_DATE);
        assertThat(testProductHistory.getCurrencyClassId()).isEqualTo(UPDATED_CURRENCY_CLASS_ID);
        assertThat(testProductHistory.getBonus()).isEqualTo(UPDATED_BONUS);
        assertThat(testProductHistory.getWarrantyClassId()).isEqualTo(UPDATED_WARRANTY_CLASS_ID);
        assertThat(testProductHistory.getDeliveryPlaceClassId()).isEqualTo(UPDATED_DELIVERY_PLACE_CLASS_ID);
        assertThat(testProductHistory.getPaymentPlaceClassId()).isEqualTo(UPDATED_PAYMENT_PLACE_CLASS_ID);
        assertThat(testProductHistory.getPerformance()).isEqualTo(UPDATED_PERFORMANCE);
        assertThat(testProductHistory.getOriginalityClassId()).isEqualTo(UPDATED_ORIGINALITY_CLASS_ID);
        assertThat(testProductHistory.getSatisfaction()).isEqualTo(UPDATED_SATISFACTION);
        assertThat(testProductHistory.getUsed()).isEqualTo(UPDATED_USED);
        assertThat(testProductHistory.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testProductHistory.getPartyId()).isEqualTo(UPDATED_PARTY_ID);
        assertThat(testProductHistory.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductHistory.getPriceId()).isEqualTo(UPDATED_PRICE_ID);
        assertThat(testProductHistory.getCampaignId()).isEqualTo(UPDATED_CAMPAIGN_ID);
    }

    @Test
    void putNonExistingProductHistory() throws Exception {
        int databaseSizeBeforeUpdate = productHistoryRepository.findAll().collectList().block().size();
        productHistory.setId(longCount.incrementAndGet());

        // Create the ProductHistory
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, productHistoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductHistory in the database
        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProductHistory() throws Exception {
        int databaseSizeBeforeUpdate = productHistoryRepository.findAll().collectList().block().size();
        productHistory.setId(longCount.incrementAndGet());

        // Create the ProductHistory
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductHistory in the database
        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProductHistory() throws Exception {
        int databaseSizeBeforeUpdate = productHistoryRepository.findAll().collectList().block().size();
        productHistory.setId(longCount.incrementAndGet());

        // Create the ProductHistory
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ProductHistory in the database
        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProductHistoryWithPatch() throws Exception {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        int databaseSizeBeforeUpdate = productHistoryRepository.findAll().collectList().block().size();

        // Update the productHistory using partial update
        ProductHistory partialUpdatedProductHistory = new ProductHistory();
        partialUpdatedProductHistory.setId(productHistory.getId());

        partialUpdatedProductHistory
            .name(UPDATED_NAME)
            .typeClassId(UPDATED_TYPE_CLASS_ID)
            .brandClassId(UPDATED_BRAND_CLASS_ID)
            .discount(UPDATED_DISCOUNT)
            .finalPrice(UPDATED_FINAL_PRICE)
            .transportDate(UPDATED_TRANSPORT_DATE)
            .warrantyClassId(UPDATED_WARRANTY_CLASS_ID)
            .originalityClassId(UPDATED_ORIGINALITY_CLASS_ID)
            .satisfaction(UPDATED_SATISFACTION)
            .categoryId(UPDATED_CATEGORY_ID)
            .priceId(UPDATED_PRICE_ID)
            .campaignId(UPDATED_CAMPAIGN_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProductHistory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProductHistory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ProductHistory in the database
        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeUpdate);
        ProductHistory testProductHistory = productHistoryList.get(productHistoryList.size() - 1);
        assertThat(testProductHistory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProductHistory.getTypeClassId()).isEqualTo(UPDATED_TYPE_CLASS_ID);
        assertThat(testProductHistory.getBrandClassId()).isEqualTo(UPDATED_BRAND_CLASS_ID);
        assertThat(testProductHistory.getSizee()).isEqualTo(DEFAULT_SIZEE);
        assertThat(testProductHistory.getRegularSizeClassId()).isEqualTo(DEFAULT_REGULAR_SIZE_CLASS_ID);
        assertThat(testProductHistory.getLanguageClassId()).isEqualTo(DEFAULT_LANGUAGE_CLASS_ID);
        assertThat(testProductHistory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProductHistory.getKeywords()).isEqualTo(DEFAULT_KEYWORDS);
        assertThat(testProductHistory.getPhoto1()).isEqualTo(DEFAULT_PHOTO_1);
        assertThat(testProductHistory.getPhoto1ContentType()).isEqualTo(DEFAULT_PHOTO_1_CONTENT_TYPE);
        assertThat(testProductHistory.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testProductHistory.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testProductHistory.getOriginalPrice()).isEqualTo(DEFAULT_ORIGINAL_PRICE);
        assertThat(testProductHistory.getFinalPrice()).isEqualTo(UPDATED_FINAL_PRICE);
        assertThat(testProductHistory.getPublishDate()).isEqualTo(DEFAULT_PUBLISH_DATE);
        assertThat(testProductHistory.getTransportDate()).isEqualTo(UPDATED_TRANSPORT_DATE);
        assertThat(testProductHistory.getCurrencyClassId()).isEqualTo(DEFAULT_CURRENCY_CLASS_ID);
        assertThat(testProductHistory.getBonus()).isEqualTo(DEFAULT_BONUS);
        assertThat(testProductHistory.getWarrantyClassId()).isEqualTo(UPDATED_WARRANTY_CLASS_ID);
        assertThat(testProductHistory.getDeliveryPlaceClassId()).isEqualTo(DEFAULT_DELIVERY_PLACE_CLASS_ID);
        assertThat(testProductHistory.getPaymentPlaceClassId()).isEqualTo(DEFAULT_PAYMENT_PLACE_CLASS_ID);
        assertThat(testProductHistory.getPerformance()).isEqualTo(DEFAULT_PERFORMANCE);
        assertThat(testProductHistory.getOriginalityClassId()).isEqualTo(UPDATED_ORIGINALITY_CLASS_ID);
        assertThat(testProductHistory.getSatisfaction()).isEqualTo(UPDATED_SATISFACTION);
        assertThat(testProductHistory.getUsed()).isEqualTo(DEFAULT_USED);
        assertThat(testProductHistory.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testProductHistory.getPartyId()).isEqualTo(DEFAULT_PARTY_ID);
        assertThat(testProductHistory.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testProductHistory.getPriceId()).isEqualTo(UPDATED_PRICE_ID);
        assertThat(testProductHistory.getCampaignId()).isEqualTo(UPDATED_CAMPAIGN_ID);
    }

    @Test
    void fullUpdateProductHistoryWithPatch() throws Exception {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        int databaseSizeBeforeUpdate = productHistoryRepository.findAll().collectList().block().size();

        // Update the productHistory using partial update
        ProductHistory partialUpdatedProductHistory = new ProductHistory();
        partialUpdatedProductHistory.setId(productHistory.getId());

        partialUpdatedProductHistory
            .name(UPDATED_NAME)
            .typeClassId(UPDATED_TYPE_CLASS_ID)
            .brandClassId(UPDATED_BRAND_CLASS_ID)
            .sizee(UPDATED_SIZEE)
            .regularSizeClassId(UPDATED_REGULAR_SIZE_CLASS_ID)
            .languageClassId(UPDATED_LANGUAGE_CLASS_ID)
            .description(UPDATED_DESCRIPTION)
            .keywords(UPDATED_KEYWORDS)
            .photo1(UPDATED_PHOTO_1)
            .photo1ContentType(UPDATED_PHOTO_1_CONTENT_TYPE)
            .count(UPDATED_COUNT)
            .discount(UPDATED_DISCOUNT)
            .originalPrice(UPDATED_ORIGINAL_PRICE)
            .finalPrice(UPDATED_FINAL_PRICE)
            .publishDate(UPDATED_PUBLISH_DATE)
            .transportDate(UPDATED_TRANSPORT_DATE)
            .currencyClassId(UPDATED_CURRENCY_CLASS_ID)
            .bonus(UPDATED_BONUS)
            .warrantyClassId(UPDATED_WARRANTY_CLASS_ID)
            .deliveryPlaceClassId(UPDATED_DELIVERY_PLACE_CLASS_ID)
            .paymentPlaceClassId(UPDATED_PAYMENT_PLACE_CLASS_ID)
            .performance(UPDATED_PERFORMANCE)
            .originalityClassId(UPDATED_ORIGINALITY_CLASS_ID)
            .satisfaction(UPDATED_SATISFACTION)
            .used(UPDATED_USED)
            .categoryId(UPDATED_CATEGORY_ID)
            .partyId(UPDATED_PARTY_ID)
            .productId(UPDATED_PRODUCT_ID)
            .priceId(UPDATED_PRICE_ID)
            .campaignId(UPDATED_CAMPAIGN_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProductHistory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProductHistory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ProductHistory in the database
        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeUpdate);
        ProductHistory testProductHistory = productHistoryList.get(productHistoryList.size() - 1);
        assertThat(testProductHistory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProductHistory.getTypeClassId()).isEqualTo(UPDATED_TYPE_CLASS_ID);
        assertThat(testProductHistory.getBrandClassId()).isEqualTo(UPDATED_BRAND_CLASS_ID);
        assertThat(testProductHistory.getSizee()).isEqualTo(UPDATED_SIZEE);
        assertThat(testProductHistory.getRegularSizeClassId()).isEqualTo(UPDATED_REGULAR_SIZE_CLASS_ID);
        assertThat(testProductHistory.getLanguageClassId()).isEqualTo(UPDATED_LANGUAGE_CLASS_ID);
        assertThat(testProductHistory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProductHistory.getKeywords()).isEqualTo(UPDATED_KEYWORDS);
        assertThat(testProductHistory.getPhoto1()).isEqualTo(UPDATED_PHOTO_1);
        assertThat(testProductHistory.getPhoto1ContentType()).isEqualTo(UPDATED_PHOTO_1_CONTENT_TYPE);
        assertThat(testProductHistory.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testProductHistory.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testProductHistory.getOriginalPrice()).isEqualTo(UPDATED_ORIGINAL_PRICE);
        assertThat(testProductHistory.getFinalPrice()).isEqualTo(UPDATED_FINAL_PRICE);
        assertThat(testProductHistory.getPublishDate()).isEqualTo(UPDATED_PUBLISH_DATE);
        assertThat(testProductHistory.getTransportDate()).isEqualTo(UPDATED_TRANSPORT_DATE);
        assertThat(testProductHistory.getCurrencyClassId()).isEqualTo(UPDATED_CURRENCY_CLASS_ID);
        assertThat(testProductHistory.getBonus()).isEqualTo(UPDATED_BONUS);
        assertThat(testProductHistory.getWarrantyClassId()).isEqualTo(UPDATED_WARRANTY_CLASS_ID);
        assertThat(testProductHistory.getDeliveryPlaceClassId()).isEqualTo(UPDATED_DELIVERY_PLACE_CLASS_ID);
        assertThat(testProductHistory.getPaymentPlaceClassId()).isEqualTo(UPDATED_PAYMENT_PLACE_CLASS_ID);
        assertThat(testProductHistory.getPerformance()).isEqualTo(UPDATED_PERFORMANCE);
        assertThat(testProductHistory.getOriginalityClassId()).isEqualTo(UPDATED_ORIGINALITY_CLASS_ID);
        assertThat(testProductHistory.getSatisfaction()).isEqualTo(UPDATED_SATISFACTION);
        assertThat(testProductHistory.getUsed()).isEqualTo(UPDATED_USED);
        assertThat(testProductHistory.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testProductHistory.getPartyId()).isEqualTo(UPDATED_PARTY_ID);
        assertThat(testProductHistory.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductHistory.getPriceId()).isEqualTo(UPDATED_PRICE_ID);
        assertThat(testProductHistory.getCampaignId()).isEqualTo(UPDATED_CAMPAIGN_ID);
    }

    @Test
    void patchNonExistingProductHistory() throws Exception {
        int databaseSizeBeforeUpdate = productHistoryRepository.findAll().collectList().block().size();
        productHistory.setId(longCount.incrementAndGet());

        // Create the ProductHistory
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, productHistoryDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductHistory in the database
        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProductHistory() throws Exception {
        int databaseSizeBeforeUpdate = productHistoryRepository.findAll().collectList().block().size();
        productHistory.setId(longCount.incrementAndGet());

        // Create the ProductHistory
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductHistory in the database
        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProductHistory() throws Exception {
        int databaseSizeBeforeUpdate = productHistoryRepository.findAll().collectList().block().size();
        productHistory.setId(longCount.incrementAndGet());

        // Create the ProductHistory
        ProductHistoryDTO productHistoryDTO = productHistoryMapper.toDto(productHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(productHistoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ProductHistory in the database
        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProductHistory() {
        // Initialize the database
        productHistoryRepository.save(productHistory).block();

        int databaseSizeBeforeDelete = productHistoryRepository.findAll().collectList().block().size();

        // Delete the productHistory
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, productHistory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ProductHistory> productHistoryList = productHistoryRepository.findAll().collectList().block();
        assertThat(productHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
