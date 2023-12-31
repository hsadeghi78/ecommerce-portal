package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.ProductHistory;
import com.hs.ec.portal.service.dto.ProductHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductHistory} and its DTO {@link ProductHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductHistoryMapper extends EntityMapper<ProductHistoryDTO, ProductHistory> {}
