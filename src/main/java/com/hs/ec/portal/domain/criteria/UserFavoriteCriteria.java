package com.hs.ec.portal.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hs.ec.portal.domain.UserFavorite} entity. This class is used
 * in {@link com.hs.ec.portal.web.rest.UserFavoriteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-favorites?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserFavoriteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter productId;

    private Boolean distinct;

    public UserFavoriteCriteria() {}

    public UserFavoriteCriteria(UserFavoriteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UserFavoriteCriteria copy() {
        return new UserFavoriteCriteria(this);
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
        final UserFavoriteCriteria that = (UserFavoriteCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(productId, that.productId) && Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserFavoriteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
