package com.hs.ec.portal.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hs.ec.portal.domain.Classification} entity. This class is used
 * in {@link com.hs.ec.portal.web.rest.ClassificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /classifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClassificationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter classCode;

    private StringFilter description;

    private LongFilter languageClassId;

    private LongFilter classTypeId;

    private Boolean distinct;

    public ClassificationCriteria() {}

    public ClassificationCriteria(ClassificationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.classCode = other.classCode == null ? null : other.classCode.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.languageClassId = other.languageClassId == null ? null : other.languageClassId.copy();
        this.classTypeId = other.classTypeId == null ? null : other.classTypeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ClassificationCriteria copy() {
        return new ClassificationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getClassCode() {
        return classCode;
    }

    public StringFilter classCode() {
        if (classCode == null) {
            classCode = new StringFilter();
        }
        return classCode;
    }

    public void setClassCode(StringFilter classCode) {
        this.classCode = classCode;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getLanguageClassId() {
        return languageClassId;
    }

    public LongFilter languageClassId() {
        if (languageClassId == null) {
            languageClassId = new LongFilter();
        }
        return languageClassId;
    }

    public void setLanguageClassId(LongFilter languageClassId) {
        this.languageClassId = languageClassId;
    }

    public LongFilter getClassTypeId() {
        return classTypeId;
    }

    public LongFilter classTypeId() {
        if (classTypeId == null) {
            classTypeId = new LongFilter();
        }
        return classTypeId;
    }

    public void setClassTypeId(LongFilter classTypeId) {
        this.classTypeId = classTypeId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClassificationCriteria that = (ClassificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(classCode, that.classCode) &&
            Objects.equals(description, that.description) &&
            Objects.equals(languageClassId, that.languageClassId) &&
            Objects.equals(classTypeId, that.classTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, classCode, description, languageClassId, classTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassificationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (classCode != null ? "classCode=" + classCode + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (languageClassId != null ? "languageClassId=" + languageClassId + ", " : "") +
            (classTypeId != null ? "classTypeId=" + classTypeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
