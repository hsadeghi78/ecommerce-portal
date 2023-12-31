package com.hs.ec.portal.service;

import com.hs.ec.portal.domain.criteria.WalletCriteria;
import com.hs.ec.portal.repository.WalletRepository;
import com.hs.ec.portal.service.dto.WalletDTO;
import com.hs.ec.portal.service.mapper.WalletMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.hs.ec.portal.domain.Wallet}.
 */
@Service
@Transactional
public class WalletService {

    private final Logger log = LoggerFactory.getLogger(WalletService.class);

    private final WalletRepository walletRepository;

    private final WalletMapper walletMapper;

    public WalletService(WalletRepository walletRepository, WalletMapper walletMapper) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
    }

    /**
     * Save a wallet.
     *
     * @param walletDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<WalletDTO> save(WalletDTO walletDTO) {
        log.debug("Request to save Wallet : {}", walletDTO);
        return walletRepository.save(walletMapper.toEntity(walletDTO)).map(walletMapper::toDto);
    }

    /**
     * Update a wallet.
     *
     * @param walletDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<WalletDTO> update(WalletDTO walletDTO) {
        log.debug("Request to update Wallet : {}", walletDTO);
        return walletRepository.save(walletMapper.toEntity(walletDTO)).map(walletMapper::toDto);
    }

    /**
     * Partially update a wallet.
     *
     * @param walletDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<WalletDTO> partialUpdate(WalletDTO walletDTO) {
        log.debug("Request to partially update Wallet : {}", walletDTO);

        return walletRepository
            .findById(walletDTO.getId())
            .map(existingWallet -> {
                walletMapper.partialUpdate(existingWallet, walletDTO);

                return existingWallet;
            })
            .flatMap(walletRepository::save)
            .map(walletMapper::toDto);
    }

    /**
     * Get all the wallets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WalletDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Wallets");
        return walletRepository.findAllBy(pageable).map(walletMapper::toDto);
    }

    /**
     * Find wallets by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WalletDTO> findByCriteria(WalletCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Wallets by Criteria");
        return walletRepository.findByCriteria(criteria, pageable).map(walletMapper::toDto);
    }

    /**
     * Find the count of wallets by criteria.
     * @param criteria filtering criteria
     * @return the count of wallets
     */
    public Mono<Long> countByCriteria(WalletCriteria criteria) {
        log.debug("Request to get the count of all Wallets by Criteria");
        return walletRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of wallets available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return walletRepository.count();
    }

    /**
     * Get one wallet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<WalletDTO> findOne(Long id) {
        log.debug("Request to get Wallet : {}", id);
        return walletRepository.findById(id).map(walletMapper::toDto);
    }

    /**
     * Delete the wallet by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Wallet : {}", id);
        return walletRepository.deleteById(id);
    }
}
