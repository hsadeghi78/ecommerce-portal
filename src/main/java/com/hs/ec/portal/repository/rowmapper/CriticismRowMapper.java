package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.Criticism;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Criticism}, with proper type conversions.
 */
@Service
public class CriticismRowMapper implements BiFunction<Row, String, Criticism> {

    private final ColumnConverter converter;

    public CriticismRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Criticism} stored in the database.
     */
    @Override
    public Criticism apply(Row row, String prefix) {
        Criticism entity = new Criticism();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFullName(converter.fromRow(row, prefix + "_full_name", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setContactNumber(converter.fromRow(row, prefix + "_contact_number", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        return entity;
    }
}
