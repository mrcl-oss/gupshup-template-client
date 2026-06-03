package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.button.Button;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import io.github.mrcloss.gupshup.domain.button.MPMButton;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class ProductTemplate extends Template {
    public ProductTemplate(String elementName, LanguageCode languageCode, String body, TemplateCategory category, String appId, List<String> tags, TemplateParameterFormat parameterFormat) {
        super(elementName, languageCode, body, category, appId, tags, TemplateType.PRODUCT, parameterFormat);
        super.setButtons(Collections.singletonList(new MPMButton("View items")));
    }

    @Override
    public void setButtons(List<Button> buttons) {
        throw new UnsupportedOperationException("Product template buttons cannot be modified");
    }

    @Override
    public void addButton(Button button) {
        throw new UnsupportedOperationException("Product template buttons cannot be modified");
    }
}
