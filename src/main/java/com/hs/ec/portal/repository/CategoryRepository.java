package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Category;
import com.hs.ec.portal.domain.criteria.CategoryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Category entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryRepository extends ReactiveCrudRepository<Category, Long>, CategoryRepositoryInternal {
    Flux<Category> findAllBy(Pageable pageable);

    @Override
    Mono<Category> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Category> findAllWithEagerRelationships();

    @Override
    Flux<Category> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM category entity WHERE entity.parent_id = :id")
    Flux<Category> findByParent(Long id);

    @Query("SELECT * FROM category entity WHERE entity.parent_id IS NULL")
    Flux<Category> findAllWhereParentIsNull();

    @Override
    <S extends Category> Mono<S> save(S entity);

    @Override
    Flux<Category> findAll();

    @Override
    Mono<Category> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CategoryRepositoryInternal {
    <S extends Category> Mono<S> save(S entity);

    Flux<Category> findAllBy(Pageable pageable);

    Flux<Category> findAll();

    Mono<Category> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Category> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Category> findByCriteria(CategoryCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(CategoryCriteria criteria);

    Mono<Category> findOneWithEagerRelationships(Long id);

    Flux<Category> findAllWithEagerRelationships();

    Flux<Category> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
