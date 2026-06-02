package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;

public class AuthenticationTemplate extends TextTemplate {
    private boolean addSecurityRecommendation;
    private int codeExpirationMinutes;

    public AuthenticationTemplate() {
        super();
        super.setCategory(TemplateCategory.AUTHENTICATION);
    }

    public boolean isAddSecurityRecommendation() {
        return addSecurityRecommendation;
    }

    public void setAddSecurityRecommendation(boolean addSecurityRecommendation) {
        this.addSecurityRecommendation = addSecurityRecommendation;
    }

    public int getCodeExpirationMinutes() {
        return codeExpirationMinutes;
    }

    public void setCodeExpirationMinutes(int codeExpirationMinutes) {
        if (codeExpirationMinutes < 0) {
            throw new IllegalArgumentException("Code expiration minutes cannot be negative");
        }
        this.codeExpirationMinutes = codeExpirationMinutes;
    }
}
