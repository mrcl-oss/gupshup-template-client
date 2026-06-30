package io.github.mrcloss.gupshup.client;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mrcloss.gupshup.domain.message.GifPayload;
import io.github.mrcloss.gupshup.domain.message.LocationPayload;
import io.github.mrcloss.gupshup.domain.message.Mpm;
import io.github.mrcloss.gupshup.domain.message.TextPayload;
import io.github.mrcloss.gupshup.infrastructure.dto.request.send.SendAuthenticationTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.send.SendGifTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.send.SendLocationTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.send.SendTemplateRequest;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class SendTemplatePayloadTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testMpmPayloadSerialization() throws Exception {
    Mpm.Product p1 = Mpm.Product.builder().productRetailerId("SKU_1").build();
    Mpm.Product p2 = Mpm.Product.builder().productRetailerId("SKU_2").build();
    Mpm.Section section1 =
        Mpm.Section.builder().title("Section 1").products(Arrays.asList(p1, p2)).build();
    Mpm mpm = Mpm.builder().sections(Collections.singletonList(section1)).build();

    SendTemplateRequest request =
        new SendTemplateRequest(
            "src", "dest", "appName", "template-id", Collections.singletonList("param1"), mpm);

    String json = objectMapper.writeValueAsString(request);

    // Verify it contains nested template metadata with mpm inside
    assertTrue(json.contains("\"template\":"));
    assertTrue(json.contains("\"mpm\":"));
    assertTrue(json.contains("\"sections\":"));
    assertTrue(json.contains("\"product_retailer_id\":\"SKU_1\""));
    assertTrue(json.contains("\"product_retailer_id\":\"SKU_2\""));
    assertTrue(json.contains("\"title\":\"Section 1\""));
  }

  @Test
  void testCatalogHelper() {
    SendTemplateRequest request =
        SendTemplateRequest.forCatalog(
            "src",
            "dest",
            "appName",
            "template-id",
            Arrays.asList("param1", "param2"),
            "prod-thumbnail-id");

    assertEquals(3, request.getParams().size());
    assertEquals("param1", request.getParams().get(0));
    assertEquals("param2", request.getParams().get(1));
    assertEquals("prod-thumbnail-id", request.getParams().get(2));
  }

  @Test
  void testTextPayloadSingleAndMultiplePostbacks() throws Exception {
    // Single
    TextPayload single = new TextPayload(0, "payload1");
    String singleJson = objectMapper.writeValueAsString(single);
    assertTrue(singleJson.contains("\"postbackTexts\":{\"index\":0,\"text\":\"payload1\"}"));

    // Multiple
    TextPayload.PostBackTexts pb1 = new TextPayload.PostBackTexts(0, "payload1");
    TextPayload.PostBackTexts pb2 = new TextPayload.PostBackTexts(1, "payload2");
    TextPayload multiple = new TextPayload(Arrays.asList(pb1, pb2));
    String multipleJson = objectMapper.writeValueAsString(multiple);
    assertTrue(
        multipleJson.contains(
            "\"postbackTexts\":[{\"index\":0,\"text\":\"payload1\"},{\"index\":1,\"text\":\"payload2\"}]"));
  }

  @Test
  void testLocationPayloadSingleAndMultiplePostbacks() throws Exception {
    // Single
    LocationPayload single = new LocationPayload(12.3, 45.6, "name", "address", 0, "payload1");
    String singleJson = objectMapper.writeValueAsString(single);
    assertTrue(singleJson.contains("\"postbackTexts\":{\"index\":0,\"text\":\"payload1\"}"));

    // Multiple
    LocationPayload.PostBackTexts pb1 = new LocationPayload.PostBackTexts(0, "payload1");
    LocationPayload.PostBackTexts pb2 = new LocationPayload.PostBackTexts(1, "payload2");
    LocationPayload multiple =
        new LocationPayload(12.3, 45.6, "name", "address", Arrays.asList(pb1, pb2));
    String multipleJson = objectMapper.writeValueAsString(multiple);
    assertTrue(
        multipleJson.contains(
            "\"postbackTexts\":[{\"index\":0,\"text\":\"payload1\"},{\"index\":1,\"text\":\"payload2\"}]"));
  }

  @Test
  void testLocationPayloadWithoutPostbacksSerialization() throws Exception {
    LocationPayload locationMessage =
        new LocationPayload(42.5950661, -79.0896492, "1513  Farnum Road", "New York 10019");
    String json = objectMapper.writeValueAsString(locationMessage);

    assertTrue(json.contains("\"type\":\"location\""));
    assertTrue(json.contains("\"location\":"));
    assertTrue(json.contains("\"name\":\"1513  Farnum Road\""));
    assertTrue(json.contains("\"address\":\"New York 10019\""));
    assertTrue(json.contains("\"latitude\":42.5950661"));
    assertTrue(json.contains("\"longitude\":-79.0896492"));
    assertFalse(json.contains("postbackTexts"));
  }

  @Test
  void testSendTemplateRequestWithHeaderParamsAndButtonParams() throws Exception {
    SendTemplateRequest request =
        new SendTemplateRequest(
            "src",
            "dest",
            "appName",
            "template-id",
            Arrays.asList("headerVal"),
            Arrays.asList("bodyVal1", "bodyVal2"),
            Arrays.asList("buttonVal"));

    assertEquals(4, request.getFinalParams().size());
    assertEquals("headerVal", request.getFinalParams().get(0));
    assertEquals("bodyVal1", request.getFinalParams().get(1));
    assertEquals("bodyVal2", request.getFinalParams().get(2));
    assertEquals("buttonVal", request.getFinalParams().get(3));

    String json = objectMapper.writeValueAsString(request);
    assertTrue(json.contains("\"params\":[\"headerVal\",\"bodyVal1\",\"bodyVal2\",\"buttonVal\"]"));
  }

  @Test
  void testSendLocationTemplateRequestSubclass() throws Exception {
    SendLocationTemplateRequest request =
        new SendLocationTemplateRequest(
            "src",
            "dest",
            "appName",
            "template-id",
            Arrays.asList("bodyVal"),
            Arrays.asList("buttonVal"),
            42.5950661,
            -79.0896492,
            "1513  Farnum Road",
            "New York 10019");

    assertEquals(2, request.getFinalParams().size());
    assertEquals("bodyVal", request.getFinalParams().get(0));
    assertEquals("buttonVal", request.getFinalParams().get(1));

    assertNotNull(request.getMessage());
    assertTrue(request.getMessage() instanceof LocationPayload);
    LocationPayload loc = (LocationPayload) request.getMessage();
    assertEquals("1513  Farnum Road", loc.getLocation().getName());
    assertEquals("New York 10019", loc.getLocation().getAddress());

    String json = objectMapper.writeValueAsString(request);
    assertTrue(json.contains("\"type\":\"location\""));
  }

  @Test
  void testSendAuthenticationTemplateRequestSerialization() throws Exception {
    SendAuthenticationTemplateRequest request =
        new SendAuthenticationTemplateRequest(
            "34977900141",
            "34689395507",
            "PruebasManhattan",
            "9f8755b1-05a9-4443-ac68-80e5b749e96c",
            "123456");

    assertEquals(2, request.getFinalParams().size());
    assertEquals("123456", request.getFinalParams().get(0));
    assertEquals("123456", request.getFinalParams().get(1));
    assertNull(request.getMessage());

    String json = objectMapper.writeValueAsString(request);
    assertTrue(json.contains("\"template\":{"));
    assertTrue(json.contains("\"id\":\"9f8755b1-05a9-4443-ac68-80e5b749e96c\""));
    assertTrue(json.contains("\"params\":[\"123456\",\"123456\"]"));
    assertFalse(json.contains("\"message\""));
  }

  @Test
  void testSendGifTemplateRequestSerialization() throws Exception {
    GifPayload gifPayload = new GifPayload("https://example.com/animation.gif", null);
    SendGifTemplateRequest request =
        new SendGifTemplateRequest(
            "34977900141",
            "34689395507",
            "PruebasManhattan",
            "gif-template-id",
            Arrays.asList("bodyVal"),
            gifPayload);

    assertEquals(1, request.getFinalParams().size());
    assertEquals("bodyVal", request.getFinalParams().get(0));
    assertNotNull(request.getMessage());
    assertTrue(request.getMessage() instanceof GifPayload);

    String json = objectMapper.writeValueAsString(request);
    assertTrue(json.contains("\"type\":\"gif\""));
    assertTrue(json.contains("\"gif\":{"));
    assertTrue(json.contains("\"link\":\"https://example.com/animation.gif\""));
  }
}
