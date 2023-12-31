package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.ProductItem;
import com.hs.ec.portal.domain.criteria.ProductItemCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ProductItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductItemRepository extends ReactiveCrudRepository<ProductItem, Long>, ProductItemRepositoryInternal {
    Flux<ProductItem> findAllBy(Pageable pageable);

    @Override
    Mono<ProductItem> findOneWithEagerRelationships(Long id);

    @Override
    Flux<ProductItem> findAllWithEagerRelationships();

    @Override
    Flux<ProductItem> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM product_item entity WHERE entity.product_id = :id")
    Flux<ProductItem> findByProduct(Long id);

    @Query("SELECT * FROM product_item entity WHERE entity.product_id IS NULL")
    Flux<ProductItem> findAllWhereProductIsNull();

    @Override
    <S extends ProductItem> Mono<S> save(S entity);

    @Override
    Flux<ProductItem> findAll();

    @Override
    Mono<ProductItem> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ProductItemRepositoryInternal {
    <S extends ProductItem> Mono<S> save(S entity);

    Flux<ProductItem> findAllBy(Pageable pageable);

    Flux<ProductItem> findAll();

    Mono<ProductItem> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ProductItem> findAllBy(Pageable pageable, Criteria criteria);
    Flux<ProductItem> findByCriteria(ProductItemCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ProductItemCriteria criteria);

    Mono<ProductItem> findOneWithEagerRelationships(Long id);

    Flux<ProductItem> findAllWithEagerRelationships();

    Flux<ProductItem> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
