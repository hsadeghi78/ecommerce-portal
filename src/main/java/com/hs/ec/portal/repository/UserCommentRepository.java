package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.UserComment;
import com.hs.ec.portal.domain.criteria.UserCommentCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UserComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserCommentRepository extends ReactiveCrudRepository<UserComment, Long>, UserCommentRepositoryInternal {
    Flux<UserComment> findAllBy(Pageable pageable);

    @Override
    Mono<UserComment> findOneWithEagerRelationships(Long id);

    @Override
    Flux<UserComment> findAllWithEagerRelationships();

    @Override
    Flux<UserComment> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM user_comment entity WHERE entity.party_id = :id")
    Flux<UserComment> findByParty(Long id);

    @Query("SELECT * FROM user_comment entity WHERE entity.party_id IS NULL")
    Flux<UserComment> findAllWherePartyIsNull();

    @Query("SELECT * FROM user_comment entity WHERE entity.product_id = :id")
    Flux<UserComment> findByProduct(Long id);

    @Query("SELECT * FROM user_comment entity WHERE entity.product_id IS NULL")
    Flux<UserComment> findAllWhereProductIsNull();

    @Query("SELECT * FROM user_comment entity WHERE entity.factor_id = :id")
    Flux<UserComment> findByFactor(Long id);

    @Query("SELECT * FROM user_comment entity WHERE entity.factor_id IS NULL")
    Flux<UserComment> findAllWhereFactorIsNull();

    @Query("SELECT * FROM user_comment entity WHERE entity.parent_id = :id")
    Flux<UserComment> findByParent(Long id);

    @Query("SELECT * FROM user_comment entity WHERE entity.parent_id IS NULL")
    Flux<UserComment> findAllWhereParentIsNull();

    @Override
    <S extends UserComment> Mono<S> save(S entity);

    @Override
    Flux<UserComment> findAll();

    @Override
    Mono<UserComment> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UserCommentRepositoryInternal {
    <S extends UserComment> Mono<S> save(S entity);

    Flux<UserComment> findAllBy(Pageable pageable);

    Flux<UserComment> findAll();

    Mono<UserComment> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UserComment> findAllBy(Pageable pageable, Criteria criteria);
    Flux<UserComment> findByCriteria(UserCommentCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(UserCommentCriteria criteria);

    Mono<UserComment> findOneWithEagerRelationships(Long id);

    Flux<UserComment> findAllWithEagerRelationships();

    Flux<UserComment> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
