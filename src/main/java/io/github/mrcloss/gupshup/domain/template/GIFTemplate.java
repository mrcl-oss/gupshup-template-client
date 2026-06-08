package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GIFTemplate extends MediaTemplate {
    public GIFTemplate(String elementName, LanguageCode languageCode, String body, TemplateCategory category, String appId, List<String> tags, TemplateParameterFormat parameterFormat) {
        super(elementName, languageCode, body, category, appId, tags, TemplateType.GIF, parameterFormat);
    }

    public GIFTemplate(String elementName, LanguageCode languageCode, String body, List<String> variableExamples, TemplateCategory category, String appId, List<String> tags, TemplateParameterFormat parameterFormat) {
        super(elementName, languageCode, body, variableExamples, category, appId, tags, TemplateType.GIF, parameterFormat);
    }

    @Override
    public String[] getAllowedExtensions() {
        return new String[]{".gif"};
    }
}
