package com.hs.ec.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 4 field fixed
 * @author Hossein Sadeghi (hsadeghi78@gmail.com)
 */
@Table("factor_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactorItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("row_num")
    private Integer rowNum;

    @NotNull(message = "must not be null")
    @Size(max = 200)
    @Column("title")
    private String title;

    @NotNull(message = "must not be null")
    @Column("count")
    private Integer count;

    @Column("discount")
    private Double discount;

    @Column("tax")
    private Double tax;

    @Size(max = 300)
    @Column("description")
    private String description;

    @Transient
    @JsonIgnoreProperties(value = { "location", "factorItems", "userComments", "buyerParty", "sellerParty" }, allowSetters = true)
    private Factor factor;

    @Transient
    @JsonIgnoreProperties(
        value = {
            "productItems",
            "userComments",
            "children",
            "favorites",
            "materials",
            "factorItems",
            "documents",
            "category",
            "party",
            "parent",
            "campaigns",
        },
        allowSetters = true
    )
    private Product product;

    @Column("factor_id")
    private Long factorId;

    @Column("product_id")
    private Long productId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FactorItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRowNum() {
        return this.rowNum;
    }

    public FactorItem rowNum(Integer rowNum) {
        this.setRowNum(rowNum);
        return this;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getTitle() {
        return this.title;
    }

    public FactorItem title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCount() {
        return this.count;
    }

    public FactorItem count(Integer count) {
        this.setCount(count);
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getDiscount() {
        return this.discount;
    }

    public FactorItem discount(Double discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTax() {
        return this.tax;
    }

    public FactorItem tax(Double tax) {
        this.setTax(tax);
        return this;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public String getDescription() {
        return this.description;
    }

    public FactorItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Factor getFactor() {
        return this.factor;
    }

    public void setFactor(Factor factor) {
        this.factor = factor;
        this.factorId = factor != null ? factor.getId() : null;
    }

    public FactorItem factor(Factor factor) {
        this.setFactor(factor);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.productId = product != null ? product.getId() : null;
    }

    public FactorItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Long getFactorId() {
        return this.factorId;
    }

    public void setFactorId(Long factor) {
        this.factorId = factor;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long product) {
        this.productId = product;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactorItem)) {
            return false;
        }
        return getId() != null && getId().equals(((FactorItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactorItem{" +
            "id=" + getId() +
            ", rowNum=" + getRowNum() +
            ", title='" + getTitle() + "'" +
            ", count=" + getCount() +
            ", discount=" + getDiscount() +
            ", tax=" + getTax() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
