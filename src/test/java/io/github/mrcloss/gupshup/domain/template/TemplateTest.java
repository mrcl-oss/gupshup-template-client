package io.github.mrcloss.gupshup.domain.template;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.github.mrcloss.gupshup.domain.button.PhoneNumberButton;
import io.github.mrcloss.gupshup.domain.button.StaticUrlButton;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;

public class TemplateTest {

    private Template createBaseTemplate() {
        return new Template("test_template", LanguageCode.ENGLISH, "Hello world", TemplateCategory.MARKETING, "app-123", java.util.Collections.singletonList("tag"), TemplateType.TEXT, TemplateParameterFormat.POSITIONAL);
    }

    @Test
    public void shouldNotAllowMoreThanTwoUrlButtons() {
        Template template = createBaseTemplate();
        template.addButton(new StaticUrlButton("URL 1", "https://example.com/1"));
        template.addButton(new StaticUrlButton("URL 2", "https://example.com/2"));
        
        assertThrows(IllegalArgumentException.class, () -> {
            template.addButton(new StaticUrlButton("URL 3", "https://example.com/3"));
        }, "Should not allow more than 2 URL buttons");
    }

    @Test
    public void shouldNotAllowMoreThanOnePhoneNumberButton() {
        Template template = createBaseTemplate();
        template.addButton(new PhoneNumberButton("Phone 1", "+1234567890"));
        
        assertThrows(IllegalArgumentException.class, () -> {
            template.addButton(new PhoneNumberButton("Phone 2", "+9876543210"));
        }, "Should not allow more than 1 Phone Number button");
    }

    @Test
    public void shouldNotAllowVariablesAtStartOfBody() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Template("test", LanguageCode.ENGLISH, "{{1}} text", TemplateCategory.MARKETING, "app", null, TemplateType.TEXT, TemplateParameterFormat.POSITIONAL);
        }, "Should not allow variables at the start of the body");
    }

    @Test
    public void shouldNotAllowVariablesAtEndOfBody() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Template("test", LanguageCode.ENGLISH, "text {{1}}", TemplateCategory.MARKETING, "app", null, TemplateType.TEXT, TemplateParameterFormat.POSITIONAL);
        }, "Should not allow variables at the end of the body");
    }

    @Test
    public void shouldNotAllowOnlyVariableInBody() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Template("test", LanguageCode.ENGLISH, "{{1}}", TemplateCategory.MARKETING, "app", null, TemplateType.TEXT, TemplateParameterFormat.POSITIONAL);
        }, "Should not allow only a variable in the body");
    }

    @Test
    public void shouldAllowVariablesInMiddleOfBody() {
        new Template("test", LanguageCode.ENGLISH, "text {{1}} more text", TemplateCategory.MARKETING, "app", null, TemplateType.TEXT, TemplateParameterFormat.POSITIONAL);
        // Should not throw
    }

    @Test
    public void shouldNotAllowVariablesAtEndOfBodyWithTrailingSpaces() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Template("test", LanguageCode.ENGLISH, "text {{1}} ", TemplateCategory.MARKETING, "app", null, TemplateType.TEXT, TemplateParameterFormat.POSITIONAL);
        }, "Should not allow variables at the end of the body even with trailing spaces");
    }

    @Test
    public void utilityCategoryShouldNotAllowRestrictedTypes() {
        Template template = new Template("test_utility", LanguageCode.ENGLISH, "Hello", TemplateCategory.UTILITY, "app", null, TemplateType.TEXT, TemplateParameterFormat.POSITIONAL);

        template.setTemplateType(TemplateType.GIF);
        assertThrows(IllegalStateException.class, template::validate);

        template.setTemplateType(TemplateType.CATALOG);
        assertThrows(IllegalStateException.class, template::validate);

        template.setTemplateType(TemplateType.PRODUCT);
        assertThrows(IllegalStateException.class, template::validate);

        template.setTemplateType(TemplateType.CAROUSEL);
        assertThrows(IllegalStateException.class, template::validate);
        
        template.setTemplateType(TemplateType.TEXT);
        template.validate(); // Should pass
    }

    @Test
    public void shouldRequirePlaceholdersIfVariableExamplesArePresent() {
        Template template = new Template("test_vars", LanguageCode.ENGLISH, "Hello {{1}}!", TemplateCategory.MARKETING, "app", null, TemplateType.TEXT, TemplateParameterFormat.POSITIONAL);
        template.setVariableExamples(java.util.Arrays.asList("John", "Doe"));
        
        assertThrows(IllegalStateException.class, template::validate, 
            "Should fail because {{2}} is missing");

        template.setBody("Hello {{1}} and {{2}}!");
        template.validate(); // Should pass
    }
}
