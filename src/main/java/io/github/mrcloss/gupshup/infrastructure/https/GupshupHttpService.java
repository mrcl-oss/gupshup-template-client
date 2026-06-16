package io.github.mrcloss.gupshup.infrastructure.https;

import io.github.mrcloss.gupshup.infrastructure.dto.response.BaseGupshupResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/** Service for making HTTP requests to the Gupshup API. */
public interface GupshupHttpService {

  /**
   * Executes a GET request with URL-encoded query parameters.
   *
   * @param <T> The type of the response.
   * @param path The relative path to the endpoint.
   * @param queryParams The query parameters to append to the URL as a map.
   * @param responseType The class of the response type.
   * @return The response from Gupshup.
   */
  <T extends BaseGupshupResponse> T getWithParams(
      String path, Map<String, Object> queryParams, Class<T> responseType);

  /**
   * Executes a POST request with form-urlencoded body.
   *
   * @param path The relative path to the endpoint.
   * @param body The body of the request as a map.
   * @param responseType The class of the response type.
   * @param <T> The type of the response.
   * @return The response from Gupshup.
   */
  <T extends BaseGupshupResponse> T postForm(
      String path, Map<String, Object> body, Class<T> responseType);

  /**
   * Executes a POST request with form-urlencoded body asynchronously.
   *
   * @param path The relative path to the endpoint.
   * @param body The body of the request as a map.
   * @param responseType The class of the response type.
   * @param <T> The type of the response.
   * @return A CompletableFuture with the response.
   */
  <T extends BaseGupshupResponse> CompletableFuture<T> postFormAsync(
      String path, Map<String, Object> body, Class<T> responseType);

  /**
   * Executes a DELETE request.
   *
   * @param path The relative path to the endpoint.
   * @param responseType The class of the response type.
   * @param <T> The type of the response.
   * @return The response from Gupshup.
   */
  <T extends BaseGupshupResponse> T delete(String path, Class<T> responseType);

  /**
   * Executes a DELETE request asynchronously.
   *
   * @param path The relative path to the endpoint.
   * @param responseType The class of the response type.
   * @param <T> The type of the response.
   * @return A CompletableFuture with the response.
   */
  <T extends BaseGupshupResponse> CompletableFuture<T> deleteAsync(
      String path, Class<T> responseType);
}
