package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.Party;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Party}, with proper type conversions.
 */
@Service
public class PartyRowMapper implements BiFunction<Row, String, Party> {

    private final ColumnConverter converter;

    public PartyRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Party} stored in the database.
     */
    @Override
    public Party apply(Row row, String prefix) {
        Party entity = new Party();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setPartyCode(converter.fromRow(row, prefix + "_party_code", String.class));
        entity.setTradeTitle(converter.fromRow(row, prefix + "_trade_title", String.class));
        entity.setActivationDate(converter.fromRow(row, prefix + "_activation_date", LocalDate.class));
        entity.setExpirationDate(converter.fromRow(row, prefix + "_expiration_date", LocalDate.class));
        entity.setActivationStatus(converter.fromRow(row, prefix + "_activation_status", Boolean.class));
        entity.setPhotoContentType(converter.fromRow(row, prefix + "_photo_content_type", String.class));
        entity.setPhoto(converter.fromRow(row, prefix + "_photo", byte[].class));
        entity.setPersonType(converter.fromRow(row, prefix + "_person_type", Boolean.class));
        return entity;
    }
}
