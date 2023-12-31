package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Location;
import com.hs.ec.portal.domain.criteria.LocationCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Location entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationRepository extends ReactiveCrudRepository<Location, Long>, LocationRepositoryInternal {
    Flux<Location> findAllBy(Pageable pageable);

    @Override
    Mono<Location> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Location> findAllWithEagerRelationships();

    @Override
    Flux<Location> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM location entity WHERE entity.id not in (select factor_id from factor)")
    Flux<Location> findAllWhereFactorIsNull();

    @Query("SELECT * FROM location entity WHERE entity.geo_division_id = :id")
    Flux<Location> findByGeoDivision(Long id);

    @Query("SELECT * FROM location entity WHERE entity.geo_division_id IS NULL")
    Flux<Location> findAllWhereGeoDivisionIsNull();

    @Query("SELECT * FROM location entity WHERE entity.party_id = :id")
    Flux<Location> findByParty(Long id);

    @Query("SELECT * FROM location entity WHERE entity.party_id IS NULL")
    Flux<Location> findAllWherePartyIsNull();

    @Override
    <S extends Location> Mono<S> save(S entity);

    @Override
    Flux<Location> findAll();

    @Override
    Mono<Location> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface LocationRepositoryInternal {
    <S extends Location> Mono<S> save(S entity);

    Flux<Location> findAllBy(Pageable pageable);

    Flux<Location> findAll();

    Mono<Location> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Location> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Location> findByCriteria(LocationCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(LocationCriteria criteria);

    Mono<Location> findOneWithEagerRelationships(Long id);

    Flux<Location> findAllWithEagerRelationships();

    Flux<Location> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
