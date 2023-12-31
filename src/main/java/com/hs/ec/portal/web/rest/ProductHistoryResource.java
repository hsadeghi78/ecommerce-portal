package com.hs.ec.portal.web.rest;

import com.hs.ec.portal.domain.criteria.ProductHistoryCriteria;
import com.hs.ec.portal.repository.ProductHistoryRepository;
import com.hs.ec.portal.service.ProductHistoryService;
import com.hs.ec.portal.service.dto.ProductHistoryDTO;
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
 * REST controller for managing {@link com.hs.ec.portal.domain.ProductHistory}.
 */
@RestController
@RequestMapping("/api/product-histories")
public class ProductHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ProductHistoryResource.class);

    private static final String ENTITY_NAME = "productHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductHistoryService productHistoryService;

    private final ProductHistoryRepository productHistoryRepository;

    public ProductHistoryResource(ProductHistoryService productHistoryService, ProductHistoryRepository productHistoryRepository) {
        this.productHistoryService = productHistoryService;
        this.productHistoryRepository = productHistoryRepository;
    }

    /**
     * {@code POST  /product-histories} : Create a new productHistory.
     *
     * @param productHistoryDTO the productHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productHistoryDTO, or with status {@code 400 (Bad Request)} if the productHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ProductHistoryDTO>> createProductHistory(@Valid @RequestBody ProductHistoryDTO productHistoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProductHistory : {}", productHistoryDTO);
        if (productHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new productHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return productHistoryService
            .save(productHistoryDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/product-histories/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /product-histories/:id} : Updates an existing productHistory.
     *
     * @param id the id of the productHistoryDTO to save.
     * @param productHistoryDTO the productHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the productHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductHistoryDTO>> updateProductHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductHistoryDTO productHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductHistory : {}, {}", id, productHistoryDTO);
        if (productHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return productHistoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return productHistoryService
                    .update(productHistoryDTO)
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
     * {@code PATCH  /product-histories/:id} : Partial updates given fields of an existing productHistory, field will ignore if it is null
     *
     * @param id the id of the productHistoryDTO to save.
     * @param productHistoryDTO the productHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the productHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ProductHistoryDTO>> partialUpdateProductHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductHistoryDTO productHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductHistory partially : {}, {}", id, productHistoryDTO);
        if (productHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return productHistoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ProductHistoryDTO> result = productHistoryService.partialUpdate(productHistoryDTO);

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
     * {@code GET  /product-histories} : get all the productHistories.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productHistories in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ProductHistoryDTO>>> getAllProductHistories(
        ProductHistoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get ProductHistories by criteria: {}", criteria);
        return productHistoryService
            .countByCriteria(criteria)
            .zipWith(productHistoryService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /product-histories/count} : count all the productHistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countProductHistories(ProductHistoryCriteria criteria) {
        log.debug("REST request to count ProductHistories by criteria: {}", criteria);
        return productHistoryService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /product-histories/:id} : get the "id" productHistory.
     *
     * @param id the id of the productHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductHistoryDTO>> getProductHistory(@PathVariable Long id) {
        log.debug("REST request to get ProductHistory : {}", id);
        Mono<ProductHistoryDTO> productHistoryDTO = productHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productHistoryDTO);
    }

    /**
     * {@code DELETE  /product-histories/:id} : delete the "id" productHistory.
     *
     * @param id the id of the productHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProductHistory(@PathVariable Long id) {
        log.debug("REST request to delete ProductHistory : {}", id);
        return productHistoryService
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
