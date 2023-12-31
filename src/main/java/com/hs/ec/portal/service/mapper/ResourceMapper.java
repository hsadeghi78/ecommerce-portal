package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.Resource;
import com.hs.ec.portal.service.dto.ResourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Resource} and its DTO {@link ResourceDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResourceMapper extends EntityMapper<ResourceDTO, Resource> {}
