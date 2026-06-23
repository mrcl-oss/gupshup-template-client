package io.github.mrcloss.gupshup.domain.template;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.github.mrcloss.gupshup.domain.button.Button;
import io.github.mrcloss.gupshup.domain.button.CatalogButton;
import io.github.mrcloss.gupshup.domain.button.MPMButton;
import io.github.mrcloss.gupshup.domain.button.OTPButton;
import io.github.mrcloss.gupshup.domain.button.PayNowButton;
import io.github.mrcloss.gupshup.domain.enums.ButtonType;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateStatus;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
/**
 * Represents a base WhatsApp message template in the Gupshup API.
 *
 * <p>A template contains structural information such as language code, category, body text,
 * sequential variable placeholders (e.g. {@code {{1}}}), footer text, and interactive buttons.
 *
 * <p>All templates are validated locally before submission to guarantee they comply with Gupshup
 * and Meta API guidelines.
 */
public class Template {
  private String appId;
  private TemplateStatus status;
  private TemplateCategory category;
  private Instant createdOn;
  private Instant modifiedOn;
  private String body; // data / content
  private List<String> variableExamples;
  private String elementName;
  private LanguageCode languageCode;
  private TemplateParameterFormat parameterFormat;
  private TemplateType templateType;
  private List<String> tags; // vertical
  private String reason;
  private String footer;
  private Integer messageValidity;
  private List<Button> buttons = new ArrayList<>();

  @JsonUnwrapped private LTOAttributes ltoAttributes;

  /**
   * Constructs a new Template without variable examples.
   *
   * @param elementName the unique name of the template (alphanumeric, lowercase)
   * @param languageCode the language of the template
   * @param body the template message body text
   * @param category the category of the template (e.g. MARKETING, UTILITY, AUTHENTICATION)
   * @param appId the Gupshup app ID
   * @param tags optional list of tag labels for the template
   * @param templateType the type of the template (e.g. TEXT, IMAGE)
   * @param parameterFormat the format of parameters (POSITIONAL or NAMED)
   */
  public Template(
      String elementName,
      LanguageCode languageCode,
      String body,
      TemplateCategory category,
      String appId,
      List<String> tags,
      TemplateType templateType,
      TemplateParameterFormat parameterFormat) {
    this(
        elementName,
        languageCode,
        body,
        null,
        category,
        appId,
        tags,
        templateType,
        parameterFormat);
  }

  /**
   * Constructs a new Template with variable examples.
   *
   * @param elementName the unique name of the template (alphanumeric, lowercase)
   * @param languageCode the language of the template
   * @param body the template message body text
   * @param variableExamples list of sample values corresponding to placeholders in the body text
   * @param category the category of the template
   * @param appId the Gupshup app ID
   * @param tags optional list of tag labels for the template
   * @param templateType the type of the template
   * @param parameterFormat the format of parameters
   */
  public Template(
      String elementName,
      LanguageCode languageCode,
      String body,
      List<String> variableExamples,
      TemplateCategory category,
      String appId,
      List<String> tags,
      TemplateType templateType,
      TemplateParameterFormat parameterFormat) {
    this.elementName = elementName;
    this.languageCode = languageCode;
    setBody(body);
    this.variableExamples = variableExamples;
    this.category = category;
    this.appId = appId;
    this.tags = tags;
    this.templateType = templateType;
    this.parameterFormat = parameterFormat;
  }

  /**
   * Sets the template message body text.
   *
   * @param body the body text, maximum 1024 characters, cannot start or end with a variable
   * @throws IllegalArgumentException if the body exceeds 1024 characters or starts/ends with a
   *     variable
   */
  public void setBody(String body) {
    if (body != null) {
      if (body.length() > 1024) {
        throw new IllegalArgumentException("Template body cannot exceed 1024 characters");
      }
      if (body.matches("^\\{\\{\\d+\\}\\}.*")) {
        throw new IllegalArgumentException("Template body cannot start with a variable");
      }
      if (body.matches(".*\\{\\{\\d+\\}\\}\\s*$")) {
        throw new IllegalArgumentException("Template body cannot end with a variable");
      }
    }
    this.body = body;
  }

  /**
   * Sets the template footer text.
   *
   * @param footer the footer text, maximum 60 characters
   * @throws IllegalArgumentException if the footer exceeds 60 characters
   */
  public void setFooter(String footer) {
    if (footer != null && footer.length() > 60) {
      throw new IllegalArgumentException("Template footer cannot exceed 60 characters");
    }
    this.footer = footer;
  }

  /**
   * Sets the list of interactive buttons for this template.
   *
   * @param buttons the list of buttons, maximum 10 buttons
   * @throws IllegalArgumentException if buttons count exceeds 10 or violates specific button
   *     placement rules
   */
  public void setButtons(List<Button> buttons) {
    if (buttons != null) {
      if (buttons.size() > 10) {
        throw new IllegalArgumentException("A template can have at most 10 buttons");
      }
      validateButtons(buttons);
    }
    this.buttons = buttons;
  }

