package io.github.mrcloss.gupshup.infrastructure.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * Custom Jackson deserializer to map Unix timestamps (in milliseconds or seconds) to {@link
 * Instant} values, adjusting them to the client's configured UTC offset if specified.
 */
public class GupshupInstantDeserializer extends JsonDeserializer<Instant> {

  private final ZoneId zoneId;

  /** Constructs a new deserializer with no timezone adjustment (uses UTC0). */
  public GupshupInstantDeserializer() {
    this(null);
  }

  /**
   * Constructs a new deserializer that adjusts mapped instants to the specified timezone.
   *
   * @param zoneId the target timezone to adjust the instant value to
   */
  public GupshupInstantDeserializer(ZoneId zoneId) {
    this.zoneId = zoneId;
  }

  @Override
  public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
      long timestamp = p.getLongValue();
      return toInstant(timestamp, ctxt);
    }

    String value = p.getText();
    if (value == null || value.trim().isEmpty() || "null".equalsIgnoreCase(value.trim())) {
      return null;
    }

    try {
      long timestamp = Long.parseLong(value.trim());
      return toInstant(timestamp, ctxt);
    } catch (NumberFormatException e) {
      Instant instant = Instant.parse(value);
      ZoneId targetZone = getZoneId(ctxt);
      if (targetZone != null) {
        ZoneOffset offset = targetZone.getRules().getOffset(instant);
        instant = instant.plusSeconds(offset.getTotalSeconds());
      }
      return instant;
    }
  }

  private Instant toInstant(long timestamp, DeserializationContext ctxt) {
    Instant instant =
        timestamp > 9999999999L
            ? Instant.ofEpochMilli(timestamp)
            : Instant.ofEpochSecond(timestamp);
    ZoneId targetZone = getZoneId(ctxt);
    if (targetZone != null) {
      ZoneOffset offset = targetZone.getRules().getOffset(instant);
      instant = instant.plusSeconds(offset.getTotalSeconds());
    }
    return instant;
  }

  private ZoneId getZoneId(DeserializationContext ctxt) {
    if (this.zoneId != null) {
      return this.zoneId;
    }
    if (ctxt != null) {
      java.util.TimeZone tz = ctxt.getTimeZone();
      if (tz != null) {
        ZoneId tzId = tz.toZoneId();
        String id = tzId.getId();
        if (!"UTC".equals(id) && !"GMT".equals(id) && !"Z".equals(id) && !"UTC0".equals(id)) {
          return tzId;
        }
      }
    }
    return null;
  }
}
