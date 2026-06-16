package io.github.mrcloss.gupshup.domain.template;

import static org.junit.jupiter.api.Assertions.*;

import io.github.mrcloss.gupshup.domain.button.CatalogButton;
import io.github.mrcloss.gupshup.domain.button.QuickReplyButton;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CatalogValidationTest {

  @Test
  public void catalogTemplateShouldHaveViewCatalogButtonOnInit() {
    CatalogTemplate template =
        new CatalogTemplate(
            "test_catalog",
            LanguageCode.ENGLISH,
            "Catalog body",
            TemplateCategory.MARKETING,
            "app-123",
            null,
            TemplateParameterFormat.POSITIONAL);
    assertEquals(1, template.getButtons().size());
    assertTrue(template.getButtons().get(0) instanceof CatalogButton);
    assertEquals("View catalog", template.getButtons().get(0).getText());
  }

  @Test
  public void catalogTemplateShouldNotAllowAddingButtons() {
    CatalogTemplate template =
        new CatalogTemplate(
            "test_catalog",
            LanguageCode.ENGLISH,
            "Catalog body",
            TemplateCategory.MARKETING,
            "app-123",
            null,
            TemplateParameterFormat.POSITIONAL);
    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          template.addButton(new QuickReplyButton("Test"));
        });
  }

  @Test
  public void catalogTemplateShouldNotAllowSettingButtons() {
    CatalogTemplate template =
        new CatalogTemplate(
            "test_catalog",
            LanguageCode.ENGLISH,
            "Catalog body",
            TemplateCategory.MARKETING,
            "app-123",
            null,
            TemplateParameterFormat.POSITIONAL);
    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          template.setButtons(List.of(new QuickReplyButton("Test")));
        });
  }
}
