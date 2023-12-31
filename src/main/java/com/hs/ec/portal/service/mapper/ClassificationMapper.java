package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.ClassType;
import com.hs.ec.portal.domain.Classification;
import com.hs.ec.portal.service.dto.ClassTypeDTO;
import com.hs.ec.portal.service.dto.ClassificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Classification} and its DTO {@link ClassificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClassificationMapper extends EntityMapper<ClassificationDTO, Classification> {
    @Mapping(target = "classType", source = "classType", qualifiedByName = "classTypeTitle")
    ClassificationDTO toDto(Classification s);

    @Named("classTypeTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    ClassTypeDTO toDtoClassTypeTitle(ClassType classType);
}
