package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import io.github.mrcloss.gupshup.domain.button.CatalogButton;
import java.util.Collections;

public class CatalogTemplate extends Template {
    public CatalogTemplate(String elementName, io.github.mrcloss.gupshup.domain.enums.LanguageCode languageCode, String body, io.github.mrcloss.gupshup.domain.enums.TemplateCategory category, String appId, java.util.List<String> tags, io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat parameterFormat) {
        super(elementName, languageCode, body, category, appId, tags, TemplateType.CATALOG, parameterFormat);
        super.setButtons(Collections.singletonList(new CatalogButton("View catalog")));
    }

    @Override
    public void setButtons(java.util.List<io.github.mrcloss.gupshup.domain.button.Button> buttons) {
        throw new UnsupportedOperationException("Catalog template buttons cannot be modified");
    }

    @Override
    public void addButton(io.github.mrcloss.gupshup.domain.button.Button button) {
        throw new UnsupportedOperationException("Catalog template buttons cannot be modified");
    }
}
