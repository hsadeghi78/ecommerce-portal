package com.hs.ec.portal.web.rest;

import com.hs.ec.portal.domain.criteria.UserFavoriteCriteria;
import com.hs.ec.portal.repository.UserFavoriteRepository;
import com.hs.ec.portal.service.UserFavoriteService;
import com.hs.ec.portal.service.dto.UserFavoriteDTO;
import com.hs.ec.portal.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.hs.ec.portal.domain.UserFavorite}.
 */
@RestController
@RequestMapping("/api/user-favorites")
public class UserFavoriteResource {

    private final Logger log = LoggerFactory.getLogger(UserFavoriteResource.class);

    private static final String ENTITY_NAME = "userFavorite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserFavoriteService userFavoriteService;

    private final UserFavoriteRepository userFavoriteRepository;

    public UserFavoriteResource(UserFavoriteService userFavoriteService, UserFavoriteRepository userFavoriteRepository) {
        this.userFavoriteService = userFavoriteService;
        this.userFavoriteRepository = userFavoriteRepository;
    }

    /**
     * {@code POST  /user-favorites} : Create a new userFavorite.
     *
     * @param userFavoriteDTO the userFavoriteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userFavoriteDTO, or with status {@code 400 (Bad Request)} if the userFavorite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UserFavoriteDTO>> createUserFavorite(@RequestBody UserFavoriteDTO userFavoriteDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserFavorite : {}", userFavoriteDTO);
        if (userFavoriteDTO.getId() != null) {
            throw new BadRequestAlertException("A new userFavorite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return userFavoriteService
            .save(userFavoriteDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/user-favorites/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /user-favorites/:id} : Updates an existing userFavorite.
     *
     * @param id the id of the userFavoriteDTO to save.
     * @param userFavoriteDTO the userFavoriteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userFavoriteDTO,
     * or with status {@code 400 (Bad Request)} if the userFavoriteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userFavoriteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserFavoriteDTO>> updateUserFavorite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserFavoriteDTO userFavoriteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserFavorite : {}, {}", id, userFavoriteDTO);
        if (userFavoriteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userFavoriteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userFavoriteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return userFavoriteService
                    .update(userFavoriteDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /user-favorites/:id} : Partial updates given fields of an existing userFavorite, field will ignore if it is null
     *
     * @param id the id of the userFavoriteDTO to save.
     * @param userFavoriteDTO the userFavoriteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userFavoriteDTO,
     * or with status {@code 400 (Bad Request)} if the userFavoriteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userFavoriteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userFavoriteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UserFavoriteDTO>> partialUpdateUserFavorite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserFavoriteDTO userFavoriteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserFavorite partially : {}, {}", id, userFavoriteDTO);
        if (userFavoriteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userFavoriteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userFavoriteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UserFavoriteDTO> result = userFavoriteService.partialUpdate(userFavoriteDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /user-favorites} : get all the userFavorites.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userFavorites in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<UserFavoriteDTO>>> getAllUserFavorites(
        UserFavoriteCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get UserFavorites by criteria: {}", criteria);
        return userFavoriteService
            .countByCriteria(criteria)
            .zipWith(userFavoriteService.findByCriteria(criteria, pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /user-favorites/count} : count all the userFavorites.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countUserFavorites(UserFavoriteCriteria criteria) {
        log.debug("REST request to count UserFavorites by criteria: {}", criteria);
        return userFavoriteService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /user-favorites/:id} : get the "id" userFavorite.
     *
     * @param id the id of the userFavoriteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userFavoriteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserFavoriteDTO>> getUserFavorite(@PathVariable Long id) {
        log.debug("REST request to get UserFavorite : {}", id);
        Mono<UserFavoriteDTO> userFavoriteDTO = userFavoriteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userFavoriteDTO);
    }

    /**
     * {@code DELETE  /user-favorites/:id} : delete the "id" userFavorite.
     *
     * @param id the id of the userFavoriteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUserFavorite(@PathVariable Long id) {
        log.debug("REST request to delete UserFavorite : {}", id);
        return userFavoriteService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
