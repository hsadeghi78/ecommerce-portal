package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.AgreementCriteria;
import com.hs.ec.portal.repository.AgreementRepository;
import com.hs.ec.portal.service.dto.AgreementDTO;
import com.hs.ec.portal.service.mapper.AgreementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.Agreement}.
 */
@Service
@Transactional
public class AgreementService {

    private final Logger log = LoggerFactory.getLogger(AgreementService.class);

    private final AgreementRepository agreementRepository;

    private final AgreementMapper agreementMapper;

    public AgreementService(AgreementRepository agreementRepository, AgreementMapper agreementMapper) {
        this.agreementRepository = agreementRepository;
        this.agreementMapper = agreementMapper;
    }

    /**
     * Save a agreement.
     *
     * @param agreementDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AgreementDTO> save(AgreementDTO agreementDTO) {
        log.debug("Request to save Agreement : {}", agreementDTO);
        return agreementRepository.save(agreementMapper.toEntity(agreementDTO)).map(agreementMapper::toDto);
    }

    /**
     * Update a agreement.
     *
     * @param agreementDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AgreementDTO> update(AgreementDTO agreementDTO) {
        log.debug("Request to update Agreement : {}", agreementDTO);
        return agreementRepository.save(agreementMapper.toEntity(agreementDTO)).map(agreementMapper::toDto);
    }

    /**
     * Partially update a agreement.
     *
     * @param agreementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<AgreementDTO> partialUpdate(AgreementDTO agreementDTO) {
        log.debug("Request to partially update Agreement : {}", agreementDTO);

        return agreementRepository
            .findById(agreementDTO.getId())
            .map(existingAgreement -> {
                agreementMapper.partialUpdate(existingAgreement, agreementDTO);

                return existingAgreement;
            })
            .flatMap(agreementRepository::save)
            .map(agreementMapper::toDto);
    }

    /**
     * Get all the agreements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<AgreementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Agreements");
        return agreementRepository.findAllBy(pageable).map(agreementMapper::toDto);
    }

    /**
     * Find agreements by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<AgreementDTO> findByCriteria(AgreementCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Agreements by Criteria");
        return agreementRepository.findByCriteria(criteria, pageable).map(agreementMapper::toDto);
    }

    /**
     * Find the count of agreements by criteria.
     * @param criteria filtering criteria
     * @return the count of agreements
     */
    public Mono<Long> countByCriteria(AgreementCriteria criteria) {
        log.debug("Request to get the count of all Agreements by Criteria");
        return agreementRepository.countByCriteria(criteria);
    }

    /**
     * Get all the agreements with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<AgreementDTO> findAllWithEagerRelationships(Pageable pageable) {
        return agreementRepository.findAllWithEagerRelationships(pageable).map(agreementMapper::toDto);
    }

    /**
     * Returns the number of agreements available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return agreementRepository.count();
    }

    /**
     * Get one agreement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<AgreementDTO> findOne(Long id) {
        log.debug("Request to get Agreement : {}", id);
        return agreementRepository.findOneWithEagerRelationships(id).map(agreementMapper::toDto);
    }

    /**
     * Delete the agreement by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Agreement : {}", id);
        return agreementRepository.deleteById(id);
    }
}
