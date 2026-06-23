package io.github.mrcloss.gupshup.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.mrcloss.gupshup.domain.template.CarouselCard;
import io.github.mrcloss.gupshup.domain.template.CarouselTemplate;
import io.github.mrcloss.gupshup.domain.template.MediaTemplate;
import io.github.mrcloss.gupshup.domain.template.Template;
import io.github.mrcloss.gupshup.infrastructure.dto.request.QueryParams;
import io.github.mrcloss.gupshup.infrastructure.dto.request.SendTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.request.TemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.response.CreateTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.DeleteTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.GetTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.GetTemplatesResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.OptInResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.SendTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.UploadMediaResponse;
import io.github.mrcloss.gupshup.infrastructure.http.GupshupHttpService;
import io.github.mrcloss.gupshup.infrastructure.http.JdkGupshupHttpService;
import io.github.mrcloss.gupshup.infrastructure.mapper.GupshupRequestMapper;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/** Default implementation of GupshupClient. */
public class DefaultGupshupClient implements GupshupClient {

  private final String appId;
  private final GupshupHttpService httpService;
  private final ObjectMapper objectMapper;
  private final String templateUrl;
  private final String sendTemplateUrl;

  private DefaultGupshupClient(Builder builder) {
    this.appId = builder.appId;
    this.objectMapper =
        builder.objectMapper != null ? builder.objectMapper : createDefaultObjectMapper();
    HttpClient httpClient =
        builder.httpClient != null ? builder.httpClient : HttpClient.newHttpClient();
    templateUrl = String.format("https://api.gupshup.io/wa/app/%s/template", builder.appId);
    sendTemplateUrl = templateUrl + "/v1/msg";

    this.httpService =
        builder.httpService != null
            ? builder.httpService
            : new JdkGupshupHttpService(builder.apiKey, httpClient, objectMapper);
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public GetTemplatesResponse getTemplates(QueryParams queryParams) {
    Map<String, Object> queryParamsMap = convertToMap(queryParams);
    return httpService.getWithParams(templateUrl, queryParamsMap, GetTemplatesResponse.class);
  }

  @Override
  public GetTemplatesResponse getTemplates() {
    return httpService.getWithParams(templateUrl, null, GetTemplatesResponse.class);
  }

  @Override
  public GetTemplateResponse getTemplate(String templateId) {
    return httpService.getWithParams(
        templateUrl + "/" + templateId, null, GetTemplateResponse.class);
  }

  @Override
  public CreateTemplateResponse createTemplate(Template template) {
    if (template instanceof MediaTemplate) {
      MediaTemplate mediaTemplate = (MediaTemplate) template;
      if (mediaTemplate.getMediaFile() != null
          && (mediaTemplate.getMediaId() == null
              || mediaTemplate.getMediaId().isEmpty()
              || mediaTemplate.getMediaUrl() == null
              || mediaTemplate.getMediaUrl().isEmpty())) {
        String uploadUrl = String.format("https://api.gupshup.io/wa/%s/wa/media/v2", appId);
        UploadMediaResponse uploadResponse =
            httpService.uploadMedia(
                uploadUrl, mediaTemplate.getMediaFile(), UploadMediaResponse.class);
        if (uploadResponse != null
            && uploadResponse.isSuccess()
            && uploadResponse.getMedia() != null) {
          mediaTemplate.setMediaId(uploadResponse.getMedia().getId());
          mediaTemplate.setMediaUrl(uploadResponse.getMedia().getUrl());
        } else {
          String errMsg = uploadResponse != null ? uploadResponse.getError() : "Unknown error";
          throw new io.github.mrcloss.gupshup.exception.GupshupApiException(
              "Failed to upload template media file: " + errMsg, 0, null);
        }
      }
    }

    if (template instanceof CarouselTemplate) {
      CarouselTemplate carouselTemplate = (CarouselTemplate) template;
      if (carouselTemplate.getCards() != null) {
        for (CarouselCard card : carouselTemplate.getCards()) {
          if (card.getMediaFile() != null
              && (card.getMediaId() == null
                  || card.getMediaId().isEmpty()
                  || card.getMediaUrl() == null
                  || card.getMediaUrl().isEmpty())) {
            String uploadUrl = String.format("https://api.gupshup.io/wa/%s/wa/media/v2", appId);
            UploadMediaResponse uploadResponse =
                httpService.uploadMedia(uploadUrl, card.getMediaFile(), UploadMediaResponse.class);
            if (uploadResponse != null
                && uploadResponse.isSuccess()
                && uploadResponse.getMedia() != null) {
              card.setMediaId(uploadResponse.getMedia().getId());
              card.setMediaUrl(uploadResponse.getMedia().getUrl());
            } else {
              String errMsg = uploadResponse != null ? uploadResponse.getError() : "Unknown error";
              throw new io.github.mrcloss.gupshup.exception.GupshupApiException(
                  "Failed to upload carousel card media file: " + errMsg, 0, null);
            }
          }
        }
      }
    }

    template.validate();
    Map<String, Object> body = convertToMap(GupshupRequestMapper.map(template));
    return httpService.postForm(templateUrl, body, CreateTemplateResponse.class);
  }

  @Override
  public CompletableFuture<CreateTemplateResponse> createTemplateAsync(Template template) {
    if (template instanceof MediaTemplate) {
      MediaTemplate mediaTemplate = (MediaTemplate) template;
      if (mediaTemplate.getMediaFile() != null
          && (mediaTemplate.getMediaId() == null
              || mediaTemplate.getMediaId().isEmpty()
              || mediaTemplate.getMediaUrl() == null
              || mediaTemplate.getMediaUrl().isEmpty())) {
        String uploadUrl = String.format("https://api.gupshup.io/wa/%s/wa/media/v2", appId);
        return httpService
            .uploadMediaAsync(uploadUrl, mediaTemplate.getMediaFile(), UploadMediaResponse.class)
            .thenCompose(
                uploadResponse -> {
                  if (uploadResponse != null
                      && uploadResponse.isSuccess()
                      && uploadResponse.getMedia() != null) {
                    mediaTemplate.setMediaId(uploadResponse.getMedia().getId());
                    mediaTemplate.setMediaUrl(uploadResponse.getMedia().getUrl());

                    template.validate();
                    Map<String, Object> body = convertToMap(GupshupRequestMapper.map(template));
                    return httpService.postFormAsync(
                        templateUrl, body, CreateTemplateResponse.class);
                  } else {
                    String errMsg =
                        uploadResponse != null ? uploadResponse.getError() : "Unknown error";
                    return CompletableFuture.failedFuture(
                        new io.github.mrcloss.gupshup.exception.GupshupApiException(
                            "Failed to upload template media file: " + errMsg, 0, null));
                  }
                });
      }
    }

    if (template instanceof CarouselTemplate) {
      CarouselTemplate carouselTemplate = (CarouselTemplate) template;
      if (carouselTemplate.getCards() != null) {
        List<CompletableFuture<Void>> uploadFutures = new ArrayList<>();
        for (CarouselCard card : carouselTemplate.getCards()) {
          if (card.getMediaFile() != null
              && (card.getMediaId() == null
                  || card.getMediaId().isEmpty()
                  || card.getMediaUrl() == null
                  || card.getMediaUrl().isEmpty())) {
            String uploadUrl = String.format("https://api.gupshup.io/wa/%s/wa/media/v2", appId);
            CompletableFuture<Void> uploadFuture =
                httpService
                    .uploadMediaAsync(uploadUrl, card.getMediaFile(), UploadMediaResponse.class)
                    .thenAccept(
                        uploadResponse -> {
                          if (uploadResponse != null
                              && uploadResponse.isSuccess()
                              && uploadResponse.getMedia() != null) {
                            card.setMediaId(uploadResponse.getMedia().getId());
                            card.setMediaUrl(uploadResponse.getMedia().getUrl());
                          } else {
                            String errMsg =
                                uploadResponse != null
                                    ? uploadResponse.getError()
                                    : "Unknown error";
                            throw new CompletionException(
                                new io.github.mrcloss.gupshup.exception.GupshupApiException(
                                    "Failed to upload carousel card media file: " + errMsg,
                                    0,
                                    null));
                          }
                        });
            uploadFutures.add(uploadFuture);
          }
        }

        if (!uploadFutures.isEmpty()) {
          return CompletableFuture.allOf(uploadFutures.toArray(new CompletableFuture[0]))
              .thenCompose(
                  v -> {
                    template.validate();
                    Map<String, Object> body = convertToMap(GupshupRequestMapper.map(template));
                    return httpService.postFormAsync(
                        templateUrl, body, CreateTemplateResponse.class);
                  })
              .exceptionallyCompose(
                  ex -> {
                    Throwable cause = ex.getCause();
                    if (cause instanceof io.github.mrcloss.gupshup.exception.GupshupApiException) {
                      return CompletableFuture.failedFuture(cause);
                    }
                    return CompletableFuture.failedFuture(ex);
                  });
        }
      }
    }

    template.validate();
    Map<String, Object> body = convertToMap(GupshupRequestMapper.map(template));
    return httpService.postFormAsync(templateUrl, body, CreateTemplateResponse.class);
  }

  @Override
  public DeleteTemplateResponse deleteTemplate(String templateName) {
    return httpService.delete(templateUrl + "/" + templateName, DeleteTemplateResponse.class);
  }

  @Override
  public CompletableFuture<DeleteTemplateResponse> deleteTemplateAsync(String templateName) {
    return httpService.deleteAsync(templateUrl + "/" + templateName, DeleteTemplateResponse.class);
  }

  @Override
  public SendTemplateResponse sendTemplate(SendTemplateRequest sendTemplate) {
    Map<String, Object> body = convertToMap(sendTemplate);
    return httpService.postForm(sendTemplateUrl, body, SendTemplateResponse.class);
  }

  @Override
  public OptInResponse optIn(String appName, String phoneNumber) {
    if (appName == null || appName.trim().isEmpty()) {
      throw new IllegalArgumentException("appName is required");
    }
    if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
      throw new IllegalArgumentException("phoneNumber is required");
    }
    String optInUrl = String.format("https://api.gupshup.io/wa/api/v1/optedin/%s", appName);
    Map<String, Object> body = Map.of("user", phoneNumber);
    return httpService.postForm(optInUrl, body, OptInResponse.class);
  }

