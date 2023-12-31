package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.ConsumeMaterial;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ConsumeMaterial}, with proper type conversions.
 */
@Service
public class ConsumeMaterialRowMapper implements BiFunction<Row, String, ConsumeMaterial> {

    private final ColumnConverter converter;

    public ConsumeMaterialRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ConsumeMaterial} stored in the database.
     */
    @Override
    public ConsumeMaterial apply(Row row, String prefix) {
        ConsumeMaterial entity = new ConsumeMaterial();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTypeClassId(converter.fromRow(row, prefix + "_type_class_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setValue(converter.fromRow(row, prefix + "_value", String.class));
        entity.setProductId(converter.fromRow(row, prefix + "_product_id", Long.class));
        return entity;
    }
}
