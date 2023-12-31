package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Factor;
import com.hs.ec.portal.domain.criteria.FactorCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Factor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactorRepository extends ReactiveCrudRepository<Factor, Long>, FactorRepositoryInternal {
    Flux<Factor> findAllBy(Pageable pageable);

    @Override
    Mono<Factor> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Factor> findAllWithEagerRelationships();

    @Override
    Flux<Factor> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM factor entity WHERE entity.location_id = :id")
    Flux<Factor> findByLocation(Long id);

    @Query("SELECT * FROM factor entity WHERE entity.location_id IS NULL")
    Flux<Factor> findAllWhereLocationIsNull();

    @Query("SELECT * FROM factor entity WHERE entity.buyer_party_id = :id")
    Flux<Factor> findByBuyerParty(Long id);

    @Query("SELECT * FROM factor entity WHERE entity.buyer_party_id IS NULL")
    Flux<Factor> findAllWhereBuyerPartyIsNull();

    @Query("SELECT * FROM factor entity WHERE entity.seller_party_id = :id")
    Flux<Factor> findBySellerParty(Long id);

    @Query("SELECT * FROM factor entity WHERE entity.seller_party_id IS NULL")
    Flux<Factor> findAllWhereSellerPartyIsNull();

    @Override
    <S extends Factor> Mono<S> save(S entity);

    @Override
    Flux<Factor> findAll();

    @Override
    Mono<Factor> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FactorRepositoryInternal {
    <S extends Factor> Mono<S> save(S entity);

    Flux<Factor> findAllBy(Pageable pageable);

    Flux<Factor> findAll();

    Mono<Factor> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Factor> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Factor> findByCriteria(FactorCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(FactorCriteria criteria);

    Mono<Factor> findOneWithEagerRelationships(Long id);

    Flux<Factor> findAllWithEagerRelationships();

    Flux<Factor> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
