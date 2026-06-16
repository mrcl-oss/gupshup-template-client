package io.github.mrcloss.gupshup.domain.template;

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import org.junit.jupiter.api.Test;

public class MediaValidationTest {

  @Test
  public void imageTemplateShouldOnlyAllowImageExtensions() {
    ImageTemplate template =
        new ImageTemplate(
            "test_media",
            LanguageCode.ENGLISH,
            "Media body",
            TemplateCategory.MARKETING,
            "app-123",
            null,
            TemplateParameterFormat.POSITIONAL);
    template.setMediaUrl("https://example.com/image.png"); // Valid

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          template.setMediaUrl("https://example.com/document.pdf");
        },
        "Image template should not allow .pdf files");
  }

  @Test
  public void documentTemplateShouldOnlyAllowDocumentExtensions() {
    DocumentTemplate template =
        new DocumentTemplate(
            "test_media",
            LanguageCode.ENGLISH,
            "Media body",
            TemplateCategory.MARKETING,
            "app-123",
            null,
            TemplateParameterFormat.POSITIONAL);
    template.setMediaUrl("https://example.com/doc.pdf"); // Valid

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          template.setMediaUrl("https://example.com/image.png");
        },
        "Document template should not allow .png files");
  }

  @Test
  public void videoTemplateShouldOnlyAllowVideoExtensions() {
    VideoTemplate template =
        new VideoTemplate(
            "test_media",
            LanguageCode.ENGLISH,
            "Media body",
            TemplateCategory.MARKETING,
            "app-123",
            null,
            TemplateParameterFormat.POSITIONAL);
    template.setMediaUrl("https://example.com/video.mp4"); // Valid

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          template.setMediaUrl("https://example.com/image.png");
        },
        "Video template should not allow .png files");
  }

  @Test
  public void gifTemplateShouldOnlyAllowGifExtensions() {
    GIFTemplate template =
        new GIFTemplate(
            "test_media",
            LanguageCode.ENGLISH,
            "Media body",
            TemplateCategory.MARKETING,
            "app-123",
            null,
            TemplateParameterFormat.POSITIONAL);
    template.setMediaUrl("https://example.com/animation.gif"); // Valid

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          template.setMediaUrl("https://example.com/image.png");
        },
        "GIF template should only allow .gif files");
  }

  @Test
  public void mediaUrlShouldBeValidUrl() {
    ImageTemplate template =
        new ImageTemplate(
            "test_media",
            LanguageCode.ENGLISH,
            "Media body",
            TemplateCategory.MARKETING,
            "app-123",
            null,
            TemplateParameterFormat.POSITIONAL);
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          template.setMediaUrl("invalid-url");
        },
        "Media URL must be a valid URL");
  }
}
