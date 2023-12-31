package com.hs.ec.portal.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hs.ec.portal.domain.Location} entity. This class is used
 * in {@link com.hs.ec.portal.web.rest.LocationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /locations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LocationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter typeClassId;

    private StringFilter title;

    private DoubleFilter lat;

    private DoubleFilter lon;

    private StringFilter street1;

    private StringFilter street2;

    private StringFilter street3;

    private IntegerFilter buildingNo;

    private StringFilter buildingName;

    private IntegerFilter floor;

    private IntegerFilter unit;

    private StringFilter postalCode;

    private StringFilter other;

    private LongFilter geoDivisionId;

    private LongFilter partyId;

    private Boolean distinct;

    public LocationCriteria() {}

    public LocationCriteria(LocationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.typeClassId = other.typeClassId == null ? null : other.typeClassId.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.lat = other.lat == null ? null : other.lat.copy();
        this.lon = other.lon == null ? null : other.lon.copy();
        this.street1 = other.street1 == null ? null : other.street1.copy();
        this.street2 = other.street2 == null ? null : other.street2.copy();
        this.street3 = other.street3 == null ? null : other.street3.copy();
        this.buildingNo = other.buildingNo == null ? null : other.buildingNo.copy();
        this.buildingName = other.buildingName == null ? null : other.buildingName.copy();
        this.floor = other.floor == null ? null : other.floor.copy();
        this.unit = other.unit == null ? null : other.unit.copy();
        this.postalCode = other.postalCode == null ? null : other.postalCode.copy();
        this.other = other.other == null ? null : other.other.copy();
        this.geoDivisionId = other.geoDivisionId == null ? null : other.geoDivisionId.copy();
        this.partyId = other.partyId == null ? null : other.partyId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LocationCriteria copy() {
        return new LocationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getTypeClassId() {
        return typeClassId;
    }

    public LongFilter typeClassId() {
        if (typeClassId == null) {
            typeClassId = new LongFilter();
        }
        return typeClassId;
    }

    public void setTypeClassId(LongFilter typeClassId) {
        this.typeClassId = typeClassId;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public DoubleFilter getLat() {
        return lat;
    }

    public DoubleFilter lat() {
        if (lat == null) {
            lat = new DoubleFilter();
        }
        return lat;
    }

    public void setLat(DoubleFilter lat) {
        this.lat = lat;
    }

    public DoubleFilter getLon() {
        return lon;
    }

    public DoubleFilter lon() {
        if (lon == null) {
            lon = new DoubleFilter();
        }
        return lon;
    }

    public void setLon(DoubleFilter lon) {
        this.lon = lon;
    }

    public StringFilter getStreet1() {
        return street1;
    }

    public StringFilter street1() {
        if (street1 == null) {
            street1 = new StringFilter();
        }
        return street1;
    }

    public void setStreet1(StringFilter street1) {
        this.street1 = street1;
    }

    public StringFilter getStreet2() {
        return street2;
    }

    public StringFilter street2() {
        if (street2 == null) {
            street2 = new StringFilter();
        }
        return street2;
    }

    public void setStreet2(StringFilter street2) {
        this.street2 = street2;
    }

    public StringFilter getStreet3() {
        return street3;
    }

    public StringFilter street3() {
        if (street3 == null) {
            street3 = new StringFilter();
        }
        return street3;
    }

    public void setStreet3(StringFilter street3) {
        this.street3 = street3;
    }

    public IntegerFilter getBuildingNo() {
        return buildingNo;
    }

    public IntegerFilter buildingNo() {
        if (buildingNo == null) {
            buildingNo = new IntegerFilter();
        }
        return buildingNo;
    }

    public void setBuildingNo(IntegerFilter buildingNo) {
        this.buildingNo = buildingNo;
    }

    public StringFilter getBuildingName() {
        return buildingName;
    }

    public StringFilter buildingName() {
        if (buildingName == null) {
            buildingName = new StringFilter();
        }
        return buildingName;
    }

    public void setBuildingName(StringFilter buildingName) {
        this.buildingName = buildingName;
    }

    public IntegerFilter getFloor() {
        return floor;
    }

    public IntegerFilter floor() {
        if (floor == null) {
            floor = new IntegerFilter();
        }
        return floor;
    }

    public void setFloor(IntegerFilter floor) {
        this.floor = floor;
    }

    public IntegerFilter getUnit() {
        return unit;
    }

    public IntegerFilter unit() {
        if (unit == null) {
            unit = new IntegerFilter();
        }
        return unit;
    }

    public void setUnit(IntegerFilter unit) {
        this.unit = unit;
    }

    public StringFilter getPostalCode() {
        return postalCode;
    }

    public StringFilter postalCode() {
        if (postalCode == null) {
            postalCode = new StringFilter();
        }
        return postalCode;
    }

    public void setPostalCode(StringFilter postalCode) {
        this.postalCode = postalCode;
    }

    public StringFilter getOther() {
        return other;
    }

    public StringFilter other() {
        if (other == null) {
            other = new StringFilter();
        }
        return other;
    }

    public void setOther(StringFilter other) {
        this.other = other;
    }

    public LongFilter getGeoDivisionId() {
        return geoDivisionId;
    }

    public LongFilter geoDivisionId() {
        if (geoDivisionId == null) {
            geoDivisionId = new LongFilter();
        }
        return geoDivisionId;
    }

    public void setGeoDivisionId(LongFilter geoDivisionId) {
        this.geoDivisionId = geoDivisionId;
    }

    public LongFilter getPartyId() {
        return partyId;
    }

    public LongFilter partyId() {
        if (partyId == null) {
            partyId = new LongFilter();
        }
        return partyId;
    }

    public void setPartyId(LongFilter partyId) {
        this.partyId = partyId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LocationCriteria that = (LocationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(typeClassId, that.typeClassId) &&
            Objects.equals(title, that.title) &&
            Objects.equals(lat, that.lat) &&
            Objects.equals(lon, that.lon) &&
            Objects.equals(street1, that.street1) &&
            Objects.equals(street2, that.street2) &&
            Objects.equals(street3, that.street3) &&
            Objects.equals(buildingNo, that.buildingNo) &&
            Objects.equals(buildingName, that.buildingName) &&
            Objects.equals(floor, that.floor) &&
            Objects.equals(unit, that.unit) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(other, that.other) &&
            Objects.equals(geoDivisionId, that.geoDivisionId) &&
            Objects.equals(partyId, that.partyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            typeClassId,
            title,
            lat,
            lon,
            street1,
            street2,
            street3,
            buildingNo,
            buildingName,
            floor,
            unit,
            postalCode,
            other,
            geoDivisionId,
            partyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (typeClassId != null ? "typeClassId=" + typeClassId + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (lat != null ? "lat=" + lat + ", " : "") +
            (lon != null ? "lon=" + lon + ", " : "") +
            (street1 != null ? "street1=" + street1 + ", " : "") +
            (street2 != null ? "street2=" + street2 + ", " : "") +
            (street3 != null ? "street3=" + street3 + ", " : "") +
            (buildingNo != null ? "buildingNo=" + buildingNo + ", " : "") +
            (buildingName != null ? "buildingName=" + buildingName + ", " : "") +
            (floor != null ? "floor=" + floor + ", " : "") +
            (unit != null ? "unit=" + unit + ", " : "") +
            (postalCode != null ? "postalCode=" + postalCode + ", " : "") +
            (other != null ? "other=" + other + ", " : "") +
            (geoDivisionId != null ? "geoDivisionId=" + geoDivisionId + ", " : "") +
            (partyId != null ? "partyId=" + partyId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
