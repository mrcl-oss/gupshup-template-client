package io.github.mrcloss.gupshup.domain.template;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import io.github.mrcloss.gupshup.domain.button.Button;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateStatus;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;

public class Template {
    private String appId;
    private TemplateStatus status;
    private TemplateCategory category;
    private Instant createdOn;
    private Instant modifiedOn;
    private String body; // data / content
    private List<String> variableExamples;
    private String elementName;
    private LanguageCode languageCode;
    private TemplateParameterFormat parameterFormat;
    private TemplateType templateType;
    private List<String> tags; // vertical
    private String reason;
    private String footer;
    private int messageValidity;
    private List<Button> buttons = new ArrayList<>();

    @JsonUnwrapped
    private LTOAttributes ltoAttributes;

    public Template() {
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public TemplateStatus getStatus() {
        return status;
    }

    public void setStatus(TemplateStatus status) {
        this.status = status;
    }

    public TemplateCategory getCategory() {
        return category;
    }

    public void setCategory(TemplateCategory category) {
        this.category = category;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Instant modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        if (body != null && body.length() > 1024) {
            throw new IllegalArgumentException("Template body cannot exceed 1024 characters");
        }
        this.body = body;
    }

    public List<String> getVariableExamples() {
        return variableExamples;
    }

    public void setVariableExamples(List<String> variableExamples) {
        this.variableExamples = variableExamples;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public LanguageCode getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(LanguageCode languageCode) {
        this.languageCode = languageCode;
    }

    public TemplateParameterFormat getParameterFormat() {
        return parameterFormat;
    }

    public void setParameterFormat(TemplateParameterFormat parameterFormat) {
        this.parameterFormat = parameterFormat;
    }

    public TemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        if (footer != null && footer.length() > 60) {
            throw new IllegalArgumentException("Template footer cannot exceed 60 characters");
        }
        this.footer = footer;
    }

    public int getMessageValidity() {
        return messageValidity;
    }

    public void setMessageValidity(int messageValidity) {
        this.messageValidity = messageValidity;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        if (buttons != null && buttons.size() > 10) {
            throw new IllegalArgumentException("A template can have at most 10 buttons");
        }
        this.buttons = buttons;
    }

    public void addButton(Button button) {
        if (this.buttons.size() >= 10) {
            throw new IllegalStateException("A template can have at most 10 buttons");
        }
        this.buttons.add(button);
    }

    public LTOAttributes getLtoAttributes() {
        return ltoAttributes;
    }

    public void setLtoAttributes(LTOAttributes ltoAttributes) {
        this.ltoAttributes = ltoAttributes;
    }

    /**
     * Validates that the template has the minimum required fields for submission.
     */
    public void validate() {
        if (elementName == null || elementName.trim().isEmpty()) {
            throw new IllegalStateException("Element name is required");
        }
        if (languageCode == null) {
            throw new IllegalStateException("Language code is required");
        }
        if (category == null) {
            throw new IllegalStateException("Category is required");
        }
    }
}
