package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.Category;
import com.hs.ec.portal.service.dto.CategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "categoryTitle")
    CategoryDTO toDto(Category s);

    @Named("categoryTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    CategoryDTO toDtoCategoryTitle(Category category);
}
