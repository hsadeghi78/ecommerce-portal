package com.hs.ec.portal.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hs.ec.portal.domain.Contact} entity. This class is used
 * in {@link com.hs.ec.portal.web.rest.ContactResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contacts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter contactValue;

    private LongFilter typeClassId;

    private StringFilter prefix;

    private StringFilter description;

    private LongFilter partyId;

    private Boolean distinct;

    public ContactCriteria() {}

    public ContactCriteria(ContactCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.contactValue = other.contactValue == null ? null : other.contactValue.copy();
        this.typeClassId = other.typeClassId == null ? null : other.typeClassId.copy();
        this.prefix = other.prefix == null ? null : other.prefix.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.partyId = other.partyId == null ? null : other.partyId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ContactCriteria copy() {
        return new ContactCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getContactValue() {
        return contactValue;
    }

    public StringFilter contactValue() {
        if (contactValue == null) {
            contactValue = new StringFilter();
        }
        return contactValue;
    }

    public void setContactValue(StringFilter contactValue) {
        this.contactValue = contactValue;
    }

    public LongFilter getTypeClassId() {
        return typeClassId;
    }

    public LongFilter typeClassId() {
        if (typeClassId == null) {
            typeClassId = new LongFilter();
        }
        return typeClassId;
    }

    public void setTypeClassId(LongFilter typeClassId) {
        this.typeClassId = typeClassId;
    }

    public StringFilter getPrefix() {
        return prefix;
    }

    public StringFilter prefix() {
        if (prefix == null) {
            prefix = new StringFilter();
        }
        return prefix;
    }

    public void setPrefix(StringFilter prefix) {
        this.prefix = prefix;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getPartyId() {
        return partyId;
    }

    public LongFilter partyId() {
        if (partyId == null) {
            partyId = new LongFilter();
        }
        return partyId;
    }

    public void setPartyId(LongFilter partyId) {
        this.partyId = partyId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ContactCriteria that = (ContactCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(contactValue, that.contactValue) &&
            Objects.equals(typeClassId, that.typeClassId) &&
            Objects.equals(prefix, that.prefix) &&
            Objects.equals(description, that.description) &&
            Objects.equals(partyId, that.partyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contactValue, typeClassId, prefix, description, partyId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (contactValue != null ? "contactValue=" + contactValue + ", " : "") +
            (typeClassId != null ? "typeClassId=" + typeClassId + ", " : "") +
            (prefix != null ? "prefix=" + prefix + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (partyId != null ? "partyId=" + partyId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
