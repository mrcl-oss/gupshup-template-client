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
        if (buttons != null) {
            if (buttons.isEmpty() || buttons.size() > 2) {
                throw new IllegalArgumentException("Carousel cards must have 1 or 2 buttons");
            }
            for (Button button : buttons) {
                validateButtonType(button);
            }
        }
        this.buttons = buttons;
    }

    private void validateButtonType(Button button) {
        if (!(button instanceof io.github.mrcloss.gupshup.domain.button.QuickReplyButton) &&
            !(button instanceof io.github.mrcloss.gupshup.domain.button.PhoneNumberButton) &&
            !(button instanceof io.github.mrcloss.gupshup.domain.button.UrlButton)) {
            throw new IllegalArgumentException("Carousel cards only accept QuickReply, PhoneNumber and URL Buttons");
        }
    }

    public void validate() {
        if (buttons == null || buttons.isEmpty() || buttons.size() > 2) {
            throw new IllegalStateException("Carousel cards must have 1 or 2 buttons");
        }
        for (Button button : buttons) {
            button.validate();
        }
        if (headerType == null) {
            throw new IllegalStateException("Carousel card header type is required");
        }
        if (body == null || body.trim().isEmpty()) {
            throw new IllegalStateException("Carousel card body is required");
        }
        if (variableExamples != null && !variableExamples.isEmpty()) {
            for (int i = 1; i <= variableExamples.size(); i++) {
                String placeholder = "{{" + i + "}}";
                if (!body.contains(placeholder)) {
                    throw new IllegalStateException("Carousel card body must contain " + placeholder + " when " + variableExamples.size() + " variable examples are provided");
                }
            }
        }
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
