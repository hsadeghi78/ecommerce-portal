package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.FactorCriteria;
import com.hs.ec.portal.repository.FactorRepository;
import com.hs.ec.portal.service.dto.FactorDTO;
import com.hs.ec.portal.service.mapper.FactorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.Factor}.
 */
@Service
@Transactional
public class FactorService {

    private final Logger log = LoggerFactory.getLogger(FactorService.class);

    private final FactorRepository factorRepository;

    private final FactorMapper factorMapper;

    public FactorService(FactorRepository factorRepository, FactorMapper factorMapper) {
        this.factorRepository = factorRepository;
        this.factorMapper = factorMapper;
    }

    /**
     * Save a factor.
     *
     * @param factorDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FactorDTO> save(FactorDTO factorDTO) {
        log.debug("Request to save Factor : {}", factorDTO);
        return factorRepository.save(factorMapper.toEntity(factorDTO)).map(factorMapper::toDto);
    }

    /**
     * Update a factor.
     *
     * @param factorDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FactorDTO> update(FactorDTO factorDTO) {
        log.debug("Request to update Factor : {}", factorDTO);
        return factorRepository.save(factorMapper.toEntity(factorDTO)).map(factorMapper::toDto);
    }

    /**
     * Partially update a factor.
     *
     * @param factorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<FactorDTO> partialUpdate(FactorDTO factorDTO) {
        log.debug("Request to partially update Factor : {}", factorDTO);

        return factorRepository
            .findById(factorDTO.getId())
            .map(existingFactor -> {
                factorMapper.partialUpdate(existingFactor, factorDTO);

                return existingFactor;
            })
            .flatMap(factorRepository::save)
            .map(factorMapper::toDto);
    }

    /**
     * Get all the factors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<FactorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Factors");
        return factorRepository.findAllBy(pageable).map(factorMapper::toDto);
    }

    /**
     * Find factors by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<FactorDTO> findByCriteria(FactorCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Factors by Criteria");
        return factorRepository.findByCriteria(criteria, pageable).map(factorMapper::toDto);
    }

    /**
     * Find the count of factors by criteria.
     * @param criteria filtering criteria
     * @return the count of factors
     */
    public Mono<Long> countByCriteria(FactorCriteria criteria) {
        log.debug("Request to get the count of all Factors by Criteria");
        return factorRepository.countByCriteria(criteria);
    }

    /**
     * Get all the factors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<FactorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return factorRepository.findAllWithEagerRelationships(pageable).map(factorMapper::toDto);
    }

    /**
     * Returns the number of factors available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return factorRepository.count();
    }

    /**
     * Get one factor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<FactorDTO> findOne(Long id) {
        log.debug("Request to get Factor : {}", id);
        return factorRepository.findOneWithEagerRelationships(id).map(factorMapper::toDto);
    }

    /**
     * Delete the factor by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Factor : {}", id);
        return factorRepository.deleteById(id);
    }
}
