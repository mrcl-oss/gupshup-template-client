package io.github.mrcloss.gupshup.infrastructure.dto.client;

import io.github.mrcloss.gupshup.domain.template.CatalogTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CatalogTemplateDto extends BaseTemplateDto {
  @Override
  public CatalogTemplate toDomain() {
    CatalogTemplate template =
        new CatalogTemplate(
            getElementName(),
            getLanguageCode(),
            getBody(),
            getVariableExamples(),
            getCategory(),
            getAppId(),
            getTags(),
            getParameterFormat());
    template.setFooter(getFooter());
    template.setMessageValidity(getMessageValidity());
    return template;
  }
}
