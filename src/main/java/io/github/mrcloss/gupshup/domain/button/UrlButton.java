package io.github.mrcloss.gupshup.domain.button;

import io.github.mrcloss.gupshup.domain.enums.ButtonType;

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
        // Basic URL validation
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new IllegalArgumentException("URL must start with http:// or https://");
        }
    }
}
