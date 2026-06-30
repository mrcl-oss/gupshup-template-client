package io.github.mrcloss.gupshup.domain.template;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class VariableValidationTest {

  @Test
  void templateShouldFailIfBodyHasVariablesButNoExamples() {
    Template template =
        new TextTemplate(
            "test_template",
            LanguageCode.ENGLISH,
            "Hello {{1}} how are you?",
            TemplateCategory.MARKETING,
            "app-id",
            Collections.emptyList(),
            TemplateParameterFormat.POSITIONAL);

    assertThrows(
        IllegalStateException.class,
        template::validate,
        "Should fail if body has variables but no examples");
  }

  @Test
  void templateShouldPassIfBodyHasVariablesAndExamples() {
    Template template =
        new TextTemplate(
            "test_template",
            LanguageCode.ENGLISH,
            "Hello {{1}} how are you?",
            TemplateCategory.MARKETING,
            "app-id",
            Collections.emptyList(),
            TemplateParameterFormat.POSITIONAL);
    template.setVariableExamples(Collections.singletonList("World"));

    assertDoesNotThrow(template::validate);
  }

  @Test
  void templateShouldPassIfBodyHasNoVariablesAndNoExamples() {
    Template template =
        new TextTemplate(
            "test_template",
            LanguageCode.ENGLISH,
            "Hello World",
            TemplateCategory.MARKETING,
            "app-id",
            Collections.emptyList(),
            TemplateParameterFormat.POSITIONAL);

    assertDoesNotThrow(template::validate);
  }

  @Test
  void textTemplateShouldFailIfHeaderHasVariablesButNoExamples() {
    TextTemplate template =
        new TextTemplate(
            "test_template",
            LanguageCode.ENGLISH,
            "Body text",
            TemplateCategory.MARKETING,
            "app-id",
            Collections.emptyList(),
            TemplateParameterFormat.POSITIONAL,
            "Header {{1}}");

    assertThrows(
        IllegalStateException.class,
        template::validate,
        "Should fail if header has variables but no examples");
  }

  @Test
  void textTemplateShouldPassIfHeaderHasVariablesAndExamples() {
    TextTemplate template =
        new TextTemplate(
            "test_template",
            LanguageCode.ENGLISH,
            "Body text",
            TemplateCategory.MARKETING,
            "app-id",
            Collections.emptyList(),
            TemplateParameterFormat.POSITIONAL,
            "Header {{1}}",
            Collections.singletonList("Title"));

    assertDoesNotThrow(template::validate);
  }

  @Test
  void carouselCardShouldFailIfBodyHasVariablesButNoExamples() {
    CarouselCard card =
        new CarouselCard("Hello {{1}}!", null, CarouselCard.CarouselCardHeaderType.IMAGE);
    // carousel card needs buttons to be valid
    card.setButtons(
        Collections.singletonList(
            new io.github.mrcloss.gupshup.domain.button.QuickReplyButton("Yes")));

    assertThrows(
        IllegalStateException.class,
        card::validate,
        "Should fail if carousel card body has variables but no examples");
  }

  @Test
  void testTemplateGetFilledBody() {
    Template template =
        new TextTemplate(
            "test_template",
            LanguageCode.ENGLISH,
            "Hello {{1}} how are you {{2}}?",
            TemplateCategory.MARKETING,
            "app-id",
            Collections.emptyList(),
            TemplateParameterFormat.POSITIONAL);
    template.setVariableExamples(java.util.List.of("Alice", "today"));

    org.junit.jupiter.api.Assertions.assertEquals(
        "Hello Alice how are you today?", template.getFilledBody());

    org.junit.jupiter.api.Assertions.assertEquals(
        "Hello Bob how are you tomorrow?",
        template.getFilledBody(java.util.List.of("Bob", "tomorrow")));
  }

  @Test
  void testTextTemplateGetFilledHeader() {
    TextTemplate template =
        new TextTemplate(
            "test_template",
            LanguageCode.ENGLISH,
            "Body",
            TemplateCategory.MARKETING,
            "app-id",
            Collections.emptyList(),
            TemplateParameterFormat.POSITIONAL,
            "Header {{1}}",
            java.util.List.of("Title"));

    org.junit.jupiter.api.Assertions.assertEquals("Header Title", template.getFilledHeader());

    org.junit.jupiter.api.Assertions.assertEquals(
        "Header Custom", template.getFilledHeader(java.util.List.of("Custom")));
  }

  @Test
  void testCarouselCardGetFilledBody() {
    CarouselCard card = new CarouselCard("Hello {{1}}!", CarouselCard.CarouselCardHeaderType.IMAGE);
    card.setVariableExamples(java.util.List.of("Alice"));

    org.junit.jupiter.api.Assertions.assertEquals("Hello Alice!", card.getFilledBody());

    org.junit.jupiter.api.Assertions.assertEquals(
        "Hello Bob!", card.getFilledBody(java.util.List.of("Bob")));
  }
}
