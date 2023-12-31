package com.hs.ec.portal.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ResourceAuthoritySqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("verb", table, columnPrefix + "_verb"));

        columns.add(Column.aliased("resource_id", table, columnPrefix + "_resource_id"));
        columns.add(Column.aliased("my_authority_id", table, columnPrefix + "_my_authority_id"));
        return columns;
    }
}
