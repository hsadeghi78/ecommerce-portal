package com.hs.ec.portal.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CategorySqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("code", table, columnPrefix + "_code"));
        columns.add(Column.aliased("has_child", table, columnPrefix + "_has_child"));
        columns.add(Column.aliased("level", table, columnPrefix + "_level"));
        columns.add(Column.aliased("keywords", table, columnPrefix + "_keywords"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));

        columns.add(Column.aliased("parent_id", table, columnPrefix + "_parent_id"));
        return columns;
    }
}
