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
 * Classification Types for Constants
 * @author Hossein Sadeghi (hsadeghi78@gmail.com)
 */
@Table("class_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClassType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 200)
    @Column("title")
    private String title;

    @NotNull(message = "must not be null")
    @Column("type_code")
    private Integer typeCode;

    @Size(max = 300)
    @Column("description")
    private String description;

    @Transient
    @JsonIgnoreProperties(value = { "classType" }, allowSetters = true)
    private Set<Classification> classifications = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ClassType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public ClassType title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTypeCode() {
        return this.typeCode;
    }

    public ClassType typeCode(Integer typeCode) {
        this.setTypeCode(typeCode);
        return this;
    }

    public void setTypeCode(Integer typeCode) {
        this.typeCode = typeCode;
    }

    public String getDescription() {
        return this.description;
    }

    public ClassType description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Classification> getClassifications() {
        return this.classifications;
    }

    public void setClassifications(Set<Classification> classifications) {
        if (this.classifications != null) {
            this.classifications.forEach(i -> i.setClassType(null));
        }
        if (classifications != null) {
            classifications.forEach(i -> i.setClassType(this));
        }
        this.classifications = classifications;
    }

    public ClassType classifications(Set<Classification> classifications) {
        this.setClassifications(classifications);
        return this;
    }

    public ClassType addClassifications(Classification classification) {
        this.classifications.add(classification);
        classification.setClassType(this);
        return this;
    }

    public ClassType removeClassifications(Classification classification) {
        this.classifications.remove(classification);
        classification.setClassType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassType)) {
            return false;
        }
        return getId() != null && getId().equals(((ClassType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassType{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", typeCode=" + getTypeCode() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
