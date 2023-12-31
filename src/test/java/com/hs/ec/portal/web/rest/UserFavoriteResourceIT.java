package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.Product;
import com.hs.ec.portal.domain.UserFavorite;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.repository.ProductRepository;
import com.hs.ec.portal.repository.UserFavoriteRepository;
import com.hs.ec.portal.service.UserFavoriteService;
import com.hs.ec.portal.service.dto.UserFavoriteDTO;
import com.hs.ec.portal.service.mapper.UserFavoriteMapper;
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
 * Integration tests for the {@link UserFavoriteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserFavoriteResourceIT {

    private static final String ENTITY_API_URL = "/api/user-favorites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserFavoriteRepository userFavoriteRepository;

    @Mock
    private UserFavoriteRepository userFavoriteRepositoryMock;

    @Autowired
    private UserFavoriteMapper userFavoriteMapper;

    @Mock
    private UserFavoriteService userFavoriteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UserFavorite userFavorite;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserFavorite createEntity(EntityManager em) {
        UserFavorite userFavorite = new UserFavorite();
        return userFavorite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserFavorite createUpdatedEntity(EntityManager em) {
        UserFavorite userFavorite = new UserFavorite();
        return userFavorite;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserFavorite.class).block();
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
        userFavorite = createEntity(em);
    }

    @Test
    void createUserFavorite() throws Exception {
        int databaseSizeBeforeCreate = userFavoriteRepository.findAll().collectList().block().size();
        // Create the UserFavorite
        UserFavoriteDTO userFavoriteDTO = userFavoriteMapper.toDto(userFavorite);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userFavoriteDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UserFavorite in the database
        List<UserFavorite> userFavoriteList = userFavoriteRepository.findAll().collectList().block();
        assertThat(userFavoriteList).hasSize(databaseSizeBeforeCreate + 1);
        UserFavorite testUserFavorite = userFavoriteList.get(userFavoriteList.size() - 1);
    }

    @Test
    void createUserFavoriteWithExistingId() throws Exception {
        // Create the UserFavorite with an existing ID
        userFavorite.setId(1L);
        UserFavoriteDTO userFavoriteDTO = userFavoriteMapper.toDto(userFavorite);

        int databaseSizeBeforeCreate = userFavoriteRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userFavoriteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserFavorite in the database
        List<UserFavorite> userFavoriteList = userFavoriteRepository.findAll().collectList().block();
        assertThat(userFavoriteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllUserFavorites() {
        // Initialize the database
        userFavoriteRepository.save(userFavorite).block();

        // Get all the userFavoriteList
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
            .value(hasItem(userFavorite.getId().intValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserFavoritesWithEagerRelationshipsIsEnabled() {
        when(userFavoriteServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(userFavoriteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserFavoritesWithEagerRelationshipsIsNotEnabled() {
        when(userFavoriteServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(userFavoriteRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getUserFavorite() {
        // Initialize the database
        userFavoriteRepository.save(userFavorite).block();

        // Get the userFavorite
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userFavorite.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userFavorite.getId().intValue()));
    }

    @Test
    void getUserFavoritesByIdFiltering() {
        // Initialize the database
        userFavoriteRepository.save(userFavorite).block();

        Long id = userFavorite.getId();

        defaultUserFavoriteShouldBeFound("id.equals=" + id);
        defaultUserFavoriteShouldNotBeFound("id.notEquals=" + id);

        defaultUserFavoriteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserFavoriteShouldNotBeFound("id.greaterThan=" + id);

        defaultUserFavoriteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserFavoriteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllUserFavoritesByProductIsEqualToSomething() {
        Product product = ProductResourceIT.createEntity(em);
        productRepository.save(product).block();
        Long productId = product.getId();
        userFavorite.setProductId(productId);
        userFavoriteRepository.save(userFavorite).block();
        // Get all the userFavoriteList where product equals to productId
        defaultUserFavoriteShouldBeFound("productId.equals=" + productId);

        // Get all the userFavoriteList where product equals to (productId + 1)
        defaultUserFavoriteShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserFavoriteShouldBeFound(String filter) {
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
            .value(hasItem(userFavorite.getId().intValue()));

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
    private void defaultUserFavoriteShouldNotBeFound(String filter) {
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
    void getNonExistingUserFavorite() {
        // Get the userFavorite
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserFavorite() throws Exception {
        // Initialize the database
        userFavoriteRepository.save(userFavorite).block();

        int databaseSizeBeforeUpdate = userFavoriteRepository.findAll().collectList().block().size();

        // Update the userFavorite
        UserFavorite updatedUserFavorite = userFavoriteRepository.findById(userFavorite.getId()).block();
        UserFavoriteDTO userFavoriteDTO = userFavoriteMapper.toDto(updatedUserFavorite);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userFavoriteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userFavoriteDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserFavorite in the database
        List<UserFavorite> userFavoriteList = userFavoriteRepository.findAll().collectList().block();
        assertThat(userFavoriteList).hasSize(databaseSizeBeforeUpdate);
        UserFavorite testUserFavorite = userFavoriteList.get(userFavoriteList.size() - 1);
    }

    @Test
    void putNonExistingUserFavorite() throws Exception {
        int databaseSizeBeforeUpdate = userFavoriteRepository.findAll().collectList().block().size();
        userFavorite.setId(longCount.incrementAndGet());

        // Create the UserFavorite
        UserFavoriteDTO userFavoriteDTO = userFavoriteMapper.toDto(userFavorite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userFavoriteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userFavoriteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserFavorite in the database
        List<UserFavorite> userFavoriteList = userFavoriteRepository.findAll().collectList().block();
        assertThat(userFavoriteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUserFavorite() throws Exception {
        int databaseSizeBeforeUpdate = userFavoriteRepository.findAll().collectList().block().size();
        userFavorite.setId(longCount.incrementAndGet());

        // Create the UserFavorite
        UserFavoriteDTO userFavoriteDTO = userFavoriteMapper.toDto(userFavorite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userFavoriteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserFavorite in the database
        List<UserFavorite> userFavoriteList = userFavoriteRepository.findAll().collectList().block();
        assertThat(userFavoriteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUserFavorite() throws Exception {
        int databaseSizeBeforeUpdate = userFavoriteRepository.findAll().collectList().block().size();
        userFavorite.setId(longCount.incrementAndGet());

        // Create the UserFavorite
        UserFavoriteDTO userFavoriteDTO = userFavoriteMapper.toDto(userFavorite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userFavoriteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserFavorite in the database
        List<UserFavorite> userFavoriteList = userFavoriteRepository.findAll().collectList().block();
        assertThat(userFavoriteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUserFavoriteWithPatch() throws Exception {
        // Initialize the database
        userFavoriteRepository.save(userFavorite).block();

        int databaseSizeBeforeUpdate = userFavoriteRepository.findAll().collectList().block().size();

        // Update the userFavorite using partial update
        UserFavorite partialUpdatedUserFavorite = new UserFavorite();
        partialUpdatedUserFavorite.setId(userFavorite.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserFavorite.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserFavorite))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserFavorite in the database
        List<UserFavorite> userFavoriteList = userFavoriteRepository.findAll().collectList().block();
        assertThat(userFavoriteList).hasSize(databaseSizeBeforeUpdate);
        UserFavorite testUserFavorite = userFavoriteList.get(userFavoriteList.size() - 1);
    }

    @Test
    void fullUpdateUserFavoriteWithPatch() throws Exception {
        // Initialize the database
        userFavoriteRepository.save(userFavorite).block();

        int databaseSizeBeforeUpdate = userFavoriteRepository.findAll().collectList().block().size();

        // Update the userFavorite using partial update
        UserFavorite partialUpdatedUserFavorite = new UserFavorite();
        partialUpdatedUserFavorite.setId(userFavorite.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserFavorite.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserFavorite))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserFavorite in the database
        List<UserFavorite> userFavoriteList = userFavoriteRepository.findAll().collectList().block();
        assertThat(userFavoriteList).hasSize(databaseSizeBeforeUpdate);
        UserFavorite testUserFavorite = userFavoriteList.get(userFavoriteList.size() - 1);
    }

    @Test
    void patchNonExistingUserFavorite() throws Exception {
        int databaseSizeBeforeUpdate = userFavoriteRepository.findAll().collectList().block().size();
        userFavorite.setId(longCount.incrementAndGet());

        // Create the UserFavorite
        UserFavoriteDTO userFavoriteDTO = userFavoriteMapper.toDto(userFavorite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userFavoriteDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userFavoriteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserFavorite in the database
        List<UserFavorite> userFavoriteList = userFavoriteRepository.findAll().collectList().block();
        assertThat(userFavoriteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUserFavorite() throws Exception {
        int databaseSizeBeforeUpdate = userFavoriteRepository.findAll().collectList().block().size();
        userFavorite.setId(longCount.incrementAndGet());

        // Create the UserFavorite
        UserFavoriteDTO userFavoriteDTO = userFavoriteMapper.toDto(userFavorite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userFavoriteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserFavorite in the database
        List<UserFavorite> userFavoriteList = userFavoriteRepository.findAll().collectList().block();
        assertThat(userFavoriteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUserFavorite() throws Exception {
        int databaseSizeBeforeUpdate = userFavoriteRepository.findAll().collectList().block().size();
        userFavorite.setId(longCount.incrementAndGet());

        // Create the UserFavorite
        UserFavoriteDTO userFavoriteDTO = userFavoriteMapper.toDto(userFavorite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userFavoriteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserFavorite in the database
        List<UserFavorite> userFavoriteList = userFavoriteRepository.findAll().collectList().block();
        assertThat(userFavoriteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUserFavorite() {
        // Initialize the database
        userFavoriteRepository.save(userFavorite).block();

        int databaseSizeBeforeDelete = userFavoriteRepository.findAll().collectList().block().size();

        // Delete the userFavorite
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userFavorite.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UserFavorite> userFavoriteList = userFavoriteRepository.findAll().collectList().block();
        assertThat(userFavoriteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
