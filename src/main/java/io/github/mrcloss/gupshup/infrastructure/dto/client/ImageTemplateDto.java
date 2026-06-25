package io.github.mrcloss.gupshup.infrastructure.dto.client;

import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import io.github.mrcloss.gupshup.domain.template.ImageTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ImageTemplateDto extends MediaTemplateDto {

  /**
   * Default constructor. Explicitly sets the template type to maintain consistency during
   * programmatic instantiation.
   */
  public ImageTemplateDto() {
    super();
    this.setTemplateType(TemplateType.IMAGE);
  }

  @Override
  public ImageTemplate toDomain() {
    ImageTemplate template =
        new ImageTemplate(
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
