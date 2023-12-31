package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Criticism;
import com.hs.ec.portal.domain.criteria.CriticismCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Criticism entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CriticismRepository extends ReactiveCrudRepository<Criticism, Long>, CriticismRepositoryInternal {
    Flux<Criticism> findAllBy(Pageable pageable);

    @Override
    <S extends Criticism> Mono<S> save(S entity);

    @Override
    Flux<Criticism> findAll();

    @Override
    Mono<Criticism> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CriticismRepositoryInternal {
    <S extends Criticism> Mono<S> save(S entity);

    Flux<Criticism> findAllBy(Pageable pageable);

    Flux<Criticism> findAll();

    Mono<Criticism> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Criticism> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Criticism> findByCriteria(CriticismCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(CriticismCriteria criteria);
}
