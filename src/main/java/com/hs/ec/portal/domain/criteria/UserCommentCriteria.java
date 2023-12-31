package com.hs.ec.portal.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hs.ec.portal.domain.UserComment} entity. This class is used
 * in {@link com.hs.ec.portal.web.rest.UserCommentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-comments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserCommentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter rating;

    private BooleanFilter visible;

    private StringFilter description;

    private LongFilter partyId;

    private LongFilter productId;

    private LongFilter factorId;

    private LongFilter parentId;

    private Boolean distinct;

    public UserCommentCriteria() {}

    public UserCommentCriteria(UserCommentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.rating = other.rating == null ? null : other.rating.copy();
        this.visible = other.visible == null ? null : other.visible.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.partyId = other.partyId == null ? null : other.partyId.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.factorId = other.factorId == null ? null : other.factorId.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UserCommentCriteria copy() {
        return new UserCommentCriteria(this);
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

    public FloatFilter getRating() {
        return rating;
    }

    public FloatFilter rating() {
        if (rating == null) {
            rating = new FloatFilter();
        }
        return rating;
    }

    public void setRating(FloatFilter rating) {
        this.rating = rating;
    }

    public BooleanFilter getVisible() {
        return visible;
    }

    public BooleanFilter visible() {
        if (visible == null) {
            visible = new BooleanFilter();
        }
        return visible;
    }

    public void setVisible(BooleanFilter visible) {
        this.visible = visible;
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

    public LongFilter getPartyId() {
        return partyId;
    }

    public LongFilter partyId() {
        if (partyId == null) {
            partyId = new LongFilter();
        }
        return partyId;
    }

    public void setPartyId(LongFilter partyId) {
        this.partyId = partyId;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public LongFilter productId() {
        if (productId == null) {
            productId = new LongFilter();
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public LongFilter getFactorId() {
        return factorId;
    }

    public LongFilter factorId() {
        if (factorId == null) {
            factorId = new LongFilter();
        }
        return factorId;
    }

    public void setFactorId(LongFilter factorId) {
        this.factorId = factorId;
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
        final UserCommentCriteria that = (UserCommentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(visible, that.visible) &&
            Objects.equals(description, that.description) &&
            Objects.equals(partyId, that.partyId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(factorId, that.factorId) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rating, visible, description, partyId, productId, factorId, parentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserCommentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (rating != null ? "rating=" + rating + ", " : "") +
            (visible != null ? "visible=" + visible + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (partyId != null ? "partyId=" + partyId + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (factorId != null ? "factorId=" + factorId + ", " : "") +
            (parentId != null ? "parentId=" + parentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
