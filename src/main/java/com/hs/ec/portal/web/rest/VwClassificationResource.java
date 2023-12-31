package com.hs.ec.portal.web.rest;

import com.hs.ec.portal.domain.criteria.VwClassificationCriteria;
import com.hs.ec.portal.repository.VwClassificationRepository;
import com.hs.ec.portal.service.VwClassificationService;
import com.hs.ec.portal.service.dto.VwClassificationDTO;
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
 * REST controller for managing {@link com.hs.ec.portal.domain.VwClassification}.
 */
@RestController
@RequestMapping("/api/vw-classifications")
public class VwClassificationResource {

    private final Logger log = LoggerFactory.getLogger(VwClassificationResource.class);

    private static final String ENTITY_NAME = "vwClassification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VwClassificationService vwClassificationService;

    private final VwClassificationRepository vwClassificationRepository;

    public VwClassificationResource(
        VwClassificationService vwClassificationService,
        VwClassificationRepository vwClassificationRepository
    ) {
        this.vwClassificationService = vwClassificationService;
        this.vwClassificationRepository = vwClassificationRepository;
    }

    /**
     * {@code POST  /vw-classifications} : Create a new vwClassification.
     *
     * @param vwClassificationDTO the vwClassificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vwClassificationDTO, or with status {@code 400 (Bad Request)} if the vwClassification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<VwClassificationDTO>> createVwClassification(@Valid @RequestBody VwClassificationDTO vwClassificationDTO)
        throws URISyntaxException {
        log.debug("REST request to save VwClassification : {}", vwClassificationDTO);
        if (vwClassificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new vwClassification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return vwClassificationService
            .save(vwClassificationDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/vw-classifications/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /vw-classifications/:id} : Updates an existing vwClassification.
     *
     * @param id the id of the vwClassificationDTO to save.
     * @param vwClassificationDTO the vwClassificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vwClassificationDTO,
     * or with status {@code 400 (Bad Request)} if the vwClassificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vwClassificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<VwClassificationDTO>> updateVwClassification(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VwClassificationDTO vwClassificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VwClassification : {}, {}", id, vwClassificationDTO);
        if (vwClassificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vwClassificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return vwClassificationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return vwClassificationService
                    .update(vwClassificationDTO)
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
     * {@code PATCH  /vw-classifications/:id} : Partial updates given fields of an existing vwClassification, field will ignore if it is null
     *
     * @param id the id of the vwClassificationDTO to save.
     * @param vwClassificationDTO the vwClassificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vwClassificationDTO,
     * or with status {@code 400 (Bad Request)} if the vwClassificationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vwClassificationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vwClassificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<VwClassificationDTO>> partialUpdateVwClassification(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VwClassificationDTO vwClassificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VwClassification partially : {}, {}", id, vwClassificationDTO);
        if (vwClassificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vwClassificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return vwClassificationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<VwClassificationDTO> result = vwClassificationService.partialUpdate(vwClassificationDTO);

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
     * {@code GET  /vw-classifications} : get all the vwClassifications.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vwClassifications in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<VwClassificationDTO>>> getAllVwClassifications(
        VwClassificationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get VwClassifications by criteria: {}", criteria);
        return vwClassificationService
            .countByCriteria(criteria)
            .zipWith(vwClassificationService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /vw-classifications/count} : count all the vwClassifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countVwClassifications(VwClassificationCriteria criteria) {
        log.debug("REST request to count VwClassifications by criteria: {}", criteria);
        return vwClassificationService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /vw-classifications/:id} : get the "id" vwClassification.
     *
     * @param id the id of the vwClassificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vwClassificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<VwClassificationDTO>> getVwClassification(@PathVariable Long id) {
        log.debug("REST request to get VwClassification : {}", id);
        Mono<VwClassificationDTO> vwClassificationDTO = vwClassificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vwClassificationDTO);
    }

    /**
     * {@code DELETE  /vw-classifications/:id} : delete the "id" vwClassification.
     *
     * @param id the id of the vwClassificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteVwClassification(@PathVariable Long id) {
        log.debug("REST request to delete VwClassification : {}", id);
        return vwClassificationService
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
