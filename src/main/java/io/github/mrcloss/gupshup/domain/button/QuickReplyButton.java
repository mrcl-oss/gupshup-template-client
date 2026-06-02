package io.github.mrcloss.gupshup.domain.button;

import io.github.mrcloss.gupshup.domain.enums.ButtonType;

public class QuickReplyButton extends Button {
    
    public QuickReplyButton() {
        super.setType(ButtonType.QUICK_REPLY);
    }

    public QuickReplyButton(String text) {
        super(ButtonType.QUICK_REPLY, text);
    }
}
