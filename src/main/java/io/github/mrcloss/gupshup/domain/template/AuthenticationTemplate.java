package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.button.Button;
import io.github.mrcloss.gupshup.domain.button.CopyCodeButton;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class AuthenticationTemplate extends TextTemplate {
    @Setter(AccessLevel.NONE)
    private boolean addSecurityRecommendation;
    @Setter(AccessLevel.NONE)
    private int codeExpirationMinutes;

    public AuthenticationTemplate(String elementName, LanguageCode languageCode, String body, String appId, List<String> tags, TemplateParameterFormat parameterFormat) {
        this(elementName, languageCode, body, null, appId, tags, parameterFormat);
    }

    public AuthenticationTemplate(String elementName, LanguageCode languageCode, String body, List<String> variableExamples, String appId, List<String> tags, TemplateParameterFormat parameterFormat) {
        super(elementName, languageCode, body, variableExamples, TemplateCategory.AUTHENTICATION, appId, tags, parameterFormat);
        updateButton();
    }

    public void setAddSecurityRecommendation(boolean addSecurityRecommendation) {
        if (this.addSecurityRecommendation != addSecurityRecommendation) {
            this.addSecurityRecommendation = addSecurityRecommendation;
            updateBodyWithRecommendation();
        }
    }

    @Override
    public void setBody(String body) {
        super.setBody(body);
        updateBodyWithRecommendation();
    }

    private void updateBodyWithRecommendation() {
        String currentBody = super.getBody();
        if (currentBody == null) {
            return;
        }

        String recommendation = " For your security, do not share this code.";
        if (addSecurityRecommendation) {
            if (!currentBody.endsWith(recommendation)) {
                super.setBody(currentBody + recommendation);
            }
        } else {
            if (currentBody.endsWith(recommendation)) {
                super.setBody(currentBody.substring(0, currentBody.length() - recommendation.length()));
            }
        }
    }

    public void setCodeExpirationMinutes(int codeExpirationMinutes) {
        if (codeExpirationMinutes < 0) {
            throw new IllegalArgumentException("Code expiration minutes cannot be negative");
        }
        this.codeExpirationMinutes = codeExpirationMinutes;
        updateButton();
    }

    private void updateButton() {
        String text = (codeExpirationMinutes > 0) 
            ? "This code expires in {{codeExpirationMinutes}} minutes" 
            : "Copy code";
        
        // We bypass the restriction by calling super
        super.setButtons(Collections.singletonList(new CopyCodeButton(text, "123456")));
    }

    @Override
    public void setCategory(TemplateCategory category) {
        if (category != TemplateCategory.AUTHENTICATION) {
            throw new IllegalArgumentException("Authentication template only allowed for AUTHENTICATION category");
        }
        super.setCategory(category);
    }

    @Override
    public void setButtons(List<Button> buttons) {
        throw new UnsupportedOperationException("Authentication template buttons cannot be modified");
    }

    @Override
    public void addButton(Button button) {
        throw new UnsupportedOperationException("Authentication template buttons cannot be modified");
    }
}
