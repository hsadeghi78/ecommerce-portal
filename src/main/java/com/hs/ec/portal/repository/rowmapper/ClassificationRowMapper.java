package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.Classification;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Classification}, with proper type conversions.
 */
@Service
public class ClassificationRowMapper implements BiFunction<Row, String, Classification> {

    private final ColumnConverter converter;

    public ClassificationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Classification} stored in the database.
     */
    @Override
    public Classification apply(Row row, String prefix) {
        Classification entity = new Classification();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setClassCode(converter.fromRow(row, prefix + "_class_code", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setLanguageClassId(converter.fromRow(row, prefix + "_language_class_id", Long.class));
        entity.setClassTypeId(converter.fromRow(row, prefix + "_class_type_id", Long.class));
        return entity;
    }
}
