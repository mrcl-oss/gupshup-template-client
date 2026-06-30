package io.github.mrcloss.gupshup.infrastructure.dto.request.send;

import java.util.List;

/** Specialized request for sending a Text template (including Authentication templates). */
public class SendTextTemplateRequest extends SendTemplateRequest {

  /**
   * Constructs a new SendTextTemplateRequest.
   *
   * @param source the WhatsApp Business number
   * @param destination the recipient's phone number
   * @param srcName the Gupshup app name
   * @param templateId the text template ID
   * @param bodyParams variables to fill in the template body
   */
  public SendTextTemplateRequest(
      String source,
      String destination,
      String srcName,
      String templateId,
      List<String> bodyParams) {
    super(source, destination, srcName, templateId, bodyParams);
  }

  /**
   * Constructs a new SendTextTemplateRequest with header and button params.
   *
   * @param source the WhatsApp Business number
   * @param destination the recipient's phone number
   * @param srcName the Gupshup app name
   * @param templateId the text template ID
   * @param headerParams variables to fill in the template header
   * @param bodyParams variables to fill in the template body
   * @param buttonParams variables to fill in the template buttons
   */
  public SendTextTemplateRequest(
      String source,
      String destination,
      String srcName,
      String templateId,
      List<String> headerParams,
      List<String> bodyParams,
      List<String> buttonParams) {
    super(source, destination, srcName, templateId, headerParams, bodyParams, buttonParams);
  }
}
