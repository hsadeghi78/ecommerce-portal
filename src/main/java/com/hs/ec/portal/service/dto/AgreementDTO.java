package com.hs.ec.portal.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.hs.ec.portal.domain.Agreement} entity.
 */
@Schema(description = "4 field fixed\n@author Hossein Sadeghi (hsadeghi78@gmail.com)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgreementDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private LocalDate startDate;

    @NotNull(message = "must not be null")
    private LocalDate endDate;

    /**
     * activationStatusClass PRE_REGISTER,SUSPEND,FINAL,ACTIVE,EXPIRE
     */
    @NotNull(message = "must not be null")
    @Schema(description = "activationStatusClass PRE_REGISTER,SUSPEND,FINAL,ACTIVE,EXPIRE", required = true)
    private Long activationStatusClassId;

    @NotNull(message = "must not be null")
    private Double infrastructureBenefit;

    private Double extraBenefit;

    private PartyDTO provider;

    private PartyDTO consumer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getActivationStatusClassId() {
        return activationStatusClassId;
    }

    public void setActivationStatusClassId(Long activationStatusClassId) {
        this.activationStatusClassId = activationStatusClassId;
    }

    public Double getInfrastructureBenefit() {
        return infrastructureBenefit;
    }

    public void setInfrastructureBenefit(Double infrastructureBenefit) {
        this.infrastructureBenefit = infrastructureBenefit;
    }

    public Double getExtraBenefit() {
        return extraBenefit;
    }

    public void setExtraBenefit(Double extraBenefit) {
        this.extraBenefit = extraBenefit;
    }

    public PartyDTO getProvider() {
        return provider;
    }

    public void setProvider(PartyDTO provider) {
        this.provider = provider;
    }

    public PartyDTO getConsumer() {
        return consumer;
    }

    public void setConsumer(PartyDTO consumer) {
        this.consumer = consumer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgreementDTO)) {
            return false;
        }

        AgreementDTO agreementDTO = (AgreementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, agreementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgreementDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", activationStatusClassId=" + getActivationStatusClassId() +
            ", infrastructureBenefit=" + getInfrastructureBenefit() +
            ", extraBenefit=" + getExtraBenefit() +
            ", provider=" + getProvider() +
            ", consumer=" + getConsumer() +
            "}";
    }
}
