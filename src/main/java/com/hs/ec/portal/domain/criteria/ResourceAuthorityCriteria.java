package com.hs.ec.portal.domain.criteria;

import com.hs.ec.portal.domain.enumeration.Verb;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hs.ec.portal.domain.ResourceAuthority} entity. This class is used
 * in {@link com.hs.ec.portal.web.rest.ResourceAuthorityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /resource-authorities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResourceAuthorityCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Verb
     */
    public static class VerbFilter extends Filter<Verb> {

        public VerbFilter() {}

        public VerbFilter(VerbFilter filter) {
            super(filter);
        }

        @Override
        public VerbFilter copy() {
            return new VerbFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private VerbFilter verb;

    private LongFilter resourceId;

    private LongFilter myAuthorityId;

    private Boolean distinct;

    public ResourceAuthorityCriteria() {}

    public ResourceAuthorityCriteria(ResourceAuthorityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.verb = other.verb == null ? null : other.verb.copy();
        this.resourceId = other.resourceId == null ? null : other.resourceId.copy();
        this.myAuthorityId = other.myAuthorityId == null ? null : other.myAuthorityId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ResourceAuthorityCriteria copy() {
        return new ResourceAuthorityCriteria(this);
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

    public VerbFilter getVerb() {
        return verb;
    }

    public VerbFilter verb() {
        if (verb == null) {
            verb = new VerbFilter();
        }
        return verb;
    }

    public void setVerb(VerbFilter verb) {
        this.verb = verb;
    }

    public LongFilter getResourceId() {
        return resourceId;
    }

    public LongFilter resourceId() {
        if (resourceId == null) {
            resourceId = new LongFilter();
        }
        return resourceId;
    }

    public void setResourceId(LongFilter resourceId) {
        this.resourceId = resourceId;
    }

    public LongFilter getMyAuthorityId() {
        return myAuthorityId;
    }

    public LongFilter myAuthorityId() {
        if (myAuthorityId == null) {
            myAuthorityId = new LongFilter();
        }
        return myAuthorityId;
    }

    public void setMyAuthorityId(LongFilter myAuthorityId) {
        this.myAuthorityId = myAuthorityId;
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
        final ResourceAuthorityCriteria that = (ResourceAuthorityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(verb, that.verb) &&
            Objects.equals(resourceId, that.resourceId) &&
            Objects.equals(myAuthorityId, that.myAuthorityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, verb, resourceId, myAuthorityId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceAuthorityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (verb != null ? "verb=" + verb + ", " : "") +
            (resourceId != null ? "resourceId=" + resourceId + ", " : "") +
            (myAuthorityId != null ? "myAuthorityId=" + myAuthorityId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
