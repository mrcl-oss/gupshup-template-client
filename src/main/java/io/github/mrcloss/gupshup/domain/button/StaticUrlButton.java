package io.github.mrcloss.gupshup.domain.button;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaticUrlButton extends UrlButton {
    @Setter(AccessLevel.NONE)
    private String url;

    public StaticUrlButton() {
        super();
    }

    public StaticUrlButton(String text, String url) {
        super(text);
        setUrl(url);
    }

    public void setUrl(String url) {
        validateUrl(url);
        this.url = url;
    }

    @Override
    public void validate() {
        super.validate();
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalStateException("URL is required for static URL button");
        }
    }
}
