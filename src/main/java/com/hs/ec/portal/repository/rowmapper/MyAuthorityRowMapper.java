package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.MyAuthority;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link MyAuthority}, with proper type conversions.
 */
@Service
public class MyAuthorityRowMapper implements BiFunction<Row, String, MyAuthority> {

    private final ColumnConverter converter;

    public MyAuthorityRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link MyAuthority} stored in the database.
     */
    @Override
    public MyAuthority apply(Row row, String prefix) {
        MyAuthority entity = new MyAuthority();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDisplayName(converter.fromRow(row, prefix + "_display_name", String.class));
        entity.setParentId(converter.fromRow(row, prefix + "_parent_id", Long.class));
        return entity;
    }
}
