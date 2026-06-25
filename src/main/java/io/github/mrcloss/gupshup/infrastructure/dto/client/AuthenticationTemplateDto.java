package io.github.mrcloss.gupshup.infrastructure.dto.client;

import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import io.github.mrcloss.gupshup.domain.template.AuthenticationTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthenticationTemplateDto extends BaseTemplateDto {
  private boolean addSecurityRecommendation;
  private int codeExpirationMinutes;

  /**
   * Default constructor. Explicitly sets the template type to maintain consistency during
   * programmatic instantiation.
   */
  public AuthenticationTemplateDto() {
    super();
    this.setTemplateType(TemplateType.AUTHENTICATION);
  }

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
