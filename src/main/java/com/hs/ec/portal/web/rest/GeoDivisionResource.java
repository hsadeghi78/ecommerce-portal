package com.hs.ec.portal.web.rest;

import com.hs.ec.portal.domain.criteria.GeoDivisionCriteria;
import com.hs.ec.portal.repository.GeoDivisionRepository;
import com.hs.ec.portal.service.GeoDivisionService;
import com.hs.ec.portal.service.dto.GeoDivisionDTO;
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
 * REST controller for managing {@link com.hs.ec.portal.domain.GeoDivision}.
 */
@RestController
@RequestMapping("/api/geo-divisions")
public class GeoDivisionResource {

    private final Logger log = LoggerFactory.getLogger(GeoDivisionResource.class);

    private static final String ENTITY_NAME = "geoDivision";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GeoDivisionService geoDivisionService;

    private final GeoDivisionRepository geoDivisionRepository;

    public GeoDivisionResource(GeoDivisionService geoDivisionService, GeoDivisionRepository geoDivisionRepository) {
        this.geoDivisionService = geoDivisionService;
        this.geoDivisionRepository = geoDivisionRepository;
    }

    /**
     * {@code POST  /geo-divisions} : Create a new geoDivision.
     *
     * @param geoDivisionDTO the geoDivisionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new geoDivisionDTO, or with status {@code 400 (Bad Request)} if the geoDivision has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<GeoDivisionDTO>> createGeoDivision(@Valid @RequestBody GeoDivisionDTO geoDivisionDTO)
        throws URISyntaxException {
        log.debug("REST request to save GeoDivision : {}", geoDivisionDTO);
        if (geoDivisionDTO.getId() != null) {
            throw new BadRequestAlertException("A new geoDivision cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return geoDivisionService
            .save(geoDivisionDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/geo-divisions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /geo-divisions/:id} : Updates an existing geoDivision.
     *
     * @param id the id of the geoDivisionDTO to save.
     * @param geoDivisionDTO the geoDivisionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated geoDivisionDTO,
     * or with status {@code 400 (Bad Request)} if the geoDivisionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the geoDivisionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<GeoDivisionDTO>> updateGeoDivision(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GeoDivisionDTO geoDivisionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GeoDivision : {}, {}", id, geoDivisionDTO);
        if (geoDivisionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, geoDivisionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return geoDivisionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return geoDivisionService
                    .update(geoDivisionDTO)
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
     * {@code PATCH  /geo-divisions/:id} : Partial updates given fields of an existing geoDivision, field will ignore if it is null
     *
     * @param id the id of the geoDivisionDTO to save.
     * @param geoDivisionDTO the geoDivisionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated geoDivisionDTO,
     * or with status {@code 400 (Bad Request)} if the geoDivisionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the geoDivisionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the geoDivisionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<GeoDivisionDTO>> partialUpdateGeoDivision(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GeoDivisionDTO geoDivisionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GeoDivision partially : {}, {}", id, geoDivisionDTO);
        if (geoDivisionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, geoDivisionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return geoDivisionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<GeoDivisionDTO> result = geoDivisionService.partialUpdate(geoDivisionDTO);

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
     * {@code GET  /geo-divisions} : get all the geoDivisions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of geoDivisions in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<GeoDivisionDTO>>> getAllGeoDivisions(
        GeoDivisionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get GeoDivisions by criteria: {}", criteria);
        return geoDivisionService
            .countByCriteria(criteria)
            .zipWith(geoDivisionService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /geo-divisions/count} : count all the geoDivisions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countGeoDivisions(GeoDivisionCriteria criteria) {
        log.debug("REST request to count GeoDivisions by criteria: {}", criteria);
        return geoDivisionService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /geo-divisions/:id} : get the "id" geoDivision.
     *
     * @param id the id of the geoDivisionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the geoDivisionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<GeoDivisionDTO>> getGeoDivision(@PathVariable Long id) {
        log.debug("REST request to get GeoDivision : {}", id);
        Mono<GeoDivisionDTO> geoDivisionDTO = geoDivisionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(geoDivisionDTO);
    }

    /**
     * {@code DELETE  /geo-divisions/:id} : delete the "id" geoDivision.
     *
     * @param id the id of the geoDivisionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteGeoDivision(@PathVariable Long id) {
        log.debug("REST request to delete GeoDivision : {}", id);
        return geoDivisionService
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
