package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.ResourceAuthority;
import com.hs.ec.portal.domain.enumeration.Verb;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ResourceAuthority}, with proper type conversions.
 */
@Service
public class ResourceAuthorityRowMapper implements BiFunction<Row, String, ResourceAuthority> {

    private final ColumnConverter converter;

    public ResourceAuthorityRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ResourceAuthority} stored in the database.
     */
    @Override
    public ResourceAuthority apply(Row row, String prefix) {
        ResourceAuthority entity = new ResourceAuthority();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setVerb(converter.fromRow(row, prefix + "_verb", Verb.class));
        entity.setResourceId(converter.fromRow(row, prefix + "_resource_id", Long.class));
        entity.setMyAuthorityId(converter.fromRow(row, prefix + "_my_authority_id", Long.class));
        return entity;
    }
}
