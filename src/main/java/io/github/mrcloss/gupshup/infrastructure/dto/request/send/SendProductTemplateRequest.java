package io.github.mrcloss.gupshup.infrastructure.dto.request.send;

import io.github.mrcloss.gupshup.domain.message.Mpm;
import java.util.List;

/** Specialized request for sending a Product (MPM) template. */
public class SendProductTemplateRequest extends SendTemplateRequest {

  /**
   * Constructs a new SendProductTemplateRequest.
   *
   * @param source the WhatsApp Business number
   * @param destination the recipient's phone number
   * @param srcName the Gupshup app name
   * @param templateId the product template ID
   * @param bodyParams variables to fill in the template body
   * @param mpm the multi-product message details
   */
  public SendProductTemplateRequest(
      String source,
      String destination,
      String srcName,
      String templateId,
      List<String> bodyParams,
      Mpm mpm) {
    super(source, destination, srcName, templateId, bodyParams, mpm);
  }
}
