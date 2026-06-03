package io.github.mrcloss.gupshup.domain.template;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import io.github.mrcloss.gupshup.domain.button.MPMButton;
import io.github.mrcloss.gupshup.domain.button.QuickReplyButton;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import java.util.List;

public class ProductValidationTest {

    @Test
    public void productTemplateShouldHaveViewItemsButtonOnInit() {
        ProductTemplate template = new ProductTemplate(
            "test_product",
            LanguageCode.ENGLISH,
            "Product body",
            TemplateCategory.MARKETING,
            "app-123",
            null,
            TemplateParameterFormat.POSITIONAL
        );
        assertEquals(1, template.getButtons().size());
        assertTrue(template.getButtons().get(0) instanceof MPMButton);
        assertEquals("View items", template.getButtons().get(0).getText());
    }

    @Test
    public void productTemplateShouldNotAllowAddingButtons() {
        ProductTemplate template = new ProductTemplate(
            "test_product",
            LanguageCode.ENGLISH,
            "Product body",
            TemplateCategory.MARKETING,
            "app-123",
            null,
            TemplateParameterFormat.POSITIONAL
        );
        assertThrows(UnsupportedOperationException.class, () -> {
            template.addButton(new QuickReplyButton("Test"));
        });
    }

    @Test
    public void productTemplateShouldNotAllowSettingButtons() {
        ProductTemplate template = new ProductTemplate(
            "test_product",
            LanguageCode.ENGLISH,
            "Product body",
            TemplateCategory.MARKETING,
            "app-123",
            null,
            TemplateParameterFormat.POSITIONAL
        );
        assertThrows(UnsupportedOperationException.class, () -> {
            template.setButtons(List.of(new QuickReplyButton("Test")));
        });
    }
}
