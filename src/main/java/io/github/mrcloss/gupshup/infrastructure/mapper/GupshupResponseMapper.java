package io.github.mrcloss.gupshup.infrastructure.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.mrcloss.gupshup.domain.button.Button;
import io.github.mrcloss.gupshup.domain.button.CatalogButton;
import io.github.mrcloss.gupshup.domain.button.CopyCodeButton;
import io.github.mrcloss.gupshup.domain.button.DynamicUrlButton;
import io.github.mrcloss.gupshup.domain.button.MPMButton;
import io.github.mrcloss.gupshup.domain.button.OTPButton;
import io.github.mrcloss.gupshup.domain.button.PayNowButton;
import io.github.mrcloss.gupshup.domain.button.PhoneNumberButton;
import io.github.mrcloss.gupshup.domain.button.QuickReplyButton;
import io.github.mrcloss.gupshup.domain.button.StaticUrlButton;
import io.github.mrcloss.gupshup.domain.button.UrlButton;
import io.github.mrcloss.gupshup.domain.enums.ButtonType;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateStatus;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import io.github.mrcloss.gupshup.domain.template.AuthenticationTemplate;
import io.github.mrcloss.gupshup.domain.template.CarouselCard;
import io.github.mrcloss.gupshup.domain.template.CarouselTemplate;
import io.github.mrcloss.gupshup.domain.template.CatalogTemplate;
import io.github.mrcloss.gupshup.domain.template.DocumentTemplate;
import io.github.mrcloss.gupshup.domain.template.GIFTemplate;
import io.github.mrcloss.gupshup.domain.template.ImageTemplate;
import io.github.mrcloss.gupshup.domain.template.LTOAttributes;
import io.github.mrcloss.gupshup.domain.template.LocationTemplate;
import io.github.mrcloss.gupshup.domain.template.ProductTemplate;
import io.github.mrcloss.gupshup.domain.template.Template;
import io.github.mrcloss.gupshup.domain.template.TextTemplate;
import io.github.mrcloss.gupshup.domain.template.VideoTemplate;
import io.github.mrcloss.gupshup.infrastructure.dto.response.GupshupTemplateDetails;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Mapper for converting infrastructure response DTO {@link GupshupTemplateDetails} into domain
 * {@link Template} subclasses.
 */
public class GupshupResponseMapper {

  private static final ObjectMapper DEFAULT_OBJECT_MAPPER =
      new ObjectMapper().registerModule(new JavaTimeModule());

  /**
   * Maps a {@link GupshupTemplateDetails} response into a domain {@link Template} object using a
   * default ObjectMapper.
   *
   * @param response the Gupshup template response DTO
   * @return the mapped domain template
   */
  public static Template map(GupshupTemplateDetails response) {
    return map(response, DEFAULT_OBJECT_MAPPER);
  }

