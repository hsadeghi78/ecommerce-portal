package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.Product;
import com.hs.ec.portal.domain.UserFavorite;
import com.hs.ec.portal.service.dto.ProductDTO;
import com.hs.ec.portal.service.dto.UserFavoriteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserFavorite} and its DTO {@link UserFavoriteDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserFavoriteMapper extends EntityMapper<UserFavoriteDTO, UserFavorite> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    UserFavoriteDTO toDto(UserFavorite s);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);
}
