package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.FactorItem;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link FactorItem}, with proper type conversions.
 */
@Service
public class FactorItemRowMapper implements BiFunction<Row, String, FactorItem> {

    private final ColumnConverter converter;

    public FactorItemRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link FactorItem} stored in the database.
     */
    @Override
    public FactorItem apply(Row row, String prefix) {
        FactorItem entity = new FactorItem();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setRowNum(converter.fromRow(row, prefix + "_row_num", Integer.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setCount(converter.fromRow(row, prefix + "_count", Integer.class));
        entity.setDiscount(converter.fromRow(row, prefix + "_discount", Double.class));
        entity.setTax(converter.fromRow(row, prefix + "_tax", Double.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setFactorId(converter.fromRow(row, prefix + "_factor_id", Long.class));
        entity.setProductId(converter.fromRow(row, prefix + "_product_id", Long.class));
        return entity;
    }
}
