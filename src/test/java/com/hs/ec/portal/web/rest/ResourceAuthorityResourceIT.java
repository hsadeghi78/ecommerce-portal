package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.MyAuthority;
import com.hs.ec.portal.domain.Resource;
import com.hs.ec.portal.domain.ResourceAuthority;
import com.hs.ec.portal.domain.enumeration.Verb;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.repository.MyAuthorityRepository;
import com.hs.ec.portal.repository.ResourceAuthorityRepository;
import com.hs.ec.portal.repository.ResourceRepository;
import com.hs.ec.portal.service.ResourceAuthorityService;
import com.hs.ec.portal.service.dto.ResourceAuthorityDTO;
import com.hs.ec.portal.service.mapper.ResourceAuthorityMapper;
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
 * Integration tests for the {@link ResourceAuthorityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ResourceAuthorityResourceIT {

    private static final Verb DEFAULT_VERB = Verb.NO_GRANT;
    private static final Verb UPDATED_VERB = Verb.VIEW;

    private static final String ENTITY_API_URL = "/api/resource-authorities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResourceAuthorityRepository resourceAuthorityRepository;

    @Mock
    private ResourceAuthorityRepository resourceAuthorityRepositoryMock;

    @Autowired
    private ResourceAuthorityMapper resourceAuthorityMapper;

    @Mock
    private ResourceAuthorityService resourceAuthorityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ResourceAuthority resourceAuthority;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private MyAuthorityRepository myAuthorityRepository;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceAuthority createEntity(EntityManager em) {
        ResourceAuthority resourceAuthority = new ResourceAuthority().verb(DEFAULT_VERB);
        return resourceAuthority;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceAuthority createUpdatedEntity(EntityManager em) {
        ResourceAuthority resourceAuthority = new ResourceAuthority().verb(UPDATED_VERB);
        return resourceAuthority;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ResourceAuthority.class).block();
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
        resourceAuthority = createEntity(em);
    }

    @Test
    void createResourceAuthority() throws Exception {
        int databaseSizeBeforeCreate = resourceAuthorityRepository.findAll().collectList().block().size();
        // Create the ResourceAuthority
        ResourceAuthorityDTO resourceAuthorityDTO = resourceAuthorityMapper.toDto(resourceAuthority);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceAuthorityDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ResourceAuthority in the database
        List<ResourceAuthority> resourceAuthorityList = resourceAuthorityRepository.findAll().collectList().block();
        assertThat(resourceAuthorityList).hasSize(databaseSizeBeforeCreate + 1);
        ResourceAuthority testResourceAuthority = resourceAuthorityList.get(resourceAuthorityList.size() - 1);
        assertThat(testResourceAuthority.getVerb()).isEqualTo(DEFAULT_VERB);
    }

    @Test
    void createResourceAuthorityWithExistingId() throws Exception {
        // Create the ResourceAuthority with an existing ID
        resourceAuthority.setId(1L);
        ResourceAuthorityDTO resourceAuthorityDTO = resourceAuthorityMapper.toDto(resourceAuthority);

        int databaseSizeBeforeCreate = resourceAuthorityRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceAuthorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ResourceAuthority in the database
        List<ResourceAuthority> resourceAuthorityList = resourceAuthorityRepository.findAll().collectList().block();
        assertThat(resourceAuthorityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkVerbIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceAuthorityRepository.findAll().collectList().block().size();
        // set the field null
        resourceAuthority.setVerb(null);

        // Create the ResourceAuthority, which fails.
        ResourceAuthorityDTO resourceAuthorityDTO = resourceAuthorityMapper.toDto(resourceAuthority);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceAuthorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ResourceAuthority> resourceAuthorityList = resourceAuthorityRepository.findAll().collectList().block();
        assertThat(resourceAuthorityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllResourceAuthorities() {
        // Initialize the database
        resourceAuthorityRepository.save(resourceAuthority).block();

        // Get all the resourceAuthorityList
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
            .value(hasItem(resourceAuthority.getId().intValue()))
            .jsonPath("$.[*].verb")
            .value(hasItem(DEFAULT_VERB.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllResourceAuthoritiesWithEagerRelationshipsIsEnabled() {
        when(resourceAuthorityServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(resourceAuthorityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllResourceAuthoritiesWithEagerRelationshipsIsNotEnabled() {
        when(resourceAuthorityServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(resourceAuthorityRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getResourceAuthority() {
        // Initialize the database
        resourceAuthorityRepository.save(resourceAuthority).block();

        // Get the resourceAuthority
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, resourceAuthority.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(resourceAuthority.getId().intValue()))
            .jsonPath("$.verb")
            .value(is(DEFAULT_VERB.toString()));
    }

    @Test
    void getResourceAuthoritiesByIdFiltering() {
        // Initialize the database
        resourceAuthorityRepository.save(resourceAuthority).block();

        Long id = resourceAuthority.getId();

        defaultResourceAuthorityShouldBeFound("id.equals=" + id);
        defaultResourceAuthorityShouldNotBeFound("id.notEquals=" + id);

        defaultResourceAuthorityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultResourceAuthorityShouldNotBeFound("id.greaterThan=" + id);

        defaultResourceAuthorityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultResourceAuthorityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllResourceAuthoritiesByVerbIsEqualToSomething() {
        // Initialize the database
        resourceAuthorityRepository.save(resourceAuthority).block();

        // Get all the resourceAuthorityList where verb equals to DEFAULT_VERB
        defaultResourceAuthorityShouldBeFound("verb.equals=" + DEFAULT_VERB);

        // Get all the resourceAuthorityList where verb equals to UPDATED_VERB
        defaultResourceAuthorityShouldNotBeFound("verb.equals=" + UPDATED_VERB);
    }

    @Test
    void getAllResourceAuthoritiesByVerbIsInShouldWork() {
        // Initialize the database
        resourceAuthorityRepository.save(resourceAuthority).block();

        // Get all the resourceAuthorityList where verb in DEFAULT_VERB or UPDATED_VERB
        defaultResourceAuthorityShouldBeFound("verb.in=" + DEFAULT_VERB + "," + UPDATED_VERB);

        // Get all the resourceAuthorityList where verb equals to UPDATED_VERB
        defaultResourceAuthorityShouldNotBeFound("verb.in=" + UPDATED_VERB);
    }

    @Test
    void getAllResourceAuthoritiesByVerbIsNullOrNotNull() {
        // Initialize the database
        resourceAuthorityRepository.save(resourceAuthority).block();

        // Get all the resourceAuthorityList where verb is not null
        defaultResourceAuthorityShouldBeFound("verb.specified=true");

        // Get all the resourceAuthorityList where verb is null
        defaultResourceAuthorityShouldNotBeFound("verb.specified=false");
    }

    @Test
    void getAllResourceAuthoritiesByResourceIsEqualToSomething() {
        Resource resource = ResourceResourceIT.createEntity(em);
        resourceRepository.save(resource).block();
        Long resourceId = resource.getId();
        resourceAuthority.setResourceId(resourceId);
        resourceAuthorityRepository.save(resourceAuthority).block();
        // Get all the resourceAuthorityList where resource equals to resourceId
        defaultResourceAuthorityShouldBeFound("resourceId.equals=" + resourceId);

        // Get all the resourceAuthorityList where resource equals to (resourceId + 1)
        defaultResourceAuthorityShouldNotBeFound("resourceId.equals=" + (resourceId + 1));
    }

    @Test
    void getAllResourceAuthoritiesByMyAuthorityIsEqualToSomething() {
        MyAuthority myAuthority = MyAuthorityResourceIT.createEntity(em);
        myAuthorityRepository.save(myAuthority).block();
        Long myAuthorityId = myAuthority.getId();
        resourceAuthority.setMyAuthorityId(myAuthorityId);
        resourceAuthorityRepository.save(resourceAuthority).block();
        // Get all the resourceAuthorityList where myAuthority equals to myAuthorityId
        defaultResourceAuthorityShouldBeFound("myAuthorityId.equals=" + myAuthorityId);

        // Get all the resourceAuthorityList where myAuthority equals to (myAuthorityId + 1)
        defaultResourceAuthorityShouldNotBeFound("myAuthorityId.equals=" + (myAuthorityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultResourceAuthorityShouldBeFound(String filter) {
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
            .value(hasItem(resourceAuthority.getId().intValue()))
            .jsonPath("$.[*].verb")
            .value(hasItem(DEFAULT_VERB.toString()));

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
    private void defaultResourceAuthorityShouldNotBeFound(String filter) {
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
    void getNonExistingResourceAuthority() {
        // Get the resourceAuthority
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingResourceAuthority() throws Exception {
        // Initialize the database
        resourceAuthorityRepository.save(resourceAuthority).block();

        int databaseSizeBeforeUpdate = resourceAuthorityRepository.findAll().collectList().block().size();

        // Update the resourceAuthority
        ResourceAuthority updatedResourceAuthority = resourceAuthorityRepository.findById(resourceAuthority.getId()).block();
        updatedResourceAuthority.verb(UPDATED_VERB);
        ResourceAuthorityDTO resourceAuthorityDTO = resourceAuthorityMapper.toDto(updatedResourceAuthority);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, resourceAuthorityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceAuthorityDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ResourceAuthority in the database
        List<ResourceAuthority> resourceAuthorityList = resourceAuthorityRepository.findAll().collectList().block();
        assertThat(resourceAuthorityList).hasSize(databaseSizeBeforeUpdate);
        ResourceAuthority testResourceAuthority = resourceAuthorityList.get(resourceAuthorityList.size() - 1);
        assertThat(testResourceAuthority.getVerb()).isEqualTo(UPDATED_VERB);
    }

    @Test
    void putNonExistingResourceAuthority() throws Exception {
        int databaseSizeBeforeUpdate = resourceAuthorityRepository.findAll().collectList().block().size();
        resourceAuthority.setId(longCount.incrementAndGet());

        // Create the ResourceAuthority
        ResourceAuthorityDTO resourceAuthorityDTO = resourceAuthorityMapper.toDto(resourceAuthority);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, resourceAuthorityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceAuthorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ResourceAuthority in the database
        List<ResourceAuthority> resourceAuthorityList = resourceAuthorityRepository.findAll().collectList().block();
        assertThat(resourceAuthorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchResourceAuthority() throws Exception {
        int databaseSizeBeforeUpdate = resourceAuthorityRepository.findAll().collectList().block().size();
        resourceAuthority.setId(longCount.incrementAndGet());

        // Create the ResourceAuthority
        ResourceAuthorityDTO resourceAuthorityDTO = resourceAuthorityMapper.toDto(resourceAuthority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceAuthorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ResourceAuthority in the database
        List<ResourceAuthority> resourceAuthorityList = resourceAuthorityRepository.findAll().collectList().block();
        assertThat(resourceAuthorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamResourceAuthority() throws Exception {
        int databaseSizeBeforeUpdate = resourceAuthorityRepository.findAll().collectList().block().size();
        resourceAuthority.setId(longCount.incrementAndGet());

        // Create the ResourceAuthority
        ResourceAuthorityDTO resourceAuthorityDTO = resourceAuthorityMapper.toDto(resourceAuthority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceAuthorityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ResourceAuthority in the database
        List<ResourceAuthority> resourceAuthorityList = resourceAuthorityRepository.findAll().collectList().block();
        assertThat(resourceAuthorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateResourceAuthorityWithPatch() throws Exception {
        // Initialize the database
        resourceAuthorityRepository.save(resourceAuthority).block();

        int databaseSizeBeforeUpdate = resourceAuthorityRepository.findAll().collectList().block().size();

        // Update the resourceAuthority using partial update
        ResourceAuthority partialUpdatedResourceAuthority = new ResourceAuthority();
        partialUpdatedResourceAuthority.setId(resourceAuthority.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedResourceAuthority.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedResourceAuthority))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ResourceAuthority in the database
        List<ResourceAuthority> resourceAuthorityList = resourceAuthorityRepository.findAll().collectList().block();
        assertThat(resourceAuthorityList).hasSize(databaseSizeBeforeUpdate);
        ResourceAuthority testResourceAuthority = resourceAuthorityList.get(resourceAuthorityList.size() - 1);
        assertThat(testResourceAuthority.getVerb()).isEqualTo(DEFAULT_VERB);
    }

    @Test
    void fullUpdateResourceAuthorityWithPatch() throws Exception {
        // Initialize the database
        resourceAuthorityRepository.save(resourceAuthority).block();

        int databaseSizeBeforeUpdate = resourceAuthorityRepository.findAll().collectList().block().size();

        // Update the resourceAuthority using partial update
        ResourceAuthority partialUpdatedResourceAuthority = new ResourceAuthority();
        partialUpdatedResourceAuthority.setId(resourceAuthority.getId());

        partialUpdatedResourceAuthority.verb(UPDATED_VERB);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedResourceAuthority.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedResourceAuthority))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ResourceAuthority in the database
        List<ResourceAuthority> resourceAuthorityList = resourceAuthorityRepository.findAll().collectList().block();
        assertThat(resourceAuthorityList).hasSize(databaseSizeBeforeUpdate);
        ResourceAuthority testResourceAuthority = resourceAuthorityList.get(resourceAuthorityList.size() - 1);
        assertThat(testResourceAuthority.getVerb()).isEqualTo(UPDATED_VERB);
    }

    @Test
    void patchNonExistingResourceAuthority() throws Exception {
        int databaseSizeBeforeUpdate = resourceAuthorityRepository.findAll().collectList().block().size();
        resourceAuthority.setId(longCount.incrementAndGet());

        // Create the ResourceAuthority
        ResourceAuthorityDTO resourceAuthorityDTO = resourceAuthorityMapper.toDto(resourceAuthority);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, resourceAuthorityDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceAuthorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ResourceAuthority in the database
        List<ResourceAuthority> resourceAuthorityList = resourceAuthorityRepository.findAll().collectList().block();
        assertThat(resourceAuthorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchResourceAuthority() throws Exception {
        int databaseSizeBeforeUpdate = resourceAuthorityRepository.findAll().collectList().block().size();
        resourceAuthority.setId(longCount.incrementAndGet());

        // Create the ResourceAuthority
        ResourceAuthorityDTO resourceAuthorityDTO = resourceAuthorityMapper.toDto(resourceAuthority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceAuthorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ResourceAuthority in the database
        List<ResourceAuthority> resourceAuthorityList = resourceAuthorityRepository.findAll().collectList().block();
        assertThat(resourceAuthorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamResourceAuthority() throws Exception {
        int databaseSizeBeforeUpdate = resourceAuthorityRepository.findAll().collectList().block().size();
        resourceAuthority.setId(longCount.incrementAndGet());

        // Create the ResourceAuthority
        ResourceAuthorityDTO resourceAuthorityDTO = resourceAuthorityMapper.toDto(resourceAuthority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(resourceAuthorityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ResourceAuthority in the database
        List<ResourceAuthority> resourceAuthorityList = resourceAuthorityRepository.findAll().collectList().block();
        assertThat(resourceAuthorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteResourceAuthority() {
        // Initialize the database
        resourceAuthorityRepository.save(resourceAuthority).block();

        int databaseSizeBeforeDelete = resourceAuthorityRepository.findAll().collectList().block().size();

        // Delete the resourceAuthority
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, resourceAuthority.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ResourceAuthority> resourceAuthorityList = resourceAuthorityRepository.findAll().collectList().block();
        assertThat(resourceAuthorityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
