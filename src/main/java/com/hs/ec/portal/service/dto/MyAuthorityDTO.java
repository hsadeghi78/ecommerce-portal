package com.hs.ec.portal.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hs.ec.portal.domain.MyAuthority} entity.
 */
@Schema(description = "@author Hossein Sadeghi (hsadeghi78@gmail.com)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MyAuthorityDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    private String name;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    private String displayName;

    private MyAuthorityDTO parent;

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

    public MyAuthorityDTO getParent() {
        return parent;
    }

    public void setParent(MyAuthorityDTO parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyAuthorityDTO)) {
            return false;
        }

        MyAuthorityDTO myAuthorityDTO = (MyAuthorityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, myAuthorityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MyAuthorityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            ", parent=" + getParent() +
            "}";
    }
}
