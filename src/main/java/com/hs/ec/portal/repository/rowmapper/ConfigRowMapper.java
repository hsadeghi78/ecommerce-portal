package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.Config;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Config}, with proper type conversions.
 */
@Service
public class ConfigRowMapper implements BiFunction<Row, String, Config> {

    private final ColumnConverter converter;

    public ConfigRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Config} stored in the database.
     */
    @Override
    public Config apply(Row row, String prefix) {
        Config entity = new Config();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDisplayName(converter.fromRow(row, prefix + "_display_name", String.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setValue(converter.fromRow(row, prefix + "_value", String.class));
        return entity;
    }
}
