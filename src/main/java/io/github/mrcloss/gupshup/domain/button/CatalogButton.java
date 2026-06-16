package io.github.mrcloss.gupshup.domain.button;

import io.github.mrcloss.gupshup.domain.enums.ButtonType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CatalogButton extends Button {

  public CatalogButton() {
    super.setType(ButtonType.CATALOG);
  }

  public CatalogButton(String text) {
    super(ButtonType.CATALOG, text);
  }

  @Override
  public void validate() {
    validateText(getText());
  }
}
