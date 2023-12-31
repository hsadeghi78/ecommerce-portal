package com.hs.ec.portal.web.rest;

import com.hs.ec.portal.domain.criteria.ClassTypeCriteria;
import com.hs.ec.portal.repository.ClassTypeRepository;
import com.hs.ec.portal.service.ClassTypeService;
import com.hs.ec.portal.service.dto.ClassTypeDTO;
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
 * REST controller for managing {@link com.hs.ec.portal.domain.ClassType}.
 */
@RestController
@RequestMapping("/api/class-types")
public class ClassTypeResource {

    private final Logger log = LoggerFactory.getLogger(ClassTypeResource.class);

    private static final String ENTITY_NAME = "classType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassTypeService classTypeService;

    private final ClassTypeRepository classTypeRepository;

    public ClassTypeResource(ClassTypeService classTypeService, ClassTypeRepository classTypeRepository) {
        this.classTypeService = classTypeService;
        this.classTypeRepository = classTypeRepository;
    }

    /**
     * {@code POST  /class-types} : Create a new classType.
     *
     * @param classTypeDTO the classTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classTypeDTO, or with status {@code 400 (Bad Request)} if the classType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ClassTypeDTO>> createClassType(@Valid @RequestBody ClassTypeDTO classTypeDTO) throws URISyntaxException {
        log.debug("REST request to save ClassType : {}", classTypeDTO);
        if (classTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new classType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return classTypeService
            .save(classTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/class-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /class-types/:id} : Updates an existing classType.
     *
     * @param id the id of the classTypeDTO to save.
     * @param classTypeDTO the classTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classTypeDTO,
     * or with status {@code 400 (Bad Request)} if the classTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ClassTypeDTO>> updateClassType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClassTypeDTO classTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ClassType : {}, {}", id, classTypeDTO);
        if (classTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return classTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return classTypeService
                    .update(classTypeDTO)
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
     * {@code PATCH  /class-types/:id} : Partial updates given fields of an existing classType, field will ignore if it is null
     *
     * @param id the id of the classTypeDTO to save.
     * @param classTypeDTO the classTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classTypeDTO,
     * or with status {@code 400 (Bad Request)} if the classTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the classTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the classTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ClassTypeDTO>> partialUpdateClassType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClassTypeDTO classTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ClassType partially : {}, {}", id, classTypeDTO);
        if (classTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return classTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ClassTypeDTO> result = classTypeService.partialUpdate(classTypeDTO);

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
     * {@code GET  /class-types} : get all the classTypes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classTypes in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ClassTypeDTO>>> getAllClassTypes(
        ClassTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get ClassTypes by criteria: {}", criteria);
        return classTypeService
            .countByCriteria(criteria)
            .zipWith(classTypeService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /class-types/count} : count all the classTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countClassTypes(ClassTypeCriteria criteria) {
        log.debug("REST request to count ClassTypes by criteria: {}", criteria);
        return classTypeService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /class-types/:id} : get the "id" classType.
     *
     * @param id the id of the classTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ClassTypeDTO>> getClassType(@PathVariable Long id) {
        log.debug("REST request to get ClassType : {}", id);
        Mono<ClassTypeDTO> classTypeDTO = classTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classTypeDTO);
    }

    /**
     * {@code DELETE  /class-types/:id} : delete the "id" classType.
     *
     * @param id the id of the classTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteClassType(@PathVariable Long id) {
        log.debug("REST request to delete ClassType : {}", id);
        return classTypeService
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
