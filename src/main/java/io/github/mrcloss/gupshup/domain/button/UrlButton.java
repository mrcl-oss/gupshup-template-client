package io.github.mrcloss.gupshup.domain.button;

import io.github.mrcloss.gupshup.domain.enums.ButtonType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public abstract class UrlButton extends Button {

  public UrlButton() {
    super.setType(ButtonType.URL);
  }

  public UrlButton(String text) {
    super(ButtonType.URL, text);
  }

  protected void validateUrl(String url) {
    if (url == null || url.trim().isEmpty()) {
      throw new IllegalArgumentException("URL cannot be null or empty");
    }
    // Strict URL validation
    if (!url.matches("^https?://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}.*")) {
      throw new IllegalArgumentException(
          "URL must follow a valid structure like 'https://example.com'");
    }
  }

  @Override
  public void validate() {
    validateText(getText());
  }
}
