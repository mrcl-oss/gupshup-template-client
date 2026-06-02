package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.enums.TemplateType;

public class VideoTemplate extends MediaTemplate {
    public VideoTemplate() {
        super.setTemplateType(TemplateType.VIDEO);
    }
}
