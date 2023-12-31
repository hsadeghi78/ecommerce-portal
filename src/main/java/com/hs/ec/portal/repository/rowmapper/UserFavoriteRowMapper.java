package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.UserFavorite;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link UserFavorite}, with proper type conversions.
 */
@Service
public class UserFavoriteRowMapper implements BiFunction<Row, String, UserFavorite> {

    private final ColumnConverter converter;

    public UserFavoriteRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UserFavorite} stored in the database.
     */
    @Override
    public UserFavorite apply(Row row, String prefix) {
        UserFavorite entity = new UserFavorite();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setProductId(converter.fromRow(row, prefix + "_product_id", Long.class));
        return entity;
    }
}
