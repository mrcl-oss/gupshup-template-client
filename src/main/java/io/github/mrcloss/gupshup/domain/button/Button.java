package io.github.mrcloss.gupshup.domain.button;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.mrcloss.gupshup.domain.enums.ButtonType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonDeserialize(using = ButtonDeserializer.class)
public abstract class Button {
  private ButtonType type;
  private String text;

  public abstract void validate();

  /** Basic validation for text length or content if needed. */
  protected void validateText(String text) {
    if (text == null || text.trim().isEmpty()) {
      throw new IllegalArgumentException("Button text cannot be null or empty");
    }
  }
}
