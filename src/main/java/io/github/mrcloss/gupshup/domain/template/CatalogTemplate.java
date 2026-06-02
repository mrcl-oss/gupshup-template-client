package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.enums.TemplateType;

public class CatalogTemplate extends Template {
    public CatalogTemplate() {
        super.setTemplateType(TemplateType.CATALOG);
    }
}
