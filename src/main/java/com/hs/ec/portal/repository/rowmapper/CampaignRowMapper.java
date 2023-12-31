package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.Campaign;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Campaign}, with proper type conversions.
 */
@Service
public class CampaignRowMapper implements BiFunction<Row, String, Campaign> {

    private final ColumnConverter converter;

    public CampaignRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Campaign} stored in the database.
     */
    @Override
    public Campaign apply(Row row, String prefix) {
        Campaign entity = new Campaign();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setStartDate(converter.fromRow(row, prefix + "_start_date", LocalDate.class));
        entity.setEndDate(converter.fromRow(row, prefix + "_end_date", LocalDate.class));
        entity.setPhotoContentType(converter.fromRow(row, prefix + "_photo_content_type", String.class));
        entity.setPhoto(converter.fromRow(row, prefix + "_photo", byte[].class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        return entity;
    }
}
