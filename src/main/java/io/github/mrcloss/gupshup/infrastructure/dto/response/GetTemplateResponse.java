package io.github.mrcloss.gupshup.infrastructure.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class GetTemplateResponse extends BaseGupshupResponse {
  private GupshupTemplateDetails template;
}
