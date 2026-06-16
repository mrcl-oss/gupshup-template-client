package io.github.mrcloss.gupshup.infrastructure.mapper;

import static org.junit.jupiter.api.Assertions.*;

import io.github.mrcloss.gupshup.domain.button.PayNowButton;
import io.github.mrcloss.gupshup.domain.button.PhoneNumberButton;
import io.github.mrcloss.gupshup.domain.button.StaticUrlButton;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.template.AuthenticationTemplate;
import io.github.mrcloss.gupshup.domain.template.CarouselCard;
import io.github.mrcloss.gupshup.domain.template.CarouselTemplate;
import io.github.mrcloss.gupshup.domain.template.TextTemplate;
import io.github.mrcloss.gupshup.infrastructure.dto.request.*;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class GupshupRequestMapperTest {

  @Test
  void testMapTextTemplateWithVariablesAndButtons() {
    TextTemplate template =
        new TextTemplate(
            "test_template",
            LanguageCode.ENGLISH,
            "Hello {{1}}, welcome to {{2}}!",
            TemplateCategory.MARKETING,
            "app-123",
            Arrays.asList("tag1", "tag2"),
            TemplateParameterFormat.POSITIONAL);
    template.setVariableExamples(Arrays.asList("John", "MyStore"));
    template.setHeader("News from {{1}}!");
    template.setVariableHeaderExamples(Collections.singletonList("Gupshup"));
    template.setFooter("Footer text");

    StaticUrlButton staticBtn = new StaticUrlButton("Visit", "https://example.com");
    PhoneNumberButton phoneBtn = new PhoneNumberButton("Call", "1234567890");

    PayNowButton payNowBtn =
        new PayNowButton("Pay", true, new StaticUrlButton("Pay", "https://pay.com"));

    template.setButtons(Arrays.asList(staticBtn, phoneBtn, payNowBtn));

    TemplateRequest request = GupshupRequestMapper.map(template);

    assertTrue(request instanceof TextTemplateRequest);
    TextTemplateRequest textRequest = (TextTemplateRequest) request;

    assertEquals("app-123", textRequest.getAppId());
    assertEquals(TemplateCategory.MARKETING, textRequest.getCategory());
    assertEquals("Hello {{1}}, welcome to {{2}}!", textRequest.getContent());
    assertEquals("Hello [John], welcome to [MyStore]!", textRequest.getExample());
    assertEquals("test_template", textRequest.getElementName());
    assertEquals(LanguageCode.ENGLISH, textRequest.getLanguageCode());
    assertEquals("tag1,tag2", textRequest.getVertical());
    assertEquals("Footer text", textRequest.getFooter());

    assertEquals("News from {{1}}!", textRequest.getHeader());
    assertEquals("News from [Gupshup]!", textRequest.getExampleHeader());

    assertEquals(3, textRequest.getButtons().size());

    UrlButtonRequest b1 = (UrlButtonRequest) textRequest.getButtons().get(0);
    assertEquals("Visit", b1.getText());
    assertEquals("https://example.com", b1.getUrl());
    assertNull(b1.getExample());

    PhoneNumberButtonRequest b2 = (PhoneNumberButtonRequest) textRequest.getButtons().get(1);
    assertEquals("Call", b2.getText());
    assertEquals("1234567890", b2.getPhoneNumber());

    UrlButtonRequest b3 = (UrlButtonRequest) textRequest.getButtons().get(2);
    assertEquals("Pay", b3.getText());
    assertEquals("https://pay.com", b3.getUrl());
    assertTrue(b3.getPaymentLinkPreview());
  }

  @Test
  void testMapAuthenticationTemplate() {
    AuthenticationTemplate template =
        new AuthenticationTemplate(
            "auth_template",
            LanguageCode.ENGLISH,
            "Your code is {{1}}.",
            "app-123",
            Collections.singletonList("auth"),
            TemplateParameterFormat.POSITIONAL);
    template.setVariableExamples(Collections.singletonList("123456"));
    template.setAddSecurityRecommendation(true);

    TemplateRequest request = GupshupRequestMapper.map(template);

    assertTrue(request instanceof AuthenticationTemplateRequest);
    assertEquals(
        "Your code is {{1}}. For your security, do not share this code.", request.getContent());
    assertEquals(
        "Your code is [123456]. For your security, do not share this code.", request.getExample());
    assertEquals(1, request.getButtons().size());
    assertTrue(request.getButtons().get(0) instanceof CopyCodeButtonRequest);
    assertEquals("123456", ((CopyCodeButtonRequest) request.getButtons().get(0)).getExample());
  }

  @Test
  void testMapCarouselTemplate() {
    CarouselTemplate template =
        new CarouselTemplate(
            "carousel_template",
            LanguageCode.ENGLISH,
            "Carousel body",
            TemplateCategory.MARKETING,
            "app-123",
            null,
            TemplateParameterFormat.POSITIONAL);

    CarouselCard card = new CarouselCard();
    card.setBody("Card body {{1}}!");
    card.setVariableExamples(Collections.singletonList("Value"));
    card.setHeaderType(CarouselCard.CarouselCardHeaderType.IMAGE);
    card.setMediaUrl("https://example.com/image.png");
    card.setButtons(Collections.singletonList(new StaticUrlButton("Visit", "https://example.com")));

    template.setCards(new java.util.ArrayList<>(Collections.singletonList(card)));

    TemplateRequest request = GupshupRequestMapper.map(template);

    assertTrue(request instanceof CarouselTemplateRequest);
    CarouselTemplateRequest carouselRequest = (CarouselTemplateRequest) request;
    assertEquals(1, carouselRequest.getCards().size());
    assertEquals("Card body {{1}}!", carouselRequest.getCards().get(0).getContent());
    assertEquals("Card body [Value]!", carouselRequest.getCards().get(0).getSampleText());
  }
}
