package com.hs.ec.portal.web.rest;

import com.hs.ec.portal.domain.criteria.FactorCriteria;
import com.hs.ec.portal.repository.FactorRepository;
import com.hs.ec.portal.service.FactorService;
import com.hs.ec.portal.service.dto.FactorDTO;
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
 * REST controller for managing {@link com.hs.ec.portal.domain.Factor}.
 */
@RestController
@RequestMapping("/api/factors")
public class FactorResource {

    private final Logger log = LoggerFactory.getLogger(FactorResource.class);

    private static final String ENTITY_NAME = "factor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactorService factorService;

    private final FactorRepository factorRepository;

    public FactorResource(FactorService factorService, FactorRepository factorRepository) {
        this.factorService = factorService;
        this.factorRepository = factorRepository;
    }

    /**
     * {@code POST  /factors} : Create a new factor.
     *
     * @param factorDTO the factorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factorDTO, or with status {@code 400 (Bad Request)} if the factor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<FactorDTO>> createFactor(@Valid @RequestBody FactorDTO factorDTO) throws URISyntaxException {
        log.debug("REST request to save Factor : {}", factorDTO);
        if (factorDTO.getId() != null) {
            throw new BadRequestAlertException("A new factor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return factorService
            .save(factorDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/factors/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /factors/:id} : Updates an existing factor.
     *
     * @param id the id of the factorDTO to save.
     * @param factorDTO the factorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factorDTO,
     * or with status {@code 400 (Bad Request)} if the factorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<FactorDTO>> updateFactor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FactorDTO factorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Factor : {}, {}", id, factorDTO);
        if (factorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return factorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return factorService
                    .update(factorDTO)
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
     * {@code PATCH  /factors/:id} : Partial updates given fields of an existing factor, field will ignore if it is null
     *
     * @param id the id of the factorDTO to save.
     * @param factorDTO the factorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factorDTO,
     * or with status {@code 400 (Bad Request)} if the factorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the factorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the factorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FactorDTO>> partialUpdateFactor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FactorDTO factorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Factor partially : {}, {}", id, factorDTO);
        if (factorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return factorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FactorDTO> result = factorService.partialUpdate(factorDTO);

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
     * {@code GET  /factors} : get all the factors.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factors in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<FactorDTO>>> getAllFactors(
        FactorCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get Factors by criteria: {}", criteria);
        return factorService
            .countByCriteria(criteria)
            .zipWith(factorService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /factors/count} : count all the factors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countFactors(FactorCriteria criteria) {
        log.debug("REST request to count Factors by criteria: {}", criteria);
        return factorService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /factors/:id} : get the "id" factor.
     *
     * @param id the id of the factorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FactorDTO>> getFactor(@PathVariable Long id) {
        log.debug("REST request to get Factor : {}", id);
        Mono<FactorDTO> factorDTO = factorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(factorDTO);
    }

    /**
     * {@code DELETE  /factors/:id} : delete the "id" factor.
     *
     * @param id the id of the factorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFactor(@PathVariable Long id) {
        log.debug("REST request to delete Factor : {}", id);
        return factorService
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
