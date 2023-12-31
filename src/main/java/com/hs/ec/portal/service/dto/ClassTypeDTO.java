package com.hs.ec.portal.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hs.ec.portal.domain.ClassType} entity.
 */
@Schema(description = "Classification Types for Constants\n@author Hossein Sadeghi (hsadeghi78@gmail.com)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClassTypeDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 200)
    private String title;

    @NotNull(message = "must not be null")
    private Integer typeCode;

    @Size(max = 300)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(Integer typeCode) {
        this.typeCode = typeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassTypeDTO)) {
            return false;
        }

        ClassTypeDTO classTypeDTO = (ClassTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, classTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassTypeDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", typeCode=" + getTypeCode() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
