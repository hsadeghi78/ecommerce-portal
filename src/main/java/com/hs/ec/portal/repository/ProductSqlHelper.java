package com.hs.ec.portal.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ProductSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("type_class_id", table, columnPrefix + "_type_class_id"));
        columns.add(Column.aliased("brand_class_id", table, columnPrefix + "_brand_class_id"));
        columns.add(Column.aliased("sizee", table, columnPrefix + "_sizee"));
        columns.add(Column.aliased("regular_size_class_id", table, columnPrefix + "_regular_size_class_id"));
        columns.add(Column.aliased("language_class_id", table, columnPrefix + "_language_class_id"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("keywords", table, columnPrefix + "_keywords"));
        columns.add(Column.aliased("photo_1", table, columnPrefix + "_photo_1"));
        columns.add(Column.aliased("photo_1_content_type", table, columnPrefix + "_photo_1_content_type"));
        columns.add(Column.aliased("nationality_class_id", table, columnPrefix + "_nationality_class_id"));
        columns.add(Column.aliased("count", table, columnPrefix + "_count"));
        columns.add(Column.aliased("discount", table, columnPrefix + "_discount"));
        columns.add(Column.aliased("original_price", table, columnPrefix + "_original_price"));
        columns.add(Column.aliased("final_price", table, columnPrefix + "_final_price"));
        columns.add(Column.aliased("publish_date", table, columnPrefix + "_publish_date"));
        columns.add(Column.aliased("transport_date", table, columnPrefix + "_transport_date"));
        columns.add(Column.aliased("currency_class_id", table, columnPrefix + "_currency_class_id"));
        columns.add(Column.aliased("bonus", table, columnPrefix + "_bonus"));
        columns.add(Column.aliased("warranty_class_id", table, columnPrefix + "_warranty_class_id"));
        columns.add(Column.aliased("delivery_place_class_id", table, columnPrefix + "_delivery_place_class_id"));
        columns.add(Column.aliased("payment_place_class_id", table, columnPrefix + "_payment_place_class_id"));
        columns.add(Column.aliased("performance", table, columnPrefix + "_performance"));
        columns.add(Column.aliased("originality_class_id", table, columnPrefix + "_originality_class_id"));
        columns.add(Column.aliased("satisfaction", table, columnPrefix + "_satisfaction"));
        columns.add(Column.aliased("used", table, columnPrefix + "_used"));

        columns.add(Column.aliased("category_id", table, columnPrefix + "_category_id"));
        columns.add(Column.aliased("party_id", table, columnPrefix + "_party_id"));
        columns.add(Column.aliased("parent_id", table, columnPrefix + "_parent_id"));
        return columns;
    }
}
