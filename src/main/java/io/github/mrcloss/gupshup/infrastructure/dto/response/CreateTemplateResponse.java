package io.github.mrcloss.gupshup.infrastructure.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Response returned when a template is successfully created. */
@Getter
@Setter
@ToString(callSuper = true)
public class CreateTemplateResponse extends BaseGupshupResponse {
  private GupshupTemplateDetails template;
}
