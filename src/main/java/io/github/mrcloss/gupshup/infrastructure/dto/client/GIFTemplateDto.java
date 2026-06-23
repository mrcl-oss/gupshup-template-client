package io.github.mrcloss.gupshup.infrastructure.dto.client;

import io.github.mrcloss.gupshup.domain.template.GIFTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GIFTemplateDto extends MediaTemplateDto {
  @Override
  public GIFTemplate toDomain() {
    GIFTemplate template =
        new GIFTemplate(
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
