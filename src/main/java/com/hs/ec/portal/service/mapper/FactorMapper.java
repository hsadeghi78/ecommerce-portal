package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.Factor;
import com.hs.ec.portal.domain.Location;
import com.hs.ec.portal.domain.Party;
import com.hs.ec.portal.service.dto.FactorDTO;
import com.hs.ec.portal.service.dto.LocationDTO;
import com.hs.ec.portal.service.dto.PartyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Factor} and its DTO {@link FactorDTO}.
 */
@Mapper(componentModel = "spring")
public interface FactorMapper extends EntityMapper<FactorDTO, Factor> {
    @Mapping(target = "location", source = "location", qualifiedByName = "locationId")
    @Mapping(target = "buyerParty", source = "buyerParty", qualifiedByName = "partyTitle")
    @Mapping(target = "sellerParty", source = "sellerParty", qualifiedByName = "partyTitle")
    FactorDTO toDto(Factor s);

    @Named("locationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocationDTO toDtoLocationId(Location location);

    @Named("partyTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    PartyDTO toDtoPartyTitle(Party party);
}
