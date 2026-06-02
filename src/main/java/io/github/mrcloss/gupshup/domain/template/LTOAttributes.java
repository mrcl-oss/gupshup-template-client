package io.github.mrcloss.gupshup.domain.template;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Attributes for Limited Time Offer (LTO) templates.
 */
public class LTOAttributes {
    
    @JsonProperty("isLTO")
    private final boolean isLTO = true;
    
    private boolean hasExpiration;
    private String limitedOfferText;

    public LTOAttributes() {
    }

    public LTOAttributes(boolean hasExpiration, String limitedOfferText) {
        this.hasExpiration = hasExpiration;
        this.limitedOfferText = limitedOfferText;
    }

    public boolean isLTO() {
        return isLTO;
    }

    public boolean isHasExpiration() {
        return hasExpiration;
    }

    public void setHasExpiration(boolean hasExpiration) {
        this.hasExpiration = hasExpiration;
    }

    public String getLimitedOfferText() {
        return limitedOfferText;
    }

    public void setLimitedOfferText(String limitedOfferText) {
        this.limitedOfferText = limitedOfferText;
    }
}
