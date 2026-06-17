package io.github.mrcloss.gupshup.domain.template;

import io.github.mrcloss.gupshup.domain.button.Button;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a WhatsApp Carousel template.
 *
 * <p>Carousel templates contain a list of {@link CarouselCard}s (minimum 2, maximum 10) which can
 * be scrolled horizontally by users. Each card features its own body, variables, media headers, and
 * up to 2 buttons.
 */
@Getter
@Setter
public class CarouselTemplate extends Template {
  @Setter(AccessLevel.NONE)
  private ArrayList<CarouselCard> cards;

  /**
   * Constructs a new CarouselTemplate without variable examples.
   *
   * @param elementName the unique name of the template (alphanumeric, lowercase)
   * @param languageCode the language of the template
   * @param body the template message body text
   * @param category the category of the template
   * @param appId the Gupshup app ID
   * @param tags optional list of tag labels for the template
   * @param parameterFormat the format of parameters
   */
  public CarouselTemplate(
      String elementName,
      LanguageCode languageCode,
      String body,
      TemplateCategory category,
      String appId,
      List<String> tags,
      TemplateParameterFormat parameterFormat) {
    this(elementName, languageCode, body, null, category, appId, tags, parameterFormat);
  }

  public CarouselTemplate(
      String elementName,
      LanguageCode languageCode,
      String body,
      List<String> variableExamples,
      TemplateCategory category,
      String appId,
      List<String> tags,
      TemplateParameterFormat parameterFormat) {
    super(
        elementName,
        languageCode,
        body,
        variableExamples,
        category,
        appId,
        tags,
        TemplateType.CAROUSEL,
        parameterFormat);
  }

  public void setCards(ArrayList<CarouselCard> cards) {
    if (cards != null && (cards.size() < 1 || cards.size() > 10)) {
      throw new IllegalArgumentException("Carousel must have between 1 and 10 cards");
    }
    this.cards = cards;
  }

  @Override
  public void setButtons(List<Button> buttons) {
    if (buttons != null && !buttons.isEmpty()) {
      throw new IllegalArgumentException(
          "Carousel template itself cannot have buttons, only the cards");
    }
    super.setButtons(buttons);
  }

  @Override
  public void addButton(Button button) {
    throw new IllegalStateException("Carousel template itself cannot have buttons, only the cards");
  }

  @Override
  public void validate() {
    super.validate();
    if (cards == null || cards.isEmpty()) {
      throw new IllegalStateException("Carousel template must have cards");
    }

    int buttonCount = cards.get(0).getButtons() != null ? cards.get(0).getButtons().size() : 0;
    CarouselCard.CarouselCardHeaderType headerType = cards.get(0).getHeaderType();

    for (CarouselCard card : cards) {
      card.validate();
      if ((card.getButtons() != null ? card.getButtons().size() : 0) != buttonCount) {
        throw new IllegalStateException(
            "All cards in a carousel must have the same number of buttons");
      }
      if (card.getHeaderType() != headerType) {
        throw new IllegalStateException("All cards in a carousel must have the same media type");
      }
    }
  }
}
