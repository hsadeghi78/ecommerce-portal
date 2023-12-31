package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.ConsumeMaterial;
import com.hs.ec.portal.domain.Product;
import com.hs.ec.portal.service.dto.ConsumeMaterialDTO;
import com.hs.ec.portal.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConsumeMaterial} and its DTO {@link ConsumeMaterialDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConsumeMaterialMapper extends EntityMapper<ConsumeMaterialDTO, ConsumeMaterial> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    ConsumeMaterialDTO toDto(ConsumeMaterial s);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);
}
