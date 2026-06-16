package io.github.mrcloss.gupshup.infrastructure.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import io.github.mrcloss.gupshup.domain.template.LTOAttributes;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemplateRequest {
  private String appId;
  private TemplateCategory category;
  private String content;
  private String example;
  private String elementName;
  private LanguageCode languageCode;
  private TemplateParameterFormat parameterFormat;
  private TemplateType templateType;
  private String vertical;
  private String footer;
  private Integer messageValidity;
  private List<ButtonRequest> buttons;

  @JsonUnwrapped private LTOAttributes ltoAttributes;
}
