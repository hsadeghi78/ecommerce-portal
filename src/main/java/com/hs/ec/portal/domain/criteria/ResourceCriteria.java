package com.hs.ec.portal.domain.criteria;

import com.hs.ec.portal.domain.enumeration.ResourceType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hs.ec.portal.domain.Resource} entity. This class is used
 * in {@link com.hs.ec.portal.web.rest.ResourceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /resources?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResourceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ResourceType
     */
    public static class ResourceTypeFilter extends Filter<ResourceType> {

        public ResourceTypeFilter() {}

        public ResourceTypeFilter(ResourceTypeFilter filter) {
            super(filter);
        }

        @Override
        public ResourceTypeFilter copy() {
            return new ResourceTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter displayName;

    private StringFilter apiUri;

    private ResourceTypeFilter resourceType;

    private Boolean distinct;

    public ResourceCriteria() {}

    public ResourceCriteria(ResourceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.displayName = other.displayName == null ? null : other.displayName.copy();
        this.apiUri = other.apiUri == null ? null : other.apiUri.copy();
        this.resourceType = other.resourceType == null ? null : other.resourceType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ResourceCriteria copy() {
        return new ResourceCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDisplayName() {
        return displayName;
    }

    public StringFilter displayName() {
        if (displayName == null) {
            displayName = new StringFilter();
        }
        return displayName;
    }

    public void setDisplayName(StringFilter displayName) {
        this.displayName = displayName;
    }

    public StringFilter getApiUri() {
        return apiUri;
    }

    public StringFilter apiUri() {
        if (apiUri == null) {
            apiUri = new StringFilter();
        }
        return apiUri;
    }

    public void setApiUri(StringFilter apiUri) {
        this.apiUri = apiUri;
    }

    public ResourceTypeFilter getResourceType() {
        return resourceType;
    }

    public ResourceTypeFilter resourceType() {
        if (resourceType == null) {
            resourceType = new ResourceTypeFilter();
        }
        return resourceType;
    }

    public void setResourceType(ResourceTypeFilter resourceType) {
        this.resourceType = resourceType;
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
        final ResourceCriteria that = (ResourceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(displayName, that.displayName) &&
            Objects.equals(apiUri, that.apiUri) &&
            Objects.equals(resourceType, that.resourceType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, displayName, apiUri, resourceType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (displayName != null ? "displayName=" + displayName + ", " : "") +
            (apiUri != null ? "apiUri=" + apiUri + ", " : "") +
            (resourceType != null ? "resourceType=" + resourceType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
