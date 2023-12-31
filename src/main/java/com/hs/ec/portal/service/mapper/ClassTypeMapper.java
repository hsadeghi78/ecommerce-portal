package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.ClassType;
import com.hs.ec.portal.service.dto.ClassTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClassType} and its DTO {@link ClassTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClassTypeMapper extends EntityMapper<ClassTypeDTO, ClassType> {}
