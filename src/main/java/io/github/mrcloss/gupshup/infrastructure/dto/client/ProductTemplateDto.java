package io.github.mrcloss.gupshup.infrastructure.dto.client;

import io.github.mrcloss.gupshup.domain.template.ProductTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductTemplateDto extends BaseTemplateDto {
  @Override
  public ProductTemplate toDomain() {
    ProductTemplate template =
        new ProductTemplate(
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
