package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.Wallet;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Wallet}, with proper type conversions.
 */
@Service
public class WalletRowMapper implements BiFunction<Row, String, Wallet> {

    private final ColumnConverter converter;

    public WalletRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Wallet} stored in the database.
     */
    @Override
    public Wallet apply(Row row, String prefix) {
        Wallet entity = new Wallet();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTransTypeClassId(converter.fromRow(row, prefix + "_trans_type_class_id", Long.class));
        entity.setStock(converter.fromRow(row, prefix + "_stock", Double.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setDeposit(converter.fromRow(row, prefix + "_deposit", Double.class));
        entity.setWithdrawal(converter.fromRow(row, prefix + "_withdrawal", Double.class));
        return entity;
    }
}
