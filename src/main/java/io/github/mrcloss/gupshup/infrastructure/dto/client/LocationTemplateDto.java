package io.github.mrcloss.gupshup.infrastructure.dto.client;

import io.github.mrcloss.gupshup.domain.template.LocationTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LocationTemplateDto extends BaseTemplateDto {
  @Override
  public LocationTemplate toDomain() {
    LocationTemplate template =
        new LocationTemplate(
            getElementName(),
            getLanguageCode(),
            getBody(),
            getVariableExamples(),
            getCategory(),
            getAppId(),
            getTags(),
            getParameterFormat());
    template.setFooter(getFooter());
    template.setButtons(getButtons());
    template.setMessageValidity(getMessageValidity());
    template.setLtoAttributes(getLtoAttributes());
    return template;
  }
}
