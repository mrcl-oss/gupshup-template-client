package io.github.mrcloss.gupshup.domain.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, 
    include = JsonTypeInfo.As.PROPERTY, 
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ImagePayload.class, name = "image"),
    @JsonSubTypes.Type(value = LocationPayload.class, name = "location"),
    @JsonSubTypes.Type(value = CarouselPayload.class, name = "carousel"),
    @JsonSubTypes.Type(value = VideoPayload.class, name = "video"),
    @JsonSubTypes.Type(value = DocumentPayload.class, name = "document"),
    @JsonSubTypes.Type(value = TextPayload.class, name = "text"),
    @JsonSubTypes.Type(value = VideoPayload.class, name = "gif")
})
public abstract class GupshupMessage {  

    @JsonIgnore 
    private final MessageType messageType;

    protected GupshupMessage(MessageType messageType) {
        this.messageType = messageType;
    }

    public enum MessageType {
        IMAGE("image"), 
        VIDEO("video"), 
        TEXT("text"),
        DOCUMENT("document"),
        GIF("gif"),
        LOCATION("location"),
        CAROUSEL("carousel");
        
        private final String value;

        MessageType(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }

}
