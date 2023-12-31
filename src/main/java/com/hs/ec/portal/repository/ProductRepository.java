package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Product;
import com.hs.ec.portal.domain.criteria.ProductCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long>, ProductRepositoryInternal {
    Flux<Product> findAllBy(Pageable pageable);

    @Override
    Mono<Product> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Product> findAllWithEagerRelationships();

    @Override
    Flux<Product> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM product entity JOIN rel_product__documents joinTable ON entity.id = joinTable.documents_id WHERE joinTable.documents_id = :id"
    )
    Flux<Product> findByDocuments(Long id);

    @Query("SELECT * FROM product entity WHERE entity.category_id = :id")
    Flux<Product> findByCategory(Long id);

    @Query("SELECT * FROM product entity WHERE entity.category_id IS NULL")
    Flux<Product> findAllWhereCategoryIsNull();

    @Query("SELECT * FROM product entity WHERE entity.party_id = :id")
    Flux<Product> findByParty(Long id);

    @Query("SELECT * FROM product entity WHERE entity.party_id IS NULL")
    Flux<Product> findAllWherePartyIsNull();

    @Query("SELECT * FROM product entity WHERE entity.parent_id = :id")
    Flux<Product> findByParent(Long id);

    @Query("SELECT * FROM product entity WHERE entity.parent_id IS NULL")
    Flux<Product> findAllWhereParentIsNull();

    @Override
    <S extends Product> Mono<S> save(S entity);

    @Override
    Flux<Product> findAll();

    @Override
    Mono<Product> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ProductRepositoryInternal {
    <S extends Product> Mono<S> save(S entity);

    Flux<Product> findAllBy(Pageable pageable);

    Flux<Product> findAll();

    Mono<Product> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Product> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Product> findByCriteria(ProductCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ProductCriteria criteria);

    Mono<Product> findOneWithEagerRelationships(Long id);

    Flux<Product> findAllWithEagerRelationships();

    Flux<Product> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
