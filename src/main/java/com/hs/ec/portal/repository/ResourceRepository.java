package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Resource;
import com.hs.ec.portal.domain.criteria.ResourceCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Resource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceRepository extends ReactiveCrudRepository<Resource, Long>, ResourceRepositoryInternal {
    Flux<Resource> findAllBy(Pageable pageable);

    @Override
    <S extends Resource> Mono<S> save(S entity);

    @Override
    Flux<Resource> findAll();

    @Override
    Mono<Resource> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ResourceRepositoryInternal {
    <S extends Resource> Mono<S> save(S entity);

    Flux<Resource> findAllBy(Pageable pageable);

    Flux<Resource> findAll();

    Mono<Resource> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Resource> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Resource> findByCriteria(ResourceCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ResourceCriteria criteria);
}