  @Override
  public CompletableFuture<OptInResponse> optInAsync(String appName, String phoneNumber) {
    if (appName == null || appName.trim().isEmpty()) {
      return CompletableFuture.failedFuture(new IllegalArgumentException("appName is required"));
    }
    if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
      return CompletableFuture.failedFuture(
          new IllegalArgumentException("phoneNumber is required"));
    }
    String optInUrl = String.format("https://api.gupshup.io/wa/api/v1/optedin/%s", appName);
    Map<String, Object> body = Map.of("user", phoneNumber);
    return httpService.postFormAsync(optInUrl, body, OptInResponse.class);
  }

  private Map<String, Object> convertToMap(QueryParams queryParams) {
    Map<String, Object> map =
        objectMapper.convertValue(queryParams, new TypeReference<Map<String, Object>>() {});
    return map;
  }

  private Map<String, Object> convertToMap(SendTemplateRequest request) {
    Map<String, Object> map =
        objectMapper.convertValue(request, new TypeReference<Map<String, Object>>() {});
    return map;
  }

  private Map<String, Object> convertToMap(TemplateRequest request) {
    Map<String, Object> map =
        objectMapper.convertValue(request, new TypeReference<Map<String, Object>>() {});
    // appId is already in the URL
    map.remove("appId");
    return map;
  }

  private static ObjectMapper createDefaultObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    return mapper;
  }

  public static class Builder {
    private String appId;
    private String apiKey;
    private HttpClient httpClient;
    private ObjectMapper objectMapper;
    private GupshupHttpService httpService;

    public Builder appId(String appId) {
      this.appId = appId;
      return this;
    }

    public Builder apiKey(String apiKey) {
      this.apiKey = apiKey;
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public Builder objectMapper(ObjectMapper objectMapper) {
      this.objectMapper = objectMapper;
      return this;
    }

    public Builder httpService(GupshupHttpService httpService) {
      this.httpService = httpService;
      return this;
    }

    public DefaultGupshupClient build() {
      if (appId == null || appId.trim().isEmpty()) {
        throw new IllegalStateException("appId is required");
      }
      if (apiKey == null || apiKey.trim().isEmpty()) {
        throw new IllegalStateException("apiKey is required");
      }
      return new DefaultGupshupClient(this);
    }
  }
}
