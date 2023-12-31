package com.hs.ec.portal.web.rest;

import com.hs.ec.portal.domain.criteria.WalletCriteria;
import com.hs.ec.portal.repository.WalletRepository;
import com.hs.ec.portal.service.WalletService;
import com.hs.ec.portal.service.dto.WalletDTO;
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
 * REST controller for managing {@link com.hs.ec.portal.domain.Wallet}.
 */
@RestController
@RequestMapping("/api/wallets")
public class WalletResource {

    private final Logger log = LoggerFactory.getLogger(WalletResource.class);

    private static final String ENTITY_NAME = "wallet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WalletService walletService;

    private final WalletRepository walletRepository;

    public WalletResource(WalletService walletService, WalletRepository walletRepository) {
        this.walletService = walletService;
        this.walletRepository = walletRepository;
    }

    /**
     * {@code POST  /wallets} : Create a new wallet.
     *
     * @param walletDTO the walletDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new walletDTO, or with status {@code 400 (Bad Request)} if the wallet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<WalletDTO>> createWallet(@Valid @RequestBody WalletDTO walletDTO) throws URISyntaxException {
        log.debug("REST request to save Wallet : {}", walletDTO);
        if (walletDTO.getId() != null) {
            throw new BadRequestAlertException("A new wallet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return walletService
            .save(walletDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/wallets/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /wallets/:id} : Updates an existing wallet.
     *
     * @param id the id of the walletDTO to save.
     * @param walletDTO the walletDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated walletDTO,
     * or with status {@code 400 (Bad Request)} if the walletDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the walletDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<WalletDTO>> updateWallet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WalletDTO walletDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Wallet : {}, {}", id, walletDTO);
        if (walletDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, walletDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return walletRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return walletService
                    .update(walletDTO)
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
     * {@code PATCH  /wallets/:id} : Partial updates given fields of an existing wallet, field will ignore if it is null
     *
     * @param id the id of the walletDTO to save.
     * @param walletDTO the walletDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated walletDTO,
     * or with status {@code 400 (Bad Request)} if the walletDTO is not valid,
     * or with status {@code 404 (Not Found)} if the walletDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the walletDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<WalletDTO>> partialUpdateWallet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WalletDTO walletDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Wallet partially : {}, {}", id, walletDTO);
        if (walletDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, walletDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return walletRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<WalletDTO> result = walletService.partialUpdate(walletDTO);

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
     * {@code GET  /wallets} : get all the wallets.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wallets in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<WalletDTO>>> getAllWallets(
        WalletCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get Wallets by criteria: {}", criteria);
        return walletService
            .countByCriteria(criteria)
            .zipWith(walletService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /wallets/count} : count all the wallets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countWallets(WalletCriteria criteria) {
        log.debug("REST request to count Wallets by criteria: {}", criteria);
        return walletService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /wallets/:id} : get the "id" wallet.
     *
     * @param id the id of the walletDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the walletDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<WalletDTO>> getWallet(@PathVariable Long id) {
        log.debug("REST request to get Wallet : {}", id);
        Mono<WalletDTO> walletDTO = walletService.findOne(id);
        return ResponseUtil.wrapOrNotFound(walletDTO);
    }

    /**
     * {@code DELETE  /wallets/:id} : delete the "id" wallet.
     *
     * @param id the id of the walletDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteWallet(@PathVariable Long id) {
        log.debug("REST request to delete Wallet : {}", id);
        return walletService
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
