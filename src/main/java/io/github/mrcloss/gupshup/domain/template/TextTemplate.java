package io.github.mrcloss.gupshup.domain.template;

import java.util.List;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;

public class TextTemplate extends Template {
    private String header;
    private List<String> variableHeaderExamples;

    public TextTemplate() {
        super.setTemplateType(TemplateType.TEXT);
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<String> getVariableHeaderExamples() {
        return variableHeaderExamples;
    }

    public void setVariableHeaderExamples(List<String> variableHeaderExamples) {
        this.variableHeaderExamples = variableHeaderExamples;
    }
}
