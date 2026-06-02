package io.github.mrcloss.gupshup.domain.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the official category classifications for Gupshup WhatsApp
 * templates.
 */
public enum TemplateCategory {
    /**
     * Templates used to send promotional offers, product announcements, and
     * marketing content.
     */
    @JsonProperty("MARKETING")
    MARKETING,

    /**
     * Templates used for One-Time Passwords (OTP) and account security
     * verification.
     */
    @JsonProperty("AUTHENTICATION")
    AUTHENTICATION,

    /**
     * Templates used to confirm transactions, provide updates, or send
     * notifications.
     */
    @JsonProperty("UTILITY")
    UTILITY
}
