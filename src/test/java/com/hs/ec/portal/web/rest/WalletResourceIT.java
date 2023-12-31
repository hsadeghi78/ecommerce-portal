package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.Wallet;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.repository.WalletRepository;
import com.hs.ec.portal.service.dto.WalletDTO;
import com.hs.ec.portal.service.mapper.WalletMapper;
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
 * Integration tests for the {@link WalletResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class WalletResourceIT {

    private static final Long DEFAULT_TRANS_TYPE_CLASS_ID = 1L;
    private static final Long UPDATED_TRANS_TYPE_CLASS_ID = 2L;
    private static final Long SMALLER_TRANS_TYPE_CLASS_ID = 1L - 1L;

    private static final Double DEFAULT_STOCK = 1D;
    private static final Double UPDATED_STOCK = 2D;
    private static final Double SMALLER_STOCK = 1D - 1D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_DEPOSIT = 1D;
    private static final Double UPDATED_DEPOSIT = 2D;
    private static final Double SMALLER_DEPOSIT = 1D - 1D;

    private static final Double DEFAULT_WITHDRAWAL = 1D;
    private static final Double UPDATED_WITHDRAWAL = 2D;
    private static final Double SMALLER_WITHDRAWAL = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/wallets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Wallet wallet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wallet createEntity(EntityManager em) {
        Wallet wallet = new Wallet()
            .transTypeClassId(DEFAULT_TRANS_TYPE_CLASS_ID)
            .stock(DEFAULT_STOCK)
            .description(DEFAULT_DESCRIPTION)
            .deposit(DEFAULT_DEPOSIT)
            .withdrawal(DEFAULT_WITHDRAWAL);
        return wallet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wallet createUpdatedEntity(EntityManager em) {
        Wallet wallet = new Wallet()
            .transTypeClassId(UPDATED_TRANS_TYPE_CLASS_ID)
            .stock(UPDATED_STOCK)
            .description(UPDATED_DESCRIPTION)
            .deposit(UPDATED_DEPOSIT)
            .withdrawal(UPDATED_WITHDRAWAL);
        return wallet;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Wallet.class).block();
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
        wallet = createEntity(em);
    }

    @Test
    void createWallet() throws Exception {
        int databaseSizeBeforeCreate = walletRepository.findAll().collectList().block().size();
        // Create the Wallet
        WalletDTO walletDTO = walletMapper.toDto(wallet);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll().collectList().block();
        assertThat(walletList).hasSize(databaseSizeBeforeCreate + 1);
        Wallet testWallet = walletList.get(walletList.size() - 1);
        assertThat(testWallet.getTransTypeClassId()).isEqualTo(DEFAULT_TRANS_TYPE_CLASS_ID);
        assertThat(testWallet.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testWallet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWallet.getDeposit()).isEqualTo(DEFAULT_DEPOSIT);
        assertThat(testWallet.getWithdrawal()).isEqualTo(DEFAULT_WITHDRAWAL);
    }

    @Test
    void createWalletWithExistingId() throws Exception {
        // Create the Wallet with an existing ID
        wallet.setId(1L);
        WalletDTO walletDTO = walletMapper.toDto(wallet);

        int databaseSizeBeforeCreate = walletRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll().collectList().block();
        assertThat(walletList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkStockIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletRepository.findAll().collectList().block().size();
        // set the field null
        wallet.setStock(null);

        // Create the Wallet, which fails.
        WalletDTO walletDTO = walletMapper.toDto(wallet);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Wallet> walletList = walletRepository.findAll().collectList().block();
        assertThat(walletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllWallets() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList
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
            .value(hasItem(wallet.getId().intValue()))
            .jsonPath("$.[*].transTypeClassId")
            .value(hasItem(DEFAULT_TRANS_TYPE_CLASS_ID.intValue()))
            .jsonPath("$.[*].stock")
            .value(hasItem(DEFAULT_STOCK.doubleValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].deposit")
            .value(hasItem(DEFAULT_DEPOSIT.doubleValue()))
            .jsonPath("$.[*].withdrawal")
            .value(hasItem(DEFAULT_WITHDRAWAL.doubleValue()));
    }

    @Test
    void getWallet() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get the wallet
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, wallet.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(wallet.getId().intValue()))
            .jsonPath("$.transTypeClassId")
            .value(is(DEFAULT_TRANS_TYPE_CLASS_ID.intValue()))
            .jsonPath("$.stock")
            .value(is(DEFAULT_STOCK.doubleValue()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.deposit")
            .value(is(DEFAULT_DEPOSIT.doubleValue()))
            .jsonPath("$.withdrawal")
            .value(is(DEFAULT_WITHDRAWAL.doubleValue()));
    }

    @Test
    void getWalletsByIdFiltering() {
        // Initialize the database
        walletRepository.save(wallet).block();

        Long id = wallet.getId();

        defaultWalletShouldBeFound("id.equals=" + id);
        defaultWalletShouldNotBeFound("id.notEquals=" + id);

        defaultWalletShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWalletShouldNotBeFound("id.greaterThan=" + id);

        defaultWalletShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWalletShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllWalletsByTransTypeClassIdIsEqualToSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where transTypeClassId equals to DEFAULT_TRANS_TYPE_CLASS_ID
        defaultWalletShouldBeFound("transTypeClassId.equals=" + DEFAULT_TRANS_TYPE_CLASS_ID);

        // Get all the walletList where transTypeClassId equals to UPDATED_TRANS_TYPE_CLASS_ID
        defaultWalletShouldNotBeFound("transTypeClassId.equals=" + UPDATED_TRANS_TYPE_CLASS_ID);
    }

    @Test
    void getAllWalletsByTransTypeClassIdIsInShouldWork() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where transTypeClassId in DEFAULT_TRANS_TYPE_CLASS_ID or UPDATED_TRANS_TYPE_CLASS_ID
        defaultWalletShouldBeFound("transTypeClassId.in=" + DEFAULT_TRANS_TYPE_CLASS_ID + "," + UPDATED_TRANS_TYPE_CLASS_ID);

        // Get all the walletList where transTypeClassId equals to UPDATED_TRANS_TYPE_CLASS_ID
        defaultWalletShouldNotBeFound("transTypeClassId.in=" + UPDATED_TRANS_TYPE_CLASS_ID);
    }

    @Test
    void getAllWalletsByTransTypeClassIdIsNullOrNotNull() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where transTypeClassId is not null
        defaultWalletShouldBeFound("transTypeClassId.specified=true");

        // Get all the walletList where transTypeClassId is null
        defaultWalletShouldNotBeFound("transTypeClassId.specified=false");
    }

    @Test
    void getAllWalletsByTransTypeClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where transTypeClassId is greater than or equal to DEFAULT_TRANS_TYPE_CLASS_ID
        defaultWalletShouldBeFound("transTypeClassId.greaterThanOrEqual=" + DEFAULT_TRANS_TYPE_CLASS_ID);

        // Get all the walletList where transTypeClassId is greater than or equal to UPDATED_TRANS_TYPE_CLASS_ID
        defaultWalletShouldNotBeFound("transTypeClassId.greaterThanOrEqual=" + UPDATED_TRANS_TYPE_CLASS_ID);
    }

    @Test
    void getAllWalletsByTransTypeClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where transTypeClassId is less than or equal to DEFAULT_TRANS_TYPE_CLASS_ID
        defaultWalletShouldBeFound("transTypeClassId.lessThanOrEqual=" + DEFAULT_TRANS_TYPE_CLASS_ID);

        // Get all the walletList where transTypeClassId is less than or equal to SMALLER_TRANS_TYPE_CLASS_ID
        defaultWalletShouldNotBeFound("transTypeClassId.lessThanOrEqual=" + SMALLER_TRANS_TYPE_CLASS_ID);
    }

    @Test
    void getAllWalletsByTransTypeClassIdIsLessThanSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where transTypeClassId is less than DEFAULT_TRANS_TYPE_CLASS_ID
        defaultWalletShouldNotBeFound("transTypeClassId.lessThan=" + DEFAULT_TRANS_TYPE_CLASS_ID);

        // Get all the walletList where transTypeClassId is less than UPDATED_TRANS_TYPE_CLASS_ID
        defaultWalletShouldBeFound("transTypeClassId.lessThan=" + UPDATED_TRANS_TYPE_CLASS_ID);
    }

    @Test
    void getAllWalletsByTransTypeClassIdIsGreaterThanSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where transTypeClassId is greater than DEFAULT_TRANS_TYPE_CLASS_ID
        defaultWalletShouldNotBeFound("transTypeClassId.greaterThan=" + DEFAULT_TRANS_TYPE_CLASS_ID);

        // Get all the walletList where transTypeClassId is greater than SMALLER_TRANS_TYPE_CLASS_ID
        defaultWalletShouldBeFound("transTypeClassId.greaterThan=" + SMALLER_TRANS_TYPE_CLASS_ID);
    }

    @Test
    void getAllWalletsByStockIsEqualToSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where stock equals to DEFAULT_STOCK
        defaultWalletShouldBeFound("stock.equals=" + DEFAULT_STOCK);

        // Get all the walletList where stock equals to UPDATED_STOCK
        defaultWalletShouldNotBeFound("stock.equals=" + UPDATED_STOCK);
    }

    @Test
    void getAllWalletsByStockIsInShouldWork() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where stock in DEFAULT_STOCK or UPDATED_STOCK
        defaultWalletShouldBeFound("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK);

        // Get all the walletList where stock equals to UPDATED_STOCK
        defaultWalletShouldNotBeFound("stock.in=" + UPDATED_STOCK);
    }

    @Test
    void getAllWalletsByStockIsNullOrNotNull() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where stock is not null
        defaultWalletShouldBeFound("stock.specified=true");

        // Get all the walletList where stock is null
        defaultWalletShouldNotBeFound("stock.specified=false");
    }

    @Test
    void getAllWalletsByStockIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where stock is greater than or equal to DEFAULT_STOCK
        defaultWalletShouldBeFound("stock.greaterThanOrEqual=" + DEFAULT_STOCK);

        // Get all the walletList where stock is greater than or equal to UPDATED_STOCK
        defaultWalletShouldNotBeFound("stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    void getAllWalletsByStockIsLessThanOrEqualToSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where stock is less than or equal to DEFAULT_STOCK
        defaultWalletShouldBeFound("stock.lessThanOrEqual=" + DEFAULT_STOCK);

        // Get all the walletList where stock is less than or equal to SMALLER_STOCK
        defaultWalletShouldNotBeFound("stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    void getAllWalletsByStockIsLessThanSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where stock is less than DEFAULT_STOCK
        defaultWalletShouldNotBeFound("stock.lessThan=" + DEFAULT_STOCK);

        // Get all the walletList where stock is less than UPDATED_STOCK
        defaultWalletShouldBeFound("stock.lessThan=" + UPDATED_STOCK);
    }

    @Test
    void getAllWalletsByStockIsGreaterThanSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where stock is greater than DEFAULT_STOCK
        defaultWalletShouldNotBeFound("stock.greaterThan=" + DEFAULT_STOCK);

        // Get all the walletList where stock is greater than SMALLER_STOCK
        defaultWalletShouldBeFound("stock.greaterThan=" + SMALLER_STOCK);
    }

    @Test
    void getAllWalletsByDescriptionIsEqualToSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where description equals to DEFAULT_DESCRIPTION
        defaultWalletShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the walletList where description equals to UPDATED_DESCRIPTION
        defaultWalletShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllWalletsByDescriptionIsInShouldWork() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultWalletShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the walletList where description equals to UPDATED_DESCRIPTION
        defaultWalletShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllWalletsByDescriptionIsNullOrNotNull() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where description is not null
        defaultWalletShouldBeFound("description.specified=true");

        // Get all the walletList where description is null
        defaultWalletShouldNotBeFound("description.specified=false");
    }

    @Test
    void getAllWalletsByDescriptionContainsSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where description contains DEFAULT_DESCRIPTION
        defaultWalletShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the walletList where description contains UPDATED_DESCRIPTION
        defaultWalletShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllWalletsByDescriptionNotContainsSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where description does not contain DEFAULT_DESCRIPTION
        defaultWalletShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the walletList where description does not contain UPDATED_DESCRIPTION
        defaultWalletShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllWalletsByDepositIsEqualToSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where deposit equals to DEFAULT_DEPOSIT
        defaultWalletShouldBeFound("deposit.equals=" + DEFAULT_DEPOSIT);

        // Get all the walletList where deposit equals to UPDATED_DEPOSIT
        defaultWalletShouldNotBeFound("deposit.equals=" + UPDATED_DEPOSIT);
    }

    @Test
    void getAllWalletsByDepositIsInShouldWork() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where deposit in DEFAULT_DEPOSIT or UPDATED_DEPOSIT
        defaultWalletShouldBeFound("deposit.in=" + DEFAULT_DEPOSIT + "," + UPDATED_DEPOSIT);

        // Get all the walletList where deposit equals to UPDATED_DEPOSIT
        defaultWalletShouldNotBeFound("deposit.in=" + UPDATED_DEPOSIT);
    }

    @Test
    void getAllWalletsByDepositIsNullOrNotNull() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where deposit is not null
        defaultWalletShouldBeFound("deposit.specified=true");

        // Get all the walletList where deposit is null
        defaultWalletShouldNotBeFound("deposit.specified=false");
    }

    @Test
    void getAllWalletsByDepositIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where deposit is greater than or equal to DEFAULT_DEPOSIT
        defaultWalletShouldBeFound("deposit.greaterThanOrEqual=" + DEFAULT_DEPOSIT);

        // Get all the walletList where deposit is greater than or equal to UPDATED_DEPOSIT
        defaultWalletShouldNotBeFound("deposit.greaterThanOrEqual=" + UPDATED_DEPOSIT);
    }

    @Test
    void getAllWalletsByDepositIsLessThanOrEqualToSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where deposit is less than or equal to DEFAULT_DEPOSIT
        defaultWalletShouldBeFound("deposit.lessThanOrEqual=" + DEFAULT_DEPOSIT);

        // Get all the walletList where deposit is less than or equal to SMALLER_DEPOSIT
        defaultWalletShouldNotBeFound("deposit.lessThanOrEqual=" + SMALLER_DEPOSIT);
    }

    @Test
    void getAllWalletsByDepositIsLessThanSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where deposit is less than DEFAULT_DEPOSIT
        defaultWalletShouldNotBeFound("deposit.lessThan=" + DEFAULT_DEPOSIT);

        // Get all the walletList where deposit is less than UPDATED_DEPOSIT
        defaultWalletShouldBeFound("deposit.lessThan=" + UPDATED_DEPOSIT);
    }

    @Test
    void getAllWalletsByDepositIsGreaterThanSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where deposit is greater than DEFAULT_DEPOSIT
        defaultWalletShouldNotBeFound("deposit.greaterThan=" + DEFAULT_DEPOSIT);

        // Get all the walletList where deposit is greater than SMALLER_DEPOSIT
        defaultWalletShouldBeFound("deposit.greaterThan=" + SMALLER_DEPOSIT);
    }

    @Test
    void getAllWalletsByWithdrawalIsEqualToSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where withdrawal equals to DEFAULT_WITHDRAWAL
        defaultWalletShouldBeFound("withdrawal.equals=" + DEFAULT_WITHDRAWAL);

        // Get all the walletList where withdrawal equals to UPDATED_WITHDRAWAL
        defaultWalletShouldNotBeFound("withdrawal.equals=" + UPDATED_WITHDRAWAL);
    }

    @Test
    void getAllWalletsByWithdrawalIsInShouldWork() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where withdrawal in DEFAULT_WITHDRAWAL or UPDATED_WITHDRAWAL
        defaultWalletShouldBeFound("withdrawal.in=" + DEFAULT_WITHDRAWAL + "," + UPDATED_WITHDRAWAL);

        // Get all the walletList where withdrawal equals to UPDATED_WITHDRAWAL
        defaultWalletShouldNotBeFound("withdrawal.in=" + UPDATED_WITHDRAWAL);
    }

    @Test
    void getAllWalletsByWithdrawalIsNullOrNotNull() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where withdrawal is not null
        defaultWalletShouldBeFound("withdrawal.specified=true");

        // Get all the walletList where withdrawal is null
        defaultWalletShouldNotBeFound("withdrawal.specified=false");
    }

    @Test
    void getAllWalletsByWithdrawalIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where withdrawal is greater than or equal to DEFAULT_WITHDRAWAL
        defaultWalletShouldBeFound("withdrawal.greaterThanOrEqual=" + DEFAULT_WITHDRAWAL);

        // Get all the walletList where withdrawal is greater than or equal to UPDATED_WITHDRAWAL
        defaultWalletShouldNotBeFound("withdrawal.greaterThanOrEqual=" + UPDATED_WITHDRAWAL);
    }

    @Test
    void getAllWalletsByWithdrawalIsLessThanOrEqualToSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where withdrawal is less than or equal to DEFAULT_WITHDRAWAL
        defaultWalletShouldBeFound("withdrawal.lessThanOrEqual=" + DEFAULT_WITHDRAWAL);

        // Get all the walletList where withdrawal is less than or equal to SMALLER_WITHDRAWAL
        defaultWalletShouldNotBeFound("withdrawal.lessThanOrEqual=" + SMALLER_WITHDRAWAL);
    }

    @Test
    void getAllWalletsByWithdrawalIsLessThanSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where withdrawal is less than DEFAULT_WITHDRAWAL
        defaultWalletShouldNotBeFound("withdrawal.lessThan=" + DEFAULT_WITHDRAWAL);

        // Get all the walletList where withdrawal is less than UPDATED_WITHDRAWAL
        defaultWalletShouldBeFound("withdrawal.lessThan=" + UPDATED_WITHDRAWAL);
    }

    @Test
    void getAllWalletsByWithdrawalIsGreaterThanSomething() {
        // Initialize the database
        walletRepository.save(wallet).block();

        // Get all the walletList where withdrawal is greater than DEFAULT_WITHDRAWAL
        defaultWalletShouldNotBeFound("withdrawal.greaterThan=" + DEFAULT_WITHDRAWAL);

        // Get all the walletList where withdrawal is greater than SMALLER_WITHDRAWAL
        defaultWalletShouldBeFound("withdrawal.greaterThan=" + SMALLER_WITHDRAWAL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWalletShouldBeFound(String filter) {
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
            .value(hasItem(wallet.getId().intValue()))
            .jsonPath("$.[*].transTypeClassId")
            .value(hasItem(DEFAULT_TRANS_TYPE_CLASS_ID.intValue()))
            .jsonPath("$.[*].stock")
            .value(hasItem(DEFAULT_STOCK.doubleValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].deposit")
            .value(hasItem(DEFAULT_DEPOSIT.doubleValue()))
            .jsonPath("$.[*].withdrawal")
            .value(hasItem(DEFAULT_WITHDRAWAL.doubleValue()));

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
    private void defaultWalletShouldNotBeFound(String filter) {
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
    void getNonExistingWallet() {
        // Get the wallet
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingWallet() throws Exception {
        // Initialize the database
        walletRepository.save(wallet).block();

        int databaseSizeBeforeUpdate = walletRepository.findAll().collectList().block().size();

        // Update the wallet
        Wallet updatedWallet = walletRepository.findById(wallet.getId()).block();
        updatedWallet
            .transTypeClassId(UPDATED_TRANS_TYPE_CLASS_ID)
            .stock(UPDATED_STOCK)
            .description(UPDATED_DESCRIPTION)
            .deposit(UPDATED_DEPOSIT)
            .withdrawal(UPDATED_WITHDRAWAL);
        WalletDTO walletDTO = walletMapper.toDto(updatedWallet);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, walletDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll().collectList().block();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
        Wallet testWallet = walletList.get(walletList.size() - 1);
        assertThat(testWallet.getTransTypeClassId()).isEqualTo(UPDATED_TRANS_TYPE_CLASS_ID);
        assertThat(testWallet.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testWallet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWallet.getDeposit()).isEqualTo(UPDATED_DEPOSIT);
        assertThat(testWallet.getWithdrawal()).isEqualTo(UPDATED_WITHDRAWAL);
    }

    @Test
    void putNonExistingWallet() throws Exception {
        int databaseSizeBeforeUpdate = walletRepository.findAll().collectList().block().size();
        wallet.setId(longCount.incrementAndGet());

        // Create the Wallet
        WalletDTO walletDTO = walletMapper.toDto(wallet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, walletDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll().collectList().block();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchWallet() throws Exception {
        int databaseSizeBeforeUpdate = walletRepository.findAll().collectList().block().size();
        wallet.setId(longCount.incrementAndGet());

        // Create the Wallet
        WalletDTO walletDTO = walletMapper.toDto(wallet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll().collectList().block();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamWallet() throws Exception {
        int databaseSizeBeforeUpdate = walletRepository.findAll().collectList().block().size();
        wallet.setId(longCount.incrementAndGet());

        // Create the Wallet
        WalletDTO walletDTO = walletMapper.toDto(wallet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll().collectList().block();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateWalletWithPatch() throws Exception {
        // Initialize the database
        walletRepository.save(wallet).block();

        int databaseSizeBeforeUpdate = walletRepository.findAll().collectList().block().size();

        // Update the wallet using partial update
        Wallet partialUpdatedWallet = new Wallet();
        partialUpdatedWallet.setId(wallet.getId());

        partialUpdatedWallet.deposit(UPDATED_DEPOSIT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWallet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedWallet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll().collectList().block();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
        Wallet testWallet = walletList.get(walletList.size() - 1);
        assertThat(testWallet.getTransTypeClassId()).isEqualTo(DEFAULT_TRANS_TYPE_CLASS_ID);
        assertThat(testWallet.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testWallet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWallet.getDeposit()).isEqualTo(UPDATED_DEPOSIT);
        assertThat(testWallet.getWithdrawal()).isEqualTo(DEFAULT_WITHDRAWAL);
    }

    @Test
    void fullUpdateWalletWithPatch() throws Exception {
        // Initialize the database
        walletRepository.save(wallet).block();

        int databaseSizeBeforeUpdate = walletRepository.findAll().collectList().block().size();

        // Update the wallet using partial update
        Wallet partialUpdatedWallet = new Wallet();
        partialUpdatedWallet.setId(wallet.getId());

        partialUpdatedWallet
            .transTypeClassId(UPDATED_TRANS_TYPE_CLASS_ID)
            .stock(UPDATED_STOCK)
            .description(UPDATED_DESCRIPTION)
            .deposit(UPDATED_DEPOSIT)
            .withdrawal(UPDATED_WITHDRAWAL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWallet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedWallet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll().collectList().block();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
        Wallet testWallet = walletList.get(walletList.size() - 1);
        assertThat(testWallet.getTransTypeClassId()).isEqualTo(UPDATED_TRANS_TYPE_CLASS_ID);
        assertThat(testWallet.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testWallet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWallet.getDeposit()).isEqualTo(UPDATED_DEPOSIT);
        assertThat(testWallet.getWithdrawal()).isEqualTo(UPDATED_WITHDRAWAL);
    }

    @Test
    void patchNonExistingWallet() throws Exception {
        int databaseSizeBeforeUpdate = walletRepository.findAll().collectList().block().size();
        wallet.setId(longCount.incrementAndGet());

        // Create the Wallet
        WalletDTO walletDTO = walletMapper.toDto(wallet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, walletDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll().collectList().block();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchWallet() throws Exception {
        int databaseSizeBeforeUpdate = walletRepository.findAll().collectList().block().size();
        wallet.setId(longCount.incrementAndGet());

        // Create the Wallet
        WalletDTO walletDTO = walletMapper.toDto(wallet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll().collectList().block();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamWallet() throws Exception {
        int databaseSizeBeforeUpdate = walletRepository.findAll().collectList().block().size();
        wallet.setId(longCount.incrementAndGet());

        // Create the Wallet
        WalletDTO walletDTO = walletMapper.toDto(wallet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll().collectList().block();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteWallet() {
        // Initialize the database
        walletRepository.save(wallet).block();

        int databaseSizeBeforeDelete = walletRepository.findAll().collectList().block().size();

        // Delete the wallet
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, wallet.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Wallet> walletList = walletRepository.findAll().collectList().block();
        assertThat(walletList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
