package com.hs.ec.portal.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hs.ec.portal.domain.Location} entity.
 */
@Schema(description = "Locations of All Partyies\n@author Hossein Sadeghi (hsadeghi78@gmail.com)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LocationDTO implements Serializable {

    private Long id;

    /**
     * typeClass WORK,HOME,BRANCH
     */
    @NotNull(message = "must not be null")
    @Schema(description = "typeClass WORK,HOME,BRANCH", required = true)
    private Long typeClassId;

    @NotNull(message = "must not be null")
    @Size(max = 200)
    private String title;

    @NotNull(message = "must not be null")
    private Double lat;

    @NotNull(message = "must not be null")
    private Double lon;

    @Size(max = 200)
    private String street1;

    @Size(max = 200)
    private String street2;

    @Size(max = 200)
    private String street3;

    @NotNull(message = "must not be null")
    private Integer buildingNo;

    @Size(max = 100)
    private String buildingName;

    private Integer floor;

    @NotNull(message = "must not be null")
    private Integer unit;

    @NotNull(message = "must not be null")
    @Size(max = 12)
    private String postalCode;

    private String other;

    private GeoDivisionDTO geoDivision;

    private PartyDTO party;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTypeClassId() {
        return typeClassId;
    }

    public void setTypeClassId(Long typeClassId) {
        this.typeClassId = typeClassId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getStreet3() {
        return street3;
    }

    public void setStreet3(String street3) {
        this.street3 = street3;
    }

    public Integer getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(Integer buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public GeoDivisionDTO getGeoDivision() {
        return geoDivision;
    }

    public void setGeoDivision(GeoDivisionDTO geoDivision) {
        this.geoDivision = geoDivision;
    }

    public PartyDTO getParty() {
        return party;
    }

    public void setParty(PartyDTO party) {
        this.party = party;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationDTO)) {
            return false;
        }

        LocationDTO locationDTO = (LocationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, locationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationDTO{" +
            "id=" + getId() +
            ", typeClassId=" + getTypeClassId() +
            ", title='" + getTitle() + "'" +
            ", lat=" + getLat() +
            ", lon=" + getLon() +
            ", street1='" + getStreet1() + "'" +
            ", street2='" + getStreet2() + "'" +
            ", street3='" + getStreet3() + "'" +
            ", buildingNo=" + getBuildingNo() +
            ", buildingName='" + getBuildingName() + "'" +
            ", floor=" + getFloor() +
            ", unit=" + getUnit() +
            ", postalCode='" + getPostalCode() + "'" +
            ", other='" + getOther() + "'" +
            ", geoDivision=" + getGeoDivision() +
            ", party=" + getParty() +
            "}";
    }
}
