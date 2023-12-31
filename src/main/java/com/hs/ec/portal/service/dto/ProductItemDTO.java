package com.hs.ec.portal.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hs.ec.portal.domain.ProductItem} entity.
 */
@Schema(description = "Extra Information Or attributes Of Product\n@author Hossein Sadeghi (hsadeghi78@gmail.com)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductItemDTO implements Serializable {

    private Long id;

    /**
     * typeClass, HEADER, BASE, TECHNICAL, DESIGN, MEMORY, CPU and ..... (Base On business maybe change)
     */
    @NotNull(message = "must not be null")
    @Schema(
        description = "typeClass, HEADER, BASE, TECHNICAL, DESIGN, MEMORY, CPU and ..... (Base On business maybe change)",
        required = true
    )
    private Long typeClassId;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    private String name;

    @NotNull(message = "must not be null")
    @Size(max = 800)
    private String value;

    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTypeClassId() {
        return typeClassId;
    }

    public void setTypeClassId(Long typeClassId) {
        this.typeClassId = typeClassId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        if (!(o instanceof ProductItemDTO)) {
            return false;
        }

        ProductItemDTO productItemDTO = (ProductItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductItemDTO{" +
            "id=" + getId() +
            ", typeClassId=" + getTypeClassId() +
            ", name='" + getName() + "'" +
            ", value='" + getValue() + "'" +
            ", product=" + getProduct() +
            "}";
    }
}
