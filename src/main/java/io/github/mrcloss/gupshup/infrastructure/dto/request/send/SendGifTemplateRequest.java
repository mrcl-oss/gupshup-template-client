package io.github.mrcloss.gupshup.infrastructure.dto.request.send;

import io.github.mrcloss.gupshup.domain.message.GifPayload;
import java.util.List;

/** Specialized request for sending a GIF template. */
public class SendGifTemplateRequest extends SendMediaTemplateRequest {

  /**
   * Constructs a new SendGifTemplateRequest.
   *
   * @param source the WhatsApp Business number
   * @param destination the recipient's phone number
   * @param srcName the Gupshup app name
   * @param templateId the GIF template ID
   * @param bodyParams variables to fill in the template body
   * @param message the GIF message payload details
   */
  public SendGifTemplateRequest(
      String source,
      String destination,
      String srcName,
      String templateId,
      List<String> bodyParams,
      GifPayload message) {
    super(source, destination, srcName, templateId, bodyParams, message);
  }

  /**
   * Constructs a new SendGifTemplateRequest with button params.
   *
   * @param source the WhatsApp Business number
   * @param destination the recipient's phone number
   * @param srcName the Gupshup app name
   * @param templateId the GIF template ID
   * @param bodyParams variables to fill in the template body
   * @param buttonParams variables to fill in the template buttons
   * @param message the GIF message payload details
   */
  public SendGifTemplateRequest(
      String source,
      String destination,
      String srcName,
      String templateId,
      List<String> bodyParams,
      List<String> buttonParams,
      GifPayload message) {
    super(source, destination, srcName, templateId, bodyParams, buttonParams, message);
  }
}
