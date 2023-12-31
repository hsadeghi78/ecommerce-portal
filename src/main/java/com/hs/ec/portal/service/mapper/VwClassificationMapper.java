package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.VwClassification;
import com.hs.ec.portal.service.dto.VwClassificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VwClassification} and its DTO {@link VwClassificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface VwClassificationMapper extends EntityMapper<VwClassificationDTO, VwClassification> {}