  /**
   * Maps a {@link GupshupTemplateDetails} response into a domain {@link Template} object using a
   * custom ObjectMapper.
   *
   * @param response the Gupshup template response DTO
   * @param customObjectMapper the Jackson ObjectMapper to deserialize JSON fields
   * @return the mapped domain template
   */
  public static Template map(GupshupTemplateDetails response, ObjectMapper customObjectMapper) {
    if (response == null) {
      return null;
    }

    String body = null;
    String footer = null;
    String header = null;
    List<String> variableExamples = null;
    List<String> variableHeaderExamples = null;
    List<Button> buttons = null;
    List<CarouselCard> cards = null;
    Boolean addSecurityRecommendation = null;
    Integer codeExpirationMinutes = null;
    String mediaUrl = null;
    String mediaId = null;
    Boolean isLTO = null;
    Boolean hasExpiration = null;
    String limitedOfferText = null;

    // Try parsing containerMeta first if present
    if (response.getContainerMeta() != null) {
      String containerStr = response.getContainerMeta().trim();
      if (containerStr.startsWith("{")) {
        try {
          JsonNode containerNode = customObjectMapper.readTree(containerStr);
          if (containerNode.has("data")) {
            body = containerNode.get("data").asText();
          } else if (containerNode.has("body")) {
            body = containerNode.get("body").asText();
          } else if (containerNode.has("content")) {
            body = containerNode.get("content").asText();
          }

          if (containerNode.has("footer")) {
            footer = containerNode.get("footer").asText();
          }

          if (containerNode.has("header")) {
            header = containerNode.get("header").asText();
          }

          if (containerNode.has("variableExamples")) {
            variableExamples =
                customObjectMapper.convertValue(
                    containerNode.get("variableExamples"), new TypeReference<List<String>>() {});
          } else if (containerNode.has("sampleText") && body != null) {
            variableExamples =
                extractVariableExamples(body, containerNode.get("sampleText").asText());
          }

          if (containerNode.has("variableHeaderExamples")) {
            variableHeaderExamples =
                customObjectMapper.convertValue(
                    containerNode.get("variableHeaderExamples"),
                    new TypeReference<List<String>>() {});
          } else if (containerNode.has("sampleHeader") && header != null) {
            variableHeaderExamples =
                extractVariableExamples(header, containerNode.get("sampleHeader").asText());
          }

          if (containerNode.has("mediaUrl")) {
            mediaUrl = containerNode.get("mediaUrl").asText();
          }

          if (containerNode.has("mediaId")) {
            mediaId = containerNode.get("mediaId").asText();
          }

          if (containerNode.has("addSecurityRecommendation")) {
            addSecurityRecommendation = containerNode.get("addSecurityRecommendation").asBoolean();
          }

          if (containerNode.has("codeExpirationMinutes")) {
            codeExpirationMinutes = containerNode.get("codeExpirationMinutes").asInt();
          }

          if (containerNode.has("buttons")) {
            buttons = parseButtons(containerNode.get("buttons"), customObjectMapper);
          }

          if (containerNode.has("cards")) {
            cards = parseCards(containerNode.get("cards"), customObjectMapper);
          }

          if (containerNode.has("isLTO")) {
            isLTO = containerNode.get("isLTO").asBoolean();
          }
          if (containerNode.has("hasExpiration")) {
            hasExpiration = containerNode.get("hasExpiration").asBoolean();
          }
          if (containerNode.has("limitedOfferText")) {
            limitedOfferText = containerNode.get("limitedOfferText").asText();
          }
        } catch (Exception e) {
          // Ignore
        }
      }
    }

    // Try parsing data if present and starts with {
    if (response.getData() != null) {
      String dataStr = response.getData().trim();
      if (dataStr.startsWith("{")) {
        try {
          JsonNode dataNode = customObjectMapper.readTree(dataStr);
          if (body == null) {
            if (dataNode.has("body")) {
              body = dataNode.get("body").asText();
            } else if (dataNode.has("content")) {
              body = dataNode.get("content").asText();
            }
          }

          if (footer == null && dataNode.has("footer")) {
            footer = dataNode.get("footer").asText();
          }

          if (header == null && dataNode.has("header")) {
            header = dataNode.get("header").asText();
          }

          if (variableExamples == null) {
            if (dataNode.has("variableExamples")) {
              variableExamples =
                  customObjectMapper.convertValue(
                      dataNode.get("variableExamples"), new TypeReference<List<String>>() {});
            } else if (dataNode.has("sampleText") && body != null) {
              variableExamples = extractVariableExamples(body, dataNode.get("sampleText").asText());
            }
          }

          if (variableHeaderExamples == null) {
            if (dataNode.has("variableHeaderExamples")) {
              variableHeaderExamples =
                  customObjectMapper.convertValue(
                      dataNode.get("variableHeaderExamples"), new TypeReference<List<String>>() {});
            } else if (dataNode.has("sampleHeader") && header != null) {
              variableHeaderExamples =
                  extractVariableExamples(header, dataNode.get("sampleHeader").asText());
            }
          }

          if (mediaUrl == null && dataNode.has("mediaUrl")) {
            mediaUrl = dataNode.get("mediaUrl").asText();
          }

          if (mediaId == null && dataNode.has("mediaId")) {
            mediaId = dataNode.get("mediaId").asText();
          }

          if (addSecurityRecommendation == null && dataNode.has("addSecurityRecommendation")) {
            addSecurityRecommendation = dataNode.get("addSecurityRecommendation").asBoolean();
          }

          if (codeExpirationMinutes == null && dataNode.has("codeExpirationMinutes")) {
            codeExpirationMinutes = dataNode.get("codeExpirationMinutes").asInt();
          }

          if (buttons == null && dataNode.has("buttons")) {
            buttons = parseButtons(dataNode.get("buttons"), customObjectMapper);
          }

          if (cards == null && dataNode.has("cards")) {
            cards = parseCards(dataNode.get("cards"), customObjectMapper);
          }

          if (isLTO == null && dataNode.has("isLTO")) {
            isLTO = dataNode.get("isLTO").asBoolean();
          }
          if (hasExpiration == null && dataNode.has("hasExpiration")) {
            hasExpiration = dataNode.get("hasExpiration").asBoolean();
          }
          if (limitedOfferText == null && dataNode.has("limitedOfferText")) {
            limitedOfferText = dataNode.get("limitedOfferText").asText();
          }
        } catch (Exception e) {
          // Ignore
        }
      }
    }

    if (body == null && response.getData() != null) {
      body = response.getData();
    }

    // Try parsing meta if present and starts with {
    if (variableExamples == null && response.getMeta() != null) {
      String metaStr = response.getMeta().trim();
      if (metaStr.startsWith("{")) {
        try {
          JsonNode metaNode = customObjectMapper.readTree(metaStr);
          if (metaNode.has("example") && body != null) {
            variableExamples = extractVariableExamples(body, metaNode.get("example").asText());
          }
        } catch (Exception e) {
          // Ignore
        }
      }
    }

    TemplateType type = TemplateType.TEXT;
    if (response.getTemplateType() != null) {
      try {
        type = TemplateType.valueOf(response.getTemplateType().toUpperCase());
      } catch (Exception e) {
        // Fall back to TEXT
      }
    }

    TemplateCategory category = TemplateCategory.UTILITY;
    if (response.getCategory() != null) {
      try {
        category = TemplateCategory.valueOf(response.getCategory().toUpperCase());
      } catch (Exception e) {
        // Fall back to UTILITY
      }
    }

    TemplateParameterFormat parameterFormat = TemplateParameterFormat.POSITIONAL;
    if (response.getParameterFormat() != null) {
      try {
        parameterFormat =
            TemplateParameterFormat.valueOf(response.getParameterFormat().toUpperCase());
      } catch (Exception e) {
        // Fall back to POSITIONAL
      }
    }

    TemplateStatus status = null;
    if (response.getStatus() != null) {
      status = TemplateStatus.fromString(response.getStatus());
    }

    String elementName = response.getElementName();
    LanguageCode languageCode =
        response.getLanguageCode() != null ? LanguageCode.of(response.getLanguageCode()) : null;
    String appId = response.getAppId();
    List<String> tags = null;
    if (response.getVertical() != null && !response.getVertical().isEmpty()) {
      tags =
          Arrays.stream(response.getVertical().split(","))
              .map(String::trim)
              .collect(Collectors.toList());
    }

    Template template;

    if (category == TemplateCategory.AUTHENTICATION) {
      AuthenticationTemplate authTemplate =
          new AuthenticationTemplate(
              elementName, languageCode, body, variableExamples, appId, tags, parameterFormat);
      if (addSecurityRecommendation != null) {
        authTemplate.setAddSecurityRecommendation(addSecurityRecommendation);
      }
      if (codeExpirationMinutes != null) {
        authTemplate.setCodeExpirationMinutes(codeExpirationMinutes);
      } else {
        if (buttons != null) {
          for (Button btn : buttons) {
            if (btn instanceof CopyCodeButton) {
              String btnText = btn.getText();
              if (btnText != null && btnText.contains("expires in")) {
                try {
                  String digits = btnText.replaceAll("\\D+", "");
                  if (!digits.isEmpty()) {
                    authTemplate.setCodeExpirationMinutes(Integer.parseInt(digits));
                  }
                } catch (Exception e) {
                  // Ignore
                }
              }
            }
          }
        }
      }
      template = authTemplate;
    } else {
      switch (type) {
        case TEXT:
          TextTemplate textTemplate =
              new TextTemplate(
                  elementName,
                  languageCode,
                  body,
                  variableExamples,
                  category,
                  appId,
                  tags,
                  parameterFormat);
          if (header != null) {
            textTemplate.setHeader(header);
          }
          if (variableHeaderExamples != null) {
            textTemplate.setVariableHeaderExamples(variableHeaderExamples);
          }
          template = textTemplate;
          break;

        case IMAGE:
          ImageTemplate imgTemplate =
              new ImageTemplate(
                  elementName,
                  languageCode,
                  body,
                  variableExamples,
                  category,
                  appId,
                  tags,
                  parameterFormat);
          imgTemplate.setMediaId(mediaId);
          imgTemplate.setMediaUrl(mediaUrl);
          template = imgTemplate;
          break;

        case VIDEO:
          VideoTemplate vidTemplate =
              new VideoTemplate(
                  elementName,
                  languageCode,
                  body,
                  variableExamples,
                  category,
                  appId,
                  tags,
                  parameterFormat);
          vidTemplate.setMediaId(mediaId);
          vidTemplate.setMediaUrl(mediaUrl);
          template = vidTemplate;
          break;

        case DOCUMENT:
          DocumentTemplate docTemplate =
              new DocumentTemplate(
                  elementName,
                  languageCode,
                  body,
                  variableExamples,
                  category,
                  appId,
                  tags,
                  parameterFormat);
          docTemplate.setMediaId(mediaId);
          docTemplate.setMediaUrl(mediaUrl);
          template = docTemplate;
          break;

        case GIF:
          GIFTemplate gifTemplate =
              new GIFTemplate(
                  elementName,
                  languageCode,
                  body,
                  variableExamples,
                  category,
                  appId,
                  tags,
                  parameterFormat);
          gifTemplate.setMediaId(mediaId);
          gifTemplate.setMediaUrl(mediaUrl);
          template = gifTemplate;
          break;

        case LOCATION:
          template =
              new LocationTemplate(
                  elementName,
                  languageCode,
                  body,
                  variableExamples,
                  category,
                  appId,
                  tags,
                  parameterFormat);
          break;

        case CATALOG:
          template =
              new CatalogTemplate(
                  elementName,
                  languageCode,
                  body,
                  variableExamples,
                  category,
                  appId,
                  tags,
                  parameterFormat);
          break;

        case PRODUCT:
          ProductTemplate prodTemplate =
              new ProductTemplate(
                  elementName,
                  languageCode,
                  body,
                  variableExamples,
                  category,
                  appId,
                  tags,
                  parameterFormat);
          if (header != null) {
            prodTemplate.setHeader(header);
          }
          if (variableHeaderExamples != null) {
            prodTemplate.setVariableHeaderExamples(variableHeaderExamples);
          }
          template = prodTemplate;
          break;

        case CAROUSEL:
          CarouselTemplate carouselTemplate =
              new CarouselTemplate(
                  elementName,
                  languageCode,
                  body,
                  variableExamples,
                  category,
                  appId,
                  tags,
                  parameterFormat);
          if (cards != null) {
            carouselTemplate.setCards(new ArrayList<>(cards));
          }
          template = carouselTemplate;
          break;

        default:
          template =
              new Template(
                  elementName,
                  languageCode,
                  body,
                  variableExamples,
                  category,
                  appId,
                  tags,
                  type,
                  parameterFormat);
          break;
      }
    }

    template.setStatus(status);
    template.setReason(response.getReason());

    if (response.getCreatedOn() != null) {
      template.setCreatedOn(response.getCreatedOn());
    }

    if (response.getModifiedOn() != null) {
      template.setModifiedOn(response.getModifiedOn());
    }

    if (footer != null) {
      template.setFooter(footer);
    }

    if (buttons != null && !buttons.isEmpty()) {
      if (!(template instanceof CatalogTemplate)
          && !(template instanceof ProductTemplate)
          && !(template instanceof AuthenticationTemplate)
          && !(template instanceof CarouselTemplate)) {
        template.setButtons(buttons);
      }
    }

    // Map LTO Attributes
    if (isLTO != null && isLTO) {
      template.setLtoAttributes(
          new LTOAttributes(hasExpiration != null && hasExpiration, limitedOfferText));
    }

    return template;
  }

