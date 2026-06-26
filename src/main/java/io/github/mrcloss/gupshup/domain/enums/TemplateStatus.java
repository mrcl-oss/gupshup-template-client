package io.github.mrcloss.gupshup.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Represents the current approval and operational lifecycle status of a template. */
public enum TemplateStatus {
  @JsonProperty("APPROVED")
  APPROVED,

  @JsonProperty("REJECTED")
  REJECTED,

  @JsonProperty("SUBMITTED")
  SUBMITTED,

  @JsonProperty("PENDING")
  PENDING,

  @JsonProperty("PAUSED")
  PAUSED,

  @JsonProperty("FAILED")
  FAILED,

  // Mapped to "DESACTIVATED" to respect the API payload while maintaining
  // standard English naming in code
  @JsonProperty("DESACTIVATED")
  DEACTIVATED,

  @JsonProperty("DISABLED")
  DISABLED,

  @JsonProperty("IN_REVIEW")
  IN_REVIEW,

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
  SANDBOX_REQUESTED;

  /**
   * Safely parses a template status string into a {@link TemplateStatus} enum.
   *
   * <p>This method is case-insensitive and handles common Gupshup/WhatsApp status variances such as
   * mapping "DESACTIVATED" to {@link #DEACTIVATED}.
   *
   * @param value the raw status string from the API
   * @return the mapped {@link TemplateStatus}, or null if the value is null or unrecognized
   */
  @JsonCreator
  public static TemplateStatus fromString(String value) {
    if (value == null) {
      return null;
    }
    String normalized = value.trim().toUpperCase();
    switch (normalized) {
      case "APPROVED":
        return APPROVED;
      case "REJECTED":
        return REJECTED;
      case "SUBMITTED":
        return SUBMITTED;
      case "PENDING":
        return PENDING;
      case "PAUSED":
        return PAUSED;
      case "FAILED":
        return FAILED;
      case "DESACTIVATED":
      case "DEACTIVATED":
        return DEACTIVATED;
      case "DISABLED":
        return DISABLED;
      case "IN_REVIEW":
        return IN_REVIEW;
      case "APPEALED":
        return APPEALED;
      case "LOCKED":
        return LOCKED;
      case "DELETED":
        return DELETED;
      case "FLAGGED":
        return FLAGGED;
      case "ARCHIVED":
        return ARCHIVED;
      case "REINSTATED":
        return REINSTATED;
      case "SANDBOX_REQUESTED":
        return SANDBOX_REQUESTED;
      default:
        try {
          return TemplateStatus.valueOf(normalized);
        } catch (Exception e) {
          return null;
        }
    }
  }
}
