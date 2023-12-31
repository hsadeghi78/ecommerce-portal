package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.Criticism;
import com.hs.ec.portal.repository.CriticismRepository;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.service.dto.CriticismDTO;
import com.hs.ec.portal.service.mapper.CriticismMapper;
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
 * Integration tests for the {@link CriticismResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CriticismResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/criticisms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CriticismRepository criticismRepository;

    @Autowired
    private CriticismMapper criticismMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Criticism criticism;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Criticism createEntity(EntityManager em) {
        Criticism criticism = new Criticism()
            .fullName(DEFAULT_FULL_NAME)
            .email(DEFAULT_EMAIL)
            .contactNumber(DEFAULT_CONTACT_NUMBER)
            .description(DEFAULT_DESCRIPTION);
        return criticism;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Criticism createUpdatedEntity(EntityManager em) {
        Criticism criticism = new Criticism()
            .fullName(UPDATED_FULL_NAME)
            .email(UPDATED_EMAIL)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .description(UPDATED_DESCRIPTION);
        return criticism;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Criticism.class).block();
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
        criticism = createEntity(em);
    }

    @Test
    void createCriticism() throws Exception {
        int databaseSizeBeforeCreate = criticismRepository.findAll().collectList().block().size();
        // Create the Criticism
        CriticismDTO criticismDTO = criticismMapper.toDto(criticism);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(criticismDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Criticism in the database
        List<Criticism> criticismList = criticismRepository.findAll().collectList().block();
        assertThat(criticismList).hasSize(databaseSizeBeforeCreate + 1);
        Criticism testCriticism = criticismList.get(criticismList.size() - 1);
        assertThat(testCriticism.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testCriticism.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCriticism.getContactNumber()).isEqualTo(DEFAULT_CONTACT_NUMBER);
        assertThat(testCriticism.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createCriticismWithExistingId() throws Exception {
        // Create the Criticism with an existing ID
        criticism.setId(1L);
        CriticismDTO criticismDTO = criticismMapper.toDto(criticism);

        int databaseSizeBeforeCreate = criticismRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(criticismDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Criticism in the database
        List<Criticism> criticismList = criticismRepository.findAll().collectList().block();
        assertThat(criticismList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = criticismRepository.findAll().collectList().block().size();
        // set the field null
        criticism.setFullName(null);

        // Create the Criticism, which fails.
        CriticismDTO criticismDTO = criticismMapper.toDto(criticism);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(criticismDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Criticism> criticismList = criticismRepository.findAll().collectList().block();
        assertThat(criticismList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = criticismRepository.findAll().collectList().block().size();
        // set the field null
        criticism.setDescription(null);

        // Create the Criticism, which fails.
        CriticismDTO criticismDTO = criticismMapper.toDto(criticism);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(criticismDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Criticism> criticismList = criticismRepository.findAll().collectList().block();
        assertThat(criticismList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCriticisms() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList
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
            .value(hasItem(criticism.getId().intValue()))
            .jsonPath("$.[*].fullName")
            .value(hasItem(DEFAULT_FULL_NAME))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].contactNumber")
            .value(hasItem(DEFAULT_CONTACT_NUMBER))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getCriticism() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get the criticism
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, criticism.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(criticism.getId().intValue()))
            .jsonPath("$.fullName")
            .value(is(DEFAULT_FULL_NAME))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.contactNumber")
            .value(is(DEFAULT_CONTACT_NUMBER))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getCriticismsByIdFiltering() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        Long id = criticism.getId();

        defaultCriticismShouldBeFound("id.equals=" + id);
        defaultCriticismShouldNotBeFound("id.notEquals=" + id);

        defaultCriticismShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCriticismShouldNotBeFound("id.greaterThan=" + id);

        defaultCriticismShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCriticismShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllCriticismsByFullNameIsEqualToSomething() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where fullName equals to DEFAULT_FULL_NAME
        defaultCriticismShouldBeFound("fullName.equals=" + DEFAULT_FULL_NAME);

        // Get all the criticismList where fullName equals to UPDATED_FULL_NAME
        defaultCriticismShouldNotBeFound("fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    void getAllCriticismsByFullNameIsInShouldWork() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where fullName in DEFAULT_FULL_NAME or UPDATED_FULL_NAME
        defaultCriticismShouldBeFound("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME);

        // Get all the criticismList where fullName equals to UPDATED_FULL_NAME
        defaultCriticismShouldNotBeFound("fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    void getAllCriticismsByFullNameIsNullOrNotNull() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where fullName is not null
        defaultCriticismShouldBeFound("fullName.specified=true");

        // Get all the criticismList where fullName is null
        defaultCriticismShouldNotBeFound("fullName.specified=false");
    }

    @Test
    void getAllCriticismsByFullNameContainsSomething() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where fullName contains DEFAULT_FULL_NAME
        defaultCriticismShouldBeFound("fullName.contains=" + DEFAULT_FULL_NAME);

        // Get all the criticismList where fullName contains UPDATED_FULL_NAME
        defaultCriticismShouldNotBeFound("fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    void getAllCriticismsByFullNameNotContainsSomething() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where fullName does not contain DEFAULT_FULL_NAME
        defaultCriticismShouldNotBeFound("fullName.doesNotContain=" + DEFAULT_FULL_NAME);

        // Get all the criticismList where fullName does not contain UPDATED_FULL_NAME
        defaultCriticismShouldBeFound("fullName.doesNotContain=" + UPDATED_FULL_NAME);
    }

    @Test
    void getAllCriticismsByEmailIsEqualToSomething() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where email equals to DEFAULT_EMAIL
        defaultCriticismShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the criticismList where email equals to UPDATED_EMAIL
        defaultCriticismShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    void getAllCriticismsByEmailIsInShouldWork() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCriticismShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the criticismList where email equals to UPDATED_EMAIL
        defaultCriticismShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    void getAllCriticismsByEmailIsNullOrNotNull() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where email is not null
        defaultCriticismShouldBeFound("email.specified=true");

        // Get all the criticismList where email is null
        defaultCriticismShouldNotBeFound("email.specified=false");
    }

    @Test
    void getAllCriticismsByEmailContainsSomething() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where email contains DEFAULT_EMAIL
        defaultCriticismShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the criticismList where email contains UPDATED_EMAIL
        defaultCriticismShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    void getAllCriticismsByEmailNotContainsSomething() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where email does not contain DEFAULT_EMAIL
        defaultCriticismShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the criticismList where email does not contain UPDATED_EMAIL
        defaultCriticismShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    void getAllCriticismsByContactNumberIsEqualToSomething() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where contactNumber equals to DEFAULT_CONTACT_NUMBER
        defaultCriticismShouldBeFound("contactNumber.equals=" + DEFAULT_CONTACT_NUMBER);

        // Get all the criticismList where contactNumber equals to UPDATED_CONTACT_NUMBER
        defaultCriticismShouldNotBeFound("contactNumber.equals=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    void getAllCriticismsByContactNumberIsInShouldWork() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where contactNumber in DEFAULT_CONTACT_NUMBER or UPDATED_CONTACT_NUMBER
        defaultCriticismShouldBeFound("contactNumber.in=" + DEFAULT_CONTACT_NUMBER + "," + UPDATED_CONTACT_NUMBER);

        // Get all the criticismList where contactNumber equals to UPDATED_CONTACT_NUMBER
        defaultCriticismShouldNotBeFound("contactNumber.in=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    void getAllCriticismsByContactNumberIsNullOrNotNull() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where contactNumber is not null
        defaultCriticismShouldBeFound("contactNumber.specified=true");

        // Get all the criticismList where contactNumber is null
        defaultCriticismShouldNotBeFound("contactNumber.specified=false");
    }

    @Test
    void getAllCriticismsByContactNumberContainsSomething() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where contactNumber contains DEFAULT_CONTACT_NUMBER
        defaultCriticismShouldBeFound("contactNumber.contains=" + DEFAULT_CONTACT_NUMBER);

        // Get all the criticismList where contactNumber contains UPDATED_CONTACT_NUMBER
        defaultCriticismShouldNotBeFound("contactNumber.contains=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    void getAllCriticismsByContactNumberNotContainsSomething() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where contactNumber does not contain DEFAULT_CONTACT_NUMBER
        defaultCriticismShouldNotBeFound("contactNumber.doesNotContain=" + DEFAULT_CONTACT_NUMBER);

        // Get all the criticismList where contactNumber does not contain UPDATED_CONTACT_NUMBER
        defaultCriticismShouldBeFound("contactNumber.doesNotContain=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    void getAllCriticismsByDescriptionIsEqualToSomething() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where description equals to DEFAULT_DESCRIPTION
        defaultCriticismShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the criticismList where description equals to UPDATED_DESCRIPTION
        defaultCriticismShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllCriticismsByDescriptionIsInShouldWork() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCriticismShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the criticismList where description equals to UPDATED_DESCRIPTION
        defaultCriticismShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllCriticismsByDescriptionIsNullOrNotNull() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where description is not null
        defaultCriticismShouldBeFound("description.specified=true");

        // Get all the criticismList where description is null
        defaultCriticismShouldNotBeFound("description.specified=false");
    }

    @Test
    void getAllCriticismsByDescriptionContainsSomething() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where description contains DEFAULT_DESCRIPTION
        defaultCriticismShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the criticismList where description contains UPDATED_DESCRIPTION
        defaultCriticismShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllCriticismsByDescriptionNotContainsSomething() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        // Get all the criticismList where description does not contain DEFAULT_DESCRIPTION
        defaultCriticismShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the criticismList where description does not contain UPDATED_DESCRIPTION
        defaultCriticismShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCriticismShouldBeFound(String filter) {
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
            .value(hasItem(criticism.getId().intValue()))
            .jsonPath("$.[*].fullName")
            .value(hasItem(DEFAULT_FULL_NAME))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].contactNumber")
            .value(hasItem(DEFAULT_CONTACT_NUMBER))
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
    private void defaultCriticismShouldNotBeFound(String filter) {
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
    void getNonExistingCriticism() {
        // Get the criticism
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCriticism() throws Exception {
        // Initialize the database
        criticismRepository.save(criticism).block();

        int databaseSizeBeforeUpdate = criticismRepository.findAll().collectList().block().size();

        // Update the criticism
        Criticism updatedCriticism = criticismRepository.findById(criticism.getId()).block();
        updatedCriticism
            .fullName(UPDATED_FULL_NAME)
            .email(UPDATED_EMAIL)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .description(UPDATED_DESCRIPTION);
        CriticismDTO criticismDTO = criticismMapper.toDto(updatedCriticism);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, criticismDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(criticismDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Criticism in the database
        List<Criticism> criticismList = criticismRepository.findAll().collectList().block();
        assertThat(criticismList).hasSize(databaseSizeBeforeUpdate);
        Criticism testCriticism = criticismList.get(criticismList.size() - 1);
        assertThat(testCriticism.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testCriticism.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCriticism.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
        assertThat(testCriticism.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingCriticism() throws Exception {
        int databaseSizeBeforeUpdate = criticismRepository.findAll().collectList().block().size();
        criticism.setId(longCount.incrementAndGet());

        // Create the Criticism
        CriticismDTO criticismDTO = criticismMapper.toDto(criticism);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, criticismDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(criticismDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Criticism in the database
        List<Criticism> criticismList = criticismRepository.findAll().collectList().block();
        assertThat(criticismList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCriticism() throws Exception {
        int databaseSizeBeforeUpdate = criticismRepository.findAll().collectList().block().size();
        criticism.setId(longCount.incrementAndGet());

        // Create the Criticism
        CriticismDTO criticismDTO = criticismMapper.toDto(criticism);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(criticismDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Criticism in the database
        List<Criticism> criticismList = criticismRepository.findAll().collectList().block();
        assertThat(criticismList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCriticism() throws Exception {
        int databaseSizeBeforeUpdate = criticismRepository.findAll().collectList().block().size();
        criticism.setId(longCount.incrementAndGet());

        // Create the Criticism
        CriticismDTO criticismDTO = criticismMapper.toDto(criticism);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(criticismDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Criticism in the database
        List<Criticism> criticismList = criticismRepository.findAll().collectList().block();
        assertThat(criticismList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCriticismWithPatch() throws Exception {
        // Initialize the database
        criticismRepository.save(criticism).block();

        int databaseSizeBeforeUpdate = criticismRepository.findAll().collectList().block().size();

        // Update the criticism using partial update
        Criticism partialUpdatedCriticism = new Criticism();
        partialUpdatedCriticism.setId(criticism.getId());

        partialUpdatedCriticism.email(UPDATED_EMAIL).contactNumber(UPDATED_CONTACT_NUMBER).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCriticism.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCriticism))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Criticism in the database
        List<Criticism> criticismList = criticismRepository.findAll().collectList().block();
        assertThat(criticismList).hasSize(databaseSizeBeforeUpdate);
        Criticism testCriticism = criticismList.get(criticismList.size() - 1);
        assertThat(testCriticism.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testCriticism.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCriticism.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
        assertThat(testCriticism.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void fullUpdateCriticismWithPatch() throws Exception {
        // Initialize the database
        criticismRepository.save(criticism).block();

        int databaseSizeBeforeUpdate = criticismRepository.findAll().collectList().block().size();

        // Update the criticism using partial update
        Criticism partialUpdatedCriticism = new Criticism();
        partialUpdatedCriticism.setId(criticism.getId());

        partialUpdatedCriticism
            .fullName(UPDATED_FULL_NAME)
            .email(UPDATED_EMAIL)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCriticism.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCriticism))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Criticism in the database
        List<Criticism> criticismList = criticismRepository.findAll().collectList().block();
        assertThat(criticismList).hasSize(databaseSizeBeforeUpdate);
        Criticism testCriticism = criticismList.get(criticismList.size() - 1);
        assertThat(testCriticism.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testCriticism.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCriticism.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
        assertThat(testCriticism.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingCriticism() throws Exception {
        int databaseSizeBeforeUpdate = criticismRepository.findAll().collectList().block().size();
        criticism.setId(longCount.incrementAndGet());

        // Create the Criticism
        CriticismDTO criticismDTO = criticismMapper.toDto(criticism);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, criticismDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(criticismDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Criticism in the database
        List<Criticism> criticismList = criticismRepository.findAll().collectList().block();
        assertThat(criticismList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCriticism() throws Exception {
        int databaseSizeBeforeUpdate = criticismRepository.findAll().collectList().block().size();
        criticism.setId(longCount.incrementAndGet());

        // Create the Criticism
        CriticismDTO criticismDTO = criticismMapper.toDto(criticism);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(criticismDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Criticism in the database
        List<Criticism> criticismList = criticismRepository.findAll().collectList().block();
        assertThat(criticismList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCriticism() throws Exception {
        int databaseSizeBeforeUpdate = criticismRepository.findAll().collectList().block().size();
        criticism.setId(longCount.incrementAndGet());

        // Create the Criticism
        CriticismDTO criticismDTO = criticismMapper.toDto(criticism);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(criticismDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Criticism in the database
        List<Criticism> criticismList = criticismRepository.findAll().collectList().block();
        assertThat(criticismList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCriticism() {
        // Initialize the database
        criticismRepository.save(criticism).block();

        int databaseSizeBeforeDelete = criticismRepository.findAll().collectList().block().size();

        // Delete the criticism
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, criticism.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Criticism> criticismList = criticismRepository.findAll().collectList().block();
        assertThat(criticismList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
