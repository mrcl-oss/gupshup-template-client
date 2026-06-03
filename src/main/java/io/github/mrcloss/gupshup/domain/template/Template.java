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

    public Template(String elementName, LanguageCode languageCode, String body, TemplateCategory category, String appId, List<String> tags, TemplateType templateType, TemplateParameterFormat parameterFormat) {
        setElementName(elementName);
        setLanguageCode(languageCode);
        setBody(body);
        setCategory(category);
        setAppId(appId);
        setTags(tags);
        setTemplateType(templateType);
        setParameterFormat(parameterFormat);
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
        if (body != null) {
            if (body.length() > 1024) {
                throw new IllegalArgumentException("Template body cannot exceed 1024 characters");
            }
            if (body.matches("^\\{\\{\\d+\\}\\}.*")) {
                throw new IllegalArgumentException("Template body cannot start with a variable");
            }
            if (body.matches(".*\\{\\{\\d+\\}\\}\\s*$")) {
                throw new IllegalArgumentException("Template body cannot end with a variable");
            }
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
        if (buttons != null) {
            if (buttons.size() > 10) {
                throw new IllegalArgumentException("A template can have at most 10 buttons");
            }
            validateButtons(buttons);
        }
        this.buttons = buttons;
    }

    public void addButton(Button button) {
        if (this.buttons.size() >= 10) {
            throw new IllegalStateException("A template can have at most 10 buttons");
        }
        List<Button> newButtons = new ArrayList<>(this.buttons);
        newButtons.add(button);
        validateButtons(newButtons);
        this.buttons.add(button);
    }

    private void validateButtons(List<Button> buttons) {
        long urlButtonsCount = buttons.stream()
                .filter(b -> b.getType() == io.github.mrcloss.gupshup.domain.enums.ButtonType.URL)
                .count();
        if (urlButtonsCount > 2) {
            throw new IllegalArgumentException("A template can have at most 2 URL buttons");
        }

        long phoneButtonsCount = buttons.stream()
                .filter(b -> b.getType() == io.github.mrcloss.gupshup.domain.enums.ButtonType.PHONE_NUMBER)
                .count();
        if (phoneButtonsCount > 1) {
            throw new IllegalArgumentException("A template can have at most 1 Phone Number button");
        }

        long payNowButtonsCount = buttons.stream()
                .filter(b -> b instanceof io.github.mrcloss.gupshup.domain.button.PayNowButton)
                .count();
        if (payNowButtonsCount > 1) {
            throw new IllegalArgumentException("A template can have at most 1 Pay Now button");
        }

        for (Button button : buttons) {
            button.validate();
            if (button instanceof io.github.mrcloss.gupshup.domain.button.OTPButton && !(this instanceof AuthenticationTemplate)) {
                throw new IllegalStateException("OTPButton is only allowed in AuthenticationTemplates");
            }
            if (button instanceof io.github.mrcloss.gupshup.domain.button.MPMButton && !(this instanceof ProductTemplate)) {
                throw new IllegalStateException("MPMButton is only allowed in ProductTemplates");
            }
            if (button instanceof io.github.mrcloss.gupshup.domain.button.CatalogButton && !(this instanceof CatalogTemplate)) {
                throw new IllegalStateException("CatalogButton is only allowed in CatalogTemplates");
            }
        }

        if (variableExamples != null && !variableExamples.isEmpty()) {
            if (body == null) {
                throw new IllegalStateException("Body is required when variable examples are provided");
            }
            for (int i = 1; i <= variableExamples.size(); i++) {
                String placeholder = "{{" + i + "}}";
                if (!body.contains(placeholder)) {
                    throw new IllegalStateException("Body must contain " + placeholder + " when " + variableExamples.size() + " variable examples are provided");
                }
            }
        }
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
        if (body != null) {
            if (body.matches("^\\{\\{\\d+\\}\\}.*")) {
                throw new IllegalStateException("Template body cannot start with a variable");
            }
            if (body.matches(".*\\{\\{\\d+\\}\\}\\s*$")) {
                throw new IllegalStateException("Template body cannot end with a variable");
            }
        }
        validateButtons(buttons);

        boolean hasPayNow = buttons.stream().anyMatch(b -> b instanceof io.github.mrcloss.gupshup.domain.button.PayNowButton);
        if (hasPayNow) {
            if (templateType != io.github.mrcloss.gupshup.domain.enums.TemplateType.TEXT) {
                throw new IllegalStateException("Pay Now button is only allowed in TEXT templates");
            }
        }

        if (category == io.github.mrcloss.gupshup.domain.enums.TemplateCategory.UTILITY) {
            if (templateType == io.github.mrcloss.gupshup.domain.enums.TemplateType.GIF ||
                templateType == io.github.mrcloss.gupshup.domain.enums.TemplateType.CATALOG ||
                templateType == io.github.mrcloss.gupshup.domain.enums.TemplateType.PRODUCT ||
                templateType == io.github.mrcloss.gupshup.domain.enums.TemplateType.CAROUSEL) {
                throw new IllegalStateException("UTILITY category templates cannot be of type GIF, CATALOG, PRODUCT, or CAROUSEL");
            }
        }

        if (ltoAttributes != null) {
            if (templateType != io.github.mrcloss.gupshup.domain.enums.TemplateType.TEXT &&
                templateType != io.github.mrcloss.gupshup.domain.enums.TemplateType.IMAGE &&
                templateType != io.github.mrcloss.gupshup.domain.enums.TemplateType.VIDEO) {
                throw new IllegalStateException("LTO templates are only allowed for TEXT, IMAGE, or VIDEO types");
            }
            boolean hasUrlButton = buttons.stream().anyMatch(b -> b.getType() == io.github.mrcloss.gupshup.domain.enums.ButtonType.URL);
            if (!hasUrlButton) {
                throw new IllegalStateException("LTO templates must have at least one URL button");
            }
            if (ltoAttributes.isHasExpiration()) {
                boolean hasCopyCodeButton = buttons.stream().anyMatch(b -> b.getType() == io.github.mrcloss.gupshup.domain.enums.ButtonType.COPY_CODE);
                if (!hasCopyCodeButton) {
                    throw new IllegalStateException("LTO templates with expiration must have at least one COPY_CODE button");
                }
            }
        }
    }
}
