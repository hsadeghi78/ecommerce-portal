package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.ConsumeMaterial;
import com.hs.ec.portal.domain.criteria.ConsumeMaterialCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ConsumeMaterial entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsumeMaterialRepository extends ReactiveCrudRepository<ConsumeMaterial, Long>, ConsumeMaterialRepositoryInternal {
    Flux<ConsumeMaterial> findAllBy(Pageable pageable);

    @Override
    Mono<ConsumeMaterial> findOneWithEagerRelationships(Long id);

    @Override
    Flux<ConsumeMaterial> findAllWithEagerRelationships();

    @Override
    Flux<ConsumeMaterial> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM consume_material entity WHERE entity.product_id = :id")
    Flux<ConsumeMaterial> findByProduct(Long id);

    @Query("SELECT * FROM consume_material entity WHERE entity.product_id IS NULL")
    Flux<ConsumeMaterial> findAllWhereProductIsNull();

    @Override
    <S extends ConsumeMaterial> Mono<S> save(S entity);

    @Override
    Flux<ConsumeMaterial> findAll();

    @Override
    Mono<ConsumeMaterial> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ConsumeMaterialRepositoryInternal {
    <S extends ConsumeMaterial> Mono<S> save(S entity);

    Flux<ConsumeMaterial> findAllBy(Pageable pageable);

    Flux<ConsumeMaterial> findAll();

    Mono<ConsumeMaterial> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ConsumeMaterial> findAllBy(Pageable pageable, Criteria criteria);
    Flux<ConsumeMaterial> findByCriteria(ConsumeMaterialCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ConsumeMaterialCriteria criteria);

    Mono<ConsumeMaterial> findOneWithEagerRelationships(Long id);

    Flux<ConsumeMaterial> findAllWithEagerRelationships();

    Flux<ConsumeMaterial> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
