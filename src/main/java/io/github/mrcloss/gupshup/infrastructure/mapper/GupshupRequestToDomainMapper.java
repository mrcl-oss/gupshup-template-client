package io.github.mrcloss.gupshup.infrastructure.mapper;

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
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import io.github.mrcloss.gupshup.domain.template.AuthenticationTemplate;
import io.github.mrcloss.gupshup.domain.template.CarouselCard;
import io.github.mrcloss.gupshup.domain.template.CarouselTemplate;
import io.github.mrcloss.gupshup.domain.template.CatalogTemplate;
import io.github.mrcloss.gupshup.domain.template.DocumentTemplate;
import io.github.mrcloss.gupshup.domain.template.GIFTemplate;
import io.github.mrcloss.gupshup.domain.template.ImageTemplate;
import io.github.mrcloss.gupshup.domain.template.LocationTemplate;
import io.github.mrcloss.gupshup.domain.template.MediaTemplate;
import io.github.mrcloss.gupshup.domain.template.ProductTemplate;
import io.github.mrcloss.gupshup.domain.template.Template;
import io.github.mrcloss.gupshup.domain.template.TextTemplate;
import io.github.mrcloss.gupshup.domain.template.VideoTemplate;
import io.github.mrcloss.gupshup.infrastructure.dto.request.ButtonRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.CarouselCardRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.CarouselTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.CopyCodeButtonRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.MediaTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.OTPButtonRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.PhoneNumberButtonRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.ProductTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.TemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.TextTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.UrlButtonRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Mapper for converting infrastructure request DTO {@link TemplateRequest} into domain {@link
 * Template} subclasses.
 */
public class GupshupRequestToDomainMapper {

