package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.ProductCriteria;
import com.hs.ec.portal.repository.ProductRepository;
import com.hs.ec.portal.service.dto.ProductDTO;
import com.hs.ec.portal.service.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.Product}.
 */
@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Save a product.
     *
     * @param productDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProductDTO> save(ProductDTO productDTO) {
        log.debug("Request to save Product : {}", productDTO);
        return productRepository.save(productMapper.toEntity(productDTO)).map(productMapper::toDto);
    }

    /**
     * Update a product.
     *
     * @param productDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProductDTO> update(ProductDTO productDTO) {
        log.debug("Request to update Product : {}", productDTO);
        return productRepository.save(productMapper.toEntity(productDTO)).map(productMapper::toDto);
    }

    /**
     * Partially update a product.
     *
     * @param productDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ProductDTO> partialUpdate(ProductDTO productDTO) {
        log.debug("Request to partially update Product : {}", productDTO);

        return productRepository
            .findById(productDTO.getId())
            .map(existingProduct -> {
                productMapper.partialUpdate(existingProduct, productDTO);

                return existingProduct;
            })
            .flatMap(productRepository::save)
            .map(productMapper::toDto);
    }

    /**
     * Get all the products.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findAllBy(pageable).map(productMapper::toDto);
    }

    /**
     * Find products by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ProductDTO> findByCriteria(ProductCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Products by Criteria");
        return productRepository.findByCriteria(criteria, pageable).map(productMapper::toDto);
    }

    /**
     * Find the count of products by criteria.
     * @param criteria filtering criteria
     * @return the count of products
     */
    public Mono<Long> countByCriteria(ProductCriteria criteria) {
        log.debug("Request to get the count of all Products by Criteria");
        return productRepository.countByCriteria(criteria);
    }

    /**
     * Get all the products with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<ProductDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productRepository.findAllWithEagerRelationships(pageable).map(productMapper::toDto);
    }

    /**
     * Returns the number of products available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return productRepository.count();
    }

    /**
     * Get one product by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ProductDTO> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findOneWithEagerRelationships(id).map(productMapper::toDto);
    }

    /**
     * Delete the product by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        return productRepository.deleteById(id);
    }
}
