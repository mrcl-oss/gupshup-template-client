package io.github.mrcloss.gupshup.infrastructure.mapper;

import static org.junit.jupiter.api.Assertions.*;

import io.github.mrcloss.gupshup.domain.enums.ButtonType;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import io.github.mrcloss.gupshup.domain.template.ImageTemplate;
import io.github.mrcloss.gupshup.domain.template.Template;
import io.github.mrcloss.gupshup.domain.template.TextTemplate;
import io.github.mrcloss.gupshup.infrastructure.dto.request.ButtonRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.ImageTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.TextTemplateRequest;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class GupshupRequestToDomainMapperTest {

  @Test
  public void testMapTextRequestToDomain() {
    TextTemplateRequest request = new TextTemplateRequest();
    request.setElementName("test_text");
    request.setLanguageCode(LanguageCode.SPANISH);
    request.setContent("Hola {{1}}, tu código es {{2}}.");
    request.setExample("Hola [Marc], tu código es [1234].");
    request.setCategory(TemplateCategory.MARKETING);
    request.setTemplateType(TemplateType.TEXT);
    request.setHeader("¡Hola!");
    request.setExampleHeader("¡Hola!");

    Template template = GupshupRequestToDomainMapper.map(request);

    assertNotNull(template);
    assertInstanceOf(TextTemplate.class, template);
    TextTemplate textTemplate = (TextTemplate) template;

    assertEquals("test_text", textTemplate.getElementName());
    assertEquals(LanguageCode.SPANISH, textTemplate.getLanguageCode());
    assertEquals("Hola {{1}}, tu código es {{2}}.", textTemplate.getBody());
    assertEquals("¡Hola!", textTemplate.getHeader());

    assertNotNull(textTemplate.getVariableExamples());
    assertEquals(2, textTemplate.getVariableExamples().size());
    assertEquals("Marc", textTemplate.getVariableExamples().get(0));
    assertEquals("1234", textTemplate.getVariableExamples().get(1));
  }

  @Test
  public void testMapImageRequestToDomain() {
    ImageTemplateRequest request = new ImageTemplateRequest();
    request.setElementName("test_image");
    request.setLanguageCode(LanguageCode.ENGLISH);
    request.setContent("Hello!");
    request.setCategory(TemplateCategory.UTILITY);
    request.setTemplateType(TemplateType.IMAGE);
    request.setMediaUrl("https://example.com/image.png");

    ButtonRequest btn = new ButtonRequest();
    btn.setType(ButtonType.QUICK_REPLY);
    btn.setText("Click me");
    request.setButtons(Collections.singletonList(btn));

    Template template = GupshupRequestToDomainMapper.map(request);

    assertNotNull(template);
    assertInstanceOf(ImageTemplate.class, template);
    ImageTemplate imageTemplate = (ImageTemplate) template;

    assertEquals("test_image", imageTemplate.getElementName());
    assertEquals("https://example.com/image.png", imageTemplate.getMediaUrl());
    assertEquals(1, imageTemplate.getButtons().size());
    assertEquals("Click me", imageTemplate.getButtons().get(0).getText());
  }
}
