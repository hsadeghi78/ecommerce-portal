package com.hs.ec.portal.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class LocationSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("type_class_id", table, columnPrefix + "_type_class_id"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("lat", table, columnPrefix + "_lat"));
        columns.add(Column.aliased("lon", table, columnPrefix + "_lon"));
        columns.add(Column.aliased("street_1", table, columnPrefix + "_street_1"));
        columns.add(Column.aliased("street_2", table, columnPrefix + "_street_2"));
        columns.add(Column.aliased("street_3", table, columnPrefix + "_street_3"));
        columns.add(Column.aliased("building_no", table, columnPrefix + "_building_no"));
        columns.add(Column.aliased("building_name", table, columnPrefix + "_building_name"));
        columns.add(Column.aliased("floor", table, columnPrefix + "_floor"));
        columns.add(Column.aliased("unit", table, columnPrefix + "_unit"));
        columns.add(Column.aliased("postal_code", table, columnPrefix + "_postal_code"));
        columns.add(Column.aliased("other", table, columnPrefix + "_other"));

        columns.add(Column.aliased("geo_division_id", table, columnPrefix + "_geo_division_id"));
        columns.add(Column.aliased("party_id", table, columnPrefix + "_party_id"));
        return columns;
    }
}
