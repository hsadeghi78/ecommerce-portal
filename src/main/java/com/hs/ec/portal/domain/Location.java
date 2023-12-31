package com.hs.ec.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Locations of All Partyies
 * @author Hossein Sadeghi (hsadeghi78@gmail.com)
 */
@Table("location")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    /**
     * typeClass WORK,HOME,BRANCH
     */
    @NotNull(message = "must not be null")
    @Column("type_class_id")
    private Long typeClassId;

    @NotNull(message = "must not be null")
    @Size(max = 200)
    @Column("title")
    private String title;

    @NotNull(message = "must not be null")
    @Column("lat")
    private Double lat;

    @NotNull(message = "must not be null")
    @Column("lon")
    private Double lon;

    @Size(max = 200)
    @Column("street_1")
    private String street1;

    @Size(max = 200)
    @Column("street_2")
    private String street2;

    @Size(max = 200)
    @Column("street_3")
    private String street3;

    @NotNull(message = "must not be null")
    @Column("building_no")
    private Integer buildingNo;

    @Size(max = 100)
    @Column("building_name")
    private String buildingName;

    @Column("floor")
    private Integer floor;

    @NotNull(message = "must not be null")
    @Column("unit")
    private Integer unit;

    @NotNull(message = "must not be null")
    @Size(max = 12)
    @Column("postal_code")
    private String postalCode;

    @Column("other")
    private String other;

    @Transient
    private Factor factor;

    @Transient
    @JsonIgnoreProperties(value = { "children", "locations", "parent" }, allowSetters = true)
    private GeoDivision geoDivision;

    @Transient
    @JsonIgnoreProperties(
        value = {
            "buyerFactors",
            "sellerFactors",
            "userComments",
            "products",
            "providerAgreements",
            "consumerAgreements",
            "contacts",
            "locations",
        },
        allowSetters = true
    )
    private Party party;

    @Column("geo_division_id")
    private Long geoDivisionId;

    @Column("party_id")
    private Long partyId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Location id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTypeClassId() {
        return this.typeClassId;
    }

    public Location typeClassId(Long typeClassId) {
        this.setTypeClassId(typeClassId);
        return this;
    }

    public void setTypeClassId(Long typeClassId) {
        this.typeClassId = typeClassId;
    }

    public String getTitle() {
        return this.title;
    }

    public Location title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getLat() {
        return this.lat;
    }

    public Location lat(Double lat) {
        this.setLat(lat);
        return this;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return this.lon;
    }

    public Location lon(Double lon) {
        this.setLon(lon);
        return this;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getStreet1() {
        return this.street1;
    }

    public Location street1(String street1) {
        this.setStreet1(street1);
        return this;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return this.street2;
    }

    public Location street2(String street2) {
        this.setStreet2(street2);
        return this;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getStreet3() {
        return this.street3;
    }

    public Location street3(String street3) {
        this.setStreet3(street3);
        return this;
    }

    public void setStreet3(String street3) {
        this.street3 = street3;
    }

    public Integer getBuildingNo() {
        return this.buildingNo;
    }

    public Location buildingNo(Integer buildingNo) {
        this.setBuildingNo(buildingNo);
        return this;
    }

    public void setBuildingNo(Integer buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getBuildingName() {
        return this.buildingName;
    }

    public Location buildingName(String buildingName) {
        this.setBuildingName(buildingName);
        return this;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Integer getFloor() {
        return this.floor;
    }

    public Location floor(Integer floor) {
        this.setFloor(floor);
        return this;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getUnit() {
        return this.unit;
    }

    public Location unit(Integer unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public Location postalCode(String postalCode) {
        this.setPostalCode(postalCode);
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getOther() {
        return this.other;
    }

    public Location other(String other) {
        this.setOther(other);
        return this;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public Factor getFactor() {
        return this.factor;
    }

    public void setFactor(Factor factor) {
        if (this.factor != null) {
            this.factor.setLocation(null);
        }
        if (factor != null) {
            factor.setLocation(this);
        }
        this.factor = factor;
    }

    public Location factor(Factor factor) {
        this.setFactor(factor);
        return this;
    }

    public GeoDivision getGeoDivision() {
        return this.geoDivision;
    }

    public void setGeoDivision(GeoDivision geoDivision) {
        this.geoDivision = geoDivision;
        this.geoDivisionId = geoDivision != null ? geoDivision.getId() : null;
    }

    public Location geoDivision(GeoDivision geoDivision) {
        this.setGeoDivision(geoDivision);
        return this;
    }

    public Party getParty() {
        return this.party;
    }

    public void setParty(Party party) {
        this.party = party;
        this.partyId = party != null ? party.getId() : null;
    }

    public Location party(Party party) {
        this.setParty(party);
        return this;
    }

    public Long getGeoDivisionId() {
        return this.geoDivisionId;
    }

    public void setGeoDivisionId(Long geoDivision) {
        this.geoDivisionId = geoDivision;
    }

    public Long getPartyId() {
        return this.partyId;
    }

    public void setPartyId(Long party) {
        this.partyId = party;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return getId() != null && getId().equals(((Location) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
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
            "}";
    }
}