  static List<Button> parseButtons(JsonNode buttonsNode, ObjectMapper objectMapper) {
    List<Button> buttons = new ArrayList<>();
    if (buttonsNode == null || !buttonsNode.isArray()) {
      return buttons;
    }
    for (JsonNode btnNode : buttonsNode) {
      Button button = parseButton(btnNode, objectMapper);
      if (button != null) {
        buttons.add(button);
      }
    }
    return buttons;
  }

  static Button parseButton(JsonNode node, ObjectMapper objectMapper) {
    if (node == null) {
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
          OTPButton.OTPButtonType otpType =
              OTPButton.OTPButtonType.valueOf(otpTypeStr.toUpperCase());
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
              Button parsedUnderlying = parseButton(node.get("underlyingUrlButton"), objectMapper);
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
      return new QuickReplyButton(text);
    }
  }

  private static List<CarouselCard> parseCards(JsonNode cardsNode, ObjectMapper objectMapper) {
    List<CarouselCard> cards = new ArrayList<>();
    if (cardsNode == null || !cardsNode.isArray()) {
      return cards;
    }
    for (JsonNode cardNode : cardsNode) {
      CarouselCard card = parseCard(cardNode, objectMapper);
      if (card != null) {
        cards.add(card);
      }
    }
    return cards;
  }

