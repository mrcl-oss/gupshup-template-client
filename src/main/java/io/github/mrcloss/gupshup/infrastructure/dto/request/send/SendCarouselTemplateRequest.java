package io.github.mrcloss.gupshup.infrastructure.dto.request.send;

import io.github.mrcloss.gupshup.domain.message.CarouselPayload;
import java.util.List;

/** Specialized request for sending a Carousel template. */
public class SendCarouselTemplateRequest extends SendTemplateRequest {

  /**
   * Constructs a new SendCarouselTemplateRequest.
   *
   * @param source the WhatsApp Business number
   * @param destination the recipient's phone number
   * @param srcName the Gupshup app name
   * @param templateId the carousel template ID
   * @param bodyParams variables to fill in the template body
   * @param message the carousel payload details
   */
  public SendCarouselTemplateRequest(
      String source,
      String destination,
      String srcName,
      String templateId,
      List<String> bodyParams,
      CarouselPayload message) {
    super(source, destination, srcName, templateId, bodyParams, message);
  }
}
