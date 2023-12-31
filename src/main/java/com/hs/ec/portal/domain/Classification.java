package com.hs.ec.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Classification for Constants
 * @author Hossein Sadeghi (hsadeghi78@gmail.com)
 */
@Table("classification")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Classification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 200)
    @Column("title")
    private String title;

    @NotNull(message = "must not be null")
    @Size(max = 25)
    @Column("class_code")
    private String classCode;

    @Size(max = 300)
    @Column("description")
    private String description;

    /**
     * such as fa_IR, en_US, ar_AE, ar_SA and .....
     */
    @NotNull(message = "must not be null")
    @Column("language_class_id")
    private Long languageClassId;

    @Transient
    @JsonIgnoreProperties(value = { "classifications" }, allowSetters = true)
    private ClassType classType;

    @Column("class_type_id")
    private Long classTypeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Classification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Classification title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClassCode() {
        return this.classCode;
    }

    public Classification classCode(String classCode) {
        this.setClassCode(classCode);
        return this;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getDescription() {
        return this.description;
    }

    public Classification description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLanguageClassId() {
        return this.languageClassId;
    }

    public Classification languageClassId(Long languageClassId) {
        this.setLanguageClassId(languageClassId);
        return this;
    }

    public void setLanguageClassId(Long languageClassId) {
        this.languageClassId = languageClassId;
    }

    public ClassType getClassType() {
        return this.classType;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
        this.classTypeId = classType != null ? classType.getId() : null;
    }

    public Classification classType(ClassType classType) {
        this.setClassType(classType);
        return this;
    }

    public Long getClassTypeId() {
        return this.classTypeId;
    }

    public void setClassTypeId(Long classType) {
        this.classTypeId = classType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Classification)) {
            return false;
        }
        return getId() != null && getId().equals(((Classification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Classification{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", classCode='" + getClassCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", languageClassId=" + getLanguageClassId() +
            "}";
    }
}
