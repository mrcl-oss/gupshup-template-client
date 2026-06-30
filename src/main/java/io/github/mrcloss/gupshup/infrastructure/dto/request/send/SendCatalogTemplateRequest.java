package io.github.mrcloss.gupshup.infrastructure.dto.request.send;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Specialized request for sending a Catalog template. */
public class SendCatalogTemplateRequest extends SendTemplateRequest {

  /**
   * Constructs a new SendCatalogTemplateRequest.
   *
   * @param source the WhatsApp Business number
   * @param destination the recipient's phone number
   * @param srcName the Gupshup app name
   * @param templateId the catalog template ID
   * @param bodyParams variables to fill in the template body
   * @param thumbnailProductId the product ID to be used as catalog thumbnail header
   */
  public SendCatalogTemplateRequest(
      String source,
      String destination,
      String srcName,
      String templateId,
      List<String> bodyParams,
      String thumbnailProductId) {
    super(
        source,
        destination,
        srcName,
        templateId,
        combineCatalogParams(bodyParams, thumbnailProductId));
  }

  private static List<String> combineCatalogParams(
      List<String> bodyParams, String thumbnailProductId) {
    List<String> allParams =
        new ArrayList<>(bodyParams != null ? bodyParams : Collections.emptyList());
    if (thumbnailProductId != null) {
      allParams.add(thumbnailProductId);
    }
    return allParams;
  }
}
