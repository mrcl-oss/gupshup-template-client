package io.github.mrcloss.gupshup.domain.template;

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.mrcloss.gupshup.domain.button.CatalogButton;
import io.github.mrcloss.gupshup.domain.button.MPMButton;
import io.github.mrcloss.gupshup.domain.button.OTPButton;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import org.junit.jupiter.api.Test;

public class SpecificButtonUsageTest {

  @Test
  public void otpButtonShouldOnlyBeInAuthenticationTemplate() {
    TextTemplate template = createTextTemplate();
    assertThrows(
        IllegalStateException.class,
        () -> {
          template.addButton(new OTPButton("OTP", OTPButton.OTPButtonType.COPY_CODE));
        },
        "OTPButton should only be allowed in AuthenticationTemplates");
  }

  @Test
  public void mpmButtonShouldOnlyBeInProductTemplate() {
    TextTemplate template = createTextTemplate();
    assertThrows(
        IllegalStateException.class,
        () -> {
          template.addButton(new MPMButton("View items"));
        },
        "MPMButton should only be allowed in ProductTemplates");
  }

  @Test
  public void catalogButtonShouldOnlyBeInCatalogTemplate() {
    TextTemplate template = createTextTemplate();
    assertThrows(
        IllegalStateException.class,
        () -> {
          template.addButton(new CatalogButton("View catalog"));
        },
        "CatalogButton should only be allowed in CatalogTemplates");
  }

  private TextTemplate createTextTemplate() {
    TextTemplate template =
        new TextTemplate(
            "test_text",
            LanguageCode.ENGLISH,
            "Hello",
            TemplateCategory.MARKETING,
            "app-123",
            null,
            TemplateParameterFormat.POSITIONAL);
    return template;
  }
}
