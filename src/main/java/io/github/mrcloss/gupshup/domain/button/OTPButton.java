package io.github.mrcloss.gupshup.domain.button;

import io.github.mrcloss.gupshup.domain.enums.ButtonType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OTPButton extends Button {
  public enum OTPButtonType {
    COPY_CODE
  }

  private OTPButtonType otpType;

  public OTPButton() {
    super.setType(ButtonType.OTP);
  }

  public OTPButton(String text, OTPButtonType otpType) {
    super(ButtonType.OTP, text);
    this.otpType = otpType;
  }

  @Override
  public void validate() {
    validateText(getText());
    if (otpType == null) {
      throw new IllegalStateException("OTP type is required");
    }
  }
}
