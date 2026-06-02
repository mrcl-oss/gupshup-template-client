package io.github.mrcloss.gupshup.domain.button;

import io.github.mrcloss.gupshup.domain.enums.ButtonType;

public class CatalogButton extends Button {
    
    public CatalogButton() {
        super.setType(ButtonType.CATALOG);
    }

    public CatalogButton(String text) {
        super(ButtonType.CATALOG, text);
    }
}
