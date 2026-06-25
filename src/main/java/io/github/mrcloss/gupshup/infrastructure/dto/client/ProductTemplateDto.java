package io.github.mrcloss.gupshup.infrastructure.dto.client;

import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import io.github.mrcloss.gupshup.domain.template.ProductTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductTemplateDto extends BaseTemplateDto {

  /**
   * Default constructor. Explicitly sets the template type to maintain consistency during
   * programmatic instantiation.
   */
  public ProductTemplateDto() {
    super();
    this.setTemplateType(TemplateType.PRODUCT);
  }

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
