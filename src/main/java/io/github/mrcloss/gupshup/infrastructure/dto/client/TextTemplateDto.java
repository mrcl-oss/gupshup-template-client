package io.github.mrcloss.gupshup.infrastructure.dto.client;

import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import io.github.mrcloss.gupshup.domain.template.TextTemplate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TextTemplateDto extends BaseTemplateDto {

  /**
   * Default constructor. Explicitly sets the template type to maintain consistency during
   * programmatic instantiation.
   */
  public TextTemplateDto() {
    super();
    this.setTemplateType(TemplateType.TEXT);
  }

  private String header;
  private List<String> variableHeaderExamples;

  @Override
  public TextTemplate toDomain() {
    TextTemplate template =
        new TextTemplate(
            getElementName(),
            getLanguageCode(),
            getBody(),
            getVariableExamples(),
            getCategory(),
            getAppId(),
            getTags(),
            getParameterFormat(),
            header,
            variableHeaderExamples);
    template.setFooter(getFooter());
    template.setButtons(getButtons());
    template.setMessageValidity(getMessageValidity());
    template.setLtoAttributes(getLtoAttributes());
    return template;
  }
}
