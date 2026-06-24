package io.github.mrcloss.gupshup.infrastructure.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.mrcloss.gupshup.domain.message.GupshupMessage;
import io.github.mrcloss.gupshup.domain.message.Mpm;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendTemplateRequest {

  private final String channel = "whatsapp";

  private String source;
  private String destination;

  @JsonProperty("src.name")
  private String srcName;

  @JsonIgnore private String templateId;

  @JsonIgnore private List<String> params;

  @JsonIgnore private Mpm mpm;

  private GupshupMessage message;

  @JsonProperty("template")
  public Map<String, Object> getNestedTemplateData() {
    Map<String, Object> templateObj = new HashMap<>();
    templateObj.put("id", this.templateId);
    templateObj.put("params", this.params != null ? this.params : Collections.emptyList());
    if (this.mpm != null) {
      templateObj.put("mpm", this.mpm);
    }
    return templateObj;
  }

  public SendTemplateRequest(
      String source,
      String destination,
      String srcName,
      String templateId,
      List<String> params,
      GupshupMessage message) {
    this.source = source;
    this.destination = destination;
    this.srcName = srcName;
    this.templateId = templateId;
    this.params = params;
    this.message = message;
  }

  public SendTemplateRequest(
      String source, String destination, String srcName, String templateId, List<String> params) {
    this(source, destination, srcName, templateId, params, (GupshupMessage) null);
  }

  public SendTemplateRequest(
      String source,
      String destination,
      String srcName,
      String templateId,
      List<String> params,
      Mpm mpm) {
    this(source, destination, srcName, templateId, params, (GupshupMessage) null);
    this.mpm = mpm;
  }

  /**
   * Helper static factory to create a template request for a Catalog template.
   *
   * @param source the WhatsApp Business number
   * @param destination the recipient's phone number
   * @param srcName the Gupshup app name
   * @param templateId the catalog template ID
   * @param bodyParams variables to fill in the template body (can be null/empty)
   * @param thumbnailProductId the product ID to be used as catalog thumbnail header (placed at the
   *     end of params)
   * @return a new SendTemplateRequest instance
   */
  public static SendTemplateRequest forCatalog(
      String source,
      String destination,
      String srcName,
      String templateId,
      List<String> bodyParams,
      String thumbnailProductId) {
    List<String> allParams =
        new ArrayList<>(bodyParams != null ? bodyParams : Collections.emptyList());
    if (thumbnailProductId != null) {
      allParams.add(thumbnailProductId);
    }
    return new SendTemplateRequest(source, destination, srcName, templateId, allParams);
  }
}
