package io.github.mrcloss.gupshup.domain.button;

public class StaticUrlButton extends UrlButton {
    private String url;

    public StaticUrlButton() {
        super();
    }

    public StaticUrlButton(String text, String url) {
        super(text);
        setUrl(url);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        validateUrl(url);
        this.url = url;
    }
}
