package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.VwClassification;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link VwClassification}, with proper type conversions.
 */
@Service
public class VwClassificationRowMapper implements BiFunction<Row, String, VwClassification> {

    private final ColumnConverter converter;

    public VwClassificationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link VwClassification} stored in the database.
     */
    @Override
    public VwClassification apply(Row row, String prefix) {
        VwClassification entity = new VwClassification();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setClassCode(converter.fromRow(row, prefix + "_class_code", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setLanguageClassId(converter.fromRow(row, prefix + "_language_class_id", Long.class));
        entity.setTypeTitle(converter.fromRow(row, prefix + "_type_title", String.class));
        entity.setTypeCode(converter.fromRow(row, prefix + "_type_code", Integer.class));
        entity.setTypeDesc(converter.fromRow(row, prefix + "_type_desc", String.class));
        return entity;
    }
}
