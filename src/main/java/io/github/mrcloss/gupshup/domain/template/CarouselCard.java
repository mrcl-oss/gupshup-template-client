package io.github.mrcloss.gupshup.domain.template;

import java.util.List;
import io.github.mrcloss.gupshup.domain.button.Button;

public class CarouselCard {

    public enum CarouselCardHeaderType {
        VIDEO,
        IMAGE,
    }

    private String body;
    private List<Button> buttons;
    private List<String> variableExamples;
    private String mediaId;
    private String mediaUrl;
    private CarouselCardHeaderType headerType;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        if (buttons != null && buttons.size() > 2) {
            throw new IllegalArgumentException("Carousel cards can have at most 2 buttons");
        }
        this.buttons = buttons;
    }

    public List<String> getVariableExamples() {
        return variableExamples;
    }

    public void setVariableExamples(List<String> variableExamples) {
        this.variableExamples = variableExamples;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        if (mediaUrl != null && !mediaUrl.startsWith("http://") && !mediaUrl.startsWith("https://")) {
            throw new IllegalArgumentException("Media URL must start with http:// or https://");
        }
        this.mediaUrl = mediaUrl;
    }

    public CarouselCardHeaderType getHeaderType() {
        return headerType;
    }

    public void setHeaderType(CarouselCardHeaderType headerType) {
        this.headerType = headerType;
    }
}
