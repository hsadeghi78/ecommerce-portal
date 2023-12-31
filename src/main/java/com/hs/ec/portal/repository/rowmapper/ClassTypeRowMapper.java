package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.ClassType;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ClassType}, with proper type conversions.
 */
@Service
public class ClassTypeRowMapper implements BiFunction<Row, String, ClassType> {

    private final ColumnConverter converter;

    public ClassTypeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ClassType} stored in the database.
     */
    @Override
    public ClassType apply(Row row, String prefix) {
        ClassType entity = new ClassType();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setTypeCode(converter.fromRow(row, prefix + "_type_code", Integer.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        return entity;
    }
}
