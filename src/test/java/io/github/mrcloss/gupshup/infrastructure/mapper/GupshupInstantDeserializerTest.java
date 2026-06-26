package io.github.mrcloss.gupshup.infrastructure.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

class GupshupInstantDeserializerTest {

  @Test
  void testDeserializeEpochMilliUTC0() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(Instant.class, new GupshupInstantDeserializer());
    mapper.registerModule(module);

    String json = "\"1687164143000\"";
    Instant instant = mapper.readValue(json, Instant.class);

    assertEquals(Instant.ofEpochMilli(1687164143000L), instant);
  }

  @Test
  void testDeserializeEpochSecondUTC0() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(Instant.class, new GupshupInstantDeserializer());
    mapper.registerModule(module);

    String json = "\"1687164200\"";
    Instant instant = mapper.readValue(json, Instant.class);

    assertEquals(Instant.ofEpochSecond(1687164200L), instant);
  }

  @Test
  void testDeserializeWithZoneIdOffset() throws Exception {
    // Madrid is UTC+2 in June (DST)
    ZoneId madridZone = ZoneId.of("Europe/Madrid");
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(Instant.class, new GupshupInstantDeserializer(madridZone));
    mapper.registerModule(module);

    // 1687164143000L = 2023-06-19T08:42:23Z
    // In Europe/Madrid, this is 10:42:23.
    // So the adjusted instant should be 2023-06-19T10:42:23Z
    String json = "\"1687164143000\"";
    Instant instant = mapper.readValue(json, Instant.class);

    Instant expected = Instant.parse("2023-06-19T10:42:23Z");
    assertEquals(expected, instant);
  }

  @Test
  void testDeserializeStringIso() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(Instant.class, new GupshupInstantDeserializer(ZoneId.of("UTC")));
    mapper.registerModule(module);

    String json = "\"2023-06-19T08:42:23Z\"";
    Instant instant = mapper.readValue(json, Instant.class);

    assertEquals(Instant.parse("2023-06-19T08:42:23Z"), instant);
  }

  @Test
  void testDeserializeNull() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(Instant.class, new GupshupInstantDeserializer());
    mapper.registerModule(module);

    String json = "null";
    Instant instant = mapper.readValue(json, Instant.class);

    assertNull(instant);
  }
}
