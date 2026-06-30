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

  @lombok.Setter(lombok.AccessLevel.NONE)
  private String message;

  private String error;

  public void setMessage(Object message) {
    if (message instanceof String) {
      this.message = (String) message;
    } else if (message instanceof java.util.Map) {
      java.util.Map<?, ?> map = (java.util.Map<?, ?>) message;
      Object msgVal = map.get("message");
      this.message = msgVal != null ? msgVal.toString() : map.toString();
    } else if (message != null) {
      this.message = message.toString();
    }
  }

  /**
   * Helper method to check if the request was successful.
   *
   * @return true if status is "success"
   */
  public boolean isSuccess() {
    return "success".equalsIgnoreCase(status);
  }
}
