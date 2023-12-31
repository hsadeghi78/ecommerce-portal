package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.MyAuthorityCriteria;
import com.hs.ec.portal.repository.MyAuthorityRepository;
import com.hs.ec.portal.service.dto.MyAuthorityDTO;
import com.hs.ec.portal.service.mapper.MyAuthorityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.MyAuthority}.
 */
@Service
@Transactional
public class MyAuthorityService {

    private final Logger log = LoggerFactory.getLogger(MyAuthorityService.class);

    private final MyAuthorityRepository myAuthorityRepository;

    private final MyAuthorityMapper myAuthorityMapper;

    public MyAuthorityService(MyAuthorityRepository myAuthorityRepository, MyAuthorityMapper myAuthorityMapper) {
        this.myAuthorityRepository = myAuthorityRepository;
        this.myAuthorityMapper = myAuthorityMapper;
    }

    /**
     * Save a myAuthority.
     *
     * @param myAuthorityDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<MyAuthorityDTO> save(MyAuthorityDTO myAuthorityDTO) {
        log.debug("Request to save MyAuthority : {}", myAuthorityDTO);
        return myAuthorityRepository.save(myAuthorityMapper.toEntity(myAuthorityDTO)).map(myAuthorityMapper::toDto);
    }

    /**
     * Update a myAuthority.
     *
     * @param myAuthorityDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<MyAuthorityDTO> update(MyAuthorityDTO myAuthorityDTO) {
        log.debug("Request to update MyAuthority : {}", myAuthorityDTO);
        return myAuthorityRepository.save(myAuthorityMapper.toEntity(myAuthorityDTO)).map(myAuthorityMapper::toDto);
    }

    /**
     * Partially update a myAuthority.
     *
     * @param myAuthorityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<MyAuthorityDTO> partialUpdate(MyAuthorityDTO myAuthorityDTO) {
        log.debug("Request to partially update MyAuthority : {}", myAuthorityDTO);

        return myAuthorityRepository
            .findById(myAuthorityDTO.getId())
            .map(existingMyAuthority -> {
                myAuthorityMapper.partialUpdate(existingMyAuthority, myAuthorityDTO);

                return existingMyAuthority;
            })
            .flatMap(myAuthorityRepository::save)
            .map(myAuthorityMapper::toDto);
    }

    /**
     * Get all the myAuthorities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<MyAuthorityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MyAuthorities");
        return myAuthorityRepository.findAllBy(pageable).map(myAuthorityMapper::toDto);
    }

    /**
     * Find myAuthorities by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<MyAuthorityDTO> findByCriteria(MyAuthorityCriteria criteria, Pageable pageable) {
        log.debug("Request to get all MyAuthorities by Criteria");
        return myAuthorityRepository.findByCriteria(criteria, pageable).map(myAuthorityMapper::toDto);
    }

    /**
     * Find the count of myAuthorities by criteria.
     * @param criteria filtering criteria
     * @return the count of myAuthorities
     */
    public Mono<Long> countByCriteria(MyAuthorityCriteria criteria) {
        log.debug("Request to get the count of all MyAuthorities by Criteria");
        return myAuthorityRepository.countByCriteria(criteria);
    }

    /**
     * Get all the myAuthorities with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<MyAuthorityDTO> findAllWithEagerRelationships(Pageable pageable) {
        return myAuthorityRepository.findAllWithEagerRelationships(pageable).map(myAuthorityMapper::toDto);
    }

    /**
     * Returns the number of myAuthorities available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return myAuthorityRepository.count();
    }

    /**
     * Get one myAuthority by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<MyAuthorityDTO> findOne(Long id) {
        log.debug("Request to get MyAuthority : {}", id);
        return myAuthorityRepository.findOneWithEagerRelationships(id).map(myAuthorityMapper::toDto);
    }

    /**
     * Delete the myAuthority by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete MyAuthority : {}", id);
        return myAuthorityRepository.deleteById(id);
    }
}