  public static Template map(TemplateRequest request) {
    if (request == null) {
      return null;
    }

    String body = request.getContent();
    List<String> variableExamples = extractVariableExamples(body, request.getExample());
    String elementName = request.getElementName();
    LanguageCode languageCode = request.getLanguageCode();
    String appId = request.getAppId();
    TemplateCategory category =
        request.getCategory() != null ? request.getCategory() : TemplateCategory.UTILITY;
    TemplateParameterFormat parameterFormat =
        request.getParameterFormat() != null
            ? request.getParameterFormat()
            : TemplateParameterFormat.POSITIONAL;
    TemplateType type =
        request.getTemplateType() != null ? request.getTemplateType() : TemplateType.TEXT;

    List<String> tags = null;
    if (request.getVertical() != null && !request.getVertical().isEmpty()) {
      tags =
          Arrays.stream(request.getVertical().split(","))
              .map(String::trim)
              .collect(Collectors.toList());
    }

    Template template;

    if (category == TemplateCategory.AUTHENTICATION) {
      AuthenticationTemplate authTemplate =
          new AuthenticationTemplate(
              elementName, languageCode, body, variableExamples, appId, tags, parameterFormat);

      if (body != null && body.endsWith(" For your security, do not share this code.")) {
        authTemplate.setAddSecurityRecommendation(true);
      }

      if (request.getButtons() != null) {
        for (ButtonRequest btnReq : request.getButtons()) {
          if (btnReq instanceof CopyCodeButtonRequest) {
            String btnText = btnReq.getText();
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
          if (request instanceof TextTemplateRequest) {
            TextTemplateRequest textRequest = (TextTemplateRequest) request;
            textTemplate.setHeader(textRequest.getHeader());
            if (textRequest.getHeader() != null && textRequest.getExampleHeader() != null) {
              textTemplate.setVariableHeaderExamples(
                  extractVariableExamples(textRequest.getHeader(), textRequest.getExampleHeader()));
            }
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
          mapMediaProperties(request, imgTemplate);
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
          mapMediaProperties(request, vidTemplate);
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
          mapMediaProperties(request, docTemplate);
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
          mapMediaProperties(request, gifTemplate);
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
          if (request instanceof ProductTemplateRequest) {
            ProductTemplateRequest prodRequest = (ProductTemplateRequest) request;
            prodTemplate.setHeader(prodRequest.getHeader());
            if (prodRequest.getHeader() != null && prodRequest.getExampleHeader() != null) {
              prodTemplate.setVariableHeaderExamples(
                  extractVariableExamples(prodRequest.getHeader(), prodRequest.getExampleHeader()));
            }
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
          if (request instanceof CarouselTemplateRequest) {
            CarouselTemplateRequest carouselRequest = (CarouselTemplateRequest) request;
            if (carouselRequest.getCards() != null) {
              ArrayList<CarouselCard> cards =
                  carouselRequest.getCards().stream()
                      .map(GupshupRequestToDomainMapper::mapCard)
                      .collect(Collectors.toCollection(ArrayList::new));
              carouselTemplate.setCards(cards);
            }
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

    template.setFooter(request.getFooter());
    template.setMessageValidity(request.getMessageValidity());
    template.setLtoAttributes(request.getLtoAttributes());
    template.setReason(request.getReason());

    if (request.getButtons() != null && !request.getButtons().isEmpty()) {
      if (!(template instanceof CatalogTemplate)
          && !(template instanceof ProductTemplate)
          && !(template instanceof AuthenticationTemplate)
          && !(template instanceof CarouselTemplate)) {
        template.setButtons(
            request.getButtons().stream()
                .map(GupshupRequestToDomainMapper::mapButton)
                .collect(Collectors.toList()));
      }
    }

    return template;
  }

  private static void mapMediaProperties(TemplateRequest request, MediaTemplate mediaTemplate) {
    if (request instanceof MediaTemplateRequest) {
      MediaTemplateRequest mediaRequest = (MediaTemplateRequest) request;
      mediaTemplate.setMediaId(mediaRequest.getMediaId());
      mediaTemplate.setMediaUrl(mediaRequest.getMediaUrl());
    }
  }

  private static CarouselCard mapCard(CarouselCardRequest cardRequest) {
    if (cardRequest == null) {
      return null;
    }
    String body = cardRequest.getBody();
    List<String> variableExamples = extractVariableExamples(body, cardRequest.getSampleText());

    CarouselCard.CarouselCardHeaderType headerType = CarouselCard.CarouselCardHeaderType.IMAGE;
    if (cardRequest.getHeaderType() != null) {
      try {
        headerType =
            CarouselCard.CarouselCardHeaderType.valueOf(cardRequest.getHeaderType().toUpperCase());
      } catch (Exception e) {
        if (cardRequest.getMediaUrl() != null) {
          String lower = cardRequest.getMediaUrl().toLowerCase();
          if (lower.endsWith(".mp4") || lower.endsWith(".3gp") || lower.endsWith(".m4v")) {
            headerType = CarouselCard.CarouselCardHeaderType.VIDEO;
          }
        }
      }
    } else if (cardRequest.getMediaUrl() != null) {
      String lower = cardRequest.getMediaUrl().toLowerCase();
      if (lower.endsWith(".mp4") || lower.endsWith(".3gp") || lower.endsWith(".m4v")) {
        headerType = CarouselCard.CarouselCardHeaderType.VIDEO;
      }
    }

    CarouselCard card = new CarouselCard(body, variableExamples, headerType);
    card.setMediaId(cardRequest.getMediaId());
    try {
      card.setMediaUrl(cardRequest.getMediaUrl());
    } catch (Exception e) {
      // Ignore URL parsing exceptions
    }
    if (cardRequest.getButtons() != null) {
      card.setButtons(
          cardRequest.getButtons().stream()
              .map(GupshupRequestToDomainMapper::mapButton)
              .collect(Collectors.toList()));
    }
    return card;
  }

  public static Button mapButton(ButtonRequest buttonRequest) {
    if (buttonRequest == null) {
      return null;
    }
    ButtonType type =
        buttonRequest.getType() != null ? buttonRequest.getType() : ButtonType.QUICK_REPLY;
    String text = buttonRequest.getText();

    switch (type) {
      case QUICK_REPLY:
        return new QuickReplyButton(text);

      case PHONE_NUMBER:
        String phoneNum = "";
        if (buttonRequest instanceof PhoneNumberButtonRequest) {
          phoneNum = ((PhoneNumberButtonRequest) buttonRequest).getPhoneNumber();
        }
        return new PhoneNumberButton(text, phoneNum);

      case COPY_CODE:
        String copyExample = "123456";
        if (buttonRequest instanceof CopyCodeButtonRequest) {
          copyExample = ((CopyCodeButtonRequest) buttonRequest).getExample();
        }
        return new CopyCodeButton(text, copyExample);

      case OTP:
        OTPButton.OTPButtonType otpType = OTPButton.OTPButtonType.COPY_CODE;
        if (buttonRequest instanceof OTPButtonRequest) {
          String requestOtpType = ((OTPButtonRequest) buttonRequest).getOtpType();
          if (requestOtpType != null) {
            try {
              otpType = OTPButton.OTPButtonType.valueOf(requestOtpType.toUpperCase());
            } catch (Exception e) {
              // Default
            }
          }
        }
        return new OTPButton(text, otpType);

      case CATALOG:
        return new CatalogButton(text);

      case MPM:
        return new MPMButton(text);

      case URL:
        if (buttonRequest instanceof UrlButtonRequest) {
          UrlButtonRequest urlRequest = (UrlButtonRequest) buttonRequest;
          boolean isPayNow = urlRequest.getPaymentLinkPreview() != null;

          if (isPayNow) {
            boolean preview =
                urlRequest.getPaymentLinkPreview() != null && urlRequest.getPaymentLinkPreview();
            String url = urlRequest.getUrl() != null ? urlRequest.getUrl() : "https://example.com";
            UrlButton underlying = new StaticUrlButton(text, url);
            return new PayNowButton(text, preview, underlying);
          }

          String url = urlRequest.getUrl() != null ? urlRequest.getUrl() : "";
          if (url.contains("{{1}}")) {
            String example =
                (urlRequest.getExample() != null && !urlRequest.getExample().isEmpty())
                    ? urlRequest.getExample().get(0)
                    : "example";
            return new DynamicUrlButton(text, url, example);
          } else {
            return new StaticUrlButton(text, url.isEmpty() ? "https://example.com" : url);
          }
        }
        return new StaticUrlButton(text, "https://example.com");

      default:
        return new QuickReplyButton(text);
    }
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
    return examples;
  }
}
