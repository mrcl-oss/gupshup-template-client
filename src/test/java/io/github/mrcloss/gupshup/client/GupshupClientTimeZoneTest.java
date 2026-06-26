package io.github.mrcloss.gupshup.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mrcloss.gupshup.infrastructure.dto.response.GetTemplateResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class GupshupClientTimeZoneTest {

  private final String appId = "test-app";
  private final String apiKey = "test-key";

  @Test
  void testUtcStringParsing() {
    DefaultGupshupClient client1 =
        DefaultGupshupClient.builder().appId(appId).apiKey(apiKey).utc("+02:00").build();
    assertEquals(ZoneOffset.of("+02:00"), client1.getZoneId());

    DefaultGupshupClient client2 =
        DefaultGupshupClient.builder().appId(appId).apiKey(apiKey).utc("2").build();
    assertEquals(ZoneOffset.ofHours(2), client2.getZoneId());

    DefaultGupshupClient client3 =
        DefaultGupshupClient.builder().appId(appId).apiKey(apiKey).utc("-5").build();
    assertEquals(ZoneOffset.ofHours(-5), client3.getZoneId());

    DefaultGupshupClient client4 =
        DefaultGupshupClient.builder().appId(appId).apiKey(apiKey).utc("Europe/Madrid").build();
    assertEquals(ZoneId.of("Europe/Madrid"), client4.getZoneId());

    DefaultGupshupClient clientNull =
        DefaultGupshupClient.builder().appId(appId).apiKey(apiKey).utc((String) null).build();
    assertNull(clientNull.getZoneId());
  }

  @Test
  void testObjectMapperTimeZoneIntegration() throws Exception {
    DefaultGupshupClient client =
        DefaultGupshupClient.builder().appId(appId).apiKey(apiKey).utc("+02:00").build();

    java.lang.reflect.Field field = DefaultGupshupClient.class.getDeclaredField("objectMapper");
    field.setAccessible(true);
    ObjectMapper mapper = (ObjectMapper) field.get(client);

    String json = "{\"template\": {\"createdOn\": 1687164143000}}";
    GetTemplateResponse response = mapper.readValue(json, GetTemplateResponse.class);

    // 1687164143000L = 2023-06-19T08:42:23Z
    // In UTC+2 (configured), this should be adjusted to 2023-06-19T10:42:23Z
    Instant expected = Instant.parse("2023-06-19T10:42:23Z");
    assertEquals(expected, response.getTemplate().getCreatedOn());
  }
}
