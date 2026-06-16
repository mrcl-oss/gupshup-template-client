package io.github.mrcloss.gupshup.domain.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ButtonType {
  @JsonProperty("QUICK_REPLY")
  QUICK_REPLY,
  @JsonProperty("URL")
  URL,
  @JsonProperty("PHONE_NUMBER")
  PHONE_NUMBER,
  @JsonProperty("COPY_CODE")
  COPY_CODE,
  @JsonProperty("CATALOG")
  CATALOG,
  @JsonProperty("MPM")
  MPM,
  @JsonProperty("OTP")
  OTP,
}
