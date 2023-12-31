package com.hs.ec.portal.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 4 field fixed
 * @author Hossein Sadeghi (hsadeghi78@gmail.com)
 */
@Table("wallet")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Wallet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    /**
     * TransactionTypeClass for ONLINE_RECHARGE, FACTOR_SETTLEMENT, SUBSCRIPTION_FEE
     */
    @Column("trans_type_class_id")
    private Long transTypeClassId;

    @NotNull(message = "must not be null")
    @Column("stock")
    private Double stock;

    @Size(max = 1000)
    @Column("description")
    private String description;

    @Column("deposit")
    private Double deposit;

    @Column("withdrawal")
    private Double withdrawal;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Wallet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransTypeClassId() {
        return this.transTypeClassId;
    }

    public Wallet transTypeClassId(Long transTypeClassId) {
        this.setTransTypeClassId(transTypeClassId);
        return this;
    }

    public void setTransTypeClassId(Long transTypeClassId) {
        this.transTypeClassId = transTypeClassId;
    }

    public Double getStock() {
        return this.stock;
    }

    public Wallet stock(Double stock) {
        this.setStock(stock);
        return this;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return this.description;
    }

    public Wallet description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDeposit() {
        return this.deposit;
    }

    public Wallet deposit(Double deposit) {
        this.setDeposit(deposit);
        return this;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public Double getWithdrawal() {
        return this.withdrawal;
    }

    public Wallet withdrawal(Double withdrawal) {
        this.setWithdrawal(withdrawal);
        return this;
    }

    public void setWithdrawal(Double withdrawal) {
        this.withdrawal = withdrawal;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Wallet)) {
            return false;
        }
        return getId() != null && getId().equals(((Wallet) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Wallet{" +
            "id=" + getId() +
            ", transTypeClassId=" + getTransTypeClassId() +
            ", stock=" + getStock() +
            ", description='" + getDescription() + "'" +
            ", deposit=" + getDeposit() +
            ", withdrawal=" + getWithdrawal() +
            "}";
    }
}
