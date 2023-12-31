package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.UserFavoriteCriteria;
import com.hs.ec.portal.repository.UserFavoriteRepository;
import com.hs.ec.portal.service.dto.UserFavoriteDTO;
import com.hs.ec.portal.service.mapper.UserFavoriteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.UserFavorite}.
 */
@Service
@Transactional
public class UserFavoriteService {

    private final Logger log = LoggerFactory.getLogger(UserFavoriteService.class);

    private final UserFavoriteRepository userFavoriteRepository;

    private final UserFavoriteMapper userFavoriteMapper;

    public UserFavoriteService(UserFavoriteRepository userFavoriteRepository, UserFavoriteMapper userFavoriteMapper) {
        this.userFavoriteRepository = userFavoriteRepository;
        this.userFavoriteMapper = userFavoriteMapper;
    }

    /**
     * Save a userFavorite.
     *
     * @param userFavoriteDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UserFavoriteDTO> save(UserFavoriteDTO userFavoriteDTO) {
        log.debug("Request to save UserFavorite : {}", userFavoriteDTO);
        return userFavoriteRepository.save(userFavoriteMapper.toEntity(userFavoriteDTO)).map(userFavoriteMapper::toDto);
    }

    /**
     * Update a userFavorite.
     *
     * @param userFavoriteDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UserFavoriteDTO> update(UserFavoriteDTO userFavoriteDTO) {
        log.debug("Request to update UserFavorite : {}", userFavoriteDTO);
        return userFavoriteRepository.save(userFavoriteMapper.toEntity(userFavoriteDTO)).map(userFavoriteMapper::toDto);
    }

    /**
     * Partially update a userFavorite.
     *
     * @param userFavoriteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UserFavoriteDTO> partialUpdate(UserFavoriteDTO userFavoriteDTO) {
        log.debug("Request to partially update UserFavorite : {}", userFavoriteDTO);

        return userFavoriteRepository
            .findById(userFavoriteDTO.getId())
            .map(existingUserFavorite -> {
                userFavoriteMapper.partialUpdate(existingUserFavorite, userFavoriteDTO);

                return existingUserFavorite;
            })
            .flatMap(userFavoriteRepository::save)
            .map(userFavoriteMapper::toDto);
    }

    /**
     * Get all the userFavorites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UserFavoriteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserFavorites");
        return userFavoriteRepository.findAllBy(pageable).map(userFavoriteMapper::toDto);
    }

    /**
     * Find userFavorites by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UserFavoriteDTO> findByCriteria(UserFavoriteCriteria criteria, Pageable pageable) {
        log.debug("Request to get all UserFavorites by Criteria");
        return userFavoriteRepository.findByCriteria(criteria, pageable).map(userFavoriteMapper::toDto);
    }

    /**
     * Find the count of userFavorites by criteria.
     * @param criteria filtering criteria
     * @return the count of userFavorites
     */
    public Mono<Long> countByCriteria(UserFavoriteCriteria criteria) {
        log.debug("Request to get the count of all UserFavorites by Criteria");
        return userFavoriteRepository.countByCriteria(criteria);
    }

    /**
     * Get all the userFavorites with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<UserFavoriteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userFavoriteRepository.findAllWithEagerRelationships(pageable).map(userFavoriteMapper::toDto);
    }

    /**
     * Returns the number of userFavorites available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return userFavoriteRepository.count();
    }

    /**
     * Get one userFavorite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UserFavoriteDTO> findOne(Long id) {
        log.debug("Request to get UserFavorite : {}", id);
        return userFavoriteRepository.findOneWithEagerRelationships(id).map(userFavoriteMapper::toDto);
    }

    /**
     * Delete the userFavorite by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete UserFavorite : {}", id);
        return userFavoriteRepository.deleteById(id);
    }
}
