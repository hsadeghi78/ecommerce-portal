package com.hs.ec.portal.web.rest;

import com.hs.ec.portal.domain.criteria.PartyCriteria;
import com.hs.ec.portal.repository.PartyRepository;
import com.hs.ec.portal.service.PartyService;
import com.hs.ec.portal.service.dto.PartyDTO;
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
 * REST controller for managing {@link com.hs.ec.portal.domain.Party}.
 */
@RestController
@RequestMapping("/api/parties")
public class PartyResource {

    private final Logger log = LoggerFactory.getLogger(PartyResource.class);

    private static final String ENTITY_NAME = "party";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartyService partyService;

    private final PartyRepository partyRepository;

    public PartyResource(PartyService partyService, PartyRepository partyRepository) {
        this.partyService = partyService;
        this.partyRepository = partyRepository;
    }

    /**
     * {@code POST  /parties} : Create a new party.
     *
     * @param partyDTO the partyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partyDTO, or with status {@code 400 (Bad Request)} if the party has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PartyDTO>> createParty(@Valid @RequestBody PartyDTO partyDTO) throws URISyntaxException {
        log.debug("REST request to save Party : {}", partyDTO);
        if (partyDTO.getId() != null) {
            throw new BadRequestAlertException("A new party cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return partyService
            .save(partyDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/parties/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /parties/:id} : Updates an existing party.
     *
     * @param id the id of the partyDTO to save.
     * @param partyDTO the partyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyDTO,
     * or with status {@code 400 (Bad Request)} if the partyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PartyDTO>> updateParty(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PartyDTO partyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Party : {}, {}", id, partyDTO);
        if (partyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return partyRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return partyService
                    .update(partyDTO)
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
     * {@code PATCH  /parties/:id} : Partial updates given fields of an existing party, field will ignore if it is null
     *
     * @param id the id of the partyDTO to save.
     * @param partyDTO the partyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyDTO,
     * or with status {@code 400 (Bad Request)} if the partyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the partyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the partyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PartyDTO>> partialUpdateParty(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PartyDTO partyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Party partially : {}, {}", id, partyDTO);
        if (partyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return partyRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PartyDTO> result = partyService.partialUpdate(partyDTO);

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
     * {@code GET  /parties} : get all the parties.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parties in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PartyDTO>>> getAllParties(
        PartyCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get Parties by criteria: {}", criteria);
        return partyService
            .countByCriteria(criteria)
            .zipWith(partyService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /parties/count} : count all the parties.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countParties(PartyCriteria criteria) {
        log.debug("REST request to count Parties by criteria: {}", criteria);
        return partyService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /parties/:id} : get the "id" party.
     *
     * @param id the id of the partyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PartyDTO>> getParty(@PathVariable Long id) {
        log.debug("REST request to get Party : {}", id);
        Mono<PartyDTO> partyDTO = partyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partyDTO);
    }

    /**
     * {@code DELETE  /parties/:id} : delete the "id" party.
     *
     * @param id the id of the partyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteParty(@PathVariable Long id) {
        log.debug("REST request to delete Party : {}", id);
        return partyService
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
