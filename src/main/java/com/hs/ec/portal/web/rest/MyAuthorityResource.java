package com.hs.ec.portal.web.rest;

import com.hs.ec.portal.domain.criteria.MyAuthorityCriteria;
import com.hs.ec.portal.repository.MyAuthorityRepository;
import com.hs.ec.portal.service.MyAuthorityService;
import com.hs.ec.portal.service.dto.MyAuthorityDTO;
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
 * REST controller for managing {@link com.hs.ec.portal.domain.MyAuthority}.
 */
@RestController
@RequestMapping("/api/my-authorities")
public class MyAuthorityResource {

    private final Logger log = LoggerFactory.getLogger(MyAuthorityResource.class);

    private static final String ENTITY_NAME = "myAuthority";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MyAuthorityService myAuthorityService;

    private final MyAuthorityRepository myAuthorityRepository;

    public MyAuthorityResource(MyAuthorityService myAuthorityService, MyAuthorityRepository myAuthorityRepository) {
        this.myAuthorityService = myAuthorityService;
        this.myAuthorityRepository = myAuthorityRepository;
    }

    /**
     * {@code POST  /my-authorities} : Create a new myAuthority.
     *
     * @param myAuthorityDTO the myAuthorityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new myAuthorityDTO, or with status {@code 400 (Bad Request)} if the myAuthority has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<MyAuthorityDTO>> createMyAuthority(@Valid @RequestBody MyAuthorityDTO myAuthorityDTO)
        throws URISyntaxException {
        log.debug("REST request to save MyAuthority : {}", myAuthorityDTO);
        if (myAuthorityDTO.getId() != null) {
            throw new BadRequestAlertException("A new myAuthority cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return myAuthorityService
            .save(myAuthorityDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/my-authorities/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /my-authorities/:id} : Updates an existing myAuthority.
     *
     * @param id the id of the myAuthorityDTO to save.
     * @param myAuthorityDTO the myAuthorityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myAuthorityDTO,
     * or with status {@code 400 (Bad Request)} if the myAuthorityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the myAuthorityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<MyAuthorityDTO>> updateMyAuthority(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MyAuthorityDTO myAuthorityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MyAuthority : {}, {}", id, myAuthorityDTO);
        if (myAuthorityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, myAuthorityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return myAuthorityRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return myAuthorityService
                    .update(myAuthorityDTO)
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
     * {@code PATCH  /my-authorities/:id} : Partial updates given fields of an existing myAuthority, field will ignore if it is null
     *
     * @param id the id of the myAuthorityDTO to save.
     * @param myAuthorityDTO the myAuthorityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myAuthorityDTO,
     * or with status {@code 400 (Bad Request)} if the myAuthorityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the myAuthorityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the myAuthorityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<MyAuthorityDTO>> partialUpdateMyAuthority(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MyAuthorityDTO myAuthorityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MyAuthority partially : {}, {}", id, myAuthorityDTO);
        if (myAuthorityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, myAuthorityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return myAuthorityRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<MyAuthorityDTO> result = myAuthorityService.partialUpdate(myAuthorityDTO);

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
     * {@code GET  /my-authorities} : get all the myAuthorities.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of myAuthorities in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<MyAuthorityDTO>>> getAllMyAuthorities(
        MyAuthorityCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get MyAuthorities by criteria: {}", criteria);
        return myAuthorityService
            .countByCriteria(criteria)
            .zipWith(myAuthorityService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /my-authorities/count} : count all the myAuthorities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countMyAuthorities(MyAuthorityCriteria criteria) {
        log.debug("REST request to count MyAuthorities by criteria: {}", criteria);
        return myAuthorityService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /my-authorities/:id} : get the "id" myAuthority.
     *
     * @param id the id of the myAuthorityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the myAuthorityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<MyAuthorityDTO>> getMyAuthority(@PathVariable Long id) {
        log.debug("REST request to get MyAuthority : {}", id);
        Mono<MyAuthorityDTO> myAuthorityDTO = myAuthorityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(myAuthorityDTO);
    }

    /**
     * {@code DELETE  /my-authorities/:id} : delete the "id" myAuthority.
     *
     * @param id the id of the myAuthorityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMyAuthority(@PathVariable Long id) {
        log.debug("REST request to delete MyAuthority : {}", id);
        return myAuthorityService
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
