package com.hs.ec.portal.web.rest;

import com.hs.ec.portal.domain.criteria.ResourceAuthorityCriteria;
import com.hs.ec.portal.repository.ResourceAuthorityRepository;
import com.hs.ec.portal.service.ResourceAuthorityService;
import com.hs.ec.portal.service.dto.ResourceAuthorityDTO;
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
 * REST controller for managing {@link com.hs.ec.portal.domain.ResourceAuthority}.
 */
@RestController
@RequestMapping("/api/resource-authorities")
public class ResourceAuthorityResource {

    private final Logger log = LoggerFactory.getLogger(ResourceAuthorityResource.class);

    private static final String ENTITY_NAME = "resourceAuthority";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResourceAuthorityService resourceAuthorityService;

    private final ResourceAuthorityRepository resourceAuthorityRepository;

    public ResourceAuthorityResource(
        ResourceAuthorityService resourceAuthorityService,
        ResourceAuthorityRepository resourceAuthorityRepository
    ) {
        this.resourceAuthorityService = resourceAuthorityService;
        this.resourceAuthorityRepository = resourceAuthorityRepository;
    }

    /**
     * {@code POST  /resource-authorities} : Create a new resourceAuthority.
     *
     * @param resourceAuthorityDTO the resourceAuthorityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourceAuthorityDTO, or with status {@code 400 (Bad Request)} if the resourceAuthority has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ResourceAuthorityDTO>> createResourceAuthority(
        @Valid @RequestBody ResourceAuthorityDTO resourceAuthorityDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ResourceAuthority : {}", resourceAuthorityDTO);
        if (resourceAuthorityDTO.getId() != null) {
            throw new BadRequestAlertException("A new resourceAuthority cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return resourceAuthorityService
            .save(resourceAuthorityDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/resource-authorities/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /resource-authorities/:id} : Updates an existing resourceAuthority.
     *
     * @param id the id of the resourceAuthorityDTO to save.
     * @param resourceAuthorityDTO the resourceAuthorityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceAuthorityDTO,
     * or with status {@code 400 (Bad Request)} if the resourceAuthorityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceAuthorityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ResourceAuthorityDTO>> updateResourceAuthority(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ResourceAuthorityDTO resourceAuthorityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ResourceAuthority : {}, {}", id, resourceAuthorityDTO);
        if (resourceAuthorityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceAuthorityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return resourceAuthorityRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return resourceAuthorityService
                    .update(resourceAuthorityDTO)
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
     * {@code PATCH  /resource-authorities/:id} : Partial updates given fields of an existing resourceAuthority, field will ignore if it is null
     *
     * @param id the id of the resourceAuthorityDTO to save.
     * @param resourceAuthorityDTO the resourceAuthorityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceAuthorityDTO,
     * or with status {@code 400 (Bad Request)} if the resourceAuthorityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the resourceAuthorityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the resourceAuthorityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ResourceAuthorityDTO>> partialUpdateResourceAuthority(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ResourceAuthorityDTO resourceAuthorityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ResourceAuthority partially : {}, {}", id, resourceAuthorityDTO);
        if (resourceAuthorityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceAuthorityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return resourceAuthorityRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ResourceAuthorityDTO> result = resourceAuthorityService.partialUpdate(resourceAuthorityDTO);

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
     * {@code GET  /resource-authorities} : get all the resourceAuthorities.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourceAuthorities in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ResourceAuthorityDTO>>> getAllResourceAuthorities(
        ResourceAuthorityCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get ResourceAuthorities by criteria: {}", criteria);
        return resourceAuthorityService
            .countByCriteria(criteria)
            .zipWith(resourceAuthorityService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /resource-authorities/count} : count all the resourceAuthorities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countResourceAuthorities(ResourceAuthorityCriteria criteria) {
        log.debug("REST request to count ResourceAuthorities by criteria: {}", criteria);
        return resourceAuthorityService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /resource-authorities/:id} : get the "id" resourceAuthority.
     *
     * @param id the id of the resourceAuthorityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourceAuthorityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ResourceAuthorityDTO>> getResourceAuthority(@PathVariable Long id) {
        log.debug("REST request to get ResourceAuthority : {}", id);
        Mono<ResourceAuthorityDTO> resourceAuthorityDTO = resourceAuthorityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resourceAuthorityDTO);
    }

    /**
     * {@code DELETE  /resource-authorities/:id} : delete the "id" resourceAuthority.
     *
     * @param id the id of the resourceAuthorityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteResourceAuthority(@PathVariable Long id) {
        log.debug("REST request to delete ResourceAuthority : {}", id);
        return resourceAuthorityService
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
