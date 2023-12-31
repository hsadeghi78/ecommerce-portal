package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.Factor;
import com.hs.ec.portal.domain.Party;
import com.hs.ec.portal.domain.Product;
import com.hs.ec.portal.domain.UserComment;
import com.hs.ec.portal.service.dto.FactorDTO;
import com.hs.ec.portal.service.dto.PartyDTO;
import com.hs.ec.portal.service.dto.ProductDTO;
import com.hs.ec.portal.service.dto.UserCommentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserComment} and its DTO {@link UserCommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserCommentMapper extends EntityMapper<UserCommentDTO, UserComment> {
    @Mapping(target = "party", source = "party", qualifiedByName = "partyTitle")
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    @Mapping(target = "factor", source = "factor", qualifiedByName = "factorId")
    @Mapping(target = "parent", source = "parent", qualifiedByName = "userCommentId")
    UserCommentDTO toDto(UserComment s);

    @Named("userCommentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserCommentDTO toDtoUserCommentId(UserComment userComment);

    @Named("partyTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    PartyDTO toDtoPartyTitle(Party party);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);

    @Named("factorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FactorDTO toDtoFactorId(Factor factor);
}
