package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.GeoDivision;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link GeoDivision}, with proper type conversions.
 */
@Service
public class GeoDivisionRowMapper implements BiFunction<Row, String, GeoDivision> {

    private final ColumnConverter converter;

    public GeoDivisionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link GeoDivision} stored in the database.
     */
    @Override
    public GeoDivision apply(Row row, String prefix) {
        GeoDivision entity = new GeoDivision();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", Long.class));
        entity.setLevel(converter.fromRow(row, prefix + "_level", Integer.class));
        entity.setParentId(converter.fromRow(row, prefix + "_parent_id", Long.class));
        return entity;
    }
}
