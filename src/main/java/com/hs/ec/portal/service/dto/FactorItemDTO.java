package com.hs.ec.portal.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hs.ec.portal.domain.FactorItem} entity.
 */
@Schema(description = "4 field fixed\n@author Hossein Sadeghi (hsadeghi78@gmail.com)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactorItemDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Integer rowNum;

    @NotNull(message = "must not be null")
    @Size(max = 200)
    private String title;

    @NotNull(message = "must not be null")
    private Integer count;

    private Double discount;

    private Double tax;

    @Size(max = 300)
    private String description;

    private FactorDTO factor;

    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FactorDTO getFactor() {
        return factor;
    }

    public void setFactor(FactorDTO factor) {
        this.factor = factor;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactorItemDTO)) {
            return false;
        }

        FactorItemDTO factorItemDTO = (FactorItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, factorItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactorItemDTO{" +
            "id=" + getId() +
            ", rowNum=" + getRowNum() +
            ", title='" + getTitle() + "'" +
            ", count=" + getCount() +
            ", discount=" + getDiscount() +
            ", tax=" + getTax() +
            ", description='" + getDescription() + "'" +
            ", factor=" + getFactor() +
            ", product=" + getProduct() +
            "}";
    }
}
