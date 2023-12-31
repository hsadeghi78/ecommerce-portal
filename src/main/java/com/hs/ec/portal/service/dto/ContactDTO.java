package com.hs.ec.portal.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hs.ec.portal.domain.Contact} entity.
 */
@Schema(description = "4 field fixed\n@author Hossein Sadeghi (hsadeghi78@gmail.com)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String contactValue;

    /**
     * typeClass TELEPHONE, MOBILE, FAX, EMAIL, PAGER
     */
    @NotNull(message = "must not be null")
    @Schema(description = "typeClass TELEPHONE, MOBILE, FAX, EMAIL, PAGER", required = true)
    private Long typeClassId;

    @Size(max = 7)
    private String prefix;

    @Size(max = 500)
    private String description;

    private PartyDTO party;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContactValue() {
        return contactValue;
    }

    public void setContactValue(String contactValue) {
        this.contactValue = contactValue;
    }

    public Long getTypeClassId() {
        return typeClassId;
    }

    public void setTypeClassId(Long typeClassId) {
        this.typeClassId = typeClassId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactDTO)) {
            return false;
        }

        ContactDTO contactDTO = (ContactDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactDTO{" +
            "id=" + getId() +
            ", contactValue='" + getContactValue() + "'" +
            ", typeClassId=" + getTypeClassId() +
            ", prefix='" + getPrefix() + "'" +
            ", description='" + getDescription() + "'" +
            ", party=" + getParty() +
            "}";
    }
}
