package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.button.Button;
import io.github.mrcloss.gupshup.domain.button.CopyCodeButton;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a specialized WhatsApp template for sending One-Time Passwords (OTPs) or verification
 * codes.
 *
 * <p>Authentication templates must belong to the {@code AUTHENTICATION} category, cannot feature a
 * header, and must include exactly one OTP button (which is either a copy-code button or a one-tap
 * autofill button).
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class AuthenticationTemplate extends TextTemplate {
  @Setter(AccessLevel.NONE)
  private boolean addSecurityRecommendation;

  @Setter(AccessLevel.NONE)
  private int codeExpirationMinutes;

  /**
   * Constructs a new AuthenticationTemplate without variable examples.
   *
   * @param elementName the unique name of the template (alphanumeric, lowercase)
   * @param languageCode the language of the template
   * @param body the template message body text
   * @param appId the Gupshup app ID
   * @param tags optional list of tag labels for the template
   * @param parameterFormat the format of parameters
   */
  public AuthenticationTemplate(
      String elementName,
      LanguageCode languageCode,
      String body,
      String appId,
      List<String> tags,
      TemplateParameterFormat parameterFormat) {
    this(elementName, languageCode, body, null, appId, tags, parameterFormat);
  }

  public AuthenticationTemplate(
      String elementName,
      LanguageCode languageCode,
      String body,
      List<String> variableExamples,
      String appId,
      List<String> tags,
      TemplateParameterFormat parameterFormat) {
    super(
        elementName,
        languageCode,
        "{{1}} is your verification code.",
        Collections.singletonList("123456"),
        TemplateCategory.AUTHENTICATION,
        appId,
        tags,
        parameterFormat);
    updateButton();
    updateFooter();
  }

  public void setAddSecurityRecommendation(boolean addSecurityRecommendation) {
    if (this.addSecurityRecommendation != addSecurityRecommendation) {
      this.addSecurityRecommendation = addSecurityRecommendation;
      updateBodyWithRecommendation();
    }
  }

  @Override
  public void setBody(String body) {
    super.setBody("{{1}} is your verification code.");
    updateBodyWithRecommendation();
  }

  @Override
  public void setVariableExamples(List<String> variableExamples) {
    super.setVariableExamples(Collections.singletonList("123456"));
  }

  private void updateBodyWithRecommendation() {
    String currentBody = super.getBody();
    if (currentBody == null) {
      return;
    }

    String recommendation = " For your security, do not share this code.";
    if (addSecurityRecommendation) {
      if (!currentBody.endsWith(recommendation)) {
        super.setBody(currentBody + recommendation);
      }
    } else {
      if (currentBody.endsWith(recommendation)) {
        super.setBody(currentBody.substring(0, currentBody.length() - recommendation.length()));
      }
    }
  }

  public void setCodeExpirationMinutes(int codeExpirationMinutes) {
    if (codeExpirationMinutes < 0 || codeExpirationMinutes > 90) {
      throw new IllegalArgumentException(
          "Code expiration minutes must be between 1 and 90 (or 0 for no expiration)");
    }
    this.codeExpirationMinutes = codeExpirationMinutes;
    updateButton();
    updateFooter();
  }

  private void updateFooter() {
    if (codeExpirationMinutes > 0) {
      String suffix = (codeExpirationMinutes == 1) ? " minute" : " minutes";
      super.setFooter("This code expires in " + codeExpirationMinutes + suffix);
    } else {
      super.setFooter(null);
    }
  }

  private void updateButton() {
    String text = "Copy code";

    // We bypass the restriction by calling super
    super.setButtons(Collections.singletonList(new CopyCodeButton(text, "123456")));
  }

  @Override
  public void setTemplateType(TemplateType templateType) {
    if (templateType != TemplateType.TEXT) {
      throw new IllegalArgumentException("Authentication templates must always be of type TEXT");
    }
    super.setTemplateType(templateType);
  }

  @Override
  public void setCategory(TemplateCategory category) {
    if (category != TemplateCategory.AUTHENTICATION) {
      throw new IllegalArgumentException(
          "Authentication template only allowed for AUTHENTICATION category");
    }
    super.setCategory(category);
  }

  @Override
  public void setHeader(String header) {
    if (header != null && !header.trim().isEmpty()) {
      throw new IllegalArgumentException("Authentication templates cannot have a header");
    }
    super.setHeader(header);
  }

  @Override
  public void setFooter(String footer) {
    updateFooter();
  }

  @Override
  public void validate() {
    super.validate();
    if (getHeader() != null && !getHeader().trim().isEmpty()) {
      throw new IllegalStateException("Authentication templates cannot have a header");
    }
  }

  @Override
  public void setButtons(List<Button> buttons) {
    throw new UnsupportedOperationException("Authentication template buttons cannot be modified");
  }

  @Override
  public void addButton(Button button) {
    throw new UnsupportedOperationException("Authentication template buttons cannot be modified");
  }
}
