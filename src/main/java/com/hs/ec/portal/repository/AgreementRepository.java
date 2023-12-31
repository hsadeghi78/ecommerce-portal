package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Agreement;
import com.hs.ec.portal.domain.criteria.AgreementCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Agreement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgreementRepository extends ReactiveCrudRepository<Agreement, Long>, AgreementRepositoryInternal {
    Flux<Agreement> findAllBy(Pageable pageable);

    @Override
    Mono<Agreement> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Agreement> findAllWithEagerRelationships();

    @Override
    Flux<Agreement> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM agreement entity WHERE entity.provider_id = :id")
    Flux<Agreement> findByProvider(Long id);

    @Query("SELECT * FROM agreement entity WHERE entity.provider_id IS NULL")
    Flux<Agreement> findAllWhereProviderIsNull();

    @Query("SELECT * FROM agreement entity WHERE entity.consumer_id = :id")
    Flux<Agreement> findByConsumer(Long id);

    @Query("SELECT * FROM agreement entity WHERE entity.consumer_id IS NULL")
    Flux<Agreement> findAllWhereConsumerIsNull();

    @Override
    <S extends Agreement> Mono<S> save(S entity);

    @Override
    Flux<Agreement> findAll();

    @Override
    Mono<Agreement> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AgreementRepositoryInternal {
    <S extends Agreement> Mono<S> save(S entity);

    Flux<Agreement> findAllBy(Pageable pageable);

    Flux<Agreement> findAll();

    Mono<Agreement> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Agreement> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Agreement> findByCriteria(AgreementCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(AgreementCriteria criteria);

    Mono<Agreement> findOneWithEagerRelationships(Long id);

    Flux<Agreement> findAllWithEagerRelationships();

    Flux<Agreement> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
