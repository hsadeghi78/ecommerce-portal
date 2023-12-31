package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.Party;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.repository.PartyRepository;
import com.hs.ec.portal.service.dto.PartyDTO;
import com.hs.ec.portal.service.mapper.PartyMapper;
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
 * Integration tests for the {@link PartyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PartyResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PARTY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PARTY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TRADE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TRADE_TITLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ACTIVATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTIVATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ACTIVATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EXPIRATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ACTIVATION_STATUS = false;
    private static final Boolean UPDATED_ACTIVATION_STATUS = true;

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_PERSON_TYPE = false;
    private static final Boolean UPDATED_PERSON_TYPE = true;

    private static final String ENTITY_API_URL = "/api/parties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private PartyMapper partyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Party party;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Party createEntity(EntityManager em) {
        Party party = new Party()
            .title(DEFAULT_TITLE)
            .partyCode(DEFAULT_PARTY_CODE)
            .tradeTitle(DEFAULT_TRADE_TITLE)
            .activationDate(DEFAULT_ACTIVATION_DATE)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .activationStatus(DEFAULT_ACTIVATION_STATUS)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .personType(DEFAULT_PERSON_TYPE);
        return party;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Party createUpdatedEntity(EntityManager em) {
        Party party = new Party()
            .title(UPDATED_TITLE)
            .partyCode(UPDATED_PARTY_CODE)
            .tradeTitle(UPDATED_TRADE_TITLE)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .activationStatus(UPDATED_ACTIVATION_STATUS)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .personType(UPDATED_PERSON_TYPE);
        return party;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Party.class).block();
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
        party = createEntity(em);
    }

    @Test
    void createParty() throws Exception {
        int databaseSizeBeforeCreate = partyRepository.findAll().collectList().block().size();
        // Create the Party
        PartyDTO partyDTO = partyMapper.toDto(party);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(partyDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll().collectList().block();
        assertThat(partyList).hasSize(databaseSizeBeforeCreate + 1);
        Party testParty = partyList.get(partyList.size() - 1);
        assertThat(testParty.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testParty.getPartyCode()).isEqualTo(DEFAULT_PARTY_CODE);
        assertThat(testParty.getTradeTitle()).isEqualTo(DEFAULT_TRADE_TITLE);
        assertThat(testParty.getActivationDate()).isEqualTo(DEFAULT_ACTIVATION_DATE);
        assertThat(testParty.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testParty.getActivationStatus()).isEqualTo(DEFAULT_ACTIVATION_STATUS);
        assertThat(testParty.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testParty.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testParty.getPersonType()).isEqualTo(DEFAULT_PERSON_TYPE);
    }

    @Test
    void createPartyWithExistingId() throws Exception {
        // Create the Party with an existing ID
        party.setId(1L);
        PartyDTO partyDTO = partyMapper.toDto(party);

        int databaseSizeBeforeCreate = partyRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(partyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll().collectList().block();
        assertThat(partyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().collectList().block().size();
        // set the field null
        party.setTitle(null);

        // Create the Party, which fails.
        PartyDTO partyDTO = partyMapper.toDto(party);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(partyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Party> partyList = partyRepository.findAll().collectList().block();
        assertThat(partyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPartyCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().collectList().block().size();
        // set the field null
        party.setPartyCode(null);

        // Create the Party, which fails.
        PartyDTO partyDTO = partyMapper.toDto(party);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(partyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Party> partyList = partyRepository.findAll().collectList().block();
        assertThat(partyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTradeTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().collectList().block().size();
        // set the field null
        party.setTradeTitle(null);

        // Create the Party, which fails.
        PartyDTO partyDTO = partyMapper.toDto(party);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(partyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Party> partyList = partyRepository.findAll().collectList().block();
        assertThat(partyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkActivationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().collectList().block().size();
        // set the field null
        party.setActivationDate(null);

        // Create the Party, which fails.
        PartyDTO partyDTO = partyMapper.toDto(party);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(partyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Party> partyList = partyRepository.findAll().collectList().block();
        assertThat(partyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkActivationStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().collectList().block().size();
        // set the field null
        party.setActivationStatus(null);

        // Create the Party, which fails.
        PartyDTO partyDTO = partyMapper.toDto(party);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(partyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Party> partyList = partyRepository.findAll().collectList().block();
        assertThat(partyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPersonTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().collectList().block().size();
        // set the field null
        party.setPersonType(null);

        // Create the Party, which fails.
        PartyDTO partyDTO = partyMapper.toDto(party);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(partyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Party> partyList = partyRepository.findAll().collectList().block();
        assertThat(partyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllParties() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList
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
            .value(hasItem(party.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].partyCode")
            .value(hasItem(DEFAULT_PARTY_CODE))
            .jsonPath("$.[*].tradeTitle")
            .value(hasItem(DEFAULT_TRADE_TITLE))
            .jsonPath("$.[*].activationDate")
            .value(hasItem(DEFAULT_ACTIVATION_DATE.toString()))
            .jsonPath("$.[*].expirationDate")
            .value(hasItem(DEFAULT_EXPIRATION_DATE.toString()))
            .jsonPath("$.[*].activationStatus")
            .value(hasItem(DEFAULT_ACTIVATION_STATUS.booleanValue()))
            .jsonPath("$.[*].photoContentType")
            .value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE))
            .jsonPath("$.[*].photo")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .jsonPath("$.[*].personType")
            .value(hasItem(DEFAULT_PERSON_TYPE.booleanValue()));
    }

    @Test
    void getParty() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get the party
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, party.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(party.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.partyCode")
            .value(is(DEFAULT_PARTY_CODE))
            .jsonPath("$.tradeTitle")
            .value(is(DEFAULT_TRADE_TITLE))
            .jsonPath("$.activationDate")
            .value(is(DEFAULT_ACTIVATION_DATE.toString()))
            .jsonPath("$.expirationDate")
            .value(is(DEFAULT_EXPIRATION_DATE.toString()))
            .jsonPath("$.activationStatus")
            .value(is(DEFAULT_ACTIVATION_STATUS.booleanValue()))
            .jsonPath("$.photoContentType")
            .value(is(DEFAULT_PHOTO_CONTENT_TYPE))
            .jsonPath("$.photo")
            .value(is(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .jsonPath("$.personType")
            .value(is(DEFAULT_PERSON_TYPE.booleanValue()));
    }

    @Test
    void getPartiesByIdFiltering() {
        // Initialize the database
        partyRepository.save(party).block();

        Long id = party.getId();

        defaultPartyShouldBeFound("id.equals=" + id);
        defaultPartyShouldNotBeFound("id.notEquals=" + id);

        defaultPartyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPartyShouldNotBeFound("id.greaterThan=" + id);

        defaultPartyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPartyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllPartiesByTitleIsEqualToSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where title equals to DEFAULT_TITLE
        defaultPartyShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the partyList where title equals to UPDATED_TITLE
        defaultPartyShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    void getAllPartiesByTitleIsInShouldWork() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultPartyShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the partyList where title equals to UPDATED_TITLE
        defaultPartyShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    void getAllPartiesByTitleIsNullOrNotNull() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where title is not null
        defaultPartyShouldBeFound("title.specified=true");

        // Get all the partyList where title is null
        defaultPartyShouldNotBeFound("title.specified=false");
    }

    @Test
    void getAllPartiesByTitleContainsSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where title contains DEFAULT_TITLE
        defaultPartyShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the partyList where title contains UPDATED_TITLE
        defaultPartyShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    void getAllPartiesByTitleNotContainsSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where title does not contain DEFAULT_TITLE
        defaultPartyShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the partyList where title does not contain UPDATED_TITLE
        defaultPartyShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    void getAllPartiesByPartyCodeIsEqualToSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where partyCode equals to DEFAULT_PARTY_CODE
        defaultPartyShouldBeFound("partyCode.equals=" + DEFAULT_PARTY_CODE);

        // Get all the partyList where partyCode equals to UPDATED_PARTY_CODE
        defaultPartyShouldNotBeFound("partyCode.equals=" + UPDATED_PARTY_CODE);
    }

    @Test
    void getAllPartiesByPartyCodeIsInShouldWork() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where partyCode in DEFAULT_PARTY_CODE or UPDATED_PARTY_CODE
        defaultPartyShouldBeFound("partyCode.in=" + DEFAULT_PARTY_CODE + "," + UPDATED_PARTY_CODE);

        // Get all the partyList where partyCode equals to UPDATED_PARTY_CODE
        defaultPartyShouldNotBeFound("partyCode.in=" + UPDATED_PARTY_CODE);
    }

    @Test
    void getAllPartiesByPartyCodeIsNullOrNotNull() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where partyCode is not null
        defaultPartyShouldBeFound("partyCode.specified=true");

        // Get all the partyList where partyCode is null
        defaultPartyShouldNotBeFound("partyCode.specified=false");
    }

    @Test
    void getAllPartiesByPartyCodeContainsSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where partyCode contains DEFAULT_PARTY_CODE
        defaultPartyShouldBeFound("partyCode.contains=" + DEFAULT_PARTY_CODE);

        // Get all the partyList where partyCode contains UPDATED_PARTY_CODE
        defaultPartyShouldNotBeFound("partyCode.contains=" + UPDATED_PARTY_CODE);
    }

    @Test
    void getAllPartiesByPartyCodeNotContainsSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where partyCode does not contain DEFAULT_PARTY_CODE
        defaultPartyShouldNotBeFound("partyCode.doesNotContain=" + DEFAULT_PARTY_CODE);

        // Get all the partyList where partyCode does not contain UPDATED_PARTY_CODE
        defaultPartyShouldBeFound("partyCode.doesNotContain=" + UPDATED_PARTY_CODE);
    }

    @Test
    void getAllPartiesByTradeTitleIsEqualToSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where tradeTitle equals to DEFAULT_TRADE_TITLE
        defaultPartyShouldBeFound("tradeTitle.equals=" + DEFAULT_TRADE_TITLE);

        // Get all the partyList where tradeTitle equals to UPDATED_TRADE_TITLE
        defaultPartyShouldNotBeFound("tradeTitle.equals=" + UPDATED_TRADE_TITLE);
    }

    @Test
    void getAllPartiesByTradeTitleIsInShouldWork() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where tradeTitle in DEFAULT_TRADE_TITLE or UPDATED_TRADE_TITLE
        defaultPartyShouldBeFound("tradeTitle.in=" + DEFAULT_TRADE_TITLE + "," + UPDATED_TRADE_TITLE);

        // Get all the partyList where tradeTitle equals to UPDATED_TRADE_TITLE
        defaultPartyShouldNotBeFound("tradeTitle.in=" + UPDATED_TRADE_TITLE);
    }

    @Test
    void getAllPartiesByTradeTitleIsNullOrNotNull() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where tradeTitle is not null
        defaultPartyShouldBeFound("tradeTitle.specified=true");

        // Get all the partyList where tradeTitle is null
        defaultPartyShouldNotBeFound("tradeTitle.specified=false");
    }

    @Test
    void getAllPartiesByTradeTitleContainsSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where tradeTitle contains DEFAULT_TRADE_TITLE
        defaultPartyShouldBeFound("tradeTitle.contains=" + DEFAULT_TRADE_TITLE);

        // Get all the partyList where tradeTitle contains UPDATED_TRADE_TITLE
        defaultPartyShouldNotBeFound("tradeTitle.contains=" + UPDATED_TRADE_TITLE);
    }

    @Test
    void getAllPartiesByTradeTitleNotContainsSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where tradeTitle does not contain DEFAULT_TRADE_TITLE
        defaultPartyShouldNotBeFound("tradeTitle.doesNotContain=" + DEFAULT_TRADE_TITLE);

        // Get all the partyList where tradeTitle does not contain UPDATED_TRADE_TITLE
        defaultPartyShouldBeFound("tradeTitle.doesNotContain=" + UPDATED_TRADE_TITLE);
    }

    @Test
    void getAllPartiesByActivationDateIsEqualToSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where activationDate equals to DEFAULT_ACTIVATION_DATE
        defaultPartyShouldBeFound("activationDate.equals=" + DEFAULT_ACTIVATION_DATE);

        // Get all the partyList where activationDate equals to UPDATED_ACTIVATION_DATE
        defaultPartyShouldNotBeFound("activationDate.equals=" + UPDATED_ACTIVATION_DATE);
    }

    @Test
    void getAllPartiesByActivationDateIsInShouldWork() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where activationDate in DEFAULT_ACTIVATION_DATE or UPDATED_ACTIVATION_DATE
        defaultPartyShouldBeFound("activationDate.in=" + DEFAULT_ACTIVATION_DATE + "," + UPDATED_ACTIVATION_DATE);

        // Get all the partyList where activationDate equals to UPDATED_ACTIVATION_DATE
        defaultPartyShouldNotBeFound("activationDate.in=" + UPDATED_ACTIVATION_DATE);
    }

    @Test
    void getAllPartiesByActivationDateIsNullOrNotNull() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where activationDate is not null
        defaultPartyShouldBeFound("activationDate.specified=true");

        // Get all the partyList where activationDate is null
        defaultPartyShouldNotBeFound("activationDate.specified=false");
    }

    @Test
    void getAllPartiesByActivationDateIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where activationDate is greater than or equal to DEFAULT_ACTIVATION_DATE
        defaultPartyShouldBeFound("activationDate.greaterThanOrEqual=" + DEFAULT_ACTIVATION_DATE);

        // Get all the partyList where activationDate is greater than or equal to UPDATED_ACTIVATION_DATE
        defaultPartyShouldNotBeFound("activationDate.greaterThanOrEqual=" + UPDATED_ACTIVATION_DATE);
    }

    @Test
    void getAllPartiesByActivationDateIsLessThanOrEqualToSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where activationDate is less than or equal to DEFAULT_ACTIVATION_DATE
        defaultPartyShouldBeFound("activationDate.lessThanOrEqual=" + DEFAULT_ACTIVATION_DATE);

        // Get all the partyList where activationDate is less than or equal to SMALLER_ACTIVATION_DATE
        defaultPartyShouldNotBeFound("activationDate.lessThanOrEqual=" + SMALLER_ACTIVATION_DATE);
    }

    @Test
    void getAllPartiesByActivationDateIsLessThanSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where activationDate is less than DEFAULT_ACTIVATION_DATE
        defaultPartyShouldNotBeFound("activationDate.lessThan=" + DEFAULT_ACTIVATION_DATE);

        // Get all the partyList where activationDate is less than UPDATED_ACTIVATION_DATE
        defaultPartyShouldBeFound("activationDate.lessThan=" + UPDATED_ACTIVATION_DATE);
    }

    @Test
    void getAllPartiesByActivationDateIsGreaterThanSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where activationDate is greater than DEFAULT_ACTIVATION_DATE
        defaultPartyShouldNotBeFound("activationDate.greaterThan=" + DEFAULT_ACTIVATION_DATE);

        // Get all the partyList where activationDate is greater than SMALLER_ACTIVATION_DATE
        defaultPartyShouldBeFound("activationDate.greaterThan=" + SMALLER_ACTIVATION_DATE);
    }

    @Test
    void getAllPartiesByExpirationDateIsEqualToSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where expirationDate equals to DEFAULT_EXPIRATION_DATE
        defaultPartyShouldBeFound("expirationDate.equals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the partyList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultPartyShouldNotBeFound("expirationDate.equals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    void getAllPartiesByExpirationDateIsInShouldWork() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where expirationDate in DEFAULT_EXPIRATION_DATE or UPDATED_EXPIRATION_DATE
        defaultPartyShouldBeFound("expirationDate.in=" + DEFAULT_EXPIRATION_DATE + "," + UPDATED_EXPIRATION_DATE);

        // Get all the partyList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultPartyShouldNotBeFound("expirationDate.in=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    void getAllPartiesByExpirationDateIsNullOrNotNull() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where expirationDate is not null
        defaultPartyShouldBeFound("expirationDate.specified=true");

        // Get all the partyList where expirationDate is null
        defaultPartyShouldNotBeFound("expirationDate.specified=false");
    }

    @Test
    void getAllPartiesByExpirationDateIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where expirationDate is greater than or equal to DEFAULT_EXPIRATION_DATE
        defaultPartyShouldBeFound("expirationDate.greaterThanOrEqual=" + DEFAULT_EXPIRATION_DATE);

        // Get all the partyList where expirationDate is greater than or equal to UPDATED_EXPIRATION_DATE
        defaultPartyShouldNotBeFound("expirationDate.greaterThanOrEqual=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    void getAllPartiesByExpirationDateIsLessThanOrEqualToSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where expirationDate is less than or equal to DEFAULT_EXPIRATION_DATE
        defaultPartyShouldBeFound("expirationDate.lessThanOrEqual=" + DEFAULT_EXPIRATION_DATE);

        // Get all the partyList where expirationDate is less than or equal to SMALLER_EXPIRATION_DATE
        defaultPartyShouldNotBeFound("expirationDate.lessThanOrEqual=" + SMALLER_EXPIRATION_DATE);
    }

    @Test
    void getAllPartiesByExpirationDateIsLessThanSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where expirationDate is less than DEFAULT_EXPIRATION_DATE
        defaultPartyShouldNotBeFound("expirationDate.lessThan=" + DEFAULT_EXPIRATION_DATE);

        // Get all the partyList where expirationDate is less than UPDATED_EXPIRATION_DATE
        defaultPartyShouldBeFound("expirationDate.lessThan=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    void getAllPartiesByExpirationDateIsGreaterThanSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where expirationDate is greater than DEFAULT_EXPIRATION_DATE
        defaultPartyShouldNotBeFound("expirationDate.greaterThan=" + DEFAULT_EXPIRATION_DATE);

        // Get all the partyList where expirationDate is greater than SMALLER_EXPIRATION_DATE
        defaultPartyShouldBeFound("expirationDate.greaterThan=" + SMALLER_EXPIRATION_DATE);
    }

    @Test
    void getAllPartiesByActivationStatusIsEqualToSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where activationStatus equals to DEFAULT_ACTIVATION_STATUS
        defaultPartyShouldBeFound("activationStatus.equals=" + DEFAULT_ACTIVATION_STATUS);

        // Get all the partyList where activationStatus equals to UPDATED_ACTIVATION_STATUS
        defaultPartyShouldNotBeFound("activationStatus.equals=" + UPDATED_ACTIVATION_STATUS);
    }

    @Test
    void getAllPartiesByActivationStatusIsInShouldWork() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where activationStatus in DEFAULT_ACTIVATION_STATUS or UPDATED_ACTIVATION_STATUS
        defaultPartyShouldBeFound("activationStatus.in=" + DEFAULT_ACTIVATION_STATUS + "," + UPDATED_ACTIVATION_STATUS);

        // Get all the partyList where activationStatus equals to UPDATED_ACTIVATION_STATUS
        defaultPartyShouldNotBeFound("activationStatus.in=" + UPDATED_ACTIVATION_STATUS);
    }

    @Test
    void getAllPartiesByActivationStatusIsNullOrNotNull() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where activationStatus is not null
        defaultPartyShouldBeFound("activationStatus.specified=true");

        // Get all the partyList where activationStatus is null
        defaultPartyShouldNotBeFound("activationStatus.specified=false");
    }

    @Test
    void getAllPartiesByPersonTypeIsEqualToSomething() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where personType equals to DEFAULT_PERSON_TYPE
        defaultPartyShouldBeFound("personType.equals=" + DEFAULT_PERSON_TYPE);

        // Get all the partyList where personType equals to UPDATED_PERSON_TYPE
        defaultPartyShouldNotBeFound("personType.equals=" + UPDATED_PERSON_TYPE);
    }

    @Test
    void getAllPartiesByPersonTypeIsInShouldWork() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where personType in DEFAULT_PERSON_TYPE or UPDATED_PERSON_TYPE
        defaultPartyShouldBeFound("personType.in=" + DEFAULT_PERSON_TYPE + "," + UPDATED_PERSON_TYPE);

        // Get all the partyList where personType equals to UPDATED_PERSON_TYPE
        defaultPartyShouldNotBeFound("personType.in=" + UPDATED_PERSON_TYPE);
    }

    @Test
    void getAllPartiesByPersonTypeIsNullOrNotNull() {
        // Initialize the database
        partyRepository.save(party).block();

        // Get all the partyList where personType is not null
        defaultPartyShouldBeFound("personType.specified=true");

        // Get all the partyList where personType is null
        defaultPartyShouldNotBeFound("personType.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartyShouldBeFound(String filter) {
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
            .value(hasItem(party.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].partyCode")
            .value(hasItem(DEFAULT_PARTY_CODE))
            .jsonPath("$.[*].tradeTitle")
            .value(hasItem(DEFAULT_TRADE_TITLE))
            .jsonPath("$.[*].activationDate")
            .value(hasItem(DEFAULT_ACTIVATION_DATE.toString()))
            .jsonPath("$.[*].expirationDate")
            .value(hasItem(DEFAULT_EXPIRATION_DATE.toString()))
            .jsonPath("$.[*].activationStatus")
            .value(hasItem(DEFAULT_ACTIVATION_STATUS.booleanValue()))
            .jsonPath("$.[*].photoContentType")
            .value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE))
            .jsonPath("$.[*].photo")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .jsonPath("$.[*].personType")
            .value(hasItem(DEFAULT_PERSON_TYPE.booleanValue()));

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
    private void defaultPartyShouldNotBeFound(String filter) {
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
    void getNonExistingParty() {
        // Get the party
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingParty() throws Exception {
        // Initialize the database
        partyRepository.save(party).block();

        int databaseSizeBeforeUpdate = partyRepository.findAll().collectList().block().size();

        // Update the party
        Party updatedParty = partyRepository.findById(party.getId()).block();
        updatedParty
            .title(UPDATED_TITLE)
            .partyCode(UPDATED_PARTY_CODE)
            .tradeTitle(UPDATED_TRADE_TITLE)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .activationStatus(UPDATED_ACTIVATION_STATUS)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .personType(UPDATED_PERSON_TYPE);
        PartyDTO partyDTO = partyMapper.toDto(updatedParty);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, partyDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(partyDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll().collectList().block();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
        Party testParty = partyList.get(partyList.size() - 1);
        assertThat(testParty.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testParty.getPartyCode()).isEqualTo(UPDATED_PARTY_CODE);
        assertThat(testParty.getTradeTitle()).isEqualTo(UPDATED_TRADE_TITLE);
        assertThat(testParty.getActivationDate()).isEqualTo(UPDATED_ACTIVATION_DATE);
        assertThat(testParty.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testParty.getActivationStatus()).isEqualTo(UPDATED_ACTIVATION_STATUS);
        assertThat(testParty.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testParty.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testParty.getPersonType()).isEqualTo(UPDATED_PERSON_TYPE);
    }

    @Test
    void putNonExistingParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().collectList().block().size();
        party.setId(longCount.incrementAndGet());

        // Create the Party
        PartyDTO partyDTO = partyMapper.toDto(party);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, partyDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(partyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll().collectList().block();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().collectList().block().size();
        party.setId(longCount.incrementAndGet());

        // Create the Party
        PartyDTO partyDTO = partyMapper.toDto(party);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(partyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll().collectList().block();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().collectList().block().size();
        party.setId(longCount.incrementAndGet());

        // Create the Party
        PartyDTO partyDTO = partyMapper.toDto(party);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(partyDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll().collectList().block();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePartyWithPatch() throws Exception {
        // Initialize the database
        partyRepository.save(party).block();

        int databaseSizeBeforeUpdate = partyRepository.findAll().collectList().block().size();

        // Update the party using partial update
        Party partialUpdatedParty = new Party();
        partialUpdatedParty.setId(party.getId());

        partialUpdatedParty
            .title(UPDATED_TITLE)
            .tradeTitle(UPDATED_TRADE_TITLE)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .personType(UPDATED_PERSON_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedParty.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedParty))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll().collectList().block();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
        Party testParty = partyList.get(partyList.size() - 1);
        assertThat(testParty.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testParty.getPartyCode()).isEqualTo(DEFAULT_PARTY_CODE);
        assertThat(testParty.getTradeTitle()).isEqualTo(UPDATED_TRADE_TITLE);
        assertThat(testParty.getActivationDate()).isEqualTo(UPDATED_ACTIVATION_DATE);
        assertThat(testParty.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testParty.getActivationStatus()).isEqualTo(DEFAULT_ACTIVATION_STATUS);
        assertThat(testParty.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testParty.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testParty.getPersonType()).isEqualTo(UPDATED_PERSON_TYPE);
    }

    @Test
    void fullUpdatePartyWithPatch() throws Exception {
        // Initialize the database
        partyRepository.save(party).block();

        int databaseSizeBeforeUpdate = partyRepository.findAll().collectList().block().size();

        // Update the party using partial update
        Party partialUpdatedParty = new Party();
        partialUpdatedParty.setId(party.getId());

        partialUpdatedParty
            .title(UPDATED_TITLE)
            .partyCode(UPDATED_PARTY_CODE)
            .tradeTitle(UPDATED_TRADE_TITLE)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .activationStatus(UPDATED_ACTIVATION_STATUS)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .personType(UPDATED_PERSON_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedParty.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedParty))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll().collectList().block();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
        Party testParty = partyList.get(partyList.size() - 1);
        assertThat(testParty.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testParty.getPartyCode()).isEqualTo(UPDATED_PARTY_CODE);
        assertThat(testParty.getTradeTitle()).isEqualTo(UPDATED_TRADE_TITLE);
        assertThat(testParty.getActivationDate()).isEqualTo(UPDATED_ACTIVATION_DATE);
        assertThat(testParty.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testParty.getActivationStatus()).isEqualTo(UPDATED_ACTIVATION_STATUS);
        assertThat(testParty.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testParty.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testParty.getPersonType()).isEqualTo(UPDATED_PERSON_TYPE);
    }

    @Test
    void patchNonExistingParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().collectList().block().size();
        party.setId(longCount.incrementAndGet());

        // Create the Party
        PartyDTO partyDTO = partyMapper.toDto(party);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partyDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll().collectList().block();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().collectList().block().size();
        party.setId(longCount.incrementAndGet());

        // Create the Party
        PartyDTO partyDTO = partyMapper.toDto(party);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll().collectList().block();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().collectList().block().size();
        party.setId(longCount.incrementAndGet());

        // Create the Party
        PartyDTO partyDTO = partyMapper.toDto(party);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partyDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll().collectList().block();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteParty() {
        // Initialize the database
        partyRepository.save(party).block();

        int databaseSizeBeforeDelete = partyRepository.findAll().collectList().block().size();

        // Delete the party
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, party.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Party> partyList = partyRepository.findAll().collectList().block();
        assertThat(partyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
