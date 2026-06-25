package io.github.mrcloss.gupshup.infrastructure.dto.client;

import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import io.github.mrcloss.gupshup.domain.template.DocumentTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DocumentTemplateDto extends MediaTemplateDto {

  /**
   * Default constructor. Explicitly sets the template type to maintain consistency during
   * programmatic instantiation.
   */
  public DocumentTemplateDto() {
    super();
    this.setTemplateType(TemplateType.DOCUMENT);
  }

  @Override
  public DocumentTemplate toDomain() {
    DocumentTemplate template =
        new DocumentTemplate(
            getElementName(),
            getLanguageCode(),
            getBody(),
            getVariableExamples(),
            getCategory(),
            getAppId(),
            getTags(),
            getParameterFormat());
    template.setMediaId(getMediaId());
    template.setMediaUrl(getMediaUrl());
    template.setFooter(getFooter());
    template.setButtons(getButtons());
    template.setMessageValidity(getMessageValidity());
    template.setLtoAttributes(getLtoAttributes());
    return template;
  }
}
