package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.Config;
import com.hs.ec.portal.service.dto.ConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Config} and its DTO {@link ConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConfigMapper extends EntityMapper<ConfigDTO, Config> {}
