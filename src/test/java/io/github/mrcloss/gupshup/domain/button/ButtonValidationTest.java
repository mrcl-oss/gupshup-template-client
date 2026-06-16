package io.github.mrcloss.gupshup.domain.button;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ButtonValidationTest {

  @Test
  public void copyCodeButtonShouldNotAllowSpaces() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new CopyCodeButton("Copy", "CODE 123");
        },
        "Copy code button should not allow spaces");
  }

  @Test
  public void copyCodeButtonShouldNotAllowSpecialCharacters() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new CopyCodeButton("Copy", "CODE!");
        },
        "Copy code button should not allow special characters");
  }

  @Test
  public void dynamicUrlButtonShouldWorkWithStrictValidation() {
    new DynamicUrlButton("Visit", "https://example.com/{{1}}", "user123");
    // Should not throw
  }

  @Test
  public void dynamicUrlButtonShouldFailWithInvalidBaseUrl() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new DynamicUrlButton("Visit", "invalid-url/{{1}}", "user123");
        },
        "Dynamic URL should follow strict structure for base URL");
  }
}
