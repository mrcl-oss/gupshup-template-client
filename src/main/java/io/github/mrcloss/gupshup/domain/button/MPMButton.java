package io.github.mrcloss.gupshup.domain.button;

import io.github.mrcloss.gupshup.domain.enums.ButtonType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MPMButton extends Button {

  public MPMButton() {
    super.setType(ButtonType.MPM);
  }

  public MPMButton(String text) {
    super(ButtonType.MPM, text);
  }

  @Override
  public void validate() {
    validateText(getText());
  }
}
