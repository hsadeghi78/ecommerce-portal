package com.hs.ec.portal.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hs.ec.portal.domain.Agreement} entity. This class is used
 * in {@link com.hs.ec.portal.web.rest.AgreementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /agreements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgreementCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private LongFilter activationStatusClassId;

    private DoubleFilter infrastructureBenefit;

    private DoubleFilter extraBenefit;

    private LongFilter providerId;

    private LongFilter consumerId;

    private Boolean distinct;

    public AgreementCriteria() {}

    public AgreementCriteria(AgreementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.activationStatusClassId = other.activationStatusClassId == null ? null : other.activationStatusClassId.copy();
        this.infrastructureBenefit = other.infrastructureBenefit == null ? null : other.infrastructureBenefit.copy();
        this.extraBenefit = other.extraBenefit == null ? null : other.extraBenefit.copy();
        this.providerId = other.providerId == null ? null : other.providerId.copy();
        this.consumerId = other.consumerId == null ? null : other.consumerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AgreementCriteria copy() {
        return new AgreementCriteria(this);
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

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            startDate = new LocalDateFilter();
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            endDate = new LocalDateFilter();
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public LongFilter getActivationStatusClassId() {
        return activationStatusClassId;
    }

    public LongFilter activationStatusClassId() {
        if (activationStatusClassId == null) {
            activationStatusClassId = new LongFilter();
        }
        return activationStatusClassId;
    }

    public void setActivationStatusClassId(LongFilter activationStatusClassId) {
        this.activationStatusClassId = activationStatusClassId;
    }

    public DoubleFilter getInfrastructureBenefit() {
        return infrastructureBenefit;
    }

    public DoubleFilter infrastructureBenefit() {
        if (infrastructureBenefit == null) {
            infrastructureBenefit = new DoubleFilter();
        }
        return infrastructureBenefit;
    }

    public void setInfrastructureBenefit(DoubleFilter infrastructureBenefit) {
        this.infrastructureBenefit = infrastructureBenefit;
    }

    public DoubleFilter getExtraBenefit() {
        return extraBenefit;
    }

    public DoubleFilter extraBenefit() {
        if (extraBenefit == null) {
            extraBenefit = new DoubleFilter();
        }
        return extraBenefit;
    }

    public void setExtraBenefit(DoubleFilter extraBenefit) {
        this.extraBenefit = extraBenefit;
    }

    public LongFilter getProviderId() {
        return providerId;
    }

    public LongFilter providerId() {
        if (providerId == null) {
            providerId = new LongFilter();
        }
        return providerId;
    }

    public void setProviderId(LongFilter providerId) {
        this.providerId = providerId;
    }

    public LongFilter getConsumerId() {
        return consumerId;
    }

    public LongFilter consumerId() {
        if (consumerId == null) {
            consumerId = new LongFilter();
        }
        return consumerId;
    }

    public void setConsumerId(LongFilter consumerId) {
        this.consumerId = consumerId;
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
        final AgreementCriteria that = (AgreementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(activationStatusClassId, that.activationStatusClassId) &&
            Objects.equals(infrastructureBenefit, that.infrastructureBenefit) &&
            Objects.equals(extraBenefit, that.extraBenefit) &&
            Objects.equals(providerId, that.providerId) &&
            Objects.equals(consumerId, that.consumerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            startDate,
            endDate,
            activationStatusClassId,
            infrastructureBenefit,
            extraBenefit,
            providerId,
            consumerId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgreementCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (activationStatusClassId != null ? "activationStatusClassId=" + activationStatusClassId + ", " : "") +
            (infrastructureBenefit != null ? "infrastructureBenefit=" + infrastructureBenefit + ", " : "") +
            (extraBenefit != null ? "extraBenefit=" + extraBenefit + ", " : "") +
            (providerId != null ? "providerId=" + providerId + ", " : "") +
            (consumerId != null ? "consumerId=" + consumerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
