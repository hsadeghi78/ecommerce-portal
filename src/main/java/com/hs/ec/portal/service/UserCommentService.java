package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.UserCommentCriteria;
import com.hs.ec.portal.repository.UserCommentRepository;
import com.hs.ec.portal.service.dto.UserCommentDTO;
import com.hs.ec.portal.service.mapper.UserCommentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.UserComment}.
 */
@Service
@Transactional
public class UserCommentService {

    private final Logger log = LoggerFactory.getLogger(UserCommentService.class);

    private final UserCommentRepository userCommentRepository;

    private final UserCommentMapper userCommentMapper;

    public UserCommentService(UserCommentRepository userCommentRepository, UserCommentMapper userCommentMapper) {
        this.userCommentRepository = userCommentRepository;
        this.userCommentMapper = userCommentMapper;
    }

    /**
     * Save a userComment.
     *
     * @param userCommentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UserCommentDTO> save(UserCommentDTO userCommentDTO) {
        log.debug("Request to save UserComment : {}", userCommentDTO);
        return userCommentRepository.save(userCommentMapper.toEntity(userCommentDTO)).map(userCommentMapper::toDto);
    }

    /**
     * Update a userComment.
     *
     * @param userCommentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UserCommentDTO> update(UserCommentDTO userCommentDTO) {
        log.debug("Request to update UserComment : {}", userCommentDTO);
        return userCommentRepository.save(userCommentMapper.toEntity(userCommentDTO)).map(userCommentMapper::toDto);
    }

    /**
     * Partially update a userComment.
     *
     * @param userCommentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UserCommentDTO> partialUpdate(UserCommentDTO userCommentDTO) {
        log.debug("Request to partially update UserComment : {}", userCommentDTO);

        return userCommentRepository
            .findById(userCommentDTO.getId())
            .map(existingUserComment -> {
                userCommentMapper.partialUpdate(existingUserComment, userCommentDTO);

                return existingUserComment;
            })
            .flatMap(userCommentRepository::save)
            .map(userCommentMapper::toDto);
    }

    /**
     * Get all the userComments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UserCommentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserComments");
        return userCommentRepository.findAllBy(pageable).map(userCommentMapper::toDto);
    }

    /**
     * Find userComments by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UserCommentDTO> findByCriteria(UserCommentCriteria criteria, Pageable pageable) {
        log.debug("Request to get all UserComments by Criteria");
        return userCommentRepository.findByCriteria(criteria, pageable).map(userCommentMapper::toDto);
    }

    /**
     * Find the count of userComments by criteria.
     * @param criteria filtering criteria
     * @return the count of userComments
     */
    public Mono<Long> countByCriteria(UserCommentCriteria criteria) {
        log.debug("Request to get the count of all UserComments by Criteria");
        return userCommentRepository.countByCriteria(criteria);
    }

    /**
     * Get all the userComments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<UserCommentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userCommentRepository.findAllWithEagerRelationships(pageable).map(userCommentMapper::toDto);
    }

    /**
     * Returns the number of userComments available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return userCommentRepository.count();
    }

    /**
     * Get one userComment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UserCommentDTO> findOne(Long id) {
        log.debug("Request to get UserComment : {}", id);
        return userCommentRepository.findOneWithEagerRelationships(id).map(userCommentMapper::toDto);
    }

    /**
     * Delete the userComment by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete UserComment : {}", id);
        return userCommentRepository.deleteById(id);
    }
}
