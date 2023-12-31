package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.ResourceAuthorityCriteria;
import com.hs.ec.portal.repository.ResourceAuthorityRepository;
import com.hs.ec.portal.service.dto.ResourceAuthorityDTO;
import com.hs.ec.portal.service.mapper.ResourceAuthorityMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.ResourceAuthority}.
 */
@Service
@Transactional
public class ResourceAuthorityService {

    private final Logger log = LoggerFactory.getLogger(ResourceAuthorityService.class);

    private final ResourceAuthorityRepository resourceAuthorityRepository;

    private final ResourceAuthorityMapper resourceAuthorityMapper;

    public ResourceAuthorityService(
        ResourceAuthorityRepository resourceAuthorityRepository,
        ResourceAuthorityMapper resourceAuthorityMapper
    ) {
        this.resourceAuthorityRepository = resourceAuthorityRepository;
        this.resourceAuthorityMapper = resourceAuthorityMapper;
    }

    /**
     * Save a resourceAuthority.
     *
     * @param resourceAuthorityDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ResourceAuthorityDTO> save(ResourceAuthorityDTO resourceAuthorityDTO) {
        log.debug("Request to save ResourceAuthority : {}", resourceAuthorityDTO);
        return resourceAuthorityRepository.save(resourceAuthorityMapper.toEntity(resourceAuthorityDTO)).map(resourceAuthorityMapper::toDto);
    }

    /**
     * Update a resourceAuthority.
     *
     * @param resourceAuthorityDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ResourceAuthorityDTO> update(ResourceAuthorityDTO resourceAuthorityDTO) {
        log.debug("Request to update ResourceAuthority : {}", resourceAuthorityDTO);
        return resourceAuthorityRepository.save(resourceAuthorityMapper.toEntity(resourceAuthorityDTO)).map(resourceAuthorityMapper::toDto);
    }

    /**
     * Partially update a resourceAuthority.
     *
     * @param resourceAuthorityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ResourceAuthorityDTO> partialUpdate(ResourceAuthorityDTO resourceAuthorityDTO) {
        log.debug("Request to partially update ResourceAuthority : {}", resourceAuthorityDTO);

        return resourceAuthorityRepository
            .findById(resourceAuthorityDTO.getId())
            .map(existingResourceAuthority -> {
                resourceAuthorityMapper.partialUpdate(existingResourceAuthority, resourceAuthorityDTO);

                return existingResourceAuthority;
            })
            .flatMap(resourceAuthorityRepository::save)
            .map(resourceAuthorityMapper::toDto);
    }

    /**
     * Get all the resourceAuthorities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ResourceAuthorityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ResourceAuthorities");
        return resourceAuthorityRepository.findAllBy(pageable).map(resourceAuthorityMapper::toDto);
    }

    /**
     * Find resourceAuthorities by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ResourceAuthorityDTO> findByCriteria(ResourceAuthorityCriteria criteria, Pageable pageable) {
        log.debug("Request to get all ResourceAuthorities by Criteria");
        return resourceAuthorityRepository.findByCriteria(criteria, pageable).map(resourceAuthorityMapper::toDto);
    }

    /**
     * Find the count of resourceAuthorities by criteria.
     * @param criteria filtering criteria
     * @return the count of resourceAuthorities
     */
    public Mono<Long> countByCriteria(ResourceAuthorityCriteria criteria) {
        log.debug("Request to get the count of all ResourceAuthorities by Criteria");
        return resourceAuthorityRepository.countByCriteria(criteria);
    }

    /**
     * Get all the resourceAuthorities with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<ResourceAuthorityDTO> findAllWithEagerRelationships(Pageable pageable) {
        return resourceAuthorityRepository.findAllWithEagerRelationships(pageable).map(resourceAuthorityMapper::toDto);
    }

    /**
     * Returns the number of resourceAuthorities available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return resourceAuthorityRepository.count();
    }

    /**
     * Get one resourceAuthority by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ResourceAuthorityDTO> findOne(Long id) {
        log.debug("Request to get ResourceAuthority : {}", id);
        return resourceAuthorityRepository.findOneWithEagerRelationships(id).map(resourceAuthorityMapper::toDto);
    }

    /**
     * Delete the resourceAuthority by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ResourceAuthority : {}", id);
        return resourceAuthorityRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Flux<ResourceAuthorityDTO> findByAuthorities(List<String> authorities, Pageable page) {
        log.debug("find by criteria : {}, page: {}", authorities, page);
        return resourceAuthorityRepository
            .findAllWithEagerRelationships(Pageable.ofSize(100000))
            .filter(resourceAuthority -> authorities.contains(resourceAuthority.getMyAuthority().getName()))
            .map(resourceAuthorityMapper::toDto);
        //Solution 1 ba irad:
        /*Flux<MyAuthority> authorityList = myAuthorityRepository.findByNameIn(authorities);
        return authorityList.flatMap(myAuthority -> {
            try {
                List<Long> authIds = new ArrayList<>();
                authIds.add(myAuthority.getId());
                return resourceAuthorityRepository.findByMyAuthorityIdIn(authIds, page).map(resourceAuthorityMapper::toDto);
            }catch (Exception e){
                e.printStackTrace();
                return Flux.empty();
            }
        });*/
    }
}
