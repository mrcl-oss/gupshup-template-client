package io.github.mrcloss.gupshup.infrastructure.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/** Detailed information of a Gupshup WhatsApp template returned by the API. */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GupshupTemplate {
  private String id;
  private String appId;
  private String elementName;
  private String externalId;
  private String reason;
  private String languageCode;
  private String languagePolicy;
  private String oldCategory;
  private String category;
  private String internalCategory;
  private String templateType;
  private String internalType;
  private String status;
  private String state;
  private String quality;
  private String parameterFormat;
  private String vertical;
  private String data;
  private String namespace;
  private String wabaId;
  private String source;
  private String stage;
  private Long createdOn;
  private Long modifiedOn;
  private String buttonSupported;
  private Integer priority;
  private Integer retry;
  private String meta;
  private String containerMeta;
}
