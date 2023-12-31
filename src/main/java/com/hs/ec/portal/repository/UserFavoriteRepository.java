package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.UserFavorite;
import com.hs.ec.portal.domain.criteria.UserFavoriteCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UserFavorite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserFavoriteRepository extends ReactiveCrudRepository<UserFavorite, Long>, UserFavoriteRepositoryInternal {
    Flux<UserFavorite> findAllBy(Pageable pageable);

    @Override
    Mono<UserFavorite> findOneWithEagerRelationships(Long id);

    @Override
    Flux<UserFavorite> findAllWithEagerRelationships();

    @Override
    Flux<UserFavorite> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM user_favorite entity WHERE entity.product_id = :id")
    Flux<UserFavorite> findByProduct(Long id);

    @Query("SELECT * FROM user_favorite entity WHERE entity.product_id IS NULL")
    Flux<UserFavorite> findAllWhereProductIsNull();

    @Override
    <S extends UserFavorite> Mono<S> save(S entity);

    @Override
    Flux<UserFavorite> findAll();

    @Override
    Mono<UserFavorite> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UserFavoriteRepositoryInternal {
    <S extends UserFavorite> Mono<S> save(S entity);

    Flux<UserFavorite> findAllBy(Pageable pageable);

    Flux<UserFavorite> findAll();

    Mono<UserFavorite> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UserFavorite> findAllBy(Pageable pageable, Criteria criteria);
    Flux<UserFavorite> findByCriteria(UserFavoriteCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(UserFavoriteCriteria criteria);

    Mono<UserFavorite> findOneWithEagerRelationships(Long id);

    Flux<UserFavorite> findAllWithEagerRelationships();

    Flux<UserFavorite> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
