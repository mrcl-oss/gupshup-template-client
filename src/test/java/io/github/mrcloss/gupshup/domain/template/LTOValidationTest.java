package io.github.mrcloss.gupshup.domain.template;

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.mrcloss.gupshup.domain.button.StaticUrlButton;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import org.junit.jupiter.api.Test;

public class LTOValidationTest {

  @Test
  public void ltoShouldOnlyBeAllowedInTextVideoOrImage() {
    Template template =
        new Template(
            "test_lto",
            LanguageCode.ENGLISH,
            "LTO body",
            TemplateCategory.MARKETING,
            "app-123",
            null,
            TemplateType.CATALOG,
            TemplateParameterFormat.POSITIONAL);
    template.setLtoAttributes(new LTOAttributes(false, "LTO"));

    assertThrows(
        IllegalStateException.class,
        () -> {
          template.validate();
        },
        "LTO should only be allowed in TEXT, VIDEO or IMAGE templates");
  }

  @Test
  public void ltoShouldRequireUrlButton() {
    Template template = createBaseTemplate();
    template.setTemplateType(TemplateType.TEXT);
    template.setLtoAttributes(new LTOAttributes(false, "LTO"));

    assertThrows(
        IllegalStateException.class,
        () -> {
          template.validate();
        },
        "LTO templates must have a URL button");
  }

  @Test
  public void ltoWithExpirationShouldRequireCopyCodeButton() {
    Template template = createBaseTemplate();
    template.setTemplateType(TemplateType.TEXT);
    template.setLtoAttributes(new LTOAttributes(true, "LTO"));
    template.addButton(new StaticUrlButton("Visit", "https://example.com"));

    assertThrows(
        IllegalStateException.class,
        () -> {
          template.validate();
        },
        "LTO templates with expiration must have a COPY_CODE button");
  }

  private Template createBaseTemplate() {
    return new Template(
        "test_lto",
        LanguageCode.ENGLISH,
        "LTO body",
        TemplateCategory.MARKETING,
        "app-123",
        null,
        TemplateType.TEXT,
        TemplateParameterFormat.POSITIONAL);
  }
}
