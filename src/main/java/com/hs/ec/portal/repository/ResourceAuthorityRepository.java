package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.ResourceAuthority;
import com.hs.ec.portal.domain.criteria.ResourceAuthorityCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ResourceAuthority entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceAuthorityRepository extends ReactiveCrudRepository<ResourceAuthority, Long>, ResourceAuthorityRepositoryInternal {
    Flux<ResourceAuthority> findAllBy(Pageable pageable);

    @Override
    Mono<ResourceAuthority> findOneWithEagerRelationships(Long id);

    @Override
    Flux<ResourceAuthority> findAllWithEagerRelationships();

    @Override
    Flux<ResourceAuthority> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM resource_authority entity WHERE entity.resource_id = :id")
    Flux<ResourceAuthority> findByResource(Long id);

    @Query("SELECT * FROM resource_authority entity WHERE entity.resource_id IS NULL")
    Flux<ResourceAuthority> findAllWhereResourceIsNull();

    @Query("SELECT * FROM resource_authority entity WHERE entity.my_authority_id = :id")
    Flux<ResourceAuthority> findByMyAuthority(Long id);

    @Query("SELECT * FROM resource_authority entity WHERE entity.my_authority_id IS NULL")
    Flux<ResourceAuthority> findAllWhereMyAuthorityIsNull();

    @Override
    <S extends ResourceAuthority> Mono<S> save(S entity);

    @Override
    Flux<ResourceAuthority> findAll();

    @Override
    Mono<ResourceAuthority> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ResourceAuthorityRepositoryInternal {
    <S extends ResourceAuthority> Mono<S> save(S entity);

    Flux<ResourceAuthority> findAllBy(Pageable pageable);

    Flux<ResourceAuthority> findAll();

    Mono<ResourceAuthority> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ResourceAuthority> findAllBy(Pageable pageable, Criteria criteria);
    Flux<ResourceAuthority> findByCriteria(ResourceAuthorityCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ResourceAuthorityCriteria criteria);

    Mono<ResourceAuthority> findOneWithEagerRelationships(Long id);

    Flux<ResourceAuthority> findAllWithEagerRelationships();

    Flux<ResourceAuthority> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
