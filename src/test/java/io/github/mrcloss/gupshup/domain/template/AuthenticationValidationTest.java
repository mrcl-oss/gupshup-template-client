package io.github.mrcloss.gupshup.domain.template;

import static org.junit.jupiter.api.Assertions.*;

import io.github.mrcloss.gupshup.domain.button.CopyCodeButton;
import io.github.mrcloss.gupshup.domain.button.QuickReplyButton;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import org.junit.jupiter.api.Test;

public class AuthenticationValidationTest {

  private AuthenticationTemplate createTemplate() {
    return new AuthenticationTemplate(
        "auth_template",
        LanguageCode.ENGLISH,
        "Your code is {{1}}.",
        "app-123",
        null,
        TemplateParameterFormat.POSITIONAL);
  }

  @Test
  public void authenticationTemplateShouldHaveCopyCodeButtonOnInit() {
    AuthenticationTemplate template = createTemplate();
    assertEquals(1, template.getButtons().size());
    assertTrue(template.getButtons().get(0) instanceof CopyCodeButton);
    assertEquals("Copy code", template.getButtons().get(0).getText());
  }

  @Test
  public void authenticationTemplateShouldKeepButtonTextAsCopyCodeWhenExpirationChanges() {
    AuthenticationTemplate template = createTemplate();
    template.setCodeExpirationMinutes(10);
    assertEquals("Copy code", template.getButtons().get(0).getText());

    template.setCodeExpirationMinutes(0);
    assertEquals("Copy code", template.getButtons().get(0).getText());
  }

  @Test
  public void authenticationTemplateShouldValidateCodeExpirationMinutesRange() {
    AuthenticationTemplate template = createTemplate();

    // Valid values: 0 (no expiration), and 1 to 90
    assertDoesNotThrow(() -> template.setCodeExpirationMinutes(0));
    assertDoesNotThrow(() -> template.setCodeExpirationMinutes(1));
    assertDoesNotThrow(() -> template.setCodeExpirationMinutes(45));
    assertDoesNotThrow(() -> template.setCodeExpirationMinutes(90));

    // Invalid values: negative, or greater than 90
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          template.setCodeExpirationMinutes(-1);
        });
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          template.setCodeExpirationMinutes(91);
        });
  }

  @Test
  public void authenticationTemplateShouldOnlyAllowAuthenticationCategory() {
    AuthenticationTemplate template = createTemplate();
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          template.setCategory(TemplateCategory.MARKETING);
        });
  }

  @Test
  public void authenticationTemplateShouldNotAllowAddingButtons() {
    AuthenticationTemplate template = createTemplate();
    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          template.addButton(new QuickReplyButton("Test"));
        });
  }

  @Test
  public void authenticationTemplateShouldAddSecurityRecommendation() {
    AuthenticationTemplate template = createTemplate();
    template.setAddSecurityRecommendation(true);
    assertEquals(
        "{{1}} is your verification code. For your security, do not share this code.",
        template.getBody());

    template.setAddSecurityRecommendation(false);
    assertEquals("{{1}} is your verification code.", template.getBody());
  }

  @Test
  public void authenticationTemplateShouldForciblyOverwriteBodyAndVariables() {
    AuthenticationTemplate template = createTemplate();
    template.setBody("Custom template body {{1}}");
    assertEquals("{{1}} is your verification code.", template.getBody());

    template.setVariableExamples(java.util.List.of("different_var"));
    assertEquals("123456", template.getVariableExamples().get(0));
  }

  @Test
  public void authenticationTemplateShouldAlwaysBeOfTypeText() {
    AuthenticationTemplate template = createTemplate();
    assertEquals(TemplateType.TEXT, template.getTemplateType());

    // Should throw exception if trying to change it to something else
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          template.setTemplateType(TemplateType.IMAGE);
        });
  }

  @Test
  public void authenticationTemplateShouldNotAllowHeader() {
    AuthenticationTemplate template = createTemplate();

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          template.setHeader("Header text");
        });
  }

  @Test
  public void authenticationTemplateShouldAutoGenerateFooterAndIgnoreCustomFooter() {
    AuthenticationTemplate template = createTemplate();

    // Default: 0 expiration minutes -> footer is null
    assertEquals(0, template.getCodeExpirationMinutes());
    assertNull(template.getFooter());

    // 1 expiration minute -> footer is "This code expires in 1 minute"
    template.setCodeExpirationMinutes(1);
    assertEquals("This code expires in 1 minute", template.getFooter());

    // 10 expiration minutes -> footer is "This code expires in 10 minutes"
    template.setCodeExpirationMinutes(10);
    assertEquals("This code expires in 10 minutes", template.getFooter());

    // Custom setFooter is ignored, footer remains correct
    template.setFooter("Custom Footer");
    assertEquals("This code expires in 10 minutes", template.getFooter());
  }
}
