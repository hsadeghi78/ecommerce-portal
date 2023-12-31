package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.Location;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Location}, with proper type conversions.
 */
@Service
public class LocationRowMapper implements BiFunction<Row, String, Location> {

    private final ColumnConverter converter;

    public LocationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Location} stored in the database.
     */
    @Override
    public Location apply(Row row, String prefix) {
        Location entity = new Location();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTypeClassId(converter.fromRow(row, prefix + "_type_class_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setLat(converter.fromRow(row, prefix + "_lat", Double.class));
        entity.setLon(converter.fromRow(row, prefix + "_lon", Double.class));
        entity.setStreet1(converter.fromRow(row, prefix + "_street_1", String.class));
        entity.setStreet2(converter.fromRow(row, prefix + "_street_2", String.class));
        entity.setStreet3(converter.fromRow(row, prefix + "_street_3", String.class));
        entity.setBuildingNo(converter.fromRow(row, prefix + "_building_no", Integer.class));
        entity.setBuildingName(converter.fromRow(row, prefix + "_building_name", String.class));
        entity.setFloor(converter.fromRow(row, prefix + "_floor", Integer.class));
        entity.setUnit(converter.fromRow(row, prefix + "_unit", Integer.class));
        entity.setPostalCode(converter.fromRow(row, prefix + "_postal_code", String.class));
        entity.setOther(converter.fromRow(row, prefix + "_other", String.class));
        entity.setGeoDivisionId(converter.fromRow(row, prefix + "_geo_division_id", Long.class));
        entity.setPartyId(converter.fromRow(row, prefix + "_party_id", Long.class));
        return entity;
    }
}
