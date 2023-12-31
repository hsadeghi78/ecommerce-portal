package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.ProductHistory;
import com.hs.ec.portal.domain.enumeration.Performance;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ProductHistory}, with proper type conversions.
 */
@Service
public class ProductHistoryRowMapper implements BiFunction<Row, String, ProductHistory> {

    private final ColumnConverter converter;

    public ProductHistoryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ProductHistory} stored in the database.
     */
    @Override
    public ProductHistory apply(Row row, String prefix) {
        ProductHistory entity = new ProductHistory();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setTypeClassId(converter.fromRow(row, prefix + "_type_class_id", Long.class));
        entity.setBrandClassId(converter.fromRow(row, prefix + "_brand_class_id", Long.class));
        entity.setSizee(converter.fromRow(row, prefix + "_sizee", String.class));
        entity.setRegularSizeClassId(converter.fromRow(row, prefix + "_regular_size_class_id", Long.class));
        entity.setLanguageClassId(converter.fromRow(row, prefix + "_language_class_id", Long.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setKeywords(converter.fromRow(row, prefix + "_keywords", String.class));
        entity.setPhoto1ContentType(converter.fromRow(row, prefix + "_photo_1_content_type", String.class));
        entity.setPhoto1(converter.fromRow(row, prefix + "_photo_1", byte[].class));
        entity.setCount(converter.fromRow(row, prefix + "_count", Double.class));
        entity.setDiscount(converter.fromRow(row, prefix + "_discount", Float.class));
        entity.setOriginalPrice(converter.fromRow(row, prefix + "_original_price", Double.class));
        entity.setFinalPrice(converter.fromRow(row, prefix + "_final_price", Double.class));
        entity.setPublishDate(converter.fromRow(row, prefix + "_publish_date", LocalDate.class));
        entity.setTransportDate(converter.fromRow(row, prefix + "_transport_date", LocalDate.class));
        entity.setCurrencyClassId(converter.fromRow(row, prefix + "_currency_class_id", Long.class));
        entity.setBonus(converter.fromRow(row, prefix + "_bonus", Float.class));
        entity.setWarrantyClassId(converter.fromRow(row, prefix + "_warranty_class_id", Long.class));
        entity.setDeliveryPlaceClassId(converter.fromRow(row, prefix + "_delivery_place_class_id", Long.class));
        entity.setPaymentPlaceClassId(converter.fromRow(row, prefix + "_payment_place_class_id", Long.class));
        entity.setPerformance(converter.fromRow(row, prefix + "_performance", Performance.class));
        entity.setOriginalityClassId(converter.fromRow(row, prefix + "_originality_class_id", Long.class));
        entity.setSatisfaction(converter.fromRow(row, prefix + "_satisfaction", Float.class));
        entity.setUsed(converter.fromRow(row, prefix + "_used", Boolean.class));
        entity.setCategoryId(converter.fromRow(row, prefix + "_category_id", Long.class));
        entity.setPartyId(converter.fromRow(row, prefix + "_party_id", Long.class));
        entity.setProductId(converter.fromRow(row, prefix + "_product_id", Long.class));
        entity.setPriceId(converter.fromRow(row, prefix + "_price_id", Long.class));
        entity.setCampaignId(converter.fromRow(row, prefix + "_campaign_id", Long.class));
        return entity;
    }
}
