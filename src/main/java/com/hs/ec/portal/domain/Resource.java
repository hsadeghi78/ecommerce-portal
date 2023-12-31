package com.hs.ec.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hs.ec.portal.domain.enumeration.ResourceType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
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
@Table("resource")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Size(max = 300)
    @Column("display_name")
    private String displayName;

    @NotNull(message = "must not be null")
    @Size(max = 1000)
    @Column("api_uri")
    private String apiUri;

    @NotNull(message = "must not be null")
    @Column("resource_type")
    private ResourceType resourceType;

    @Transient
    @JsonIgnoreProperties(value = { "resource", "myAuthority" }, allowSetters = true)
    private Set<ResourceAuthority> resourceAuthorities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Resource id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Resource name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Resource displayName(String displayName) {
        this.setDisplayName(displayName);
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getApiUri() {
        return this.apiUri;
    }

    public Resource apiUri(String apiUri) {
        this.setApiUri(apiUri);
        return this;
    }

    public void setApiUri(String apiUri) {
        this.apiUri = apiUri;
    }

    public ResourceType getResourceType() {
        return this.resourceType;
    }

    public Resource resourceType(ResourceType resourceType) {
        this.setResourceType(resourceType);
        return this;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public Set<ResourceAuthority> getResourceAuthorities() {
        return this.resourceAuthorities;
    }

    public void setResourceAuthorities(Set<ResourceAuthority> resourceAuthorities) {
        if (this.resourceAuthorities != null) {
            this.resourceAuthorities.forEach(i -> i.setResource(null));
        }
        if (resourceAuthorities != null) {
            resourceAuthorities.forEach(i -> i.setResource(this));
        }
        this.resourceAuthorities = resourceAuthorities;
    }

    public Resource resourceAuthorities(Set<ResourceAuthority> resourceAuthorities) {
        this.setResourceAuthorities(resourceAuthorities);
        return this;
    }

    public Resource addResourceAuthorities(ResourceAuthority resourceAuthority) {
        this.resourceAuthorities.add(resourceAuthority);
        resourceAuthority.setResource(this);
        return this;
    }

    public Resource removeResourceAuthorities(ResourceAuthority resourceAuthority) {
        this.resourceAuthorities.remove(resourceAuthority);
        resourceAuthority.setResource(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resource)) {
            return false;
        }
        return getId() != null && getId().equals(((Resource) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Resource{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            ", apiUri='" + getApiUri() + "'" +
            ", resourceType='" + getResourceType() + "'" +
            "}";
    }
}
