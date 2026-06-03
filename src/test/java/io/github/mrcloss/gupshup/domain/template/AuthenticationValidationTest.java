package io.github.mrcloss.gupshup.domain.template;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.button.CopyCodeButton;
import io.github.mrcloss.gupshup.domain.button.QuickReplyButton;

public class AuthenticationValidationTest {

    private AuthenticationTemplate createTemplate() {
        return new AuthenticationTemplate("auth_template", LanguageCode.ENGLISH, "Your code is {{1}}.", "app-123", null, TemplateParameterFormat.POSITIONAL);
    }

    @Test
    public void authenticationTemplateShouldHaveCopyCodeButtonOnInit() {
        AuthenticationTemplate template = createTemplate();
        assertEquals(1, template.getButtons().size());
        assertTrue(template.getButtons().get(0) instanceof CopyCodeButton);
        assertEquals("Copy code", template.getButtons().get(0).getText());
    }

    @Test
    public void authenticationTemplateShouldUpdateButtonTextWhenExpirationChanges() {
        AuthenticationTemplate template = createTemplate();
        template.setCodeExpirationMinutes(10);
        assertEquals("This code expires in {{codeExpirationMinutes}} minutes", template.getButtons().get(0).getText());
        
        template.setCodeExpirationMinutes(0);
        assertEquals("Copy code", template.getButtons().get(0).getText());
    }

    @Test
    public void authenticationTemplateShouldOnlyAllowAuthenticationCategory() {
        AuthenticationTemplate template = createTemplate();
        assertThrows(IllegalArgumentException.class, () -> {
            template.setCategory(TemplateCategory.MARKETING);
        });
    }

    @Test
    public void authenticationTemplateShouldNotAllowAddingButtons() {
        AuthenticationTemplate template = createTemplate();
        assertThrows(UnsupportedOperationException.class, () -> {
            template.addButton(new QuickReplyButton("Test"));
        });
    }

    @Test
    public void authenticationTemplateShouldAddSecurityRecommendation() {
        AuthenticationTemplate template = createTemplate();
        template.setBody("Your code is {{1}}.");
        template.setAddSecurityRecommendation(true);
        assertEquals("Your code is {{1}}. For your security, do not share this code.", template.getBody());
        
        template.setAddSecurityRecommendation(false);
        assertEquals("Your code is {{1}}.", template.getBody());
    }

    @Test
    public void authenticationTemplateShouldMaintainRecommendationOnNewBody() {
        AuthenticationTemplate template = createTemplate();
        template.setAddSecurityRecommendation(true);
        template.setBody("New code: {{1}}.");
        assertEquals("New code: {{1}}. For your security, do not share this code.", template.getBody());
    }
}
