package com.hs.ec.portal.service.mapper;

import com.hs.ec.portal.domain.MyAuthority;
import com.hs.ec.portal.service.dto.MyAuthorityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MyAuthority} and its DTO {@link MyAuthorityDTO}.
 */
@Mapper(componentModel = "spring")
public interface MyAuthorityMapper extends EntityMapper<MyAuthorityDTO, MyAuthority> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "myAuthorityName")
    MyAuthorityDTO toDto(MyAuthority s);

    @Named("myAuthorityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MyAuthorityDTO toDtoMyAuthorityName(MyAuthority myAuthority);
}
