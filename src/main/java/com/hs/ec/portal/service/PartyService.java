package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.PartyCriteria;
import com.hs.ec.portal.repository.PartyRepository;
import com.hs.ec.portal.service.dto.PartyDTO;
import com.hs.ec.portal.service.mapper.PartyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.Party}.
 */
@Service
@Transactional
public class PartyService {

    private final Logger log = LoggerFactory.getLogger(PartyService.class);

    private final PartyRepository partyRepository;

    private final PartyMapper partyMapper;

    public PartyService(PartyRepository partyRepository, PartyMapper partyMapper) {
        this.partyRepository = partyRepository;
        this.partyMapper = partyMapper;
    }

    /**
     * Save a party.
     *
     * @param partyDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PartyDTO> save(PartyDTO partyDTO) {
        log.debug("Request to save Party : {}", partyDTO);
        return partyRepository.save(partyMapper.toEntity(partyDTO)).map(partyMapper::toDto);
    }

    /**
     * Update a party.
     *
     * @param partyDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PartyDTO> update(PartyDTO partyDTO) {
        log.debug("Request to update Party : {}", partyDTO);
        return partyRepository.save(partyMapper.toEntity(partyDTO)).map(partyMapper::toDto);
    }

    /**
     * Partially update a party.
     *
     * @param partyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PartyDTO> partialUpdate(PartyDTO partyDTO) {
        log.debug("Request to partially update Party : {}", partyDTO);

        return partyRepository
            .findById(partyDTO.getId())
            .map(existingParty -> {
                partyMapper.partialUpdate(existingParty, partyDTO);

                return existingParty;
            })
            .flatMap(partyRepository::save)
            .map(partyMapper::toDto);
    }

    /**
     * Get all the parties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PartyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Parties");
        return partyRepository.findAllBy(pageable).map(partyMapper::toDto);
    }

    /**
     * Find parties by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PartyDTO> findByCriteria(PartyCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Parties by Criteria");
        return partyRepository.findByCriteria(criteria, pageable).map(partyMapper::toDto);
    }

    /**
     * Find the count of parties by criteria.
     * @param criteria filtering criteria
     * @return the count of parties
     */
    public Mono<Long> countByCriteria(PartyCriteria criteria) {
        log.debug("Request to get the count of all Parties by Criteria");
        return partyRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of parties available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return partyRepository.count();
    }

    /**
     * Get one party by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PartyDTO> findOne(Long id) {
        log.debug("Request to get Party : {}", id);
        return partyRepository.findById(id).map(partyMapper::toDto);
    }

    /**
     * Delete the party by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Party : {}", id);
        return partyRepository.deleteById(id);
    }
}
