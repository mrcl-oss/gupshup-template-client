package io.github.mrcloss.gupshup.domain.button;

import io.github.mrcloss.gupshup.domain.enums.ButtonType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneNumberButton extends Button {
    @Setter(AccessLevel.NONE)
    private String phoneNumber;

    public PhoneNumberButton() {
        super.setType(ButtonType.PHONE_NUMBER);
    }

    public PhoneNumberButton(String text, String phoneNumber) {
        super(ButtonType.PHONE_NUMBER, text);
        setPhoneNumber(phoneNumber);
    }

    public void setPhoneNumber(String phoneNumber) {
        validatePhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("^\\+?[1-9]\\d{1,14}$")) {
            throw new IllegalArgumentException("Invalid phone number format. It should follow E.164 format (e.g., +1234567890)");
        }
    }

    @Override
    public void validate() {
        validateText(getText());
        if (phoneNumber == null) {
            throw new IllegalStateException("Phone number is required");
        }
    }
}
