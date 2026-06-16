package io.github.mrcloss.gupshup.domain.template;

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.mrcloss.gupshup.domain.button.PayNowButton;
import io.github.mrcloss.gupshup.domain.button.StaticUrlButton;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import org.junit.jupiter.api.Test;

public class HeaderAndPaymentValidationTest {

  @Test
  public void textTemplateHeaderShouldAllowAtMostOneVariable() {
    TextTemplate template =
        new TextTemplate(
            "test_text",
            LanguageCode.ENGLISH,
            "Hello world",
            TemplateCategory.MARKETING,
            "app-123",
            null,
            TemplateParameterFormat.POSITIONAL);
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          template.setHeader("Hello {{1}}, welcome {{2}}");
        },
        "Header should not allow more than one variable");
  }

  @Test
  public void shouldNotAllowMoreThanOnePayNowButton() {
    Template template =
        new TextTemplate(
            "test_text",
            LanguageCode.ENGLISH,
            "Hello world",
            TemplateCategory.MARKETING,
            "app-123",
            null,
            TemplateParameterFormat.POSITIONAL);
    template.addButton(
        new PayNowButton("Pay 1", true, new StaticUrlButton("URL", "https://example.com/pay1")));

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          template.addButton(
              new PayNowButton(
                  "Pay 2", true, new StaticUrlButton("URL", "https://example.com/pay2")));
        },
        "Should not allow more than one Pay Now button");
  }

  @Test
  public void payNowButtonShouldOnlyBeAllowedInTextTemplate() {
    // We'll use a generic Template or a non-text specific one if available
    Template template =
        new Template(
            "test_template",
            LanguageCode.ENGLISH,
            "Hello world",
            TemplateCategory.MARKETING,
            "app-123",
            null,
            TemplateType.IMAGE,
            TemplateParameterFormat.POSITIONAL);

    assertThrows(
        IllegalStateException.class,
        () -> {
          template.addButton(
              new PayNowButton("Pay", true, new StaticUrlButton("URL", "https://example.com/pay")));
          template.validate();
        },
        "Pay Now button should only be allowed in TEXT templates");
  }

  @Test
  public void payNowButtonShouldNotAllowHeader() {
    TextTemplate template =
        new TextTemplate(
            "test_text",
            LanguageCode.ENGLISH,
            "Hello world",
            TemplateCategory.MARKETING,
            "app-123",
            null,
            TemplateParameterFormat.POSITIONAL);
    template.setHeader("Some Header");
    template.addButton(
        new PayNowButton("Pay", true, new StaticUrlButton("URL", "https://example.com/pay")));

    assertThrows(
        IllegalStateException.class,
        () -> {
          template.validate();
        },
        "Templates with Pay Now button cannot have a header");
  }
}
