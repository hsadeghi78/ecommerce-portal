package com.hs.ec.portal.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class UserCommentSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("rating", table, columnPrefix + "_rating"));
        columns.add(Column.aliased("visible", table, columnPrefix + "_visible"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));

        columns.add(Column.aliased("party_id", table, columnPrefix + "_party_id"));
        columns.add(Column.aliased("product_id", table, columnPrefix + "_product_id"));
        columns.add(Column.aliased("factor_id", table, columnPrefix + "_factor_id"));
        columns.add(Column.aliased("parent_id", table, columnPrefix + "_parent_id"));
        return columns;
    }
}
