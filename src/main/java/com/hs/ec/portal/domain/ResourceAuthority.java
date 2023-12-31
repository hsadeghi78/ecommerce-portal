package com.hs.ec.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hs.ec.portal.domain.enumeration.Verb;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Hossein Sadeghi (hsadeghi78@gmail.com)
 */
@Table("resource_authority")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResourceAuthority implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("verb")
    private Verb verb;

    @Transient
    @JsonIgnoreProperties(value = { "resourceAuthorities" }, allowSetters = true)
    private Resource resource;

    @Transient
    @JsonIgnoreProperties(value = { "children", "resourceAuthorities", "parent" }, allowSetters = true)
    private MyAuthority myAuthority;

    @Column("resource_id")
    private Long resourceId;

    @Column("my_authority_id")
    private Long myAuthorityId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ResourceAuthority id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Verb getVerb() {
        return this.verb;
    }

    public ResourceAuthority verb(Verb verb) {
        this.setVerb(verb);
        return this;
    }

    public void setVerb(Verb verb) {
        this.verb = verb;
    }

    public Resource getResource() {
        return this.resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
        this.resourceId = resource != null ? resource.getId() : null;
    }

    public ResourceAuthority resource(Resource resource) {
        this.setResource(resource);
        return this;
    }

    public MyAuthority getMyAuthority() {
        return this.myAuthority;
    }

    public void setMyAuthority(MyAuthority myAuthority) {
        this.myAuthority = myAuthority;
        this.myAuthorityId = myAuthority != null ? myAuthority.getId() : null;
    }

    public ResourceAuthority myAuthority(MyAuthority myAuthority) {
        this.setMyAuthority(myAuthority);
        return this;
    }

    public Long getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(Long resource) {
        this.resourceId = resource;
    }

    public Long getMyAuthorityId() {
        return this.myAuthorityId;
    }

    public void setMyAuthorityId(Long myAuthority) {
        this.myAuthorityId = myAuthority;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceAuthority)) {
            return false;
        }
        return getId() != null && getId().equals(((ResourceAuthority) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceAuthority{" +
            "id=" + getId() +
            ", verb='" + getVerb() + "'" +
            "}";
    }
}
