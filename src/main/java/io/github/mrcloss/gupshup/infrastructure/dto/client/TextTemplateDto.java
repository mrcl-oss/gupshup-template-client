package io.github.mrcloss.gupshup.infrastructure.dto.client;

import io.github.mrcloss.gupshup.domain.template.TextTemplate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TextTemplateDto extends BaseTemplateDto {
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
