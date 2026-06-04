package io.github.mrcloss.gupshup.infrastructure.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UrlButtonRequest extends ButtonRequest {
    private String url;
    private List<String> example;
    private Boolean paymentLinkPreview;
}
