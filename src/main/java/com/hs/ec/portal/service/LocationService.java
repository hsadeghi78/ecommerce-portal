package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.LocationCriteria;
import com.hs.ec.portal.repository.LocationRepository;
import com.hs.ec.portal.service.dto.LocationDTO;
import com.hs.ec.portal.service.mapper.LocationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.Location}.
 */
@Service
@Transactional
public class LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationService.class);

    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    public LocationService(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    /**
     * Save a location.
     *
     * @param locationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<LocationDTO> save(LocationDTO locationDTO) {
        log.debug("Request to save Location : {}", locationDTO);
        return locationRepository.save(locationMapper.toEntity(locationDTO)).map(locationMapper::toDto);
    }

    /**
     * Update a location.
     *
     * @param locationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<LocationDTO> update(LocationDTO locationDTO) {
        log.debug("Request to update Location : {}", locationDTO);
        return locationRepository.save(locationMapper.toEntity(locationDTO)).map(locationMapper::toDto);
    }

    /**
     * Partially update a location.
     *
     * @param locationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<LocationDTO> partialUpdate(LocationDTO locationDTO) {
        log.debug("Request to partially update Location : {}", locationDTO);

        return locationRepository
            .findById(locationDTO.getId())
            .map(existingLocation -> {
                locationMapper.partialUpdate(existingLocation, locationDTO);

                return existingLocation;
            })
            .flatMap(locationRepository::save)
            .map(locationMapper::toDto);
    }

    /**
     * Get all the locations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<LocationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Locations");
        return locationRepository.findAllBy(pageable).map(locationMapper::toDto);
    }

    /**
     * Find locations by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<LocationDTO> findByCriteria(LocationCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Locations by Criteria");
        return locationRepository.findByCriteria(criteria, pageable).map(locationMapper::toDto);
    }

    /**
     * Find the count of locations by criteria.
     * @param criteria filtering criteria
     * @return the count of locations
     */
    public Mono<Long> countByCriteria(LocationCriteria criteria) {
        log.debug("Request to get the count of all Locations by Criteria");
        return locationRepository.countByCriteria(criteria);
    }

    /**
     * Get all the locations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<LocationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return locationRepository.findAllWithEagerRelationships(pageable).map(locationMapper::toDto);
    }

    /**
     *  Get all the locations where Factor is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<LocationDTO> findAllWhereFactorIsNull() {
        log.debug("Request to get all locations where Factor is null");
        return locationRepository.findAllWhereFactorIsNull().map(locationMapper::toDto);
    }

    /**
     * Returns the number of locations available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return locationRepository.count();
    }

    /**
     * Get one location by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<LocationDTO> findOne(Long id) {
        log.debug("Request to get Location : {}", id);
        return locationRepository.findOneWithEagerRelationships(id).map(locationMapper::toDto);
    }

    /**
     * Delete the location by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Location : {}", id);
        return locationRepository.deleteById(id);
    }
}
