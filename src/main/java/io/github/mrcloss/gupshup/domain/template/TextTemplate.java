package io.github.mrcloss.gupshup.domain.template;

import java.util.List;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;

public class TextTemplate extends Template {
    private String header;
    private List<String> variableHeaderExamples;

    public TextTemplate(String elementName, io.github.mrcloss.gupshup.domain.enums.LanguageCode languageCode, String body, io.github.mrcloss.gupshup.domain.enums.TemplateCategory category, String appId, List<String> tags, io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat parameterFormat) {
        super(elementName, languageCode, body, category, appId, tags, TemplateType.TEXT, parameterFormat);
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        if (header != null) {
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\\{\\{\\d+\\}\\}").matcher(header);
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

    public List<String> getVariableHeaderExamples() {
        return variableHeaderExamples;
    }

    public void setVariableHeaderExamples(List<String> variableHeaderExamples) {
        this.variableHeaderExamples = variableHeaderExamples;
    }

    @Override
    public void validate() {
        super.validate();
        boolean hasPayNow = getButtons().stream().anyMatch(b -> b instanceof io.github.mrcloss.gupshup.domain.button.PayNowButton);
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
