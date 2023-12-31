package com.hs.ec.portal.web.rest;

import com.hs.ec.portal.domain.criteria.ClassificationCriteria;
import com.hs.ec.portal.repository.ClassificationRepository;
import com.hs.ec.portal.service.ClassificationService;
import com.hs.ec.portal.service.dto.ClassificationDTO;
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
 * REST controller for managing {@link com.hs.ec.portal.domain.Classification}.
 */
@RestController
@RequestMapping("/api/classifications")
public class ClassificationResource {

    private final Logger log = LoggerFactory.getLogger(ClassificationResource.class);

    private static final String ENTITY_NAME = "classification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassificationService classificationService;

    private final ClassificationRepository classificationRepository;

    public ClassificationResource(ClassificationService classificationService, ClassificationRepository classificationRepository) {
        this.classificationService = classificationService;
        this.classificationRepository = classificationRepository;
    }

    /**
     * {@code POST  /classifications} : Create a new classification.
     *
     * @param classificationDTO the classificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classificationDTO, or with status {@code 400 (Bad Request)} if the classification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ClassificationDTO>> createClassification(@Valid @RequestBody ClassificationDTO classificationDTO)
        throws URISyntaxException {
        log.debug("REST request to save Classification : {}", classificationDTO);
        if (classificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new classification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return classificationService
            .save(classificationDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/classifications/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /classifications/:id} : Updates an existing classification.
     *
     * @param id the id of the classificationDTO to save.
     * @param classificationDTO the classificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classificationDTO,
     * or with status {@code 400 (Bad Request)} if the classificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ClassificationDTO>> updateClassification(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClassificationDTO classificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Classification : {}, {}", id, classificationDTO);
        if (classificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return classificationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return classificationService
                    .update(classificationDTO)
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
     * {@code PATCH  /classifications/:id} : Partial updates given fields of an existing classification, field will ignore if it is null
     *
     * @param id the id of the classificationDTO to save.
     * @param classificationDTO the classificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classificationDTO,
     * or with status {@code 400 (Bad Request)} if the classificationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the classificationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the classificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ClassificationDTO>> partialUpdateClassification(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClassificationDTO classificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Classification partially : {}, {}", id, classificationDTO);
        if (classificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return classificationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ClassificationDTO> result = classificationService.partialUpdate(classificationDTO);

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
     * {@code GET  /classifications} : get all the classifications.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classifications in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ClassificationDTO>>> getAllClassifications(
        ClassificationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get Classifications by criteria: {}", criteria);
        return classificationService
            .countByCriteria(criteria)
            .zipWith(classificationService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /classifications/count} : count all the classifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countClassifications(ClassificationCriteria criteria) {
        log.debug("REST request to count Classifications by criteria: {}", criteria);
        return classificationService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /classifications/:id} : get the "id" classification.
     *
     * @param id the id of the classificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ClassificationDTO>> getClassification(@PathVariable Long id) {
        log.debug("REST request to get Classification : {}", id);
        Mono<ClassificationDTO> classificationDTO = classificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classificationDTO);
    }

    /**
     * {@code DELETE  /classifications/:id} : delete the "id" classification.
     *
     * @param id the id of the classificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteClassification(@PathVariable Long id) {
        log.debug("REST request to delete Classification : {}", id);
        return classificationService
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
