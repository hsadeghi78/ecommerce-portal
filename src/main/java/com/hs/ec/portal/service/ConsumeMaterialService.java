package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.ConsumeMaterialCriteria;
import com.hs.ec.portal.repository.ConsumeMaterialRepository;
import com.hs.ec.portal.service.dto.ConsumeMaterialDTO;
import com.hs.ec.portal.service.mapper.ConsumeMaterialMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.ConsumeMaterial}.
 */
@Service
@Transactional
public class ConsumeMaterialService {

    private final Logger log = LoggerFactory.getLogger(ConsumeMaterialService.class);

    private final ConsumeMaterialRepository consumeMaterialRepository;

    private final ConsumeMaterialMapper consumeMaterialMapper;

    public ConsumeMaterialService(ConsumeMaterialRepository consumeMaterialRepository, ConsumeMaterialMapper consumeMaterialMapper) {
        this.consumeMaterialRepository = consumeMaterialRepository;
        this.consumeMaterialMapper = consumeMaterialMapper;
    }

    /**
     * Save a consumeMaterial.
     *
     * @param consumeMaterialDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ConsumeMaterialDTO> save(ConsumeMaterialDTO consumeMaterialDTO) {
        log.debug("Request to save ConsumeMaterial : {}", consumeMaterialDTO);
        return consumeMaterialRepository.save(consumeMaterialMapper.toEntity(consumeMaterialDTO)).map(consumeMaterialMapper::toDto);
    }

    /**
     * Update a consumeMaterial.
     *
     * @param consumeMaterialDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ConsumeMaterialDTO> update(ConsumeMaterialDTO consumeMaterialDTO) {
        log.debug("Request to update ConsumeMaterial : {}", consumeMaterialDTO);
        return consumeMaterialRepository.save(consumeMaterialMapper.toEntity(consumeMaterialDTO)).map(consumeMaterialMapper::toDto);
    }

    /**
     * Partially update a consumeMaterial.
     *
     * @param consumeMaterialDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ConsumeMaterialDTO> partialUpdate(ConsumeMaterialDTO consumeMaterialDTO) {
        log.debug("Request to partially update ConsumeMaterial : {}", consumeMaterialDTO);

        return consumeMaterialRepository
            .findById(consumeMaterialDTO.getId())
            .map(existingConsumeMaterial -> {
                consumeMaterialMapper.partialUpdate(existingConsumeMaterial, consumeMaterialDTO);

                return existingConsumeMaterial;
            })
            .flatMap(consumeMaterialRepository::save)
            .map(consumeMaterialMapper::toDto);
    }

    /**
     * Get all the consumeMaterials.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ConsumeMaterialDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConsumeMaterials");
        return consumeMaterialRepository.findAllBy(pageable).map(consumeMaterialMapper::toDto);
    }

    /**
     * Find consumeMaterials by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ConsumeMaterialDTO> findByCriteria(ConsumeMaterialCriteria criteria, Pageable pageable) {
        log.debug("Request to get all ConsumeMaterials by Criteria");
        return consumeMaterialRepository.findByCriteria(criteria, pageable).map(consumeMaterialMapper::toDto);
    }

    /**
     * Find the count of consumeMaterials by criteria.
     * @param criteria filtering criteria
     * @return the count of consumeMaterials
     */
    public Mono<Long> countByCriteria(ConsumeMaterialCriteria criteria) {
        log.debug("Request to get the count of all ConsumeMaterials by Criteria");
        return consumeMaterialRepository.countByCriteria(criteria);
    }

    /**
     * Get all the consumeMaterials with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<ConsumeMaterialDTO> findAllWithEagerRelationships(Pageable pageable) {
        return consumeMaterialRepository.findAllWithEagerRelationships(pageable).map(consumeMaterialMapper::toDto);
    }

    /**
     * Returns the number of consumeMaterials available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return consumeMaterialRepository.count();
    }

    /**
     * Get one consumeMaterial by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ConsumeMaterialDTO> findOne(Long id) {
        log.debug("Request to get ConsumeMaterial : {}", id);
        return consumeMaterialRepository.findOneWithEagerRelationships(id).map(consumeMaterialMapper::toDto);
    }

    /**
     * Delete the consumeMaterial by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ConsumeMaterial : {}", id);
        return consumeMaterialRepository.deleteById(id);
    }
}
