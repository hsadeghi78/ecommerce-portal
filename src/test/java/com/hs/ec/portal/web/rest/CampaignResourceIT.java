package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.Campaign;
import com.hs.ec.portal.repository.CampaignRepository;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.service.CampaignService;
import com.hs.ec.portal.service.dto.CampaignDTO;
import com.hs.ec.portal.service.mapper.CampaignMapper;
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
 * Integration tests for the {@link CampaignResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CampaignResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/campaigns";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CampaignRepository campaignRepository;

    @Mock
    private CampaignRepository campaignRepositoryMock;

    @Autowired
    private CampaignMapper campaignMapper;

    @Mock
    private CampaignService campaignServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Campaign campaign;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Campaign createEntity(EntityManager em) {
        Campaign campaign = new Campaign()
            .title(DEFAULT_TITLE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return campaign;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Campaign createUpdatedEntity(EntityManager em) {
        Campaign campaign = new Campaign()
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);
        return campaign;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_campaign__products").block();
            em.deleteAll(Campaign.class).block();
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
        campaign = createEntity(em);
    }

    @Test
    void createCampaign() throws Exception {
        int databaseSizeBeforeCreate = campaignRepository.findAll().collectList().block().size();
        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeCreate + 1);
        Campaign testCampaign = campaignList.get(campaignList.size() - 1);
        assertThat(testCampaign.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCampaign.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCampaign.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testCampaign.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testCampaign.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testCampaign.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createCampaignWithExistingId() throws Exception {
        // Create the Campaign with an existing ID
        campaign.setId(1L);
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        int databaseSizeBeforeCreate = campaignRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = campaignRepository.findAll().collectList().block().size();
        // set the field null
        campaign.setTitle(null);

        // Create the Campaign, which fails.
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = campaignRepository.findAll().collectList().block().size();
        // set the field null
        campaign.setStartDate(null);

        // Create the Campaign, which fails.
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = campaignRepository.findAll().collectList().block().size();
        // set the field null
        campaign.setEndDate(null);

        // Create the Campaign, which fails.
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCampaigns() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList
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
            .value(hasItem(campaign.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].startDate")
            .value(hasItem(DEFAULT_START_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
            .jsonPath("$.[*].photoContentType")
            .value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE))
            .jsonPath("$.[*].photo")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCampaignsWithEagerRelationshipsIsEnabled() {
        when(campaignServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(campaignServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCampaignsWithEagerRelationshipsIsNotEnabled() {
        when(campaignServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(campaignRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getCampaign() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get the campaign
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, campaign.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(campaign.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.startDate")
            .value(is(DEFAULT_START_DATE.toString()))
            .jsonPath("$.endDate")
            .value(is(DEFAULT_END_DATE.toString()))
            .jsonPath("$.photoContentType")
            .value(is(DEFAULT_PHOTO_CONTENT_TYPE))
            .jsonPath("$.photo")
            .value(is(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getCampaignsByIdFiltering() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        Long id = campaign.getId();

        defaultCampaignShouldBeFound("id.equals=" + id);
        defaultCampaignShouldNotBeFound("id.notEquals=" + id);

        defaultCampaignShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCampaignShouldNotBeFound("id.greaterThan=" + id);

        defaultCampaignShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCampaignShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllCampaignsByTitleIsEqualToSomething() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where title equals to DEFAULT_TITLE
        defaultCampaignShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the campaignList where title equals to UPDATED_TITLE
        defaultCampaignShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    void getAllCampaignsByTitleIsInShouldWork() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCampaignShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the campaignList where title equals to UPDATED_TITLE
        defaultCampaignShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    void getAllCampaignsByTitleIsNullOrNotNull() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where title is not null
        defaultCampaignShouldBeFound("title.specified=true");

        // Get all the campaignList where title is null
        defaultCampaignShouldNotBeFound("title.specified=false");
    }

    @Test
    void getAllCampaignsByTitleContainsSomething() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where title contains DEFAULT_TITLE
        defaultCampaignShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the campaignList where title contains UPDATED_TITLE
        defaultCampaignShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    void getAllCampaignsByTitleNotContainsSomething() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where title does not contain DEFAULT_TITLE
        defaultCampaignShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the campaignList where title does not contain UPDATED_TITLE
        defaultCampaignShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    void getAllCampaignsByStartDateIsEqualToSomething() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where startDate equals to DEFAULT_START_DATE
        defaultCampaignShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the campaignList where startDate equals to UPDATED_START_DATE
        defaultCampaignShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    void getAllCampaignsByStartDateIsInShouldWork() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultCampaignShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the campaignList where startDate equals to UPDATED_START_DATE
        defaultCampaignShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    void getAllCampaignsByStartDateIsNullOrNotNull() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where startDate is not null
        defaultCampaignShouldBeFound("startDate.specified=true");

        // Get all the campaignList where startDate is null
        defaultCampaignShouldNotBeFound("startDate.specified=false");
    }

    @Test
    void getAllCampaignsByStartDateIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultCampaignShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the campaignList where startDate is greater than or equal to UPDATED_START_DATE
        defaultCampaignShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    void getAllCampaignsByStartDateIsLessThanOrEqualToSomething() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where startDate is less than or equal to DEFAULT_START_DATE
        defaultCampaignShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the campaignList where startDate is less than or equal to SMALLER_START_DATE
        defaultCampaignShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    void getAllCampaignsByStartDateIsLessThanSomething() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where startDate is less than DEFAULT_START_DATE
        defaultCampaignShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the campaignList where startDate is less than UPDATED_START_DATE
        defaultCampaignShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    void getAllCampaignsByStartDateIsGreaterThanSomething() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where startDate is greater than DEFAULT_START_DATE
        defaultCampaignShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the campaignList where startDate is greater than SMALLER_START_DATE
        defaultCampaignShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    void getAllCampaignsByEndDateIsEqualToSomething() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where endDate equals to DEFAULT_END_DATE
        defaultCampaignShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the campaignList where endDate equals to UPDATED_END_DATE
        defaultCampaignShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    void getAllCampaignsByEndDateIsInShouldWork() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultCampaignShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the campaignList where endDate equals to UPDATED_END_DATE
        defaultCampaignShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    void getAllCampaignsByEndDateIsNullOrNotNull() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where endDate is not null
        defaultCampaignShouldBeFound("endDate.specified=true");

        // Get all the campaignList where endDate is null
        defaultCampaignShouldNotBeFound("endDate.specified=false");
    }

    @Test
    void getAllCampaignsByEndDateIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultCampaignShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the campaignList where endDate is greater than or equal to UPDATED_END_DATE
        defaultCampaignShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    void getAllCampaignsByEndDateIsLessThanOrEqualToSomething() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where endDate is less than or equal to DEFAULT_END_DATE
        defaultCampaignShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the campaignList where endDate is less than or equal to SMALLER_END_DATE
        defaultCampaignShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    void getAllCampaignsByEndDateIsLessThanSomething() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where endDate is less than DEFAULT_END_DATE
        defaultCampaignShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the campaignList where endDate is less than UPDATED_END_DATE
        defaultCampaignShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    void getAllCampaignsByEndDateIsGreaterThanSomething() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where endDate is greater than DEFAULT_END_DATE
        defaultCampaignShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the campaignList where endDate is greater than SMALLER_END_DATE
        defaultCampaignShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    void getAllCampaignsByDescriptionIsEqualToSomething() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where description equals to DEFAULT_DESCRIPTION
        defaultCampaignShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the campaignList where description equals to UPDATED_DESCRIPTION
        defaultCampaignShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllCampaignsByDescriptionIsInShouldWork() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCampaignShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the campaignList where description equals to UPDATED_DESCRIPTION
        defaultCampaignShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllCampaignsByDescriptionIsNullOrNotNull() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where description is not null
        defaultCampaignShouldBeFound("description.specified=true");

        // Get all the campaignList where description is null
        defaultCampaignShouldNotBeFound("description.specified=false");
    }

    @Test
    void getAllCampaignsByDescriptionContainsSomething() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where description contains DEFAULT_DESCRIPTION
        defaultCampaignShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the campaignList where description contains UPDATED_DESCRIPTION
        defaultCampaignShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllCampaignsByDescriptionNotContainsSomething() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        // Get all the campaignList where description does not contain DEFAULT_DESCRIPTION
        defaultCampaignShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the campaignList where description does not contain UPDATED_DESCRIPTION
        defaultCampaignShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCampaignShouldBeFound(String filter) {
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
            .value(hasItem(campaign.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].startDate")
            .value(hasItem(DEFAULT_START_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
            .jsonPath("$.[*].photoContentType")
            .value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE))
            .jsonPath("$.[*].photo")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO)))
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
    private void defaultCampaignShouldNotBeFound(String filter) {
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
    void getNonExistingCampaign() {
        // Get the campaign
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCampaign() throws Exception {
        // Initialize the database
        campaignRepository.save(campaign).block();

        int databaseSizeBeforeUpdate = campaignRepository.findAll().collectList().block().size();

        // Update the campaign
        Campaign updatedCampaign = campaignRepository.findById(campaign.getId()).block();
        updatedCampaign
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);
        CampaignDTO campaignDTO = campaignMapper.toDto(updatedCampaign);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, campaignDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
        Campaign testCampaign = campaignList.get(campaignList.size() - 1);
        assertThat(testCampaign.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCampaign.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCampaign.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testCampaign.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testCampaign.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testCampaign.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().collectList().block().size();
        campaign.setId(longCount.incrementAndGet());

        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, campaignDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().collectList().block().size();
        campaign.setId(longCount.incrementAndGet());

        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().collectList().block().size();
        campaign.setId(longCount.incrementAndGet());

        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCampaignWithPatch() throws Exception {
        // Initialize the database
        campaignRepository.save(campaign).block();

        int databaseSizeBeforeUpdate = campaignRepository.findAll().collectList().block().size();

        // Update the campaign using partial update
        Campaign partialUpdatedCampaign = new Campaign();
        partialUpdatedCampaign.setId(campaign.getId());

        partialUpdatedCampaign
            .title(UPDATED_TITLE)
            .endDate(UPDATED_END_DATE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCampaign.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCampaign))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
        Campaign testCampaign = campaignList.get(campaignList.size() - 1);
        assertThat(testCampaign.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCampaign.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCampaign.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testCampaign.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testCampaign.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testCampaign.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void fullUpdateCampaignWithPatch() throws Exception {
        // Initialize the database
        campaignRepository.save(campaign).block();

        int databaseSizeBeforeUpdate = campaignRepository.findAll().collectList().block().size();

        // Update the campaign using partial update
        Campaign partialUpdatedCampaign = new Campaign();
        partialUpdatedCampaign.setId(campaign.getId());

        partialUpdatedCampaign
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCampaign.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCampaign))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
        Campaign testCampaign = campaignList.get(campaignList.size() - 1);
        assertThat(testCampaign.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCampaign.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCampaign.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testCampaign.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testCampaign.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testCampaign.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().collectList().block().size();
        campaign.setId(longCount.incrementAndGet());

        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, campaignDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().collectList().block().size();
        campaign.setId(longCount.incrementAndGet());

        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().collectList().block().size();
        campaign.setId(longCount.incrementAndGet());

        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(campaignDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCampaign() {
        // Initialize the database
        campaignRepository.save(campaign).block();

        int databaseSizeBeforeDelete = campaignRepository.findAll().collectList().block().size();

        // Delete the campaign
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, campaign.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Campaign> campaignList = campaignRepository.findAll().collectList().block();
        assertThat(campaignList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
