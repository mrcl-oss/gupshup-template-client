package io.github.mrcloss.gupshup.infrastructure.dto.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mrcloss.gupshup.domain.button.Button;
import io.github.mrcloss.gupshup.domain.button.StaticUrlButton;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import io.github.mrcloss.gupshup.domain.template.ImageTemplate;
import io.github.mrcloss.gupshup.domain.template.TextTemplate;
import org.junit.jupiter.api.Test;

public class ClientDtoTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void testTextTemplateDtoPolymorphismAndMapping() throws Exception {
    String json =
        "{"
            + "\"templateType\":\"TEXT\","
            + "\"elementName\":\"welcome_msg\","
            + "\"languageCode\":\"ES\","
            + "\"body\":\"Hola {{1}}, bienvenido!\","
            + "\"category\":\"MARKETING\","
            + "\"header\":\"¡Hola!\","
            + "\"variableExamples\":[\"Marc\"],"
            + "\"variableHeaderExamples\":[]"
            + "}";

    BaseTemplateDto dto = objectMapper.readValue(json, BaseTemplateDto.class);

    assertNotNull(dto);
    assertInstanceOf(TextTemplateDto.class, dto);
    TextTemplateDto textDto = (TextTemplateDto) dto;
    assertEquals("welcome_msg", textDto.getElementName());
    assertEquals(LanguageCode.SPANISH, textDto.getLanguageCode());
    assertEquals("¡Hola!", textDto.getHeader());

    // Map to domain template
    TextTemplate domain = textDto.toDomain();
    assertNotNull(domain);
    assertEquals("welcome_msg", domain.getElementName());
    assertEquals(LanguageCode.SPANISH, domain.getLanguageCode());
    assertEquals("¡Hola!", domain.getHeader());
    assertEquals(TemplateType.TEXT, domain.getTemplateType());
    assertEquals(TemplateCategory.MARKETING, domain.getCategory());
  }

  @Test
  public void testImageTemplateDtoPolymorphismAndMapping() throws Exception {
    String json =
        "{"
            + "\"templateType\":\"IMAGE\","
            + "\"elementName\":\"promo_img\","
            + "\"languageCode\":\"EN\","
            + "\"body\":\"Check this out!\","
            + "\"category\":\"UTILITY\","
            + "\"mediaUrl\":\"https://example.com/promo.png\""
            + "}";

    BaseTemplateDto dto = objectMapper.readValue(json, BaseTemplateDto.class);

    assertNotNull(dto);
    assertInstanceOf(ImageTemplateDto.class, dto);
    ImageTemplateDto imgDto = (ImageTemplateDto) dto;
    assertEquals("promo_img", imgDto.getElementName());
    assertEquals("https://example.com/promo.png", imgDto.getMediaUrl());

    // Map to domain template
    ImageTemplate domain = imgDto.toDomain();
    assertNotNull(domain);
    assertEquals("promo_img", domain.getElementName());
    assertEquals("https://example.com/promo.png", domain.getMediaUrl());
    assertEquals(TemplateType.IMAGE, domain.getTemplateType());
  }

  @Test
  public void testAuthenticationTemplateDtoPolymorphismAndMapping() throws Exception {
    String json =
        "{"
            + "\"templateType\":\"AUTHENTICATION\","
            + "\"elementName\":\"auth_otp\","
            + "\"languageCode\":\"EN\","
            + "\"body\":\"Your verification code is {{1}}.\","
            + "\"category\":\"AUTHENTICATION\","
            + "\"addSecurityRecommendation\":true,"
            + "\"codeExpirationMinutes\":10"
            + "}";

    BaseTemplateDto dto = objectMapper.readValue(json, BaseTemplateDto.class);

    assertNotNull(dto);
    assertInstanceOf(AuthenticationTemplateDto.class, dto);
    AuthenticationTemplateDto authDto = (AuthenticationTemplateDto) dto;
    assertEquals("auth_otp", authDto.getElementName());
    assertEquals(true, authDto.isAddSecurityRecommendation());
    assertEquals(10, authDto.getCodeExpirationMinutes());

    // Map to domain template
    var domain = authDto.toDomain();
    assertNotNull(domain);
    assertEquals("auth_otp", domain.getElementName());
    assertEquals(TemplateType.TEXT, domain.getTemplateType());
  }

  @Test
  public void testCatalogTemplateDtoWithButtonsDeserialization() throws Exception {
    String json =
        "{"
            + "\"templateType\":\"CATALOG\","
            + "\"elementName\":\"my_catalog\","
            + "\"languageCode\":\"EN\","
            + "\"body\":\"Here is our catalog\","
            + "\"category\":\"UTILITY\","
            + "\"buttons\":["
            + "  {\"type\":\"URL\",\"text\":\"Visit Shop\",\"url\":\"https://example.com/shop\"},"
            + "  {\"type\":\"QUICK_REPLY\",\"text\":\"Help\"}"
            + "]"
            + "}";

    BaseTemplateDto dto = objectMapper.readValue(json, BaseTemplateDto.class);

    assertNotNull(dto);
    assertInstanceOf(CatalogTemplateDto.class, dto);
    CatalogTemplateDto catalogDto = (CatalogTemplateDto) dto;
    assertEquals("my_catalog", catalogDto.getElementName());
    assertNotNull(catalogDto.getButtons());
    assertEquals(2, catalogDto.getButtons().size());

    Button button1 = catalogDto.getButtons().get(0);
    assertInstanceOf(StaticUrlButton.class, button1);
    assertEquals("Visit Shop", button1.getText());
    assertEquals("https://example.com/shop", ((StaticUrlButton) button1).getUrl());

    Button button2 = catalogDto.getButtons().get(1);
    assertInstanceOf(io.github.mrcloss.gupshup.domain.button.QuickReplyButton.class, button2);
    assertEquals("Help", button2.getText());
  }
}
