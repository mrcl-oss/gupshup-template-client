package io.github.mrcloss.gupshup.infrastructure.dto.request.send;

import io.github.mrcloss.gupshup.domain.message.LocationPayload;
import java.util.List;

/** Specialized request for sending a Location template. */
public class SendLocationTemplateRequest extends SendTemplateRequest {

  /**
   * Constructs a new SendLocationTemplateRequest.
   *
   * @param source the WhatsApp Business number
   * @param destination the recipient's phone number
   * @param srcName the Gupshup app name
   * @param templateId the location template ID
   * @param bodyParams variables to fill in the template body
   * @param latitude the location latitude
   * @param longitude the location longitude
   * @param name the location name
   * @param address the location address
   */
  public SendLocationTemplateRequest(
      String source,
      String destination,
      String srcName,
      String templateId,
      List<String> bodyParams,
      double latitude,
      double longitude,
      String name,
      String address) {
    super(
        source,
        destination,
        srcName,
        templateId,
        bodyParams,
        new LocationPayload(latitude, longitude, name, address));
  }

  /**
   * Constructs a new SendLocationTemplateRequest with button params.
   *
   * @param source the WhatsApp Business number
   * @param destination the recipient's phone number
   * @param srcName the Gupshup app name
   * @param templateId the location template ID
   * @param bodyParams variables to fill in the template body
   * @param buttonParams variables to fill in the template buttons
   * @param latitude the location latitude
   * @param longitude the location longitude
   * @param name the location name
   * @param address the location address
   */
  public SendLocationTemplateRequest(
      String source,
      String destination,
      String srcName,
      String templateId,
      List<String> bodyParams,
      List<String> buttonParams,
      double latitude,
      double longitude,
      String name,
      String address) {
    super(
        source,
        destination,
        srcName,
        templateId,
        null,
        bodyParams,
        buttonParams,
        new LocationPayload(latitude, longitude, name, address));
  }
}
