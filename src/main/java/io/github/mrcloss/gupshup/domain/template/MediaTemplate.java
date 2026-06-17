package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract base class for WhatsApp media templates.
 *
 * <p>Media templates support attaching an image, video, document, or GIF as a header message,
 * specified either by a public URL or by a pre-uploaded media ID.
 */
@Getter
@Setter
public abstract class MediaTemplate extends Template {
  private String mediaId;

  @Setter(AccessLevel.NONE)
  private String mediaUrl;

  /**
   * Constructs a new MediaTemplate without variable examples.
   *
   * @param elementName the unique name of the template (alphanumeric, lowercase)
   * @param languageCode the language of the template
   * @param body the template message body text
   * @param category the category of the template
   * @param appId the Gupshup app ID
   * @param tags optional list of tag labels for the template
   * @param templateType the type of the template (e.g. IMAGE, VIDEO)
   * @param parameterFormat the format of parameters
   */
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

  /**
   * Returns the list of file extensions allowed for this media type.
   *
   * @return an array of allowed extensions (e.g. {@code .jpg}, {@code .png})
   */
  public abstract String[] getAllowedExtensions();

  /**
   * Sets the media URL for the template.
   *
   * @param mediaUrl the public URL of the media asset, must use HTTP or HTTPS and end with an
   *     allowed extension
   * @throws IllegalArgumentException if the URL is invalid or does not match the allowed extensions
   */
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
