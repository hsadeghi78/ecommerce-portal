package com.hs.ec.portal.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hs.ec.portal.domain.Party} entity. This class is used
 * in {@link com.hs.ec.portal.web.rest.PartyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /parties?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PartyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter partyCode;

    private StringFilter tradeTitle;

    private LocalDateFilter activationDate;

    private LocalDateFilter expirationDate;

    private BooleanFilter activationStatus;

    private BooleanFilter personType;

    private Boolean distinct;

    public PartyCriteria() {}

    public PartyCriteria(PartyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.partyCode = other.partyCode == null ? null : other.partyCode.copy();
        this.tradeTitle = other.tradeTitle == null ? null : other.tradeTitle.copy();
        this.activationDate = other.activationDate == null ? null : other.activationDate.copy();
        this.expirationDate = other.expirationDate == null ? null : other.expirationDate.copy();
        this.activationStatus = other.activationStatus == null ? null : other.activationStatus.copy();
        this.personType = other.personType == null ? null : other.personType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PartyCriteria copy() {
        return new PartyCriteria(this);
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

    public StringFilter getPartyCode() {
        return partyCode;
    }

    public StringFilter partyCode() {
        if (partyCode == null) {
            partyCode = new StringFilter();
        }
        return partyCode;
    }

    public void setPartyCode(StringFilter partyCode) {
        this.partyCode = partyCode;
    }

    public StringFilter getTradeTitle() {
        return tradeTitle;
    }

    public StringFilter tradeTitle() {
        if (tradeTitle == null) {
            tradeTitle = new StringFilter();
        }
        return tradeTitle;
    }

    public void setTradeTitle(StringFilter tradeTitle) {
        this.tradeTitle = tradeTitle;
    }

    public LocalDateFilter getActivationDate() {
        return activationDate;
    }

    public LocalDateFilter activationDate() {
        if (activationDate == null) {
            activationDate = new LocalDateFilter();
        }
        return activationDate;
    }

    public void setActivationDate(LocalDateFilter activationDate) {
        this.activationDate = activationDate;
    }

    public LocalDateFilter getExpirationDate() {
        return expirationDate;
    }

    public LocalDateFilter expirationDate() {
        if (expirationDate == null) {
            expirationDate = new LocalDateFilter();
        }
        return expirationDate;
    }

    public void setExpirationDate(LocalDateFilter expirationDate) {
        this.expirationDate = expirationDate;
    }

    public BooleanFilter getActivationStatus() {
        return activationStatus;
    }

    public BooleanFilter activationStatus() {
        if (activationStatus == null) {
            activationStatus = new BooleanFilter();
        }
        return activationStatus;
    }

    public void setActivationStatus(BooleanFilter activationStatus) {
        this.activationStatus = activationStatus;
    }

    public BooleanFilter getPersonType() {
        return personType;
    }

    public BooleanFilter personType() {
        if (personType == null) {
            personType = new BooleanFilter();
        }
        return personType;
    }

    public void setPersonType(BooleanFilter personType) {
        this.personType = personType;
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
        final PartyCriteria that = (PartyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(partyCode, that.partyCode) &&
            Objects.equals(tradeTitle, that.tradeTitle) &&
            Objects.equals(activationDate, that.activationDate) &&
            Objects.equals(expirationDate, that.expirationDate) &&
            Objects.equals(activationStatus, that.activationStatus) &&
            Objects.equals(personType, that.personType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, partyCode, tradeTitle, activationDate, expirationDate, activationStatus, personType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (partyCode != null ? "partyCode=" + partyCode + ", " : "") +
            (tradeTitle != null ? "tradeTitle=" + tradeTitle + ", " : "") +
            (activationDate != null ? "activationDate=" + activationDate + ", " : "") +
            (expirationDate != null ? "expirationDate=" + expirationDate + ", " : "") +
            (activationStatus != null ? "activationStatus=" + activationStatus + ", " : "") +
            (personType != null ? "personType=" + personType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
