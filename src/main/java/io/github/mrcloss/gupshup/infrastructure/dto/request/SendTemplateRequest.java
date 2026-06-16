package io.github.mrcloss.gupshup.infrastructure.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.mrcloss.gupshup.domain.message.GupshupMessage;
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

  private GupshupMessage message;

  @JsonProperty("template")
  public Map<String, Object> getNestedTemplateData() {
    Map<String, Object> templateObj = new HashMap<>();
    templateObj.put("id", this.templateId);
    templateObj.put("params", this.params != null ? this.params : Collections.emptyList());
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
    this(source, destination, srcName, templateId, params, null);
  }
}