  /**
   * Appends an interactive button to the template.
   *
   * @param button the button to add
   * @throws IllegalStateException if the template already has 10 buttons
   * @throws IllegalArgumentException if adding the button violates placement rules
   */
  public void addButton(Button button) {
    if (this.buttons.size() >= 10) {
      throw new IllegalStateException("A template can have at most 10 buttons");
    }
    List<Button> newButtons = new ArrayList<>(this.buttons);
    newButtons.add(button);
    validateButtons(newButtons);
    this.buttons.add(button);
  }

  private void validateButtons(List<Button> buttons) {
    long urlButtonsCount = buttons.stream().filter(b -> b.getType() == ButtonType.URL).count();
    if (urlButtonsCount > 2) {
      throw new IllegalArgumentException("A template can have at most 2 URL buttons");
    }

    long phoneButtonsCount =
        buttons.stream().filter(b -> b.getType() == ButtonType.PHONE_NUMBER).count();
    if (phoneButtonsCount > 1) {
      throw new IllegalArgumentException("A template can have at most 1 Phone Number button");
    }

    long payNowButtonsCount = buttons.stream().filter(b -> b instanceof PayNowButton).count();
    if (payNowButtonsCount > 1) {
      throw new IllegalArgumentException("A template can have at most 1 Pay Now button");
    }

    for (Button button : buttons) {
      button.validate();
      if (button instanceof OTPButton && !(this instanceof AuthenticationTemplate)) {
        throw new IllegalStateException("OTPButton is only allowed in AuthenticationTemplates");
      }
      if (button instanceof MPMButton && !(this instanceof ProductTemplate)) {
        throw new IllegalStateException("MPMButton is only allowed in ProductTemplates");
      }
      if (button instanceof CatalogButton && !(this instanceof CatalogTemplate)) {
        throw new IllegalStateException("CatalogButton is only allowed in CatalogTemplates");
      }
    }

    if (variableExamples != null && !variableExamples.isEmpty()) {
      if (body == null) {
        throw new IllegalStateException("Body is required when variable examples are provided");
      }
      for (int i = 1; i <= variableExamples.size(); i++) {
        String placeholder = "{{" + i + "}}";
        if (!body.contains(placeholder)) {
          throw new IllegalStateException(
              "Body must contain "
                  + placeholder
                  + " when "
                  + variableExamples.size()
                  + " variable examples are provided");
        }
      }
    }
  }

  /** Validates that the template has the minimum required fields for submission. */
  public void validate() {
    if (elementName == null || elementName.trim().isEmpty()) {
      throw new IllegalStateException("Element name is required");
    }
    if (languageCode == null) {
      throw new IllegalStateException("Language code is required");
    }
    if (category == null) {
      throw new IllegalStateException("Category is required");
    }
    if (body != null) {
      if (body.matches("^\\{\\{\\d+\\}\\}.*")) {
        throw new IllegalStateException("Template body cannot start with a variable");
      }
      if (body.matches(".*\\{\\{\\d+\\}\\}\\s*$")) {
        throw new IllegalStateException("Template body cannot end with a variable");
      }
    }

    boolean hasExamples = variableExamples != null && !variableExamples.isEmpty();
    boolean hasVariablesInBody =
        body != null && java.util.regex.Pattern.compile("\\{\\{\\d+\\}\\}").matcher(body).find();

    if (hasVariablesInBody && !hasExamples) {
      throw new IllegalStateException(
          "Body cannot contain variables if variable examples are not provided");
    }

    validateButtons(buttons);

    boolean hasPayNow = buttons.stream().anyMatch(b -> b instanceof PayNowButton);
    if (hasPayNow) {
      if (templateType != TemplateType.TEXT) {
        throw new IllegalStateException("Pay Now button is only allowed in TEXT templates");
      }
    }

    if (category == TemplateCategory.UTILITY) {
      if (templateType == TemplateType.GIF
          || templateType == TemplateType.CATALOG
          || templateType == TemplateType.PRODUCT
          || templateType == TemplateType.CAROUSEL) {
        throw new IllegalStateException(
            "UTILITY category templates cannot be of type GIF, CATALOG, PRODUCT, or CAROUSEL");
      }
    }

    if (ltoAttributes != null) {
      if (templateType != TemplateType.TEXT
          && templateType != TemplateType.IMAGE
          && templateType != TemplateType.VIDEO) {
        throw new IllegalStateException(
            "LTO templates are only allowed for TEXT, IMAGE, or VIDEO types");
      }
      boolean hasUrlButton = buttons.stream().anyMatch(b -> b.getType() == ButtonType.URL);
      if (!hasUrlButton) {
        throw new IllegalStateException("LTO templates must have at least one URL button");
      }
      if (ltoAttributes.isHasExpiration()) {
        boolean hasCopyCodeButton =
            buttons.stream().anyMatch(b -> b.getType() == ButtonType.COPY_CODE);
        if (!hasCopyCodeButton) {
          throw new IllegalStateException(
              "LTO templates with expiration must have at least one COPY_CODE button");
        }
      }
    }
  }

  /**
   * Checks if this template requires attaching a media file (either directly or via its
   * components).
   *
   * @return true if the template requires media, false otherwise
   */
  public boolean isMediaRequired() {
    return templateType != null && templateType.isMediaRequired();
  }
}
