package io.github.mrcloss.gupshup.domain.button;

import io.github.mrcloss.gupshup.domain.enums.ButtonType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CopyCodeButton extends Button {
    @Setter(AccessLevel.NONE)
    private String exampleValue;

    public CopyCodeButton() {
        super.setType(ButtonType.COPY_CODE);
    }

    public CopyCodeButton(String text, String exampleValue) {
        super(ButtonType.COPY_CODE, text);
        setExampleValue(exampleValue);
    }

    public void setExampleValue(String exampleValue) {
        if (exampleValue == null || exampleValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Example value cannot be null or empty for copy code button");
        }
        if (!exampleValue.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("Example value cannot contain spaces or special characters");
        }
        this.exampleValue = exampleValue;
    }

    @Override
    public void validate() {
        validateText(getText());
        if (exampleValue == null || exampleValue.trim().isEmpty()) {
            throw new IllegalStateException("Example value is required for copy code button");
        }
        if (!exampleValue.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalStateException("Example value cannot contain spaces or special characters");
        }
    }
}
