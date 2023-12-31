package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.MyAuthority;
import com.hs.ec.portal.domain.MyAuthority;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.repository.MyAuthorityRepository;
import com.hs.ec.portal.repository.MyAuthorityRepository;
import com.hs.ec.portal.service.MyAuthorityService;
import com.hs.ec.portal.service.dto.MyAuthorityDTO;
import com.hs.ec.portal.service.mapper.MyAuthorityMapper;
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
 * Integration tests for the {@link MyAuthorityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MyAuthorityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/my-authorities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MyAuthorityRepository myAuthorityRepository;

    @Mock
    private MyAuthorityRepository myAuthorityRepositoryMock;

    @Autowired
    private MyAuthorityMapper myAuthorityMapper;

    @Mock
    private MyAuthorityService myAuthorityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private MyAuthority myAuthority;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyAuthority createEntity(EntityManager em) {
        MyAuthority myAuthority = new MyAuthority().name(DEFAULT_NAME).displayName(DEFAULT_DISPLAY_NAME);
        return myAuthority;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyAuthority createUpdatedEntity(EntityManager em) {
        MyAuthority myAuthority = new MyAuthority().name(UPDATED_NAME).displayName(UPDATED_DISPLAY_NAME);
        return myAuthority;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(MyAuthority.class).block();
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
        myAuthority = createEntity(em);
    }

    @Test
    void createMyAuthority() throws Exception {
        int databaseSizeBeforeCreate = myAuthorityRepository.findAll().collectList().block().size();
        // Create the MyAuthority
        MyAuthorityDTO myAuthorityDTO = myAuthorityMapper.toDto(myAuthority);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(myAuthorityDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the MyAuthority in the database
        List<MyAuthority> myAuthorityList = myAuthorityRepository.findAll().collectList().block();
        assertThat(myAuthorityList).hasSize(databaseSizeBeforeCreate + 1);
        MyAuthority testMyAuthority = myAuthorityList.get(myAuthorityList.size() - 1);
        assertThat(testMyAuthority.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMyAuthority.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
    }

    @Test
    void createMyAuthorityWithExistingId() throws Exception {
        // Create the MyAuthority with an existing ID
        myAuthority.setId(1L);
        MyAuthorityDTO myAuthorityDTO = myAuthorityMapper.toDto(myAuthority);

        int databaseSizeBeforeCreate = myAuthorityRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(myAuthorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MyAuthority in the database
        List<MyAuthority> myAuthorityList = myAuthorityRepository.findAll().collectList().block();
        assertThat(myAuthorityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = myAuthorityRepository.findAll().collectList().block().size();
        // set the field null
        myAuthority.setName(null);

        // Create the MyAuthority, which fails.
        MyAuthorityDTO myAuthorityDTO = myAuthorityMapper.toDto(myAuthority);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(myAuthorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<MyAuthority> myAuthorityList = myAuthorityRepository.findAll().collectList().block();
        assertThat(myAuthorityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDisplayNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = myAuthorityRepository.findAll().collectList().block().size();
        // set the field null
        myAuthority.setDisplayName(null);

        // Create the MyAuthority, which fails.
        MyAuthorityDTO myAuthorityDTO = myAuthorityMapper.toDto(myAuthority);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(myAuthorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<MyAuthority> myAuthorityList = myAuthorityRepository.findAll().collectList().block();
        assertThat(myAuthorityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllMyAuthorities() {
        // Initialize the database
        myAuthorityRepository.save(myAuthority).block();

        // Get all the myAuthorityList
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
            .value(hasItem(myAuthority.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].displayName")
            .value(hasItem(DEFAULT_DISPLAY_NAME));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMyAuthoritiesWithEagerRelationshipsIsEnabled() {
        when(myAuthorityServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(myAuthorityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMyAuthoritiesWithEagerRelationshipsIsNotEnabled() {
        when(myAuthorityServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(myAuthorityRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getMyAuthority() {
        // Initialize the database
        myAuthorityRepository.save(myAuthority).block();

        // Get the myAuthority
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, myAuthority.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(myAuthority.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.displayName")
            .value(is(DEFAULT_DISPLAY_NAME));
    }

    @Test
    void getMyAuthoritiesByIdFiltering() {
        // Initialize the database
        myAuthorityRepository.save(myAuthority).block();

        Long id = myAuthority.getId();

        defaultMyAuthorityShouldBeFound("id.equals=" + id);
        defaultMyAuthorityShouldNotBeFound("id.notEquals=" + id);

        defaultMyAuthorityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMyAuthorityShouldNotBeFound("id.greaterThan=" + id);

        defaultMyAuthorityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMyAuthorityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllMyAuthoritiesByNameIsEqualToSomething() {
        // Initialize the database
        myAuthorityRepository.save(myAuthority).block();

        // Get all the myAuthorityList where name equals to DEFAULT_NAME
        defaultMyAuthorityShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the myAuthorityList where name equals to UPDATED_NAME
        defaultMyAuthorityShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    void getAllMyAuthoritiesByNameIsInShouldWork() {
        // Initialize the database
        myAuthorityRepository.save(myAuthority).block();

        // Get all the myAuthorityList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMyAuthorityShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the myAuthorityList where name equals to UPDATED_NAME
        defaultMyAuthorityShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    void getAllMyAuthoritiesByNameIsNullOrNotNull() {
        // Initialize the database
        myAuthorityRepository.save(myAuthority).block();

        // Get all the myAuthorityList where name is not null
        defaultMyAuthorityShouldBeFound("name.specified=true");

        // Get all the myAuthorityList where name is null
        defaultMyAuthorityShouldNotBeFound("name.specified=false");
    }

    @Test
    void getAllMyAuthoritiesByNameContainsSomething() {
        // Initialize the database
        myAuthorityRepository.save(myAuthority).block();

        // Get all the myAuthorityList where name contains DEFAULT_NAME
        defaultMyAuthorityShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the myAuthorityList where name contains UPDATED_NAME
        defaultMyAuthorityShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    void getAllMyAuthoritiesByNameNotContainsSomething() {
        // Initialize the database
        myAuthorityRepository.save(myAuthority).block();

        // Get all the myAuthorityList where name does not contain DEFAULT_NAME
        defaultMyAuthorityShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the myAuthorityList where name does not contain UPDATED_NAME
        defaultMyAuthorityShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    void getAllMyAuthoritiesByDisplayNameIsEqualToSomething() {
        // Initialize the database
        myAuthorityRepository.save(myAuthority).block();

        // Get all the myAuthorityList where displayName equals to DEFAULT_DISPLAY_NAME
        defaultMyAuthorityShouldBeFound("displayName.equals=" + DEFAULT_DISPLAY_NAME);

        // Get all the myAuthorityList where displayName equals to UPDATED_DISPLAY_NAME
        defaultMyAuthorityShouldNotBeFound("displayName.equals=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    void getAllMyAuthoritiesByDisplayNameIsInShouldWork() {
        // Initialize the database
        myAuthorityRepository.save(myAuthority).block();

        // Get all the myAuthorityList where displayName in DEFAULT_DISPLAY_NAME or UPDATED_DISPLAY_NAME
        defaultMyAuthorityShouldBeFound("displayName.in=" + DEFAULT_DISPLAY_NAME + "," + UPDATED_DISPLAY_NAME);

        // Get all the myAuthorityList where displayName equals to UPDATED_DISPLAY_NAME
        defaultMyAuthorityShouldNotBeFound("displayName.in=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    void getAllMyAuthoritiesByDisplayNameIsNullOrNotNull() {
        // Initialize the database
        myAuthorityRepository.save(myAuthority).block();

        // Get all the myAuthorityList where displayName is not null
        defaultMyAuthorityShouldBeFound("displayName.specified=true");

        // Get all the myAuthorityList where displayName is null
        defaultMyAuthorityShouldNotBeFound("displayName.specified=false");
    }

    @Test
    void getAllMyAuthoritiesByDisplayNameContainsSomething() {
        // Initialize the database
        myAuthorityRepository.save(myAuthority).block();

        // Get all the myAuthorityList where displayName contains DEFAULT_DISPLAY_NAME
        defaultMyAuthorityShouldBeFound("displayName.contains=" + DEFAULT_DISPLAY_NAME);

        // Get all the myAuthorityList where displayName contains UPDATED_DISPLAY_NAME
        defaultMyAuthorityShouldNotBeFound("displayName.contains=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    void getAllMyAuthoritiesByDisplayNameNotContainsSomething() {
        // Initialize the database
        myAuthorityRepository.save(myAuthority).block();

        // Get all the myAuthorityList where displayName does not contain DEFAULT_DISPLAY_NAME
        defaultMyAuthorityShouldNotBeFound("displayName.doesNotContain=" + DEFAULT_DISPLAY_NAME);

        // Get all the myAuthorityList where displayName does not contain UPDATED_DISPLAY_NAME
        defaultMyAuthorityShouldBeFound("displayName.doesNotContain=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    void getAllMyAuthoritiesByParentIsEqualToSomething() {
        MyAuthority parent = MyAuthorityResourceIT.createEntity(em);
        myAuthorityRepository.save(parent).block();
        Long parentId = parent.getId();
        myAuthority.setParentId(parentId);
        myAuthorityRepository.save(myAuthority).block();
        // Get all the myAuthorityList where parent equals to parentId
        defaultMyAuthorityShouldBeFound("parentId.equals=" + parentId);

        // Get all the myAuthorityList where parent equals to (parentId + 1)
        defaultMyAuthorityShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMyAuthorityShouldBeFound(String filter) {
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
            .value(hasItem(myAuthority.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].displayName")
            .value(hasItem(DEFAULT_DISPLAY_NAME));

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
    private void defaultMyAuthorityShouldNotBeFound(String filter) {
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
    void getNonExistingMyAuthority() {
        // Get the myAuthority
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMyAuthority() throws Exception {
        // Initialize the database
        myAuthorityRepository.save(myAuthority).block();

        int databaseSizeBeforeUpdate = myAuthorityRepository.findAll().collectList().block().size();

        // Update the myAuthority
        MyAuthority updatedMyAuthority = myAuthorityRepository.findById(myAuthority.getId()).block();
        updatedMyAuthority.name(UPDATED_NAME).displayName(UPDATED_DISPLAY_NAME);
        MyAuthorityDTO myAuthorityDTO = myAuthorityMapper.toDto(updatedMyAuthority);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, myAuthorityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(myAuthorityDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MyAuthority in the database
        List<MyAuthority> myAuthorityList = myAuthorityRepository.findAll().collectList().block();
        assertThat(myAuthorityList).hasSize(databaseSizeBeforeUpdate);
        MyAuthority testMyAuthority = myAuthorityList.get(myAuthorityList.size() - 1);
        assertThat(testMyAuthority.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMyAuthority.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
    }

    @Test
    void putNonExistingMyAuthority() throws Exception {
        int databaseSizeBeforeUpdate = myAuthorityRepository.findAll().collectList().block().size();
        myAuthority.setId(longCount.incrementAndGet());

        // Create the MyAuthority
        MyAuthorityDTO myAuthorityDTO = myAuthorityMapper.toDto(myAuthority);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, myAuthorityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(myAuthorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MyAuthority in the database
        List<MyAuthority> myAuthorityList = myAuthorityRepository.findAll().collectList().block();
        assertThat(myAuthorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMyAuthority() throws Exception {
        int databaseSizeBeforeUpdate = myAuthorityRepository.findAll().collectList().block().size();
        myAuthority.setId(longCount.incrementAndGet());

        // Create the MyAuthority
        MyAuthorityDTO myAuthorityDTO = myAuthorityMapper.toDto(myAuthority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(myAuthorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MyAuthority in the database
        List<MyAuthority> myAuthorityList = myAuthorityRepository.findAll().collectList().block();
        assertThat(myAuthorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMyAuthority() throws Exception {
        int databaseSizeBeforeUpdate = myAuthorityRepository.findAll().collectList().block().size();
        myAuthority.setId(longCount.incrementAndGet());

        // Create the MyAuthority
        MyAuthorityDTO myAuthorityDTO = myAuthorityMapper.toDto(myAuthority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(myAuthorityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MyAuthority in the database
        List<MyAuthority> myAuthorityList = myAuthorityRepository.findAll().collectList().block();
        assertThat(myAuthorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMyAuthorityWithPatch() throws Exception {
        // Initialize the database
        myAuthorityRepository.save(myAuthority).block();

        int databaseSizeBeforeUpdate = myAuthorityRepository.findAll().collectList().block().size();

        // Update the myAuthority using partial update
        MyAuthority partialUpdatedMyAuthority = new MyAuthority();
        partialUpdatedMyAuthority.setId(myAuthority.getId());

        partialUpdatedMyAuthority.name(UPDATED_NAME).displayName(UPDATED_DISPLAY_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMyAuthority.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMyAuthority))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MyAuthority in the database
        List<MyAuthority> myAuthorityList = myAuthorityRepository.findAll().collectList().block();
        assertThat(myAuthorityList).hasSize(databaseSizeBeforeUpdate);
        MyAuthority testMyAuthority = myAuthorityList.get(myAuthorityList.size() - 1);
        assertThat(testMyAuthority.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMyAuthority.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
    }

    @Test
    void fullUpdateMyAuthorityWithPatch() throws Exception {
        // Initialize the database
        myAuthorityRepository.save(myAuthority).block();

        int databaseSizeBeforeUpdate = myAuthorityRepository.findAll().collectList().block().size();

        // Update the myAuthority using partial update
        MyAuthority partialUpdatedMyAuthority = new MyAuthority();
        partialUpdatedMyAuthority.setId(myAuthority.getId());

        partialUpdatedMyAuthority.name(UPDATED_NAME).displayName(UPDATED_DISPLAY_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMyAuthority.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMyAuthority))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MyAuthority in the database
        List<MyAuthority> myAuthorityList = myAuthorityRepository.findAll().collectList().block();
        assertThat(myAuthorityList).hasSize(databaseSizeBeforeUpdate);
        MyAuthority testMyAuthority = myAuthorityList.get(myAuthorityList.size() - 1);
        assertThat(testMyAuthority.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMyAuthority.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
    }

    @Test
    void patchNonExistingMyAuthority() throws Exception {
        int databaseSizeBeforeUpdate = myAuthorityRepository.findAll().collectList().block().size();
        myAuthority.setId(longCount.incrementAndGet());

        // Create the MyAuthority
        MyAuthorityDTO myAuthorityDTO = myAuthorityMapper.toDto(myAuthority);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, myAuthorityDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(myAuthorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MyAuthority in the database
        List<MyAuthority> myAuthorityList = myAuthorityRepository.findAll().collectList().block();
        assertThat(myAuthorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMyAuthority() throws Exception {
        int databaseSizeBeforeUpdate = myAuthorityRepository.findAll().collectList().block().size();
        myAuthority.setId(longCount.incrementAndGet());

        // Create the MyAuthority
        MyAuthorityDTO myAuthorityDTO = myAuthorityMapper.toDto(myAuthority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(myAuthorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MyAuthority in the database
        List<MyAuthority> myAuthorityList = myAuthorityRepository.findAll().collectList().block();
        assertThat(myAuthorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMyAuthority() throws Exception {
        int databaseSizeBeforeUpdate = myAuthorityRepository.findAll().collectList().block().size();
        myAuthority.setId(longCount.incrementAndGet());

        // Create the MyAuthority
        MyAuthorityDTO myAuthorityDTO = myAuthorityMapper.toDto(myAuthority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(myAuthorityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MyAuthority in the database
        List<MyAuthority> myAuthorityList = myAuthorityRepository.findAll().collectList().block();
        assertThat(myAuthorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMyAuthority() {
        // Initialize the database
        myAuthorityRepository.save(myAuthority).block();

        int databaseSizeBeforeDelete = myAuthorityRepository.findAll().collectList().block().size();

        // Delete the myAuthority
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, myAuthority.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<MyAuthority> myAuthorityList = myAuthorityRepository.findAll().collectList().block();
        assertThat(myAuthorityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
