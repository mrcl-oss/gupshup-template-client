package io.github.mrcloss.gupshup.domain.button;

import io.github.mrcloss.gupshup.domain.enums.ButtonType;

public abstract class Button {
    private ButtonType type;
    private String text;

    public Button() {
    }

    public Button(ButtonType type, String text) {
        this.type = type;
        this.text = text;
    }

    public ButtonType getType() {
        return type;
    }

    public void setType(ButtonType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public abstract void validate();

    /**
     * Basic validation for text length or content if needed.
     */
    protected void validateText(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Button text cannot be null or empty");
        }
    }
}
