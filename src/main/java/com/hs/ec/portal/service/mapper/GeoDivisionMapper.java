package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.GeoDivision;
import com.hs.ec.portal.service.dto.GeoDivisionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GeoDivision} and its DTO {@link GeoDivisionDTO}.
 */
@Mapper(componentModel = "spring")
public interface GeoDivisionMapper extends EntityMapper<GeoDivisionDTO, GeoDivision> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "geoDivisionName")
    GeoDivisionDTO toDto(GeoDivision s);

    @Named("geoDivisionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    GeoDivisionDTO toDtoGeoDivisionName(GeoDivision geoDivision);
}
