package com.hs.ec.portal.service.dto;

import com.hs.ec.portal.domain.enumeration.Verb;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hs.ec.portal.domain.ResourceAuthority} entity.
 */
@Schema(description = "@author Hossein Sadeghi (hsadeghi78@gmail.com)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResourceAuthorityDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Verb verb;

    private ResourceDTO resource;

    private MyAuthorityDTO myAuthority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Verb getVerb() {
        return verb;
    }

    public void setVerb(Verb verb) {
        this.verb = verb;
    }

    public ResourceDTO getResource() {
        return resource;
    }

    public void setResource(ResourceDTO resource) {
        this.resource = resource;
    }

    public MyAuthorityDTO getMyAuthority() {
        return myAuthority;
    }

    public void setMyAuthority(MyAuthorityDTO myAuthority) {
        this.myAuthority = myAuthority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceAuthorityDTO)) {
            return false;
        }

        ResourceAuthorityDTO resourceAuthorityDTO = (ResourceAuthorityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, resourceAuthorityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceAuthorityDTO{" +
            "id=" + getId() +
            ", verb='" + getVerb() + "'" +
            ", resource=" + getResource() +
            ", myAuthority=" + getMyAuthority() +
            "}";
    }
}
