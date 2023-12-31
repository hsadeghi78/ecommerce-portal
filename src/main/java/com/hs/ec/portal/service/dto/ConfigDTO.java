package com.hs.ec.portal.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hs.ec.portal.domain.Config} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConfigDTO implements Serializable {

    private Long id;

    @Size(max = 300)
    private String displayName;

    @Size(max = 300)
    private String code;

    @Size(max = 1500)
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigDTO)) {
            return false;
        }

        ConfigDTO configDTO = (ConfigDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, configDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConfigDTO{" +
            "id=" + getId() +
            ", displayName='" + getDisplayName() + "'" +
            ", code='" + getCode() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
