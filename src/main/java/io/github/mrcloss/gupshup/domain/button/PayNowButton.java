package io.github.mrcloss.gupshup.domain.button;

import io.github.mrcloss.gupshup.domain.enums.ButtonType;

public class PayNowButton extends UrlButton {
    private boolean paymentLinkPreview;
    private UrlButton underlyingUrlButton;

    public PayNowButton() {
        super.setType(ButtonType.URL); // PayNow is technically a URL button in many implementations
    }

    public PayNowButton(String text, boolean paymentLinkPreview, UrlButton underlyingUrlButton) {
        super(text);
        this.paymentLinkPreview = paymentLinkPreview;
        this.underlyingUrlButton = underlyingUrlButton;
    }

    public boolean isPaymentLinkPreview() {
        return paymentLinkPreview;
    }

    public void setPaymentLinkPreview(boolean paymentLinkPreview) {
        this.paymentLinkPreview = paymentLinkPreview;
    }

    public UrlButton getUnderlyingUrlButton() {
        return underlyingUrlButton;
    }

    public void setUnderlyingUrlButton(UrlButton underlyingUrlButton) {
        this.underlyingUrlButton = underlyingUrlButton;
    }
}
