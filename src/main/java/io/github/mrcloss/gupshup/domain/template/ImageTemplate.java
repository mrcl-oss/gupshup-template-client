package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageTemplate extends MediaTemplate {
  public ImageTemplate(
      String elementName,
      LanguageCode languageCode,
      String body,
      TemplateCategory category,
      String appId,
      List<String> tags,
      TemplateParameterFormat parameterFormat) {
    super(
        elementName,
        languageCode,
        body,
        category,
        appId,
        tags,
        TemplateType.IMAGE,
        parameterFormat);
  }

  public ImageTemplate(
      String elementName,
      LanguageCode languageCode,
      String body,
      List<String> variableExamples,
      TemplateCategory category,
      String appId,
      List<String> tags,
      TemplateParameterFormat parameterFormat) {
    super(
        elementName,
        languageCode,
        body,
        variableExamples,
        category,
        appId,
        tags,
        TemplateType.IMAGE,
        parameterFormat);
  }

  @Override
  public String[] getAllowedExtensions() {
    return new String[] {".jpg", ".jpeg", ".png", ".webp"};
  }
}
