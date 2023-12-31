package com.hs.ec.portal.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hs.ec.portal.domain.UserComment} entity.
 */
@Schema(description = "Comment for manage comment on paries and products\n4 field fixed\n@author Hossein Sadeghi (hsadeghi78@gmail.com)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserCommentDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Float rating;

    @NotNull(message = "must not be null")
    private Boolean visible;

    @Size(max = 2000)
    private String description;

    private PartyDTO party;

    private ProductDTO product;

    private FactorDTO factor;

    private UserCommentDTO parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PartyDTO getParty() {
        return party;
    }

    public void setParty(PartyDTO party) {
        this.party = party;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public FactorDTO getFactor() {
        return factor;
    }

    public void setFactor(FactorDTO factor) {
        this.factor = factor;
    }

    public UserCommentDTO getParent() {
        return parent;
    }

    public void setParent(UserCommentDTO parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserCommentDTO)) {
            return false;
        }

        UserCommentDTO userCommentDTO = (UserCommentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userCommentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserCommentDTO{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            ", visible='" + getVisible() + "'" +
            ", description='" + getDescription() + "'" +
            ", party=" + getParty() +
            ", product=" + getProduct() +
            ", factor=" + getFactor() +
            ", parent=" + getParent() +
            "}";
    }
}
