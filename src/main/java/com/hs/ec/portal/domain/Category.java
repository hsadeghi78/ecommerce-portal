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
 * Category And subcategories
 * @author Hossein Sadeghi (hsadeghi78@gmail.com)
 */
@Table("category")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column("title")
    private String title;

    @NotNull(message = "must not be null")
    @Size(max = 10)
    @Column("code")
    private String code;

    @NotNull(message = "must not be null")
    @Column("has_child")
    private Boolean hasChild;

    @NotNull(message = "must not be null")
    @Column("level")
    private Integer level;

    @Size(max = 500)
    @Column("keywords")
    private String keywords;

    @Size(max = 500)
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

    @Transient
    @JsonIgnoreProperties(value = { "products", "children", "parent" }, allowSetters = true)
    private Set<Category> children = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "products", "children", "parent" }, allowSetters = true)
    private Category parent;

    @Column("parent_id")
    private Long parentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Category id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Category title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return this.code;
    }

    public Category code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getHasChild() {
        return this.hasChild;
    }

    public Category hasChild(Boolean hasChild) {
        this.setHasChild(hasChild);
        return this;
    }

    public void setHasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }

    public Integer getLevel() {
        return this.level;
    }

    public Category level(Integer level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public Category keywords(String keywords) {
        this.setKeywords(keywords);
        return this;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return this.description;
    }

    public Category description(String description) {
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
        if (this.products != null) {
            this.products.forEach(i -> i.setCategory(null));
        }
        if (products != null) {
            products.forEach(i -> i.setCategory(this));
        }
        this.products = products;
    }

    public Category products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Category addProducts(Product product) {
        this.products.add(product);
        product.setCategory(this);
        return this;
    }

    public Category removeProducts(Product product) {
        this.products.remove(product);
        product.setCategory(null);
        return this;
    }

    public Set<Category> getChildren() {
        return this.children;
    }

    public void setChildren(Set<Category> categories) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (categories != null) {
            categories.forEach(i -> i.setParent(this));
        }
        this.children = categories;
    }

    public Category children(Set<Category> categories) {
        this.setChildren(categories);
        return this;
    }

    public Category addChildren(Category category) {
        this.children.add(category);
        category.setParent(this);
        return this;
    }

    public Category removeChildren(Category category) {
        this.children.remove(category);
        category.setParent(null);
        return this;
    }

    public Category getParent() {
        return this.parent;
    }

    public void setParent(Category category) {
        this.parent = category;
        this.parentId = category != null ? category.getId() : null;
    }

    public Category parent(Category category) {
        this.setParent(category);
        return this;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long category) {
        this.parentId = category;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return getId() != null && getId().equals(((Category) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", code='" + getCode() + "'" +
            ", hasChild='" + getHasChild() + "'" +
            ", level=" + getLevel() +
            ", keywords='" + getKeywords() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
