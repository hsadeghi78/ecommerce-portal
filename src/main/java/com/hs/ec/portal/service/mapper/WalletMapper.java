package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.Wallet;
import com.hs.ec.portal.service.dto.WalletDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Wallet} and its DTO {@link WalletDTO}.
 */
@Mapper(componentModel = "spring")
public interface WalletMapper extends EntityMapper<WalletDTO, Wallet> {}
