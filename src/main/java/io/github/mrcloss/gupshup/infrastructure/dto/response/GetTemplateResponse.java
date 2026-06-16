package io.github.mrcloss.gupshup.infrastructure.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTemplateResponse extends BaseGupshupResponse {
    private GupshupTemplate template;
}
