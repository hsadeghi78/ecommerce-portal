package com.hs.ec.portal.domain.criteria;

import com.hs.ec.portal.domain.enumeration.Performance;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hs.ec.portal.domain.Product} entity. This class is used
 * in {@link com.hs.ec.portal.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Performance
     */
    public static class PerformanceFilter extends Filter<Performance> {

        public PerformanceFilter() {}

        public PerformanceFilter(PerformanceFilter filter) {
            super(filter);
        }

        @Override
        public PerformanceFilter copy() {
            return new PerformanceFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter typeClassId;

    private LongFilter brandClassId;

    private StringFilter sizee;

    private LongFilter regularSizeClassId;

    private LongFilter languageClassId;

    private StringFilter description;

    private StringFilter keywords;

    private LongFilter nationalityClassId;

    private DoubleFilter count;

    private FloatFilter discount;

    private DoubleFilter originalPrice;

    private DoubleFilter finalPrice;

    private LocalDateFilter publishDate;

    private LocalDateFilter transportDate;

    private LongFilter currencyClassId;

    private FloatFilter bonus;

    private LongFilter warrantyClassId;

    private LongFilter deliveryPlaceClassId;

    private LongFilter paymentPlaceClassId;

    private PerformanceFilter performance;

    private LongFilter originalityClassId;

    private FloatFilter satisfaction;

    private BooleanFilter used;

    private LongFilter categoryId;

    private LongFilter partyId;

    private LongFilter parentId;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.typeClassId = other.typeClassId == null ? null : other.typeClassId.copy();
        this.brandClassId = other.brandClassId == null ? null : other.brandClassId.copy();
        this.sizee = other.sizee == null ? null : other.sizee.copy();
        this.regularSizeClassId = other.regularSizeClassId == null ? null : other.regularSizeClassId.copy();
        this.languageClassId = other.languageClassId == null ? null : other.languageClassId.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.keywords = other.keywords == null ? null : other.keywords.copy();
        this.nationalityClassId = other.nationalityClassId == null ? null : other.nationalityClassId.copy();
        this.count = other.count == null ? null : other.count.copy();
        this.discount = other.discount == null ? null : other.discount.copy();
        this.originalPrice = other.originalPrice == null ? null : other.originalPrice.copy();
        this.finalPrice = other.finalPrice == null ? null : other.finalPrice.copy();
        this.publishDate = other.publishDate == null ? null : other.publishDate.copy();
        this.transportDate = other.transportDate == null ? null : other.transportDate.copy();
        this.currencyClassId = other.currencyClassId == null ? null : other.currencyClassId.copy();
        this.bonus = other.bonus == null ? null : other.bonus.copy();
        this.warrantyClassId = other.warrantyClassId == null ? null : other.warrantyClassId.copy();
        this.deliveryPlaceClassId = other.deliveryPlaceClassId == null ? null : other.deliveryPlaceClassId.copy();
        this.paymentPlaceClassId = other.paymentPlaceClassId == null ? null : other.paymentPlaceClassId.copy();
        this.performance = other.performance == null ? null : other.performance.copy();
        this.originalityClassId = other.originalityClassId == null ? null : other.originalityClassId.copy();
        this.satisfaction = other.satisfaction == null ? null : other.satisfaction.copy();
        this.used = other.used == null ? null : other.used.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.partyId = other.partyId == null ? null : other.partyId.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
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

    public LongFilter getBrandClassId() {
        return brandClassId;
    }

    public LongFilter brandClassId() {
        if (brandClassId == null) {
            brandClassId = new LongFilter();
        }
        return brandClassId;
    }

    public void setBrandClassId(LongFilter brandClassId) {
        this.brandClassId = brandClassId;
    }

    public StringFilter getSizee() {
        return sizee;
    }

    public StringFilter sizee() {
        if (sizee == null) {
            sizee = new StringFilter();
        }
        return sizee;
    }

    public void setSizee(StringFilter sizee) {
        this.sizee = sizee;
    }

    public LongFilter getRegularSizeClassId() {
        return regularSizeClassId;
    }

    public LongFilter regularSizeClassId() {
        if (regularSizeClassId == null) {
            regularSizeClassId = new LongFilter();
        }
        return regularSizeClassId;
    }

    public void setRegularSizeClassId(LongFilter regularSizeClassId) {
        this.regularSizeClassId = regularSizeClassId;
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

    public LongFilter getNationalityClassId() {
        return nationalityClassId;
    }

    public LongFilter nationalityClassId() {
        if (nationalityClassId == null) {
            nationalityClassId = new LongFilter();
        }
        return nationalityClassId;
    }

    public void setNationalityClassId(LongFilter nationalityClassId) {
        this.nationalityClassId = nationalityClassId;
    }

    public DoubleFilter getCount() {
        return count;
    }

    public DoubleFilter count() {
        if (count == null) {
            count = new DoubleFilter();
        }
        return count;
    }

    public void setCount(DoubleFilter count) {
        this.count = count;
    }

    public FloatFilter getDiscount() {
        return discount;
    }

    public FloatFilter discount() {
        if (discount == null) {
            discount = new FloatFilter();
        }
        return discount;
    }

    public void setDiscount(FloatFilter discount) {
        this.discount = discount;
    }

    public DoubleFilter getOriginalPrice() {
        return originalPrice;
    }

    public DoubleFilter originalPrice() {
        if (originalPrice == null) {
            originalPrice = new DoubleFilter();
        }
        return originalPrice;
    }

    public void setOriginalPrice(DoubleFilter originalPrice) {
        this.originalPrice = originalPrice;
    }

    public DoubleFilter getFinalPrice() {
        return finalPrice;
    }

    public DoubleFilter finalPrice() {
        if (finalPrice == null) {
            finalPrice = new DoubleFilter();
        }
        return finalPrice;
    }

    public void setFinalPrice(DoubleFilter finalPrice) {
        this.finalPrice = finalPrice;
    }

    public LocalDateFilter getPublishDate() {
        return publishDate;
    }

    public LocalDateFilter publishDate() {
        if (publishDate == null) {
            publishDate = new LocalDateFilter();
        }
        return publishDate;
    }

    public void setPublishDate(LocalDateFilter publishDate) {
        this.publishDate = publishDate;
    }

    public LocalDateFilter getTransportDate() {
        return transportDate;
    }

    public LocalDateFilter transportDate() {
        if (transportDate == null) {
            transportDate = new LocalDateFilter();
        }
        return transportDate;
    }

    public void setTransportDate(LocalDateFilter transportDate) {
        this.transportDate = transportDate;
    }

    public LongFilter getCurrencyClassId() {
        return currencyClassId;
    }

    public LongFilter currencyClassId() {
        if (currencyClassId == null) {
            currencyClassId = new LongFilter();
        }
        return currencyClassId;
    }

    public void setCurrencyClassId(LongFilter currencyClassId) {
        this.currencyClassId = currencyClassId;
    }

    public FloatFilter getBonus() {
        return bonus;
    }

    public FloatFilter bonus() {
        if (bonus == null) {
            bonus = new FloatFilter();
        }
        return bonus;
    }

    public void setBonus(FloatFilter bonus) {
        this.bonus = bonus;
    }

    public LongFilter getWarrantyClassId() {
        return warrantyClassId;
    }

    public LongFilter warrantyClassId() {
        if (warrantyClassId == null) {
            warrantyClassId = new LongFilter();
        }
        return warrantyClassId;
    }

    public void setWarrantyClassId(LongFilter warrantyClassId) {
        this.warrantyClassId = warrantyClassId;
    }

    public LongFilter getDeliveryPlaceClassId() {
        return deliveryPlaceClassId;
    }

    public LongFilter deliveryPlaceClassId() {
        if (deliveryPlaceClassId == null) {
            deliveryPlaceClassId = new LongFilter();
        }
        return deliveryPlaceClassId;
    }

    public void setDeliveryPlaceClassId(LongFilter deliveryPlaceClassId) {
        this.deliveryPlaceClassId = deliveryPlaceClassId;
    }

    public LongFilter getPaymentPlaceClassId() {
        return paymentPlaceClassId;
    }

    public LongFilter paymentPlaceClassId() {
        if (paymentPlaceClassId == null) {
            paymentPlaceClassId = new LongFilter();
        }
        return paymentPlaceClassId;
    }

    public void setPaymentPlaceClassId(LongFilter paymentPlaceClassId) {
        this.paymentPlaceClassId = paymentPlaceClassId;
    }

    public PerformanceFilter getPerformance() {
        return performance;
    }

    public PerformanceFilter performance() {
        if (performance == null) {
            performance = new PerformanceFilter();
        }
        return performance;
    }

    public void setPerformance(PerformanceFilter performance) {
        this.performance = performance;
    }

    public LongFilter getOriginalityClassId() {
        return originalityClassId;
    }

    public LongFilter originalityClassId() {
        if (originalityClassId == null) {
            originalityClassId = new LongFilter();
        }
        return originalityClassId;
    }

    public void setOriginalityClassId(LongFilter originalityClassId) {
        this.originalityClassId = originalityClassId;
    }

    public FloatFilter getSatisfaction() {
        return satisfaction;
    }

    public FloatFilter satisfaction() {
        if (satisfaction == null) {
            satisfaction = new FloatFilter();
        }
        return satisfaction;
    }

    public void setSatisfaction(FloatFilter satisfaction) {
        this.satisfaction = satisfaction;
    }

    public BooleanFilter getUsed() {
        return used;
    }

    public BooleanFilter used() {
        if (used == null) {
            used = new BooleanFilter();
        }
        return used;
    }

    public void setUsed(BooleanFilter used) {
        this.used = used;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            categoryId = new LongFilter();
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
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
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(typeClassId, that.typeClassId) &&
            Objects.equals(brandClassId, that.brandClassId) &&
            Objects.equals(sizee, that.sizee) &&
            Objects.equals(regularSizeClassId, that.regularSizeClassId) &&
            Objects.equals(languageClassId, that.languageClassId) &&
            Objects.equals(description, that.description) &&
            Objects.equals(keywords, that.keywords) &&
            Objects.equals(nationalityClassId, that.nationalityClassId) &&
            Objects.equals(count, that.count) &&
            Objects.equals(discount, that.discount) &&
            Objects.equals(originalPrice, that.originalPrice) &&
            Objects.equals(finalPrice, that.finalPrice) &&
            Objects.equals(publishDate, that.publishDate) &&
            Objects.equals(transportDate, that.transportDate) &&
            Objects.equals(currencyClassId, that.currencyClassId) &&
            Objects.equals(bonus, that.bonus) &&
            Objects.equals(warrantyClassId, that.warrantyClassId) &&
            Objects.equals(deliveryPlaceClassId, that.deliveryPlaceClassId) &&
            Objects.equals(paymentPlaceClassId, that.paymentPlaceClassId) &&
            Objects.equals(performance, that.performance) &&
            Objects.equals(originalityClassId, that.originalityClassId) &&
            Objects.equals(satisfaction, that.satisfaction) &&
            Objects.equals(used, that.used) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(partyId, that.partyId) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            typeClassId,
            brandClassId,
            sizee,
            regularSizeClassId,
            languageClassId,
            description,
            keywords,
            nationalityClassId,
            count,
            discount,
            originalPrice,
            finalPrice,
            publishDate,
            transportDate,
            currencyClassId,
            bonus,
            warrantyClassId,
            deliveryPlaceClassId,
            paymentPlaceClassId,
            performance,
            originalityClassId,
            satisfaction,
            used,
            categoryId,
            partyId,
            parentId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (typeClassId != null ? "typeClassId=" + typeClassId + ", " : "") +
            (brandClassId != null ? "brandClassId=" + brandClassId + ", " : "") +
            (sizee != null ? "sizee=" + sizee + ", " : "") +
            (regularSizeClassId != null ? "regularSizeClassId=" + regularSizeClassId + ", " : "") +
            (languageClassId != null ? "languageClassId=" + languageClassId + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (keywords != null ? "keywords=" + keywords + ", " : "") +
            (nationalityClassId != null ? "nationalityClassId=" + nationalityClassId + ", " : "") +
            (count != null ? "count=" + count + ", " : "") +
            (discount != null ? "discount=" + discount + ", " : "") +
            (originalPrice != null ? "originalPrice=" + originalPrice + ", " : "") +
            (finalPrice != null ? "finalPrice=" + finalPrice + ", " : "") +
            (publishDate != null ? "publishDate=" + publishDate + ", " : "") +
            (transportDate != null ? "transportDate=" + transportDate + ", " : "") +
            (currencyClassId != null ? "currencyClassId=" + currencyClassId + ", " : "") +
            (bonus != null ? "bonus=" + bonus + ", " : "") +
            (warrantyClassId != null ? "warrantyClassId=" + warrantyClassId + ", " : "") +
            (deliveryPlaceClassId != null ? "deliveryPlaceClassId=" + deliveryPlaceClassId + ", " : "") +
            (paymentPlaceClassId != null ? "paymentPlaceClassId=" + paymentPlaceClassId + ", " : "") +
            (performance != null ? "performance=" + performance + ", " : "") +
            (originalityClassId != null ? "originalityClassId=" + originalityClassId + ", " : "") +
            (satisfaction != null ? "satisfaction=" + satisfaction + ", " : "") +
            (used != null ? "used=" + used + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            (partyId != null ? "partyId=" + partyId + ", " : "") +
            (parentId != null ? "parentId=" + parentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
