package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.ContactCriteria;
import com.hs.ec.portal.repository.ContactRepository;
import com.hs.ec.portal.service.dto.ContactDTO;
import com.hs.ec.portal.service.mapper.ContactMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.Contact}.
 */
@Service
@Transactional
public class ContactService {

    private final Logger log = LoggerFactory.getLogger(ContactService.class);

    private final ContactRepository contactRepository;

    private final ContactMapper contactMapper;

    public ContactService(ContactRepository contactRepository, ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
    }

    /**
     * Save a contact.
     *
     * @param contactDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ContactDTO> save(ContactDTO contactDTO) {
        log.debug("Request to save Contact : {}", contactDTO);
        return contactRepository.save(contactMapper.toEntity(contactDTO)).map(contactMapper::toDto);
    }

    /**
     * Update a contact.
     *
     * @param contactDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ContactDTO> update(ContactDTO contactDTO) {
        log.debug("Request to update Contact : {}", contactDTO);
        return contactRepository.save(contactMapper.toEntity(contactDTO)).map(contactMapper::toDto);
    }

    /**
     * Partially update a contact.
     *
     * @param contactDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ContactDTO> partialUpdate(ContactDTO contactDTO) {
        log.debug("Request to partially update Contact : {}", contactDTO);

        return contactRepository
            .findById(contactDTO.getId())
            .map(existingContact -> {
                contactMapper.partialUpdate(existingContact, contactDTO);

                return existingContact;
            })
            .flatMap(contactRepository::save)
            .map(contactMapper::toDto);
    }

    /**
     * Get all the contacts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ContactDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contacts");
        return contactRepository.findAllBy(pageable).map(contactMapper::toDto);
    }

    /**
     * Find contacts by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ContactDTO> findByCriteria(ContactCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Contacts by Criteria");
        return contactRepository.findByCriteria(criteria, pageable).map(contactMapper::toDto);
    }

    /**
     * Find the count of contacts by criteria.
     * @param criteria filtering criteria
     * @return the count of contacts
     */
    public Mono<Long> countByCriteria(ContactCriteria criteria) {
        log.debug("Request to get the count of all Contacts by Criteria");
        return contactRepository.countByCriteria(criteria);
    }

    /**
     * Get all the contacts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<ContactDTO> findAllWithEagerRelationships(Pageable pageable) {
        return contactRepository.findAllWithEagerRelationships(pageable).map(contactMapper::toDto);
    }

    /**
     * Returns the number of contacts available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return contactRepository.count();
    }

    /**
     * Get one contact by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ContactDTO> findOne(Long id) {
        log.debug("Request to get Contact : {}", id);
        return contactRepository.findOneWithEagerRelationships(id).map(contactMapper::toDto);
    }

    /**
     * Delete the contact by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Contact : {}", id);
        return contactRepository.deleteById(id);
    }
}
