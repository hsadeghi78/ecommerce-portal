package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.GeoDivisionCriteria;
import com.hs.ec.portal.repository.GeoDivisionRepository;
import com.hs.ec.portal.service.dto.GeoDivisionDTO;
import com.hs.ec.portal.service.mapper.GeoDivisionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.GeoDivision}.
 */
@Service
@Transactional
public class GeoDivisionService {

    private final Logger log = LoggerFactory.getLogger(GeoDivisionService.class);

    private final GeoDivisionRepository geoDivisionRepository;

    private final GeoDivisionMapper geoDivisionMapper;

    public GeoDivisionService(GeoDivisionRepository geoDivisionRepository, GeoDivisionMapper geoDivisionMapper) {
        this.geoDivisionRepository = geoDivisionRepository;
        this.geoDivisionMapper = geoDivisionMapper;
    }

    /**
     * Save a geoDivision.
     *
     * @param geoDivisionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<GeoDivisionDTO> save(GeoDivisionDTO geoDivisionDTO) {
        log.debug("Request to save GeoDivision : {}", geoDivisionDTO);
        return geoDivisionRepository.save(geoDivisionMapper.toEntity(geoDivisionDTO)).map(geoDivisionMapper::toDto);
    }

    /**
     * Update a geoDivision.
     *
     * @param geoDivisionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<GeoDivisionDTO> update(GeoDivisionDTO geoDivisionDTO) {
        log.debug("Request to update GeoDivision : {}", geoDivisionDTO);
        return geoDivisionRepository.save(geoDivisionMapper.toEntity(geoDivisionDTO)).map(geoDivisionMapper::toDto);
    }

    /**
     * Partially update a geoDivision.
     *
     * @param geoDivisionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<GeoDivisionDTO> partialUpdate(GeoDivisionDTO geoDivisionDTO) {
        log.debug("Request to partially update GeoDivision : {}", geoDivisionDTO);

        return geoDivisionRepository
            .findById(geoDivisionDTO.getId())
            .map(existingGeoDivision -> {
                geoDivisionMapper.partialUpdate(existingGeoDivision, geoDivisionDTO);

                return existingGeoDivision;
            })
            .flatMap(geoDivisionRepository::save)
            .map(geoDivisionMapper::toDto);
    }

    /**
     * Get all the geoDivisions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<GeoDivisionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GeoDivisions");
        return geoDivisionRepository.findAllBy(pageable).map(geoDivisionMapper::toDto);
    }

    /**
     * Find geoDivisions by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<GeoDivisionDTO> findByCriteria(GeoDivisionCriteria criteria, Pageable pageable) {
        log.debug("Request to get all GeoDivisions by Criteria");
        return geoDivisionRepository.findByCriteria(criteria, pageable).map(geoDivisionMapper::toDto);
    }

    /**
     * Find the count of geoDivisions by criteria.
     * @param criteria filtering criteria
     * @return the count of geoDivisions
     */
    public Mono<Long> countByCriteria(GeoDivisionCriteria criteria) {
        log.debug("Request to get the count of all GeoDivisions by Criteria");
        return geoDivisionRepository.countByCriteria(criteria);
    }

    /**
     * Get all the geoDivisions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<GeoDivisionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return geoDivisionRepository.findAllWithEagerRelationships(pageable).map(geoDivisionMapper::toDto);
    }

    /**
     * Returns the number of geoDivisions available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return geoDivisionRepository.count();
    }

    /**
     * Get one geoDivision by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<GeoDivisionDTO> findOne(Long id) {
        log.debug("Request to get GeoDivision : {}", id);
        return geoDivisionRepository.findOneWithEagerRelationships(id).map(geoDivisionMapper::toDto);
    }

    /**
     * Delete the geoDivision by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete GeoDivision : {}", id);
        return geoDivisionRepository.deleteById(id);
    }
}
