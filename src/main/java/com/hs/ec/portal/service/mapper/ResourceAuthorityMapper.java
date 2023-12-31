package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.MyAuthority;
import com.hs.ec.portal.domain.Resource;
import com.hs.ec.portal.domain.ResourceAuthority;
import com.hs.ec.portal.service.dto.MyAuthorityDTO;
import com.hs.ec.portal.service.dto.ResourceAuthorityDTO;
import com.hs.ec.portal.service.dto.ResourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResourceAuthority} and its DTO {@link ResourceAuthorityDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResourceAuthorityMapper extends EntityMapper<ResourceAuthorityDTO, ResourceAuthority> {
    @Mapping(target = "resource", source = "resource", qualifiedByName = "resourceDisplayName")
    @Mapping(target = "myAuthority", source = "myAuthority", qualifiedByName = "myAuthorityDisplayName")
    ResourceAuthorityDTO toDto(ResourceAuthority s);

    @Named("resourceDisplayName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "displayName", source = "displayName")
    ResourceDTO toDtoResourceDisplayName(Resource resource);

    @Named("myAuthorityDisplayName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "displayName", source = "displayName")
    MyAuthorityDTO toDtoMyAuthorityDisplayName(MyAuthority myAuthority);
}
