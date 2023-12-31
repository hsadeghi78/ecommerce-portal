package com.hs.ec.portal.web.rest;

import com.hs.ec.portal.domain.criteria.ResourceCriteria;
import com.hs.ec.portal.repository.ResourceRepository;
import com.hs.ec.portal.service.ResourceService;
import com.hs.ec.portal.service.dto.ResourceDTO;
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
 * REST controller for managing {@link com.hs.ec.portal.domain.Resource}.
 */
@RestController
@RequestMapping("/api/resources")
public class ResourceResource {

    private final Logger log = LoggerFactory.getLogger(ResourceResource.class);

    private static final String ENTITY_NAME = "resource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResourceService resourceService;

    private final ResourceRepository resourceRepository;

    public ResourceResource(ResourceService resourceService, ResourceRepository resourceRepository) {
        this.resourceService = resourceService;
        this.resourceRepository = resourceRepository;
    }

    /**
     * {@code POST  /resources} : Create a new resource.
     *
     * @param resourceDTO the resourceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourceDTO, or with status {@code 400 (Bad Request)} if the resource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ResourceDTO>> createResource(@Valid @RequestBody ResourceDTO resourceDTO) throws URISyntaxException {
        log.debug("REST request to save Resource : {}", resourceDTO);
        if (resourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new resource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return resourceService
            .save(resourceDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/resources/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /resources/:id} : Updates an existing resource.
     *
     * @param id the id of the resourceDTO to save.
     * @param resourceDTO the resourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceDTO,
     * or with status {@code 400 (Bad Request)} if the resourceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ResourceDTO>> updateResource(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ResourceDTO resourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Resource : {}, {}", id, resourceDTO);
        if (resourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return resourceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return resourceService
                    .update(resourceDTO)
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
     * {@code PATCH  /resources/:id} : Partial updates given fields of an existing resource, field will ignore if it is null
     *
     * @param id the id of the resourceDTO to save.
     * @param resourceDTO the resourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceDTO,
     * or with status {@code 400 (Bad Request)} if the resourceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the resourceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the resourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ResourceDTO>> partialUpdateResource(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ResourceDTO resourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Resource partially : {}, {}", id, resourceDTO);
        if (resourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return resourceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ResourceDTO> result = resourceService.partialUpdate(resourceDTO);

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
     * {@code GET  /resources} : get all the resources.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resources in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ResourceDTO>>> getAllResources(
        ResourceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get Resources by criteria: {}", criteria);
        return resourceService
            .countByCriteria(criteria)
            .zipWith(resourceService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /resources/count} : count all the resources.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countResources(ResourceCriteria criteria) {
        log.debug("REST request to count Resources by criteria: {}", criteria);
        return resourceService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /resources/:id} : get the "id" resource.
     *
     * @param id the id of the resourceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ResourceDTO>> getResource(@PathVariable Long id) {
        log.debug("REST request to get Resource : {}", id);
        Mono<ResourceDTO> resourceDTO = resourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resourceDTO);
    }

    /**
     * {@code DELETE  /resources/:id} : delete the "id" resource.
     *
     * @param id the id of the resourceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteResource(@PathVariable Long id) {
        log.debug("REST request to delete Resource : {}", id);
        return resourceService
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
