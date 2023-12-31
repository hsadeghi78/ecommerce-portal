package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Party;
import com.hs.ec.portal.domain.criteria.PartyCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Party entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartyRepository extends ReactiveCrudRepository<Party, Long>, PartyRepositoryInternal {
    Flux<Party> findAllBy(Pageable pageable);

    @Override
    <S extends Party> Mono<S> save(S entity);

    @Override
    Flux<Party> findAll();

    @Override
    Mono<Party> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PartyRepositoryInternal {
    <S extends Party> Mono<S> save(S entity);

    Flux<Party> findAllBy(Pageable pageable);

    Flux<Party> findAll();

    Mono<Party> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Party> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Party> findByCriteria(PartyCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(PartyCriteria criteria);
}
