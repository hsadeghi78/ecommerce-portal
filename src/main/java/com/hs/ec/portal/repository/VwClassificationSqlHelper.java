package com.hs.ec.portal.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class VwClassificationSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("class_code", table, columnPrefix + "_class_code"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("language_class_id", table, columnPrefix + "_language_class_id"));
        columns.add(Column.aliased("type_title", table, columnPrefix + "_type_title"));
        columns.add(Column.aliased("type_code", table, columnPrefix + "_type_code"));
        columns.add(Column.aliased("type_desc", table, columnPrefix + "_type_desc"));

        return columns;
    }
}
