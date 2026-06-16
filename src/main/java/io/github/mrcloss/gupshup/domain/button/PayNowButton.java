package io.github.mrcloss.gupshup.domain.button;

import io.github.mrcloss.gupshup.domain.enums.ButtonType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayNowButton extends UrlButton {
  private boolean paymentLinkPreview;
  private UrlButton underlyingUrlButton;

  public PayNowButton() {
    super.setType(ButtonType.URL); // PayNow is technically a URL button in many implementations
  }

  public PayNowButton(String text, boolean paymentLinkPreview, UrlButton underlyingUrlButton) {
    super(text);
    this.paymentLinkPreview = paymentLinkPreview;
    this.underlyingUrlButton = underlyingUrlButton;
  }

  @Override
  public void validate() {
    validateText(getText());
    if (underlyingUrlButton == null) {
      throw new IllegalStateException("Underlying URL button is required for Pay Now button");
    }
    underlyingUrlButton.validate();
  }
}
