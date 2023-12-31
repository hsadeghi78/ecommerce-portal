package com.hs.ec.portal.web.rest;

import com.hs.ec.portal.domain.criteria.AgreementCriteria;
import com.hs.ec.portal.repository.AgreementRepository;
import com.hs.ec.portal.service.AgreementService;
import com.hs.ec.portal.service.dto.AgreementDTO;
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
 * REST controller for managing {@link com.hs.ec.portal.domain.Agreement}.
 */
@RestController
@RequestMapping("/api/agreements")
public class AgreementResource {

    private final Logger log = LoggerFactory.getLogger(AgreementResource.class);

    private static final String ENTITY_NAME = "agreement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgreementService agreementService;

    private final AgreementRepository agreementRepository;

    public AgreementResource(AgreementService agreementService, AgreementRepository agreementRepository) {
        this.agreementService = agreementService;
        this.agreementRepository = agreementRepository;
    }

    /**
     * {@code POST  /agreements} : Create a new agreement.
     *
     * @param agreementDTO the agreementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agreementDTO, or with status {@code 400 (Bad Request)} if the agreement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AgreementDTO>> createAgreement(@Valid @RequestBody AgreementDTO agreementDTO) throws URISyntaxException {
        log.debug("REST request to save Agreement : {}", agreementDTO);
        if (agreementDTO.getId() != null) {
            throw new BadRequestAlertException("A new agreement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return agreementService
            .save(agreementDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/agreements/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /agreements/:id} : Updates an existing agreement.
     *
     * @param id the id of the agreementDTO to save.
     * @param agreementDTO the agreementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agreementDTO,
     * or with status {@code 400 (Bad Request)} if the agreementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agreementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AgreementDTO>> updateAgreement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AgreementDTO agreementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Agreement : {}, {}", id, agreementDTO);
        if (agreementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agreementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return agreementRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return agreementService
                    .update(agreementDTO)
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
     * {@code PATCH  /agreements/:id} : Partial updates given fields of an existing agreement, field will ignore if it is null
     *
     * @param id the id of the agreementDTO to save.
     * @param agreementDTO the agreementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agreementDTO,
     * or with status {@code 400 (Bad Request)} if the agreementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the agreementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the agreementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AgreementDTO>> partialUpdateAgreement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AgreementDTO agreementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Agreement partially : {}, {}", id, agreementDTO);
        if (agreementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agreementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return agreementRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AgreementDTO> result = agreementService.partialUpdate(agreementDTO);

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
     * {@code GET  /agreements} : get all the agreements.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agreements in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<AgreementDTO>>> getAllAgreements(
        AgreementCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get Agreements by criteria: {}", criteria);
        return agreementService
            .countByCriteria(criteria)
            .zipWith(agreementService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /agreements/count} : count all the agreements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countAgreements(AgreementCriteria criteria) {
        log.debug("REST request to count Agreements by criteria: {}", criteria);
        return agreementService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /agreements/:id} : get the "id" agreement.
     *
     * @param id the id of the agreementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agreementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AgreementDTO>> getAgreement(@PathVariable Long id) {
        log.debug("REST request to get Agreement : {}", id);
        Mono<AgreementDTO> agreementDTO = agreementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agreementDTO);
    }

    /**
     * {@code DELETE  /agreements/:id} : delete the "id" agreement.
     *
     * @param id the id of the agreementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAgreement(@PathVariable Long id) {
        log.debug("REST request to delete Agreement : {}", id);
        return agreementService
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
