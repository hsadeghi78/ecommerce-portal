package com.hs.ec.portal.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hs.ec.portal.domain.FactorItem} entity. This class is used
 * in {@link com.hs.ec.portal.web.rest.FactorItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /factor-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactorItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter rowNum;

    private StringFilter title;

    private IntegerFilter count;

    private DoubleFilter discount;

    private DoubleFilter tax;

    private StringFilter description;

    private LongFilter factorId;

    private LongFilter productId;

    private Boolean distinct;

    public FactorItemCriteria() {}

    public FactorItemCriteria(FactorItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.rowNum = other.rowNum == null ? null : other.rowNum.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.count = other.count == null ? null : other.count.copy();
        this.discount = other.discount == null ? null : other.discount.copy();
        this.tax = other.tax == null ? null : other.tax.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.factorId = other.factorId == null ? null : other.factorId.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FactorItemCriteria copy() {
        return new FactorItemCriteria(this);
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

    public IntegerFilter getRowNum() {
        return rowNum;
    }

    public IntegerFilter rowNum() {
        if (rowNum == null) {
            rowNum = new IntegerFilter();
        }
        return rowNum;
    }

    public void setRowNum(IntegerFilter rowNum) {
        this.rowNum = rowNum;
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

    public IntegerFilter getCount() {
        return count;
    }

    public IntegerFilter count() {
        if (count == null) {
            count = new IntegerFilter();
        }
        return count;
    }

    public void setCount(IntegerFilter count) {
        this.count = count;
    }

    public DoubleFilter getDiscount() {
        return discount;
    }

    public DoubleFilter discount() {
        if (discount == null) {
            discount = new DoubleFilter();
        }
        return discount;
    }

    public void setDiscount(DoubleFilter discount) {
        this.discount = discount;
    }

    public DoubleFilter getTax() {
        return tax;
    }

    public DoubleFilter tax() {
        if (tax == null) {
            tax = new DoubleFilter();
        }
        return tax;
    }

    public void setTax(DoubleFilter tax) {
        this.tax = tax;
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
        final FactorItemCriteria that = (FactorItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(rowNum, that.rowNum) &&
            Objects.equals(title, that.title) &&
            Objects.equals(count, that.count) &&
            Objects.equals(discount, that.discount) &&
            Objects.equals(tax, that.tax) &&
            Objects.equals(description, that.description) &&
            Objects.equals(factorId, that.factorId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rowNum, title, count, discount, tax, description, factorId, productId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactorItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (rowNum != null ? "rowNum=" + rowNum + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (count != null ? "count=" + count + ", " : "") +
            (discount != null ? "discount=" + discount + ", " : "") +
            (tax != null ? "tax=" + tax + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (factorId != null ? "factorId=" + factorId + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
