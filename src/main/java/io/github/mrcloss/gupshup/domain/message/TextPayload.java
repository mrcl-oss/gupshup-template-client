package io.github.mrcloss.gupshup.domain.message;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TextPayload extends GupshupMessage{
    private final PostBackTexts postbackTexts;
    
    public TextPayload(int index, String text) {
        super(MessageType.TEXT);
        this.postbackTexts = new PostBackTexts(index, text);
    }

    @Getter
    public static class PostBackTexts {

        private final int index;
        private final String text;

        public PostBackTexts(int index, String text) {
            this.index = index;
            this.text = text;
        }
    }
}
