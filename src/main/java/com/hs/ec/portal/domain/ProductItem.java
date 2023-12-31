package com.hs.ec.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Extra Information Or attributes Of Product
 * @author Hossein Sadeghi (hsadeghi78@gmail.com)
 */
@Table("product_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    /**
     * typeClass, HEADER, BASE, TECHNICAL, DESIGN, MEMORY, CPU and ..... (Base On business maybe change)
     */
    @NotNull(message = "must not be null")
    @Column("type_class_id")
    private Long typeClassId;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Size(max = 800)
    @Column("value")
    private String value;

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

    @Column("product_id")
    private Long productId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTypeClassId() {
        return this.typeClassId;
    }

    public ProductItem typeClassId(Long typeClassId) {
        this.setTypeClassId(typeClassId);
        return this;
    }

    public void setTypeClassId(Long typeClassId) {
        this.typeClassId = typeClassId;
    }

    public String getName() {
        return this.name;
    }

    public ProductItem name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public ProductItem value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.productId = product != null ? product.getId() : null;
    }

    public ProductItem product(Product product) {
        this.setProduct(product);
        return this;
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
        if (!(o instanceof ProductItem)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductItem{" +
            "id=" + getId() +
            ", typeClassId=" + getTypeClassId() +
            ", name='" + getName() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
