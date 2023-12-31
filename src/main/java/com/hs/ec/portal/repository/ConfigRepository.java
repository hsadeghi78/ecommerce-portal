package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Config;
import com.hs.ec.portal.domain.criteria.ConfigCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Config entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigRepository extends ReactiveCrudRepository<Config, Long>, ConfigRepositoryInternal {
    Flux<Config> findAllBy(Pageable pageable);

    @Override
    <S extends Config> Mono<S> save(S entity);

    @Override
    Flux<Config> findAll();

    @Override
    Mono<Config> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ConfigRepositoryInternal {
    <S extends Config> Mono<S> save(S entity);

    Flux<Config> findAllBy(Pageable pageable);

    Flux<Config> findAll();

    Mono<Config> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Config> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Config> findByCriteria(ConfigCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ConfigCriteria criteria);
}
