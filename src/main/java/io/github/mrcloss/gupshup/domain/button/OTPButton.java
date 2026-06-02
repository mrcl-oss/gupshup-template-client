package io.github.mrcloss.gupshup.domain.button;

import io.github.mrcloss.gupshup.domain.enums.ButtonType;

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

    public OTPButtonType getOtpType() {
        return otpType;
    }

    public void setOtpType(OTPButtonType otpType) {
        this.otpType = otpType;
    }
}
