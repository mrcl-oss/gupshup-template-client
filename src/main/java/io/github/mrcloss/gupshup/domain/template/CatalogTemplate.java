package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.button.Button;
import io.github.mrcloss.gupshup.domain.button.CatalogButton;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a WhatsApp Catalog template.
 *
 * <p>Catalog templates are designed to display a shop inventory button. They include exactly one
 * static {@link CatalogButton} ("View catalog"), which is automatically added. Custom buttons
 * cannot be added or modified on a Catalog template.
 */
@Getter
@Setter
public class CatalogTemplate extends Template {
  /**
   * Constructs a new CatalogTemplate without variable examples.
   *
   * @param elementName the unique name of the template (alphanumeric, lowercase)
   * @param languageCode the language of the template
   * @param body the template message body text
   * @param category the category of the template
   * @param appId the Gupshup app ID
   * @param tags optional list of tag labels for the template
   * @param parameterFormat the format of parameters
   */
  public CatalogTemplate(
      String elementName,
      LanguageCode languageCode,
      String body,
      TemplateCategory category,
      String appId,
      List<String> tags,
      TemplateParameterFormat parameterFormat) {
    this(elementName, languageCode, body, null, category, appId, tags, parameterFormat);
  }

  public CatalogTemplate(
      String elementName,
      LanguageCode languageCode,
      String body,
      List<String> variableExamples,
      TemplateCategory category,
      String appId,
      List<String> tags,
      TemplateParameterFormat parameterFormat) {
    super(
        elementName,
        languageCode,
        body,
        variableExamples,
        category,
        appId,
        tags,
        TemplateType.CATALOG,
        parameterFormat);
    super.setButtons(Collections.singletonList(new CatalogButton("View catalog")));
  }

  @Override
  public void setButtons(List<Button> buttons) {
    throw new UnsupportedOperationException("Catalog template buttons cannot be modified");
  }

  @Override
  public void addButton(Button button) {
    throw new UnsupportedOperationException("Catalog template buttons cannot be modified");
  }
}
