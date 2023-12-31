package com.hs.ec.portal.service.dto;

import com.hs.ec.portal.domain.enumeration.Performance;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.hs.ec.portal.domain.ProductHistory} entity.
 */
@Schema(
    description = "4 field fixed\n@author Hossein Sadeghi (hsadeghi78@gmail.com)\nafter per update on Product on object add to tfhis table. be handling in DFenging and view for this object Only for admin"
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductHistoryDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 500)
    private String name;

    /**
     * Type is optional for some of kinds of product
     */
    @Schema(description = "Type is optional for some of kinds of product")
    private Long typeClassId;

    /**
     * brand of product such as SAMSUNG, APPLE, DELL, ....
     */
    @Schema(description = "brand of product such as SAMSUNG, APPLE, DELL, ....")
    private Long brandClassId;

    /**
     * bitwiz of sizes: 34,35,36,40,......
     */
    @Size(max = 100)
    @Schema(description = "bitwiz of sizes: 34,35,36,40,......")
    private String sizee;

    /**
     * XSMALL, SMALL, MEDIUM, LARG, XLARG, XXLARG, XXXLARG, FREE
     */
    @NotNull(message = "must not be null")
    @Schema(description = "XSMALL, SMALL, MEDIUM, LARG, XLARG, XXLARG, XXXLARG, FREE", required = true)
    private Long regularSizeClassId;

    /**
     * such as fa_IR, en_US, ar_AE, ar_SA and .....
     */
    @NotNull(message = "must not be null")
    @Schema(description = "such as fa_IR, en_US, ar_AE, ar_SA and .....", required = true)
    private Long languageClassId;

    @Size(max = 2500)
    private String description;

    /**
     * Comma Seprated keywords for search and tags
     */
    @Size(max = 500)
    @Schema(description = "Comma Seprated keywords for search and tags")
    private String keywords;

    @Lob
    private byte[] photo1;

    private String photo1ContentType;

    /**
     * Product Count as per Party
     */
    @NotNull(message = "must not be null")
    @Schema(description = "Product Count as per Party", required = true)
    private Double count;

    private Float discount;

    /**
     * original price is before discount
     */
    @NotNull(message = "must not be null")
    @Schema(description = "original price is before discount", required = true)
    private Double originalPrice;

    /**
     * producer price is after discount
     */
    @NotNull(message = "must not be null")
    @Schema(description = "producer price is after discount", required = true)
    private Double finalPrice;

    @NotNull(message = "must not be null")
    private LocalDate publishDate;

    /**
     * first time for deliver or send product
     */
    @NotNull(message = "must not be null")
    @Schema(description = "first time for deliver or send product", required = true)
    private LocalDate transportDate;

    /**
     * such as IRR,IRT, AED,USD and .....
     */
    @NotNull(message = "must not be null")
    @Schema(description = "such as IRR,IRT, AED,USD and .....", required = true)
    private Long currencyClassId;

    private Float bonus;

    /**
     * warrantyClass, ORIGINALITY, KARABI_KALA, BATRY, DURATION(1Year,2year)
     */
    @Schema(description = "warrantyClass, ORIGINALITY, KARABI_KALA, BATRY, DURATION(1Year,2year)")
    private Long warrantyClassId;

    /**
     * deliveryPlaceClass, ON_SITE, IN_STORE,CUSTOMER_ADDRESS
     */
    @Schema(description = "deliveryPlaceClass, ON_SITE, IN_STORE,CUSTOMER_ADDRESS")
    private Long deliveryPlaceClassId;

    /**
     * paymentPlaceClass, ONLINE,CREDIT_CARD, CASH_ON_DELIVERY, WALLET
     */
    @Schema(description = "paymentPlaceClass, ONLINE,CREDIT_CARD, CASH_ON_DELIVERY, WALLET")
    private Long paymentPlaceClassId;

    /**
     * keyfiyate va karaee mahsol
     */
    @Schema(description = "keyfiyate va karaee mahsol")
    private Performance performance;

    /**
     * originalityClass, ORIGINAL, HIGH_COPY,FAKE
     */
    @Schema(description = "originalityClass, ORIGINAL, HIGH_COPY,FAKE")
    private Long originalityClassId;

    /**
     * mizane rezayat
     */
    @Schema(description = "mizane rezayat")
    private Float satisfaction;

    /**
     * new or used product
     */
    @NotNull(message = "must not be null")
    @Schema(description = "new or used product", required = true)
    private Boolean used;

    @NotNull(message = "must not be null")
    private Long categoryId;

    @NotNull(message = "must not be null")
    private Long partyId;

    @NotNull(message = "must not be null")
    private Long productId;

    @NotNull(message = "must not be null")
    private Long priceId;

    private Long campaignId;

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

    public Long getTypeClassId() {
        return typeClassId;
    }

    public void setTypeClassId(Long typeClassId) {
        this.typeClassId = typeClassId;
    }

    public Long getBrandClassId() {
        return brandClassId;
    }

    public void setBrandClassId(Long brandClassId) {
        this.brandClassId = brandClassId;
    }

    public String getSizee() {
        return sizee;
    }

    public void setSizee(String sizee) {
        this.sizee = sizee;
    }

    public Long getRegularSizeClassId() {
        return regularSizeClassId;
    }

    public void setRegularSizeClassId(Long regularSizeClassId) {
        this.regularSizeClassId = regularSizeClassId;
    }

    public Long getLanguageClassId() {
        return languageClassId;
    }

    public void setLanguageClassId(Long languageClassId) {
        this.languageClassId = languageClassId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public byte[] getPhoto1() {
        return photo1;
    }

    public void setPhoto1(byte[] photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto1ContentType() {
        return photo1ContentType;
    }

    public void setPhoto1ContentType(String photo1ContentType) {
        this.photo1ContentType = photo1ContentType;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public LocalDate getTransportDate() {
        return transportDate;
    }

    public void setTransportDate(LocalDate transportDate) {
        this.transportDate = transportDate;
    }

    public Long getCurrencyClassId() {
        return currencyClassId;
    }

    public void setCurrencyClassId(Long currencyClassId) {
        this.currencyClassId = currencyClassId;
    }

    public Float getBonus() {
        return bonus;
    }

    public void setBonus(Float bonus) {
        this.bonus = bonus;
    }

    public Long getWarrantyClassId() {
        return warrantyClassId;
    }

    public void setWarrantyClassId(Long warrantyClassId) {
        this.warrantyClassId = warrantyClassId;
    }

    public Long getDeliveryPlaceClassId() {
        return deliveryPlaceClassId;
    }

    public void setDeliveryPlaceClassId(Long deliveryPlaceClassId) {
        this.deliveryPlaceClassId = deliveryPlaceClassId;
    }

    public Long getPaymentPlaceClassId() {
        return paymentPlaceClassId;
    }

    public void setPaymentPlaceClassId(Long paymentPlaceClassId) {
        this.paymentPlaceClassId = paymentPlaceClassId;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    public Long getOriginalityClassId() {
        return originalityClassId;
    }

    public void setOriginalityClassId(Long originalityClassId) {
        this.originalityClassId = originalityClassId;
    }

    public Float getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(Float satisfaction) {
        this.satisfaction = satisfaction;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductHistoryDTO)) {
            return false;
        }

        ProductHistoryDTO productHistoryDTO = (ProductHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductHistoryDTO{" +
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
