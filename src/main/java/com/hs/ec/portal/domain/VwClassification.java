package com.hs.ec.portal.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * a view from join classType and classification
 * @author Hossein Sadeghi (hsadeghi78@gmail.com)
 */
@Table("vw_classification")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VwClassification implements Serializable {

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

    @NotNull(message = "must not be null")
    @Column("language_class_id")
    private Long languageClassId;

    @NotNull(message = "must not be null")
    @Size(max = 200)
    @Column("type_title")
    private String typeTitle;

    @NotNull(message = "must not be null")
    @Column("type_code")
    private Integer typeCode;

    @Size(max = 300)
    @Column("type_desc")
    private String typeDesc;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VwClassification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public VwClassification title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClassCode() {
        return this.classCode;
    }

    public VwClassification classCode(String classCode) {
        this.setClassCode(classCode);
        return this;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getDescription() {
        return this.description;
    }

    public VwClassification description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLanguageClassId() {
        return this.languageClassId;
    }

    public VwClassification languageClassId(Long languageClassId) {
        this.setLanguageClassId(languageClassId);
        return this;
    }

    public void setLanguageClassId(Long languageClassId) {
        this.languageClassId = languageClassId;
    }

    public String getTypeTitle() {
        return this.typeTitle;
    }

    public VwClassification typeTitle(String typeTitle) {
        this.setTypeTitle(typeTitle);
        return this;
    }

    public void setTypeTitle(String typeTitle) {
        this.typeTitle = typeTitle;
    }

    public Integer getTypeCode() {
        return this.typeCode;
    }

    public VwClassification typeCode(Integer typeCode) {
        this.setTypeCode(typeCode);
        return this;
    }

    public void setTypeCode(Integer typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeDesc() {
        return this.typeDesc;
    }

    public VwClassification typeDesc(String typeDesc) {
        this.setTypeDesc(typeDesc);
        return this;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VwClassification)) {
            return false;
        }
        return getId() != null && getId().equals(((VwClassification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VwClassification{" +
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
