package com.hs.ec.portal.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class AgreementSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("start_date", table, columnPrefix + "_start_date"));
        columns.add(Column.aliased("end_date", table, columnPrefix + "_end_date"));
        columns.add(Column.aliased("activation_status_class_id", table, columnPrefix + "_activation_status_class_id"));
        columns.add(Column.aliased("infrastructure_benefit", table, columnPrefix + "_infrastructure_benefit"));
        columns.add(Column.aliased("extra_benefit", table, columnPrefix + "_extra_benefit"));

        columns.add(Column.aliased("provider_id", table, columnPrefix + "_provider_id"));
        columns.add(Column.aliased("consumer_id", table, columnPrefix + "_consumer_id"));
        return columns;
    }
}
