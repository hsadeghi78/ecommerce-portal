package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.FileDocumentCriteria;
import com.hs.ec.portal.repository.FileDocumentRepository;
import com.hs.ec.portal.service.dto.FileDocumentDTO;
import com.hs.ec.portal.service.mapper.FileDocumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.FileDocument}.
 */
@Service
@Transactional
public class FileDocumentService {

    private final Logger log = LoggerFactory.getLogger(FileDocumentService.class);

    private final FileDocumentRepository fileDocumentRepository;

    private final FileDocumentMapper fileDocumentMapper;

    public FileDocumentService(FileDocumentRepository fileDocumentRepository, FileDocumentMapper fileDocumentMapper) {
        this.fileDocumentRepository = fileDocumentRepository;
        this.fileDocumentMapper = fileDocumentMapper;
    }

    /**
     * Save a fileDocument.
     *
     * @param fileDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FileDocumentDTO> save(FileDocumentDTO fileDocumentDTO) {
        log.debug("Request to save FileDocument : {}", fileDocumentDTO);
        return fileDocumentRepository.save(fileDocumentMapper.toEntity(fileDocumentDTO)).map(fileDocumentMapper::toDto);
    }

    /**
     * Update a fileDocument.
     *
     * @param fileDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FileDocumentDTO> update(FileDocumentDTO fileDocumentDTO) {
        log.debug("Request to update FileDocument : {}", fileDocumentDTO);
        return fileDocumentRepository.save(fileDocumentMapper.toEntity(fileDocumentDTO)).map(fileDocumentMapper::toDto);
    }

    /**
     * Partially update a fileDocument.
     *
     * @param fileDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<FileDocumentDTO> partialUpdate(FileDocumentDTO fileDocumentDTO) {
        log.debug("Request to partially update FileDocument : {}", fileDocumentDTO);

        return fileDocumentRepository
            .findById(fileDocumentDTO.getId())
            .map(existingFileDocument -> {
                fileDocumentMapper.partialUpdate(existingFileDocument, fileDocumentDTO);

                return existingFileDocument;
            })
            .flatMap(fileDocumentRepository::save)
            .map(fileDocumentMapper::toDto);
    }

    /**
     * Get all the fileDocuments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<FileDocumentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FileDocuments");
        return fileDocumentRepository.findAllBy(pageable).map(fileDocumentMapper::toDto);
    }

    /**
     * Find fileDocuments by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<FileDocumentDTO> findByCriteria(FileDocumentCriteria criteria, Pageable pageable) {
        log.debug("Request to get all FileDocuments by Criteria");
        return fileDocumentRepository.findByCriteria(criteria, pageable).map(fileDocumentMapper::toDto);
    }

    /**
     * Find the count of fileDocuments by criteria.
     * @param criteria filtering criteria
     * @return the count of fileDocuments
     */
    public Mono<Long> countByCriteria(FileDocumentCriteria criteria) {
        log.debug("Request to get the count of all FileDocuments by Criteria");
        return fileDocumentRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of fileDocuments available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return fileDocumentRepository.count();
    }

    /**
     * Get one fileDocument by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<FileDocumentDTO> findOne(Long id) {
        log.debug("Request to get FileDocument : {}", id);
        return fileDocumentRepository.findById(id).map(fileDocumentMapper::toDto);
    }

    /**
     * Delete the fileDocument by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete FileDocument : {}", id);
        return fileDocumentRepository.deleteById(id);
    }
}
