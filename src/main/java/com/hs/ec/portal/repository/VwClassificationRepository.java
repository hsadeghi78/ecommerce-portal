package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.VwClassification;
import com.hs.ec.portal.domain.criteria.VwClassificationCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the VwClassification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VwClassificationRepository extends ReactiveCrudRepository<VwClassification, Long>, VwClassificationRepositoryInternal {
    Flux<VwClassification> findAllBy(Pageable pageable);

    @Override
    <S extends VwClassification> Mono<S> save(S entity);

    @Override
    Flux<VwClassification> findAll();

    @Override
    Mono<VwClassification> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface VwClassificationRepositoryInternal {
    <S extends VwClassification> Mono<S> save(S entity);

    Flux<VwClassification> findAllBy(Pageable pageable);

    Flux<VwClassification> findAll();

    Mono<VwClassification> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<VwClassification> findAllBy(Pageable pageable, Criteria criteria);
    Flux<VwClassification> findByCriteria(VwClassificationCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(VwClassificationCriteria criteria);
}
