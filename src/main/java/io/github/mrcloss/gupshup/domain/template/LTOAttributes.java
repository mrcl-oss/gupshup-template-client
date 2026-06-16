package io.github.mrcloss.gupshup.domain.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/** Attributes for Limited Time Offer (LTO) templates. */
@Getter
@Setter
public class LTOAttributes {

  @JsonProperty("isLTO")
  private final boolean isLTO = true;

  private boolean hasExpiration;
  private String limitedOfferText;

  public LTOAttributes() {}

  public LTOAttributes(boolean hasExpiration, String limitedOfferText) {
    this.hasExpiration = hasExpiration;
    this.limitedOfferText = limitedOfferText;
  }
}
