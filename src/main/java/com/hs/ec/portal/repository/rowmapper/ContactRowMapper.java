package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.Contact;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Contact}, with proper type conversions.
 */
@Service
public class ContactRowMapper implements BiFunction<Row, String, Contact> {

    private final ColumnConverter converter;

    public ContactRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Contact} stored in the database.
     */
    @Override
    public Contact apply(Row row, String prefix) {
        Contact entity = new Contact();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setContactValue(converter.fromRow(row, prefix + "_contact_value", String.class));
        entity.setTypeClassId(converter.fromRow(row, prefix + "_type_class_id", Long.class));
        entity.setPrefix(converter.fromRow(row, prefix + "_prefix", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setPartyId(converter.fromRow(row, prefix + "_party_id", Long.class));
        return entity;
    }
}
