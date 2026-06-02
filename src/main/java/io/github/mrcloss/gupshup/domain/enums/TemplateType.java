package io.github.mrcloss.gupshup.domain.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the structural type and media format of the template.
 */
public enum TemplateType {

    @JsonProperty("TEXT")
    TEXT,

    @JsonProperty("IMAGE")
    IMAGE,

    @JsonProperty("VIDEO")
    VIDEO,

    @JsonProperty("DOCUMENT")
    DOCUMENT,

    @JsonProperty("LOCATION")
    LOCATION,

    @JsonProperty("GIF")
    GIF,

    @JsonProperty("CATALOG")
    CATALOG,

    @JsonProperty("PRODUCT")
    PRODUCT,

    @JsonProperty("CAROUSEL")
    CAROUSEL
}
