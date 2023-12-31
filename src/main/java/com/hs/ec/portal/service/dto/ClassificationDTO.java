package com.hs.ec.portal.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hs.ec.portal.domain.Classification} entity.
 */
@Schema(description = "Classification for Constants\n@author Hossein Sadeghi (hsadeghi78@gmail.com)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClassificationDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 200)
    private String title;

    @NotNull(message = "must not be null")
    @Size(max = 25)
    private String classCode;

    @Size(max = 300)
    private String description;

    /**
     * such as fa_IR, en_US, ar_AE, ar_SA and .....
     */
    @NotNull(message = "must not be null")
    @Schema(description = "such as fa_IR, en_US, ar_AE, ar_SA and .....", required = true)
    private Long languageClassId;

    private ClassTypeDTO classType;

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

    public ClassTypeDTO getClassType() {
        return classType;
    }

    public void setClassType(ClassTypeDTO classType) {
        this.classType = classType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassificationDTO)) {
            return false;
        }

        ClassificationDTO classificationDTO = (ClassificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, classificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassificationDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", classCode='" + getClassCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", languageClassId=" + getLanguageClassId() +
            ", classType=" + getClassType() +
            "}";
    }
}
