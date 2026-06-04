package io.github.mrcloss.gupshup.infrastructure.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarouselCardRequest {
    private String content;
    private List<ButtonRequest> buttons;
    private String sampleText;
    private String mediaId;
    private String mediaUrl;
}
