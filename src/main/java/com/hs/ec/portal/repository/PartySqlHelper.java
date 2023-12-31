package com.hs.ec.portal.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PartySqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("party_code", table, columnPrefix + "_party_code"));
        columns.add(Column.aliased("trade_title", table, columnPrefix + "_trade_title"));
        columns.add(Column.aliased("activation_date", table, columnPrefix + "_activation_date"));
        columns.add(Column.aliased("expiration_date", table, columnPrefix + "_expiration_date"));
        columns.add(Column.aliased("activation_status", table, columnPrefix + "_activation_status"));
        columns.add(Column.aliased("photo", table, columnPrefix + "_photo"));
        columns.add(Column.aliased("photo_content_type", table, columnPrefix + "_photo_content_type"));
        columns.add(Column.aliased("person_type", table, columnPrefix + "_person_type"));

        return columns;
    }
}
