package io.github.mrcloss.gupshup.infrastructure.dto.request.send;

import java.util.List;

/** Specialized request for sending an Authentication template. */
public class SendAuthenticationTemplateRequest extends SendTemplateRequest {

  /**
   * Constructs a new SendAuthenticationTemplateRequest.
   *
   * @param source the WhatsApp Business number
   * @param destination the recipient's phone number
   * @param srcName the Gupshup app name
   * @param templateId the authentication template ID
   * @param code the authentication code to send (used for both the body and the COPY_CODE button)
   */
  public SendAuthenticationTemplateRequest(
      String source, String destination, String srcName, String templateId, String code) {
    super(source, destination, srcName, templateId, List.of(code, code));
  }
}
