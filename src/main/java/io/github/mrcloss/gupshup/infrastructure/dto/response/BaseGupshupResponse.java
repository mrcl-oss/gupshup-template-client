package io.github.mrcloss.gupshup.infrastructure.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Base class for all Gupshup API responses. */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseGupshupResponse {
  private String status;
  private String message;
  private String error;

  /**
   * Helper method to check if the request was successful.
   *
   * @return true if status is "success"
   */
  public boolean isSuccess() {
    return "success".equalsIgnoreCase(status);
  }
}
