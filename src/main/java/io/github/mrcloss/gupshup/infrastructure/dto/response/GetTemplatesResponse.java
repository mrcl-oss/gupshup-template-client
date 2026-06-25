package io.github.mrcloss.gupshup.infrastructure.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class GetTemplatesResponse extends BaseGupshupResponse {
  List<GupshupTemplateDetails> templates;
}
