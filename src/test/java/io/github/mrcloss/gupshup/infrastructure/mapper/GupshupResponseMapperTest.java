package io.github.mrcloss.gupshup.infrastructure.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.mrcloss.gupshup.domain.button.*;
import io.github.mrcloss.gupshup.domain.enums.*;
import io.github.mrcloss.gupshup.domain.template.*;
import io.github.mrcloss.gupshup.infrastructure.dto.response.GupshupTemplateDetails;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class GupshupResponseMapperTest {

  private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

  @Test
  void testMapNull() {
    assertNull(GupshupResponseMapper.map(null));
  }

  @Test
  void testMapFallbackPlainBodyText() {
    GupshupTemplateDetails gt = new GupshupTemplateDetails();
    gt.setAppId("app-123");
    gt.setElementName("hello_template");
    gt.setTemplateType("TEXT");
    gt.setCategory("MARKETING");
    gt.setLanguageCode("en_US");
    gt.setData("Hello World! This is a simple body text.");

    Template result = GupshupResponseMapper.map(gt);

    assertTrue(result instanceof TextTemplate);
    assertEquals("app-123", result.getAppId());
    assertEquals("hello_template", result.getElementName());
    assertEquals(TemplateType.TEXT, result.getTemplateType());
    assertEquals(TemplateCategory.MARKETING, result.getCategory());
    assertEquals(LanguageCode.of("en_US"), result.getLanguageCode());
    assertEquals("Hello World! This is a simple body text.", result.getBody());
    assertNull(result.getVariableExamples());
  }

  @Test
  void testMapTextTemplateWithJsonData() throws Exception {
    GupshupTemplateDetails gt = new GupshupTemplateDetails();
    gt.setAppId("app-123");
    gt.setElementName("order_confirmed");
    gt.setTemplateType("TEXT");
    gt.setCategory("UTILITY");
    gt.setLanguageCode("en");
    gt.setParameterFormat("POSITIONAL");
    gt.setStatus("APPROVED");
    gt.setReason("Approved automatically");
    gt.setCreatedOn(Instant.ofEpochMilli(1687164143000L)); // Milliseconds
    gt.setModifiedOn(Instant.ofEpochSecond(1687164200L)); // Seconds

    // Build the stringified JSON data
    String jsonData =
        "{\"body\":\"Hi {{1}}, your order {{2}} is confirmed!\",\"footer\":\"Thanks for"
            + " shopping!\",\"header\":\"Order Confirmation {{1}}\","
            + "\"variableExamples\":[\"John\",\"#98765\"],\"variableHeaderExamples\":[\"Store\"],"
            + "\"buttons\":[  {\"type\":\"QUICK_REPLY\",\"text\":\"Track Order\"}, "
            + " {\"type\":\"PHONE_NUMBER\",\"text\":\"Call"
            + " Support\",\"phoneNumber\":\"+15550199\"},  {\"type\":\"URL\",\"text\":\"Visit"
            + " Shop\",\"url\":\"https://example.com/shop\"}]}";
    gt.setData(jsonData);

    Template result = GupshupResponseMapper.map(gt);

    assertTrue(result instanceof TextTemplate);
    TextTemplate textTemplate = (TextTemplate) result;

    assertEquals("Hi {{1}}, your order {{2}} is confirmed!", textTemplate.getBody());
    assertEquals("Thanks for shopping!", textTemplate.getFooter());
    assertEquals("Order Confirmation {{1}}", textTemplate.getHeader());
    assertEquals(Arrays.asList("John", "#98765"), textTemplate.getVariableExamples());
    assertEquals(Collections.singletonList("Store"), textTemplate.getVariableHeaderExamples());
    assertEquals(TemplateStatus.APPROVED, textTemplate.getStatus());
    assertEquals("Approved automatically", textTemplate.getReason());
    assertEquals(Instant.ofEpochMilli(1687164143000L), textTemplate.getCreatedOn());
    assertEquals(Instant.ofEpochSecond(1687164200L), textTemplate.getModifiedOn());

    List<Button> buttons = textTemplate.getButtons();
    assertEquals(3, buttons.size());

    assertTrue(buttons.get(0) instanceof QuickReplyButton);
    assertEquals("Track Order", buttons.get(0).getText());

    assertTrue(buttons.get(1) instanceof PhoneNumberButton);
    assertEquals("Call Support", buttons.get(1).getText());
    assertEquals("+15550199", ((PhoneNumberButton) buttons.get(1)).getPhoneNumber());

    assertTrue(buttons.get(2) instanceof StaticUrlButton);
    assertEquals("Visit Shop", buttons.get(2).getText());
    assertEquals("https://example.com/shop", ((StaticUrlButton) buttons.get(2)).getUrl());
  }

  @Test
  void testMapImageTemplate() {
    GupshupTemplateDetails gt = new GupshupTemplateDetails();
    gt.setAppId("app-123");
    gt.setElementName("image_promo");
    gt.setTemplateType("IMAGE");
    gt.setCategory("MARKETING");
    gt.setLanguageCode("es_ES");
    gt.setVertical("retail,shopping");

    String jsonData =
        "{"
            + "\"body\":\"Take a look at our new collection!\","
            + "\"mediaUrl\":\"https://example.com/image.jpg\","
            + "\"mediaId\":\"image-id-xyz\""
            + "}";
    gt.setData(jsonData);

    Template result = GupshupResponseMapper.map(gt);

    assertTrue(result instanceof ImageTemplate);
    ImageTemplate imageTemplate = (ImageTemplate) result;

    assertEquals("Take a look at our new collection!", imageTemplate.getBody());
    assertEquals("https://example.com/image.jpg", imageTemplate.getMediaUrl());
    assertEquals("image-id-xyz", imageTemplate.getMediaId());
    assertEquals(Arrays.asList("retail", "shopping"), imageTemplate.getTags());
  }

  @Test
  void testMapVideoTemplate() {
    GupshupTemplateDetails gt = new GupshupTemplateDetails();
    gt.setTemplateType("VIDEO");
    gt.setData("{\"body\":\"Watch this video.\",\"mediaUrl\":\"https://example.com/video.mp4\"}");

    Template result = GupshupResponseMapper.map(gt);

    assertTrue(result instanceof VideoTemplate);
    VideoTemplate videoTemplate = (VideoTemplate) result;
    assertEquals("Watch this video.", videoTemplate.getBody());
    assertEquals("https://example.com/video.mp4", videoTemplate.getMediaUrl());
  }

  @Test
  void testMapDocumentTemplate() {
    GupshupTemplateDetails gt = new GupshupTemplateDetails();
    gt.setTemplateType("DOCUMENT");
    gt.setData("{\"body\":\"Read this doc.\",\"mediaUrl\":\"https://example.com/doc.pdf\"}");

    Template result = GupshupResponseMapper.map(gt);

    assertTrue(result instanceof DocumentTemplate);
    DocumentTemplate docTemplate = (DocumentTemplate) result;
    assertEquals("Read this doc.", docTemplate.getBody());
    assertEquals("https://example.com/doc.pdf", docTemplate.getMediaUrl());
  }

  @Test
  void testMapGIFTemplate() {
    GupshupTemplateDetails gt = new GupshupTemplateDetails();
    gt.setTemplateType("GIF");
    gt.setData("{\"body\":\"Funny GIF.\",\"mediaUrl\":\"https://example.com/gif.mp4\"}");

    Template result = GupshupResponseMapper.map(gt);

    assertTrue(result instanceof GIFTemplate);
    GIFTemplate gifTemplate = (GIFTemplate) result;
    assertEquals("Funny GIF.", gifTemplate.getBody());
    assertEquals("https://example.com/gif.mp4", gifTemplate.getMediaUrl());
  }

  @Test
  void testMapLocationTemplate() {
    GupshupTemplateDetails gt = new GupshupTemplateDetails();
    gt.setTemplateType("LOCATION");
    gt.setData("{\"body\":\"Come visit us!\"}");

    Template result = GupshupResponseMapper.map(gt);

    assertTrue(result instanceof LocationTemplate);
    assertEquals("Come visit us!", result.getBody());
  }

  @Test
  void testMapCatalogTemplate() {
    GupshupTemplateDetails gt = new GupshupTemplateDetails();
    gt.setTemplateType("CATALOG");
    gt.setData("{\"body\":\"Check out our catalog!\"}");

    Template result = GupshupResponseMapper.map(gt);

    assertTrue(result instanceof CatalogTemplate);
    assertEquals("Check out our catalog!", result.getBody());
    assertEquals(1, result.getButtons().size());
    assertTrue(result.getButtons().get(0) instanceof CatalogButton);
  }

  @Test
  void testMapProductTemplate() {
    GupshupTemplateDetails gt = new GupshupTemplateDetails();
    gt.setTemplateType("PRODUCT");
    gt.setData("{\"body\":\"Buy our items!\"}");

    Template result = GupshupResponseMapper.map(gt);

    assertTrue(result instanceof ProductTemplate);
    assertEquals("Buy our items!", result.getBody());
    assertEquals(1, result.getButtons().size());
    assertTrue(result.getButtons().get(0) instanceof MPMButton);
  }

  @Test
  void testMapAuthenticationTemplate() {
    GupshupTemplateDetails gt = new GupshupTemplateDetails();
    gt.setTemplateType("TEXT");
    gt.setCategory("AUTHENTICATION");
    gt.setData(
        "{\"body\":\"Your verification code is"
            + " {{1}}.\",\"variableExamples\":[\"998877\"],\"addSecurityRecommendation\":true,\"codeExpirationMinutes\":5}");

    Template result = GupshupResponseMapper.map(gt);

    assertTrue(result instanceof AuthenticationTemplate);
    AuthenticationTemplate authTemplate = (AuthenticationTemplate) result;
    assertTrue(authTemplate.isAddSecurityRecommendation());
    assertEquals(5, authTemplate.getCodeExpirationMinutes());
    assertEquals(
        "Your verification code is {{1}}. For your security, do not share this code.",
        authTemplate.getBody());
    assertEquals(1, authTemplate.getButtons().size());
    assertTrue(authTemplate.getButtons().get(0) instanceof CopyCodeButton);
    assertEquals(
        "This code expires in {{codeExpirationMinutes}} minutes",
        authTemplate.getButtons().get(0).getText());
  }

  @Test
  void testMapAuthenticationTemplateFromButtonTextFallback() {
    GupshupTemplateDetails gt = new GupshupTemplateDetails();
    gt.setTemplateType("TEXT");
    gt.setCategory("AUTHENTICATION");
    gt.setData(
        "{\"body\":\"Code is {{1}}.\",\"buttons\":[{\"type\":\"COPY_CODE\",\"text\":\"This code"
            + " expires in 15 minutes\"}]}");

    Template result = GupshupResponseMapper.map(gt);

    assertTrue(result instanceof AuthenticationTemplate);
    AuthenticationTemplate authTemplate = (AuthenticationTemplate) result;
    assertEquals(15, authTemplate.getCodeExpirationMinutes());
  }

  @Test
  void testMapCarouselTemplate() {
    GupshupTemplateDetails gt = new GupshupTemplateDetails();
    gt.setTemplateType("CAROUSEL");
    gt.setCategory("MARKETING");

    String jsonData =
        "{"
            + "\"body\":\"Select an option:\","
            + "\"cards\":["
            + "  {"
            + "    \"body\":\"First Card Body {{1}}\","
            + "    \"variableExamples\":[\"Card1Var\"],"
            + "    \"headerType\":\"IMAGE\","
            + "    \"mediaUrl\":\"https://example.com/card1.jpg\","
            + "    \"buttons\":[{\"type\":\"QUICK_REPLY\",\"text\":\"Select Card 1\"}]"
            + "  },"
            + "  {"
            + "    \"body\":\"Second Card Body {{1}}\","
            + "    \"variableExamples\":[\"Card2Var\"],"
            + "    \"headerType\":\"IMAGE\","
            + "    \"mediaUrl\":\"https://example.com/card2.jpg\","
            + "    \"buttons\":[{\"type\":\"QUICK_REPLY\",\"text\":\"Select Card 2\"}]"
            + "  }"
            + "]"
            + "}";
    gt.setData(jsonData);

    Template result = GupshupResponseMapper.map(gt);

    assertTrue(result instanceof CarouselTemplate);
    CarouselTemplate carousel = (CarouselTemplate) result;

    assertEquals("Select an option:", carousel.getBody());
    assertEquals(2, carousel.getCards().size());

    CarouselCard card1 = carousel.getCards().get(0);
    assertEquals("First Card Body {{1}}", card1.getBody());
    assertEquals(Collections.singletonList("Card1Var"), card1.getVariableExamples());
    assertEquals(CarouselCard.CarouselCardHeaderType.IMAGE, card1.getHeaderType());
    assertEquals("https://example.com/card1.jpg", card1.getMediaUrl());
    assertEquals(1, card1.getButtons().size());
    assertEquals("Select Card 1", card1.getButtons().get(0).getText());
  }

  @Test
  void testMapButtonsAllTypes() {
    GupshupTemplateDetails gt = new GupshupTemplateDetails();
    gt.setTemplateType("TEXT");

    String jsonData =
        "{\"body\":\"Test buttons.\",\"buttons\":[  {\"type\":\"URL\",\"text\":\"Dynamic"
            + " Link\",\"url\":\"https://example.com/{{1}}\",\"example\":[\"user-123\"]}, "
            + " {\"type\":\"URL\",\"text\":\"Pay Now"
            + " Item\",\"paymentLinkPreview\":true,\"url\":\"https://pay.com/order1\"}]}";
    gt.setData(jsonData);

    Template result = GupshupResponseMapper.map(gt);

    List<Button> buttons = result.getButtons();
    assertEquals(2, buttons.size());

    assertTrue(buttons.get(0) instanceof DynamicUrlButton);
    DynamicUrlButton dynamicBtn = (DynamicUrlButton) buttons.get(0);
    assertEquals("https://example.com/{{1}}", dynamicBtn.getUrlTemplate());
    assertEquals("user-123", dynamicBtn.getVariableExample());

    assertTrue(buttons.get(1) instanceof PayNowButton);
    PayNowButton payNowBtn = (PayNowButton) buttons.get(1);
    assertTrue(payNowBtn.isPaymentLinkPreview());
    assertTrue(payNowBtn.getUnderlyingUrlButton() instanceof StaticUrlButton);
    assertEquals(
        "https://pay.com/order1", ((StaticUrlButton) payNowBtn.getUnderlyingUrlButton()).getUrl());
  }

  @Test
  void testParseOtpButtonDirectly() throws Exception {
    String otpJson = "{\"type\":\"OTP\",\"text\":\"Verification OTP\",\"otpType\":\"COPY_CODE\"}";
    com.fasterxml.jackson.databind.JsonNode node = objectMapper.readTree(otpJson);
    Button parsed = GupshupResponseMapper.parseButton(node, objectMapper);

    assertTrue(parsed instanceof OTPButton);
    assertEquals("Verification OTP", parsed.getText());
    assertEquals(OTPButton.OTPButtonType.COPY_CODE, ((OTPButton) parsed).getOtpType());
  }

  @Test
  void testMapLtoAttributes() {
    GupshupTemplateDetails gt = new GupshupTemplateDetails();
    gt.setTemplateType("TEXT");
    gt.setData(
        "{\"body\":\"Hurry up!\",\"isLTO\":true,\"hasExpiration\":true,\"limitedOfferText\":\"Offer"
            + " ends soon!\"}");

    Template result = GupshupResponseMapper.map(gt);

    assertNotNull(result.getLtoAttributes());
    assertTrue(result.getLtoAttributes().isLTO());
    assertTrue(result.getLtoAttributes().isHasExpiration());
    assertEquals("Offer ends soon!", result.getLtoAttributes().getLimitedOfferText());
  }

  @Test
  void testMapWithContainerMeta() {
    GupshupTemplateDetails gt = new GupshupTemplateDetails();
    gt.setAppId("7ad73d28-4347-4f32-a212-1bdc571d183b");
    gt.setTemplateType("TEXT");
    gt.setCategory("MARKETING");
    gt.setElementName("pruebaborrar");
    gt.setLanguageCode("es");
    gt.setData(
        "Prueba {{1}} a aaa \n"
            + "Esto es el cuerpo {{1}} a\n"
            + "Esto es el pie | [boton1] | [boton2,https://example.com{{1}}]");
    gt.setContainerMeta(
        "{\"appId\":\"7ad73d28-4347-4f32-a212-1bdc571d183b\",\"data\":\"Esto es el cuerpo {{1}}"
            + " a\",\"buttons\":[{\"type\":\"QUICK_REPLY\",\"text\":\"boton1\"},"
            + "{\"type\":\"URL\",\"text\":\"boton2\",\"url\":\"https://example.com{{1}}\",\"example\":[\"https://example.com/hola\"]}],\"header\":\"Prueba"
            + " {{1}} a aaa \",\"footer\":\"Esto es el pie\",\"sampleText\":\"Esto es el cuerpo"
            + " [adios] a\",\"sampleHeader\":\"Prueba [hola] a aaa \",\"enableSample\":true}");

    Template result = GupshupResponseMapper.map(gt);

    assertTrue(result instanceof TextTemplate);
    TextTemplate textTemplate = (TextTemplate) result;

    assertEquals("Esto es el cuerpo {{1}} a", textTemplate.getBody());
    assertEquals("Prueba {{1}} a aaa ", textTemplate.getHeader());
    assertEquals("Esto es el pie", textTemplate.getFooter());

    assertEquals(Collections.singletonList("adios"), textTemplate.getVariableExamples());
    assertEquals(Collections.singletonList("hola"), textTemplate.getVariableHeaderExamples());

    List<Button> buttons = textTemplate.getButtons();
    assertEquals(2, buttons.size());

    assertTrue(buttons.get(0) instanceof QuickReplyButton);
    assertEquals("boton1", buttons.get(0).getText());

    assertTrue(buttons.get(1) instanceof DynamicUrlButton);
    DynamicUrlButton dynBtn = (DynamicUrlButton) buttons.get(1);
    assertEquals("boton2", dynBtn.getText());
    assertEquals("https://example.com{{1}}", dynBtn.getUrlTemplate());
    assertEquals("https://example.com/hola", dynBtn.getVariableExample());
  }

  @Test
  void testMapTemplateStatus() {
    String[] statuses = {
      "APPROVED",
      "PENDING",
      "DESACTIVATED",
      "DEACTIVATED",
      "DISABLED",
      "IN_REVIEW",
      "invalid_status"
    };
    TemplateStatus[] expected = {
      TemplateStatus.APPROVED,
      TemplateStatus.PENDING,
      TemplateStatus.DEACTIVATED,
      TemplateStatus.DEACTIVATED,
      TemplateStatus.DISABLED,
      TemplateStatus.IN_REVIEW,
      null
    };

    for (int i = 0; i < statuses.length; i++) {
      GupshupTemplateDetails gt = new GupshupTemplateDetails();
      gt.setAppId("app-123");
      gt.setElementName("test_status_" + i);
      gt.setTemplateType("TEXT");
      gt.setCategory("MARKETING");
      gt.setLanguageCode("en");
      gt.setData("Hello World");
      gt.setStatus(statuses[i]);

      Template result = GupshupResponseMapper.map(gt);
      assertEquals(expected[i], result.getStatus(), "Failed mapping for status: " + statuses[i]);
    }
  }

  @Test
  void testTemplateStatusJacksonDeserialization() throws Exception {
    String jsonApproved = "\"APPROVED\"";
    String jsonPending = "\"PENDING\"";
    String jsonDesactivated = "\"DESACTIVATED\"";
    String jsonDeactivated = "\"DEACTIVATED\"";
    String jsonDisabled = "\"DISABLED\"";
    String jsonInReview = "\"IN_REVIEW\"";

    assertEquals(
        TemplateStatus.APPROVED, objectMapper.readValue(jsonApproved, TemplateStatus.class));
    assertEquals(TemplateStatus.PENDING, objectMapper.readValue(jsonPending, TemplateStatus.class));
    assertEquals(
        TemplateStatus.DEACTIVATED, objectMapper.readValue(jsonDesactivated, TemplateStatus.class));
    assertEquals(
        TemplateStatus.DEACTIVATED, objectMapper.readValue(jsonDeactivated, TemplateStatus.class));
    assertEquals(
        TemplateStatus.DISABLED, objectMapper.readValue(jsonDisabled, TemplateStatus.class));
    assertEquals(
        TemplateStatus.IN_REVIEW, objectMapper.readValue(jsonInReview, TemplateStatus.class));
  }
}
