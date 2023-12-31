package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.Party;
import com.hs.ec.portal.service.dto.PartyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Party} and its DTO {@link PartyDTO}.
 */
@Mapper(componentModel = "spring")
public interface PartyMapper extends EntityMapper<PartyDTO, Party> {}
