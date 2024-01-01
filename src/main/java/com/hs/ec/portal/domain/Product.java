package com.hs.ec.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hs.ec.portal.domain.enumeration.Performance;
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
 * 4 field fixed and Party, prices, items
 * @author Hossein Sadeghi (hsadeghi78@gmail.com)
 */
@Table("product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 500)
    @Column("name")
    private String name;

    /**
     * Type is optional for some of kinds of product
     */
    @Column("type_class_id")
    private Long typeClassId;

    /**
     * brand of product such as SAMSUNG, APPLE, DELL, ....
     */
    @Column("brand_class_id")
    private Long brandClassId;

    /**
     * bitwiz of sizes: 34,35,36,40,......
     */
    @Size(max = 100)
    @Column("sizee")
    private String sizee;

    /**
     * XSMALL, SMALL, MEDIUM, LARG, XLARG, XXLARG, XXXLARG, FREE
     */
    @NotNull(message = "must not be null")
    @Column("regular_size_class_id")
    private Long regularSizeClassId;

    /**
     * such as fa_IR, en_US, ar_AE, ar_SA and .....
     */
    @NotNull(message = "must not be null")
    @Column("language_class_id")
    private Long languageClassId;

    @Size(max = 2500)
    @Column("description")
    private String description;

    /**
     * Comma Seprated keywords for search and tags
     */
    @Size(max = 500)
    @Column("keywords")
    private String keywords;

    @Column("photo_1")
    private byte[] photo1;

    @NotNull
    @Column("photo_1_content_type")
    private String photo1ContentType;

    /**
     * Iranian, Indian , ......
     */
    @Column("nationality_class_id")
    private Long nationalityClassId;

    /**
     * Product Count as per Party
     */
    @NotNull(message = "must not be null")
    @Column("count")
    private Double count;

    @Column("discount")
    private Float discount;

    /**
     * original price is before discount
     */
    @NotNull(message = "must not be null")
    @Column("original_price")
    private Double originalPrice;

    /**
     * producer price is after discount
     */
    @NotNull(message = "must not be null")
    @Column("final_price")
    private Double finalPrice;

    @NotNull(message = "must not be null")
    @Column("publish_date")
    private LocalDate publishDate;

    /**
     * first time for deliver or send product
     */
    @NotNull(message = "must not be null")
    @Column("transport_date")
    private LocalDate transportDate;

    /**
     * such as IRR,IRT, AED,USD and .....
     */
    @NotNull(message = "must not be null")
    @Column("currency_class_id")
    private Long currencyClassId;

    @Column("bonus")
    private Float bonus;

    /**
     * warrantyClass, ORIGINALITY, KARABI_KALA, BATRY, DURATION(1Year,2year)
     */
    @Column("warranty_class_id")
    private Long warrantyClassId;

    /**
     * deliveryPlaceClass, ON_SITE, IN_STORE,CUSTOMER_ADDRESS
     */
    @Column("delivery_place_class_id")
    private Long deliveryPlaceClassId;

    /**
     * paymentPlaceClass, ONLINE,CREDIT_CARD, CASH_ON_DELIVERY, WALLET
     */
    @Column("payment_place_class_id")
    private Long paymentPlaceClassId;

    /**
     * keyfiyate va karaee mahsol
     */
    @Column("performance")
    private Performance performance;

    /**
     * originalityClass, ORIGINAL, HIGH_COPY,FAKE
     */
    @Column("originality_class_id")
    private Long originalityClassId;

    /**
     * mizane rezayat
     */
    @Column("satisfaction")
    private Float satisfaction;

    /**
     * new or used product
     */
    @NotNull(message = "must not be null")
    @Column("used")
    private Boolean used;

    @Transient
    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    private Set<ProductItem> productItems = new HashSet<>();

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
    private Set<Product> children = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    private Set<UserFavorite> favorites = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    private Set<ConsumeMaterial> materials = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "factor", "product" }, allowSetters = true)
    private Set<FactorItem> factorItems = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "prices" }, allowSetters = true)
    private Set<FileDocument> documents = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "products", "children", "parent" }, allowSetters = true)
    private Category category;

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
    private Product parent;

    @Transient
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Set<Campaign> campaigns = new HashSet<>();

    @Column("category_id")
    private Long categoryId;

    @Column("party_id")
    private Long partyId;

    @Column("parent_id")
    private Long parentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTypeClassId() {
        return this.typeClassId;
    }

    public Product typeClassId(Long typeClassId) {
        this.setTypeClassId(typeClassId);
        return this;
    }

    public void setTypeClassId(Long typeClassId) {
        this.typeClassId = typeClassId;
    }

    public Long getBrandClassId() {
        return this.brandClassId;
    }

    public Product brandClassId(Long brandClassId) {
        this.setBrandClassId(brandClassId);
        return this;
    }

    public void setBrandClassId(Long brandClassId) {
        this.brandClassId = brandClassId;
    }

    public String getSizee() {
        return this.sizee;
    }

    public Product sizee(String sizee) {
        this.setSizee(sizee);
        return this;
    }

    public void setSizee(String sizee) {
        this.sizee = sizee;
    }

    public Long getRegularSizeClassId() {
        return this.regularSizeClassId;
    }

    public Product regularSizeClassId(Long regularSizeClassId) {
        this.setRegularSizeClassId(regularSizeClassId);
        return this;
    }

    public void setRegularSizeClassId(Long regularSizeClassId) {
        this.regularSizeClassId = regularSizeClassId;
    }

    public Long getLanguageClassId() {
        return this.languageClassId;
    }

    public Product languageClassId(Long languageClassId) {
        this.setLanguageClassId(languageClassId);
        return this;
    }

    public void setLanguageClassId(Long languageClassId) {
        this.languageClassId = languageClassId;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public Product keywords(String keywords) {
        this.setKeywords(keywords);
        return this;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public byte[] getPhoto1() {
        return this.photo1;
    }

    public Product photo1(byte[] photo1) {
        this.setPhoto1(photo1);
        return this;
    }

    public void setPhoto1(byte[] photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto1ContentType() {
        return this.photo1ContentType;
    }

    public Product photo1ContentType(String photo1ContentType) {
        this.photo1ContentType = photo1ContentType;
        return this;
    }

    public void setPhoto1ContentType(String photo1ContentType) {
        this.photo1ContentType = photo1ContentType;
    }

    public Long getNationalityClassId() {
        return this.nationalityClassId;
    }

    public Product nationalityClassId(Long nationalityClassId) {
        this.setNationalityClassId(nationalityClassId);
        return this;
    }

    public void setNationalityClassId(Long nationalityClassId) {
        this.nationalityClassId = nationalityClassId;
    }

    public Double getCount() {
        return this.count;
    }

    public Product count(Double count) {
        this.setCount(count);
        return this;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Float getDiscount() {
        return this.discount;
    }

    public Product discount(Float discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Double getOriginalPrice() {
        return this.originalPrice;
    }

    public Product originalPrice(Double originalPrice) {
        this.setOriginalPrice(originalPrice);
        return this;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getFinalPrice() {
        return this.finalPrice;
    }

    public Product finalPrice(Double finalPrice) {
        this.setFinalPrice(finalPrice);
        return this;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public LocalDate getPublishDate() {
        return this.publishDate;
    }

    public Product publishDate(LocalDate publishDate) {
        this.setPublishDate(publishDate);
        return this;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public LocalDate getTransportDate() {
        return this.transportDate;
    }

    public Product transportDate(LocalDate transportDate) {
        this.setTransportDate(transportDate);
        return this;
    }

    public void setTransportDate(LocalDate transportDate) {
        this.transportDate = transportDate;
    }

    public Long getCurrencyClassId() {
        return this.currencyClassId;
    }

    public Product currencyClassId(Long currencyClassId) {
        this.setCurrencyClassId(currencyClassId);
        return this;
    }

    public void setCurrencyClassId(Long currencyClassId) {
        this.currencyClassId = currencyClassId;
    }

    public Float getBonus() {
        return this.bonus;
    }

    public Product bonus(Float bonus) {
        this.setBonus(bonus);
        return this;
    }

    public void setBonus(Float bonus) {
        this.bonus = bonus;
    }

    public Long getWarrantyClassId() {
        return this.warrantyClassId;
    }

    public Product warrantyClassId(Long warrantyClassId) {
        this.setWarrantyClassId(warrantyClassId);
        return this;
    }

    public void setWarrantyClassId(Long warrantyClassId) {
        this.warrantyClassId = warrantyClassId;
    }

    public Long getDeliveryPlaceClassId() {
        return this.deliveryPlaceClassId;
    }

    public Product deliveryPlaceClassId(Long deliveryPlaceClassId) {
        this.setDeliveryPlaceClassId(deliveryPlaceClassId);
        return this;
    }

    public void setDeliveryPlaceClassId(Long deliveryPlaceClassId) {
        this.deliveryPlaceClassId = deliveryPlaceClassId;
    }

    public Long getPaymentPlaceClassId() {
        return this.paymentPlaceClassId;
    }

    public Product paymentPlaceClassId(Long paymentPlaceClassId) {
        this.setPaymentPlaceClassId(paymentPlaceClassId);
        return this;
    }

    public void setPaymentPlaceClassId(Long paymentPlaceClassId) {
        this.paymentPlaceClassId = paymentPlaceClassId;
    }

    public Performance getPerformance() {
        return this.performance;
    }

    public Product performance(Performance performance) {
        this.setPerformance(performance);
        return this;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    public Long getOriginalityClassId() {
        return this.originalityClassId;
    }

    public Product originalityClassId(Long originalityClassId) {
        this.setOriginalityClassId(originalityClassId);
        return this;
    }

    public void setOriginalityClassId(Long originalityClassId) {
        this.originalityClassId = originalityClassId;
    }

    public Float getSatisfaction() {
        return this.satisfaction;
    }

    public Product satisfaction(Float satisfaction) {
        this.setSatisfaction(satisfaction);
        return this;
    }

    public void setSatisfaction(Float satisfaction) {
        this.satisfaction = satisfaction;
    }

    public Boolean getUsed() {
        return this.used;
    }

    public Product used(Boolean used) {
        this.setUsed(used);
        return this;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public Set<ProductItem> getProductItems() {
        return this.productItems;
    }

    public void setProductItems(Set<ProductItem> productItems) {
        if (this.productItems != null) {
            this.productItems.forEach(i -> i.setProduct(null));
        }
        if (productItems != null) {
            productItems.forEach(i -> i.setProduct(this));
        }
        this.productItems = productItems;
    }

    public Product productItems(Set<ProductItem> productItems) {
        this.setProductItems(productItems);
        return this;
    }

    public Product addProductItems(ProductItem productItem) {
        this.productItems.add(productItem);
        productItem.setProduct(this);
        return this;
    }

    public Product removeProductItems(ProductItem productItem) {
        this.productItems.remove(productItem);
        productItem.setProduct(null);
        return this;
    }

    public Set<UserComment> getUserComments() {
        return this.userComments;
    }

    public void setUserComments(Set<UserComment> userComments) {
        if (this.userComments != null) {
            this.userComments.forEach(i -> i.setProduct(null));
        }
        if (userComments != null) {
            userComments.forEach(i -> i.setProduct(this));
        }
        this.userComments = userComments;
    }

    public Product userComments(Set<UserComment> userComments) {
        this.setUserComments(userComments);
        return this;
    }

    public Product addUserComments(UserComment userComment) {
        this.userComments.add(userComment);
        userComment.setProduct(this);
        return this;
    }

    public Product removeUserComments(UserComment userComment) {
        this.userComments.remove(userComment);
        userComment.setProduct(null);
        return this;
    }

    public Set<Product> getChildren() {
        return this.children;
    }

    public void setChildren(Set<Product> products) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (products != null) {
            products.forEach(i -> i.setParent(this));
        }
        this.children = products;
    }

    public Product children(Set<Product> products) {
        this.setChildren(products);
        return this;
    }

    public Product addChildren(Product product) {
        this.children.add(product);
        product.setParent(this);
        return this;
    }

    public Product removeChildren(Product product) {
        this.children.remove(product);
        product.setParent(null);
        return this;
    }

    public Set<UserFavorite> getFavorites() {
        return this.favorites;
    }

    public void setFavorites(Set<UserFavorite> userFavorites) {
        if (this.favorites != null) {
            this.favorites.forEach(i -> i.setProduct(null));
        }
        if (userFavorites != null) {
            userFavorites.forEach(i -> i.setProduct(this));
        }
        this.favorites = userFavorites;
    }

    public Product favorites(Set<UserFavorite> userFavorites) {
        this.setFavorites(userFavorites);
        return this;
    }

    public Product addFavorites(UserFavorite userFavorite) {
        this.favorites.add(userFavorite);
        userFavorite.setProduct(this);
        return this;
    }

    public Product removeFavorites(UserFavorite userFavorite) {
        this.favorites.remove(userFavorite);
        userFavorite.setProduct(null);
        return this;
    }

    public Set<ConsumeMaterial> getMaterials() {
        return this.materials;
    }

    public void setMaterials(Set<ConsumeMaterial> consumeMaterials) {
        if (this.materials != null) {
            this.materials.forEach(i -> i.setProduct(null));
        }
        if (consumeMaterials != null) {
            consumeMaterials.forEach(i -> i.setProduct(this));
        }
        this.materials = consumeMaterials;
    }

    public Product materials(Set<ConsumeMaterial> consumeMaterials) {
        this.setMaterials(consumeMaterials);
        return this;
    }

    public Product addMaterials(ConsumeMaterial consumeMaterial) {
        this.materials.add(consumeMaterial);
        consumeMaterial.setProduct(this);
        return this;
    }

    public Product removeMaterials(ConsumeMaterial consumeMaterial) {
        this.materials.remove(consumeMaterial);
        consumeMaterial.setProduct(null);
        return this;
    }

    public Set<FactorItem> getFactorItems() {
        return this.factorItems;
    }

    public void setFactorItems(Set<FactorItem> factorItems) {
        if (this.factorItems != null) {
            this.factorItems.forEach(i -> i.setProduct(null));
        }
        if (factorItems != null) {
            factorItems.forEach(i -> i.setProduct(this));
        }
        this.factorItems = factorItems;
    }

    public Product factorItems(Set<FactorItem> factorItems) {
        this.setFactorItems(factorItems);
        return this;
    }

    public Product addFactorItems(FactorItem factorItem) {
        this.factorItems.add(factorItem);
        factorItem.setProduct(this);
        return this;
    }

    public Product removeFactorItems(FactorItem factorItem) {
        this.factorItems.remove(factorItem);
        factorItem.setProduct(null);
        return this;
    }

    public Set<FileDocument> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<FileDocument> fileDocuments) {
        this.documents = fileDocuments;
    }

    public Product documents(Set<FileDocument> fileDocuments) {
        this.setDocuments(fileDocuments);
        return this;
    }

    public Product addDocuments(FileDocument fileDocument) {
        this.documents.add(fileDocument);
        return this;
    }

    public Product removeDocuments(FileDocument fileDocument) {
        this.documents.remove(fileDocument);
        return this;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
        this.categoryId = category != null ? category.getId() : null;
    }

    public Product category(Category category) {
        this.setCategory(category);
        return this;
    }

    public Party getParty() {
        return this.party;
    }

    public void setParty(Party party) {
        this.party = party;
        this.partyId = party != null ? party.getId() : null;
    }

    public Product party(Party party) {
        this.setParty(party);
        return this;
    }

    public Product getParent() {
        return this.parent;
    }

    public void setParent(Product product) {
        this.parent = product;
        this.parentId = product != null ? product.getId() : null;
    }

    public Product parent(Product product) {
        this.setParent(product);
        return this;
    }

    public Set<Campaign> getCampaigns() {
        return this.campaigns;
    }

    public void setCampaigns(Set<Campaign> campaigns) {
        if (this.campaigns != null) {
            this.campaigns.forEach(i -> i.removeProducts(this));
        }
        if (campaigns != null) {
            campaigns.forEach(i -> i.addProducts(this));
        }
        this.campaigns = campaigns;
    }

    public Product campaigns(Set<Campaign> campaigns) {
        this.setCampaigns(campaigns);
        return this;
    }

    public Product addCampaigns(Campaign campaign) {
        this.campaigns.add(campaign);
        campaign.getProducts().add(this);
        return this;
    }

    public Product removeCampaigns(Campaign campaign) {
        this.campaigns.remove(campaign);
        campaign.getProducts().remove(this);
        return this;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Long category) {
        this.categoryId = category;
    }

    public Long getPartyId() {
        return this.partyId;
    }

    public void setPartyId(Long party) {
        this.partyId = party;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long product) {
        this.parentId = product;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", typeClassId=" + getTypeClassId() +
            ", brandClassId=" + getBrandClassId() +
            ", sizee='" + getSizee() + "'" +
            ", regularSizeClassId=" + getRegularSizeClassId() +
            ", languageClassId=" + getLanguageClassId() +
            ", description='" + getDescription() + "'" +
            ", keywords='" + getKeywords() + "'" +
            ", photo1='" + getPhoto1() + "'" +
            ", photo1ContentType='" + getPhoto1ContentType() + "'" +
            ", nationalityClassId=" + getNationalityClassId() +
            ", count=" + getCount() +
            ", discount=" + getDiscount() +
            ", originalPrice=" + getOriginalPrice() +
            ", finalPrice=" + getFinalPrice() +
            ", publishDate='" + getPublishDate() + "'" +
            ", transportDate='" + getTransportDate() + "'" +
            ", currencyClassId=" + getCurrencyClassId() +
            ", bonus=" + getBonus() +
            ", warrantyClassId=" + getWarrantyClassId() +
            ", deliveryPlaceClassId=" + getDeliveryPlaceClassId() +
            ", paymentPlaceClassId=" + getPaymentPlaceClassId() +
            ", performance='" + getPerformance() + "'" +
            ", originalityClassId=" + getOriginalityClassId() +
            ", satisfaction=" + getSatisfaction() +
            ", used='" + getUsed() + "'" +
            "}";
    }
}
