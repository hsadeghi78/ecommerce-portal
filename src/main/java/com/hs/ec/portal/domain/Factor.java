package com.hs.ec.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 4 field fixed
 * @author Hossein Sadeghi (hsadeghi78@gmail.com)
 */
@Table("factor")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Factor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 200)
    @Column("title")
    private String title;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column("factor_code")
    private String factorCode;

    /**
     * FactorStatus{PURCHASE_ORDER, INITIATE, SUSPEND, UPDATED, FINAL, PREPARING, READY_TO_DELIVERY, DELIVERY_SUCCESS, DELIVERY_FAIL, CANCEL, DELETED, INDOOR, OUTDOOR}
     */
    @NotNull(message = "must not be null")
    @Column("last_status_class_id")
    private Long lastStatusClassId;

    /**
     * for FREE, UNPAID, PREPAID_PARTIAL, PREPAID_COMPLETE, POSTPAID, SETTLEMENT
     */
    @NotNull(message = "must not be null")
    @Column("payment_state_class_id")
    private Long paymentStateClassId;

    @Column("category_class_id")
    private Long categoryClassId;

    @NotNull(message = "must not be null")
    @Column("total_price")
    private Double totalPrice;

    @Column("discount")
    private Double discount;

    @Column("discount_code")
    private String discountCode;

    @Column("final_tax")
    private Double finalTax;

    @NotNull(message = "must not be null")
    @Column("payable")
    private Double payable;

    @Size(max = 1000)
    @Column("description")
    private String description;

    @Transient
    private Location location;

    @Transient
    @JsonIgnoreProperties(value = { "factor", "product" }, allowSetters = true)
    private Set<FactorItem> factorItems = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "children", "party", "product", "factor", "parent" }, allowSetters = true)
    private Set<UserComment> userComments = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = {
            "buyerFactors",
            "sellerFactors",
            "userComments",
            "products",
            "providerAgreements",
            "consumerAgreements",
            "contacts",
            "locations",
        },
        allowSetters = true
    )
    private Party buyerParty;

    @Transient
    @JsonIgnoreProperties(
        value = {
            "buyerFactors",
            "sellerFactors",
            "userComments",
            "products",
            "providerAgreements",
            "consumerAgreements",
            "contacts",
            "locations",
        },
        allowSetters = true
    )
    private Party sellerParty;

    @Column("location_id")
    private Long locationId;

    @Column("buyer_party_id")
    private Long buyerPartyId;

    @Column("seller_party_id")
    private Long sellerPartyId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Factor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Factor title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFactorCode() {
        return this.factorCode;
    }

    public Factor factorCode(String factorCode) {
        this.setFactorCode(factorCode);
        return this;
    }

    public void setFactorCode(String factorCode) {
        this.factorCode = factorCode;
    }

    public Long getLastStatusClassId() {
        return this.lastStatusClassId;
    }

    public Factor lastStatusClassId(Long lastStatusClassId) {
        this.setLastStatusClassId(lastStatusClassId);
        return this;
    }

    public void setLastStatusClassId(Long lastStatusClassId) {
        this.lastStatusClassId = lastStatusClassId;
    }

    public Long getPaymentStateClassId() {
        return this.paymentStateClassId;
    }

    public Factor paymentStateClassId(Long paymentStateClassId) {
        this.setPaymentStateClassId(paymentStateClassId);
        return this;
    }

    public void setPaymentStateClassId(Long paymentStateClassId) {
        this.paymentStateClassId = paymentStateClassId;
    }

    public Long getCategoryClassId() {
        return this.categoryClassId;
    }

    public Factor categoryClassId(Long categoryClassId) {
        this.setCategoryClassId(categoryClassId);
        return this;
    }

    public void setCategoryClassId(Long categoryClassId) {
        this.categoryClassId = categoryClassId;
    }

    public Double getTotalPrice() {
        return this.totalPrice;
    }

    public Factor totalPrice(Double totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getDiscount() {
        return this.discount;
    }

    public Factor discount(Double discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getDiscountCode() {
        return this.discountCode;
    }

    public Factor discountCode(String discountCode) {
        this.setDiscountCode(discountCode);
        return this;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public Double getFinalTax() {
        return this.finalTax;
    }

    public Factor finalTax(Double finalTax) {
        this.setFinalTax(finalTax);
        return this;
    }

    public void setFinalTax(Double finalTax) {
        this.finalTax = finalTax;
    }

    public Double getPayable() {
        return this.payable;
    }

    public Factor payable(Double payable) {
        this.setPayable(payable);
        return this;
    }

    public void setPayable(Double payable) {
        this.payable = payable;
    }

    public String getDescription() {
        return this.description;
    }

    public Factor description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
        this.locationId = location != null ? location.getId() : null;
    }

    public Factor location(Location location) {
        this.setLocation(location);
        return this;
    }

    public Set<FactorItem> getFactorItems() {
        return this.factorItems;
    }

    public void setFactorItems(Set<FactorItem> factorItems) {
        if (this.factorItems != null) {
            this.factorItems.forEach(i -> i.setFactor(null));
        }
        if (factorItems != null) {
            factorItems.forEach(i -> i.setFactor(this));
        }
        this.factorItems = factorItems;
    }

    public Factor factorItems(Set<FactorItem> factorItems) {
        this.setFactorItems(factorItems);
        return this;
    }

    public Factor addFactorItems(FactorItem factorItem) {
        this.factorItems.add(factorItem);
        factorItem.setFactor(this);
        return this;
    }

    public Factor removeFactorItems(FactorItem factorItem) {
        this.factorItems.remove(factorItem);
        factorItem.setFactor(null);
        return this;
    }

    public Set<UserComment> getUserComments() {
        return this.userComments;
    }

    public void setUserComments(Set<UserComment> userComments) {
        if (this.userComments != null) {
            this.userComments.forEach(i -> i.setFactor(null));
        }
        if (userComments != null) {
            userComments.forEach(i -> i.setFactor(this));
        }
        this.userComments = userComments;
    }

    public Factor userComments(Set<UserComment> userComments) {
        this.setUserComments(userComments);
        return this;
    }

    public Factor addUserComments(UserComment userComment) {
        this.userComments.add(userComment);
        userComment.setFactor(this);
        return this;
    }

    public Factor removeUserComments(UserComment userComment) {
        this.userComments.remove(userComment);
        userComment.setFactor(null);
        return this;
    }

    public Party getBuyerParty() {
        return this.buyerParty;
    }

    public void setBuyerParty(Party party) {
        this.buyerParty = party;
        this.buyerPartyId = party != null ? party.getId() : null;
    }

    public Factor buyerParty(Party party) {
        this.setBuyerParty(party);
        return this;
    }

    public Party getSellerParty() {
        return this.sellerParty;
    }

    public void setSellerParty(Party party) {
        this.sellerParty = party;
        this.sellerPartyId = party != null ? party.getId() : null;
    }

    public Factor sellerParty(Party party) {
        this.setSellerParty(party);
        return this;
    }

    public Long getLocationId() {
        return this.locationId;
    }

    public void setLocationId(Long location) {
        this.locationId = location;
    }

    public Long getBuyerPartyId() {
        return this.buyerPartyId;
    }

    public void setBuyerPartyId(Long party) {
        this.buyerPartyId = party;
    }

    public Long getSellerPartyId() {
        return this.sellerPartyId;
    }

    public void setSellerPartyId(Long party) {
        this.sellerPartyId = party;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Factor)) {
            return false;
        }
        return getId() != null && getId().equals(((Factor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Factor{" +
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
            "}";
    }
}
