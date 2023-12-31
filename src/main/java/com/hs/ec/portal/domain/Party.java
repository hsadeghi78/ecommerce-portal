package com.hs.ec.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
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
@Table("party")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Party implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 200)
    @Column("title")
    private String title;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column("party_code")
    private String partyCode;

    @NotNull(message = "must not be null")
    @Size(max = 200)
    @Column("trade_title")
    private String tradeTitle;

    @NotNull(message = "must not be null")
    @Column("activation_date")
    private LocalDate activationDate;

    @Column("expiration_date")
    private LocalDate expirationDate;

    @NotNull(message = "must not be null")
    @Column("activation_status")
    private Boolean activationStatus;

    @Column("photo")
    private byte[] photo;

    @Column("photo_content_type")
    private String photoContentType;

    /**
     * PersonType : TRUE>REAL_PERSON, FALSE>LEGAL_PERSON
     */
    @NotNull(message = "must not be null")
    @Column("person_type")
    private Boolean personType;

    @Transient
    @JsonIgnoreProperties(value = { "location", "factorItems", "userComments", "buyerParty", "sellerParty" }, allowSetters = true)
    private Set<Factor> buyerFactors = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "location", "factorItems", "userComments", "buyerParty", "sellerParty" }, allowSetters = true)
    private Set<Factor> sellerFactors = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "children", "party", "product", "factor", "parent" }, allowSetters = true)
    private Set<UserComment> userComments = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = {
            "productItems",
            "userComments",
            "children",
            "favorites",
            "materials",
            "factorItems",
            "documents",
            "category",
            "party",
            "parent",
            "campaigns",
        },
        allowSetters = true
    )
    private Set<Product> products = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "provider", "consumer" }, allowSetters = true)
    private Set<Agreement> providerAgreements = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "provider", "consumer" }, allowSetters = true)
    private Set<Agreement> consumerAgreements = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "party" }, allowSetters = true)
    private Set<Contact> contacts = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "factor", "geoDivision", "party" }, allowSetters = true)
    private Set<Location> locations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Party id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Party title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPartyCode() {
        return this.partyCode;
    }

    public Party partyCode(String partyCode) {
        this.setPartyCode(partyCode);
        return this;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public String getTradeTitle() {
        return this.tradeTitle;
    }

    public Party tradeTitle(String tradeTitle) {
        this.setTradeTitle(tradeTitle);
        return this;
    }

    public void setTradeTitle(String tradeTitle) {
        this.tradeTitle = tradeTitle;
    }

    public LocalDate getActivationDate() {
        return this.activationDate;
    }

    public Party activationDate(LocalDate activationDate) {
        this.setActivationDate(activationDate);
        return this;
    }

    public void setActivationDate(LocalDate activationDate) {
        this.activationDate = activationDate;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public Party expirationDate(LocalDate expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getActivationStatus() {
        return this.activationStatus;
    }

    public Party activationStatus(Boolean activationStatus) {
        this.setActivationStatus(activationStatus);
        return this;
    }

    public void setActivationStatus(Boolean activationStatus) {
        this.activationStatus = activationStatus;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Party photo(byte[] photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Party photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Boolean getPersonType() {
        return this.personType;
    }

    public Party personType(Boolean personType) {
        this.setPersonType(personType);
        return this;
    }

    public void setPersonType(Boolean personType) {
        this.personType = personType;
    }

    public Set<Factor> getBuyerFactors() {
        return this.buyerFactors;
    }

    public void setBuyerFactors(Set<Factor> factors) {
        if (this.buyerFactors != null) {
            this.buyerFactors.forEach(i -> i.setBuyerParty(null));
        }
        if (factors != null) {
            factors.forEach(i -> i.setBuyerParty(this));
        }
        this.buyerFactors = factors;
    }

    public Party buyerFactors(Set<Factor> factors) {
        this.setBuyerFactors(factors);
        return this;
    }

    public Party addBuyerFactors(Factor factor) {
        this.buyerFactors.add(factor);
        factor.setBuyerParty(this);
        return this;
    }

    public Party removeBuyerFactors(Factor factor) {
        this.buyerFactors.remove(factor);
        factor.setBuyerParty(null);
        return this;
    }

    public Set<Factor> getSellerFactors() {
        return this.sellerFactors;
    }

    public void setSellerFactors(Set<Factor> factors) {
        if (this.sellerFactors != null) {
            this.sellerFactors.forEach(i -> i.setSellerParty(null));
        }
        if (factors != null) {
            factors.forEach(i -> i.setSellerParty(this));
        }
        this.sellerFactors = factors;
    }

    public Party sellerFactors(Set<Factor> factors) {
        this.setSellerFactors(factors);
        return this;
    }

    public Party addSellerFactors(Factor factor) {
        this.sellerFactors.add(factor);
        factor.setSellerParty(this);
        return this;
    }

    public Party removeSellerFactors(Factor factor) {
        this.sellerFactors.remove(factor);
        factor.setSellerParty(null);
        return this;
    }

    public Set<UserComment> getUserComments() {
        return this.userComments;
    }

    public void setUserComments(Set<UserComment> userComments) {
        if (this.userComments != null) {
            this.userComments.forEach(i -> i.setParty(null));
        }
        if (userComments != null) {
            userComments.forEach(i -> i.setParty(this));
        }
        this.userComments = userComments;
    }

    public Party userComments(Set<UserComment> userComments) {
        this.setUserComments(userComments);
        return this;
    }

    public Party addUserComments(UserComment userComment) {
        this.userComments.add(userComment);
        userComment.setParty(this);
        return this;
    }

    public Party removeUserComments(UserComment userComment) {
        this.userComments.remove(userComment);
        userComment.setParty(null);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setParty(null));
        }
        if (products != null) {
            products.forEach(i -> i.setParty(this));
        }
        this.products = products;
    }

    public Party products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Party addProducts(Product product) {
        this.products.add(product);
        product.setParty(this);
        return this;
    }

    public Party removeProducts(Product product) {
        this.products.remove(product);
        product.setParty(null);
        return this;
    }

    public Set<Agreement> getProviderAgreements() {
        return this.providerAgreements;
    }

    public void setProviderAgreements(Set<Agreement> agreements) {
        if (this.providerAgreements != null) {
            this.providerAgreements.forEach(i -> i.setProvider(null));
        }
        if (agreements != null) {
            agreements.forEach(i -> i.setProvider(this));
        }
        this.providerAgreements = agreements;
    }

    public Party providerAgreements(Set<Agreement> agreements) {
        this.setProviderAgreements(agreements);
        return this;
    }

    public Party addProviderAgreements(Agreement agreement) {
        this.providerAgreements.add(agreement);
        agreement.setProvider(this);
        return this;
    }

    public Party removeProviderAgreements(Agreement agreement) {
        this.providerAgreements.remove(agreement);
        agreement.setProvider(null);
        return this;
    }

    public Set<Agreement> getConsumerAgreements() {
        return this.consumerAgreements;
    }

    public void setConsumerAgreements(Set<Agreement> agreements) {
        if (this.consumerAgreements != null) {
            this.consumerAgreements.forEach(i -> i.setConsumer(null));
        }
        if (agreements != null) {
            agreements.forEach(i -> i.setConsumer(this));
        }
        this.consumerAgreements = agreements;
    }

    public Party consumerAgreements(Set<Agreement> agreements) {
        this.setConsumerAgreements(agreements);
        return this;
    }

    public Party addConsumerAgreements(Agreement agreement) {
        this.consumerAgreements.add(agreement);
        agreement.setConsumer(this);
        return this;
    }

    public Party removeConsumerAgreements(Agreement agreement) {
        this.consumerAgreements.remove(agreement);
        agreement.setConsumer(null);
        return this;
    }

    public Set<Contact> getContacts() {
        return this.contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        if (this.contacts != null) {
            this.contacts.forEach(i -> i.setParty(null));
        }
        if (contacts != null) {
            contacts.forEach(i -> i.setParty(this));
        }
        this.contacts = contacts;
    }

    public Party contacts(Set<Contact> contacts) {
        this.setContacts(contacts);
        return this;
    }

    public Party addContacts(Contact contact) {
        this.contacts.add(contact);
        contact.setParty(this);
        return this;
    }

    public Party removeContacts(Contact contact) {
        this.contacts.remove(contact);
        contact.setParty(null);
        return this;
    }

    public Set<Location> getLocations() {
        return this.locations;
    }

    public void setLocations(Set<Location> locations) {
        if (this.locations != null) {
            this.locations.forEach(i -> i.setParty(null));
        }
        if (locations != null) {
            locations.forEach(i -> i.setParty(this));
        }
        this.locations = locations;
    }

    public Party locations(Set<Location> locations) {
        this.setLocations(locations);
        return this;
    }

    public Party addLocations(Location location) {
        this.locations.add(location);
        location.setParty(this);
        return this;
    }

    public Party removeLocations(Location location) {
        this.locations.remove(location);
        location.setParty(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Party)) {
            return false;
        }
        return getId() != null && getId().equals(((Party) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Party{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", partyCode='" + getPartyCode() + "'" +
            ", tradeTitle='" + getTradeTitle() + "'" +
            ", activationDate='" + getActivationDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", activationStatus='" + getActivationStatus() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", personType='" + getPersonType() + "'" +
            "}";
    }
}
