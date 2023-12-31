package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.FactorItem;
import com.hs.ec.portal.domain.criteria.FactorItemCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the FactorItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactorItemRepository extends ReactiveCrudRepository<FactorItem, Long>, FactorItemRepositoryInternal {
    Flux<FactorItem> findAllBy(Pageable pageable);

    @Override
    Mono<FactorItem> findOneWithEagerRelationships(Long id);

    @Override
    Flux<FactorItem> findAllWithEagerRelationships();

    @Override
    Flux<FactorItem> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM factor_item entity WHERE entity.factor_id = :id")
    Flux<FactorItem> findByFactor(Long id);

    @Query("SELECT * FROM factor_item entity WHERE entity.factor_id IS NULL")
    Flux<FactorItem> findAllWhereFactorIsNull();

    @Query("SELECT * FROM factor_item entity WHERE entity.product_id = :id")
    Flux<FactorItem> findByProduct(Long id);

    @Query("SELECT * FROM factor_item entity WHERE entity.product_id IS NULL")
    Flux<FactorItem> findAllWhereProductIsNull();

    @Override
    <S extends FactorItem> Mono<S> save(S entity);

    @Override
    Flux<FactorItem> findAll();

    @Override
    Mono<FactorItem> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FactorItemRepositoryInternal {
    <S extends FactorItem> Mono<S> save(S entity);

    Flux<FactorItem> findAllBy(Pageable pageable);

    Flux<FactorItem> findAll();

    Mono<FactorItem> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<FactorItem> findAllBy(Pageable pageable, Criteria criteria);
    Flux<FactorItem> findByCriteria(FactorItemCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(FactorItemCriteria criteria);

    Mono<FactorItem> findOneWithEagerRelationships(Long id);

    Flux<FactorItem> findAllWithEagerRelationships();

    Flux<FactorItem> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
