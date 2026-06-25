package io.github.mrcloss.gupshup.infrastructure.dto.client;

import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import io.github.mrcloss.gupshup.domain.template.CatalogTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** DTO representation for a Catalog Template. */
@Data
@EqualsAndHashCode(callSuper = true)
public class CatalogTemplateDto extends BaseTemplateDto {

  /**
   * Default constructor. Explicitly sets the template type to maintain consistency during
   * programmatic instantiation.
   */
  public CatalogTemplateDto() {
    super();
    this.setTemplateType(TemplateType.CATALOG);
  }

  /**
   * Maps this DTO to its corresponding Domain entity.
   *
   * @return A valid CatalogTemplate domain instance.
   */
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
