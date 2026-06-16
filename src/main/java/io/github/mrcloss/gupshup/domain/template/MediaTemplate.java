package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class MediaTemplate extends Template {
  private String mediaId;

  @Setter(AccessLevel.NONE)
  private String mediaUrl;

  public MediaTemplate(
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

  public MediaTemplate(
      String elementName,
      LanguageCode languageCode,
      String body,
      List<String> variableExamples,
      TemplateCategory category,
      String appId,
      List<String> tags,
      TemplateType templateType,
      TemplateParameterFormat parameterFormat) {
    super(
        elementName,
        languageCode,
        body,
        variableExamples,
        category,
        appId,
        tags,
        templateType,
        parameterFormat);
  }

  public abstract String[] getAllowedExtensions();

  public void setMediaUrl(String mediaUrl) {
    if (mediaUrl != null) {
      if (!mediaUrl.startsWith("http://") && !mediaUrl.startsWith("https://")) {
        throw new IllegalArgumentException("Media URL must start with http:// or https://");
      }

      // Strict URL format check (simplified for this context but ensuring it looks like a URL with
      // a path)
      if (!mediaUrl.matches("^https?://[\\w\\.-]+(?:\\.[\\w\\.-]+)+[/\\w\\.-]*/?$")) {
        throw new IllegalArgumentException("Invalid Media URL format");
      }

      String[] allowedExtensions = getAllowedExtensions();
      boolean validExtension = false;
      String lowerUrl = mediaUrl.toLowerCase();
      for (String ext : allowedExtensions) {
        if (lowerUrl.endsWith(ext.toLowerCase())) {
          validExtension = true;
          break;
        }
      }

      if (!validExtension) {
        throw new IllegalArgumentException(
            "Media URL extension not allowed for this template type. Allowed: "
                + String.join(", ", allowedExtensions));
      }
    }
    this.mediaUrl = mediaUrl;
  }
}
