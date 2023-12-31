package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.ClassTypeCriteria;
import com.hs.ec.portal.repository.ClassTypeRepository;
import com.hs.ec.portal.service.dto.ClassTypeDTO;
import com.hs.ec.portal.service.mapper.ClassTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.ClassType}.
 */
@Service
@Transactional
public class ClassTypeService {

    private final Logger log = LoggerFactory.getLogger(ClassTypeService.class);

    private final ClassTypeRepository classTypeRepository;

    private final ClassTypeMapper classTypeMapper;

    public ClassTypeService(ClassTypeRepository classTypeRepository, ClassTypeMapper classTypeMapper) {
        this.classTypeRepository = classTypeRepository;
        this.classTypeMapper = classTypeMapper;
    }

    /**
     * Save a classType.
     *
     * @param classTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ClassTypeDTO> save(ClassTypeDTO classTypeDTO) {
        log.debug("Request to save ClassType : {}", classTypeDTO);
        return classTypeRepository.save(classTypeMapper.toEntity(classTypeDTO)).map(classTypeMapper::toDto);
    }

    /**
     * Update a classType.
     *
     * @param classTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ClassTypeDTO> update(ClassTypeDTO classTypeDTO) {
        log.debug("Request to update ClassType : {}", classTypeDTO);
        return classTypeRepository.save(classTypeMapper.toEntity(classTypeDTO)).map(classTypeMapper::toDto);
    }

    /**
     * Partially update a classType.
     *
     * @param classTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ClassTypeDTO> partialUpdate(ClassTypeDTO classTypeDTO) {
        log.debug("Request to partially update ClassType : {}", classTypeDTO);

        return classTypeRepository
            .findById(classTypeDTO.getId())
            .map(existingClassType -> {
                classTypeMapper.partialUpdate(existingClassType, classTypeDTO);

                return existingClassType;
            })
            .flatMap(classTypeRepository::save)
            .map(classTypeMapper::toDto);
    }

    /**
     * Get all the classTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ClassTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ClassTypes");
        return classTypeRepository.findAllBy(pageable).map(classTypeMapper::toDto);
    }

    /**
     * Find classTypes by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ClassTypeDTO> findByCriteria(ClassTypeCriteria criteria, Pageable pageable) {
        log.debug("Request to get all ClassTypes by Criteria");
        return classTypeRepository.findByCriteria(criteria, pageable).map(classTypeMapper::toDto);
    }

    /**
     * Find the count of classTypes by criteria.
     * @param criteria filtering criteria
     * @return the count of classTypes
     */
    public Mono<Long> countByCriteria(ClassTypeCriteria criteria) {
        log.debug("Request to get the count of all ClassTypes by Criteria");
        return classTypeRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of classTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return classTypeRepository.count();
    }

    /**
     * Get one classType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ClassTypeDTO> findOne(Long id) {
        log.debug("Request to get ClassType : {}", id);
        return classTypeRepository.findById(id).map(classTypeMapper::toDto);
    }

    /**
     * Delete the classType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ClassType : {}", id);
        return classTypeRepository.deleteById(id);
    }
}
