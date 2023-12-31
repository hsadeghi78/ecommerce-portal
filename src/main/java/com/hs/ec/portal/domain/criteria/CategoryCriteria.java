package com.hs.ec.portal.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hs.ec.portal.domain.Category} entity. This class is used
 * in {@link com.hs.ec.portal.web.rest.CategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter code;

    private BooleanFilter hasChild;

    private IntegerFilter level;

    private StringFilter keywords;

    private StringFilter description;

    private LongFilter parentId;

    private Boolean distinct;

    public CategoryCriteria() {}

    public CategoryCriteria(CategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.hasChild = other.hasChild == null ? null : other.hasChild.copy();
        this.level = other.level == null ? null : other.level.copy();
        this.keywords = other.keywords == null ? null : other.keywords.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CategoryCriteria copy() {
        return new CategoryCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public BooleanFilter getHasChild() {
        return hasChild;
    }

    public BooleanFilter hasChild() {
        if (hasChild == null) {
            hasChild = new BooleanFilter();
        }
        return hasChild;
    }

    public void setHasChild(BooleanFilter hasChild) {
        this.hasChild = hasChild;
    }

    public IntegerFilter getLevel() {
        return level;
    }

    public IntegerFilter level() {
        if (level == null) {
            level = new IntegerFilter();
        }
        return level;
    }

    public void setLevel(IntegerFilter level) {
        this.level = level;
    }

    public StringFilter getKeywords() {
        return keywords;
    }

    public StringFilter keywords() {
        if (keywords == null) {
            keywords = new StringFilter();
        }
        return keywords;
    }

    public void setKeywords(StringFilter keywords) {
        this.keywords = keywords;
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

    public LongFilter getParentId() {
        return parentId;
    }

    public LongFilter parentId() {
        if (parentId == null) {
            parentId = new LongFilter();
        }
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
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
        final CategoryCriteria that = (CategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(code, that.code) &&
            Objects.equals(hasChild, that.hasChild) &&
            Objects.equals(level, that.level) &&
            Objects.equals(keywords, that.keywords) &&
            Objects.equals(description, that.description) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, code, hasChild, level, keywords, description, parentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (hasChild != null ? "hasChild=" + hasChild + ", " : "") +
            (level != null ? "level=" + level + ", " : "") +
            (keywords != null ? "keywords=" + keywords + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (parentId != null ? "parentId=" + parentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
