package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.Factor;
import com.hs.ec.portal.domain.FactorItem;
import com.hs.ec.portal.domain.Product;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.repository.FactorItemRepository;
import com.hs.ec.portal.repository.FactorRepository;
import com.hs.ec.portal.repository.ProductRepository;
import com.hs.ec.portal.service.FactorItemService;
import com.hs.ec.portal.service.dto.FactorItemDTO;
import com.hs.ec.portal.service.mapper.FactorItemMapper;
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
 * Integration tests for the {@link FactorItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FactorItemResourceIT {

    private static final Integer DEFAULT_ROW_NUM = 1;
    private static final Integer UPDATED_ROW_NUM = 2;
    private static final Integer SMALLER_ROW_NUM = 1 - 1;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_COUNT = 1;
    private static final Integer UPDATED_COUNT = 2;
    private static final Integer SMALLER_COUNT = 1 - 1;

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;
    private static final Double SMALLER_DISCOUNT = 1D - 1D;

    private static final Double DEFAULT_TAX = 1D;
    private static final Double UPDATED_TAX = 2D;
    private static final Double SMALLER_TAX = 1D - 1D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/factor-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FactorItemRepository factorItemRepository;

    @Mock
    private FactorItemRepository factorItemRepositoryMock;

    @Autowired
    private FactorItemMapper factorItemMapper;

    @Mock
    private FactorItemService factorItemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FactorItem factorItem;

    @Autowired
    private FactorRepository factorRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactorItem createEntity(EntityManager em) {
        FactorItem factorItem = new FactorItem()
            .rowNum(DEFAULT_ROW_NUM)
            .title(DEFAULT_TITLE)
            .count(DEFAULT_COUNT)
            .discount(DEFAULT_DISCOUNT)
            .tax(DEFAULT_TAX)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        Factor factor;
        factor = em.insert(FactorResourceIT.createEntity(em)).block();
        factorItem.setFactor(factor);
        // Add required entity
        Product product;
        product = em.insert(ProductResourceIT.createEntity(em)).block();
        factorItem.setProduct(product);
        return factorItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactorItem createUpdatedEntity(EntityManager em) {
        FactorItem factorItem = new FactorItem()
            .rowNum(UPDATED_ROW_NUM)
            .title(UPDATED_TITLE)
            .count(UPDATED_COUNT)
            .discount(UPDATED_DISCOUNT)
            .tax(UPDATED_TAX)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        Factor factor;
        factor = em.insert(FactorResourceIT.createUpdatedEntity(em)).block();
        factorItem.setFactor(factor);
        // Add required entity
        Product product;
        product = em.insert(ProductResourceIT.createUpdatedEntity(em)).block();
        factorItem.setProduct(product);
        return factorItem;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FactorItem.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        FactorResourceIT.deleteEntities(em);
        ProductResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        factorItem = createEntity(em);
    }

    @Test
    void createFactorItem() throws Exception {
        int databaseSizeBeforeCreate = factorItemRepository.findAll().collectList().block().size();
        // Create the FactorItem
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItem);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorItemDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the FactorItem in the database
        List<FactorItem> factorItemList = factorItemRepository.findAll().collectList().block();
        assertThat(factorItemList).hasSize(databaseSizeBeforeCreate + 1);
        FactorItem testFactorItem = factorItemList.get(factorItemList.size() - 1);
        assertThat(testFactorItem.getRowNum()).isEqualTo(DEFAULT_ROW_NUM);
        assertThat(testFactorItem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFactorItem.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testFactorItem.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testFactorItem.getTax()).isEqualTo(DEFAULT_TAX);
        assertThat(testFactorItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createFactorItemWithExistingId() throws Exception {
        // Create the FactorItem with an existing ID
        factorItem.setId(1L);
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItem);

        int databaseSizeBeforeCreate = factorItemRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FactorItem in the database
        List<FactorItem> factorItemList = factorItemRepository.findAll().collectList().block();
        assertThat(factorItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkRowNumIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorItemRepository.findAll().collectList().block().size();
        // set the field null
        factorItem.setRowNum(null);

        // Create the FactorItem, which fails.
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItem);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FactorItem> factorItemList = factorItemRepository.findAll().collectList().block();
        assertThat(factorItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorItemRepository.findAll().collectList().block().size();
        // set the field null
        factorItem.setTitle(null);

        // Create the FactorItem, which fails.
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItem);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FactorItem> factorItemList = factorItemRepository.findAll().collectList().block();
        assertThat(factorItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorItemRepository.findAll().collectList().block().size();
        // set the field null
        factorItem.setCount(null);

        // Create the FactorItem, which fails.
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItem);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FactorItem> factorItemList = factorItemRepository.findAll().collectList().block();
        assertThat(factorItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllFactorItems() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList
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
            .value(hasItem(factorItem.getId().intValue()))
            .jsonPath("$.[*].rowNum")
            .value(hasItem(DEFAULT_ROW_NUM))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].count")
            .value(hasItem(DEFAULT_COUNT))
            .jsonPath("$.[*].discount")
            .value(hasItem(DEFAULT_DISCOUNT.doubleValue()))
            .jsonPath("$.[*].tax")
            .value(hasItem(DEFAULT_TAX.doubleValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFactorItemsWithEagerRelationshipsIsEnabled() {
        when(factorItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(factorItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFactorItemsWithEagerRelationshipsIsNotEnabled() {
        when(factorItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(factorItemRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getFactorItem() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get the factorItem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, factorItem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(factorItem.getId().intValue()))
            .jsonPath("$.rowNum")
            .value(is(DEFAULT_ROW_NUM))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.count")
            .value(is(DEFAULT_COUNT))
            .jsonPath("$.discount")
            .value(is(DEFAULT_DISCOUNT.doubleValue()))
            .jsonPath("$.tax")
            .value(is(DEFAULT_TAX.doubleValue()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getFactorItemsByIdFiltering() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        Long id = factorItem.getId();

        defaultFactorItemShouldBeFound("id.equals=" + id);
        defaultFactorItemShouldNotBeFound("id.notEquals=" + id);

        defaultFactorItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFactorItemShouldNotBeFound("id.greaterThan=" + id);

        defaultFactorItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFactorItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllFactorItemsByRowNumIsEqualToSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where rowNum equals to DEFAULT_ROW_NUM
        defaultFactorItemShouldBeFound("rowNum.equals=" + DEFAULT_ROW_NUM);

        // Get all the factorItemList where rowNum equals to UPDATED_ROW_NUM
        defaultFactorItemShouldNotBeFound("rowNum.equals=" + UPDATED_ROW_NUM);
    }

    @Test
    void getAllFactorItemsByRowNumIsInShouldWork() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where rowNum in DEFAULT_ROW_NUM or UPDATED_ROW_NUM
        defaultFactorItemShouldBeFound("rowNum.in=" + DEFAULT_ROW_NUM + "," + UPDATED_ROW_NUM);

        // Get all the factorItemList where rowNum equals to UPDATED_ROW_NUM
        defaultFactorItemShouldNotBeFound("rowNum.in=" + UPDATED_ROW_NUM);
    }

    @Test
    void getAllFactorItemsByRowNumIsNullOrNotNull() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where rowNum is not null
        defaultFactorItemShouldBeFound("rowNum.specified=true");

        // Get all the factorItemList where rowNum is null
        defaultFactorItemShouldNotBeFound("rowNum.specified=false");
    }

    @Test
    void getAllFactorItemsByRowNumIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where rowNum is greater than or equal to DEFAULT_ROW_NUM
        defaultFactorItemShouldBeFound("rowNum.greaterThanOrEqual=" + DEFAULT_ROW_NUM);

        // Get all the factorItemList where rowNum is greater than or equal to UPDATED_ROW_NUM
        defaultFactorItemShouldNotBeFound("rowNum.greaterThanOrEqual=" + UPDATED_ROW_NUM);
    }

    @Test
    void getAllFactorItemsByRowNumIsLessThanOrEqualToSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where rowNum is less than or equal to DEFAULT_ROW_NUM
        defaultFactorItemShouldBeFound("rowNum.lessThanOrEqual=" + DEFAULT_ROW_NUM);

        // Get all the factorItemList where rowNum is less than or equal to SMALLER_ROW_NUM
        defaultFactorItemShouldNotBeFound("rowNum.lessThanOrEqual=" + SMALLER_ROW_NUM);
    }

    @Test
    void getAllFactorItemsByRowNumIsLessThanSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where rowNum is less than DEFAULT_ROW_NUM
        defaultFactorItemShouldNotBeFound("rowNum.lessThan=" + DEFAULT_ROW_NUM);

        // Get all the factorItemList where rowNum is less than UPDATED_ROW_NUM
        defaultFactorItemShouldBeFound("rowNum.lessThan=" + UPDATED_ROW_NUM);
    }

    @Test
    void getAllFactorItemsByRowNumIsGreaterThanSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where rowNum is greater than DEFAULT_ROW_NUM
        defaultFactorItemShouldNotBeFound("rowNum.greaterThan=" + DEFAULT_ROW_NUM);

        // Get all the factorItemList where rowNum is greater than SMALLER_ROW_NUM
        defaultFactorItemShouldBeFound("rowNum.greaterThan=" + SMALLER_ROW_NUM);
    }

    @Test
    void getAllFactorItemsByTitleIsEqualToSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where title equals to DEFAULT_TITLE
        defaultFactorItemShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the factorItemList where title equals to UPDATED_TITLE
        defaultFactorItemShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    void getAllFactorItemsByTitleIsInShouldWork() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultFactorItemShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the factorItemList where title equals to UPDATED_TITLE
        defaultFactorItemShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    void getAllFactorItemsByTitleIsNullOrNotNull() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where title is not null
        defaultFactorItemShouldBeFound("title.specified=true");

        // Get all the factorItemList where title is null
        defaultFactorItemShouldNotBeFound("title.specified=false");
    }

    @Test
    void getAllFactorItemsByTitleContainsSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where title contains DEFAULT_TITLE
        defaultFactorItemShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the factorItemList where title contains UPDATED_TITLE
        defaultFactorItemShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    void getAllFactorItemsByTitleNotContainsSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where title does not contain DEFAULT_TITLE
        defaultFactorItemShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the factorItemList where title does not contain UPDATED_TITLE
        defaultFactorItemShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    void getAllFactorItemsByCountIsEqualToSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where count equals to DEFAULT_COUNT
        defaultFactorItemShouldBeFound("count.equals=" + DEFAULT_COUNT);

        // Get all the factorItemList where count equals to UPDATED_COUNT
        defaultFactorItemShouldNotBeFound("count.equals=" + UPDATED_COUNT);
    }

    @Test
    void getAllFactorItemsByCountIsInShouldWork() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where count in DEFAULT_COUNT or UPDATED_COUNT
        defaultFactorItemShouldBeFound("count.in=" + DEFAULT_COUNT + "," + UPDATED_COUNT);

        // Get all the factorItemList where count equals to UPDATED_COUNT
        defaultFactorItemShouldNotBeFound("count.in=" + UPDATED_COUNT);
    }

    @Test
    void getAllFactorItemsByCountIsNullOrNotNull() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where count is not null
        defaultFactorItemShouldBeFound("count.specified=true");

        // Get all the factorItemList where count is null
        defaultFactorItemShouldNotBeFound("count.specified=false");
    }

    @Test
    void getAllFactorItemsByCountIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where count is greater than or equal to DEFAULT_COUNT
        defaultFactorItemShouldBeFound("count.greaterThanOrEqual=" + DEFAULT_COUNT);

        // Get all the factorItemList where count is greater than or equal to UPDATED_COUNT
        defaultFactorItemShouldNotBeFound("count.greaterThanOrEqual=" + UPDATED_COUNT);
    }

    @Test
    void getAllFactorItemsByCountIsLessThanOrEqualToSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where count is less than or equal to DEFAULT_COUNT
        defaultFactorItemShouldBeFound("count.lessThanOrEqual=" + DEFAULT_COUNT);

        // Get all the factorItemList where count is less than or equal to SMALLER_COUNT
        defaultFactorItemShouldNotBeFound("count.lessThanOrEqual=" + SMALLER_COUNT);
    }

    @Test
    void getAllFactorItemsByCountIsLessThanSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where count is less than DEFAULT_COUNT
        defaultFactorItemShouldNotBeFound("count.lessThan=" + DEFAULT_COUNT);

        // Get all the factorItemList where count is less than UPDATED_COUNT
        defaultFactorItemShouldBeFound("count.lessThan=" + UPDATED_COUNT);
    }

    @Test
    void getAllFactorItemsByCountIsGreaterThanSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where count is greater than DEFAULT_COUNT
        defaultFactorItemShouldNotBeFound("count.greaterThan=" + DEFAULT_COUNT);

        // Get all the factorItemList where count is greater than SMALLER_COUNT
        defaultFactorItemShouldBeFound("count.greaterThan=" + SMALLER_COUNT);
    }

    @Test
    void getAllFactorItemsByDiscountIsEqualToSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where discount equals to DEFAULT_DISCOUNT
        defaultFactorItemShouldBeFound("discount.equals=" + DEFAULT_DISCOUNT);

        // Get all the factorItemList where discount equals to UPDATED_DISCOUNT
        defaultFactorItemShouldNotBeFound("discount.equals=" + UPDATED_DISCOUNT);
    }

    @Test
    void getAllFactorItemsByDiscountIsInShouldWork() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where discount in DEFAULT_DISCOUNT or UPDATED_DISCOUNT
        defaultFactorItemShouldBeFound("discount.in=" + DEFAULT_DISCOUNT + "," + UPDATED_DISCOUNT);

        // Get all the factorItemList where discount equals to UPDATED_DISCOUNT
        defaultFactorItemShouldNotBeFound("discount.in=" + UPDATED_DISCOUNT);
    }

    @Test
    void getAllFactorItemsByDiscountIsNullOrNotNull() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where discount is not null
        defaultFactorItemShouldBeFound("discount.specified=true");

        // Get all the factorItemList where discount is null
        defaultFactorItemShouldNotBeFound("discount.specified=false");
    }

    @Test
    void getAllFactorItemsByDiscountIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where discount is greater than or equal to DEFAULT_DISCOUNT
        defaultFactorItemShouldBeFound("discount.greaterThanOrEqual=" + DEFAULT_DISCOUNT);

        // Get all the factorItemList where discount is greater than or equal to UPDATED_DISCOUNT
        defaultFactorItemShouldNotBeFound("discount.greaterThanOrEqual=" + UPDATED_DISCOUNT);
    }

    @Test
    void getAllFactorItemsByDiscountIsLessThanOrEqualToSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where discount is less than or equal to DEFAULT_DISCOUNT
        defaultFactorItemShouldBeFound("discount.lessThanOrEqual=" + DEFAULT_DISCOUNT);

        // Get all the factorItemList where discount is less than or equal to SMALLER_DISCOUNT
        defaultFactorItemShouldNotBeFound("discount.lessThanOrEqual=" + SMALLER_DISCOUNT);
    }

    @Test
    void getAllFactorItemsByDiscountIsLessThanSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where discount is less than DEFAULT_DISCOUNT
        defaultFactorItemShouldNotBeFound("discount.lessThan=" + DEFAULT_DISCOUNT);

        // Get all the factorItemList where discount is less than UPDATED_DISCOUNT
        defaultFactorItemShouldBeFound("discount.lessThan=" + UPDATED_DISCOUNT);
    }

    @Test
    void getAllFactorItemsByDiscountIsGreaterThanSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where discount is greater than DEFAULT_DISCOUNT
        defaultFactorItemShouldNotBeFound("discount.greaterThan=" + DEFAULT_DISCOUNT);

        // Get all the factorItemList where discount is greater than SMALLER_DISCOUNT
        defaultFactorItemShouldBeFound("discount.greaterThan=" + SMALLER_DISCOUNT);
    }

    @Test
    void getAllFactorItemsByTaxIsEqualToSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where tax equals to DEFAULT_TAX
        defaultFactorItemShouldBeFound("tax.equals=" + DEFAULT_TAX);

        // Get all the factorItemList where tax equals to UPDATED_TAX
        defaultFactorItemShouldNotBeFound("tax.equals=" + UPDATED_TAX);
    }

    @Test
    void getAllFactorItemsByTaxIsInShouldWork() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where tax in DEFAULT_TAX or UPDATED_TAX
        defaultFactorItemShouldBeFound("tax.in=" + DEFAULT_TAX + "," + UPDATED_TAX);

        // Get all the factorItemList where tax equals to UPDATED_TAX
        defaultFactorItemShouldNotBeFound("tax.in=" + UPDATED_TAX);
    }

    @Test
    void getAllFactorItemsByTaxIsNullOrNotNull() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where tax is not null
        defaultFactorItemShouldBeFound("tax.specified=true");

        // Get all the factorItemList where tax is null
        defaultFactorItemShouldNotBeFound("tax.specified=false");
    }

    @Test
    void getAllFactorItemsByTaxIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where tax is greater than or equal to DEFAULT_TAX
        defaultFactorItemShouldBeFound("tax.greaterThanOrEqual=" + DEFAULT_TAX);

        // Get all the factorItemList where tax is greater than or equal to UPDATED_TAX
        defaultFactorItemShouldNotBeFound("tax.greaterThanOrEqual=" + UPDATED_TAX);
    }

    @Test
    void getAllFactorItemsByTaxIsLessThanOrEqualToSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where tax is less than or equal to DEFAULT_TAX
        defaultFactorItemShouldBeFound("tax.lessThanOrEqual=" + DEFAULT_TAX);

        // Get all the factorItemList where tax is less than or equal to SMALLER_TAX
        defaultFactorItemShouldNotBeFound("tax.lessThanOrEqual=" + SMALLER_TAX);
    }

    @Test
    void getAllFactorItemsByTaxIsLessThanSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where tax is less than DEFAULT_TAX
        defaultFactorItemShouldNotBeFound("tax.lessThan=" + DEFAULT_TAX);

        // Get all the factorItemList where tax is less than UPDATED_TAX
        defaultFactorItemShouldBeFound("tax.lessThan=" + UPDATED_TAX);
    }

    @Test
    void getAllFactorItemsByTaxIsGreaterThanSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where tax is greater than DEFAULT_TAX
        defaultFactorItemShouldNotBeFound("tax.greaterThan=" + DEFAULT_TAX);

        // Get all the factorItemList where tax is greater than SMALLER_TAX
        defaultFactorItemShouldBeFound("tax.greaterThan=" + SMALLER_TAX);
    }

    @Test
    void getAllFactorItemsByDescriptionIsEqualToSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where description equals to DEFAULT_DESCRIPTION
        defaultFactorItemShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the factorItemList where description equals to UPDATED_DESCRIPTION
        defaultFactorItemShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllFactorItemsByDescriptionIsInShouldWork() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFactorItemShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the factorItemList where description equals to UPDATED_DESCRIPTION
        defaultFactorItemShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllFactorItemsByDescriptionIsNullOrNotNull() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where description is not null
        defaultFactorItemShouldBeFound("description.specified=true");

        // Get all the factorItemList where description is null
        defaultFactorItemShouldNotBeFound("description.specified=false");
    }

    @Test
    void getAllFactorItemsByDescriptionContainsSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where description contains DEFAULT_DESCRIPTION
        defaultFactorItemShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the factorItemList where description contains UPDATED_DESCRIPTION
        defaultFactorItemShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllFactorItemsByDescriptionNotContainsSomething() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        // Get all the factorItemList where description does not contain DEFAULT_DESCRIPTION
        defaultFactorItemShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the factorItemList where description does not contain UPDATED_DESCRIPTION
        defaultFactorItemShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllFactorItemsByFactorIsEqualToSomething() {
        Factor factor = FactorResourceIT.createEntity(em);
        factorRepository.save(factor).block();
        Long factorId = factor.getId();
        factorItem.setFactorId(factorId);
        factorItemRepository.save(factorItem).block();
        // Get all the factorItemList where factor equals to factorId
        defaultFactorItemShouldBeFound("factorId.equals=" + factorId);

        // Get all the factorItemList where factor equals to (factorId + 1)
        defaultFactorItemShouldNotBeFound("factorId.equals=" + (factorId + 1));
    }

    @Test
    void getAllFactorItemsByProductIsEqualToSomething() {
        Product product = ProductResourceIT.createEntity(em);
        productRepository.save(product).block();
        Long productId = product.getId();
        factorItem.setProductId(productId);
        factorItemRepository.save(factorItem).block();
        // Get all the factorItemList where product equals to productId
        defaultFactorItemShouldBeFound("productId.equals=" + productId);

        // Get all the factorItemList where product equals to (productId + 1)
        defaultFactorItemShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFactorItemShouldBeFound(String filter) {
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
            .value(hasItem(factorItem.getId().intValue()))
            .jsonPath("$.[*].rowNum")
            .value(hasItem(DEFAULT_ROW_NUM))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].count")
            .value(hasItem(DEFAULT_COUNT))
            .jsonPath("$.[*].discount")
            .value(hasItem(DEFAULT_DISCOUNT.doubleValue()))
            .jsonPath("$.[*].tax")
            .value(hasItem(DEFAULT_TAX.doubleValue()))
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
    private void defaultFactorItemShouldNotBeFound(String filter) {
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
    void getNonExistingFactorItem() {
        // Get the factorItem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFactorItem() throws Exception {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        int databaseSizeBeforeUpdate = factorItemRepository.findAll().collectList().block().size();

        // Update the factorItem
        FactorItem updatedFactorItem = factorItemRepository.findById(factorItem.getId()).block();
        updatedFactorItem
            .rowNum(UPDATED_ROW_NUM)
            .title(UPDATED_TITLE)
            .count(UPDATED_COUNT)
            .discount(UPDATED_DISCOUNT)
            .tax(UPDATED_TAX)
            .description(UPDATED_DESCRIPTION);
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(updatedFactorItem);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, factorItemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorItemDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FactorItem in the database
        List<FactorItem> factorItemList = factorItemRepository.findAll().collectList().block();
        assertThat(factorItemList).hasSize(databaseSizeBeforeUpdate);
        FactorItem testFactorItem = factorItemList.get(factorItemList.size() - 1);
        assertThat(testFactorItem.getRowNum()).isEqualTo(UPDATED_ROW_NUM);
        assertThat(testFactorItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFactorItem.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testFactorItem.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testFactorItem.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testFactorItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingFactorItem() throws Exception {
        int databaseSizeBeforeUpdate = factorItemRepository.findAll().collectList().block().size();
        factorItem.setId(longCount.incrementAndGet());

        // Create the FactorItem
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, factorItemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FactorItem in the database
        List<FactorItem> factorItemList = factorItemRepository.findAll().collectList().block();
        assertThat(factorItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFactorItem() throws Exception {
        int databaseSizeBeforeUpdate = factorItemRepository.findAll().collectList().block().size();
        factorItem.setId(longCount.incrementAndGet());

        // Create the FactorItem
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FactorItem in the database
        List<FactorItem> factorItemList = factorItemRepository.findAll().collectList().block();
        assertThat(factorItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFactorItem() throws Exception {
        int databaseSizeBeforeUpdate = factorItemRepository.findAll().collectList().block().size();
        factorItem.setId(longCount.incrementAndGet());

        // Create the FactorItem
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorItemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FactorItem in the database
        List<FactorItem> factorItemList = factorItemRepository.findAll().collectList().block();
        assertThat(factorItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFactorItemWithPatch() throws Exception {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        int databaseSizeBeforeUpdate = factorItemRepository.findAll().collectList().block().size();

        // Update the factorItem using partial update
        FactorItem partialUpdatedFactorItem = new FactorItem();
        partialUpdatedFactorItem.setId(factorItem.getId());

        partialUpdatedFactorItem.rowNum(UPDATED_ROW_NUM).count(UPDATED_COUNT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFactorItem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFactorItem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FactorItem in the database
        List<FactorItem> factorItemList = factorItemRepository.findAll().collectList().block();
        assertThat(factorItemList).hasSize(databaseSizeBeforeUpdate);
        FactorItem testFactorItem = factorItemList.get(factorItemList.size() - 1);
        assertThat(testFactorItem.getRowNum()).isEqualTo(UPDATED_ROW_NUM);
        assertThat(testFactorItem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFactorItem.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testFactorItem.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testFactorItem.getTax()).isEqualTo(DEFAULT_TAX);
        assertThat(testFactorItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void fullUpdateFactorItemWithPatch() throws Exception {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        int databaseSizeBeforeUpdate = factorItemRepository.findAll().collectList().block().size();

        // Update the factorItem using partial update
        FactorItem partialUpdatedFactorItem = new FactorItem();
        partialUpdatedFactorItem.setId(factorItem.getId());

        partialUpdatedFactorItem
            .rowNum(UPDATED_ROW_NUM)
            .title(UPDATED_TITLE)
            .count(UPDATED_COUNT)
            .discount(UPDATED_DISCOUNT)
            .tax(UPDATED_TAX)
            .description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFactorItem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFactorItem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FactorItem in the database
        List<FactorItem> factorItemList = factorItemRepository.findAll().collectList().block();
        assertThat(factorItemList).hasSize(databaseSizeBeforeUpdate);
        FactorItem testFactorItem = factorItemList.get(factorItemList.size() - 1);
        assertThat(testFactorItem.getRowNum()).isEqualTo(UPDATED_ROW_NUM);
        assertThat(testFactorItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFactorItem.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testFactorItem.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testFactorItem.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testFactorItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingFactorItem() throws Exception {
        int databaseSizeBeforeUpdate = factorItemRepository.findAll().collectList().block().size();
        factorItem.setId(longCount.incrementAndGet());

        // Create the FactorItem
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, factorItemDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FactorItem in the database
        List<FactorItem> factorItemList = factorItemRepository.findAll().collectList().block();
        assertThat(factorItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFactorItem() throws Exception {
        int databaseSizeBeforeUpdate = factorItemRepository.findAll().collectList().block().size();
        factorItem.setId(longCount.incrementAndGet());

        // Create the FactorItem
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FactorItem in the database
        List<FactorItem> factorItemList = factorItemRepository.findAll().collectList().block();
        assertThat(factorItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFactorItem() throws Exception {
        int databaseSizeBeforeUpdate = factorItemRepository.findAll().collectList().block().size();
        factorItem.setId(longCount.incrementAndGet());

        // Create the FactorItem
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(factorItemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FactorItem in the database
        List<FactorItem> factorItemList = factorItemRepository.findAll().collectList().block();
        assertThat(factorItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFactorItem() {
        // Initialize the database
        factorItemRepository.save(factorItem).block();

        int databaseSizeBeforeDelete = factorItemRepository.findAll().collectList().block().size();

        // Delete the factorItem
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, factorItem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<FactorItem> factorItemList = factorItemRepository.findAll().collectList().block();
        assertThat(factorItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
