package com.hs.ec.portal.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ResourceSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("display_name", table, columnPrefix + "_display_name"));
        columns.add(Column.aliased("api_uri", table, columnPrefix + "_api_uri"));
        columns.add(Column.aliased("resource_type", table, columnPrefix + "_resource_type"));

        return columns;
    }
}
