package com.hs.ec.portal.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hs.ec.portal.domain.Criticism} entity. This class is used
 * in {@link com.hs.ec.portal.web.rest.CriticismResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /criticisms?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CriticismCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fullName;

    private StringFilter email;

    private StringFilter contactNumber;

    private StringFilter description;

    private Boolean distinct;

    public CriticismCriteria() {}

    public CriticismCriteria(CriticismCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fullName = other.fullName == null ? null : other.fullName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.contactNumber = other.contactNumber == null ? null : other.contactNumber.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CriticismCriteria copy() {
        return new CriticismCriteria(this);
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

    public StringFilter getFullName() {
        return fullName;
    }

    public StringFilter fullName() {
        if (fullName == null) {
            fullName = new StringFilter();
        }
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getContactNumber() {
        return contactNumber;
    }

    public StringFilter contactNumber() {
        if (contactNumber == null) {
            contactNumber = new StringFilter();
        }
        return contactNumber;
    }

    public void setContactNumber(StringFilter contactNumber) {
        this.contactNumber = contactNumber;
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
        final CriticismCriteria that = (CriticismCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(contactNumber, that.contactNumber) &&
            Objects.equals(description, that.description) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, email, contactNumber, description, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CriticismCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fullName != null ? "fullName=" + fullName + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (contactNumber != null ? "contactNumber=" + contactNumber + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
