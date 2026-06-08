package io.github.mrcloss.gupshup.domain.template;

import java.util.List;
import io.github.mrcloss.gupshup.domain.button.Button;
import io.github.mrcloss.gupshup.domain.button.PhoneNumberButton;
import io.github.mrcloss.gupshup.domain.button.QuickReplyButton;
import io.github.mrcloss.gupshup.domain.button.UrlButton;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    public CarouselCard() {
    }

    public CarouselCard(String body, CarouselCardHeaderType headerType) {
        this.body = body;
        this.headerType = headerType;
    }

    public CarouselCard(String body, List<String> variableExamples, CarouselCardHeaderType headerType) {
        this.body = body;
        this.variableExamples = variableExamples;
        this.headerType = headerType;
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
        if (!(button instanceof QuickReplyButton) &&
            !(button instanceof PhoneNumberButton) &&
            !(button instanceof UrlButton)) {
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

        boolean hasExamples = variableExamples != null && !variableExamples.isEmpty();
        boolean hasVariablesInBody = body != null && java.util.regex.Pattern.compile("\\{\\{\\d+\\}\\}").matcher(body).find();

        if (hasVariablesInBody && !hasExamples) {
            throw new IllegalStateException("Carousel card body cannot contain variables if variable examples are not provided");
        }

        if (hasExamples) {
            for (int i = 1; i <= variableExamples.size(); i++) {
                String placeholder = "{{" + i + "}}";
                if (!body.contains(placeholder)) {
                    throw new IllegalStateException("Carousel card body must contain " + placeholder + " when " + variableExamples.size() + " variable examples are provided");
                }
            }
        }
    }

    public void setMediaUrl(String mediaUrl) {
        if (mediaUrl != null && !mediaUrl.startsWith("http://") && !mediaUrl.startsWith("https://")) {
            throw new IllegalArgumentException("Media URL must start with http:// or https://");
        }
        this.mediaUrl = mediaUrl;
    }
}
