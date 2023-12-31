package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.Agreement;
import com.hs.ec.portal.domain.Party;
import com.hs.ec.portal.service.dto.AgreementDTO;
import com.hs.ec.portal.service.dto.PartyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Agreement} and its DTO {@link AgreementDTO}.
 */
@Mapper(componentModel = "spring")
public interface AgreementMapper extends EntityMapper<AgreementDTO, Agreement> {
    @Mapping(target = "provider", source = "provider", qualifiedByName = "partyTitle")
    @Mapping(target = "consumer", source = "consumer", qualifiedByName = "partyTitle")
    AgreementDTO toDto(Agreement s);

    @Named("partyTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    PartyDTO toDtoPartyTitle(Party party);
}