  private static CarouselCard parseCard(JsonNode node, ObjectMapper objectMapper) {
    if (node == null) {
      return null;
    }
    String body = node.has("body") ? node.get("body").asText() : "";
    if (body.isEmpty() && node.has("content")) {
      body = node.get("content").asText();
    }
    String headerTypeStr = node.has("headerType") ? node.get("headerType").asText() : "IMAGE";
    CarouselCard.CarouselCardHeaderType headerType = CarouselCard.CarouselCardHeaderType.IMAGE;
    try {
      headerType = CarouselCard.CarouselCardHeaderType.valueOf(headerTypeStr.toUpperCase());
    } catch (Exception e) {
      // Default to IMAGE
    }

    List<String> variableExamples = null;
    if (node.has("variableExamples")) {
      variableExamples =
          objectMapper.convertValue(
              node.get("variableExamples"), new TypeReference<List<String>>() {});
    }

    CarouselCard card = new CarouselCard(body, variableExamples, headerType);

    if (node.has("mediaUrl")) {
      try {
        card.setMediaUrl(node.get("mediaUrl").asText());
      } catch (Exception e) {
        // Ignore URL format errors in mapped resources
      }
    }
    if (node.has("mediaId")) {
      card.setMediaId(node.get("mediaId").asText());
    }
    if (node.has("buttons")) {
      List<Button> buttons = parseButtons(node.get("buttons"), objectMapper);
      if (!buttons.isEmpty()) {
        try {
          card.setButtons(buttons);
        } catch (Exception e) {
          // Ignore
        }
      }
    }
    return card;
  }

  private static List<String> extractVariableExamples(String content, String example) {
    if (content == null || example == null || example.isEmpty()) {
      return null;
    }
    List<String> examples = new ArrayList<>();
    Pattern pattern = Pattern.compile("\\[(.*?)\\]");
    Matcher matcher = pattern.matcher(example);
    while (matcher.find()) {
      examples.add(matcher.group(1));
    }
    return examples.isEmpty() ? null : examples;
  }
}
