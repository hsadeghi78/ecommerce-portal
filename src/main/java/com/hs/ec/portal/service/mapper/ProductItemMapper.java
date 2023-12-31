package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.Product;
import com.hs.ec.portal.domain.ProductItem;
import com.hs.ec.portal.service.dto.ProductDTO;
import com.hs.ec.portal.service.dto.ProductItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductItem} and its DTO {@link ProductItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductItemMapper extends EntityMapper<ProductItemDTO, ProductItem> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    ProductItemDTO toDto(ProductItem s);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);
}
