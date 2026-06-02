package io.github.mrcloss.gupshup.domain.template;

public abstract class MediaTemplate extends Template {
    private String mediaId;
    private String mediaUrl;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        if (mediaUrl != null && !mediaUrl.startsWith("http://") && !mediaUrl.startsWith("https://")) {
            throw new IllegalArgumentException("Media URL must start with http:// or https://");
        }
        this.mediaUrl = mediaUrl;
    }
}
