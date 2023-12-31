package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Classification;
import com.hs.ec.portal.domain.criteria.ClassificationCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Classification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassificationRepository extends ReactiveCrudRepository<Classification, Long>, ClassificationRepositoryInternal {
    Flux<Classification> findAllBy(Pageable pageable);

    @Override
    Mono<Classification> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Classification> findAllWithEagerRelationships();

    @Override
    Flux<Classification> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM classification entity WHERE entity.class_type_id = :id")
    Flux<Classification> findByClassType(Long id);

    @Query("SELECT * FROM classification entity WHERE entity.class_type_id IS NULL")
    Flux<Classification> findAllWhereClassTypeIsNull();

    @Override
    <S extends Classification> Mono<S> save(S entity);

    @Override
    Flux<Classification> findAll();

    @Override
    Mono<Classification> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ClassificationRepositoryInternal {
    <S extends Classification> Mono<S> save(S entity);

    Flux<Classification> findAllBy(Pageable pageable);

    Flux<Classification> findAll();

    Mono<Classification> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Classification> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Classification> findByCriteria(ClassificationCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ClassificationCriteria criteria);

    Mono<Classification> findOneWithEagerRelationships(Long id);

    Flux<Classification> findAllWithEagerRelationships();

    Flux<Classification> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
