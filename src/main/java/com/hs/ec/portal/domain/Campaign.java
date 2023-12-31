package com.hs.ec.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
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
@Table("campaign")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Campaign implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 200)
    @Column("title")
    private String title;

    @NotNull(message = "must not be null")
    @Column("start_date")
    private LocalDate startDate;

    @NotNull(message = "must not be null")
    @Column("end_date")
    private LocalDate endDate;

    @Column("photo")
    private byte[] photo;

    @NotNull
    @Column("photo_content_type")
    private String photoContentType;

    @Size(max = 1000)
    @Column("description")
    private String description;

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
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Campaign id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Campaign title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Campaign startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Campaign endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Campaign photo(byte[] photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Campaign photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getDescription() {
        return this.description;
    }

    public Campaign description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Campaign products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Campaign addProducts(Product product) {
        this.products.add(product);
        return this;
    }

    public Campaign removeProducts(Product product) {
        this.products.remove(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Campaign)) {
            return false;
        }
        return getId() != null && getId().equals(((Campaign) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Campaign{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
