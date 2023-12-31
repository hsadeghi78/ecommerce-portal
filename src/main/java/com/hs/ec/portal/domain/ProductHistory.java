package com.hs.ec.portal.domain;

import com.hs.ec.portal.domain.enumeration.Performance;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 4 field fixed
 * @author Hossein Sadeghi (hsadeghi78@gmail.com)
 * after per update on Product on object add to tfhis table. be handling in DFenging and view for this object Only for admin
 */
@Table("price_history")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductHistory implements Serializable {

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

    @NotNull(message = "must not be null")
    @Column("category_id")
    private Long categoryId;

    @NotNull(message = "must not be null")
    @Column("party_id")
    private Long partyId;

    @NotNull(message = "must not be null")
    @Column("product_id")
    private Long productId;

    @NotNull(message = "must not be null")
    @Column("price_id")
    private Long priceId;

    @Column("campaign_id")
    private Long campaignId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ProductHistory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTypeClassId() {
        return this.typeClassId;
    }

    public ProductHistory typeClassId(Long typeClassId) {
        this.setTypeClassId(typeClassId);
        return this;
    }

    public void setTypeClassId(Long typeClassId) {
        this.typeClassId = typeClassId;
    }

    public Long getBrandClassId() {
        return this.brandClassId;
    }

    public ProductHistory brandClassId(Long brandClassId) {
        this.setBrandClassId(brandClassId);
        return this;
    }

    public void setBrandClassId(Long brandClassId) {
        this.brandClassId = brandClassId;
    }

    public String getSizee() {
        return this.sizee;
    }

    public ProductHistory sizee(String sizee) {
        this.setSizee(sizee);
        return this;
    }

    public void setSizee(String sizee) {
        this.sizee = sizee;
    }

    public Long getRegularSizeClassId() {
        return this.regularSizeClassId;
    }

    public ProductHistory regularSizeClassId(Long regularSizeClassId) {
        this.setRegularSizeClassId(regularSizeClassId);
        return this;
    }

    public void setRegularSizeClassId(Long regularSizeClassId) {
        this.regularSizeClassId = regularSizeClassId;
    }

    public Long getLanguageClassId() {
        return this.languageClassId;
    }

    public ProductHistory languageClassId(Long languageClassId) {
        this.setLanguageClassId(languageClassId);
        return this;
    }

    public void setLanguageClassId(Long languageClassId) {
        this.languageClassId = languageClassId;
    }

    public String getDescription() {
        return this.description;
    }

    public ProductHistory description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public ProductHistory keywords(String keywords) {
        this.setKeywords(keywords);
        return this;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public byte[] getPhoto1() {
        return this.photo1;
    }

    public ProductHistory photo1(byte[] photo1) {
        this.setPhoto1(photo1);
        return this;
    }

    public void setPhoto1(byte[] photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto1ContentType() {
        return this.photo1ContentType;
    }

    public ProductHistory photo1ContentType(String photo1ContentType) {
        this.photo1ContentType = photo1ContentType;
        return this;
    }

    public void setPhoto1ContentType(String photo1ContentType) {
        this.photo1ContentType = photo1ContentType;
    }

    public Double getCount() {
        return this.count;
    }

    public ProductHistory count(Double count) {
        this.setCount(count);
        return this;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Float getDiscount() {
        return this.discount;
    }

    public ProductHistory discount(Float discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Double getOriginalPrice() {
        return this.originalPrice;
    }

    public ProductHistory originalPrice(Double originalPrice) {
        this.setOriginalPrice(originalPrice);
        return this;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getFinalPrice() {
        return this.finalPrice;
    }

    public ProductHistory finalPrice(Double finalPrice) {
        this.setFinalPrice(finalPrice);
        return this;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public LocalDate getPublishDate() {
        return this.publishDate;
    }

    public ProductHistory publishDate(LocalDate publishDate) {
        this.setPublishDate(publishDate);
        return this;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public LocalDate getTransportDate() {
        return this.transportDate;
    }

    public ProductHistory transportDate(LocalDate transportDate) {
        this.setTransportDate(transportDate);
        return this;
    }

    public void setTransportDate(LocalDate transportDate) {
        this.transportDate = transportDate;
    }

    public Long getCurrencyClassId() {
        return this.currencyClassId;
    }

    public ProductHistory currencyClassId(Long currencyClassId) {
        this.setCurrencyClassId(currencyClassId);
        return this;
    }

    public void setCurrencyClassId(Long currencyClassId) {
        this.currencyClassId = currencyClassId;
    }

    public Float getBonus() {
        return this.bonus;
    }

    public ProductHistory bonus(Float bonus) {
        this.setBonus(bonus);
        return this;
    }

    public void setBonus(Float bonus) {
        this.bonus = bonus;
    }

    public Long getWarrantyClassId() {
        return this.warrantyClassId;
    }

    public ProductHistory warrantyClassId(Long warrantyClassId) {
        this.setWarrantyClassId(warrantyClassId);
        return this;
    }

    public void setWarrantyClassId(Long warrantyClassId) {
        this.warrantyClassId = warrantyClassId;
    }

    public Long getDeliveryPlaceClassId() {
        return this.deliveryPlaceClassId;
    }

    public ProductHistory deliveryPlaceClassId(Long deliveryPlaceClassId) {
        this.setDeliveryPlaceClassId(deliveryPlaceClassId);
        return this;
    }

    public void setDeliveryPlaceClassId(Long deliveryPlaceClassId) {
        this.deliveryPlaceClassId = deliveryPlaceClassId;
    }

    public Long getPaymentPlaceClassId() {
        return this.paymentPlaceClassId;
    }

    public ProductHistory paymentPlaceClassId(Long paymentPlaceClassId) {
        this.setPaymentPlaceClassId(paymentPlaceClassId);
        return this;
    }

    public void setPaymentPlaceClassId(Long paymentPlaceClassId) {
        this.paymentPlaceClassId = paymentPlaceClassId;
    }

    public Performance getPerformance() {
        return this.performance;
    }

    public ProductHistory performance(Performance performance) {
        this.setPerformance(performance);
        return this;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    public Long getOriginalityClassId() {
        return this.originalityClassId;
    }

    public ProductHistory originalityClassId(Long originalityClassId) {
        this.setOriginalityClassId(originalityClassId);
        return this;
    }

    public void setOriginalityClassId(Long originalityClassId) {
        this.originalityClassId = originalityClassId;
    }

    public Float getSatisfaction() {
        return this.satisfaction;
    }

    public ProductHistory satisfaction(Float satisfaction) {
        this.setSatisfaction(satisfaction);
        return this;
    }

    public void setSatisfaction(Float satisfaction) {
        this.satisfaction = satisfaction;
    }

    public Boolean getUsed() {
        return this.used;
    }

    public ProductHistory used(Boolean used) {
        this.setUsed(used);
        return this;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public ProductHistory categoryId(Long categoryId) {
        this.setCategoryId(categoryId);
        return this;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getPartyId() {
        return this.partyId;
    }

    public ProductHistory partyId(Long partyId) {
        this.setPartyId(partyId);
        return this;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public Long getProductId() {
        return this.productId;
    }

    public ProductHistory productId(Long productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getPriceId() {
        return this.priceId;
    }

    public ProductHistory priceId(Long priceId) {
        this.setPriceId(priceId);
        return this;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    public Long getCampaignId() {
        return this.campaignId;
    }

    public ProductHistory campaignId(Long campaignId) {
        this.setCampaignId(campaignId);
        return this;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductHistory{" +
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
            ", categoryId=" + getCategoryId() +
            ", partyId=" + getPartyId() +
            ", productId=" + getProductId() +
            ", priceId=" + getPriceId() +
            ", campaignId=" + getCampaignId() +
            "}";
    }
}
