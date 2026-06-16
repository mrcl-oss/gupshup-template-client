package io.github.mrcloss.gupshup.infrastructure.dto.response;

import lombok.Getter;
import lombok.Setter;

/** Response returned when a template is successfully created. */
@Getter
@Setter
public class CreateTemplateResponse extends BaseGupshupResponse {
  private GupshupTemplate template;
}
