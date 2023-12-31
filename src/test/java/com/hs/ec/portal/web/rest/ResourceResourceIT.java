package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.Resource;
import com.hs.ec.portal.domain.enumeration.ResourceType;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.repository.ResourceRepository;
import com.hs.ec.portal.service.dto.ResourceDTO;
import com.hs.ec.portal.service.mapper.ResourceMapper;
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
 * Integration tests for the {@link ResourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ResourceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_API_URI = "AAAAAAAAAA";
    private static final String UPDATED_API_URI = "BBBBBBBBBB";

    private static final ResourceType DEFAULT_RESOURCE_TYPE = ResourceType.DOMAIN;
    private static final ResourceType UPDATED_RESOURCE_TYPE = ResourceType.COMPONENT;

    private static final String ENTITY_API_URL = "/api/resources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Resource resource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resource createEntity(EntityManager em) {
        Resource resource = new Resource()
            .name(DEFAULT_NAME)
            .displayName(DEFAULT_DISPLAY_NAME)
            .apiUri(DEFAULT_API_URI)
            .resourceType(DEFAULT_RESOURCE_TYPE);
        return resource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resource createUpdatedEntity(EntityManager em) {
        Resource resource = new Resource()
            .name(UPDATED_NAME)
            .displayName(UPDATED_DISPLAY_NAME)
            .apiUri(UPDATED_API_URI)
            .resourceType(UPDATED_RESOURCE_TYPE);
        return resource;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Resource.class).block();
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
        resource = createEntity(em);
    }

    @Test
    void createResource() throws Exception {
        int databaseSizeBeforeCreate = resourceRepository.findAll().collectList().block().size();
        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll().collectList().block();
        assertThat(resourceList).hasSize(databaseSizeBeforeCreate + 1);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testResource.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
        assertThat(testResource.getApiUri()).isEqualTo(DEFAULT_API_URI);
        assertThat(testResource.getResourceType()).isEqualTo(DEFAULT_RESOURCE_TYPE);
    }

    @Test
    void createResourceWithExistingId() throws Exception {
        // Create the Resource with an existing ID
        resource.setId(1L);
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        int databaseSizeBeforeCreate = resourceRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll().collectList().block();
        assertThat(resourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceRepository.findAll().collectList().block().size();
        // set the field null
        resource.setName(null);

        // Create the Resource, which fails.
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Resource> resourceList = resourceRepository.findAll().collectList().block();
        assertThat(resourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDisplayNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceRepository.findAll().collectList().block().size();
        // set the field null
        resource.setDisplayName(null);

        // Create the Resource, which fails.
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Resource> resourceList = resourceRepository.findAll().collectList().block();
        assertThat(resourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkApiUriIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceRepository.findAll().collectList().block().size();
        // set the field null
        resource.setApiUri(null);

        // Create the Resource, which fails.
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Resource> resourceList = resourceRepository.findAll().collectList().block();
        assertThat(resourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkResourceTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceRepository.findAll().collectList().block().size();
        // set the field null
        resource.setResourceType(null);

        // Create the Resource, which fails.
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Resource> resourceList = resourceRepository.findAll().collectList().block();
        assertThat(resourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllResources() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get all the resourceList
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
            .value(hasItem(resource.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].displayName")
            .value(hasItem(DEFAULT_DISPLAY_NAME))
            .jsonPath("$.[*].apiUri")
            .value(hasItem(DEFAULT_API_URI))
            .jsonPath("$.[*].resourceType")
            .value(hasItem(DEFAULT_RESOURCE_TYPE.toString()));
    }

    @Test
    void getResource() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get the resource
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, resource.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(resource.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.displayName")
            .value(is(DEFAULT_DISPLAY_NAME))
            .jsonPath("$.apiUri")
            .value(is(DEFAULT_API_URI))
            .jsonPath("$.resourceType")
            .value(is(DEFAULT_RESOURCE_TYPE.toString()));
    }

    @Test
    void getResourcesByIdFiltering() {
        // Initialize the database
        resourceRepository.save(resource).block();

        Long id = resource.getId();

        defaultResourceShouldBeFound("id.equals=" + id);
        defaultResourceShouldNotBeFound("id.notEquals=" + id);

        defaultResourceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultResourceShouldNotBeFound("id.greaterThan=" + id);

        defaultResourceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultResourceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllResourcesByNameIsEqualToSomething() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get all the resourceList where name equals to DEFAULT_NAME
        defaultResourceShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the resourceList where name equals to UPDATED_NAME
        defaultResourceShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    void getAllResourcesByNameIsInShouldWork() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get all the resourceList where name in DEFAULT_NAME or UPDATED_NAME
        defaultResourceShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the resourceList where name equals to UPDATED_NAME
        defaultResourceShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    void getAllResourcesByNameIsNullOrNotNull() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get all the resourceList where name is not null
        defaultResourceShouldBeFound("name.specified=true");

        // Get all the resourceList where name is null
        defaultResourceShouldNotBeFound("name.specified=false");
    }

    @Test
    void getAllResourcesByNameContainsSomething() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get all the resourceList where name contains DEFAULT_NAME
        defaultResourceShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the resourceList where name contains UPDATED_NAME
        defaultResourceShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    void getAllResourcesByNameNotContainsSomething() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get all the resourceList where name does not contain DEFAULT_NAME
        defaultResourceShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the resourceList where name does not contain UPDATED_NAME
        defaultResourceShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    void getAllResourcesByDisplayNameIsEqualToSomething() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get all the resourceList where displayName equals to DEFAULT_DISPLAY_NAME
        defaultResourceShouldBeFound("displayName.equals=" + DEFAULT_DISPLAY_NAME);

        // Get all the resourceList where displayName equals to UPDATED_DISPLAY_NAME
        defaultResourceShouldNotBeFound("displayName.equals=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    void getAllResourcesByDisplayNameIsInShouldWork() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get all the resourceList where displayName in DEFAULT_DISPLAY_NAME or UPDATED_DISPLAY_NAME
        defaultResourceShouldBeFound("displayName.in=" + DEFAULT_DISPLAY_NAME + "," + UPDATED_DISPLAY_NAME);

        // Get all the resourceList where displayName equals to UPDATED_DISPLAY_NAME
        defaultResourceShouldNotBeFound("displayName.in=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    void getAllResourcesByDisplayNameIsNullOrNotNull() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get all the resourceList where displayName is not null
        defaultResourceShouldBeFound("displayName.specified=true");

        // Get all the resourceList where displayName is null
        defaultResourceShouldNotBeFound("displayName.specified=false");
    }

    @Test
    void getAllResourcesByDisplayNameContainsSomething() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get all the resourceList where displayName contains DEFAULT_DISPLAY_NAME
        defaultResourceShouldBeFound("displayName.contains=" + DEFAULT_DISPLAY_NAME);

        // Get all the resourceList where displayName contains UPDATED_DISPLAY_NAME
        defaultResourceShouldNotBeFound("displayName.contains=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    void getAllResourcesByDisplayNameNotContainsSomething() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get all the resourceList where displayName does not contain DEFAULT_DISPLAY_NAME
        defaultResourceShouldNotBeFound("displayName.doesNotContain=" + DEFAULT_DISPLAY_NAME);

        // Get all the resourceList where displayName does not contain UPDATED_DISPLAY_NAME
        defaultResourceShouldBeFound("displayName.doesNotContain=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    void getAllResourcesByApiUriIsEqualToSomething() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get all the resourceList where apiUri equals to DEFAULT_API_URI
        defaultResourceShouldBeFound("apiUri.equals=" + DEFAULT_API_URI);

        // Get all the resourceList where apiUri equals to UPDATED_API_URI
        defaultResourceShouldNotBeFound("apiUri.equals=" + UPDATED_API_URI);
    }

    @Test
    void getAllResourcesByApiUriIsInShouldWork() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get all the resourceList where apiUri in DEFAULT_API_URI or UPDATED_API_URI
        defaultResourceShouldBeFound("apiUri.in=" + DEFAULT_API_URI + "," + UPDATED_API_URI);

        // Get all the resourceList where apiUri equals to UPDATED_API_URI
        defaultResourceShouldNotBeFound("apiUri.in=" + UPDATED_API_URI);
    }

    @Test
    void getAllResourcesByApiUriIsNullOrNotNull() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get all the resourceList where apiUri is not null
        defaultResourceShouldBeFound("apiUri.specified=true");

        // Get all the resourceList where apiUri is null
        defaultResourceShouldNotBeFound("apiUri.specified=false");
    }

    @Test
    void getAllResourcesByApiUriContainsSomething() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get all the resourceList where apiUri contains DEFAULT_API_URI
        defaultResourceShouldBeFound("apiUri.contains=" + DEFAULT_API_URI);

        // Get all the resourceList where apiUri contains UPDATED_API_URI
        defaultResourceShouldNotBeFound("apiUri.contains=" + UPDATED_API_URI);
    }

    @Test
    void getAllResourcesByApiUriNotContainsSomething() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get all the resourceList where apiUri does not contain DEFAULT_API_URI
        defaultResourceShouldNotBeFound("apiUri.doesNotContain=" + DEFAULT_API_URI);

        // Get all the resourceList where apiUri does not contain UPDATED_API_URI
        defaultResourceShouldBeFound("apiUri.doesNotContain=" + UPDATED_API_URI);
    }

    @Test
    void getAllResourcesByResourceTypeIsEqualToSomething() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get all the resourceList where resourceType equals to DEFAULT_RESOURCE_TYPE
        defaultResourceShouldBeFound("resourceType.equals=" + DEFAULT_RESOURCE_TYPE);

        // Get all the resourceList where resourceType equals to UPDATED_RESOURCE_TYPE
        defaultResourceShouldNotBeFound("resourceType.equals=" + UPDATED_RESOURCE_TYPE);
    }

    @Test
    void getAllResourcesByResourceTypeIsInShouldWork() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get all the resourceList where resourceType in DEFAULT_RESOURCE_TYPE or UPDATED_RESOURCE_TYPE
        defaultResourceShouldBeFound("resourceType.in=" + DEFAULT_RESOURCE_TYPE + "," + UPDATED_RESOURCE_TYPE);

        // Get all the resourceList where resourceType equals to UPDATED_RESOURCE_TYPE
        defaultResourceShouldNotBeFound("resourceType.in=" + UPDATED_RESOURCE_TYPE);
    }

    @Test
    void getAllResourcesByResourceTypeIsNullOrNotNull() {
        // Initialize the database
        resourceRepository.save(resource).block();

        // Get all the resourceList where resourceType is not null
        defaultResourceShouldBeFound("resourceType.specified=true");

        // Get all the resourceList where resourceType is null
        defaultResourceShouldNotBeFound("resourceType.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultResourceShouldBeFound(String filter) {
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
            .value(hasItem(resource.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].displayName")
            .value(hasItem(DEFAULT_DISPLAY_NAME))
            .jsonPath("$.[*].apiUri")
            .value(hasItem(DEFAULT_API_URI))
            .jsonPath("$.[*].resourceType")
            .value(hasItem(DEFAULT_RESOURCE_TYPE.toString()));

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
    private void defaultResourceShouldNotBeFound(String filter) {
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
    void getNonExistingResource() {
        // Get the resource
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingResource() throws Exception {
        // Initialize the database
        resourceRepository.save(resource).block();

        int databaseSizeBeforeUpdate = resourceRepository.findAll().collectList().block().size();

        // Update the resource
        Resource updatedResource = resourceRepository.findById(resource.getId()).block();
        updatedResource.name(UPDATED_NAME).displayName(UPDATED_DISPLAY_NAME).apiUri(UPDATED_API_URI).resourceType(UPDATED_RESOURCE_TYPE);
        ResourceDTO resourceDTO = resourceMapper.toDto(updatedResource);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, resourceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll().collectList().block();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testResource.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testResource.getApiUri()).isEqualTo(UPDATED_API_URI);
        assertThat(testResource.getResourceType()).isEqualTo(UPDATED_RESOURCE_TYPE);
    }

    @Test
    void putNonExistingResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().collectList().block().size();
        resource.setId(longCount.incrementAndGet());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, resourceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll().collectList().block();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().collectList().block().size();
        resource.setId(longCount.incrementAndGet());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll().collectList().block();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().collectList().block().size();
        resource.setId(longCount.incrementAndGet());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll().collectList().block();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateResourceWithPatch() throws Exception {
        // Initialize the database
        resourceRepository.save(resource).block();

        int databaseSizeBeforeUpdate = resourceRepository.findAll().collectList().block().size();

        // Update the resource using partial update
        Resource partialUpdatedResource = new Resource();
        partialUpdatedResource.setId(resource.getId());

        partialUpdatedResource.name(UPDATED_NAME).resourceType(UPDATED_RESOURCE_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedResource.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedResource))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll().collectList().block();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testResource.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
        assertThat(testResource.getApiUri()).isEqualTo(DEFAULT_API_URI);
        assertThat(testResource.getResourceType()).isEqualTo(UPDATED_RESOURCE_TYPE);
    }

    @Test
    void fullUpdateResourceWithPatch() throws Exception {
        // Initialize the database
        resourceRepository.save(resource).block();

        int databaseSizeBeforeUpdate = resourceRepository.findAll().collectList().block().size();

        // Update the resource using partial update
        Resource partialUpdatedResource = new Resource();
        partialUpdatedResource.setId(resource.getId());

        partialUpdatedResource
            .name(UPDATED_NAME)
            .displayName(UPDATED_DISPLAY_NAME)
            .apiUri(UPDATED_API_URI)
            .resourceType(UPDATED_RESOURCE_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedResource.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedResource))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll().collectList().block();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testResource.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testResource.getApiUri()).isEqualTo(UPDATED_API_URI);
        assertThat(testResource.getResourceType()).isEqualTo(UPDATED_RESOURCE_TYPE);
    }

    @Test
    void patchNonExistingResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().collectList().block().size();
        resource.setId(longCount.incrementAndGet());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, resourceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll().collectList().block();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().collectList().block().size();
        resource.setId(longCount.incrementAndGet());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll().collectList().block();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().collectList().block().size();
        resource.setId(longCount.incrementAndGet());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll().collectList().block();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteResource() {
        // Initialize the database
        resourceRepository.save(resource).block();

        int databaseSizeBeforeDelete = resourceRepository.findAll().collectList().block().size();

        // Delete the resource
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, resource.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Resource> resourceList = resourceRepository.findAll().collectList().block();
        assertThat(resourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
