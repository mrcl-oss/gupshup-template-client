package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.button.CopyCodeButton;
import java.util.Collections;

public class AuthenticationTemplate extends TextTemplate {
    private boolean addSecurityRecommendation;
    private int codeExpirationMinutes;

    public AuthenticationTemplate(String elementName, io.github.mrcloss.gupshup.domain.enums.LanguageCode languageCode, String body, String appId, java.util.List<String> tags, io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat parameterFormat) {
        super(elementName, languageCode, body, TemplateCategory.AUTHENTICATION, appId, tags, parameterFormat);
        updateButton();
    }

    public boolean isAddSecurityRecommendation() {
        return addSecurityRecommendation;
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

    public int getCodeExpirationMinutes() {
        return codeExpirationMinutes;
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
    public void setButtons(java.util.List<io.github.mrcloss.gupshup.domain.button.Button> buttons) {
        throw new UnsupportedOperationException("Authentication template buttons cannot be modified");
    }

    @Override
    public void addButton(io.github.mrcloss.gupshup.domain.button.Button button) {
        throw new UnsupportedOperationException("Authentication template buttons cannot be modified");
    }
}
