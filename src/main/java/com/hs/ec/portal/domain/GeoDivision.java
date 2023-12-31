package com.hs.ec.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * For management Gographic Division in 4 level(Country,Province,County,City)
 * @author Hossein Sadeghi (hsadeghi78@gmail.com)
 */
@Table("geo_division")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GeoDivision implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 200)
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("code")
    private Long code;

    @NotNull(message = "must not be null")
    @Column("level")
    private Integer level;

    @Transient
    @JsonIgnoreProperties(value = { "children", "locations", "parent" }, allowSetters = true)
    private Set<GeoDivision> children = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "factor", "geoDivision", "party" }, allowSetters = true)
    private Set<Location> locations = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "children", "locations", "parent" }, allowSetters = true)
    private GeoDivision parent;

    @Column("parent_id")
    private Long parentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GeoDivision id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public GeoDivision name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCode() {
        return this.code;
    }

    public GeoDivision code(Long code) {
        this.setCode(code);
        return this;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Integer getLevel() {
        return this.level;
    }

    public GeoDivision level(Integer level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Set<GeoDivision> getChildren() {
        return this.children;
    }

    public void setChildren(Set<GeoDivision> geoDivisions) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (geoDivisions != null) {
            geoDivisions.forEach(i -> i.setParent(this));
        }
        this.children = geoDivisions;
    }

    public GeoDivision children(Set<GeoDivision> geoDivisions) {
        this.setChildren(geoDivisions);
        return this;
    }

    public GeoDivision addChildren(GeoDivision geoDivision) {
        this.children.add(geoDivision);
        geoDivision.setParent(this);
        return this;
    }

    public GeoDivision removeChildren(GeoDivision geoDivision) {
        this.children.remove(geoDivision);
        geoDivision.setParent(null);
        return this;
    }

    public Set<Location> getLocations() {
        return this.locations;
    }

    public void setLocations(Set<Location> locations) {
        if (this.locations != null) {
            this.locations.forEach(i -> i.setGeoDivision(null));
        }
        if (locations != null) {
            locations.forEach(i -> i.setGeoDivision(this));
        }
        this.locations = locations;
    }

    public GeoDivision locations(Set<Location> locations) {
        this.setLocations(locations);
        return this;
    }

    public GeoDivision addLocations(Location location) {
        this.locations.add(location);
        location.setGeoDivision(this);
        return this;
    }

    public GeoDivision removeLocations(Location location) {
        this.locations.remove(location);
        location.setGeoDivision(null);
        return this;
    }

    public GeoDivision getParent() {
        return this.parent;
    }

    public void setParent(GeoDivision geoDivision) {
        this.parent = geoDivision;
        this.parentId = geoDivision != null ? geoDivision.getId() : null;
    }

    public GeoDivision parent(GeoDivision geoDivision) {
        this.setParent(geoDivision);
        return this;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long geoDivision) {
        this.parentId = geoDivision;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeoDivision)) {
            return false;
        }
        return getId() != null && getId().equals(((GeoDivision) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GeoDivision{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code=" + getCode() +
            ", level=" + getLevel() +
            "}";
    }
}
