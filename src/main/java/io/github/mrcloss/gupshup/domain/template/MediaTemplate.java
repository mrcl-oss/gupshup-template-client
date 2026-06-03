package io.github.mrcloss.gupshup.domain.template;

public abstract class MediaTemplate extends Template {
    private String mediaId;
    private String mediaUrl;

    public MediaTemplate(String elementName, io.github.mrcloss.gupshup.domain.enums.LanguageCode languageCode, String body, io.github.mrcloss.gupshup.domain.enums.TemplateCategory category, String appId, java.util.List<String> tags, io.github.mrcloss.gupshup.domain.enums.TemplateType templateType, io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat parameterFormat) {
        super(elementName, languageCode, body, category, appId, tags, templateType, parameterFormat);
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public abstract String[] getAllowedExtensions();

    public void setMediaUrl(String mediaUrl) {
        if (mediaUrl != null) {
            if (!mediaUrl.startsWith("http://") && !mediaUrl.startsWith("https://")) {
                throw new IllegalArgumentException("Media URL must start with http:// or https://");
            }
            
            // Strict URL format check (simplified for this context but ensuring it looks like a URL with a path)
            if (!mediaUrl.matches("^https?://[\\w\\.-]+(?:\\.[\\w\\.-]+)+[/\\w\\.-]*/?$")) {
                throw new IllegalArgumentException("Invalid Media URL format");
            }

            String[] allowedExtensions = getAllowedExtensions();
            boolean validExtension = false;
            String lowerUrl = mediaUrl.toLowerCase();
            for (String ext : allowedExtensions) {
                if (lowerUrl.endsWith(ext.toLowerCase())) {
                    validExtension = true;
                    break;
                }
            }

            if (!validExtension) {
                throw new IllegalArgumentException("Media URL extension not allowed for this template type. Allowed: " + String.join(", ", allowedExtensions));
            }
        }
        this.mediaUrl = mediaUrl;
    }
}
