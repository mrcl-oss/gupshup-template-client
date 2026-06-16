package io.github.mrcloss.gupshup.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/**
 * Value Object representing a Language Code (e.g., "es", "en", "cat"). Designed as an extensible
 * Pseudo-Enum to allow standard auto-completion while supporting custom or future language codes
 * without requiring library updates.
 */
public final class LanguageCode {

  public static final LanguageCode SPANISH = new LanguageCode("es");
  public static final LanguageCode SPANISH_ES = new LanguageCode("es_ES");
  public static final LanguageCode ENGLISH = new LanguageCode("en");
  public static final LanguageCode CATALAN = new LanguageCode("cat");
  public static final LanguageCode PORTUGUESE_BR = new LanguageCode("pt_BR");

  private final String code;

  /**
   * Private constructor to enforce the use of the Factory Method.
   *
   * @param code The internal language code string.
   */
  private LanguageCode(String code) {
    this.code = code;
  }

  /**
   * Factory Method. The @JsonCreator annotation instructs Jackson (and frameworks like Spring Data
   * MongoDB) to use this method when converting a String from a JSON payload or database into this
   * object.
   *
   * @param code The language code as text (e.g., "es", "cat").
   * @return A safe, instantiated LanguageCode object.
   * @throws IllegalArgumentException if the provided code is null or empty.
   */
  @JsonCreator
  public static LanguageCode of(String code) {
    if (code == null || code.trim().isEmpty()) {
      throw new IllegalArgumentException("Language code cannot be null or empty.");
    }

    return new LanguageCode(code.trim().toLowerCase());
  }

  /**
   * The @JsonValue annotation instructs Jackson that when serializing to Mongo or sending via HTTP,
   * it should ONLY output this raw String value, rather than wrapping it in a complex JSON object.
   *
   * @return The underlying language code string.
   */
  @JsonValue
  public String getCode() {
    return code;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LanguageCode that = (LanguageCode) o;
    return code.equals(that.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code);
  }

  @Override
  public String toString() {
    return code;
  }
}
