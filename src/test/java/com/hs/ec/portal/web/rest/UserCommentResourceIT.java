package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.Factor;
import com.hs.ec.portal.domain.Party;
import com.hs.ec.portal.domain.Product;
import com.hs.ec.portal.domain.UserComment;
import com.hs.ec.portal.domain.UserComment;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.repository.FactorRepository;
import com.hs.ec.portal.repository.PartyRepository;
import com.hs.ec.portal.repository.ProductRepository;
import com.hs.ec.portal.repository.UserCommentRepository;
import com.hs.ec.portal.repository.UserCommentRepository;
import com.hs.ec.portal.service.UserCommentService;
import com.hs.ec.portal.service.dto.UserCommentDTO;
import com.hs.ec.portal.service.mapper.UserCommentMapper;
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
 * Integration tests for the {@link UserCommentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserCommentResourceIT {

    private static final Float DEFAULT_RATING = 1F;
    private static final Float UPDATED_RATING = 2F;
    private static final Float SMALLER_RATING = 1F - 1F;

    private static final Boolean DEFAULT_VISIBLE = false;
    private static final Boolean UPDATED_VISIBLE = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserCommentRepository userCommentRepository;

    @Mock
    private UserCommentRepository userCommentRepositoryMock;

    @Autowired
    private UserCommentMapper userCommentMapper;

    @Mock
    private UserCommentService userCommentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UserComment userComment;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FactorRepository factorRepository;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserComment createEntity(EntityManager em) {
        UserComment userComment = new UserComment().rating(DEFAULT_RATING).visible(DEFAULT_VISIBLE).description(DEFAULT_DESCRIPTION);
        return userComment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserComment createUpdatedEntity(EntityManager em) {
        UserComment userComment = new UserComment().rating(UPDATED_RATING).visible(UPDATED_VISIBLE).description(UPDATED_DESCRIPTION);
        return userComment;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserComment.class).block();
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
        userComment = createEntity(em);
    }

    @Test
    void createUserComment() throws Exception {
        int databaseSizeBeforeCreate = userCommentRepository.findAll().collectList().block().size();
        // Create the UserComment
        UserCommentDTO userCommentDTO = userCommentMapper.toDto(userComment);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userCommentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll().collectList().block();
        assertThat(userCommentList).hasSize(databaseSizeBeforeCreate + 1);
        UserComment testUserComment = userCommentList.get(userCommentList.size() - 1);
        assertThat(testUserComment.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testUserComment.getVisible()).isEqualTo(DEFAULT_VISIBLE);
        assertThat(testUserComment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createUserCommentWithExistingId() throws Exception {
        // Create the UserComment with an existing ID
        userComment.setId(1L);
        UserCommentDTO userCommentDTO = userCommentMapper.toDto(userComment);

        int databaseSizeBeforeCreate = userCommentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userCommentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll().collectList().block();
        assertThat(userCommentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkRatingIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCommentRepository.findAll().collectList().block().size();
        // set the field null
        userComment.setRating(null);

        // Create the UserComment, which fails.
        UserCommentDTO userCommentDTO = userCommentMapper.toDto(userComment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userCommentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserComment> userCommentList = userCommentRepository.findAll().collectList().block();
        assertThat(userCommentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkVisibleIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCommentRepository.findAll().collectList().block().size();
        // set the field null
        userComment.setVisible(null);

        // Create the UserComment, which fails.
        UserCommentDTO userCommentDTO = userCommentMapper.toDto(userComment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userCommentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserComment> userCommentList = userCommentRepository.findAll().collectList().block();
        assertThat(userCommentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllUserComments() {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        // Get all the userCommentList
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
            .value(hasItem(userComment.getId().intValue()))
            .jsonPath("$.[*].rating")
            .value(hasItem(DEFAULT_RATING.doubleValue()))
            .jsonPath("$.[*].visible")
            .value(hasItem(DEFAULT_VISIBLE.booleanValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserCommentsWithEagerRelationshipsIsEnabled() {
        when(userCommentServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(userCommentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserCommentsWithEagerRelationshipsIsNotEnabled() {
        when(userCommentServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(userCommentRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getUserComment() {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        // Get the userComment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userComment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userComment.getId().intValue()))
            .jsonPath("$.rating")
            .value(is(DEFAULT_RATING.doubleValue()))
            .jsonPath("$.visible")
            .value(is(DEFAULT_VISIBLE.booleanValue()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getUserCommentsByIdFiltering() {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        Long id = userComment.getId();

        defaultUserCommentShouldBeFound("id.equals=" + id);
        defaultUserCommentShouldNotBeFound("id.notEquals=" + id);

        defaultUserCommentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserCommentShouldNotBeFound("id.greaterThan=" + id);

        defaultUserCommentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserCommentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllUserCommentsByRatingIsEqualToSomething() {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        // Get all the userCommentList where rating equals to DEFAULT_RATING
        defaultUserCommentShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the userCommentList where rating equals to UPDATED_RATING
        defaultUserCommentShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    void getAllUserCommentsByRatingIsInShouldWork() {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        // Get all the userCommentList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultUserCommentShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the userCommentList where rating equals to UPDATED_RATING
        defaultUserCommentShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    void getAllUserCommentsByRatingIsNullOrNotNull() {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        // Get all the userCommentList where rating is not null
        defaultUserCommentShouldBeFound("rating.specified=true");

        // Get all the userCommentList where rating is null
        defaultUserCommentShouldNotBeFound("rating.specified=false");
    }

    @Test
    void getAllUserCommentsByRatingIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        // Get all the userCommentList where rating is greater than or equal to DEFAULT_RATING
        defaultUserCommentShouldBeFound("rating.greaterThanOrEqual=" + DEFAULT_RATING);

        // Get all the userCommentList where rating is greater than or equal to UPDATED_RATING
        defaultUserCommentShouldNotBeFound("rating.greaterThanOrEqual=" + UPDATED_RATING);
    }

    @Test
    void getAllUserCommentsByRatingIsLessThanOrEqualToSomething() {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        // Get all the userCommentList where rating is less than or equal to DEFAULT_RATING
        defaultUserCommentShouldBeFound("rating.lessThanOrEqual=" + DEFAULT_RATING);

        // Get all the userCommentList where rating is less than or equal to SMALLER_RATING
        defaultUserCommentShouldNotBeFound("rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    void getAllUserCommentsByRatingIsLessThanSomething() {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        // Get all the userCommentList where rating is less than DEFAULT_RATING
        defaultUserCommentShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the userCommentList where rating is less than UPDATED_RATING
        defaultUserCommentShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }

    @Test
    void getAllUserCommentsByRatingIsGreaterThanSomething() {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        // Get all the userCommentList where rating is greater than DEFAULT_RATING
        defaultUserCommentShouldNotBeFound("rating.greaterThan=" + DEFAULT_RATING);

        // Get all the userCommentList where rating is greater than SMALLER_RATING
        defaultUserCommentShouldBeFound("rating.greaterThan=" + SMALLER_RATING);
    }

    @Test
    void getAllUserCommentsByVisibleIsEqualToSomething() {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        // Get all the userCommentList where visible equals to DEFAULT_VISIBLE
        defaultUserCommentShouldBeFound("visible.equals=" + DEFAULT_VISIBLE);

        // Get all the userCommentList where visible equals to UPDATED_VISIBLE
        defaultUserCommentShouldNotBeFound("visible.equals=" + UPDATED_VISIBLE);
    }

    @Test
    void getAllUserCommentsByVisibleIsInShouldWork() {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        // Get all the userCommentList where visible in DEFAULT_VISIBLE or UPDATED_VISIBLE
        defaultUserCommentShouldBeFound("visible.in=" + DEFAULT_VISIBLE + "," + UPDATED_VISIBLE);

        // Get all the userCommentList where visible equals to UPDATED_VISIBLE
        defaultUserCommentShouldNotBeFound("visible.in=" + UPDATED_VISIBLE);
    }

    @Test
    void getAllUserCommentsByVisibleIsNullOrNotNull() {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        // Get all the userCommentList where visible is not null
        defaultUserCommentShouldBeFound("visible.specified=true");

        // Get all the userCommentList where visible is null
        defaultUserCommentShouldNotBeFound("visible.specified=false");
    }

    @Test
    void getAllUserCommentsByDescriptionIsEqualToSomething() {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        // Get all the userCommentList where description equals to DEFAULT_DESCRIPTION
        defaultUserCommentShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the userCommentList where description equals to UPDATED_DESCRIPTION
        defaultUserCommentShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllUserCommentsByDescriptionIsInShouldWork() {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        // Get all the userCommentList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultUserCommentShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the userCommentList where description equals to UPDATED_DESCRIPTION
        defaultUserCommentShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllUserCommentsByDescriptionIsNullOrNotNull() {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        // Get all the userCommentList where description is not null
        defaultUserCommentShouldBeFound("description.specified=true");

        // Get all the userCommentList where description is null
        defaultUserCommentShouldNotBeFound("description.specified=false");
    }

    @Test
    void getAllUserCommentsByDescriptionContainsSomething() {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        // Get all the userCommentList where description contains DEFAULT_DESCRIPTION
        defaultUserCommentShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the userCommentList where description contains UPDATED_DESCRIPTION
        defaultUserCommentShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllUserCommentsByDescriptionNotContainsSomething() {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        // Get all the userCommentList where description does not contain DEFAULT_DESCRIPTION
        defaultUserCommentShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the userCommentList where description does not contain UPDATED_DESCRIPTION
        defaultUserCommentShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllUserCommentsByPartyIsEqualToSomething() {
        Party party = PartyResourceIT.createEntity(em);
        partyRepository.save(party).block();
        Long partyId = party.getId();
        userComment.setPartyId(partyId);
        userCommentRepository.save(userComment).block();
        // Get all the userCommentList where party equals to partyId
        defaultUserCommentShouldBeFound("partyId.equals=" + partyId);

        // Get all the userCommentList where party equals to (partyId + 1)
        defaultUserCommentShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    void getAllUserCommentsByProductIsEqualToSomething() {
        Product product = ProductResourceIT.createEntity(em);
        productRepository.save(product).block();
        Long productId = product.getId();
        userComment.setProductId(productId);
        userCommentRepository.save(userComment).block();
        // Get all the userCommentList where product equals to productId
        defaultUserCommentShouldBeFound("productId.equals=" + productId);

        // Get all the userCommentList where product equals to (productId + 1)
        defaultUserCommentShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    void getAllUserCommentsByFactorIsEqualToSomething() {
        Factor factor = FactorResourceIT.createEntity(em);
        factorRepository.save(factor).block();
        Long factorId = factor.getId();
        userComment.setFactorId(factorId);
        userCommentRepository.save(userComment).block();
        // Get all the userCommentList where factor equals to factorId
        defaultUserCommentShouldBeFound("factorId.equals=" + factorId);

        // Get all the userCommentList where factor equals to (factorId + 1)
        defaultUserCommentShouldNotBeFound("factorId.equals=" + (factorId + 1));
    }

    @Test
    void getAllUserCommentsByParentIsEqualToSomething() {
        UserComment parent = UserCommentResourceIT.createEntity(em);
        userCommentRepository.save(parent).block();
        Long parentId = parent.getId();
        userComment.setParentId(parentId);
        userCommentRepository.save(userComment).block();
        // Get all the userCommentList where parent equals to parentId
        defaultUserCommentShouldBeFound("parentId.equals=" + parentId);

        // Get all the userCommentList where parent equals to (parentId + 1)
        defaultUserCommentShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserCommentShouldBeFound(String filter) {
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
            .value(hasItem(userComment.getId().intValue()))
            .jsonPath("$.[*].rating")
            .value(hasItem(DEFAULT_RATING.doubleValue()))
            .jsonPath("$.[*].visible")
            .value(hasItem(DEFAULT_VISIBLE.booleanValue()))
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
    private void defaultUserCommentShouldNotBeFound(String filter) {
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
    void getNonExistingUserComment() {
        // Get the userComment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserComment() throws Exception {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        int databaseSizeBeforeUpdate = userCommentRepository.findAll().collectList().block().size();

        // Update the userComment
        UserComment updatedUserComment = userCommentRepository.findById(userComment.getId()).block();
        updatedUserComment.rating(UPDATED_RATING).visible(UPDATED_VISIBLE).description(UPDATED_DESCRIPTION);
        UserCommentDTO userCommentDTO = userCommentMapper.toDto(updatedUserComment);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userCommentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userCommentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll().collectList().block();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
        UserComment testUserComment = userCommentList.get(userCommentList.size() - 1);
        assertThat(testUserComment.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testUserComment.getVisible()).isEqualTo(UPDATED_VISIBLE);
        assertThat(testUserComment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingUserComment() throws Exception {
        int databaseSizeBeforeUpdate = userCommentRepository.findAll().collectList().block().size();
        userComment.setId(longCount.incrementAndGet());

        // Create the UserComment
        UserCommentDTO userCommentDTO = userCommentMapper.toDto(userComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userCommentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userCommentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll().collectList().block();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUserComment() throws Exception {
        int databaseSizeBeforeUpdate = userCommentRepository.findAll().collectList().block().size();
        userComment.setId(longCount.incrementAndGet());

        // Create the UserComment
        UserCommentDTO userCommentDTO = userCommentMapper.toDto(userComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userCommentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll().collectList().block();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUserComment() throws Exception {
        int databaseSizeBeforeUpdate = userCommentRepository.findAll().collectList().block().size();
        userComment.setId(longCount.incrementAndGet());

        // Create the UserComment
        UserCommentDTO userCommentDTO = userCommentMapper.toDto(userComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userCommentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll().collectList().block();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUserCommentWithPatch() throws Exception {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        int databaseSizeBeforeUpdate = userCommentRepository.findAll().collectList().block().size();

        // Update the userComment using partial update
        UserComment partialUpdatedUserComment = new UserComment();
        partialUpdatedUserComment.setId(userComment.getId());

        partialUpdatedUserComment.description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserComment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserComment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll().collectList().block();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
        UserComment testUserComment = userCommentList.get(userCommentList.size() - 1);
        assertThat(testUserComment.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testUserComment.getVisible()).isEqualTo(DEFAULT_VISIBLE);
        assertThat(testUserComment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void fullUpdateUserCommentWithPatch() throws Exception {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        int databaseSizeBeforeUpdate = userCommentRepository.findAll().collectList().block().size();

        // Update the userComment using partial update
        UserComment partialUpdatedUserComment = new UserComment();
        partialUpdatedUserComment.setId(userComment.getId());

        partialUpdatedUserComment.rating(UPDATED_RATING).visible(UPDATED_VISIBLE).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserComment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserComment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll().collectList().block();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
        UserComment testUserComment = userCommentList.get(userCommentList.size() - 1);
        assertThat(testUserComment.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testUserComment.getVisible()).isEqualTo(UPDATED_VISIBLE);
        assertThat(testUserComment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingUserComment() throws Exception {
        int databaseSizeBeforeUpdate = userCommentRepository.findAll().collectList().block().size();
        userComment.setId(longCount.incrementAndGet());

        // Create the UserComment
        UserCommentDTO userCommentDTO = userCommentMapper.toDto(userComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userCommentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userCommentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll().collectList().block();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUserComment() throws Exception {
        int databaseSizeBeforeUpdate = userCommentRepository.findAll().collectList().block().size();
        userComment.setId(longCount.incrementAndGet());

        // Create the UserComment
        UserCommentDTO userCommentDTO = userCommentMapper.toDto(userComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userCommentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll().collectList().block();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUserComment() throws Exception {
        int databaseSizeBeforeUpdate = userCommentRepository.findAll().collectList().block().size();
        userComment.setId(longCount.incrementAndGet());

        // Create the UserComment
        UserCommentDTO userCommentDTO = userCommentMapper.toDto(userComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userCommentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll().collectList().block();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUserComment() {
        // Initialize the database
        userCommentRepository.save(userComment).block();

        int databaseSizeBeforeDelete = userCommentRepository.findAll().collectList().block().size();

        // Delete the userComment
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userComment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UserComment> userCommentList = userCommentRepository.findAll().collectList().block();
        assertThat(userCommentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
