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
 * @author Hossein Sadeghi (hsadeghi78@gmail.com)
 */
@Table("my_authority")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MyAuthority implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column("display_name")
    private String displayName;

    @Transient
    @JsonIgnoreProperties(value = { "children", "resourceAuthorities", "parent" }, allowSetters = true)
    private Set<MyAuthority> children = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "resource", "myAuthority" }, allowSetters = true)
    private Set<ResourceAuthority> resourceAuthorities = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "children", "resourceAuthorities", "parent" }, allowSetters = true)
    private MyAuthority parent;

    @Column("parent_id")
    private Long parentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MyAuthority id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MyAuthority name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public MyAuthority displayName(String displayName) {
        this.setDisplayName(displayName);
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Set<MyAuthority> getChildren() {
        return this.children;
    }

    public void setChildren(Set<MyAuthority> myAuthorities) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (myAuthorities != null) {
            myAuthorities.forEach(i -> i.setParent(this));
        }
        this.children = myAuthorities;
    }

    public MyAuthority children(Set<MyAuthority> myAuthorities) {
        this.setChildren(myAuthorities);
        return this;
    }

    public MyAuthority addChildren(MyAuthority myAuthority) {
        this.children.add(myAuthority);
        myAuthority.setParent(this);
        return this;
    }

    public MyAuthority removeChildren(MyAuthority myAuthority) {
        this.children.remove(myAuthority);
        myAuthority.setParent(null);
        return this;
    }

    public Set<ResourceAuthority> getResourceAuthorities() {
        return this.resourceAuthorities;
    }

    public void setResourceAuthorities(Set<ResourceAuthority> resourceAuthorities) {
        if (this.resourceAuthorities != null) {
            this.resourceAuthorities.forEach(i -> i.setMyAuthority(null));
        }
        if (resourceAuthorities != null) {
            resourceAuthorities.forEach(i -> i.setMyAuthority(this));
        }
        this.resourceAuthorities = resourceAuthorities;
    }

    public MyAuthority resourceAuthorities(Set<ResourceAuthority> resourceAuthorities) {
        this.setResourceAuthorities(resourceAuthorities);
        return this;
    }

    public MyAuthority addResourceAuthorities(ResourceAuthority resourceAuthority) {
        this.resourceAuthorities.add(resourceAuthority);
        resourceAuthority.setMyAuthority(this);
        return this;
    }

    public MyAuthority removeResourceAuthorities(ResourceAuthority resourceAuthority) {
        this.resourceAuthorities.remove(resourceAuthority);
        resourceAuthority.setMyAuthority(null);
        return this;
    }

    public MyAuthority getParent() {
        return this.parent;
    }

    public void setParent(MyAuthority myAuthority) {
        this.parent = myAuthority;
        this.parentId = myAuthority != null ? myAuthority.getId() : null;
    }

    public MyAuthority parent(MyAuthority myAuthority) {
        this.setParent(myAuthority);
        return this;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long myAuthority) {
        this.parentId = myAuthority;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyAuthority)) {
            return false;
        }
        return getId() != null && getId().equals(((MyAuthority) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MyAuthority{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            "}";
    }
}
