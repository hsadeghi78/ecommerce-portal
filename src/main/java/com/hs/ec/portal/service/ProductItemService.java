package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.ProductItemCriteria;
import com.hs.ec.portal.repository.ProductItemRepository;
import com.hs.ec.portal.service.dto.ProductItemDTO;
import com.hs.ec.portal.service.mapper.ProductItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.ProductItem}.
 */
@Service
@Transactional
public class ProductItemService {

    private final Logger log = LoggerFactory.getLogger(ProductItemService.class);

    private final ProductItemRepository productItemRepository;

    private final ProductItemMapper productItemMapper;

    public ProductItemService(ProductItemRepository productItemRepository, ProductItemMapper productItemMapper) {
        this.productItemRepository = productItemRepository;
        this.productItemMapper = productItemMapper;
    }

    /**
     * Save a productItem.
     *
     * @param productItemDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProductItemDTO> save(ProductItemDTO productItemDTO) {
        log.debug("Request to save ProductItem : {}", productItemDTO);
        return productItemRepository.save(productItemMapper.toEntity(productItemDTO)).map(productItemMapper::toDto);
    }

    /**
     * Update a productItem.
     *
     * @param productItemDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProductItemDTO> update(ProductItemDTO productItemDTO) {
        log.debug("Request to update ProductItem : {}", productItemDTO);
        return productItemRepository.save(productItemMapper.toEntity(productItemDTO)).map(productItemMapper::toDto);
    }

    /**
     * Partially update a productItem.
     *
     * @param productItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ProductItemDTO> partialUpdate(ProductItemDTO productItemDTO) {
        log.debug("Request to partially update ProductItem : {}", productItemDTO);

        return productItemRepository
            .findById(productItemDTO.getId())
            .map(existingProductItem -> {
                productItemMapper.partialUpdate(existingProductItem, productItemDTO);

                return existingProductItem;
            })
            .flatMap(productItemRepository::save)
            .map(productItemMapper::toDto);
    }

    /**
     * Get all the productItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ProductItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductItems");
        return productItemRepository.findAllBy(pageable).map(productItemMapper::toDto);
    }

    /**
     * Find productItems by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ProductItemDTO> findByCriteria(ProductItemCriteria criteria, Pageable pageable) {
        log.debug("Request to get all ProductItems by Criteria");
        return productItemRepository.findByCriteria(criteria, pageable).map(productItemMapper::toDto);
    }

    /**
     * Find the count of productItems by criteria.
     * @param criteria filtering criteria
     * @return the count of productItems
     */
    public Mono<Long> countByCriteria(ProductItemCriteria criteria) {
        log.debug("Request to get the count of all ProductItems by Criteria");
        return productItemRepository.countByCriteria(criteria);
    }

    /**
     * Get all the productItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<ProductItemDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productItemRepository.findAllWithEagerRelationships(pageable).map(productItemMapper::toDto);
    }

    /**
     * Returns the number of productItems available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return productItemRepository.count();
    }

    /**
     * Get one productItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ProductItemDTO> findOne(Long id) {
        log.debug("Request to get ProductItem : {}", id);
        return productItemRepository.findOneWithEagerRelationships(id).map(productItemMapper::toDto);
    }

    /**
     * Delete the productItem by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ProductItem : {}", id);
        return productItemRepository.deleteById(id);
    }
}
