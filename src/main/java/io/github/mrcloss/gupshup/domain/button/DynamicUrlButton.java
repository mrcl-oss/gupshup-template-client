package io.github.mrcloss.gupshup.domain.button;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class DynamicUrlButton extends UrlButton {
  @Setter(AccessLevel.NONE)
  private String urlTemplate;

  @Setter(AccessLevel.NONE)
  private String variableExample;

  public DynamicUrlButton() {
    super();
  }

  public DynamicUrlButton(String text, String urlTemplate, String variableExample) {
    super(text);
    setUrlTemplate(urlTemplate);
    setVariableExample(variableExample);
  }

  public void setUrlTemplate(String urlTemplate) {
    validateUrl(urlTemplate);
    if (!urlTemplate.contains("{{1}}")) {
      throw new IllegalArgumentException("Dynamic URL template must contain '{{1}}' placeholder");
    }
    this.urlTemplate = urlTemplate;
    if (this.variableExample != null) {
      this.variableExample = extractVariableValue(urlTemplate, this.variableExample);
    }
  }

  public void setVariableExample(String variableExample) {
    if (variableExample == null || variableExample.trim().isEmpty()) {
      throw new IllegalArgumentException(
          "Variable example cannot be null or empty for dynamic URL");
    }
    this.variableExample = extractVariableValue(this.urlTemplate, variableExample);
  }

  /**
   * Returns the URL template with the variable placeholder replaced by the example value.
   *
   * @return the replaced URL
   */
  public String getReplacedUrl() {
    if (urlTemplate == null) {
      return null;
    }
    if (variableExample == null) {
      return urlTemplate;
    }
    int index = urlTemplate.indexOf("{{1}}");
    if (index != -1) {
      String prefix = urlTemplate.substring(0, index);
      String suffix = urlTemplate.substring(index + 5);
      if (prefix.matches(".*[a-zA-Z0-9.-]$")
          && !prefix.contains("?")
          && !prefix.contains("&")
          && !prefix.contains("=")
          && !prefix.contains("#")
          && !variableExample.startsWith("/")) {
        return prefix + "/" + variableExample + suffix;
      }
    }
    return urlTemplate.replace("{{1}}", variableExample);
  }

  private String extractVariableValue(String template, String example) {
    if (template == null || !template.contains("{{1}}") || example == null) {
      return example;
    }
    int index = template.indexOf("{{1}}");
    String prefix = template.substring(0, index);
    String suffix = template.substring(index + 5);
    if (example.startsWith(prefix)
        && (suffix.isEmpty() || example.endsWith(suffix))
        && example.length() >= prefix.length() + suffix.length()) {
      String extracted = example.substring(prefix.length(), example.length() - suffix.length());
      if (extracted.startsWith("/") && !prefix.endsWith("/")) {
        extracted = extracted.substring(1);
      }
      return extracted;
    }
    return example;
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
