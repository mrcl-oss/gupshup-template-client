package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.button.Button;
import io.github.mrcloss.gupshup.domain.button.CatalogButton;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class CatalogTemplate extends Template {
    public CatalogTemplate(String elementName, LanguageCode languageCode, String body, TemplateCategory category, String appId, List<String> tags, TemplateParameterFormat parameterFormat) {
        super(elementName, languageCode, body, category, appId, tags, TemplateType.CATALOG, parameterFormat);
        super.setButtons(Collections.singletonList(new CatalogButton("View catalog")));
    }

    @Override
    public void setButtons(List<Button> buttons) {
        throw new UnsupportedOperationException("Catalog template buttons cannot be modified");
    }

    @Override
    public void addButton(Button button) {
        throw new UnsupportedOperationException("Catalog template buttons cannot be modified");
    }
}
