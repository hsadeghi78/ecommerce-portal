package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.ProductHistory;
import com.hs.ec.portal.domain.criteria.ProductHistoryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ProductHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductHistoryRepository extends ReactiveCrudRepository<ProductHistory, Long>, ProductHistoryRepositoryInternal {
    Flux<ProductHistory> findAllBy(Pageable pageable);

    @Override
    <S extends ProductHistory> Mono<S> save(S entity);

    @Override
    Flux<ProductHistory> findAll();

    @Override
    Mono<ProductHistory> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ProductHistoryRepositoryInternal {
    <S extends ProductHistory> Mono<S> save(S entity);

    Flux<ProductHistory> findAllBy(Pageable pageable);

    Flux<ProductHistory> findAll();

    Mono<ProductHistory> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ProductHistory> findAllBy(Pageable pageable, Criteria criteria);
    Flux<ProductHistory> findByCriteria(ProductHistoryCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ProductHistoryCriteria criteria);
}
