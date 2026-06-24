package io.github.mrcloss.gupshup.client;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mrcloss.gupshup.domain.message.LocationPayload;
import io.github.mrcloss.gupshup.domain.message.Mpm;
import io.github.mrcloss.gupshup.domain.message.TextPayload;
import io.github.mrcloss.gupshup.infrastructure.dto.request.SendTemplateRequest;
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
}
