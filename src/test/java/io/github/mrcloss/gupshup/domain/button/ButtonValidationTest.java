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

  @Test
  public void testDynamicUrlButtonVariableExtraction() {
    // Test case 1: normal template with slash, full url example
    DynamicUrlButton btn1 =
        new DynamicUrlButton("Visit", "https://example.com/{{1}}", "https://example.com/hola");
    org.junit.jupiter.api.Assertions.assertEquals("hola", btn1.getVariableExample());
    org.junit.jupiter.api.Assertions.assertEquals(
        "https://example.com/hola", btn1.getReplacedUrl());

    // Test case 2: template without slash, full url example
    DynamicUrlButton btn2 =
        new DynamicUrlButton("Visit", "https://example.com{{1}}", "https://example.com/hola");
    org.junit.jupiter.api.Assertions.assertEquals("hola", btn2.getVariableExample());
    org.junit.jupiter.api.Assertions.assertEquals(
        "https://example.com/hola", btn2.getReplacedUrl());

    // Test case 3: nested path template, full url example
    DynamicUrlButton btn3 =
        new DynamicUrlButton(
            "Visit", "https://example.com/products/{{1}}", "https://example.com/products/123");
    org.junit.jupiter.api.Assertions.assertEquals("123", btn3.getVariableExample());
    org.junit.jupiter.api.Assertions.assertEquals(
        "https://example.com/products/123", btn3.getReplacedUrl());

    // Test case 4: query parameter template, query parameter example
    DynamicUrlButton btn4 =
        new DynamicUrlButton(
            "Visit", "https://example.com/search?q={{1}}", "https://example.com/search?q=hola");
    org.junit.jupiter.api.Assertions.assertEquals("hola", btn4.getVariableExample());
    org.junit.jupiter.api.Assertions.assertEquals(
        "https://example.com/search?q=hola", btn4.getReplacedUrl());

    // Test case 5: normal template with slash, simple value example
    DynamicUrlButton btn5 = new DynamicUrlButton("Visit", "https://example.com/{{1}}", "hola");
    org.junit.jupiter.api.Assertions.assertEquals("hola", btn5.getVariableExample());
    org.junit.jupiter.api.Assertions.assertEquals(
        "https://example.com/hola", btn5.getReplacedUrl());
  }
}
