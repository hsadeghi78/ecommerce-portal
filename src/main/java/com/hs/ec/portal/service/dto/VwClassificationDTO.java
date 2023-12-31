package com.hs.ec.portal.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hs.ec.portal.domain.VwClassification} entity.
 */
@Schema(description = "a view from join classType and classification\n@author Hossein Sadeghi (hsadeghi78@gmail.com)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VwClassificationDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 200)
    private String title;

    @NotNull(message = "must not be null")
    @Size(max = 25)
    private String classCode;

    @Size(max = 300)
    private String description;

    @NotNull(message = "must not be null")
    private Long languageClassId;

    @NotNull(message = "must not be null")
    @Size(max = 200)
    private String typeTitle;

    @NotNull(message = "must not be null")
    private Integer typeCode;

    @Size(max = 300)
    private String typeDesc;

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

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLanguageClassId() {
        return languageClassId;
    }

    public void setLanguageClassId(Long languageClassId) {
        this.languageClassId = languageClassId;
    }

    public String getTypeTitle() {
        return typeTitle;
    }

    public void setTypeTitle(String typeTitle) {
        this.typeTitle = typeTitle;
    }

    public Integer getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(Integer typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VwClassificationDTO)) {
            return false;
        }

        VwClassificationDTO vwClassificationDTO = (VwClassificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vwClassificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VwClassificationDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", classCode='" + getClassCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", languageClassId=" + getLanguageClassId() +
            ", typeTitle='" + getTypeTitle() + "'" +
            ", typeCode=" + getTypeCode() +
            ", typeDesc='" + getTypeDesc() + "'" +
            "}";
    }
}
