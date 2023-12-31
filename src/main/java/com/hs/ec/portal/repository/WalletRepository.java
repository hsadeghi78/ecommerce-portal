package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Wallet;
import com.hs.ec.portal.domain.criteria.WalletCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Wallet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WalletRepository extends ReactiveCrudRepository<Wallet, Long>, WalletRepositoryInternal {
    Flux<Wallet> findAllBy(Pageable pageable);

    @Override
    <S extends Wallet> Mono<S> save(S entity);

    @Override
    Flux<Wallet> findAll();

    @Override
    Mono<Wallet> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface WalletRepositoryInternal {
    <S extends Wallet> Mono<S> save(S entity);

    Flux<Wallet> findAllBy(Pageable pageable);

    Flux<Wallet> findAll();

    Mono<Wallet> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Wallet> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Wallet> findByCriteria(WalletCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(WalletCriteria criteria);
}
