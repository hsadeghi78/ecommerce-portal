package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.FactorItemCriteria;
import com.hs.ec.portal.repository.FactorItemRepository;
import com.hs.ec.portal.service.dto.FactorItemDTO;
import com.hs.ec.portal.service.mapper.FactorItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.FactorItem}.
 */
@Service
@Transactional
public class FactorItemService {

    private final Logger log = LoggerFactory.getLogger(FactorItemService.class);

    private final FactorItemRepository factorItemRepository;

    private final FactorItemMapper factorItemMapper;

    public FactorItemService(FactorItemRepository factorItemRepository, FactorItemMapper factorItemMapper) {
        this.factorItemRepository = factorItemRepository;
        this.factorItemMapper = factorItemMapper;
    }

    /**
     * Save a factorItem.
     *
     * @param factorItemDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FactorItemDTO> save(FactorItemDTO factorItemDTO) {
        log.debug("Request to save FactorItem : {}", factorItemDTO);
        return factorItemRepository.save(factorItemMapper.toEntity(factorItemDTO)).map(factorItemMapper::toDto);
    }

    /**
     * Update a factorItem.
     *
     * @param factorItemDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FactorItemDTO> update(FactorItemDTO factorItemDTO) {
        log.debug("Request to update FactorItem : {}", factorItemDTO);
        return factorItemRepository.save(factorItemMapper.toEntity(factorItemDTO)).map(factorItemMapper::toDto);
    }

    /**
     * Partially update a factorItem.
     *
     * @param factorItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<FactorItemDTO> partialUpdate(FactorItemDTO factorItemDTO) {
        log.debug("Request to partially update FactorItem : {}", factorItemDTO);

        return factorItemRepository
            .findById(factorItemDTO.getId())
            .map(existingFactorItem -> {
                factorItemMapper.partialUpdate(existingFactorItem, factorItemDTO);

                return existingFactorItem;
            })
            .flatMap(factorItemRepository::save)
            .map(factorItemMapper::toDto);
    }

    /**
     * Get all the factorItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<FactorItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FactorItems");
        return factorItemRepository.findAllBy(pageable).map(factorItemMapper::toDto);
    }

    /**
     * Find factorItems by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<FactorItemDTO> findByCriteria(FactorItemCriteria criteria, Pageable pageable) {
        log.debug("Request to get all FactorItems by Criteria");
        return factorItemRepository.findByCriteria(criteria, pageable).map(factorItemMapper::toDto);
    }

    /**
     * Find the count of factorItems by criteria.
     * @param criteria filtering criteria
     * @return the count of factorItems
     */
    public Mono<Long> countByCriteria(FactorItemCriteria criteria) {
        log.debug("Request to get the count of all FactorItems by Criteria");
        return factorItemRepository.countByCriteria(criteria);
    }

    /**
     * Get all the factorItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<FactorItemDTO> findAllWithEagerRelationships(Pageable pageable) {
        return factorItemRepository.findAllWithEagerRelationships(pageable).map(factorItemMapper::toDto);
    }

    /**
     * Returns the number of factorItems available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return factorItemRepository.count();
    }

    /**
     * Get one factorItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<FactorItemDTO> findOne(Long id) {
        log.debug("Request to get FactorItem : {}", id);
        return factorItemRepository.findOneWithEagerRelationships(id).map(factorItemMapper::toDto);
    }

    /**
     * Delete the factorItem by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete FactorItem : {}", id);
        return factorItemRepository.deleteById(id);
    }
}
