package com.hs.ec.portal.service.dto;

import com.hs.ec.portal.domain.enumeration.ResourceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hs.ec.portal.domain.Resource} entity.
 */
@Schema(description = "4 field fixed\n@author Hossein Sadeghi (hsadeghi78@gmail.com)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResourceDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    private String name;

    @NotNull(message = "must not be null")
    @Size(max = 300)
    private String displayName;

    @NotNull(message = "must not be null")
    @Size(max = 1000)
    private String apiUri;

    @NotNull(message = "must not be null")
    private ResourceType resourceType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getApiUri() {
        return apiUri;
    }

    public void setApiUri(String apiUri) {
        this.apiUri = apiUri;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceDTO)) {
            return false;
        }

        ResourceDTO resourceDTO = (ResourceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, resourceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            ", apiUri='" + getApiUri() + "'" +
            ", resourceType='" + getResourceType() + "'" +
            "}";
    }
}
