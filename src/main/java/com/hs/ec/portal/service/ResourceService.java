package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.ResourceCriteria;
import com.hs.ec.portal.repository.ResourceRepository;
import com.hs.ec.portal.service.dto.ResourceDTO;
import com.hs.ec.portal.service.mapper.ResourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.Resource}.
 */
@Service
@Transactional
public class ResourceService {

    private final Logger log = LoggerFactory.getLogger(ResourceService.class);

    private final ResourceRepository resourceRepository;

    private final ResourceMapper resourceMapper;

    public ResourceService(ResourceRepository resourceRepository, ResourceMapper resourceMapper) {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
    }

    /**
     * Save a resource.
     *
     * @param resourceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ResourceDTO> save(ResourceDTO resourceDTO) {
        log.debug("Request to save Resource : {}", resourceDTO);
        return resourceRepository.save(resourceMapper.toEntity(resourceDTO)).map(resourceMapper::toDto);
    }

    /**
     * Update a resource.
     *
     * @param resourceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ResourceDTO> update(ResourceDTO resourceDTO) {
        log.debug("Request to update Resource : {}", resourceDTO);
        return resourceRepository.save(resourceMapper.toEntity(resourceDTO)).map(resourceMapper::toDto);
    }

    /**
     * Partially update a resource.
     *
     * @param resourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ResourceDTO> partialUpdate(ResourceDTO resourceDTO) {
        log.debug("Request to partially update Resource : {}", resourceDTO);

        return resourceRepository
            .findById(resourceDTO.getId())
            .map(existingResource -> {
                resourceMapper.partialUpdate(existingResource, resourceDTO);

                return existingResource;
            })
            .flatMap(resourceRepository::save)
            .map(resourceMapper::toDto);
    }

    /**
     * Get all the resources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ResourceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Resources");
        return resourceRepository.findAllBy(pageable).map(resourceMapper::toDto);
    }

    /**
     * Find resources by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ResourceDTO> findByCriteria(ResourceCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Resources by Criteria");
        return resourceRepository.findByCriteria(criteria, pageable).map(resourceMapper::toDto);
    }

    /**
     * Find the count of resources by criteria.
     * @param criteria filtering criteria
     * @return the count of resources
     */
    public Mono<Long> countByCriteria(ResourceCriteria criteria) {
        log.debug("Request to get the count of all Resources by Criteria");
        return resourceRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of resources available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return resourceRepository.count();
    }

    /**
     * Get one resource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ResourceDTO> findOne(Long id) {
        log.debug("Request to get Resource : {}", id);
        return resourceRepository.findById(id).map(resourceMapper::toDto);
    }

    /**
     * Delete the resource by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Resource : {}", id);
        return resourceRepository.deleteById(id);
    }
}
