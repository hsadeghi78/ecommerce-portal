package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.GeoDivision;
import com.hs.ec.portal.domain.Location;
import com.hs.ec.portal.domain.Party;
import com.hs.ec.portal.service.dto.GeoDivisionDTO;
import com.hs.ec.portal.service.dto.LocationDTO;
import com.hs.ec.portal.service.dto.PartyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Location} and its DTO {@link LocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {
    @Mapping(target = "geoDivision", source = "geoDivision", qualifiedByName = "geoDivisionId")
    @Mapping(target = "party", source = "party", qualifiedByName = "partyTitle")
    LocationDTO toDto(Location s);

    @Named("geoDivisionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GeoDivisionDTO toDtoGeoDivisionId(GeoDivision geoDivision);

    @Named("partyTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    PartyDTO toDtoPartyTitle(Party party);
}
