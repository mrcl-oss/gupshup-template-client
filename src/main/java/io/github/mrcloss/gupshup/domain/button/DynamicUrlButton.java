package io.github.mrcloss.gupshup.domain.button;

public class DynamicUrlButton extends UrlButton {
    private String urlTemplate;
    private String variableExample;

    public DynamicUrlButton() {
        super();
    }

    public DynamicUrlButton(String text, String urlTemplate, String variableExample) {
        super(text);
        setUrlTemplate(urlTemplate);
        setVariableExample(variableExample);
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public void setUrlTemplate(String urlTemplate) {
        validateUrl(urlTemplate);
        if (!urlTemplate.contains("{{1}}")) {
            throw new IllegalArgumentException("Dynamic URL template must contain '{{1}}' placeholder");
        }
        this.urlTemplate = urlTemplate;
    }

    public String getVariableExample() {
        return variableExample;
    }

    public void setVariableExample(String variableExample) {
        if (variableExample == null || variableExample.trim().isEmpty()) {
            throw new IllegalArgumentException("Variable example cannot be null or empty for dynamic URL");
        }
        this.variableExample = variableExample;
    }

    @Override
    public void validate() {
        validateText(getText());
        if (urlTemplate == null || urlTemplate.trim().isEmpty()) {
            throw new IllegalStateException("URL template is required for dynamic URL button");
        }
        if (!urlTemplate.contains("{{1}}")) {
            throw new IllegalStateException("Dynamic URL template must contain '{{1}}' placeholder");
        }
        if (variableExample == null || variableExample.trim().isEmpty()) {
            throw new IllegalStateException("Variable example is required for dynamic URL button");
        }
    }
}
