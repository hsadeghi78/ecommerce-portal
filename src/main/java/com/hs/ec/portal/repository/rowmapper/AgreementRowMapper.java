package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.Agreement;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Agreement}, with proper type conversions.
 */
@Service
public class AgreementRowMapper implements BiFunction<Row, String, Agreement> {

    private final ColumnConverter converter;

    public AgreementRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Agreement} stored in the database.
     */
    @Override
    public Agreement apply(Row row, String prefix) {
        Agreement entity = new Agreement();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setStartDate(converter.fromRow(row, prefix + "_start_date", LocalDate.class));
        entity.setEndDate(converter.fromRow(row, prefix + "_end_date", LocalDate.class));
        entity.setActivationStatusClassId(converter.fromRow(row, prefix + "_activation_status_class_id", Long.class));
        entity.setInfrastructureBenefit(converter.fromRow(row, prefix + "_infrastructure_benefit", Double.class));
        entity.setExtraBenefit(converter.fromRow(row, prefix + "_extra_benefit", Double.class));
        entity.setProviderId(converter.fromRow(row, prefix + "_provider_id", Long.class));
        entity.setConsumerId(converter.fromRow(row, prefix + "_consumer_id", Long.class));
        return entity;
    }
}
