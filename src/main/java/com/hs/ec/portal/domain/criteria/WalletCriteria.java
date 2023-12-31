package com.hs.ec.portal.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hs.ec.portal.domain.Wallet} entity. This class is used
 * in {@link com.hs.ec.portal.web.rest.WalletResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /wallets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WalletCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter transTypeClassId;

    private DoubleFilter stock;

    private StringFilter description;

    private DoubleFilter deposit;

    private DoubleFilter withdrawal;

    private Boolean distinct;

    public WalletCriteria() {}

    public WalletCriteria(WalletCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.transTypeClassId = other.transTypeClassId == null ? null : other.transTypeClassId.copy();
        this.stock = other.stock == null ? null : other.stock.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.deposit = other.deposit == null ? null : other.deposit.copy();
        this.withdrawal = other.withdrawal == null ? null : other.withdrawal.copy();
        this.distinct = other.distinct;
    }

    @Override
    public WalletCriteria copy() {
        return new WalletCriteria(this);
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

    public LongFilter getTransTypeClassId() {
        return transTypeClassId;
    }

    public LongFilter transTypeClassId() {
        if (transTypeClassId == null) {
            transTypeClassId = new LongFilter();
        }
        return transTypeClassId;
    }

    public void setTransTypeClassId(LongFilter transTypeClassId) {
        this.transTypeClassId = transTypeClassId;
    }

    public DoubleFilter getStock() {
        return stock;
    }

    public DoubleFilter stock() {
        if (stock == null) {
            stock = new DoubleFilter();
        }
        return stock;
    }

    public void setStock(DoubleFilter stock) {
        this.stock = stock;
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

    public DoubleFilter getDeposit() {
        return deposit;
    }

    public DoubleFilter deposit() {
        if (deposit == null) {
            deposit = new DoubleFilter();
        }
        return deposit;
    }

    public void setDeposit(DoubleFilter deposit) {
        this.deposit = deposit;
    }

    public DoubleFilter getWithdrawal() {
        return withdrawal;
    }

    public DoubleFilter withdrawal() {
        if (withdrawal == null) {
            withdrawal = new DoubleFilter();
        }
        return withdrawal;
    }

    public void setWithdrawal(DoubleFilter withdrawal) {
        this.withdrawal = withdrawal;
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
        final WalletCriteria that = (WalletCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(transTypeClassId, that.transTypeClassId) &&
            Objects.equals(stock, that.stock) &&
            Objects.equals(description, that.description) &&
            Objects.equals(deposit, that.deposit) &&
            Objects.equals(withdrawal, that.withdrawal) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transTypeClassId, stock, description, deposit, withdrawal, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WalletCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (transTypeClassId != null ? "transTypeClassId=" + transTypeClassId + ", " : "") +
            (stock != null ? "stock=" + stock + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (deposit != null ? "deposit=" + deposit + ", " : "") +
            (withdrawal != null ? "withdrawal=" + withdrawal + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
