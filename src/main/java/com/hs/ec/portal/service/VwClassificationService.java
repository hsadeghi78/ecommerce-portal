package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.VwClassificationCriteria;
import com.hs.ec.portal.repository.VwClassificationRepository;
import com.hs.ec.portal.service.dto.VwClassificationDTO;
import com.hs.ec.portal.service.mapper.VwClassificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.VwClassification}.
 */
@Service
@Transactional
public class VwClassificationService {

    private final Logger log = LoggerFactory.getLogger(VwClassificationService.class);

    private final VwClassificationRepository vwClassificationRepository;

    private final VwClassificationMapper vwClassificationMapper;

    public VwClassificationService(VwClassificationRepository vwClassificationRepository, VwClassificationMapper vwClassificationMapper) {
        this.vwClassificationRepository = vwClassificationRepository;
        this.vwClassificationMapper = vwClassificationMapper;
    }

    /**
     * Save a vwClassification.
     *
     * @param vwClassificationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<VwClassificationDTO> save(VwClassificationDTO vwClassificationDTO) {
        log.debug("Request to save VwClassification : {}", vwClassificationDTO);
        return vwClassificationRepository.save(vwClassificationMapper.toEntity(vwClassificationDTO)).map(vwClassificationMapper::toDto);
    }

    /**
     * Update a vwClassification.
     *
     * @param vwClassificationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<VwClassificationDTO> update(VwClassificationDTO vwClassificationDTO) {
        log.debug("Request to update VwClassification : {}", vwClassificationDTO);
        return vwClassificationRepository.save(vwClassificationMapper.toEntity(vwClassificationDTO)).map(vwClassificationMapper::toDto);
    }

    /**
     * Partially update a vwClassification.
     *
     * @param vwClassificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<VwClassificationDTO> partialUpdate(VwClassificationDTO vwClassificationDTO) {
        log.debug("Request to partially update VwClassification : {}", vwClassificationDTO);

        return vwClassificationRepository
            .findById(vwClassificationDTO.getId())
            .map(existingVwClassification -> {
                vwClassificationMapper.partialUpdate(existingVwClassification, vwClassificationDTO);

                return existingVwClassification;
            })
            .flatMap(vwClassificationRepository::save)
            .map(vwClassificationMapper::toDto);
    }

    /**
     * Get all the vwClassifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<VwClassificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VwClassifications");
        return vwClassificationRepository.findAllBy(pageable).map(vwClassificationMapper::toDto);
    }

    /**
     * Find vwClassifications by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<VwClassificationDTO> findByCriteria(VwClassificationCriteria criteria, Pageable pageable) {
        log.debug("Request to get all VwClassifications by Criteria");
        return vwClassificationRepository.findByCriteria(criteria, pageable).map(vwClassificationMapper::toDto);
    }

    /**
     * Find the count of vwClassifications by criteria.
     * @param criteria filtering criteria
     * @return the count of vwClassifications
     */
    public Mono<Long> countByCriteria(VwClassificationCriteria criteria) {
        log.debug("Request to get the count of all VwClassifications by Criteria");
        return vwClassificationRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of vwClassifications available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return vwClassificationRepository.count();
    }

    /**
     * Get one vwClassification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<VwClassificationDTO> findOne(Long id) {
        log.debug("Request to get VwClassification : {}", id);
        return vwClassificationRepository.findById(id).map(vwClassificationMapper::toDto);
    }

    /**
     * Delete the vwClassification by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete VwClassification : {}", id);
        return vwClassificationRepository.deleteById(id);
    }
}
