package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.enums.TemplateType;

public class VideoTemplate extends MediaTemplate {
    public VideoTemplate(String elementName, io.github.mrcloss.gupshup.domain.enums.LanguageCode languageCode, String body, io.github.mrcloss.gupshup.domain.enums.TemplateCategory category, String appId, java.util.List<String> tags, io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat parameterFormat) {
        super(elementName, languageCode, body, category, appId, tags, TemplateType.VIDEO, parameterFormat);
    }

    @Override
    public String[] getAllowedExtensions() {
        return new String[]{".mp4", ".3gp"};
    }
}
