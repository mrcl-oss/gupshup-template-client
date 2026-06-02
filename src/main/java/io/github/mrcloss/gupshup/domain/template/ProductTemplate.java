package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.enums.TemplateType;

public class ProductTemplate extends Template {
    public ProductTemplate() {
        super.setTemplateType(TemplateType.PRODUCT);
    }
}
