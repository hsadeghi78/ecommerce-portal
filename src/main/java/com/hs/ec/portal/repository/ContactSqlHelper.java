package com.hs.ec.portal.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ContactSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("contact_value", table, columnPrefix + "_contact_value"));
        columns.add(Column.aliased("type_class_id", table, columnPrefix + "_type_class_id"));
        columns.add(Column.aliased("prefix", table, columnPrefix + "_prefix"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));

        columns.add(Column.aliased("party_id", table, columnPrefix + "_party_id"));
        return columns;
    }
}
