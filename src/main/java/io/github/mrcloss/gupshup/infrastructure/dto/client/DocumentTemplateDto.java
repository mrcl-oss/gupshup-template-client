package io.github.mrcloss.gupshup.infrastructure.dto.client;

import io.github.mrcloss.gupshup.domain.template.DocumentTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DocumentTemplateDto extends MediaTemplateDto {
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
