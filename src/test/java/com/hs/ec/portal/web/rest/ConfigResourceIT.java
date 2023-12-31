package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.Config;
import com.hs.ec.portal.repository.ConfigRepository;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.service.dto.ConfigDTO;
import com.hs.ec.portal.service.mapper.ConfigMapper;
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
 * Integration tests for the {@link ConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ConfigResourceIT {

    private static final String DEFAULT_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Config config;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Config createEntity(EntityManager em) {
        Config config = new Config().displayName(DEFAULT_DISPLAY_NAME).code(DEFAULT_CODE).value(DEFAULT_VALUE);
        return config;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Config createUpdatedEntity(EntityManager em) {
        Config config = new Config().displayName(UPDATED_DISPLAY_NAME).code(UPDATED_CODE).value(UPDATED_VALUE);
        return config;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Config.class).block();
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
        config = createEntity(em);
    }

    @Test
    void createConfig() throws Exception {
        int databaseSizeBeforeCreate = configRepository.findAll().collectList().block().size();
        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(configDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll().collectList().block();
        assertThat(configList).hasSize(databaseSizeBeforeCreate + 1);
        Config testConfig = configList.get(configList.size() - 1);
        assertThat(testConfig.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
        assertThat(testConfig.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testConfig.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    void createConfigWithExistingId() throws Exception {
        // Create the Config with an existing ID
        config.setId(1L);
        ConfigDTO configDTO = configMapper.toDto(config);

        int databaseSizeBeforeCreate = configRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(configDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll().collectList().block();
        assertThat(configList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllConfigs() {
        // Initialize the database
        configRepository.save(config).block();

        // Get all the configList
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
            .value(hasItem(config.getId().intValue()))
            .jsonPath("$.[*].displayName")
            .value(hasItem(DEFAULT_DISPLAY_NAME))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE))
            .jsonPath("$.[*].value")
            .value(hasItem(DEFAULT_VALUE));
    }

    @Test
    void getConfig() {
        // Initialize the database
        configRepository.save(config).block();

        // Get the config
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, config.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(config.getId().intValue()))
            .jsonPath("$.displayName")
            .value(is(DEFAULT_DISPLAY_NAME))
            .jsonPath("$.code")
            .value(is(DEFAULT_CODE))
            .jsonPath("$.value")
            .value(is(DEFAULT_VALUE));
    }

    @Test
    void getConfigsByIdFiltering() {
        // Initialize the database
        configRepository.save(config).block();

        Long id = config.getId();

        defaultConfigShouldBeFound("id.equals=" + id);
        defaultConfigShouldNotBeFound("id.notEquals=" + id);

        defaultConfigShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultConfigShouldNotBeFound("id.greaterThan=" + id);

        defaultConfigShouldBeFound("id.lessThanOrEqual=" + id);
        defaultConfigShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllConfigsByDisplayNameIsEqualToSomething() {
        // Initialize the database
        configRepository.save(config).block();

        // Get all the configList where displayName equals to DEFAULT_DISPLAY_NAME
        defaultConfigShouldBeFound("displayName.equals=" + DEFAULT_DISPLAY_NAME);

        // Get all the configList where displayName equals to UPDATED_DISPLAY_NAME
        defaultConfigShouldNotBeFound("displayName.equals=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    void getAllConfigsByDisplayNameIsInShouldWork() {
        // Initialize the database
        configRepository.save(config).block();

        // Get all the configList where displayName in DEFAULT_DISPLAY_NAME or UPDATED_DISPLAY_NAME
        defaultConfigShouldBeFound("displayName.in=" + DEFAULT_DISPLAY_NAME + "," + UPDATED_DISPLAY_NAME);

        // Get all the configList where displayName equals to UPDATED_DISPLAY_NAME
        defaultConfigShouldNotBeFound("displayName.in=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    void getAllConfigsByDisplayNameIsNullOrNotNull() {
        // Initialize the database
        configRepository.save(config).block();

        // Get all the configList where displayName is not null
        defaultConfigShouldBeFound("displayName.specified=true");

        // Get all the configList where displayName is null
        defaultConfigShouldNotBeFound("displayName.specified=false");
    }

    @Test
    void getAllConfigsByDisplayNameContainsSomething() {
        // Initialize the database
        configRepository.save(config).block();

        // Get all the configList where displayName contains DEFAULT_DISPLAY_NAME
        defaultConfigShouldBeFound("displayName.contains=" + DEFAULT_DISPLAY_NAME);

        // Get all the configList where displayName contains UPDATED_DISPLAY_NAME
        defaultConfigShouldNotBeFound("displayName.contains=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    void getAllConfigsByDisplayNameNotContainsSomething() {
        // Initialize the database
        configRepository.save(config).block();

        // Get all the configList where displayName does not contain DEFAULT_DISPLAY_NAME
        defaultConfigShouldNotBeFound("displayName.doesNotContain=" + DEFAULT_DISPLAY_NAME);

        // Get all the configList where displayName does not contain UPDATED_DISPLAY_NAME
        defaultConfigShouldBeFound("displayName.doesNotContain=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    void getAllConfigsByCodeIsEqualToSomething() {
        // Initialize the database
        configRepository.save(config).block();

        // Get all the configList where code equals to DEFAULT_CODE
        defaultConfigShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the configList where code equals to UPDATED_CODE
        defaultConfigShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    void getAllConfigsByCodeIsInShouldWork() {
        // Initialize the database
        configRepository.save(config).block();

        // Get all the configList where code in DEFAULT_CODE or UPDATED_CODE
        defaultConfigShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the configList where code equals to UPDATED_CODE
        defaultConfigShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    void getAllConfigsByCodeIsNullOrNotNull() {
        // Initialize the database
        configRepository.save(config).block();

        // Get all the configList where code is not null
        defaultConfigShouldBeFound("code.specified=true");

        // Get all the configList where code is null
        defaultConfigShouldNotBeFound("code.specified=false");
    }

    @Test
    void getAllConfigsByCodeContainsSomething() {
        // Initialize the database
        configRepository.save(config).block();

        // Get all the configList where code contains DEFAULT_CODE
        defaultConfigShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the configList where code contains UPDATED_CODE
        defaultConfigShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    void getAllConfigsByCodeNotContainsSomething() {
        // Initialize the database
        configRepository.save(config).block();

        // Get all the configList where code does not contain DEFAULT_CODE
        defaultConfigShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the configList where code does not contain UPDATED_CODE
        defaultConfigShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    void getAllConfigsByValueIsEqualToSomething() {
        // Initialize the database
        configRepository.save(config).block();

        // Get all the configList where value equals to DEFAULT_VALUE
        defaultConfigShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the configList where value equals to UPDATED_VALUE
        defaultConfigShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    void getAllConfigsByValueIsInShouldWork() {
        // Initialize the database
        configRepository.save(config).block();

        // Get all the configList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultConfigShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the configList where value equals to UPDATED_VALUE
        defaultConfigShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    void getAllConfigsByValueIsNullOrNotNull() {
        // Initialize the database
        configRepository.save(config).block();

        // Get all the configList where value is not null
        defaultConfigShouldBeFound("value.specified=true");

        // Get all the configList where value is null
        defaultConfigShouldNotBeFound("value.specified=false");
    }

    @Test
    void getAllConfigsByValueContainsSomething() {
        // Initialize the database
        configRepository.save(config).block();

        // Get all the configList where value contains DEFAULT_VALUE
        defaultConfigShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the configList where value contains UPDATED_VALUE
        defaultConfigShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    void getAllConfigsByValueNotContainsSomething() {
        // Initialize the database
        configRepository.save(config).block();

        // Get all the configList where value does not contain DEFAULT_VALUE
        defaultConfigShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the configList where value does not contain UPDATED_VALUE
        defaultConfigShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConfigShouldBeFound(String filter) {
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
            .value(hasItem(config.getId().intValue()))
            .jsonPath("$.[*].displayName")
            .value(hasItem(DEFAULT_DISPLAY_NAME))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE))
            .jsonPath("$.[*].value")
            .value(hasItem(DEFAULT_VALUE));

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
    private void defaultConfigShouldNotBeFound(String filter) {
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
    void getNonExistingConfig() {
        // Get the config
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingConfig() throws Exception {
        // Initialize the database
        configRepository.save(config).block();

        int databaseSizeBeforeUpdate = configRepository.findAll().collectList().block().size();

        // Update the config
        Config updatedConfig = configRepository.findById(config.getId()).block();
        updatedConfig.displayName(UPDATED_DISPLAY_NAME).code(UPDATED_CODE).value(UPDATED_VALUE);
        ConfigDTO configDTO = configMapper.toDto(updatedConfig);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, configDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(configDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll().collectList().block();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
        Config testConfig = configList.get(configList.size() - 1);
        assertThat(testConfig.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testConfig.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testConfig.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void putNonExistingConfig() throws Exception {
        int databaseSizeBeforeUpdate = configRepository.findAll().collectList().block().size();
        config.setId(longCount.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, configDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(configDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll().collectList().block();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchConfig() throws Exception {
        int databaseSizeBeforeUpdate = configRepository.findAll().collectList().block().size();
        config.setId(longCount.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(configDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll().collectList().block();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamConfig() throws Exception {
        int databaseSizeBeforeUpdate = configRepository.findAll().collectList().block().size();
        config.setId(longCount.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(configDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll().collectList().block();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateConfigWithPatch() throws Exception {
        // Initialize the database
        configRepository.save(config).block();

        int databaseSizeBeforeUpdate = configRepository.findAll().collectList().block().size();

        // Update the config using partial update
        Config partialUpdatedConfig = new Config();
        partialUpdatedConfig.setId(config.getId());

        partialUpdatedConfig.code(UPDATED_CODE).value(UPDATED_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedConfig.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedConfig))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll().collectList().block();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
        Config testConfig = configList.get(configList.size() - 1);
        assertThat(testConfig.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
        assertThat(testConfig.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testConfig.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void fullUpdateConfigWithPatch() throws Exception {
        // Initialize the database
        configRepository.save(config).block();

        int databaseSizeBeforeUpdate = configRepository.findAll().collectList().block().size();

        // Update the config using partial update
        Config partialUpdatedConfig = new Config();
        partialUpdatedConfig.setId(config.getId());

        partialUpdatedConfig.displayName(UPDATED_DISPLAY_NAME).code(UPDATED_CODE).value(UPDATED_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedConfig.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedConfig))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll().collectList().block();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
        Config testConfig = configList.get(configList.size() - 1);
        assertThat(testConfig.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testConfig.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testConfig.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void patchNonExistingConfig() throws Exception {
        int databaseSizeBeforeUpdate = configRepository.findAll().collectList().block().size();
        config.setId(longCount.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, configDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(configDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll().collectList().block();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchConfig() throws Exception {
        int databaseSizeBeforeUpdate = configRepository.findAll().collectList().block().size();
        config.setId(longCount.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(configDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll().collectList().block();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamConfig() throws Exception {
        int databaseSizeBeforeUpdate = configRepository.findAll().collectList().block().size();
        config.setId(longCount.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(configDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll().collectList().block();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteConfig() {
        // Initialize the database
        configRepository.save(config).block();

        int databaseSizeBeforeDelete = configRepository.findAll().collectList().block().size();

        // Delete the config
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, config.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Config> configList = configRepository.findAll().collectList().block();
        assertThat(configList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
