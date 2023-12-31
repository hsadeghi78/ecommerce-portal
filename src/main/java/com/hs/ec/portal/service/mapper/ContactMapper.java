package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.Contact;
import com.hs.ec.portal.domain.Party;
import com.hs.ec.portal.service.dto.ContactDTO;
import com.hs.ec.portal.service.dto.PartyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contact} and its DTO {@link ContactDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContactMapper extends EntityMapper<ContactDTO, Contact> {
    @Mapping(target = "party", source = "party", qualifiedByName = "partyTitle")
    ContactDTO toDto(Contact s);

    @Named("partyTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    PartyDTO toDtoPartyTitle(Party party);
}
