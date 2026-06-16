package io.github.mrcloss.gupshup.exception;

import lombok.Getter;

@Getter
public class GupshupApiException extends RuntimeException {
  private final int statusCode;
  private final String responseBody;

  public GupshupApiException(String message, int statusCode, String responseBody) {
    super(message);
    this.statusCode = statusCode;
    this.responseBody = responseBody;
  }
}
