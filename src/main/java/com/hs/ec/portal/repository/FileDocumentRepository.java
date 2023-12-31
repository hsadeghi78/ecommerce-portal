package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.FileDocument;
import com.hs.ec.portal.domain.criteria.FileDocumentCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the FileDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileDocumentRepository extends ReactiveCrudRepository<FileDocument, Long>, FileDocumentRepositoryInternal {
    Flux<FileDocument> findAllBy(Pageable pageable);

    @Override
    <S extends FileDocument> Mono<S> save(S entity);

    @Override
    Flux<FileDocument> findAll();

    @Override
    Mono<FileDocument> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FileDocumentRepositoryInternal {
    <S extends FileDocument> Mono<S> save(S entity);

    Flux<FileDocument> findAllBy(Pageable pageable);

    Flux<FileDocument> findAll();

    Mono<FileDocument> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<FileDocument> findAllBy(Pageable pageable, Criteria criteria);
    Flux<FileDocument> findByCriteria(FileDocumentCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(FileDocumentCriteria criteria);
}
