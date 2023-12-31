package com.hs.ec.portal.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hs.ec.portal.domain.Wallet} entity.
 */
@Schema(description = "4 field fixed\n@author Hossein Sadeghi (hsadeghi78@gmail.com)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WalletDTO implements Serializable {

    private Long id;

    /**
     * TransactionTypeClass for ONLINE_RECHARGE, FACTOR_SETTLEMENT, SUBSCRIPTION_FEE
     */
    @Schema(description = "TransactionTypeClass for ONLINE_RECHARGE, FACTOR_SETTLEMENT, SUBSCRIPTION_FEE")
    private Long transTypeClassId;

    @NotNull(message = "must not be null")
    private Double stock;

    @Size(max = 1000)
    private String description;

    private Double deposit;

    private Double withdrawal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransTypeClassId() {
        return transTypeClassId;
    }

    public void setTransTypeClassId(Long transTypeClassId) {
        this.transTypeClassId = transTypeClassId;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public Double getWithdrawal() {
        return withdrawal;
    }

    public void setWithdrawal(Double withdrawal) {
        this.withdrawal = withdrawal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WalletDTO)) {
            return false;
        }

        WalletDTO walletDTO = (WalletDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, walletDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WalletDTO{" +
            "id=" + getId() +
            ", transTypeClassId=" + getTransTypeClassId() +
            ", stock=" + getStock() +
            ", description='" + getDescription() + "'" +
            ", deposit=" + getDeposit() +
            ", withdrawal=" + getWithdrawal() +
            "}";
    }
}
