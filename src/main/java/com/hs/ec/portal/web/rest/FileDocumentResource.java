package com.hs.ec.portal.web.rest;

import com.hs.ec.portal.domain.criteria.FileDocumentCriteria;
import com.hs.ec.portal.repository.FileDocumentRepository;
import com.hs.ec.portal.service.FileDocumentService;
import com.hs.ec.portal.service.dto.FileDocumentDTO;
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
 * REST controller for managing {@link com.hs.ec.portal.domain.FileDocument}.
 */
@RestController
@RequestMapping("/api/file-documents")
public class FileDocumentResource {

    private final Logger log = LoggerFactory.getLogger(FileDocumentResource.class);

    private static final String ENTITY_NAME = "fileDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileDocumentService fileDocumentService;

    private final FileDocumentRepository fileDocumentRepository;

    public FileDocumentResource(FileDocumentService fileDocumentService, FileDocumentRepository fileDocumentRepository) {
        this.fileDocumentService = fileDocumentService;
        this.fileDocumentRepository = fileDocumentRepository;
    }

    /**
     * {@code POST  /file-documents} : Create a new fileDocument.
     *
     * @param fileDocumentDTO the fileDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileDocumentDTO, or with status {@code 400 (Bad Request)} if the fileDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<FileDocumentDTO>> createFileDocument(@Valid @RequestBody FileDocumentDTO fileDocumentDTO)
        throws URISyntaxException {
        log.debug("REST request to save FileDocument : {}", fileDocumentDTO);
        if (fileDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new fileDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return fileDocumentService
            .save(fileDocumentDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/file-documents/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /file-documents/:id} : Updates an existing fileDocument.
     *
     * @param id the id of the fileDocumentDTO to save.
     * @param fileDocumentDTO the fileDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the fileDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<FileDocumentDTO>> updateFileDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FileDocumentDTO fileDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FileDocument : {}, {}", id, fileDocumentDTO);
        if (fileDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fileDocumentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return fileDocumentService
                    .update(fileDocumentDTO)
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
     * {@code PATCH  /file-documents/:id} : Partial updates given fields of an existing fileDocument, field will ignore if it is null
     *
     * @param id the id of the fileDocumentDTO to save.
     * @param fileDocumentDTO the fileDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the fileDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fileDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fileDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FileDocumentDTO>> partialUpdateFileDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FileDocumentDTO fileDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FileDocument partially : {}, {}", id, fileDocumentDTO);
        if (fileDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fileDocumentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FileDocumentDTO> result = fileDocumentService.partialUpdate(fileDocumentDTO);

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
     * {@code GET  /file-documents} : get all the fileDocuments.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileDocuments in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<FileDocumentDTO>>> getAllFileDocuments(
        FileDocumentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get FileDocuments by criteria: {}", criteria);
        return fileDocumentService
            .countByCriteria(criteria)
            .zipWith(fileDocumentService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /file-documents/count} : count all the fileDocuments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countFileDocuments(FileDocumentCriteria criteria) {
        log.debug("REST request to count FileDocuments by criteria: {}", criteria);
        return fileDocumentService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /file-documents/:id} : get the "id" fileDocument.
     *
     * @param id the id of the fileDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FileDocumentDTO>> getFileDocument(@PathVariable Long id) {
        log.debug("REST request to get FileDocument : {}", id);
        Mono<FileDocumentDTO> fileDocumentDTO = fileDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fileDocumentDTO);
    }

    /**
     * {@code DELETE  /file-documents/:id} : delete the "id" fileDocument.
     *
     * @param id the id of the fileDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFileDocument(@PathVariable Long id) {
        log.debug("REST request to delete FileDocument : {}", id);
        return fileDocumentService
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
