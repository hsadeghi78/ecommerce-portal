package com.hs.ec.portal.web.rest;

import com.hs.ec.portal.domain.criteria.ConfigCriteria;
import com.hs.ec.portal.repository.ConfigRepository;
import com.hs.ec.portal.service.ConfigService;
import com.hs.ec.portal.service.dto.ConfigDTO;
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
 * REST controller for managing {@link com.hs.ec.portal.domain.Config}.
 */
@RestController
@RequestMapping("/api/configs")
public class ConfigResource {

    private final Logger log = LoggerFactory.getLogger(ConfigResource.class);

    private static final String ENTITY_NAME = "config";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigService configService;

    private final ConfigRepository configRepository;

    public ConfigResource(ConfigService configService, ConfigRepository configRepository) {
        this.configService = configService;
        this.configRepository = configRepository;
    }

    /**
     * {@code POST  /configs} : Create a new config.
     *
     * @param configDTO the configDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configDTO, or with status {@code 400 (Bad Request)} if the config has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ConfigDTO>> createConfig(@Valid @RequestBody ConfigDTO configDTO) throws URISyntaxException {
        log.debug("REST request to save Config : {}", configDTO);
        if (configDTO.getId() != null) {
            throw new BadRequestAlertException("A new config cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return configService
            .save(configDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/configs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /configs/:id} : Updates an existing config.
     *
     * @param id the id of the configDTO to save.
     * @param configDTO the configDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configDTO,
     * or with status {@code 400 (Bad Request)} if the configDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ConfigDTO>> updateConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ConfigDTO configDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Config : {}, {}", id, configDTO);
        if (configDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return configRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return configService
                    .update(configDTO)
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
     * {@code PATCH  /configs/:id} : Partial updates given fields of an existing config, field will ignore if it is null
     *
     * @param id the id of the configDTO to save.
     * @param configDTO the configDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configDTO,
     * or with status {@code 400 (Bad Request)} if the configDTO is not valid,
     * or with status {@code 404 (Not Found)} if the configDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the configDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ConfigDTO>> partialUpdateConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ConfigDTO configDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Config partially : {}, {}", id, configDTO);
        if (configDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return configRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ConfigDTO> result = configService.partialUpdate(configDTO);

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
     * {@code GET  /configs} : get all the configs.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configs in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ConfigDTO>>> getAllConfigs(
        ConfigCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get Configs by criteria: {}", criteria);
        return configService
            .countByCriteria(criteria)
            .zipWith(configService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /configs/count} : count all the configs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countConfigs(ConfigCriteria criteria) {
        log.debug("REST request to count Configs by criteria: {}", criteria);
        return configService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /configs/:id} : get the "id" config.
     *
     * @param id the id of the configDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ConfigDTO>> getConfig(@PathVariable Long id) {
        log.debug("REST request to get Config : {}", id);
        Mono<ConfigDTO> configDTO = configService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configDTO);
    }

    /**
     * {@code DELETE  /configs/:id} : delete the "id" config.
     *
     * @param id the id of the configDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteConfig(@PathVariable Long id) {
        log.debug("REST request to delete Config : {}", id);
        return configService
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
