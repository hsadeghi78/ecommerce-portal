package com.hs.ec.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 4 field fixed
 * @author Hossein Sadeghi (hsadeghi78@gmail.com)
 */
@Table("contact")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("contact_value")
    private String contactValue;

    /**
     * typeClass TELEPHONE, MOBILE, FAX, EMAIL, PAGER
     */
    @NotNull(message = "must not be null")
    @Column("type_class_id")
    private Long typeClassId;

    @Size(max = 7)
    @Column("prefix")
    private String prefix;

    @Size(max = 500)
    @Column("description")
    private String description;

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

    @Column("party_id")
    private Long partyId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contact id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContactValue() {
        return this.contactValue;
    }

    public Contact contactValue(String contactValue) {
        this.setContactValue(contactValue);
        return this;
    }

    public void setContactValue(String contactValue) {
        this.contactValue = contactValue;
    }

    public Long getTypeClassId() {
        return this.typeClassId;
    }

    public Contact typeClassId(Long typeClassId) {
        this.setTypeClassId(typeClassId);
        return this;
    }

    public void setTypeClassId(Long typeClassId) {
        this.typeClassId = typeClassId;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public Contact prefix(String prefix) {
        this.setPrefix(prefix);
        return this;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getDescription() {
        return this.description;
    }

    public Contact description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Party getParty() {
        return this.party;
    }

    public void setParty(Party party) {
        this.party = party;
        this.partyId = party != null ? party.getId() : null;
    }

    public Contact party(Party party) {
        this.setParty(party);
        return this;
    }

    public Long getPartyId() {
        return this.partyId;
    }

    public void setPartyId(Long party) {
        this.partyId = party;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contact)) {
            return false;
        }
        return getId() != null && getId().equals(((Contact) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contact{" +
            "id=" + getId() +
            ", contactValue='" + getContactValue() + "'" +
            ", typeClassId=" + getTypeClassId() +
            ", prefix='" + getPrefix() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
