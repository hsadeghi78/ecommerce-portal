package com.hs.ec.portal.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hs.ec.portal.domain.GeoDivision} entity.
 */
@Schema(
    description = "For management Gographic Division in 4 level(Country,Province,County,City)\n@author Hossein Sadeghi (hsadeghi78@gmail.com)"
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GeoDivisionDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 200)
    private String name;

    @NotNull(message = "must not be null")
    private Long code;

    @NotNull(message = "must not be null")
    private Integer level;

    private GeoDivisionDTO parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public GeoDivisionDTO getParent() {
        return parent;
    }

    public void setParent(GeoDivisionDTO parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeoDivisionDTO)) {
            return false;
        }

        GeoDivisionDTO geoDivisionDTO = (GeoDivisionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, geoDivisionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GeoDivisionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code=" + getCode() +
            ", level=" + getLevel() +
            ", parent=" + getParent() +
            "}";
    }
}
