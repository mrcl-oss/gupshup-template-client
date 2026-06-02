package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.enums.TemplateType;

public class ImageTemplate extends MediaTemplate {
    public ImageTemplate() {
        super.setTemplateType(TemplateType.IMAGE);
    }
}
