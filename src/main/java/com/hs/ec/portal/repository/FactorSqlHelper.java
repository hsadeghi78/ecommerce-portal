package com.hs.ec.portal.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class FactorSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("factor_code", table, columnPrefix + "_factor_code"));
        columns.add(Column.aliased("last_status_class_id", table, columnPrefix + "_last_status_class_id"));
        columns.add(Column.aliased("payment_state_class_id", table, columnPrefix + "_payment_state_class_id"));
        columns.add(Column.aliased("category_class_id", table, columnPrefix + "_category_class_id"));
        columns.add(Column.aliased("total_price", table, columnPrefix + "_total_price"));
        columns.add(Column.aliased("discount", table, columnPrefix + "_discount"));
        columns.add(Column.aliased("discount_code", table, columnPrefix + "_discount_code"));
        columns.add(Column.aliased("final_tax", table, columnPrefix + "_final_tax"));
        columns.add(Column.aliased("payable", table, columnPrefix + "_payable"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));

        columns.add(Column.aliased("location_id", table, columnPrefix + "_location_id"));
        columns.add(Column.aliased("buyer_party_id", table, columnPrefix + "_buyer_party_id"));
        columns.add(Column.aliased("seller_party_id", table, columnPrefix + "_seller_party_id"));
        return columns;
    }
}
