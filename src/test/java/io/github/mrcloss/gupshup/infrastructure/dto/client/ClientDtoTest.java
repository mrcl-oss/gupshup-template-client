package io.github.mrcloss.gupshup.infrastructure.dto.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
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
}
