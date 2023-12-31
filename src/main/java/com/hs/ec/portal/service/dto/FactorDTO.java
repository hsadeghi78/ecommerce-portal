package com.hs.ec.portal.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hs.ec.portal.domain.Factor} entity.
 */
@Schema(description = "4 field fixed\n@author Hossein Sadeghi (hsadeghi78@gmail.com)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactorDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 200)
    private String title;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    private String factorCode;

    /**
     * FactorStatus{PURCHASE_ORDER, INITIATE, SUSPEND, UPDATED, FINAL, PREPARING, READY_TO_DELIVERY, DELIVERY_SUCCESS, DELIVERY_FAIL, CANCEL, DELETED, INDOOR, OUTDOOR}
     */
    @NotNull(message = "must not be null")
    @Schema(
        description = "FactorStatus{PURCHASE_ORDER, INITIATE, SUSPEND, UPDATED, FINAL, PREPARING, READY_TO_DELIVERY, DELIVERY_SUCCESS, DELIVERY_FAIL, CANCEL, DELETED, INDOOR, OUTDOOR}",
        required = true
    )
    private Long lastStatusClassId;

    /**
     * for FREE, UNPAID, PREPAID_PARTIAL, PREPAID_COMPLETE, POSTPAID, SETTLEMENT
     */
    @NotNull(message = "must not be null")
    @Schema(description = "for FREE, UNPAID, PREPAID_PARTIAL, PREPAID_COMPLETE, POSTPAID, SETTLEMENT", required = true)
    private Long paymentStateClassId;

    private Long categoryClassId;

    @NotNull(message = "must not be null")
    private Double totalPrice;

    private Double discount;

    private String discountCode;

    private Double finalTax;

    @NotNull(message = "must not be null")
    private Double payable;

    @Size(max = 1000)
    private String description;

    private LocationDTO location;

    private PartyDTO buyerParty;

    private PartyDTO sellerParty;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFactorCode() {
        return factorCode;
    }

    public void setFactorCode(String factorCode) {
        this.factorCode = factorCode;
    }

    public Long getLastStatusClassId() {
        return lastStatusClassId;
    }

    public void setLastStatusClassId(Long lastStatusClassId) {
        this.lastStatusClassId = lastStatusClassId;
    }

    public Long getPaymentStateClassId() {
        return paymentStateClassId;
    }

    public void setPaymentStateClassId(Long paymentStateClassId) {
        this.paymentStateClassId = paymentStateClassId;
    }

    public Long getCategoryClassId() {
        return categoryClassId;
    }

    public void setCategoryClassId(Long categoryClassId) {
        this.categoryClassId = categoryClassId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public Double getFinalTax() {
        return finalTax;
    }

    public void setFinalTax(Double finalTax) {
        this.finalTax = finalTax;
    }

    public Double getPayable() {
        return payable;
    }

    public void setPayable(Double payable) {
        this.payable = payable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public PartyDTO getBuyerParty() {
        return buyerParty;
    }

    public void setBuyerParty(PartyDTO buyerParty) {
        this.buyerParty = buyerParty;
    }

    public PartyDTO getSellerParty() {
        return sellerParty;
    }

    public void setSellerParty(PartyDTO sellerParty) {
        this.sellerParty = sellerParty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactorDTO)) {
            return false;
        }

        FactorDTO factorDTO = (FactorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, factorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactorDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", factorCode='" + getFactorCode() + "'" +
            ", lastStatusClassId=" + getLastStatusClassId() +
            ", paymentStateClassId=" + getPaymentStateClassId() +
            ", categoryClassId=" + getCategoryClassId() +
            ", totalPrice=" + getTotalPrice() +
            ", discount=" + getDiscount() +
            ", discountCode='" + getDiscountCode() + "'" +
            ", finalTax=" + getFinalTax() +
            ", payable=" + getPayable() +
            ", description='" + getDescription() + "'" +
            ", location=" + getLocation() +
            ", buyerParty=" + getBuyerParty() +
            ", sellerParty=" + getSellerParty() +
            "}";
    }
}
