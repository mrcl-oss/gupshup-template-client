package io.github.mrcloss.gupshup.domain.button;

import io.github.mrcloss.gupshup.domain.enums.ButtonType;

public class CopyCodeButton extends Button {
    private String exampleValue;

    public CopyCodeButton() {
        super.setType(ButtonType.COPY_CODE);
    }

    public CopyCodeButton(String text, String exampleValue) {
        super(ButtonType.COPY_CODE, text);
        setExampleValue(exampleValue);
    }

    public String getExampleValue() {
        return exampleValue;
    }

    public void setExampleValue(String exampleValue) {
        if (exampleValue == null || exampleValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Example value cannot be null or empty for copy code button");
        }
        this.exampleValue = exampleValue;
    }
}
