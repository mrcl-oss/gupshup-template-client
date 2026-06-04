package io.github.mrcloss.gupshup.infrastructure.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GupshupResponse {
    private String status;
    private String message;
    private Object data;
    private String error;
}
