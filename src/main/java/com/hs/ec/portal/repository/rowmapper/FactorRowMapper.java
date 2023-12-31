package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.Factor;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Factor}, with proper type conversions.
 */
@Service
public class FactorRowMapper implements BiFunction<Row, String, Factor> {

    private final ColumnConverter converter;

    public FactorRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Factor} stored in the database.
     */
    @Override
    public Factor apply(Row row, String prefix) {
        Factor entity = new Factor();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setFactorCode(converter.fromRow(row, prefix + "_factor_code", String.class));
        entity.setLastStatusClassId(converter.fromRow(row, prefix + "_last_status_class_id", Long.class));
        entity.setPaymentStateClassId(converter.fromRow(row, prefix + "_payment_state_class_id", Long.class));
        entity.setCategoryClassId(converter.fromRow(row, prefix + "_category_class_id", Long.class));
        entity.setTotalPrice(converter.fromRow(row, prefix + "_total_price", Double.class));
        entity.setDiscount(converter.fromRow(row, prefix + "_discount", Double.class));
        entity.setDiscountCode(converter.fromRow(row, prefix + "_discount_code", String.class));
        entity.setFinalTax(converter.fromRow(row, prefix + "_final_tax", Double.class));
        entity.setPayable(converter.fromRow(row, prefix + "_payable", Double.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setLocationId(converter.fromRow(row, prefix + "_location_id", Long.class));
        entity.setBuyerPartyId(converter.fromRow(row, prefix + "_buyer_party_id", Long.class));
        entity.setSellerPartyId(converter.fromRow(row, prefix + "_seller_party_id", Long.class));
        return entity;
    }
}
