package io.github.mrcloss.gupshup.infrastructure.dto.client;

import io.github.mrcloss.gupshup.domain.template.AuthenticationTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthenticationTemplateDto extends BaseTemplateDto {
  private boolean addSecurityRecommendation;
  private int codeExpirationMinutes;

  @Override
  public AuthenticationTemplate toDomain() {
    AuthenticationTemplate template =
        new AuthenticationTemplate(
            getElementName(),
            getLanguageCode(),
            getBody(),
            getVariableExamples(),
            getAppId(),
            getTags(),
            getParameterFormat());
    template.setAddSecurityRecommendation(addSecurityRecommendation);
    template.setCodeExpirationMinutes(codeExpirationMinutes);
    template.setFooter(getFooter());
    template.setMessageValidity(getMessageValidity());
    return template;
  }
}
