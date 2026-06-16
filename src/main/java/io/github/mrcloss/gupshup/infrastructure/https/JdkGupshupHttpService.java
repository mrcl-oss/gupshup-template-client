package io.github.mrcloss.gupshup.infrastructure.https;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mrcloss.gupshup.exception.GupshupApiException;
import io.github.mrcloss.gupshup.infrastructure.dto.response.BaseGupshupResponse;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/** Implementation of GupshupHttpService using JDK's HttpClient. */
public class JdkGupshupHttpService implements GupshupHttpService {

  private final String apiKey;
  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;

  public JdkGupshupHttpService(String apiKey, HttpClient httpClient, ObjectMapper objectMapper) {
    this.apiKey = apiKey;
    this.httpClient = httpClient;
    this.objectMapper = objectMapper;
  }

  @Override
  public <T extends BaseGupshupResponse> T getWithParams(
      String path, Map<String, Object> queryParams, Class<T> responseType) {
    try {
      String finalPath = buildQueryString(path, queryParams);

      HttpRequest request = buildBaseRequest(path).uri(URI.create(finalPath)).GET().build();

      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      return handleResponse(response, responseType);

    } catch (GupshupApiException e) {
      throw e;
    } catch (Exception e) {
      throw new GupshupApiException("Failed to execute GET request: " + e.getMessage(), 0, null);
    }
  }

  @Override
  public <T extends BaseGupshupResponse> T postForm(
      String path, Map<String, Object> body, Class<T> responseType) {
    try {
      String formBody = buildFormBody(body);
      HttpRequest request =
          buildBaseRequest(path)
              .header("Content-Type", "application/x-www-form-urlencoded")
              .POST(HttpRequest.BodyPublishers.ofString(formBody))
              .build();

      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      return handleResponse(response, responseType);
    } catch (GupshupApiException e) {
      throw e;
    } catch (Exception e) {
      throw new GupshupApiException("Failed to execute POST request: " + e.getMessage(), 0, null);
    }
  }

  @Override
  public <T extends BaseGupshupResponse> CompletableFuture<T> postFormAsync(
      String path, Map<String, Object> body, Class<T> responseType) {
    try {
      String formBody = buildFormBody(body);
      HttpRequest request =
          buildBaseRequest(path)
              .header("Content-Type", "application/x-www-form-urlencoded")
              .POST(HttpRequest.BodyPublishers.ofString(formBody))
              .build();

      return httpClient
          .sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(response -> handleResponse(response, responseType));
    } catch (Exception e) {
      return CompletableFuture.failedFuture(
          new GupshupApiException(
              "Failed to start async POST request: " + e.getMessage(), 0, null));
    }
  }

  @Override
  public <T extends BaseGupshupResponse> T delete(String path, Class<T> responseType) {
    try {
      HttpRequest request = buildBaseRequest(path).DELETE().build();

      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      return handleResponse(response, responseType);
    } catch (GupshupApiException e) {
      throw e;
    } catch (Exception e) {
      throw new GupshupApiException("Failed to execute DELETE request: " + e.getMessage(), 0, null);
    }
  }

  @Override
  public <T extends BaseGupshupResponse> CompletableFuture<T> deleteAsync(
      String path, Class<T> responseType) {
    try {
      HttpRequest request = buildBaseRequest(path).DELETE().build();

      return httpClient
          .sendAsync(request, HttpResponse.BodyHandlers.ofString())
          .thenApply(response -> handleResponse(response, responseType));
    } catch (Exception e) {
      return CompletableFuture.failedFuture(
          new GupshupApiException(
              "Failed to start async DELETE request: " + e.getMessage(), 0, null));
    }
  }

  private HttpRequest.Builder buildBaseRequest(String path) {
    return HttpRequest.newBuilder()
        .uri(URI.create(path))
        .header("apikey", apiKey)
        .header("Accept", "application/json");
  }

  private String buildFormBody(Map<String, Object> body) {
    return body.entrySet().stream()
        .filter(e -> e.getValue() != null)
        .map(
            e -> {
              String value;
              if (e.getValue() instanceof String) {
                value = (String) e.getValue();
              } else if (e.getValue() instanceof Iterable || e.getValue() instanceof Map) {
                try {
                  value = objectMapper.writeValueAsString(e.getValue());
                } catch (Exception ex) {
                  value = e.getValue().toString();
                }
              } else {
                try {
                  String jsonValue = objectMapper.writeValueAsString(e.getValue());
                  if (jsonValue.startsWith("\"") && jsonValue.endsWith("\"")) {
                    value = jsonValue.substring(1, jsonValue.length() - 1);
                  } else {
                    value = jsonValue;
                  }
                } catch (Exception ex) {
                  value = e.getValue().toString();
                }
              }
              return URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8)
                  + "="
                  + URLEncoder.encode(value, StandardCharsets.UTF_8);
            })
        .collect(Collectors.joining("&"));
  }

  private String buildQueryString(String path, Map<String, Object> queryParams) {

    String finalPath = path;

    if (queryParams != null && !queryParams.isEmpty()) {
      String queryString =
          queryParams.entrySet().stream()
              .filter(e -> e.getValue() != null)
              .map(
                  e ->
                      URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8)
                          + "="
                          + URLEncoder.encode(e.getValue().toString(), StandardCharsets.UTF_8))
              .collect(Collectors.joining("&"));

      String separator = path.contains("?") ? "&" : "?";
      finalPath = path + separator + queryString;
    }

    return finalPath;
  }

  private <T extends BaseGupshupResponse> T handleResponse(
      HttpResponse<String> response, Class<T> responseType) {
    String body = response.body();
    int statusCode = response.statusCode();

    try {
      if (body == null || body.trim().isEmpty()) {
        if (statusCode >= 200 && statusCode < 300) {
          T successResponse = responseType.getDeclaredConstructor().newInstance();
          successResponse.setStatus("success");
          return successResponse;
        }
        throw new GupshupApiException("Empty response body from Gupshup", statusCode, body);
      }

      T gupshupResponse = objectMapper.readValue(body, responseType);

      if (statusCode >= 200 && statusCode < 300) {
        return gupshupResponse;
      } else {
        String errorMessage =
            gupshupResponse.getError() != null
                ? gupshupResponse.getError()
                : (gupshupResponse.getMessage() != null
                    ? gupshupResponse.getMessage()
                    : "Gupshup API error");
        throw new GupshupApiException(errorMessage, statusCode, body);
      }
    } catch (GupshupApiException e) {
      throw e;
    } catch (Exception e) {
      throw new GupshupApiException(
          "Failed to parse Gupshup response: " + e.getMessage(), statusCode, body);
    }
  }
}
