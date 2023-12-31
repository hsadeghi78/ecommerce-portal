package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Campaign;
import com.hs.ec.portal.domain.criteria.CampaignCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Campaign entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CampaignRepository extends ReactiveCrudRepository<Campaign, Long>, CampaignRepositoryInternal {
    Flux<Campaign> findAllBy(Pageable pageable);

    @Override
    Mono<Campaign> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Campaign> findAllWithEagerRelationships();

    @Override
    Flux<Campaign> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM campaign entity JOIN rel_campaign__products joinTable ON entity.id = joinTable.products_id WHERE joinTable.products_id = :id"
    )
    Flux<Campaign> findByProducts(Long id);

    @Override
    <S extends Campaign> Mono<S> save(S entity);

    @Override
    Flux<Campaign> findAll();

    @Override
    Mono<Campaign> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CampaignRepositoryInternal {
    <S extends Campaign> Mono<S> save(S entity);

    Flux<Campaign> findAllBy(Pageable pageable);

    Flux<Campaign> findAll();

    Mono<Campaign> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Campaign> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Campaign> findByCriteria(CampaignCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(CampaignCriteria criteria);

    Mono<Campaign> findOneWithEagerRelationships(Long id);

    Flux<Campaign> findAllWithEagerRelationships();

    Flux<Campaign> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
