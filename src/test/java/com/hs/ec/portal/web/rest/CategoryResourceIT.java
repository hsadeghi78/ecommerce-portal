package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.Category;
import com.hs.ec.portal.domain.Category;
import com.hs.ec.portal.repository.CategoryRepository;
import com.hs.ec.portal.repository.CategoryRepository;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.service.CategoryService;
import com.hs.ec.portal.service.dto.CategoryDTO;
import com.hs.ec.portal.service.mapper.CategoryMapper;
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
 * Integration tests for the {@link CategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CategoryResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HAS_CHILD = false;
    private static final Boolean UPDATED_HAS_CHILD = true;

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;
    private static final Integer SMALLER_LEVEL = 1 - 1;

    private static final String DEFAULT_KEYWORDS = "AAAAAAAAAA";
    private static final String UPDATED_KEYWORDS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryRepository categoryRepositoryMock;

    @Autowired
    private CategoryMapper categoryMapper;

    @Mock
    private CategoryService categoryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Category category;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Category createEntity(EntityManager em) {
        Category category = new Category()
            .title(DEFAULT_TITLE)
            .code(DEFAULT_CODE)
            .hasChild(DEFAULT_HAS_CHILD)
            .level(DEFAULT_LEVEL)
            .keywords(DEFAULT_KEYWORDS)
            .description(DEFAULT_DESCRIPTION);
        return category;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Category createUpdatedEntity(EntityManager em) {
        Category category = new Category()
            .title(UPDATED_TITLE)
            .code(UPDATED_CODE)
            .hasChild(UPDATED_HAS_CHILD)
            .level(UPDATED_LEVEL)
            .keywords(UPDATED_KEYWORDS)
            .description(UPDATED_DESCRIPTION);
        return category;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Category.class).block();
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
        category = createEntity(em);
    }

    @Test
    void createCategory() throws Exception {
        int databaseSizeBeforeCreate = categoryRepository.findAll().collectList().block().size();
        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoryDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll().collectList().block();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate + 1);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCategory.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCategory.getHasChild()).isEqualTo(DEFAULT_HAS_CHILD);
        assertThat(testCategory.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testCategory.getKeywords()).isEqualTo(DEFAULT_KEYWORDS);
        assertThat(testCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createCategoryWithExistingId() throws Exception {
        // Create the Category with an existing ID
        category.setId(1L);
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        int databaseSizeBeforeCreate = categoryRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll().collectList().block();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().collectList().block().size();
        // set the field null
        category.setTitle(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Category> categoryList = categoryRepository.findAll().collectList().block();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().collectList().block().size();
        // set the field null
        category.setCode(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Category> categoryList = categoryRepository.findAll().collectList().block();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkHasChildIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().collectList().block().size();
        // set the field null
        category.setHasChild(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Category> categoryList = categoryRepository.findAll().collectList().block();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().collectList().block().size();
        // set the field null
        category.setLevel(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Category> categoryList = categoryRepository.findAll().collectList().block();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCategories() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList
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
            .value(hasItem(category.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE))
            .jsonPath("$.[*].hasChild")
            .value(hasItem(DEFAULT_HAS_CHILD.booleanValue()))
            .jsonPath("$.[*].level")
            .value(hasItem(DEFAULT_LEVEL))
            .jsonPath("$.[*].keywords")
            .value(hasItem(DEFAULT_KEYWORDS))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCategoriesWithEagerRelationshipsIsEnabled() {
        when(categoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(categoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCategoriesWithEagerRelationshipsIsNotEnabled() {
        when(categoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(categoryRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getCategory() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get the category
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, category.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(category.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.code")
            .value(is(DEFAULT_CODE))
            .jsonPath("$.hasChild")
            .value(is(DEFAULT_HAS_CHILD.booleanValue()))
            .jsonPath("$.level")
            .value(is(DEFAULT_LEVEL))
            .jsonPath("$.keywords")
            .value(is(DEFAULT_KEYWORDS))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getCategoriesByIdFiltering() {
        // Initialize the database
        categoryRepository.save(category).block();

        Long id = category.getId();

        defaultCategoryShouldBeFound("id.equals=" + id);
        defaultCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCategoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllCategoriesByTitleIsEqualToSomething() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where title equals to DEFAULT_TITLE
        defaultCategoryShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the categoryList where title equals to UPDATED_TITLE
        defaultCategoryShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    void getAllCategoriesByTitleIsInShouldWork() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCategoryShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the categoryList where title equals to UPDATED_TITLE
        defaultCategoryShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    void getAllCategoriesByTitleIsNullOrNotNull() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where title is not null
        defaultCategoryShouldBeFound("title.specified=true");

        // Get all the categoryList where title is null
        defaultCategoryShouldNotBeFound("title.specified=false");
    }

    @Test
    void getAllCategoriesByTitleContainsSomething() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where title contains DEFAULT_TITLE
        defaultCategoryShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the categoryList where title contains UPDATED_TITLE
        defaultCategoryShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    void getAllCategoriesByTitleNotContainsSomething() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where title does not contain DEFAULT_TITLE
        defaultCategoryShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the categoryList where title does not contain UPDATED_TITLE
        defaultCategoryShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    void getAllCategoriesByCodeIsEqualToSomething() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where code equals to DEFAULT_CODE
        defaultCategoryShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the categoryList where code equals to UPDATED_CODE
        defaultCategoryShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    void getAllCategoriesByCodeIsInShouldWork() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where code in DEFAULT_CODE or UPDATED_CODE
        defaultCategoryShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the categoryList where code equals to UPDATED_CODE
        defaultCategoryShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    void getAllCategoriesByCodeIsNullOrNotNull() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where code is not null
        defaultCategoryShouldBeFound("code.specified=true");

        // Get all the categoryList where code is null
        defaultCategoryShouldNotBeFound("code.specified=false");
    }

    @Test
    void getAllCategoriesByCodeContainsSomething() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where code contains DEFAULT_CODE
        defaultCategoryShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the categoryList where code contains UPDATED_CODE
        defaultCategoryShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    void getAllCategoriesByCodeNotContainsSomething() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where code does not contain DEFAULT_CODE
        defaultCategoryShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the categoryList where code does not contain UPDATED_CODE
        defaultCategoryShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    void getAllCategoriesByHasChildIsEqualToSomething() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where hasChild equals to DEFAULT_HAS_CHILD
        defaultCategoryShouldBeFound("hasChild.equals=" + DEFAULT_HAS_CHILD);

        // Get all the categoryList where hasChild equals to UPDATED_HAS_CHILD
        defaultCategoryShouldNotBeFound("hasChild.equals=" + UPDATED_HAS_CHILD);
    }

    @Test
    void getAllCategoriesByHasChildIsInShouldWork() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where hasChild in DEFAULT_HAS_CHILD or UPDATED_HAS_CHILD
        defaultCategoryShouldBeFound("hasChild.in=" + DEFAULT_HAS_CHILD + "," + UPDATED_HAS_CHILD);

        // Get all the categoryList where hasChild equals to UPDATED_HAS_CHILD
        defaultCategoryShouldNotBeFound("hasChild.in=" + UPDATED_HAS_CHILD);
    }

    @Test
    void getAllCategoriesByHasChildIsNullOrNotNull() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where hasChild is not null
        defaultCategoryShouldBeFound("hasChild.specified=true");

        // Get all the categoryList where hasChild is null
        defaultCategoryShouldNotBeFound("hasChild.specified=false");
    }

    @Test
    void getAllCategoriesByLevelIsEqualToSomething() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where level equals to DEFAULT_LEVEL
        defaultCategoryShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the categoryList where level equals to UPDATED_LEVEL
        defaultCategoryShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    void getAllCategoriesByLevelIsInShouldWork() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultCategoryShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the categoryList where level equals to UPDATED_LEVEL
        defaultCategoryShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    void getAllCategoriesByLevelIsNullOrNotNull() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where level is not null
        defaultCategoryShouldBeFound("level.specified=true");

        // Get all the categoryList where level is null
        defaultCategoryShouldNotBeFound("level.specified=false");
    }

    @Test
    void getAllCategoriesByLevelIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where level is greater than or equal to DEFAULT_LEVEL
        defaultCategoryShouldBeFound("level.greaterThanOrEqual=" + DEFAULT_LEVEL);

        // Get all the categoryList where level is greater than or equal to UPDATED_LEVEL
        defaultCategoryShouldNotBeFound("level.greaterThanOrEqual=" + UPDATED_LEVEL);
    }

    @Test
    void getAllCategoriesByLevelIsLessThanOrEqualToSomething() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where level is less than or equal to DEFAULT_LEVEL
        defaultCategoryShouldBeFound("level.lessThanOrEqual=" + DEFAULT_LEVEL);

        // Get all the categoryList where level is less than or equal to SMALLER_LEVEL
        defaultCategoryShouldNotBeFound("level.lessThanOrEqual=" + SMALLER_LEVEL);
    }

    @Test
    void getAllCategoriesByLevelIsLessThanSomething() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where level is less than DEFAULT_LEVEL
        defaultCategoryShouldNotBeFound("level.lessThan=" + DEFAULT_LEVEL);

        // Get all the categoryList where level is less than UPDATED_LEVEL
        defaultCategoryShouldBeFound("level.lessThan=" + UPDATED_LEVEL);
    }

    @Test
    void getAllCategoriesByLevelIsGreaterThanSomething() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where level is greater than DEFAULT_LEVEL
        defaultCategoryShouldNotBeFound("level.greaterThan=" + DEFAULT_LEVEL);

        // Get all the categoryList where level is greater than SMALLER_LEVEL
        defaultCategoryShouldBeFound("level.greaterThan=" + SMALLER_LEVEL);
    }

    @Test
    void getAllCategoriesByKeywordsIsEqualToSomething() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where keywords equals to DEFAULT_KEYWORDS
        defaultCategoryShouldBeFound("keywords.equals=" + DEFAULT_KEYWORDS);

        // Get all the categoryList where keywords equals to UPDATED_KEYWORDS
        defaultCategoryShouldNotBeFound("keywords.equals=" + UPDATED_KEYWORDS);
    }

    @Test
    void getAllCategoriesByKeywordsIsInShouldWork() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where keywords in DEFAULT_KEYWORDS or UPDATED_KEYWORDS
        defaultCategoryShouldBeFound("keywords.in=" + DEFAULT_KEYWORDS + "," + UPDATED_KEYWORDS);

        // Get all the categoryList where keywords equals to UPDATED_KEYWORDS
        defaultCategoryShouldNotBeFound("keywords.in=" + UPDATED_KEYWORDS);
    }

    @Test
    void getAllCategoriesByKeywordsIsNullOrNotNull() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where keywords is not null
        defaultCategoryShouldBeFound("keywords.specified=true");

        // Get all the categoryList where keywords is null
        defaultCategoryShouldNotBeFound("keywords.specified=false");
    }

    @Test
    void getAllCategoriesByKeywordsContainsSomething() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where keywords contains DEFAULT_KEYWORDS
        defaultCategoryShouldBeFound("keywords.contains=" + DEFAULT_KEYWORDS);

        // Get all the categoryList where keywords contains UPDATED_KEYWORDS
        defaultCategoryShouldNotBeFound("keywords.contains=" + UPDATED_KEYWORDS);
    }

    @Test
    void getAllCategoriesByKeywordsNotContainsSomething() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where keywords does not contain DEFAULT_KEYWORDS
        defaultCategoryShouldNotBeFound("keywords.doesNotContain=" + DEFAULT_KEYWORDS);

        // Get all the categoryList where keywords does not contain UPDATED_KEYWORDS
        defaultCategoryShouldBeFound("keywords.doesNotContain=" + UPDATED_KEYWORDS);
    }

    @Test
    void getAllCategoriesByDescriptionIsEqualToSomething() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where description equals to DEFAULT_DESCRIPTION
        defaultCategoryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the categoryList where description equals to UPDATED_DESCRIPTION
        defaultCategoryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllCategoriesByDescriptionIsInShouldWork() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCategoryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the categoryList where description equals to UPDATED_DESCRIPTION
        defaultCategoryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllCategoriesByDescriptionIsNullOrNotNull() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where description is not null
        defaultCategoryShouldBeFound("description.specified=true");

        // Get all the categoryList where description is null
        defaultCategoryShouldNotBeFound("description.specified=false");
    }

    @Test
    void getAllCategoriesByDescriptionContainsSomething() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where description contains DEFAULT_DESCRIPTION
        defaultCategoryShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the categoryList where description contains UPDATED_DESCRIPTION
        defaultCategoryShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllCategoriesByDescriptionNotContainsSomething() {
        // Initialize the database
        categoryRepository.save(category).block();

        // Get all the categoryList where description does not contain DEFAULT_DESCRIPTION
        defaultCategoryShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the categoryList where description does not contain UPDATED_DESCRIPTION
        defaultCategoryShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllCategoriesByParentIsEqualToSomething() {
        Category parent = CategoryResourceIT.createEntity(em);
        categoryRepository.save(parent).block();
        Long parentId = parent.getId();
        category.setParentId(parentId);
        categoryRepository.save(category).block();
        // Get all the categoryList where parent equals to parentId
        defaultCategoryShouldBeFound("parentId.equals=" + parentId);

        // Get all the categoryList where parent equals to (parentId + 1)
        defaultCategoryShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoryShouldBeFound(String filter) {
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
            .value(hasItem(category.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE))
            .jsonPath("$.[*].hasChild")
            .value(hasItem(DEFAULT_HAS_CHILD.booleanValue()))
            .jsonPath("$.[*].level")
            .value(hasItem(DEFAULT_LEVEL))
            .jsonPath("$.[*].keywords")
            .value(hasItem(DEFAULT_KEYWORDS))
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
    private void defaultCategoryShouldNotBeFound(String filter) {
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
    void getNonExistingCategory() {
        // Get the category
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCategory() throws Exception {
        // Initialize the database
        categoryRepository.save(category).block();

        int databaseSizeBeforeUpdate = categoryRepository.findAll().collectList().block().size();

        // Update the category
        Category updatedCategory = categoryRepository.findById(category.getId()).block();
        updatedCategory
            .title(UPDATED_TITLE)
            .code(UPDATED_CODE)
            .hasChild(UPDATED_HAS_CHILD)
            .level(UPDATED_LEVEL)
            .keywords(UPDATED_KEYWORDS)
            .description(UPDATED_DESCRIPTION);
        CategoryDTO categoryDTO = categoryMapper.toDto(updatedCategory);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, categoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoryDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll().collectList().block();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCategory.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCategory.getHasChild()).isEqualTo(UPDATED_HAS_CHILD);
        assertThat(testCategory.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testCategory.getKeywords()).isEqualTo(UPDATED_KEYWORDS);
        assertThat(testCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().collectList().block().size();
        category.setId(longCount.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, categoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll().collectList().block();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().collectList().block().size();
        category.setId(longCount.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll().collectList().block();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().collectList().block().size();
        category.setId(longCount.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll().collectList().block();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCategoryWithPatch() throws Exception {
        // Initialize the database
        categoryRepository.save(category).block();

        int databaseSizeBeforeUpdate = categoryRepository.findAll().collectList().block().size();

        // Update the category using partial update
        Category partialUpdatedCategory = new Category();
        partialUpdatedCategory.setId(category.getId());

        partialUpdatedCategory.hasChild(UPDATED_HAS_CHILD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCategory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCategory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll().collectList().block();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCategory.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCategory.getHasChild()).isEqualTo(UPDATED_HAS_CHILD);
        assertThat(testCategory.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testCategory.getKeywords()).isEqualTo(DEFAULT_KEYWORDS);
        assertThat(testCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void fullUpdateCategoryWithPatch() throws Exception {
        // Initialize the database
        categoryRepository.save(category).block();

        int databaseSizeBeforeUpdate = categoryRepository.findAll().collectList().block().size();

        // Update the category using partial update
        Category partialUpdatedCategory = new Category();
        partialUpdatedCategory.setId(category.getId());

        partialUpdatedCategory
            .title(UPDATED_TITLE)
            .code(UPDATED_CODE)
            .hasChild(UPDATED_HAS_CHILD)
            .level(UPDATED_LEVEL)
            .keywords(UPDATED_KEYWORDS)
            .description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCategory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCategory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll().collectList().block();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCategory.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCategory.getHasChild()).isEqualTo(UPDATED_HAS_CHILD);
        assertThat(testCategory.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testCategory.getKeywords()).isEqualTo(UPDATED_KEYWORDS);
        assertThat(testCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().collectList().block().size();
        category.setId(longCount.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, categoryDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll().collectList().block();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().collectList().block().size();
        category.setId(longCount.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll().collectList().block();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().collectList().block().size();
        category.setId(longCount.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll().collectList().block();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCategory() {
        // Initialize the database
        categoryRepository.save(category).block();

        int databaseSizeBeforeDelete = categoryRepository.findAll().collectList().block().size();

        // Delete the category
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, category.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Category> categoryList = categoryRepository.findAll().collectList().block();
        assertThat(categoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
