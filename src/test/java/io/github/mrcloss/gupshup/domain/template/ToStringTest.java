package io.github.mrcloss.gupshup.domain.template;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.mrcloss.gupshup.domain.button.QuickReplyButton;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import io.github.mrcloss.gupshup.domain.message.TextPayload;
import io.github.mrcloss.gupshup.infrastructure.dto.response.GetTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.GupshupTemplateDetails;
import org.junit.jupiter.api.Test;

public class ToStringTest {

  @Test
  public void testTemplateToString() {
    Template template =
        new Template(
            "test_template",
            LanguageCode.ENGLISH,
            "Hello world",
            TemplateCategory.MARKETING,
            "app-123",
            java.util.Collections.singletonList("tag"),
            TemplateType.TEXT,
            TemplateParameterFormat.POSITIONAL);

    String toString = template.toString();
    System.out.println("TEMPLATE TOSTRING: " + toString);

    assertTrue(toString.contains("test_template"));
    assertTrue(toString.contains("en"));
    assertTrue(toString.contains("Hello world"));
    assertTrue(toString.contains("MARKETING"));
    assertTrue(toString.contains("app-123"));
  }

  @Test
  public void testTextTemplateToString() {
    TextTemplate textTemplate =
        new TextTemplate(
            "text_template",
            LanguageCode.ENGLISH,
            "Hello world",
            TemplateCategory.MARKETING,
            "app-123",
            java.util.Collections.singletonList("tag"),
            TemplateParameterFormat.POSITIONAL,
            "Header text");

    String toString = textTemplate.toString();
    System.out.println("TEXT_TEMPLATE TOSTRING: " + toString);

    // Verify it includes fields from super class (callSuper = true)
    assertTrue(toString.contains("text_template"));
    assertTrue(toString.contains("en"));
    // Verify it includes subclass fields
    assertTrue(toString.contains("Header text"));
  }

  @Test
  public void testButtonToString() {
    QuickReplyButton button = new QuickReplyButton("Click me");
    String toString = button.toString();

    // Verify superclass fields (text and type) are printed
    assertTrue(toString.contains("Click me"));
    assertTrue(toString.contains("QUICK_REPLY"));
  }

  @Test
  public void testMessagePayloadToString() {
    TextPayload payload = new TextPayload(1, "Postback message");
    String toString = payload.toString();

    // Verify superclass fields (messageType) are printed
    assertTrue(toString.contains("TEXT"));
    // Verify nested objects / subclass fields are printed
    assertTrue(toString.contains("PostBackTexts"));
    assertTrue(toString.contains("Postback message"));
  }

  @Test
  public void testGupshupTemplateDetailsToString() {
    GupshupTemplateDetails details = new GupshupTemplateDetails();
    details.setId("dca4b081-4c58-4d2d-a458-a06c0b7beba0");
    details.setElementName("my_template_name");
    details.setCategory("MARKETING");

    String toString = details.toString();
    assertTrue(toString.contains("dca4b081-4c58-4d2d-a458-a06c0b7beba0"));
    assertTrue(toString.contains("my_template_name"));
    assertTrue(toString.contains("MARKETING"));
  }

  @Test
  public void testGetTemplateResponseToString() {
    GetTemplateResponse response = new GetTemplateResponse();
    response.setStatus("success");
    GupshupTemplateDetails details = new GupshupTemplateDetails();
    details.setId("template-id");
    response.setTemplate(details);

    String toString = response.toString();
    assertTrue(toString.contains("success"));
    assertTrue(toString.contains("template-id"));
  }
}
