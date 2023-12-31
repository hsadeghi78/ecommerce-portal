package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.ConfigCriteria;
import com.hs.ec.portal.repository.ConfigRepository;
import com.hs.ec.portal.service.dto.ConfigDTO;
import com.hs.ec.portal.service.mapper.ConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.Config}.
 */
@Service
@Transactional
public class ConfigService {

    private final Logger log = LoggerFactory.getLogger(ConfigService.class);

    private final ConfigRepository configRepository;

    private final ConfigMapper configMapper;

    public ConfigService(ConfigRepository configRepository, ConfigMapper configMapper) {
        this.configRepository = configRepository;
        this.configMapper = configMapper;
    }

    /**
     * Save a config.
     *
     * @param configDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ConfigDTO> save(ConfigDTO configDTO) {
        log.debug("Request to save Config : {}", configDTO);
        return configRepository.save(configMapper.toEntity(configDTO)).map(configMapper::toDto);
    }

    /**
     * Update a config.
     *
     * @param configDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ConfigDTO> update(ConfigDTO configDTO) {
        log.debug("Request to update Config : {}", configDTO);
        return configRepository.save(configMapper.toEntity(configDTO)).map(configMapper::toDto);
    }

    /**
     * Partially update a config.
     *
     * @param configDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ConfigDTO> partialUpdate(ConfigDTO configDTO) {
        log.debug("Request to partially update Config : {}", configDTO);

        return configRepository
            .findById(configDTO.getId())
            .map(existingConfig -> {
                configMapper.partialUpdate(existingConfig, configDTO);

                return existingConfig;
            })
            .flatMap(configRepository::save)
            .map(configMapper::toDto);
    }

    /**
     * Get all the configs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ConfigDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Configs");
        return configRepository.findAllBy(pageable).map(configMapper::toDto);
    }

    /**
     * Find configs by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ConfigDTO> findByCriteria(ConfigCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Configs by Criteria");
        return configRepository.findByCriteria(criteria, pageable).map(configMapper::toDto);
    }

    /**
     * Find the count of configs by criteria.
     * @param criteria filtering criteria
     * @return the count of configs
     */
    public Mono<Long> countByCriteria(ConfigCriteria criteria) {
        log.debug("Request to get the count of all Configs by Criteria");
        return configRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of configs available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return configRepository.count();
    }

    /**
     * Get one config by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ConfigDTO> findOne(Long id) {
        log.debug("Request to get Config : {}", id);
        return configRepository.findById(id).map(configMapper::toDto);
    }

    /**
     * Delete the config by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Config : {}", id);
        return configRepository.deleteById(id);
    }
}
