package io.github.mrcloss.gupshup.infrastructure.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTemplatesResponse extends BaseGupshupResponse {
  List<GupshupTemplate> templates;
}
