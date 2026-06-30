package io.github.mrcloss.gupshup.infrastructure.dto.request.send;

import io.github.mrcloss.gupshup.domain.message.MediaPayload;
import java.util.List;

/** Specialized request for sending a Media (Image, Video, Document, GIF) template. */
public class SendMediaTemplateRequest extends SendTemplateRequest {

  /**
   * Constructs a new SendMediaTemplateRequest.
   *
   * @param source the WhatsApp Business number
   * @param destination the recipient's phone number
   * @param srcName the Gupshup app name
   * @param templateId the media template ID
   * @param bodyParams variables to fill in the template body
   * @param message the media payload details
   */
  public SendMediaTemplateRequest(
      String source,
      String destination,
      String srcName,
      String templateId,
      List<String> bodyParams,
      MediaPayload message) {
    super(source, destination, srcName, templateId, bodyParams, message);
  }

  /**
   * Constructs a new SendMediaTemplateRequest with button params.
   *
   * @param source the WhatsApp Business number
   * @param destination the recipient's phone number
   * @param srcName the Gupshup app name
   * @param templateId the media template ID
   * @param bodyParams variables to fill in the template body
   * @param buttonParams variables to fill in the template buttons
   * @param message the media payload details
   */
  public SendMediaTemplateRequest(
      String source,
      String destination,
      String srcName,
      String templateId,
      List<String> bodyParams,
      List<String> buttonParams,
      MediaPayload message) {
    super(source, destination, srcName, templateId, null, bodyParams, buttonParams, message);
  }
}
