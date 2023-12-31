package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.Category;
import com.hs.ec.portal.domain.FileDocument;
import com.hs.ec.portal.domain.Party;
import com.hs.ec.portal.domain.Product;
import com.hs.ec.portal.service.dto.CategoryDTO;
import com.hs.ec.portal.service.dto.FileDocumentDTO;
import com.hs.ec.portal.service.dto.PartyDTO;
import com.hs.ec.portal.service.dto.ProductDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "documents", source = "documents", qualifiedByName = "fileDocumentIdSet")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryTitle")
    @Mapping(target = "party", source = "party", qualifiedByName = "partyTitle")
    @Mapping(target = "parent", source = "parent", qualifiedByName = "productName")
    ProductDTO toDto(Product s);

    @Mapping(target = "removeDocuments", ignore = true)
    Product toEntity(ProductDTO productDTO);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);

    @Named("fileDocumentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FileDocumentDTO toDtoFileDocumentId(FileDocument fileDocument);

    @Named("fileDocumentIdSet")
    default Set<FileDocumentDTO> toDtoFileDocumentIdSet(Set<FileDocument> fileDocument) {
        return fileDocument.stream().map(this::toDtoFileDocumentId).collect(Collectors.toSet());
    }

    @Named("categoryTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    CategoryDTO toDtoCategoryTitle(Category category);

    @Named("partyTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    PartyDTO toDtoPartyTitle(Party party);
}
