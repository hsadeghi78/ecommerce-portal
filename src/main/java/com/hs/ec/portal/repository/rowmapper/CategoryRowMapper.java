package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.Category;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Category}, with proper type conversions.
 */
@Service
public class CategoryRowMapper implements BiFunction<Row, String, Category> {

    private final ColumnConverter converter;

    public CategoryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Category} stored in the database.
     */
    @Override
    public Category apply(Row row, String prefix) {
        Category entity = new Category();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setHasChild(converter.fromRow(row, prefix + "_has_child", Boolean.class));
        entity.setLevel(converter.fromRow(row, prefix + "_level", Integer.class));
        entity.setKeywords(converter.fromRow(row, prefix + "_keywords", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setParentId(converter.fromRow(row, prefix + "_parent_id", Long.class));
        return entity;
    }
}
