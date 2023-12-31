package com.hs.ec.portal.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hs.ec.portal.domain.Factor} entity. This class is used
 * in {@link com.hs.ec.portal.web.rest.FactorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /factors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter factorCode;

    private LongFilter lastStatusClassId;

    private LongFilter paymentStateClassId;

    private LongFilter categoryClassId;

    private DoubleFilter totalPrice;

    private DoubleFilter discount;

    private StringFilter discountCode;

    private DoubleFilter finalTax;

    private DoubleFilter payable;

    private StringFilter description;

    private LongFilter locationId;

    private LongFilter buyerPartyId;

    private LongFilter sellerPartyId;

    private Boolean distinct;

    public FactorCriteria() {}

    public FactorCriteria(FactorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.factorCode = other.factorCode == null ? null : other.factorCode.copy();
        this.lastStatusClassId = other.lastStatusClassId == null ? null : other.lastStatusClassId.copy();
        this.paymentStateClassId = other.paymentStateClassId == null ? null : other.paymentStateClassId.copy();
        this.categoryClassId = other.categoryClassId == null ? null : other.categoryClassId.copy();
        this.totalPrice = other.totalPrice == null ? null : other.totalPrice.copy();
        this.discount = other.discount == null ? null : other.discount.copy();
        this.discountCode = other.discountCode == null ? null : other.discountCode.copy();
        this.finalTax = other.finalTax == null ? null : other.finalTax.copy();
        this.payable = other.payable == null ? null : other.payable.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.buyerPartyId = other.buyerPartyId == null ? null : other.buyerPartyId.copy();
        this.sellerPartyId = other.sellerPartyId == null ? null : other.sellerPartyId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FactorCriteria copy() {
        return new FactorCriteria(this);
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

    public StringFilter getFactorCode() {
        return factorCode;
    }

    public StringFilter factorCode() {
        if (factorCode == null) {
            factorCode = new StringFilter();
        }
        return factorCode;
    }

    public void setFactorCode(StringFilter factorCode) {
        this.factorCode = factorCode;
    }

    public LongFilter getLastStatusClassId() {
        return lastStatusClassId;
    }

    public LongFilter lastStatusClassId() {
        if (lastStatusClassId == null) {
            lastStatusClassId = new LongFilter();
        }
        return lastStatusClassId;
    }

    public void setLastStatusClassId(LongFilter lastStatusClassId) {
        this.lastStatusClassId = lastStatusClassId;
    }

    public LongFilter getPaymentStateClassId() {
        return paymentStateClassId;
    }

    public LongFilter paymentStateClassId() {
        if (paymentStateClassId == null) {
            paymentStateClassId = new LongFilter();
        }
        return paymentStateClassId;
    }

    public void setPaymentStateClassId(LongFilter paymentStateClassId) {
        this.paymentStateClassId = paymentStateClassId;
    }

    public LongFilter getCategoryClassId() {
        return categoryClassId;
    }

    public LongFilter categoryClassId() {
        if (categoryClassId == null) {
            categoryClassId = new LongFilter();
        }
        return categoryClassId;
    }

    public void setCategoryClassId(LongFilter categoryClassId) {
        this.categoryClassId = categoryClassId;
    }

    public DoubleFilter getTotalPrice() {
        return totalPrice;
    }

    public DoubleFilter totalPrice() {
        if (totalPrice == null) {
            totalPrice = new DoubleFilter();
        }
        return totalPrice;
    }

    public void setTotalPrice(DoubleFilter totalPrice) {
        this.totalPrice = totalPrice;
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

    public StringFilter getDiscountCode() {
        return discountCode;
    }

    public StringFilter discountCode() {
        if (discountCode == null) {
            discountCode = new StringFilter();
        }
        return discountCode;
    }

    public void setDiscountCode(StringFilter discountCode) {
        this.discountCode = discountCode;
    }

    public DoubleFilter getFinalTax() {
        return finalTax;
    }

    public DoubleFilter finalTax() {
        if (finalTax == null) {
            finalTax = new DoubleFilter();
        }
        return finalTax;
    }

    public void setFinalTax(DoubleFilter finalTax) {
        this.finalTax = finalTax;
    }

    public DoubleFilter getPayable() {
        return payable;
    }

    public DoubleFilter payable() {
        if (payable == null) {
            payable = new DoubleFilter();
        }
        return payable;
    }

    public void setPayable(DoubleFilter payable) {
        this.payable = payable;
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

    public LongFilter getLocationId() {
        return locationId;
    }

    public LongFilter locationId() {
        if (locationId == null) {
            locationId = new LongFilter();
        }
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public LongFilter getBuyerPartyId() {
        return buyerPartyId;
    }

    public LongFilter buyerPartyId() {
        if (buyerPartyId == null) {
            buyerPartyId = new LongFilter();
        }
        return buyerPartyId;
    }

    public void setBuyerPartyId(LongFilter buyerPartyId) {
        this.buyerPartyId = buyerPartyId;
    }

    public LongFilter getSellerPartyId() {
        return sellerPartyId;
    }

    public LongFilter sellerPartyId() {
        if (sellerPartyId == null) {
            sellerPartyId = new LongFilter();
        }
        return sellerPartyId;
    }

    public void setSellerPartyId(LongFilter sellerPartyId) {
        this.sellerPartyId = sellerPartyId;
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
        final FactorCriteria that = (FactorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(factorCode, that.factorCode) &&
            Objects.equals(lastStatusClassId, that.lastStatusClassId) &&
            Objects.equals(paymentStateClassId, that.paymentStateClassId) &&
            Objects.equals(categoryClassId, that.categoryClassId) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(discount, that.discount) &&
            Objects.equals(discountCode, that.discountCode) &&
            Objects.equals(finalTax, that.finalTax) &&
            Objects.equals(payable, that.payable) &&
            Objects.equals(description, that.description) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(buyerPartyId, that.buyerPartyId) &&
            Objects.equals(sellerPartyId, that.sellerPartyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            factorCode,
            lastStatusClassId,
            paymentStateClassId,
            categoryClassId,
            totalPrice,
            discount,
            discountCode,
            finalTax,
            payable,
            description,
            locationId,
            buyerPartyId,
            sellerPartyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (factorCode != null ? "factorCode=" + factorCode + ", " : "") +
            (lastStatusClassId != null ? "lastStatusClassId=" + lastStatusClassId + ", " : "") +
            (paymentStateClassId != null ? "paymentStateClassId=" + paymentStateClassId + ", " : "") +
            (categoryClassId != null ? "categoryClassId=" + categoryClassId + ", " : "") +
            (totalPrice != null ? "totalPrice=" + totalPrice + ", " : "") +
            (discount != null ? "discount=" + discount + ", " : "") +
            (discountCode != null ? "discountCode=" + discountCode + ", " : "") +
            (finalTax != null ? "finalTax=" + finalTax + ", " : "") +
            (payable != null ? "payable=" + payable + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (locationId != null ? "locationId=" + locationId + ", " : "") +
            (buyerPartyId != null ? "buyerPartyId=" + buyerPartyId + ", " : "") +
            (sellerPartyId != null ? "sellerPartyId=" + sellerPartyId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
