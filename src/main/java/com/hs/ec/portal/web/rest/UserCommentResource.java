package com.hs.ec.portal.web.rest;

import com.hs.ec.portal.domain.criteria.UserCommentCriteria;
import com.hs.ec.portal.repository.UserCommentRepository;
import com.hs.ec.portal.service.UserCommentService;
import com.hs.ec.portal.service.dto.UserCommentDTO;
import com.hs.ec.portal.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.hs.ec.portal.domain.UserComment}.
 */
@RestController
@RequestMapping("/api/user-comments")
public class UserCommentResource {

    private final Logger log = LoggerFactory.getLogger(UserCommentResource.class);

    private static final String ENTITY_NAME = "userComment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserCommentService userCommentService;

    private final UserCommentRepository userCommentRepository;

    public UserCommentResource(UserCommentService userCommentService, UserCommentRepository userCommentRepository) {
        this.userCommentService = userCommentService;
        this.userCommentRepository = userCommentRepository;
    }

    /**
     * {@code POST  /user-comments} : Create a new userComment.
     *
     * @param userCommentDTO the userCommentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userCommentDTO, or with status {@code 400 (Bad Request)} if the userComment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UserCommentDTO>> createUserComment(@Valid @RequestBody UserCommentDTO userCommentDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserComment : {}", userCommentDTO);
        if (userCommentDTO.getId() != null) {
            throw new BadRequestAlertException("A new userComment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return userCommentService
            .save(userCommentDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/user-comments/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /user-comments/:id} : Updates an existing userComment.
     *
     * @param id the id of the userCommentDTO to save.
     * @param userCommentDTO the userCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userCommentDTO,
     * or with status {@code 400 (Bad Request)} if the userCommentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserCommentDTO>> updateUserComment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserCommentDTO userCommentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserComment : {}, {}", id, userCommentDTO);
        if (userCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userCommentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return userCommentService
                    .update(userCommentDTO)
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
     * {@code PATCH  /user-comments/:id} : Partial updates given fields of an existing userComment, field will ignore if it is null
     *
     * @param id the id of the userCommentDTO to save.
     * @param userCommentDTO the userCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userCommentDTO,
     * or with status {@code 400 (Bad Request)} if the userCommentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userCommentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UserCommentDTO>> partialUpdateUserComment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserCommentDTO userCommentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserComment partially : {}, {}", id, userCommentDTO);
        if (userCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userCommentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UserCommentDTO> result = userCommentService.partialUpdate(userCommentDTO);

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
     * {@code GET  /user-comments} : get all the userComments.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userComments in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<UserCommentDTO>>> getAllUserComments(
        UserCommentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get UserComments by criteria: {}", criteria);
        return userCommentService
            .countByCriteria(criteria)
            .zipWith(userCommentService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /user-comments/count} : count all the userComments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countUserComments(UserCommentCriteria criteria) {
        log.debug("REST request to count UserComments by criteria: {}", criteria);
        return userCommentService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /user-comments/:id} : get the "id" userComment.
     *
     * @param id the id of the userCommentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userCommentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserCommentDTO>> getUserComment(@PathVariable Long id) {
        log.debug("REST request to get UserComment : {}", id);
        Mono<UserCommentDTO> userCommentDTO = userCommentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userCommentDTO);
    }

    /**
     * {@code DELETE  /user-comments/:id} : delete the "id" userComment.
     *
     * @param id the id of the userCommentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUserComment(@PathVariable Long id) {
        log.debug("REST request to delete UserComment : {}", id);
        return userCommentService
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
