package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.CampaignCriteria;
import com.hs.ec.portal.repository.CampaignRepository;
import com.hs.ec.portal.service.dto.CampaignDTO;
import com.hs.ec.portal.service.mapper.CampaignMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.Campaign}.
 */
@Service
@Transactional
public class CampaignService {

    private final Logger log = LoggerFactory.getLogger(CampaignService.class);

    private final CampaignRepository campaignRepository;

    private final CampaignMapper campaignMapper;

    public CampaignService(CampaignRepository campaignRepository, CampaignMapper campaignMapper) {
        this.campaignRepository = campaignRepository;
        this.campaignMapper = campaignMapper;
    }

    /**
     * Save a campaign.
     *
     * @param campaignDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CampaignDTO> save(CampaignDTO campaignDTO) {
        log.debug("Request to save Campaign : {}", campaignDTO);
        return campaignRepository.save(campaignMapper.toEntity(campaignDTO)).map(campaignMapper::toDto);
    }

    /**
     * Update a campaign.
     *
     * @param campaignDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CampaignDTO> update(CampaignDTO campaignDTO) {
        log.debug("Request to update Campaign : {}", campaignDTO);
        return campaignRepository.save(campaignMapper.toEntity(campaignDTO)).map(campaignMapper::toDto);
    }

    /**
     * Partially update a campaign.
     *
     * @param campaignDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CampaignDTO> partialUpdate(CampaignDTO campaignDTO) {
        log.debug("Request to partially update Campaign : {}", campaignDTO);

        return campaignRepository
            .findById(campaignDTO.getId())
            .map(existingCampaign -> {
                campaignMapper.partialUpdate(existingCampaign, campaignDTO);

                return existingCampaign;
            })
            .flatMap(campaignRepository::save)
            .map(campaignMapper::toDto);
    }

    /**
     * Get all the campaigns.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CampaignDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Campaigns");
        return campaignRepository.findAllBy(pageable).map(campaignMapper::toDto);
    }

    /**
     * Find campaigns by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CampaignDTO> findByCriteria(CampaignCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Campaigns by Criteria");
        return campaignRepository.findByCriteria(criteria, pageable).map(campaignMapper::toDto);
    }

    /**
     * Find the count of campaigns by criteria.
     * @param criteria filtering criteria
     * @return the count of campaigns
     */
    public Mono<Long> countByCriteria(CampaignCriteria criteria) {
        log.debug("Request to get the count of all Campaigns by Criteria");
        return campaignRepository.countByCriteria(criteria);
    }

    /**
     * Get all the campaigns with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<CampaignDTO> findAllWithEagerRelationships(Pageable pageable) {
        return campaignRepository.findAllWithEagerRelationships(pageable).map(campaignMapper::toDto);
    }

    /**
     * Returns the number of campaigns available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return campaignRepository.count();
    }

    /**
     * Get one campaign by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CampaignDTO> findOne(Long id) {
        log.debug("Request to get Campaign : {}", id);
        return campaignRepository.findOneWithEagerRelationships(id).map(campaignMapper::toDto);
    }

    /**
     * Delete the campaign by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Campaign : {}", id);
        return campaignRepository.deleteById(id);
    }
}
