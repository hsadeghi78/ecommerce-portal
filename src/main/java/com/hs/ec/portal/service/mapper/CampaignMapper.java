package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.Campaign;
import com.hs.ec.portal.domain.Product;
import com.hs.ec.portal.service.dto.CampaignDTO;
import com.hs.ec.portal.service.dto.ProductDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Campaign} and its DTO {@link CampaignDTO}.
 */
@Mapper(componentModel = "spring")
public interface CampaignMapper extends EntityMapper<CampaignDTO, Campaign> {
    @Mapping(target = "products", source = "products", qualifiedByName = "productIdSet")
    CampaignDTO toDto(Campaign s);

    @Mapping(target = "removeProducts", ignore = true)
    Campaign toEntity(CampaignDTO campaignDTO);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("productIdSet")
    default Set<ProductDTO> toDtoProductIdSet(Set<Product> product) {
        return product.stream().map(this::toDtoProductId).collect(Collectors.toSet());
    }
}
