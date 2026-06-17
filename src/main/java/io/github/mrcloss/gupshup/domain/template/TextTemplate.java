package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.button.PayNowButton;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a text-only WhatsApp template.
 *
 * <p>Unlike media templates, a text template may optionally contain a plain text header with at
 * most 1 variable placeholder (e.g. {@code Hello {{1}}}).
 */
@Getter
@Setter
public class TextTemplate extends Template {
  @Setter(AccessLevel.NONE)
  private String header;

  private List<String> variableHeaderExamples;

  /**
   * Constructs a new TextTemplate without variable examples or header.
   *
   * @param elementName the unique name of the template (alphanumeric, lowercase)
   * @param languageCode the language of the template
   * @param body the template message body text
   * @param category the category of the template
   * @param appId the Gupshup app ID
   * @param tags optional list of tag labels for the template
   * @param parameterFormat the format of parameters
   */
  public TextTemplate(
      String elementName,
      LanguageCode languageCode,
      String body,
      TemplateCategory category,
      String appId,
      List<String> tags,
      TemplateParameterFormat parameterFormat) {
    super(
        elementName, languageCode, body, category, appId, tags, TemplateType.TEXT, parameterFormat);
  }

  public TextTemplate(
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
        TemplateType.TEXT,
        parameterFormat);
  }

  public TextTemplate(
      String elementName,
      LanguageCode languageCode,
      String body,
      TemplateCategory category,
      String appId,
      List<String> tags,
      TemplateParameterFormat parameterFormat,
      String header) {
    super(
        elementName, languageCode, body, category, appId, tags, TemplateType.TEXT, parameterFormat);
    this.header = header;
  }

  public TextTemplate(
      String elementName,
      LanguageCode languageCode,
      String body,
      List<String> variableExamples,
      TemplateCategory category,
      String appId,
      List<String> tags,
      TemplateParameterFormat parameterFormat,
      String header) {
    super(
        elementName,
        languageCode,
        body,
        variableExamples,
        category,
        appId,
        tags,
        TemplateType.TEXT,
        parameterFormat);
    this.header = header;
  }

  public TextTemplate(
      String elementName,
      LanguageCode languageCode,
      String body,
      TemplateCategory category,
      String appId,
      List<String> tags,
      TemplateParameterFormat parameterFormat,
      String header,
      List<String> variableHeaderExamples) {
    super(
        elementName, languageCode, body, category, appId, tags, TemplateType.TEXT, parameterFormat);
    this.header = header;
    this.variableHeaderExamples = variableHeaderExamples;
  }

  public TextTemplate(
      String elementName,
      LanguageCode languageCode,
      String body,
      List<String> variableExamples,
      TemplateCategory category,
      String appId,
      List<String> tags,
      TemplateParameterFormat parameterFormat,
      String header,
      List<String> variableHeaderExamples) {
    super(
        elementName,
        languageCode,
        body,
        variableExamples,
        category,
        appId,
        tags,
        TemplateType.TEXT,
        parameterFormat);
    this.header = header;
    this.variableHeaderExamples = variableHeaderExamples;
  }

  /**
   * Sets the header text for the text template.
   *
   * @param header the header text, can contain at most 1 variable placeholder
   * @throws IllegalArgumentException if the header contains more than 1 variable placeholder
   */
  public void setHeader(String header) {
    if (header != null) {
      Matcher matcher = Pattern.compile("\\{\\{\\d+\\}\\}").matcher(header);
      int count = 0;
      while (matcher.find()) {
        count++;
      }
      if (count > 1) {
        throw new IllegalArgumentException("Text template header can have at most 1 variable");
      }
    }
    this.header = header;
  }

  @Override
  public void validate() {
    super.validate();
    boolean hasPayNow = getButtons().stream().anyMatch(b -> b instanceof PayNowButton);
    if (hasPayNow && header != null && !header.trim().isEmpty()) {
      throw new IllegalStateException("Text templates with Pay Now button cannot have a header");
    }

    boolean hasHeaderExamples = variableHeaderExamples != null && !variableHeaderExamples.isEmpty();
    boolean hasVariablesInHeader =
        header != null && Pattern.compile("\\{\\{\\d+\\}\\}").matcher(header).find();

    if (hasVariablesInHeader && !hasHeaderExamples) {
      throw new IllegalStateException(
          "Header cannot contain variables if variable header examples are not provided");
    }

    if (hasHeaderExamples) {
      if (header == null) {
        throw new IllegalStateException(
            "Header is required when variable header examples are provided");
      }
      for (int i = 1; i <= variableHeaderExamples.size(); i++) {
        String placeholder = "{{" + i + "}}";
        if (!header.contains(placeholder)) {
          throw new IllegalStateException(
              "Header must contain "
                  + placeholder
                  + " when "
                  + variableHeaderExamples.size()
                  + " variable header examples are provided");
        }
      }
    }
  }
}
