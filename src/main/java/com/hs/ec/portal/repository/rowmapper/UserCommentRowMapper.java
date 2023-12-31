package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.UserComment;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UserComment}, with proper type conversions.
 */
@Service
public class UserCommentRowMapper implements BiFunction<Row, String, UserComment> {

    private final ColumnConverter converter;

    public UserCommentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UserComment} stored in the database.
     */
    @Override
    public UserComment apply(Row row, String prefix) {
        UserComment entity = new UserComment();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setRating(converter.fromRow(row, prefix + "_rating", Float.class));
        entity.setVisible(converter.fromRow(row, prefix + "_visible", Boolean.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setPartyId(converter.fromRow(row, prefix + "_party_id", Long.class));
        entity.setProductId(converter.fromRow(row, prefix + "_product_id", Long.class));
        entity.setFactorId(converter.fromRow(row, prefix + "_factor_id", Long.class));
        entity.setParentId(converter.fromRow(row, prefix + "_parent_id", Long.class));
        return entity;
    }
}
