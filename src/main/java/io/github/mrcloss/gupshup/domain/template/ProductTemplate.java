package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.button.Button;
import io.github.mrcloss.gupshup.domain.button.MPMButton;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a WhatsApp Product template.
 *
 * <p>Product templates are designed to show a specific product or a list of products (Multi-Product
 * Message). They include exactly one static {@link MPMButton} ("View items"), which is
 * automatically added. Custom buttons cannot be added or modified on a Product template.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class ProductTemplate extends Template {
  /**
   * Constructs a new ProductTemplate without variable examples.
   *
   * @param elementName the unique name of the template (alphanumeric, lowercase)
   * @param languageCode the language of the template
   * @param body the template message body text
   * @param category the category of the template
   * @param appId the Gupshup app ID
   * @param tags optional list of tag labels for the template
   * @param parameterFormat the format of parameters
   */
  public ProductTemplate(
      String elementName,
      LanguageCode languageCode,
      String body,
      TemplateCategory category,
      String appId,
      List<String> tags,
      TemplateParameterFormat parameterFormat) {
    this(elementName, languageCode, body, null, category, appId, tags, parameterFormat);
  }

  public ProductTemplate(
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
        TemplateType.PRODUCT,
        parameterFormat);
    super.setButtons(Collections.singletonList(new MPMButton("View items")));
  }

  @Override
  public void setButtons(List<Button> buttons) {
    throw new UnsupportedOperationException("Product template buttons cannot be modified");
  }

  @Override
  public void addButton(Button button) {
    throw new UnsupportedOperationException("Product template buttons cannot be modified");
  }
}
