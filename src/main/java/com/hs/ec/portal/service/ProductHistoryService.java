package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.ProductHistoryCriteria;
import com.hs.ec.portal.repository.ProductHistoryRepository;
import com.hs.ec.portal.service.dto.ProductHistoryDTO;
import com.hs.ec.portal.service.mapper.ProductHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.ProductHistory}.
 */
@Service
@Transactional
public class ProductHistoryService {

    private final Logger log = LoggerFactory.getLogger(ProductHistoryService.class);

    private final ProductHistoryRepository productHistoryRepository;

    private final ProductHistoryMapper productHistoryMapper;

    public ProductHistoryService(ProductHistoryRepository productHistoryRepository, ProductHistoryMapper productHistoryMapper) {
        this.productHistoryRepository = productHistoryRepository;
        this.productHistoryMapper = productHistoryMapper;
    }

    /**
     * Save a productHistory.
     *
     * @param productHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProductHistoryDTO> save(ProductHistoryDTO productHistoryDTO) {
        log.debug("Request to save ProductHistory : {}", productHistoryDTO);
        return productHistoryRepository.save(productHistoryMapper.toEntity(productHistoryDTO)).map(productHistoryMapper::toDto);
    }

    /**
     * Update a productHistory.
     *
     * @param productHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProductHistoryDTO> update(ProductHistoryDTO productHistoryDTO) {
        log.debug("Request to update ProductHistory : {}", productHistoryDTO);
        return productHistoryRepository.save(productHistoryMapper.toEntity(productHistoryDTO)).map(productHistoryMapper::toDto);
    }

    /**
     * Partially update a productHistory.
     *
     * @param productHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ProductHistoryDTO> partialUpdate(ProductHistoryDTO productHistoryDTO) {
        log.debug("Request to partially update ProductHistory : {}", productHistoryDTO);

        return productHistoryRepository
            .findById(productHistoryDTO.getId())
            .map(existingProductHistory -> {
                productHistoryMapper.partialUpdate(existingProductHistory, productHistoryDTO);

                return existingProductHistory;
            })
            .flatMap(productHistoryRepository::save)
            .map(productHistoryMapper::toDto);
    }

    /**
     * Get all the productHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ProductHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductHistories");
        return productHistoryRepository.findAllBy(pageable).map(productHistoryMapper::toDto);
    }

    /**
     * Find productHistories by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ProductHistoryDTO> findByCriteria(ProductHistoryCriteria criteria, Pageable pageable) {
        log.debug("Request to get all ProductHistories by Criteria");
        return productHistoryRepository.findByCriteria(criteria, pageable).map(productHistoryMapper::toDto);
    }

    /**
     * Find the count of productHistories by criteria.
     * @param criteria filtering criteria
     * @return the count of productHistories
     */
    public Mono<Long> countByCriteria(ProductHistoryCriteria criteria) {
        log.debug("Request to get the count of all ProductHistories by Criteria");
        return productHistoryRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of productHistories available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return productHistoryRepository.count();
    }

    /**
     * Get one productHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ProductHistoryDTO> findOne(Long id) {
        log.debug("Request to get ProductHistory : {}", id);
        return productHistoryRepository.findById(id).map(productHistoryMapper::toDto);
    }

    /**
     * Delete the productHistory by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ProductHistory : {}", id);
        return productHistoryRepository.deleteById(id);
    }
}
