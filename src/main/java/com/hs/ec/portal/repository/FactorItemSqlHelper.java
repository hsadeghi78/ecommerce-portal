package com.hs.ec.portal.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class FactorItemSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("row_num", table, columnPrefix + "_row_num"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("count", table, columnPrefix + "_count"));
        columns.add(Column.aliased("discount", table, columnPrefix + "_discount"));
        columns.add(Column.aliased("tax", table, columnPrefix + "_tax"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));

        columns.add(Column.aliased("factor_id", table, columnPrefix + "_factor_id"));
        columns.add(Column.aliased("product_id", table, columnPrefix + "_product_id"));
        return columns;
    }
}
