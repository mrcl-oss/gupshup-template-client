package io.github.mrcloss.gupshup.domain.template;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.mrcloss.gupshup.domain.button.PayNowButton;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextTemplate extends Template {
    @Setter(AccessLevel.NONE)
    private String header;
    private List<String> variableHeaderExamples;

    public TextTemplate(String elementName, LanguageCode languageCode, String body, TemplateCategory category, String appId, List<String> tags, TemplateParameterFormat parameterFormat) {
        super(elementName, languageCode, body, category, appId, tags, TemplateType.TEXT, parameterFormat);
    }

    public void setHeader(String header) {
        if (header != null) {
            Matcher matcher = Pattern.compile("\\{\\{\\d+\\}\\}").matcher(header);
            int count = 0;
            while (matcher.find()) {
                count++;
            }
            if (count > 1) {
                throw new IllegalArgumentException("Text template header can have at most 1 variable");
            }
        }
        this.header = header;
    }

    @Override
    public void validate() {
        super.validate();
        boolean hasPayNow = getButtons().stream().anyMatch(b -> b instanceof PayNowButton);
        if (hasPayNow && header != null && !header.trim().isEmpty()) {
            throw new IllegalStateException("Text templates with Pay Now button cannot have a header");
        }

        if (variableHeaderExamples != null && !variableHeaderExamples.isEmpty()) {
            if (header == null) {
                throw new IllegalStateException("Header is required when variable header examples are provided");
            }
            for (int i = 1; i <= variableHeaderExamples.size(); i++) {
                String placeholder = "{{" + i + "}}";
                if (!header.contains(placeholder)) {
                    throw new IllegalStateException("Header must contain " + placeholder + " when " + variableHeaderExamples.size() + " variable header examples are provided");
                }
            }
        }
    }
}
