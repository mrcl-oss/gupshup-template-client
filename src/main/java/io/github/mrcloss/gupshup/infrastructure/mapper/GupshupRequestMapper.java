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
import io.github.mrcloss.gupshup.infrastructure.dto.request.AuthenticationTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.ButtonRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.CarouselCardRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.CarouselTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.CatalogButtonRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.CatalogTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.CopyCodeButtonRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.DocumentTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.GIFTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.ImageTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.LocationTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.MPMButtonRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.MediaTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.OTPButtonRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.PhoneNumberButtonRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.ProductTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.QuickReplyButtonRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.TemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.TextTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.UrlButtonRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.VideoTemplateRequest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GupshupRequestMapper {

  public static TemplateRequest map(Template template) {
    TemplateRequest request = createRequest(template);

    request.setAppId(template.getAppId());
    request.setCategory(template.getCategory());
    request.setContent(template.getBody());
    request.setElementName(template.getElementName());
    request.setLanguageCode(template.getLanguageCode());
    request.setParameterFormat(template.getParameterFormat());
    request.setTemplateType(template.getTemplateType());
    request.setFooter(template.getFooter());
    request.setMessageValidity(template.getMessageValidity());
    request.setLtoAttributes(template.getLtoAttributes());

    if (template.getTags() != null) {
      request.setVertical(String.join(",", template.getTags()));
    }

    request.setExample(formatExample(template.getBody(), template.getVariableExamples()));

    if (template.getButtons() != null) {
      request.setButtons(
          template.getButtons().stream()
              .map(GupshupRequestMapper::mapButton)
              .collect(Collectors.toList()));
    }

    if (template instanceof TextTemplate) {
      TextTemplate textTemplate = (TextTemplate) template;
      TextTemplateRequest textRequest = (TextTemplateRequest) request;
      textRequest.setHeader(textTemplate.getHeader());
      textRequest.setExampleHeader(
          formatExample(textTemplate.getHeader(), textTemplate.getVariableHeaderExamples()));
    }

    if (template instanceof CarouselTemplate) {
      CarouselTemplate carouselTemplate = (CarouselTemplate) template;
      CarouselTemplateRequest carouselRequest = (CarouselTemplateRequest) request;
      if (carouselTemplate.getCards() != null) {
        carouselRequest.setCards(
            carouselTemplate.getCards().stream()
                .map(GupshupRequestMapper::mapCard)
                .collect(Collectors.toList()));
      }
    }

    if (template instanceof MediaTemplate) {
      MediaTemplate mediaTemplate = (MediaTemplate) template;
      MediaTemplateRequest mediaRequest = (MediaTemplateRequest) request;
      mediaRequest.setMediaId(mediaTemplate.getMediaId());
      mediaRequest.setMediaUrl(mediaTemplate.getMediaUrl());
    }

    return request;
  }

  private static TemplateRequest createRequest(Template template) {
    if (template instanceof AuthenticationTemplate) {
      return new AuthenticationTemplateRequest();
    }
    if (template instanceof CarouselTemplate) {
      return new CarouselTemplateRequest();
    }
    if (template instanceof CatalogTemplate) {
      return new CatalogTemplateRequest();
    }
    if (template instanceof DocumentTemplate) {
      return new DocumentTemplateRequest();
    }
    if (template instanceof GIFTemplate) {
      return new GIFTemplateRequest();
    }
    if (template instanceof ImageTemplate) {
      return new ImageTemplateRequest();
    }
    if (template instanceof LocationTemplate) {
      return new LocationTemplateRequest();
    }
    if (template instanceof ProductTemplate) {
      return new ProductTemplateRequest();
    }
    if (template instanceof TextTemplate) {
      return new TextTemplateRequest();
    }
    if (template instanceof VideoTemplate) {
      return new VideoTemplateRequest();
    }
    return new TemplateRequest();
  }

  private static CarouselCardRequest mapCard(CarouselCard card) {
    CarouselCardRequest request = new CarouselCardRequest();
    request.setContent(card.getBody());
    request.setMediaId(card.getMediaId());
    request.setMediaUrl(card.getMediaUrl());
    request.setSampleText(formatExample(card.getBody(), card.getVariableExamples()));
    if (card.getButtons() != null) {
      request.setButtons(
          card.getButtons().stream()
              .map(GupshupRequestMapper::mapButton)
              .collect(Collectors.toList()));
    }
    return request;
  }

  private static ButtonRequest mapButton(Button button) {
    if (button instanceof UrlButton) {
      UrlButtonRequest request = new UrlButtonRequest();
      request.setType(button.getType());
      request.setText(button.getText());

      if (button instanceof PayNowButton) {
        PayNowButton payNow = (PayNowButton) button;
        request.setPaymentLinkPreview(payNow.isPaymentLinkPreview());
        mapUrlProperties(payNow.getUnderlyingUrlButton(), request);
      } else {
        mapUrlProperties((UrlButton) button, request);
      }
      return request;
    }

    if (button instanceof QuickReplyButton) {
      QuickReplyButtonRequest request = new QuickReplyButtonRequest();
      request.setType(button.getType());
      request.setText(button.getText());
      return request;
    }

    if (button instanceof PhoneNumberButton) {
      PhoneNumberButtonRequest request = new PhoneNumberButtonRequest();
      request.setType(button.getType());
      request.setText(button.getText());
      request.setPhoneNumber(((PhoneNumberButton) button).getPhoneNumber());
      return request;
    }

    if (button instanceof CopyCodeButton) {
      CopyCodeButtonRequest request = new CopyCodeButtonRequest();
      request.setType(button.getType());
      request.setText(button.getText());
      request.setExample(((CopyCodeButton) button).getExampleValue());
      return request;
    }

    if (button instanceof OTPButton) {
      OTPButtonRequest request = new OTPButtonRequest();
      request.setType(button.getType());
      request.setText(button.getText());
      request.setOtpType(((OTPButton) button).getOtpType().name());
      return request;
    }

    if (button instanceof MPMButton) {
      MPMButtonRequest request = new MPMButtonRequest();
      request.setType(button.getType());
      request.setText(button.getText());
      return request;
    }

    if (button instanceof CatalogButton) {
      CatalogButtonRequest request = new CatalogButtonRequest();
      request.setType(button.getType());
      request.setText(button.getText());
      return request;
    }

    ButtonRequest request = new ButtonRequest();
    request.setType(button.getType());
    request.setText(button.getText());
    return request;
  }

  private static void mapUrlProperties(UrlButton urlButton, UrlButtonRequest request) {
    if (urlButton instanceof StaticUrlButton) {
      request.setUrl(((StaticUrlButton) urlButton).getUrl());
    } else if (urlButton instanceof DynamicUrlButton) {
      DynamicUrlButton dynamic = (DynamicUrlButton) urlButton;
      request.setUrl(dynamic.getUrlTemplate());
      if (dynamic.getVariableExample() != null) {
        request.setExample(Collections.singletonList(dynamic.getVariableExample()));
      }
    }
  }

  private static String formatExample(String content, List<String> examples) {
    if (content == null || examples == null || examples.isEmpty()) {
      return content;
    }
    String result = content;
    for (int i = 0; i < examples.size(); i++) {
      String placeholder = "{{" + (i + 1) + "}}";
      String value = "[" + examples.get(i) + "]";
      result = result.replace(placeholder, value);
    }
    return result;
  }
}
