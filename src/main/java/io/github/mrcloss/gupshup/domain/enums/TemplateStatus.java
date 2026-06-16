package io.github.mrcloss.gupshup.domain.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Represents the current approval and operational lifecycle status of a template. */
public enum TemplateStatus {
  @JsonProperty("APPROVED")
  APPROVED,

  @JsonProperty("REJECTED")
  REJECTED,

  @JsonProperty("SUBMITTED")
  SUBMITTED,

  @JsonProperty("PAUSED")
  PAUSED,

  @JsonProperty("FAILED")
  FAILED,

  // Mapped to "DESACTIVATED" to respect the API payload while maintaining
  // standard English naming in code
  @JsonProperty("DESACTIVATED")
  DEACTIVATED,

  @JsonProperty("APPEALED")
  APPEALED,

  @JsonProperty("LOCKED")
  LOCKED,

  @JsonProperty("DELETED")
  DELETED,

  @JsonProperty("FLAGGED")
  FLAGGED,

  @JsonProperty("ARCHIVED")
  ARCHIVED,

  @JsonProperty("REINSTATED")
  REINSTATED,

  @JsonProperty("SANDBOX_REQUESTED")
  SANDBOX_REQUESTED
}
