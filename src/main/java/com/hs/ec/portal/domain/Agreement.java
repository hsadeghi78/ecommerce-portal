package com.hs.ec.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 4 field fixed
 * @author Hossein Sadeghi (hsadeghi78@gmail.com)
 */
@Table("agreement")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Agreement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("start_date")
    private LocalDate startDate;

    @NotNull(message = "must not be null")
    @Column("end_date")
    private LocalDate endDate;

    /**
     * activationStatusClass PRE_REGISTER,SUSPEND,FINAL,ACTIVE,EXPIRE
     */
    @NotNull(message = "must not be null")
    @Column("activation_status_class_id")
    private Long activationStatusClassId;

    @NotNull(message = "must not be null")
    @Column("infrastructure_benefit")
    private Double infrastructureBenefit;

    @Column("extra_benefit")
    private Double extraBenefit;

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
    private Party provider;

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
    private Party consumer;

    @Column("provider_id")
    private Long providerId;

    @Column("consumer_id")
    private Long consumerId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Agreement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Agreement name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Agreement startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Agreement endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getActivationStatusClassId() {
        return this.activationStatusClassId;
    }

    public Agreement activationStatusClassId(Long activationStatusClassId) {
        this.setActivationStatusClassId(activationStatusClassId);
        return this;
    }

    public void setActivationStatusClassId(Long activationStatusClassId) {
        this.activationStatusClassId = activationStatusClassId;
    }

    public Double getInfrastructureBenefit() {
        return this.infrastructureBenefit;
    }

    public Agreement infrastructureBenefit(Double infrastructureBenefit) {
        this.setInfrastructureBenefit(infrastructureBenefit);
        return this;
    }

    public void setInfrastructureBenefit(Double infrastructureBenefit) {
        this.infrastructureBenefit = infrastructureBenefit;
    }

    public Double getExtraBenefit() {
        return this.extraBenefit;
    }

    public Agreement extraBenefit(Double extraBenefit) {
        this.setExtraBenefit(extraBenefit);
        return this;
    }

    public void setExtraBenefit(Double extraBenefit) {
        this.extraBenefit = extraBenefit;
    }

    public Party getProvider() {
        return this.provider;
    }

    public void setProvider(Party party) {
        this.provider = party;
        this.providerId = party != null ? party.getId() : null;
    }

    public Agreement provider(Party party) {
        this.setProvider(party);
        return this;
    }

    public Party getConsumer() {
        return this.consumer;
    }

    public void setConsumer(Party party) {
        this.consumer = party;
        this.consumerId = party != null ? party.getId() : null;
    }

    public Agreement consumer(Party party) {
        this.setConsumer(party);
        return this;
    }

    public Long getProviderId() {
        return this.providerId;
    }

    public void setProviderId(Long party) {
        this.providerId = party;
    }

    public Long getConsumerId() {
        return this.consumerId;
    }

    public void setConsumerId(Long party) {
        this.consumerId = party;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Agreement)) {
            return false;
        }
        return getId() != null && getId().equals(((Agreement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Agreement{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", activationStatusClassId=" + getActivationStatusClassId() +
            ", infrastructureBenefit=" + getInfrastructureBenefit() +
            ", extraBenefit=" + getExtraBenefit() +
            "}";
    }
}
