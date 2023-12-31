package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.ClassificationCriteria;
import com.hs.ec.portal.repository.ClassificationRepository;
import com.hs.ec.portal.service.dto.ClassificationDTO;
import com.hs.ec.portal.service.mapper.ClassificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.Classification}.
 */
@Service
@Transactional
public class ClassificationService {

    private final Logger log = LoggerFactory.getLogger(ClassificationService.class);

    private final ClassificationRepository classificationRepository;

    private final ClassificationMapper classificationMapper;

    public ClassificationService(ClassificationRepository classificationRepository, ClassificationMapper classificationMapper) {
        this.classificationRepository = classificationRepository;
        this.classificationMapper = classificationMapper;
    }

    /**
     * Save a classification.
     *
     * @param classificationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ClassificationDTO> save(ClassificationDTO classificationDTO) {
        log.debug("Request to save Classification : {}", classificationDTO);
        return classificationRepository.save(classificationMapper.toEntity(classificationDTO)).map(classificationMapper::toDto);
    }

    /**
     * Update a classification.
     *
     * @param classificationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ClassificationDTO> update(ClassificationDTO classificationDTO) {
        log.debug("Request to update Classification : {}", classificationDTO);
        return classificationRepository.save(classificationMapper.toEntity(classificationDTO)).map(classificationMapper::toDto);
    }

    /**
     * Partially update a classification.
     *
     * @param classificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ClassificationDTO> partialUpdate(ClassificationDTO classificationDTO) {
        log.debug("Request to partially update Classification : {}", classificationDTO);

        return classificationRepository
            .findById(classificationDTO.getId())
            .map(existingClassification -> {
                classificationMapper.partialUpdate(existingClassification, classificationDTO);

                return existingClassification;
            })
            .flatMap(classificationRepository::save)
            .map(classificationMapper::toDto);
    }

    /**
     * Get all the classifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ClassificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Classifications");
        return classificationRepository.findAllBy(pageable).map(classificationMapper::toDto);
    }

    /**
     * Find classifications by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ClassificationDTO> findByCriteria(ClassificationCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Classifications by Criteria");
        return classificationRepository.findByCriteria(criteria, pageable).map(classificationMapper::toDto);
    }

    /**
     * Find the count of classifications by criteria.
     * @param criteria filtering criteria
     * @return the count of classifications
     */
    public Mono<Long> countByCriteria(ClassificationCriteria criteria) {
        log.debug("Request to get the count of all Classifications by Criteria");
        return classificationRepository.countByCriteria(criteria);
    }

    /**
     * Get all the classifications with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<ClassificationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return classificationRepository.findAllWithEagerRelationships(pageable).map(classificationMapper::toDto);
    }

    /**
     * Returns the number of classifications available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return classificationRepository.count();
    }

    /**
     * Get one classification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ClassificationDTO> findOne(Long id) {
        log.debug("Request to get Classification : {}", id);
        return classificationRepository.findOneWithEagerRelationships(id).map(classificationMapper::toDto);
    }

    /**
     * Delete the classification by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Classification : {}", id);
        return classificationRepository.deleteById(id);
    }
}
