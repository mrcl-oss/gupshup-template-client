package io.github.mrcloss.gupshup.domain.button;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mrcloss.gupshup.domain.enums.ButtonType;
import java.io.IOException;

/** Custom deserializer for polymorphic deserialization of Button objects based on button type. */
public class ButtonDeserializer extends JsonDeserializer<Button> {

  @Override
  public Button deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    ObjectMapper mapper = (ObjectMapper) jp.getCodec();
    JsonNode node = mapper.readTree(jp);
    if (node == null || node.isNull()) {
      return null;
    }

    String typeStr = node.has("type") ? node.get("type").asText() : "";
    String text = node.has("text") ? node.get("text").asText() : "";

    if (typeStr.isEmpty()) {
      return null;
    }

    try {
      ButtonType type = ButtonType.valueOf(typeStr.toUpperCase());
      switch (type) {
        case QUICK_REPLY:
          return new QuickReplyButton(text);

        case PHONE_NUMBER:
          String phoneNumber = node.has("phoneNumber") ? node.get("phoneNumber").asText() : "";
          if (phoneNumber.isEmpty() && node.has("phone_number")) {
            phoneNumber = node.get("phone_number").asText();
          }
          return new PhoneNumberButton(text, phoneNumber);

        case COPY_CODE:
          String codeExample = node.has("exampleValue") ? node.get("exampleValue").asText() : "";
          if (codeExample.isEmpty() && node.has("example")) {
            if (node.get("example").isArray() && node.get("example").size() > 0) {
              codeExample = node.get("example").get(0).asText();
            } else {
              codeExample = node.get("example").asText();
            }
          }
          if (codeExample.isEmpty()) {
            codeExample = "123456";
          }
          return new CopyCodeButton(text, codeExample);

        case OTP:
          String otpTypeStr = node.has("otpType") ? node.get("otpType").asText() : "COPY_CODE";
          OTPButton.OTPButtonType otpType;
          try {
            otpType = OTPButton.OTPButtonType.valueOf(otpTypeStr.toUpperCase());
          } catch (Exception e) {
            otpType = OTPButton.OTPButtonType.COPY_CODE;
          }
          return new OTPButton(text, otpType);

        case CATALOG:
          return new CatalogButton(text);

        case MPM:
          return new MPMButton(text);

        case URL:
          boolean isPayNow = node.has("paymentLinkPreview") || node.has("underlyingUrlButton");
          if (isPayNow) {
            boolean preview =
                node.has("paymentLinkPreview") && node.get("paymentLinkPreview").asBoolean();
            UrlButton underlying = null;
            if (node.has("underlyingUrlButton")) {
              JsonNode underlyingNode = node.get("underlyingUrlButton");
              Button parsedUnderlying = mapper.treeToValue(underlyingNode, Button.class);
              if (parsedUnderlying instanceof UrlButton) {
                underlying = (UrlButton) parsedUnderlying;
              }
            }
            if (underlying == null) {
              String url = node.has("url") ? node.get("url").asText() : "https://example.com";
              underlying = new StaticUrlButton(text, url);
            }
            return new PayNowButton(text, preview, underlying);
          }

          boolean isDynamic = node.has("urlTemplate") || (node.has("example") && node.has("url"));
          String url = node.has("url") ? node.get("url").asText() : "";
          if (url.contains("{{1}}") || isDynamic) {
            String urlTemplate = node.has("urlTemplate") ? node.get("urlTemplate").asText() : url;
            String varExample = "example";
            if (node.has("variableExample")) {
              varExample = node.get("variableExample").asText();
            } else if (node.has("example")) {
              if (node.get("example").isArray() && node.get("example").size() > 0) {
                varExample = node.get("example").get(0).asText();
              } else {
                varExample = node.get("example").asText();
              }
            }
            return new DynamicUrlButton(text, urlTemplate, varExample);
          } else {
            return new StaticUrlButton(text, url.isEmpty() ? "https://example.com" : url);
          }

        default:
          return null;
      }
    } catch (Exception e) {
      return null;
    }
  }
}
