package com.hs.ec.portal.web.rest;

import com.hs.ec.portal.domain.criteria.FactorItemCriteria;
import com.hs.ec.portal.repository.FactorItemRepository;
import com.hs.ec.portal.service.FactorItemService;
import com.hs.ec.portal.service.dto.FactorItemDTO;
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
 * REST controller for managing {@link com.hs.ec.portal.domain.FactorItem}.
 */
@RestController
@RequestMapping("/api/factor-items")
public class FactorItemResource {

    private final Logger log = LoggerFactory.getLogger(FactorItemResource.class);

    private static final String ENTITY_NAME = "factorItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactorItemService factorItemService;

    private final FactorItemRepository factorItemRepository;

    public FactorItemResource(FactorItemService factorItemService, FactorItemRepository factorItemRepository) {
        this.factorItemService = factorItemService;
        this.factorItemRepository = factorItemRepository;
    }

    /**
     * {@code POST  /factor-items} : Create a new factorItem.
     *
     * @param factorItemDTO the factorItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factorItemDTO, or with status {@code 400 (Bad Request)} if the factorItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<FactorItemDTO>> createFactorItem(@Valid @RequestBody FactorItemDTO factorItemDTO) throws URISyntaxException {
        log.debug("REST request to save FactorItem : {}", factorItemDTO);
        if (factorItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new factorItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return factorItemService
            .save(factorItemDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/factor-items/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /factor-items/:id} : Updates an existing factorItem.
     *
     * @param id the id of the factorItemDTO to save.
     * @param factorItemDTO the factorItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factorItemDTO,
     * or with status {@code 400 (Bad Request)} if the factorItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factorItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<FactorItemDTO>> updateFactorItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FactorItemDTO factorItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FactorItem : {}, {}", id, factorItemDTO);
        if (factorItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factorItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return factorItemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return factorItemService
                    .update(factorItemDTO)
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
     * {@code PATCH  /factor-items/:id} : Partial updates given fields of an existing factorItem, field will ignore if it is null
     *
     * @param id the id of the factorItemDTO to save.
     * @param factorItemDTO the factorItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factorItemDTO,
     * or with status {@code 400 (Bad Request)} if the factorItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the factorItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the factorItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FactorItemDTO>> partialUpdateFactorItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FactorItemDTO factorItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FactorItem partially : {}, {}", id, factorItemDTO);
        if (factorItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factorItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return factorItemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FactorItemDTO> result = factorItemService.partialUpdate(factorItemDTO);

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
     * {@code GET  /factor-items} : get all the factorItems.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factorItems in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<FactorItemDTO>>> getAllFactorItems(
        FactorItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get FactorItems by criteria: {}", criteria);
        return factorItemService
            .countByCriteria(criteria)
            .zipWith(factorItemService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /factor-items/count} : count all the factorItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countFactorItems(FactorItemCriteria criteria) {
        log.debug("REST request to count FactorItems by criteria: {}", criteria);
        return factorItemService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /factor-items/:id} : get the "id" factorItem.
     *
     * @param id the id of the factorItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factorItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FactorItemDTO>> getFactorItem(@PathVariable Long id) {
        log.debug("REST request to get FactorItem : {}", id);
        Mono<FactorItemDTO> factorItemDTO = factorItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(factorItemDTO);
    }

    /**
     * {@code DELETE  /factor-items/:id} : delete the "id" factorItem.
     *
     * @param id the id of the factorItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFactorItem(@PathVariable Long id) {
        log.debug("REST request to delete FactorItem : {}", id);
        return factorItemService
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
