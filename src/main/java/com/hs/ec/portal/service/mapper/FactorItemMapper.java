package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.Factor;
import com.hs.ec.portal.domain.FactorItem;
import com.hs.ec.portal.domain.Product;
import com.hs.ec.portal.service.dto.FactorDTO;
import com.hs.ec.portal.service.dto.FactorItemDTO;
import com.hs.ec.portal.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FactorItem} and its DTO {@link FactorItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface FactorItemMapper extends EntityMapper<FactorItemDTO, FactorItem> {
    @Mapping(target = "factor", source = "factor", qualifiedByName = "factorId")
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    FactorItemDTO toDto(FactorItem s);

    @Named("factorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FactorDTO toDtoFactorId(Factor factor);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);
}
