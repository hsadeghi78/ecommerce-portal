package com.hs.ec.portal.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hs.ec.portal.domain.UserFavorite} entity.
 */
@Schema(description = "4 field fixed\n@author Hossein Sadeghi (hsadeghi78@gmail.com)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserFavoriteDTO implements Serializable {

    private Long id;

    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(o instanceof UserFavoriteDTO)) {
            return false;
        }

        UserFavoriteDTO userFavoriteDTO = (UserFavoriteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userFavoriteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserFavoriteDTO{" +
            "id=" + getId() +
            ", product=" + getProduct() +
            "}";
    }
}
