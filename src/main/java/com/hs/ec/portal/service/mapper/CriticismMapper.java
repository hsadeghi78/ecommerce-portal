package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.Criticism;
import com.hs.ec.portal.service.dto.CriticismDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Criticism} and its DTO {@link CriticismDTO}.
 */
@Mapper(componentModel = "spring")
public interface CriticismMapper extends EntityMapper<CriticismDTO, Criticism> {}
