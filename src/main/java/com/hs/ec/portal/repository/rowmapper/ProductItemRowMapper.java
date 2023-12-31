package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.ProductItem;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ProductItem}, with proper type conversions.
 */
@Service
public class ProductItemRowMapper implements BiFunction<Row, String, ProductItem> {

    private final ColumnConverter converter;

    public ProductItemRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ProductItem} stored in the database.
     */
    @Override
    public ProductItem apply(Row row, String prefix) {
        ProductItem entity = new ProductItem();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTypeClassId(converter.fromRow(row, prefix + "_type_class_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setValue(converter.fromRow(row, prefix + "_value", String.class));
        entity.setProductId(converter.fromRow(row, prefix + "_product_id", Long.class));
        return entity;
    }
}
