package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.Resource;
import com.hs.ec.portal.domain.enumeration.ResourceType;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Resource}, with proper type conversions.
 */
@Service
public class ResourceRowMapper implements BiFunction<Row, String, Resource> {

    private final ColumnConverter converter;

    public ResourceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Resource} stored in the database.
     */
    @Override
    public Resource apply(Row row, String prefix) {
        Resource entity = new Resource();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDisplayName(converter.fromRow(row, prefix + "_display_name", String.class));
        entity.setApiUri(converter.fromRow(row, prefix + "_api_uri", String.class));
        entity.setResourceType(converter.fromRow(row, prefix + "_resource_type", ResourceType.class));
        return entity;
    }
}
