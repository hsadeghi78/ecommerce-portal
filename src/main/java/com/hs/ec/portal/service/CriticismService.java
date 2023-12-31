package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.CriticismCriteria;
import com.hs.ec.portal.repository.CriticismRepository;
import com.hs.ec.portal.service.dto.CriticismDTO;
import com.hs.ec.portal.service.mapper.CriticismMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.Criticism}.
 */
@Service
@Transactional
public class CriticismService {

    private final Logger log = LoggerFactory.getLogger(CriticismService.class);

    private final CriticismRepository criticismRepository;

    private final CriticismMapper criticismMapper;

    public CriticismService(CriticismRepository criticismRepository, CriticismMapper criticismMapper) {
        this.criticismRepository = criticismRepository;
        this.criticismMapper = criticismMapper;
    }

    /**
     * Save a criticism.
     *
     * @param criticismDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CriticismDTO> save(CriticismDTO criticismDTO) {
        log.debug("Request to save Criticism : {}", criticismDTO);
        return criticismRepository.save(criticismMapper.toEntity(criticismDTO)).map(criticismMapper::toDto);
    }

    /**
     * Update a criticism.
     *
     * @param criticismDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CriticismDTO> update(CriticismDTO criticismDTO) {
        log.debug("Request to update Criticism : {}", criticismDTO);
        return criticismRepository.save(criticismMapper.toEntity(criticismDTO)).map(criticismMapper::toDto);
    }

    /**
     * Partially update a criticism.
     *
     * @param criticismDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CriticismDTO> partialUpdate(CriticismDTO criticismDTO) {
        log.debug("Request to partially update Criticism : {}", criticismDTO);

        return criticismRepository
            .findById(criticismDTO.getId())
            .map(existingCriticism -> {
                criticismMapper.partialUpdate(existingCriticism, criticismDTO);

                return existingCriticism;
            })
            .flatMap(criticismRepository::save)
            .map(criticismMapper::toDto);
    }

    /**
     * Get all the criticisms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CriticismDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Criticisms");
        return criticismRepository.findAllBy(pageable).map(criticismMapper::toDto);
    }

    /**
     * Find criticisms by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CriticismDTO> findByCriteria(CriticismCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Criticisms by Criteria");
        return criticismRepository.findByCriteria(criteria, pageable).map(criticismMapper::toDto);
    }

    /**
     * Find the count of criticisms by criteria.
     * @param criteria filtering criteria
     * @return the count of criticisms
     */
    public Mono<Long> countByCriteria(CriticismCriteria criteria) {
        log.debug("Request to get the count of all Criticisms by Criteria");
        return criticismRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of criticisms available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return criticismRepository.count();
    }

    /**
     * Get one criticism by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CriticismDTO> findOne(Long id) {
        log.debug("Request to get Criticism : {}", id);
        return criticismRepository.findById(id).map(criticismMapper::toDto);
    }

    /**
     * Delete the criticism by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Criticism : {}", id);
        return criticismRepository.deleteById(id);
    }
}
