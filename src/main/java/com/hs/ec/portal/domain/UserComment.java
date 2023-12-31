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
 * Comment for manage comment on paries and products
 * 4 field fixed
 * @author Hossein Sadeghi (hsadeghi78@gmail.com)
 */
@Table("user_comment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("rating")
    private Float rating;

    @NotNull(message = "must not be null")
    @Column("visible")
    private Boolean visible;

    @Size(max = 2000)
    @Column("description")
    private String description;

    @Transient
    @JsonIgnoreProperties(value = { "children", "party", "product", "factor", "parent" }, allowSetters = true)
    private Set<UserComment> children = new HashSet<>();

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
    private Party party;

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

    @Transient
    @JsonIgnoreProperties(value = { "location", "factorItems", "userComments", "buyerParty", "sellerParty" }, allowSetters = true)
    private Factor factor;

    @Transient
    @JsonIgnoreProperties(value = { "children", "party", "product", "factor", "parent" }, allowSetters = true)
    private UserComment parent;

    @Column("party_id")
    private Long partyId;

    @Column("product_id")
    private Long productId;

    @Column("factor_id")
    private Long factorId;

    @Column("parent_id")
    private Long parentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserComment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getRating() {
        return this.rating;
    }

    public UserComment rating(Float rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Boolean getVisible() {
        return this.visible;
    }

    public UserComment visible(Boolean visible) {
        this.setVisible(visible);
        return this;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getDescription() {
        return this.description;
    }

    public UserComment description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<UserComment> getChildren() {
        return this.children;
    }

    public void setChildren(Set<UserComment> userComments) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (userComments != null) {
            userComments.forEach(i -> i.setParent(this));
        }
        this.children = userComments;
    }

    public UserComment children(Set<UserComment> userComments) {
        this.setChildren(userComments);
        return this;
    }

    public UserComment addChildren(UserComment userComment) {
        this.children.add(userComment);
        userComment.setParent(this);
        return this;
    }

    public UserComment removeChildren(UserComment userComment) {
        this.children.remove(userComment);
        userComment.setParent(null);
        return this;
    }

    public Party getParty() {
        return this.party;
    }

    public void setParty(Party party) {
        this.party = party;
        this.partyId = party != null ? party.getId() : null;
    }

    public UserComment party(Party party) {
        this.setParty(party);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.productId = product != null ? product.getId() : null;
    }

    public UserComment product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Factor getFactor() {
        return this.factor;
    }

    public void setFactor(Factor factor) {
        this.factor = factor;
        this.factorId = factor != null ? factor.getId() : null;
    }

    public UserComment factor(Factor factor) {
        this.setFactor(factor);
        return this;
    }

    public UserComment getParent() {
        return this.parent;
    }

    public void setParent(UserComment userComment) {
        this.parent = userComment;
        this.parentId = userComment != null ? userComment.getId() : null;
    }

    public UserComment parent(UserComment userComment) {
        this.setParent(userComment);
        return this;
    }

    public Long getPartyId() {
        return this.partyId;
    }

    public void setPartyId(Long party) {
        this.partyId = party;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long product) {
        this.productId = product;
    }

    public Long getFactorId() {
        return this.factorId;
    }

    public void setFactorId(Long factor) {
        this.factorId = factor;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long userComment) {
        this.parentId = userComment;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserComment)) {
            return false;
        }
        return getId() != null && getId().equals(((UserComment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserComment{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            ", visible='" + getVisible() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
