package com.hs.ec.portal.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class WalletSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("trans_type_class_id", table, columnPrefix + "_trans_type_class_id"));
        columns.add(Column.aliased("stock", table, columnPrefix + "_stock"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("deposit", table, columnPrefix + "_deposit"));
        columns.add(Column.aliased("withdrawal", table, columnPrefix + "_withdrawal"));

        return columns;
    }
}
