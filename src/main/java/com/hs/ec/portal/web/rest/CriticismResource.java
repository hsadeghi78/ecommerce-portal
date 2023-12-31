package com.hs.ec.portal.web.rest;

import com.hs.ec.portal.domain.criteria.CriticismCriteria;
import com.hs.ec.portal.repository.CriticismRepository;
import com.hs.ec.portal.service.CriticismService;
import com.hs.ec.portal.service.dto.CriticismDTO;
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
 * REST controller for managing {@link com.hs.ec.portal.domain.Criticism}.
 */
@RestController
@RequestMapping("/api/criticisms")
public class CriticismResource {

    private final Logger log = LoggerFactory.getLogger(CriticismResource.class);

    private static final String ENTITY_NAME = "criticism";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CriticismService criticismService;

    private final CriticismRepository criticismRepository;

    public CriticismResource(CriticismService criticismService, CriticismRepository criticismRepository) {
        this.criticismService = criticismService;
        this.criticismRepository = criticismRepository;
    }

    /**
     * {@code POST  /criticisms} : Create a new criticism.
     *
     * @param criticismDTO the criticismDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new criticismDTO, or with status {@code 400 (Bad Request)} if the criticism has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<CriticismDTO>> createCriticism(@Valid @RequestBody CriticismDTO criticismDTO) throws URISyntaxException {
        log.debug("REST request to save Criticism : {}", criticismDTO);
        if (criticismDTO.getId() != null) {
            throw new BadRequestAlertException("A new criticism cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return criticismService
            .save(criticismDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/criticisms/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /criticisms/:id} : Updates an existing criticism.
     *
     * @param id the id of the criticismDTO to save.
     * @param criticismDTO the criticismDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated criticismDTO,
     * or with status {@code 400 (Bad Request)} if the criticismDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the criticismDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CriticismDTO>> updateCriticism(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CriticismDTO criticismDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Criticism : {}, {}", id, criticismDTO);
        if (criticismDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, criticismDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return criticismRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return criticismService
                    .update(criticismDTO)
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
     * {@code PATCH  /criticisms/:id} : Partial updates given fields of an existing criticism, field will ignore if it is null
     *
     * @param id the id of the criticismDTO to save.
     * @param criticismDTO the criticismDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated criticismDTO,
     * or with status {@code 400 (Bad Request)} if the criticismDTO is not valid,
     * or with status {@code 404 (Not Found)} if the criticismDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the criticismDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CriticismDTO>> partialUpdateCriticism(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CriticismDTO criticismDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Criticism partially : {}, {}", id, criticismDTO);
        if (criticismDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, criticismDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return criticismRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CriticismDTO> result = criticismService.partialUpdate(criticismDTO);

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
     * {@code GET  /criticisms} : get all the criticisms.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of criticisms in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<CriticismDTO>>> getAllCriticisms(
        CriticismCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get Criticisms by criteria: {}", criteria);
        return criticismService
            .countByCriteria(criteria)
            .zipWith(criticismService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /criticisms/count} : count all the criticisms.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countCriticisms(CriticismCriteria criteria) {
        log.debug("REST request to count Criticisms by criteria: {}", criteria);
        return criticismService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /criticisms/:id} : get the "id" criticism.
     *
     * @param id the id of the criticismDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the criticismDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CriticismDTO>> getCriticism(@PathVariable Long id) {
        log.debug("REST request to get Criticism : {}", id);
        Mono<CriticismDTO> criticismDTO = criticismService.findOne(id);
        return ResponseUtil.wrapOrNotFound(criticismDTO);
    }

    /**
     * {@code DELETE  /criticisms/:id} : delete the "id" criticism.
     *
     * @param id the id of the criticismDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCriticism(@PathVariable Long id) {
        log.debug("REST request to delete Criticism : {}", id);
        return criticismService
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
