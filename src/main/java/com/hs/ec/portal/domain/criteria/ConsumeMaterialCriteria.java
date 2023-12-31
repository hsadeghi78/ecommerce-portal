package com.hs.ec.portal.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hs.ec.portal.domain.ConsumeMaterial} entity. This class is used
 * in {@link com.hs.ec.portal.web.rest.ConsumeMaterialResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /consume-materials?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsumeMaterialCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter typeClassId;

    private StringFilter name;

    private StringFilter value;

    private LongFilter productId;

    private Boolean distinct;

    public ConsumeMaterialCriteria() {}

    public ConsumeMaterialCriteria(ConsumeMaterialCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.typeClassId = other.typeClassId == null ? null : other.typeClassId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ConsumeMaterialCriteria copy() {
        return new ConsumeMaterialCriteria(this);
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

    public LongFilter getTypeClassId() {
        return typeClassId;
    }

    public LongFilter typeClassId() {
        if (typeClassId == null) {
            typeClassId = new LongFilter();
        }
        return typeClassId;
    }

    public void setTypeClassId(LongFilter typeClassId) {
        this.typeClassId = typeClassId;
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

    public StringFilter getValue() {
        return value;
    }

    public StringFilter value() {
        if (value == null) {
            value = new StringFilter();
        }
        return value;
    }

    public void setValue(StringFilter value) {
        this.value = value;
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
        final ConsumeMaterialCriteria that = (ConsumeMaterialCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(typeClassId, that.typeClassId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(value, that.value) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, typeClassId, name, value, productId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsumeMaterialCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (typeClassId != null ? "typeClassId=" + typeClassId + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (value != null ? "value=" + value + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
