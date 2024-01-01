package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.Category;
import com.hs.ec.portal.domain.Party;
import com.hs.ec.portal.domain.Product;
import com.hs.ec.portal.domain.Product;
import com.hs.ec.portal.domain.enumeration.Performance;
import com.hs.ec.portal.repository.CategoryRepository;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.repository.PartyRepository;
import com.hs.ec.portal.repository.ProductRepository;
import com.hs.ec.portal.repository.ProductRepository;
import com.hs.ec.portal.service.ProductService;
import com.hs.ec.portal.service.dto.ProductDTO;
import com.hs.ec.portal.service.mapper.ProductMapper;
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
import org.springframework.util.Base64Utils;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProductResourceIT {

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

    private static final Long DEFAULT_NATIONALITY_CLASS_ID = 1L;
    private static final Long UPDATED_NATIONALITY_CLASS_ID = 2L;
    private static final Long SMALLER_NATIONALITY_CLASS_ID = 1L - 1L;

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

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductRepository productRepository;

    @Mock
    private ProductRepository productRepositoryMock;

    @Autowired
    private ProductMapper productMapper;

    @Mock
    private ProductService productServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Product product;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PartyRepository partyRepository;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
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
            .nationalityClassId(DEFAULT_NATIONALITY_CLASS_ID)
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
            .used(DEFAULT_USED);
        // Add required entity
        Category category;
        category = em.insert(CategoryResourceIT.createEntity(em)).block();
        product.setCategory(category);
        return product;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product()
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
            .nationalityClassId(UPDATED_NATIONALITY_CLASS_ID)
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
            .used(UPDATED_USED);
        // Add required entity
        Category category;
        category = em.insert(CategoryResourceIT.createUpdatedEntity(em)).block();
        product.setCategory(category);
        return product;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_product__documents").block();
            em.deleteAll(Product.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        CategoryResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        product = createEntity(em);
    }

    @Test
    void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().collectList().block().size();
        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getTypeClassId()).isEqualTo(DEFAULT_TYPE_CLASS_ID);
        assertThat(testProduct.getBrandClassId()).isEqualTo(DEFAULT_BRAND_CLASS_ID);
        assertThat(testProduct.getSizee()).isEqualTo(DEFAULT_SIZEE);
        assertThat(testProduct.getRegularSizeClassId()).isEqualTo(DEFAULT_REGULAR_SIZE_CLASS_ID);
        assertThat(testProduct.getLanguageClassId()).isEqualTo(DEFAULT_LANGUAGE_CLASS_ID);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getKeywords()).isEqualTo(DEFAULT_KEYWORDS);
        assertThat(testProduct.getPhoto1()).isEqualTo(DEFAULT_PHOTO_1);
        assertThat(testProduct.getPhoto1ContentType()).isEqualTo(DEFAULT_PHOTO_1_CONTENT_TYPE);
        assertThat(testProduct.getNationalityClassId()).isEqualTo(DEFAULT_NATIONALITY_CLASS_ID);
        assertThat(testProduct.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testProduct.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testProduct.getOriginalPrice()).isEqualTo(DEFAULT_ORIGINAL_PRICE);
        assertThat(testProduct.getFinalPrice()).isEqualTo(DEFAULT_FINAL_PRICE);
        assertThat(testProduct.getPublishDate()).isEqualTo(DEFAULT_PUBLISH_DATE);
        assertThat(testProduct.getTransportDate()).isEqualTo(DEFAULT_TRANSPORT_DATE);
        assertThat(testProduct.getCurrencyClassId()).isEqualTo(DEFAULT_CURRENCY_CLASS_ID);
        assertThat(testProduct.getBonus()).isEqualTo(DEFAULT_BONUS);
        assertThat(testProduct.getWarrantyClassId()).isEqualTo(DEFAULT_WARRANTY_CLASS_ID);
        assertThat(testProduct.getDeliveryPlaceClassId()).isEqualTo(DEFAULT_DELIVERY_PLACE_CLASS_ID);
        assertThat(testProduct.getPaymentPlaceClassId()).isEqualTo(DEFAULT_PAYMENT_PLACE_CLASS_ID);
        assertThat(testProduct.getPerformance()).isEqualTo(DEFAULT_PERFORMANCE);
        assertThat(testProduct.getOriginalityClassId()).isEqualTo(DEFAULT_ORIGINALITY_CLASS_ID);
        assertThat(testProduct.getSatisfaction()).isEqualTo(DEFAULT_SATISFACTION);
        assertThat(testProduct.getUsed()).isEqualTo(DEFAULT_USED);
    }

    @Test
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        int databaseSizeBeforeCreate = productRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().collectList().block().size();
        // set the field null
        product.setName(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkRegularSizeClassIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().collectList().block().size();
        // set the field null
        product.setRegularSizeClassId(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLanguageClassIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().collectList().block().size();
        // set the field null
        product.setLanguageClassId(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().collectList().block().size();
        // set the field null
        product.setCount(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkOriginalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().collectList().block().size();
        // set the field null
        product.setOriginalPrice(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFinalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().collectList().block().size();
        // set the field null
        product.setFinalPrice(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPublishDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().collectList().block().size();
        // set the field null
        product.setPublishDate(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTransportDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().collectList().block().size();
        // set the field null
        product.setTransportDate(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCurrencyClassIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().collectList().block().size();
        // set the field null
        product.setCurrencyClassId(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUsedIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().collectList().block().size();
        // set the field null
        product.setUsed(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProducts() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList
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
            .value(hasItem(product.getId().intValue()))
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
            .jsonPath("$.[*].nationalityClassId")
            .value(hasItem(DEFAULT_NATIONALITY_CLASS_ID.intValue()))
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
            .value(hasItem(DEFAULT_USED.booleanValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsEnabled() {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(productServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsNotEnabled() {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(productRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getProduct() {
        // Initialize the database
        productRepository.save(product).block();

        // Get the product
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, product.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(product.getId().intValue()))
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
            .jsonPath("$.nationalityClassId")
            .value(is(DEFAULT_NATIONALITY_CLASS_ID.intValue()))
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
            .value(is(DEFAULT_USED.booleanValue()));
    }

    @Test
    void getProductsByIdFiltering() {
        // Initialize the database
        productRepository.save(product).block();

        Long id = product.getId();

        defaultProductShouldBeFound("id.equals=" + id);
        defaultProductShouldNotBeFound("id.notEquals=" + id);

        defaultProductShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.greaterThan=" + id);

        defaultProductShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllProductsByNameIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where name equals to DEFAULT_NAME
        defaultProductShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    void getAllProductsByNameIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProductShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    void getAllProductsByNameIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where name is not null
        defaultProductShouldBeFound("name.specified=true");

        // Get all the productList where name is null
        defaultProductShouldNotBeFound("name.specified=false");
    }

    @Test
    void getAllProductsByNameContainsSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where name contains DEFAULT_NAME
        defaultProductShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the productList where name contains UPDATED_NAME
        defaultProductShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    void getAllProductsByNameNotContainsSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where name does not contain DEFAULT_NAME
        defaultProductShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the productList where name does not contain UPDATED_NAME
        defaultProductShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    void getAllProductsByTypeClassIdIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where typeClassId equals to DEFAULT_TYPE_CLASS_ID
        defaultProductShouldBeFound("typeClassId.equals=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the productList where typeClassId equals to UPDATED_TYPE_CLASS_ID
        defaultProductShouldNotBeFound("typeClassId.equals=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllProductsByTypeClassIdIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where typeClassId in DEFAULT_TYPE_CLASS_ID or UPDATED_TYPE_CLASS_ID
        defaultProductShouldBeFound("typeClassId.in=" + DEFAULT_TYPE_CLASS_ID + "," + UPDATED_TYPE_CLASS_ID);

        // Get all the productList where typeClassId equals to UPDATED_TYPE_CLASS_ID
        defaultProductShouldNotBeFound("typeClassId.in=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllProductsByTypeClassIdIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where typeClassId is not null
        defaultProductShouldBeFound("typeClassId.specified=true");

        // Get all the productList where typeClassId is null
        defaultProductShouldNotBeFound("typeClassId.specified=false");
    }

    @Test
    void getAllProductsByTypeClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where typeClassId is greater than or equal to DEFAULT_TYPE_CLASS_ID
        defaultProductShouldBeFound("typeClassId.greaterThanOrEqual=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the productList where typeClassId is greater than or equal to UPDATED_TYPE_CLASS_ID
        defaultProductShouldNotBeFound("typeClassId.greaterThanOrEqual=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllProductsByTypeClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where typeClassId is less than or equal to DEFAULT_TYPE_CLASS_ID
        defaultProductShouldBeFound("typeClassId.lessThanOrEqual=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the productList where typeClassId is less than or equal to SMALLER_TYPE_CLASS_ID
        defaultProductShouldNotBeFound("typeClassId.lessThanOrEqual=" + SMALLER_TYPE_CLASS_ID);
    }

    @Test
    void getAllProductsByTypeClassIdIsLessThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where typeClassId is less than DEFAULT_TYPE_CLASS_ID
        defaultProductShouldNotBeFound("typeClassId.lessThan=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the productList where typeClassId is less than UPDATED_TYPE_CLASS_ID
        defaultProductShouldBeFound("typeClassId.lessThan=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllProductsByTypeClassIdIsGreaterThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where typeClassId is greater than DEFAULT_TYPE_CLASS_ID
        defaultProductShouldNotBeFound("typeClassId.greaterThan=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the productList where typeClassId is greater than SMALLER_TYPE_CLASS_ID
        defaultProductShouldBeFound("typeClassId.greaterThan=" + SMALLER_TYPE_CLASS_ID);
    }

    @Test
    void getAllProductsByBrandClassIdIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where brandClassId equals to DEFAULT_BRAND_CLASS_ID
        defaultProductShouldBeFound("brandClassId.equals=" + DEFAULT_BRAND_CLASS_ID);

        // Get all the productList where brandClassId equals to UPDATED_BRAND_CLASS_ID
        defaultProductShouldNotBeFound("brandClassId.equals=" + UPDATED_BRAND_CLASS_ID);
    }

    @Test
    void getAllProductsByBrandClassIdIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where brandClassId in DEFAULT_BRAND_CLASS_ID or UPDATED_BRAND_CLASS_ID
        defaultProductShouldBeFound("brandClassId.in=" + DEFAULT_BRAND_CLASS_ID + "," + UPDATED_BRAND_CLASS_ID);

        // Get all the productList where brandClassId equals to UPDATED_BRAND_CLASS_ID
        defaultProductShouldNotBeFound("brandClassId.in=" + UPDATED_BRAND_CLASS_ID);
    }

    @Test
    void getAllProductsByBrandClassIdIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where brandClassId is not null
        defaultProductShouldBeFound("brandClassId.specified=true");

        // Get all the productList where brandClassId is null
        defaultProductShouldNotBeFound("brandClassId.specified=false");
    }

    @Test
    void getAllProductsByBrandClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where brandClassId is greater than or equal to DEFAULT_BRAND_CLASS_ID
        defaultProductShouldBeFound("brandClassId.greaterThanOrEqual=" + DEFAULT_BRAND_CLASS_ID);

        // Get all the productList where brandClassId is greater than or equal to UPDATED_BRAND_CLASS_ID
        defaultProductShouldNotBeFound("brandClassId.greaterThanOrEqual=" + UPDATED_BRAND_CLASS_ID);
    }

    @Test
    void getAllProductsByBrandClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where brandClassId is less than or equal to DEFAULT_BRAND_CLASS_ID
        defaultProductShouldBeFound("brandClassId.lessThanOrEqual=" + DEFAULT_BRAND_CLASS_ID);

        // Get all the productList where brandClassId is less than or equal to SMALLER_BRAND_CLASS_ID
        defaultProductShouldNotBeFound("brandClassId.lessThanOrEqual=" + SMALLER_BRAND_CLASS_ID);
    }

    @Test
    void getAllProductsByBrandClassIdIsLessThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where brandClassId is less than DEFAULT_BRAND_CLASS_ID
        defaultProductShouldNotBeFound("brandClassId.lessThan=" + DEFAULT_BRAND_CLASS_ID);

        // Get all the productList where brandClassId is less than UPDATED_BRAND_CLASS_ID
        defaultProductShouldBeFound("brandClassId.lessThan=" + UPDATED_BRAND_CLASS_ID);
    }

    @Test
    void getAllProductsByBrandClassIdIsGreaterThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where brandClassId is greater than DEFAULT_BRAND_CLASS_ID
        defaultProductShouldNotBeFound("brandClassId.greaterThan=" + DEFAULT_BRAND_CLASS_ID);

        // Get all the productList where brandClassId is greater than SMALLER_BRAND_CLASS_ID
        defaultProductShouldBeFound("brandClassId.greaterThan=" + SMALLER_BRAND_CLASS_ID);
    }

    @Test
    void getAllProductsBySizeeIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where sizee equals to DEFAULT_SIZEE
        defaultProductShouldBeFound("sizee.equals=" + DEFAULT_SIZEE);

        // Get all the productList where sizee equals to UPDATED_SIZEE
        defaultProductShouldNotBeFound("sizee.equals=" + UPDATED_SIZEE);
    }

    @Test
    void getAllProductsBySizeeIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where sizee in DEFAULT_SIZEE or UPDATED_SIZEE
        defaultProductShouldBeFound("sizee.in=" + DEFAULT_SIZEE + "," + UPDATED_SIZEE);

        // Get all the productList where sizee equals to UPDATED_SIZEE
        defaultProductShouldNotBeFound("sizee.in=" + UPDATED_SIZEE);
    }

    @Test
    void getAllProductsBySizeeIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where sizee is not null
        defaultProductShouldBeFound("sizee.specified=true");

        // Get all the productList where sizee is null
        defaultProductShouldNotBeFound("sizee.specified=false");
    }

    @Test
    void getAllProductsBySizeeContainsSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where sizee contains DEFAULT_SIZEE
        defaultProductShouldBeFound("sizee.contains=" + DEFAULT_SIZEE);

        // Get all the productList where sizee contains UPDATED_SIZEE
        defaultProductShouldNotBeFound("sizee.contains=" + UPDATED_SIZEE);
    }

    @Test
    void getAllProductsBySizeeNotContainsSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where sizee does not contain DEFAULT_SIZEE
        defaultProductShouldNotBeFound("sizee.doesNotContain=" + DEFAULT_SIZEE);

        // Get all the productList where sizee does not contain UPDATED_SIZEE
        defaultProductShouldBeFound("sizee.doesNotContain=" + UPDATED_SIZEE);
    }

    @Test
    void getAllProductsByRegularSizeClassIdIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where regularSizeClassId equals to DEFAULT_REGULAR_SIZE_CLASS_ID
        defaultProductShouldBeFound("regularSizeClassId.equals=" + DEFAULT_REGULAR_SIZE_CLASS_ID);

        // Get all the productList where regularSizeClassId equals to UPDATED_REGULAR_SIZE_CLASS_ID
        defaultProductShouldNotBeFound("regularSizeClassId.equals=" + UPDATED_REGULAR_SIZE_CLASS_ID);
    }

    @Test
    void getAllProductsByRegularSizeClassIdIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where regularSizeClassId in DEFAULT_REGULAR_SIZE_CLASS_ID or UPDATED_REGULAR_SIZE_CLASS_ID
        defaultProductShouldBeFound("regularSizeClassId.in=" + DEFAULT_REGULAR_SIZE_CLASS_ID + "," + UPDATED_REGULAR_SIZE_CLASS_ID);

        // Get all the productList where regularSizeClassId equals to UPDATED_REGULAR_SIZE_CLASS_ID
        defaultProductShouldNotBeFound("regularSizeClassId.in=" + UPDATED_REGULAR_SIZE_CLASS_ID);
    }

    @Test
    void getAllProductsByRegularSizeClassIdIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where regularSizeClassId is not null
        defaultProductShouldBeFound("regularSizeClassId.specified=true");

        // Get all the productList where regularSizeClassId is null
        defaultProductShouldNotBeFound("regularSizeClassId.specified=false");
    }

    @Test
    void getAllProductsByRegularSizeClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where regularSizeClassId is greater than or equal to DEFAULT_REGULAR_SIZE_CLASS_ID
        defaultProductShouldBeFound("regularSizeClassId.greaterThanOrEqual=" + DEFAULT_REGULAR_SIZE_CLASS_ID);

        // Get all the productList where regularSizeClassId is greater than or equal to UPDATED_REGULAR_SIZE_CLASS_ID
        defaultProductShouldNotBeFound("regularSizeClassId.greaterThanOrEqual=" + UPDATED_REGULAR_SIZE_CLASS_ID);
    }

    @Test
    void getAllProductsByRegularSizeClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where regularSizeClassId is less than or equal to DEFAULT_REGULAR_SIZE_CLASS_ID
        defaultProductShouldBeFound("regularSizeClassId.lessThanOrEqual=" + DEFAULT_REGULAR_SIZE_CLASS_ID);

        // Get all the productList where regularSizeClassId is less than or equal to SMALLER_REGULAR_SIZE_CLASS_ID
        defaultProductShouldNotBeFound("regularSizeClassId.lessThanOrEqual=" + SMALLER_REGULAR_SIZE_CLASS_ID);
    }

    @Test
    void getAllProductsByRegularSizeClassIdIsLessThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where regularSizeClassId is less than DEFAULT_REGULAR_SIZE_CLASS_ID
        defaultProductShouldNotBeFound("regularSizeClassId.lessThan=" + DEFAULT_REGULAR_SIZE_CLASS_ID);

        // Get all the productList where regularSizeClassId is less than UPDATED_REGULAR_SIZE_CLASS_ID
        defaultProductShouldBeFound("regularSizeClassId.lessThan=" + UPDATED_REGULAR_SIZE_CLASS_ID);
    }

    @Test
    void getAllProductsByRegularSizeClassIdIsGreaterThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where regularSizeClassId is greater than DEFAULT_REGULAR_SIZE_CLASS_ID
        defaultProductShouldNotBeFound("regularSizeClassId.greaterThan=" + DEFAULT_REGULAR_SIZE_CLASS_ID);

        // Get all the productList where regularSizeClassId is greater than SMALLER_REGULAR_SIZE_CLASS_ID
        defaultProductShouldBeFound("regularSizeClassId.greaterThan=" + SMALLER_REGULAR_SIZE_CLASS_ID);
    }

    @Test
    void getAllProductsByLanguageClassIdIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where languageClassId equals to DEFAULT_LANGUAGE_CLASS_ID
        defaultProductShouldBeFound("languageClassId.equals=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the productList where languageClassId equals to UPDATED_LANGUAGE_CLASS_ID
        defaultProductShouldNotBeFound("languageClassId.equals=" + UPDATED_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllProductsByLanguageClassIdIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where languageClassId in DEFAULT_LANGUAGE_CLASS_ID or UPDATED_LANGUAGE_CLASS_ID
        defaultProductShouldBeFound("languageClassId.in=" + DEFAULT_LANGUAGE_CLASS_ID + "," + UPDATED_LANGUAGE_CLASS_ID);

        // Get all the productList where languageClassId equals to UPDATED_LANGUAGE_CLASS_ID
        defaultProductShouldNotBeFound("languageClassId.in=" + UPDATED_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllProductsByLanguageClassIdIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where languageClassId is not null
        defaultProductShouldBeFound("languageClassId.specified=true");

        // Get all the productList where languageClassId is null
        defaultProductShouldNotBeFound("languageClassId.specified=false");
    }

    @Test
    void getAllProductsByLanguageClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where languageClassId is greater than or equal to DEFAULT_LANGUAGE_CLASS_ID
        defaultProductShouldBeFound("languageClassId.greaterThanOrEqual=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the productList where languageClassId is greater than or equal to UPDATED_LANGUAGE_CLASS_ID
        defaultProductShouldNotBeFound("languageClassId.greaterThanOrEqual=" + UPDATED_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllProductsByLanguageClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where languageClassId is less than or equal to DEFAULT_LANGUAGE_CLASS_ID
        defaultProductShouldBeFound("languageClassId.lessThanOrEqual=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the productList where languageClassId is less than or equal to SMALLER_LANGUAGE_CLASS_ID
        defaultProductShouldNotBeFound("languageClassId.lessThanOrEqual=" + SMALLER_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllProductsByLanguageClassIdIsLessThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where languageClassId is less than DEFAULT_LANGUAGE_CLASS_ID
        defaultProductShouldNotBeFound("languageClassId.lessThan=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the productList where languageClassId is less than UPDATED_LANGUAGE_CLASS_ID
        defaultProductShouldBeFound("languageClassId.lessThan=" + UPDATED_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllProductsByLanguageClassIdIsGreaterThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where languageClassId is greater than DEFAULT_LANGUAGE_CLASS_ID
        defaultProductShouldNotBeFound("languageClassId.greaterThan=" + DEFAULT_LANGUAGE_CLASS_ID);

        // Get all the productList where languageClassId is greater than SMALLER_LANGUAGE_CLASS_ID
        defaultProductShouldBeFound("languageClassId.greaterThan=" + SMALLER_LANGUAGE_CLASS_ID);
    }

    @Test
    void getAllProductsByDescriptionIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where description equals to DEFAULT_DESCRIPTION
        defaultProductShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllProductsByDescriptionIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllProductsByDescriptionIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where description is not null
        defaultProductShouldBeFound("description.specified=true");

        // Get all the productList where description is null
        defaultProductShouldNotBeFound("description.specified=false");
    }

    @Test
    void getAllProductsByDescriptionContainsSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where description contains DEFAULT_DESCRIPTION
        defaultProductShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description contains UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllProductsByDescriptionNotContainsSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where description does not contain DEFAULT_DESCRIPTION
        defaultProductShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description does not contain UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllProductsByKeywordsIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where keywords equals to DEFAULT_KEYWORDS
        defaultProductShouldBeFound("keywords.equals=" + DEFAULT_KEYWORDS);

        // Get all the productList where keywords equals to UPDATED_KEYWORDS
        defaultProductShouldNotBeFound("keywords.equals=" + UPDATED_KEYWORDS);
    }

    @Test
    void getAllProductsByKeywordsIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where keywords in DEFAULT_KEYWORDS or UPDATED_KEYWORDS
        defaultProductShouldBeFound("keywords.in=" + DEFAULT_KEYWORDS + "," + UPDATED_KEYWORDS);

        // Get all the productList where keywords equals to UPDATED_KEYWORDS
        defaultProductShouldNotBeFound("keywords.in=" + UPDATED_KEYWORDS);
    }

    @Test
    void getAllProductsByKeywordsIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where keywords is not null
        defaultProductShouldBeFound("keywords.specified=true");

        // Get all the productList where keywords is null
        defaultProductShouldNotBeFound("keywords.specified=false");
    }

    @Test
    void getAllProductsByKeywordsContainsSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where keywords contains DEFAULT_KEYWORDS
        defaultProductShouldBeFound("keywords.contains=" + DEFAULT_KEYWORDS);

        // Get all the productList where keywords contains UPDATED_KEYWORDS
        defaultProductShouldNotBeFound("keywords.contains=" + UPDATED_KEYWORDS);
    }

    @Test
    void getAllProductsByKeywordsNotContainsSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where keywords does not contain DEFAULT_KEYWORDS
        defaultProductShouldNotBeFound("keywords.doesNotContain=" + DEFAULT_KEYWORDS);

        // Get all the productList where keywords does not contain UPDATED_KEYWORDS
        defaultProductShouldBeFound("keywords.doesNotContain=" + UPDATED_KEYWORDS);
    }

    @Test
    void getAllProductsByNationalityClassIdIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where nationalityClassId equals to DEFAULT_NATIONALITY_CLASS_ID
        defaultProductShouldBeFound("nationalityClassId.equals=" + DEFAULT_NATIONALITY_CLASS_ID);

        // Get all the productList where nationalityClassId equals to UPDATED_NATIONALITY_CLASS_ID
        defaultProductShouldNotBeFound("nationalityClassId.equals=" + UPDATED_NATIONALITY_CLASS_ID);
    }

    @Test
    void getAllProductsByNationalityClassIdIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where nationalityClassId in DEFAULT_NATIONALITY_CLASS_ID or UPDATED_NATIONALITY_CLASS_ID
        defaultProductShouldBeFound("nationalityClassId.in=" + DEFAULT_NATIONALITY_CLASS_ID + "," + UPDATED_NATIONALITY_CLASS_ID);

        // Get all the productList where nationalityClassId equals to UPDATED_NATIONALITY_CLASS_ID
        defaultProductShouldNotBeFound("nationalityClassId.in=" + UPDATED_NATIONALITY_CLASS_ID);
    }

    @Test
    void getAllProductsByNationalityClassIdIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where nationalityClassId is not null
        defaultProductShouldBeFound("nationalityClassId.specified=true");

        // Get all the productList where nationalityClassId is null
        defaultProductShouldNotBeFound("nationalityClassId.specified=false");
    }

    @Test
    void getAllProductsByNationalityClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where nationalityClassId is greater than or equal to DEFAULT_NATIONALITY_CLASS_ID
        defaultProductShouldBeFound("nationalityClassId.greaterThanOrEqual=" + DEFAULT_NATIONALITY_CLASS_ID);

        // Get all the productList where nationalityClassId is greater than or equal to UPDATED_NATIONALITY_CLASS_ID
        defaultProductShouldNotBeFound("nationalityClassId.greaterThanOrEqual=" + UPDATED_NATIONALITY_CLASS_ID);
    }

    @Test
    void getAllProductsByNationalityClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where nationalityClassId is less than or equal to DEFAULT_NATIONALITY_CLASS_ID
        defaultProductShouldBeFound("nationalityClassId.lessThanOrEqual=" + DEFAULT_NATIONALITY_CLASS_ID);

        // Get all the productList where nationalityClassId is less than or equal to SMALLER_NATIONALITY_CLASS_ID
        defaultProductShouldNotBeFound("nationalityClassId.lessThanOrEqual=" + SMALLER_NATIONALITY_CLASS_ID);
    }

    @Test
    void getAllProductsByNationalityClassIdIsLessThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where nationalityClassId is less than DEFAULT_NATIONALITY_CLASS_ID
        defaultProductShouldNotBeFound("nationalityClassId.lessThan=" + DEFAULT_NATIONALITY_CLASS_ID);

        // Get all the productList where nationalityClassId is less than UPDATED_NATIONALITY_CLASS_ID
        defaultProductShouldBeFound("nationalityClassId.lessThan=" + UPDATED_NATIONALITY_CLASS_ID);
    }

    @Test
    void getAllProductsByNationalityClassIdIsGreaterThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where nationalityClassId is greater than DEFAULT_NATIONALITY_CLASS_ID
        defaultProductShouldNotBeFound("nationalityClassId.greaterThan=" + DEFAULT_NATIONALITY_CLASS_ID);

        // Get all the productList where nationalityClassId is greater than SMALLER_NATIONALITY_CLASS_ID
        defaultProductShouldBeFound("nationalityClassId.greaterThan=" + SMALLER_NATIONALITY_CLASS_ID);
    }

    @Test
    void getAllProductsByCountIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where count equals to DEFAULT_COUNT
        defaultProductShouldBeFound("count.equals=" + DEFAULT_COUNT);

        // Get all the productList where count equals to UPDATED_COUNT
        defaultProductShouldNotBeFound("count.equals=" + UPDATED_COUNT);
    }

    @Test
    void getAllProductsByCountIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where count in DEFAULT_COUNT or UPDATED_COUNT
        defaultProductShouldBeFound("count.in=" + DEFAULT_COUNT + "," + UPDATED_COUNT);

        // Get all the productList where count equals to UPDATED_COUNT
        defaultProductShouldNotBeFound("count.in=" + UPDATED_COUNT);
    }

    @Test
    void getAllProductsByCountIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where count is not null
        defaultProductShouldBeFound("count.specified=true");

        // Get all the productList where count is null
        defaultProductShouldNotBeFound("count.specified=false");
    }

    @Test
    void getAllProductsByCountIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where count is greater than or equal to DEFAULT_COUNT
        defaultProductShouldBeFound("count.greaterThanOrEqual=" + DEFAULT_COUNT);

        // Get all the productList where count is greater than or equal to UPDATED_COUNT
        defaultProductShouldNotBeFound("count.greaterThanOrEqual=" + UPDATED_COUNT);
    }

    @Test
    void getAllProductsByCountIsLessThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where count is less than or equal to DEFAULT_COUNT
        defaultProductShouldBeFound("count.lessThanOrEqual=" + DEFAULT_COUNT);

        // Get all the productList where count is less than or equal to SMALLER_COUNT
        defaultProductShouldNotBeFound("count.lessThanOrEqual=" + SMALLER_COUNT);
    }

    @Test
    void getAllProductsByCountIsLessThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where count is less than DEFAULT_COUNT
        defaultProductShouldNotBeFound("count.lessThan=" + DEFAULT_COUNT);

        // Get all the productList where count is less than UPDATED_COUNT
        defaultProductShouldBeFound("count.lessThan=" + UPDATED_COUNT);
    }

    @Test
    void getAllProductsByCountIsGreaterThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where count is greater than DEFAULT_COUNT
        defaultProductShouldNotBeFound("count.greaterThan=" + DEFAULT_COUNT);

        // Get all the productList where count is greater than SMALLER_COUNT
        defaultProductShouldBeFound("count.greaterThan=" + SMALLER_COUNT);
    }

    @Test
    void getAllProductsByDiscountIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where discount equals to DEFAULT_DISCOUNT
        defaultProductShouldBeFound("discount.equals=" + DEFAULT_DISCOUNT);

        // Get all the productList where discount equals to UPDATED_DISCOUNT
        defaultProductShouldNotBeFound("discount.equals=" + UPDATED_DISCOUNT);
    }

    @Test
    void getAllProductsByDiscountIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where discount in DEFAULT_DISCOUNT or UPDATED_DISCOUNT
        defaultProductShouldBeFound("discount.in=" + DEFAULT_DISCOUNT + "," + UPDATED_DISCOUNT);

        // Get all the productList where discount equals to UPDATED_DISCOUNT
        defaultProductShouldNotBeFound("discount.in=" + UPDATED_DISCOUNT);
    }

    @Test
    void getAllProductsByDiscountIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where discount is not null
        defaultProductShouldBeFound("discount.specified=true");

        // Get all the productList where discount is null
        defaultProductShouldNotBeFound("discount.specified=false");
    }

    @Test
    void getAllProductsByDiscountIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where discount is greater than or equal to DEFAULT_DISCOUNT
        defaultProductShouldBeFound("discount.greaterThanOrEqual=" + DEFAULT_DISCOUNT);

        // Get all the productList where discount is greater than or equal to UPDATED_DISCOUNT
        defaultProductShouldNotBeFound("discount.greaterThanOrEqual=" + UPDATED_DISCOUNT);
    }

    @Test
    void getAllProductsByDiscountIsLessThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where discount is less than or equal to DEFAULT_DISCOUNT
        defaultProductShouldBeFound("discount.lessThanOrEqual=" + DEFAULT_DISCOUNT);

        // Get all the productList where discount is less than or equal to SMALLER_DISCOUNT
        defaultProductShouldNotBeFound("discount.lessThanOrEqual=" + SMALLER_DISCOUNT);
    }

    @Test
    void getAllProductsByDiscountIsLessThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where discount is less than DEFAULT_DISCOUNT
        defaultProductShouldNotBeFound("discount.lessThan=" + DEFAULT_DISCOUNT);

        // Get all the productList where discount is less than UPDATED_DISCOUNT
        defaultProductShouldBeFound("discount.lessThan=" + UPDATED_DISCOUNT);
    }

    @Test
    void getAllProductsByDiscountIsGreaterThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where discount is greater than DEFAULT_DISCOUNT
        defaultProductShouldNotBeFound("discount.greaterThan=" + DEFAULT_DISCOUNT);

        // Get all the productList where discount is greater than SMALLER_DISCOUNT
        defaultProductShouldBeFound("discount.greaterThan=" + SMALLER_DISCOUNT);
    }

    @Test
    void getAllProductsByOriginalPriceIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where originalPrice equals to DEFAULT_ORIGINAL_PRICE
        defaultProductShouldBeFound("originalPrice.equals=" + DEFAULT_ORIGINAL_PRICE);

        // Get all the productList where originalPrice equals to UPDATED_ORIGINAL_PRICE
        defaultProductShouldNotBeFound("originalPrice.equals=" + UPDATED_ORIGINAL_PRICE);
    }

    @Test
    void getAllProductsByOriginalPriceIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where originalPrice in DEFAULT_ORIGINAL_PRICE or UPDATED_ORIGINAL_PRICE
        defaultProductShouldBeFound("originalPrice.in=" + DEFAULT_ORIGINAL_PRICE + "," + UPDATED_ORIGINAL_PRICE);

        // Get all the productList where originalPrice equals to UPDATED_ORIGINAL_PRICE
        defaultProductShouldNotBeFound("originalPrice.in=" + UPDATED_ORIGINAL_PRICE);
    }

    @Test
    void getAllProductsByOriginalPriceIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where originalPrice is not null
        defaultProductShouldBeFound("originalPrice.specified=true");

        // Get all the productList where originalPrice is null
        defaultProductShouldNotBeFound("originalPrice.specified=false");
    }

    @Test
    void getAllProductsByOriginalPriceIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where originalPrice is greater than or equal to DEFAULT_ORIGINAL_PRICE
        defaultProductShouldBeFound("originalPrice.greaterThanOrEqual=" + DEFAULT_ORIGINAL_PRICE);

        // Get all the productList where originalPrice is greater than or equal to UPDATED_ORIGINAL_PRICE
        defaultProductShouldNotBeFound("originalPrice.greaterThanOrEqual=" + UPDATED_ORIGINAL_PRICE);
    }

    @Test
    void getAllProductsByOriginalPriceIsLessThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where originalPrice is less than or equal to DEFAULT_ORIGINAL_PRICE
        defaultProductShouldBeFound("originalPrice.lessThanOrEqual=" + DEFAULT_ORIGINAL_PRICE);

        // Get all the productList where originalPrice is less than or equal to SMALLER_ORIGINAL_PRICE
        defaultProductShouldNotBeFound("originalPrice.lessThanOrEqual=" + SMALLER_ORIGINAL_PRICE);
    }

    @Test
    void getAllProductsByOriginalPriceIsLessThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where originalPrice is less than DEFAULT_ORIGINAL_PRICE
        defaultProductShouldNotBeFound("originalPrice.lessThan=" + DEFAULT_ORIGINAL_PRICE);

        // Get all the productList where originalPrice is less than UPDATED_ORIGINAL_PRICE
        defaultProductShouldBeFound("originalPrice.lessThan=" + UPDATED_ORIGINAL_PRICE);
    }

    @Test
    void getAllProductsByOriginalPriceIsGreaterThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where originalPrice is greater than DEFAULT_ORIGINAL_PRICE
        defaultProductShouldNotBeFound("originalPrice.greaterThan=" + DEFAULT_ORIGINAL_PRICE);

        // Get all the productList where originalPrice is greater than SMALLER_ORIGINAL_PRICE
        defaultProductShouldBeFound("originalPrice.greaterThan=" + SMALLER_ORIGINAL_PRICE);
    }

    @Test
    void getAllProductsByFinalPriceIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where finalPrice equals to DEFAULT_FINAL_PRICE
        defaultProductShouldBeFound("finalPrice.equals=" + DEFAULT_FINAL_PRICE);

        // Get all the productList where finalPrice equals to UPDATED_FINAL_PRICE
        defaultProductShouldNotBeFound("finalPrice.equals=" + UPDATED_FINAL_PRICE);
    }

    @Test
    void getAllProductsByFinalPriceIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where finalPrice in DEFAULT_FINAL_PRICE or UPDATED_FINAL_PRICE
        defaultProductShouldBeFound("finalPrice.in=" + DEFAULT_FINAL_PRICE + "," + UPDATED_FINAL_PRICE);

        // Get all the productList where finalPrice equals to UPDATED_FINAL_PRICE
        defaultProductShouldNotBeFound("finalPrice.in=" + UPDATED_FINAL_PRICE);
    }

    @Test
    void getAllProductsByFinalPriceIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where finalPrice is not null
        defaultProductShouldBeFound("finalPrice.specified=true");

        // Get all the productList where finalPrice is null
        defaultProductShouldNotBeFound("finalPrice.specified=false");
    }

    @Test
    void getAllProductsByFinalPriceIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where finalPrice is greater than or equal to DEFAULT_FINAL_PRICE
        defaultProductShouldBeFound("finalPrice.greaterThanOrEqual=" + DEFAULT_FINAL_PRICE);

        // Get all the productList where finalPrice is greater than or equal to UPDATED_FINAL_PRICE
        defaultProductShouldNotBeFound("finalPrice.greaterThanOrEqual=" + UPDATED_FINAL_PRICE);
    }

    @Test
    void getAllProductsByFinalPriceIsLessThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where finalPrice is less than or equal to DEFAULT_FINAL_PRICE
        defaultProductShouldBeFound("finalPrice.lessThanOrEqual=" + DEFAULT_FINAL_PRICE);

        // Get all the productList where finalPrice is less than or equal to SMALLER_FINAL_PRICE
        defaultProductShouldNotBeFound("finalPrice.lessThanOrEqual=" + SMALLER_FINAL_PRICE);
    }

    @Test
    void getAllProductsByFinalPriceIsLessThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where finalPrice is less than DEFAULT_FINAL_PRICE
        defaultProductShouldNotBeFound("finalPrice.lessThan=" + DEFAULT_FINAL_PRICE);

        // Get all the productList where finalPrice is less than UPDATED_FINAL_PRICE
        defaultProductShouldBeFound("finalPrice.lessThan=" + UPDATED_FINAL_PRICE);
    }

    @Test
    void getAllProductsByFinalPriceIsGreaterThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where finalPrice is greater than DEFAULT_FINAL_PRICE
        defaultProductShouldNotBeFound("finalPrice.greaterThan=" + DEFAULT_FINAL_PRICE);

        // Get all the productList where finalPrice is greater than SMALLER_FINAL_PRICE
        defaultProductShouldBeFound("finalPrice.greaterThan=" + SMALLER_FINAL_PRICE);
    }

    @Test
    void getAllProductsByPublishDateIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where publishDate equals to DEFAULT_PUBLISH_DATE
        defaultProductShouldBeFound("publishDate.equals=" + DEFAULT_PUBLISH_DATE);

        // Get all the productList where publishDate equals to UPDATED_PUBLISH_DATE
        defaultProductShouldNotBeFound("publishDate.equals=" + UPDATED_PUBLISH_DATE);
    }

    @Test
    void getAllProductsByPublishDateIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where publishDate in DEFAULT_PUBLISH_DATE or UPDATED_PUBLISH_DATE
        defaultProductShouldBeFound("publishDate.in=" + DEFAULT_PUBLISH_DATE + "," + UPDATED_PUBLISH_DATE);

        // Get all the productList where publishDate equals to UPDATED_PUBLISH_DATE
        defaultProductShouldNotBeFound("publishDate.in=" + UPDATED_PUBLISH_DATE);
    }

    @Test
    void getAllProductsByPublishDateIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where publishDate is not null
        defaultProductShouldBeFound("publishDate.specified=true");

        // Get all the productList where publishDate is null
        defaultProductShouldNotBeFound("publishDate.specified=false");
    }

    @Test
    void getAllProductsByPublishDateIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where publishDate is greater than or equal to DEFAULT_PUBLISH_DATE
        defaultProductShouldBeFound("publishDate.greaterThanOrEqual=" + DEFAULT_PUBLISH_DATE);

        // Get all the productList where publishDate is greater than or equal to UPDATED_PUBLISH_DATE
        defaultProductShouldNotBeFound("publishDate.greaterThanOrEqual=" + UPDATED_PUBLISH_DATE);
    }

    @Test
    void getAllProductsByPublishDateIsLessThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where publishDate is less than or equal to DEFAULT_PUBLISH_DATE
        defaultProductShouldBeFound("publishDate.lessThanOrEqual=" + DEFAULT_PUBLISH_DATE);

        // Get all the productList where publishDate is less than or equal to SMALLER_PUBLISH_DATE
        defaultProductShouldNotBeFound("publishDate.lessThanOrEqual=" + SMALLER_PUBLISH_DATE);
    }

    @Test
    void getAllProductsByPublishDateIsLessThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where publishDate is less than DEFAULT_PUBLISH_DATE
        defaultProductShouldNotBeFound("publishDate.lessThan=" + DEFAULT_PUBLISH_DATE);

        // Get all the productList where publishDate is less than UPDATED_PUBLISH_DATE
        defaultProductShouldBeFound("publishDate.lessThan=" + UPDATED_PUBLISH_DATE);
    }

    @Test
    void getAllProductsByPublishDateIsGreaterThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where publishDate is greater than DEFAULT_PUBLISH_DATE
        defaultProductShouldNotBeFound("publishDate.greaterThan=" + DEFAULT_PUBLISH_DATE);

        // Get all the productList where publishDate is greater than SMALLER_PUBLISH_DATE
        defaultProductShouldBeFound("publishDate.greaterThan=" + SMALLER_PUBLISH_DATE);
    }

    @Test
    void getAllProductsByTransportDateIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where transportDate equals to DEFAULT_TRANSPORT_DATE
        defaultProductShouldBeFound("transportDate.equals=" + DEFAULT_TRANSPORT_DATE);

        // Get all the productList where transportDate equals to UPDATED_TRANSPORT_DATE
        defaultProductShouldNotBeFound("transportDate.equals=" + UPDATED_TRANSPORT_DATE);
    }

    @Test
    void getAllProductsByTransportDateIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where transportDate in DEFAULT_TRANSPORT_DATE or UPDATED_TRANSPORT_DATE
        defaultProductShouldBeFound("transportDate.in=" + DEFAULT_TRANSPORT_DATE + "," + UPDATED_TRANSPORT_DATE);

        // Get all the productList where transportDate equals to UPDATED_TRANSPORT_DATE
        defaultProductShouldNotBeFound("transportDate.in=" + UPDATED_TRANSPORT_DATE);
    }

    @Test
    void getAllProductsByTransportDateIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where transportDate is not null
        defaultProductShouldBeFound("transportDate.specified=true");

        // Get all the productList where transportDate is null
        defaultProductShouldNotBeFound("transportDate.specified=false");
    }

    @Test
    void getAllProductsByTransportDateIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where transportDate is greater than or equal to DEFAULT_TRANSPORT_DATE
        defaultProductShouldBeFound("transportDate.greaterThanOrEqual=" + DEFAULT_TRANSPORT_DATE);

        // Get all the productList where transportDate is greater than or equal to UPDATED_TRANSPORT_DATE
        defaultProductShouldNotBeFound("transportDate.greaterThanOrEqual=" + UPDATED_TRANSPORT_DATE);
    }

    @Test
    void getAllProductsByTransportDateIsLessThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where transportDate is less than or equal to DEFAULT_TRANSPORT_DATE
        defaultProductShouldBeFound("transportDate.lessThanOrEqual=" + DEFAULT_TRANSPORT_DATE);

        // Get all the productList where transportDate is less than or equal to SMALLER_TRANSPORT_DATE
        defaultProductShouldNotBeFound("transportDate.lessThanOrEqual=" + SMALLER_TRANSPORT_DATE);
    }

    @Test
    void getAllProductsByTransportDateIsLessThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where transportDate is less than DEFAULT_TRANSPORT_DATE
        defaultProductShouldNotBeFound("transportDate.lessThan=" + DEFAULT_TRANSPORT_DATE);

        // Get all the productList where transportDate is less than UPDATED_TRANSPORT_DATE
        defaultProductShouldBeFound("transportDate.lessThan=" + UPDATED_TRANSPORT_DATE);
    }

    @Test
    void getAllProductsByTransportDateIsGreaterThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where transportDate is greater than DEFAULT_TRANSPORT_DATE
        defaultProductShouldNotBeFound("transportDate.greaterThan=" + DEFAULT_TRANSPORT_DATE);

        // Get all the productList where transportDate is greater than SMALLER_TRANSPORT_DATE
        defaultProductShouldBeFound("transportDate.greaterThan=" + SMALLER_TRANSPORT_DATE);
    }

    @Test
    void getAllProductsByCurrencyClassIdIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where currencyClassId equals to DEFAULT_CURRENCY_CLASS_ID
        defaultProductShouldBeFound("currencyClassId.equals=" + DEFAULT_CURRENCY_CLASS_ID);

        // Get all the productList where currencyClassId equals to UPDATED_CURRENCY_CLASS_ID
        defaultProductShouldNotBeFound("currencyClassId.equals=" + UPDATED_CURRENCY_CLASS_ID);
    }

    @Test
    void getAllProductsByCurrencyClassIdIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where currencyClassId in DEFAULT_CURRENCY_CLASS_ID or UPDATED_CURRENCY_CLASS_ID
        defaultProductShouldBeFound("currencyClassId.in=" + DEFAULT_CURRENCY_CLASS_ID + "," + UPDATED_CURRENCY_CLASS_ID);

        // Get all the productList where currencyClassId equals to UPDATED_CURRENCY_CLASS_ID
        defaultProductShouldNotBeFound("currencyClassId.in=" + UPDATED_CURRENCY_CLASS_ID);
    }

    @Test
    void getAllProductsByCurrencyClassIdIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where currencyClassId is not null
        defaultProductShouldBeFound("currencyClassId.specified=true");

        // Get all the productList where currencyClassId is null
        defaultProductShouldNotBeFound("currencyClassId.specified=false");
    }

    @Test
    void getAllProductsByCurrencyClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where currencyClassId is greater than or equal to DEFAULT_CURRENCY_CLASS_ID
        defaultProductShouldBeFound("currencyClassId.greaterThanOrEqual=" + DEFAULT_CURRENCY_CLASS_ID);

        // Get all the productList where currencyClassId is greater than or equal to UPDATED_CURRENCY_CLASS_ID
        defaultProductShouldNotBeFound("currencyClassId.greaterThanOrEqual=" + UPDATED_CURRENCY_CLASS_ID);
    }

    @Test
    void getAllProductsByCurrencyClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where currencyClassId is less than or equal to DEFAULT_CURRENCY_CLASS_ID
        defaultProductShouldBeFound("currencyClassId.lessThanOrEqual=" + DEFAULT_CURRENCY_CLASS_ID);

        // Get all the productList where currencyClassId is less than or equal to SMALLER_CURRENCY_CLASS_ID
        defaultProductShouldNotBeFound("currencyClassId.lessThanOrEqual=" + SMALLER_CURRENCY_CLASS_ID);
    }

    @Test
    void getAllProductsByCurrencyClassIdIsLessThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where currencyClassId is less than DEFAULT_CURRENCY_CLASS_ID
        defaultProductShouldNotBeFound("currencyClassId.lessThan=" + DEFAULT_CURRENCY_CLASS_ID);

        // Get all the productList where currencyClassId is less than UPDATED_CURRENCY_CLASS_ID
        defaultProductShouldBeFound("currencyClassId.lessThan=" + UPDATED_CURRENCY_CLASS_ID);
    }

    @Test
    void getAllProductsByCurrencyClassIdIsGreaterThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where currencyClassId is greater than DEFAULT_CURRENCY_CLASS_ID
        defaultProductShouldNotBeFound("currencyClassId.greaterThan=" + DEFAULT_CURRENCY_CLASS_ID);

        // Get all the productList where currencyClassId is greater than SMALLER_CURRENCY_CLASS_ID
        defaultProductShouldBeFound("currencyClassId.greaterThan=" + SMALLER_CURRENCY_CLASS_ID);
    }

    @Test
    void getAllProductsByBonusIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where bonus equals to DEFAULT_BONUS
        defaultProductShouldBeFound("bonus.equals=" + DEFAULT_BONUS);

        // Get all the productList where bonus equals to UPDATED_BONUS
        defaultProductShouldNotBeFound("bonus.equals=" + UPDATED_BONUS);
    }

    @Test
    void getAllProductsByBonusIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where bonus in DEFAULT_BONUS or UPDATED_BONUS
        defaultProductShouldBeFound("bonus.in=" + DEFAULT_BONUS + "," + UPDATED_BONUS);

        // Get all the productList where bonus equals to UPDATED_BONUS
        defaultProductShouldNotBeFound("bonus.in=" + UPDATED_BONUS);
    }

    @Test
    void getAllProductsByBonusIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where bonus is not null
        defaultProductShouldBeFound("bonus.specified=true");

        // Get all the productList where bonus is null
        defaultProductShouldNotBeFound("bonus.specified=false");
    }

    @Test
    void getAllProductsByBonusIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where bonus is greater than or equal to DEFAULT_BONUS
        defaultProductShouldBeFound("bonus.greaterThanOrEqual=" + DEFAULT_BONUS);

        // Get all the productList where bonus is greater than or equal to UPDATED_BONUS
        defaultProductShouldNotBeFound("bonus.greaterThanOrEqual=" + UPDATED_BONUS);
    }

    @Test
    void getAllProductsByBonusIsLessThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where bonus is less than or equal to DEFAULT_BONUS
        defaultProductShouldBeFound("bonus.lessThanOrEqual=" + DEFAULT_BONUS);

        // Get all the productList where bonus is less than or equal to SMALLER_BONUS
        defaultProductShouldNotBeFound("bonus.lessThanOrEqual=" + SMALLER_BONUS);
    }

    @Test
    void getAllProductsByBonusIsLessThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where bonus is less than DEFAULT_BONUS
        defaultProductShouldNotBeFound("bonus.lessThan=" + DEFAULT_BONUS);

        // Get all the productList where bonus is less than UPDATED_BONUS
        defaultProductShouldBeFound("bonus.lessThan=" + UPDATED_BONUS);
    }

    @Test
    void getAllProductsByBonusIsGreaterThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where bonus is greater than DEFAULT_BONUS
        defaultProductShouldNotBeFound("bonus.greaterThan=" + DEFAULT_BONUS);

        // Get all the productList where bonus is greater than SMALLER_BONUS
        defaultProductShouldBeFound("bonus.greaterThan=" + SMALLER_BONUS);
    }

    @Test
    void getAllProductsByWarrantyClassIdIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where warrantyClassId equals to DEFAULT_WARRANTY_CLASS_ID
        defaultProductShouldBeFound("warrantyClassId.equals=" + DEFAULT_WARRANTY_CLASS_ID);

        // Get all the productList where warrantyClassId equals to UPDATED_WARRANTY_CLASS_ID
        defaultProductShouldNotBeFound("warrantyClassId.equals=" + UPDATED_WARRANTY_CLASS_ID);
    }

    @Test
    void getAllProductsByWarrantyClassIdIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where warrantyClassId in DEFAULT_WARRANTY_CLASS_ID or UPDATED_WARRANTY_CLASS_ID
        defaultProductShouldBeFound("warrantyClassId.in=" + DEFAULT_WARRANTY_CLASS_ID + "," + UPDATED_WARRANTY_CLASS_ID);

        // Get all the productList where warrantyClassId equals to UPDATED_WARRANTY_CLASS_ID
        defaultProductShouldNotBeFound("warrantyClassId.in=" + UPDATED_WARRANTY_CLASS_ID);
    }

    @Test
    void getAllProductsByWarrantyClassIdIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where warrantyClassId is not null
        defaultProductShouldBeFound("warrantyClassId.specified=true");

        // Get all the productList where warrantyClassId is null
        defaultProductShouldNotBeFound("warrantyClassId.specified=false");
    }

    @Test
    void getAllProductsByWarrantyClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where warrantyClassId is greater than or equal to DEFAULT_WARRANTY_CLASS_ID
        defaultProductShouldBeFound("warrantyClassId.greaterThanOrEqual=" + DEFAULT_WARRANTY_CLASS_ID);

        // Get all the productList where warrantyClassId is greater than or equal to UPDATED_WARRANTY_CLASS_ID
        defaultProductShouldNotBeFound("warrantyClassId.greaterThanOrEqual=" + UPDATED_WARRANTY_CLASS_ID);
    }

    @Test
    void getAllProductsByWarrantyClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where warrantyClassId is less than or equal to DEFAULT_WARRANTY_CLASS_ID
        defaultProductShouldBeFound("warrantyClassId.lessThanOrEqual=" + DEFAULT_WARRANTY_CLASS_ID);

        // Get all the productList where warrantyClassId is less than or equal to SMALLER_WARRANTY_CLASS_ID
        defaultProductShouldNotBeFound("warrantyClassId.lessThanOrEqual=" + SMALLER_WARRANTY_CLASS_ID);
    }

    @Test
    void getAllProductsByWarrantyClassIdIsLessThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where warrantyClassId is less than DEFAULT_WARRANTY_CLASS_ID
        defaultProductShouldNotBeFound("warrantyClassId.lessThan=" + DEFAULT_WARRANTY_CLASS_ID);

        // Get all the productList where warrantyClassId is less than UPDATED_WARRANTY_CLASS_ID
        defaultProductShouldBeFound("warrantyClassId.lessThan=" + UPDATED_WARRANTY_CLASS_ID);
    }

    @Test
    void getAllProductsByWarrantyClassIdIsGreaterThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where warrantyClassId is greater than DEFAULT_WARRANTY_CLASS_ID
        defaultProductShouldNotBeFound("warrantyClassId.greaterThan=" + DEFAULT_WARRANTY_CLASS_ID);

        // Get all the productList where warrantyClassId is greater than SMALLER_WARRANTY_CLASS_ID
        defaultProductShouldBeFound("warrantyClassId.greaterThan=" + SMALLER_WARRANTY_CLASS_ID);
    }

    @Test
    void getAllProductsByDeliveryPlaceClassIdIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where deliveryPlaceClassId equals to DEFAULT_DELIVERY_PLACE_CLASS_ID
        defaultProductShouldBeFound("deliveryPlaceClassId.equals=" + DEFAULT_DELIVERY_PLACE_CLASS_ID);

        // Get all the productList where deliveryPlaceClassId equals to UPDATED_DELIVERY_PLACE_CLASS_ID
        defaultProductShouldNotBeFound("deliveryPlaceClassId.equals=" + UPDATED_DELIVERY_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductsByDeliveryPlaceClassIdIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where deliveryPlaceClassId in DEFAULT_DELIVERY_PLACE_CLASS_ID or UPDATED_DELIVERY_PLACE_CLASS_ID
        defaultProductShouldBeFound("deliveryPlaceClassId.in=" + DEFAULT_DELIVERY_PLACE_CLASS_ID + "," + UPDATED_DELIVERY_PLACE_CLASS_ID);

        // Get all the productList where deliveryPlaceClassId equals to UPDATED_DELIVERY_PLACE_CLASS_ID
        defaultProductShouldNotBeFound("deliveryPlaceClassId.in=" + UPDATED_DELIVERY_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductsByDeliveryPlaceClassIdIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where deliveryPlaceClassId is not null
        defaultProductShouldBeFound("deliveryPlaceClassId.specified=true");

        // Get all the productList where deliveryPlaceClassId is null
        defaultProductShouldNotBeFound("deliveryPlaceClassId.specified=false");
    }

    @Test
    void getAllProductsByDeliveryPlaceClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where deliveryPlaceClassId is greater than or equal to DEFAULT_DELIVERY_PLACE_CLASS_ID
        defaultProductShouldBeFound("deliveryPlaceClassId.greaterThanOrEqual=" + DEFAULT_DELIVERY_PLACE_CLASS_ID);

        // Get all the productList where deliveryPlaceClassId is greater than or equal to UPDATED_DELIVERY_PLACE_CLASS_ID
        defaultProductShouldNotBeFound("deliveryPlaceClassId.greaterThanOrEqual=" + UPDATED_DELIVERY_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductsByDeliveryPlaceClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where deliveryPlaceClassId is less than or equal to DEFAULT_DELIVERY_PLACE_CLASS_ID
        defaultProductShouldBeFound("deliveryPlaceClassId.lessThanOrEqual=" + DEFAULT_DELIVERY_PLACE_CLASS_ID);

        // Get all the productList where deliveryPlaceClassId is less than or equal to SMALLER_DELIVERY_PLACE_CLASS_ID
        defaultProductShouldNotBeFound("deliveryPlaceClassId.lessThanOrEqual=" + SMALLER_DELIVERY_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductsByDeliveryPlaceClassIdIsLessThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where deliveryPlaceClassId is less than DEFAULT_DELIVERY_PLACE_CLASS_ID
        defaultProductShouldNotBeFound("deliveryPlaceClassId.lessThan=" + DEFAULT_DELIVERY_PLACE_CLASS_ID);

        // Get all the productList where deliveryPlaceClassId is less than UPDATED_DELIVERY_PLACE_CLASS_ID
        defaultProductShouldBeFound("deliveryPlaceClassId.lessThan=" + UPDATED_DELIVERY_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductsByDeliveryPlaceClassIdIsGreaterThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where deliveryPlaceClassId is greater than DEFAULT_DELIVERY_PLACE_CLASS_ID
        defaultProductShouldNotBeFound("deliveryPlaceClassId.greaterThan=" + DEFAULT_DELIVERY_PLACE_CLASS_ID);

        // Get all the productList where deliveryPlaceClassId is greater than SMALLER_DELIVERY_PLACE_CLASS_ID
        defaultProductShouldBeFound("deliveryPlaceClassId.greaterThan=" + SMALLER_DELIVERY_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductsByPaymentPlaceClassIdIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where paymentPlaceClassId equals to DEFAULT_PAYMENT_PLACE_CLASS_ID
        defaultProductShouldBeFound("paymentPlaceClassId.equals=" + DEFAULT_PAYMENT_PLACE_CLASS_ID);

        // Get all the productList where paymentPlaceClassId equals to UPDATED_PAYMENT_PLACE_CLASS_ID
        defaultProductShouldNotBeFound("paymentPlaceClassId.equals=" + UPDATED_PAYMENT_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductsByPaymentPlaceClassIdIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where paymentPlaceClassId in DEFAULT_PAYMENT_PLACE_CLASS_ID or UPDATED_PAYMENT_PLACE_CLASS_ID
        defaultProductShouldBeFound("paymentPlaceClassId.in=" + DEFAULT_PAYMENT_PLACE_CLASS_ID + "," + UPDATED_PAYMENT_PLACE_CLASS_ID);

        // Get all the productList where paymentPlaceClassId equals to UPDATED_PAYMENT_PLACE_CLASS_ID
        defaultProductShouldNotBeFound("paymentPlaceClassId.in=" + UPDATED_PAYMENT_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductsByPaymentPlaceClassIdIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where paymentPlaceClassId is not null
        defaultProductShouldBeFound("paymentPlaceClassId.specified=true");

        // Get all the productList where paymentPlaceClassId is null
        defaultProductShouldNotBeFound("paymentPlaceClassId.specified=false");
    }

    @Test
    void getAllProductsByPaymentPlaceClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where paymentPlaceClassId is greater than or equal to DEFAULT_PAYMENT_PLACE_CLASS_ID
        defaultProductShouldBeFound("paymentPlaceClassId.greaterThanOrEqual=" + DEFAULT_PAYMENT_PLACE_CLASS_ID);

        // Get all the productList where paymentPlaceClassId is greater than or equal to UPDATED_PAYMENT_PLACE_CLASS_ID
        defaultProductShouldNotBeFound("paymentPlaceClassId.greaterThanOrEqual=" + UPDATED_PAYMENT_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductsByPaymentPlaceClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where paymentPlaceClassId is less than or equal to DEFAULT_PAYMENT_PLACE_CLASS_ID
        defaultProductShouldBeFound("paymentPlaceClassId.lessThanOrEqual=" + DEFAULT_PAYMENT_PLACE_CLASS_ID);

        // Get all the productList where paymentPlaceClassId is less than or equal to SMALLER_PAYMENT_PLACE_CLASS_ID
        defaultProductShouldNotBeFound("paymentPlaceClassId.lessThanOrEqual=" + SMALLER_PAYMENT_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductsByPaymentPlaceClassIdIsLessThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where paymentPlaceClassId is less than DEFAULT_PAYMENT_PLACE_CLASS_ID
        defaultProductShouldNotBeFound("paymentPlaceClassId.lessThan=" + DEFAULT_PAYMENT_PLACE_CLASS_ID);

        // Get all the productList where paymentPlaceClassId is less than UPDATED_PAYMENT_PLACE_CLASS_ID
        defaultProductShouldBeFound("paymentPlaceClassId.lessThan=" + UPDATED_PAYMENT_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductsByPaymentPlaceClassIdIsGreaterThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where paymentPlaceClassId is greater than DEFAULT_PAYMENT_PLACE_CLASS_ID
        defaultProductShouldNotBeFound("paymentPlaceClassId.greaterThan=" + DEFAULT_PAYMENT_PLACE_CLASS_ID);

        // Get all the productList where paymentPlaceClassId is greater than SMALLER_PAYMENT_PLACE_CLASS_ID
        defaultProductShouldBeFound("paymentPlaceClassId.greaterThan=" + SMALLER_PAYMENT_PLACE_CLASS_ID);
    }

    @Test
    void getAllProductsByPerformanceIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where performance equals to DEFAULT_PERFORMANCE
        defaultProductShouldBeFound("performance.equals=" + DEFAULT_PERFORMANCE);

        // Get all the productList where performance equals to UPDATED_PERFORMANCE
        defaultProductShouldNotBeFound("performance.equals=" + UPDATED_PERFORMANCE);
    }

    @Test
    void getAllProductsByPerformanceIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where performance in DEFAULT_PERFORMANCE or UPDATED_PERFORMANCE
        defaultProductShouldBeFound("performance.in=" + DEFAULT_PERFORMANCE + "," + UPDATED_PERFORMANCE);

        // Get all the productList where performance equals to UPDATED_PERFORMANCE
        defaultProductShouldNotBeFound("performance.in=" + UPDATED_PERFORMANCE);
    }

    @Test
    void getAllProductsByPerformanceIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where performance is not null
        defaultProductShouldBeFound("performance.specified=true");

        // Get all the productList where performance is null
        defaultProductShouldNotBeFound("performance.specified=false");
    }

    @Test
    void getAllProductsByOriginalityClassIdIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where originalityClassId equals to DEFAULT_ORIGINALITY_CLASS_ID
        defaultProductShouldBeFound("originalityClassId.equals=" + DEFAULT_ORIGINALITY_CLASS_ID);

        // Get all the productList where originalityClassId equals to UPDATED_ORIGINALITY_CLASS_ID
        defaultProductShouldNotBeFound("originalityClassId.equals=" + UPDATED_ORIGINALITY_CLASS_ID);
    }

    @Test
    void getAllProductsByOriginalityClassIdIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where originalityClassId in DEFAULT_ORIGINALITY_CLASS_ID or UPDATED_ORIGINALITY_CLASS_ID
        defaultProductShouldBeFound("originalityClassId.in=" + DEFAULT_ORIGINALITY_CLASS_ID + "," + UPDATED_ORIGINALITY_CLASS_ID);

        // Get all the productList where originalityClassId equals to UPDATED_ORIGINALITY_CLASS_ID
        defaultProductShouldNotBeFound("originalityClassId.in=" + UPDATED_ORIGINALITY_CLASS_ID);
    }

    @Test
    void getAllProductsByOriginalityClassIdIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where originalityClassId is not null
        defaultProductShouldBeFound("originalityClassId.specified=true");

        // Get all the productList where originalityClassId is null
        defaultProductShouldNotBeFound("originalityClassId.specified=false");
    }

    @Test
    void getAllProductsByOriginalityClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where originalityClassId is greater than or equal to DEFAULT_ORIGINALITY_CLASS_ID
        defaultProductShouldBeFound("originalityClassId.greaterThanOrEqual=" + DEFAULT_ORIGINALITY_CLASS_ID);

        // Get all the productList where originalityClassId is greater than or equal to UPDATED_ORIGINALITY_CLASS_ID
        defaultProductShouldNotBeFound("originalityClassId.greaterThanOrEqual=" + UPDATED_ORIGINALITY_CLASS_ID);
    }

    @Test
    void getAllProductsByOriginalityClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where originalityClassId is less than or equal to DEFAULT_ORIGINALITY_CLASS_ID
        defaultProductShouldBeFound("originalityClassId.lessThanOrEqual=" + DEFAULT_ORIGINALITY_CLASS_ID);

        // Get all the productList where originalityClassId is less than or equal to SMALLER_ORIGINALITY_CLASS_ID
        defaultProductShouldNotBeFound("originalityClassId.lessThanOrEqual=" + SMALLER_ORIGINALITY_CLASS_ID);
    }

    @Test
    void getAllProductsByOriginalityClassIdIsLessThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where originalityClassId is less than DEFAULT_ORIGINALITY_CLASS_ID
        defaultProductShouldNotBeFound("originalityClassId.lessThan=" + DEFAULT_ORIGINALITY_CLASS_ID);

        // Get all the productList where originalityClassId is less than UPDATED_ORIGINALITY_CLASS_ID
        defaultProductShouldBeFound("originalityClassId.lessThan=" + UPDATED_ORIGINALITY_CLASS_ID);
    }

    @Test
    void getAllProductsByOriginalityClassIdIsGreaterThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where originalityClassId is greater than DEFAULT_ORIGINALITY_CLASS_ID
        defaultProductShouldNotBeFound("originalityClassId.greaterThan=" + DEFAULT_ORIGINALITY_CLASS_ID);

        // Get all the productList where originalityClassId is greater than SMALLER_ORIGINALITY_CLASS_ID
        defaultProductShouldBeFound("originalityClassId.greaterThan=" + SMALLER_ORIGINALITY_CLASS_ID);
    }

    @Test
    void getAllProductsBySatisfactionIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where satisfaction equals to DEFAULT_SATISFACTION
        defaultProductShouldBeFound("satisfaction.equals=" + DEFAULT_SATISFACTION);

        // Get all the productList where satisfaction equals to UPDATED_SATISFACTION
        defaultProductShouldNotBeFound("satisfaction.equals=" + UPDATED_SATISFACTION);
    }

    @Test
    void getAllProductsBySatisfactionIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where satisfaction in DEFAULT_SATISFACTION or UPDATED_SATISFACTION
        defaultProductShouldBeFound("satisfaction.in=" + DEFAULT_SATISFACTION + "," + UPDATED_SATISFACTION);

        // Get all the productList where satisfaction equals to UPDATED_SATISFACTION
        defaultProductShouldNotBeFound("satisfaction.in=" + UPDATED_SATISFACTION);
    }

    @Test
    void getAllProductsBySatisfactionIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where satisfaction is not null
        defaultProductShouldBeFound("satisfaction.specified=true");

        // Get all the productList where satisfaction is null
        defaultProductShouldNotBeFound("satisfaction.specified=false");
    }

    @Test
    void getAllProductsBySatisfactionIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where satisfaction is greater than or equal to DEFAULT_SATISFACTION
        defaultProductShouldBeFound("satisfaction.greaterThanOrEqual=" + DEFAULT_SATISFACTION);

        // Get all the productList where satisfaction is greater than or equal to UPDATED_SATISFACTION
        defaultProductShouldNotBeFound("satisfaction.greaterThanOrEqual=" + UPDATED_SATISFACTION);
    }

    @Test
    void getAllProductsBySatisfactionIsLessThanOrEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where satisfaction is less than or equal to DEFAULT_SATISFACTION
        defaultProductShouldBeFound("satisfaction.lessThanOrEqual=" + DEFAULT_SATISFACTION);

        // Get all the productList where satisfaction is less than or equal to SMALLER_SATISFACTION
        defaultProductShouldNotBeFound("satisfaction.lessThanOrEqual=" + SMALLER_SATISFACTION);
    }

    @Test
    void getAllProductsBySatisfactionIsLessThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where satisfaction is less than DEFAULT_SATISFACTION
        defaultProductShouldNotBeFound("satisfaction.lessThan=" + DEFAULT_SATISFACTION);

        // Get all the productList where satisfaction is less than UPDATED_SATISFACTION
        defaultProductShouldBeFound("satisfaction.lessThan=" + UPDATED_SATISFACTION);
    }

    @Test
    void getAllProductsBySatisfactionIsGreaterThanSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where satisfaction is greater than DEFAULT_SATISFACTION
        defaultProductShouldNotBeFound("satisfaction.greaterThan=" + DEFAULT_SATISFACTION);

        // Get all the productList where satisfaction is greater than SMALLER_SATISFACTION
        defaultProductShouldBeFound("satisfaction.greaterThan=" + SMALLER_SATISFACTION);
    }

    @Test
    void getAllProductsByUsedIsEqualToSomething() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where used equals to DEFAULT_USED
        defaultProductShouldBeFound("used.equals=" + DEFAULT_USED);

        // Get all the productList where used equals to UPDATED_USED
        defaultProductShouldNotBeFound("used.equals=" + UPDATED_USED);
    }

    @Test
    void getAllProductsByUsedIsInShouldWork() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where used in DEFAULT_USED or UPDATED_USED
        defaultProductShouldBeFound("used.in=" + DEFAULT_USED + "," + UPDATED_USED);

        // Get all the productList where used equals to UPDATED_USED
        defaultProductShouldNotBeFound("used.in=" + UPDATED_USED);
    }

    @Test
    void getAllProductsByUsedIsNullOrNotNull() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList where used is not null
        defaultProductShouldBeFound("used.specified=true");

        // Get all the productList where used is null
        defaultProductShouldNotBeFound("used.specified=false");
    }

    @Test
    void getAllProductsByCategoryIsEqualToSomething() {
        Category category = CategoryResourceIT.createEntity(em);
        categoryRepository.save(category).block();
        Long categoryId = category.getId();
        product.setCategoryId(categoryId);
        productRepository.save(product).block();
        // Get all the productList where category equals to categoryId
        defaultProductShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the productList where category equals to (categoryId + 1)
        defaultProductShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    void getAllProductsByPartyIsEqualToSomething() {
        Party party = PartyResourceIT.createEntity(em);
        partyRepository.save(party).block();
        Long partyId = party.getId();
        product.setPartyId(partyId);
        productRepository.save(product).block();
        // Get all the productList where party equals to partyId
        defaultProductShouldBeFound("partyId.equals=" + partyId);

        // Get all the productList where party equals to (partyId + 1)
        defaultProductShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    void getAllProductsByParentIsEqualToSomething() {
        Product parent = ProductResourceIT.createEntity(em);
        productRepository.save(parent).block();
        Long parentId = parent.getId();
        product.setParentId(parentId);
        productRepository.save(product).block();
        // Get all the productList where parent equals to parentId
        defaultProductShouldBeFound("parentId.equals=" + parentId);

        // Get all the productList where parent equals to (parentId + 1)
        defaultProductShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) {
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
            .value(hasItem(product.getId().intValue()))
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
            .jsonPath("$.[*].nationalityClassId")
            .value(hasItem(DEFAULT_NATIONALITY_CLASS_ID.intValue()))
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
            .value(hasItem(DEFAULT_USED.booleanValue()));

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
    private void defaultProductShouldNotBeFound(String filter) {
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
    void getNonExistingProduct() {
        // Get the product
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingProduct() throws Exception {
        // Initialize the database
        productRepository.save(product).block();

        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).block();
        updatedProduct
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
            .nationalityClassId(UPDATED_NATIONALITY_CLASS_ID)
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
            .used(UPDATED_USED);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, productDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getTypeClassId()).isEqualTo(UPDATED_TYPE_CLASS_ID);
        assertThat(testProduct.getBrandClassId()).isEqualTo(UPDATED_BRAND_CLASS_ID);
        assertThat(testProduct.getSizee()).isEqualTo(UPDATED_SIZEE);
        assertThat(testProduct.getRegularSizeClassId()).isEqualTo(UPDATED_REGULAR_SIZE_CLASS_ID);
        assertThat(testProduct.getLanguageClassId()).isEqualTo(UPDATED_LANGUAGE_CLASS_ID);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getKeywords()).isEqualTo(UPDATED_KEYWORDS);
        assertThat(testProduct.getPhoto1()).isEqualTo(UPDATED_PHOTO_1);
        assertThat(testProduct.getPhoto1ContentType()).isEqualTo(UPDATED_PHOTO_1_CONTENT_TYPE);
        assertThat(testProduct.getNationalityClassId()).isEqualTo(UPDATED_NATIONALITY_CLASS_ID);
        assertThat(testProduct.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testProduct.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testProduct.getOriginalPrice()).isEqualTo(UPDATED_ORIGINAL_PRICE);
        assertThat(testProduct.getFinalPrice()).isEqualTo(UPDATED_FINAL_PRICE);
        assertThat(testProduct.getPublishDate()).isEqualTo(UPDATED_PUBLISH_DATE);
        assertThat(testProduct.getTransportDate()).isEqualTo(UPDATED_TRANSPORT_DATE);
        assertThat(testProduct.getCurrencyClassId()).isEqualTo(UPDATED_CURRENCY_CLASS_ID);
        assertThat(testProduct.getBonus()).isEqualTo(UPDATED_BONUS);
        assertThat(testProduct.getWarrantyClassId()).isEqualTo(UPDATED_WARRANTY_CLASS_ID);
        assertThat(testProduct.getDeliveryPlaceClassId()).isEqualTo(UPDATED_DELIVERY_PLACE_CLASS_ID);
        assertThat(testProduct.getPaymentPlaceClassId()).isEqualTo(UPDATED_PAYMENT_PLACE_CLASS_ID);
        assertThat(testProduct.getPerformance()).isEqualTo(UPDATED_PERFORMANCE);
        assertThat(testProduct.getOriginalityClassId()).isEqualTo(UPDATED_ORIGINALITY_CLASS_ID);
        assertThat(testProduct.getSatisfaction()).isEqualTo(UPDATED_SATISFACTION);
        assertThat(testProduct.getUsed()).isEqualTo(UPDATED_USED);
    }

    @Test
    void putNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, productDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.save(product).block();

        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .name(UPDATED_NAME)
            .brandClassId(UPDATED_BRAND_CLASS_ID)
            .description(UPDATED_DESCRIPTION)
            .nationalityClassId(UPDATED_NATIONALITY_CLASS_ID)
            .discount(UPDATED_DISCOUNT)
            .originalPrice(UPDATED_ORIGINAL_PRICE)
            .publishDate(UPDATED_PUBLISH_DATE)
            .currencyClassId(UPDATED_CURRENCY_CLASS_ID)
            .bonus(UPDATED_BONUS)
            .warrantyClassId(UPDATED_WARRANTY_CLASS_ID)
            .deliveryPlaceClassId(UPDATED_DELIVERY_PLACE_CLASS_ID)
            .paymentPlaceClassId(UPDATED_PAYMENT_PLACE_CLASS_ID)
            .used(UPDATED_USED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getTypeClassId()).isEqualTo(DEFAULT_TYPE_CLASS_ID);
        assertThat(testProduct.getBrandClassId()).isEqualTo(UPDATED_BRAND_CLASS_ID);
        assertThat(testProduct.getSizee()).isEqualTo(DEFAULT_SIZEE);
        assertThat(testProduct.getRegularSizeClassId()).isEqualTo(DEFAULT_REGULAR_SIZE_CLASS_ID);
        assertThat(testProduct.getLanguageClassId()).isEqualTo(DEFAULT_LANGUAGE_CLASS_ID);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getKeywords()).isEqualTo(DEFAULT_KEYWORDS);
        assertThat(testProduct.getPhoto1()).isEqualTo(DEFAULT_PHOTO_1);
        assertThat(testProduct.getPhoto1ContentType()).isEqualTo(DEFAULT_PHOTO_1_CONTENT_TYPE);
        assertThat(testProduct.getNationalityClassId()).isEqualTo(UPDATED_NATIONALITY_CLASS_ID);
        assertThat(testProduct.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testProduct.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testProduct.getOriginalPrice()).isEqualTo(UPDATED_ORIGINAL_PRICE);
        assertThat(testProduct.getFinalPrice()).isEqualTo(DEFAULT_FINAL_PRICE);
        assertThat(testProduct.getPublishDate()).isEqualTo(UPDATED_PUBLISH_DATE);
        assertThat(testProduct.getTransportDate()).isEqualTo(DEFAULT_TRANSPORT_DATE);
        assertThat(testProduct.getCurrencyClassId()).isEqualTo(UPDATED_CURRENCY_CLASS_ID);
        assertThat(testProduct.getBonus()).isEqualTo(UPDATED_BONUS);
        assertThat(testProduct.getWarrantyClassId()).isEqualTo(UPDATED_WARRANTY_CLASS_ID);
        assertThat(testProduct.getDeliveryPlaceClassId()).isEqualTo(UPDATED_DELIVERY_PLACE_CLASS_ID);
        assertThat(testProduct.getPaymentPlaceClassId()).isEqualTo(UPDATED_PAYMENT_PLACE_CLASS_ID);
        assertThat(testProduct.getPerformance()).isEqualTo(DEFAULT_PERFORMANCE);
        assertThat(testProduct.getOriginalityClassId()).isEqualTo(DEFAULT_ORIGINALITY_CLASS_ID);
        assertThat(testProduct.getSatisfaction()).isEqualTo(DEFAULT_SATISFACTION);
        assertThat(testProduct.getUsed()).isEqualTo(UPDATED_USED);
    }

    @Test
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.save(product).block();

        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
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
            .nationalityClassId(UPDATED_NATIONALITY_CLASS_ID)
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
            .used(UPDATED_USED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getTypeClassId()).isEqualTo(UPDATED_TYPE_CLASS_ID);
        assertThat(testProduct.getBrandClassId()).isEqualTo(UPDATED_BRAND_CLASS_ID);
        assertThat(testProduct.getSizee()).isEqualTo(UPDATED_SIZEE);
        assertThat(testProduct.getRegularSizeClassId()).isEqualTo(UPDATED_REGULAR_SIZE_CLASS_ID);
        assertThat(testProduct.getLanguageClassId()).isEqualTo(UPDATED_LANGUAGE_CLASS_ID);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getKeywords()).isEqualTo(UPDATED_KEYWORDS);
        assertThat(testProduct.getPhoto1()).isEqualTo(UPDATED_PHOTO_1);
        assertThat(testProduct.getPhoto1ContentType()).isEqualTo(UPDATED_PHOTO_1_CONTENT_TYPE);
        assertThat(testProduct.getNationalityClassId()).isEqualTo(UPDATED_NATIONALITY_CLASS_ID);
        assertThat(testProduct.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testProduct.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testProduct.getOriginalPrice()).isEqualTo(UPDATED_ORIGINAL_PRICE);
        assertThat(testProduct.getFinalPrice()).isEqualTo(UPDATED_FINAL_PRICE);
        assertThat(testProduct.getPublishDate()).isEqualTo(UPDATED_PUBLISH_DATE);
        assertThat(testProduct.getTransportDate()).isEqualTo(UPDATED_TRANSPORT_DATE);
        assertThat(testProduct.getCurrencyClassId()).isEqualTo(UPDATED_CURRENCY_CLASS_ID);
        assertThat(testProduct.getBonus()).isEqualTo(UPDATED_BONUS);
        assertThat(testProduct.getWarrantyClassId()).isEqualTo(UPDATED_WARRANTY_CLASS_ID);
        assertThat(testProduct.getDeliveryPlaceClassId()).isEqualTo(UPDATED_DELIVERY_PLACE_CLASS_ID);
        assertThat(testProduct.getPaymentPlaceClassId()).isEqualTo(UPDATED_PAYMENT_PLACE_CLASS_ID);
        assertThat(testProduct.getPerformance()).isEqualTo(UPDATED_PERFORMANCE);
        assertThat(testProduct.getOriginalityClassId()).isEqualTo(UPDATED_ORIGINALITY_CLASS_ID);
        assertThat(testProduct.getSatisfaction()).isEqualTo(UPDATED_SATISFACTION);
        assertThat(testProduct.getUsed()).isEqualTo(UPDATED_USED);
    }

    @Test
    void patchNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, productDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProduct() {
        // Initialize the database
        productRepository.save(product).block();

        int databaseSizeBeforeDelete = productRepository.findAll().collectList().block().size();

        // Delete the product
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, product.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
