package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.enums.TemplateType;

public class DocumentTemplate extends MediaTemplate {
    public DocumentTemplate() {
        super.setTemplateType(TemplateType.DOCUMENT);
    }
}
