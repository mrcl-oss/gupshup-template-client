package io.github.mrcloss.gupshup.domain.template;

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.mrcloss.gupshup.domain.button.CopyCodeButton;
import io.github.mrcloss.gupshup.domain.button.QuickReplyButton;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CarouselValidationTest {

  @Test
  public void carouselTemplateShouldNotHaveButtons() {
    CarouselTemplate template = createValidTemplate();
    assertThrows(
        IllegalStateException.class,
        () -> {
          template.addButton(new QuickReplyButton("Reply"));
        },
        "Carousel template itself cannot have buttons");
  }

  @Test
  public void carouselCardMustHaveOneOrTwoButtons() {
    CarouselCard card = new CarouselCard();
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          card.setButtons(new ArrayList<>());
        },
        "Carousel card must have at least 1 button");
  }

  @Test
  public void carouselCardOnlyAcceptsSpecificButtons() {
    CarouselCard card = new CarouselCard();
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          card.setButtons(List.of(new CopyCodeButton("Code", "VAL")));
        },
        "Carousel cards only accept QuickReply, PhoneNumber and URL Buttons");
  }

  @Test
  public void carouselCardsMustHaveSameNumberOfButtons() {
    CarouselTemplate template = createValidTemplate();

    CarouselCard card1 = createValidCard();
    card1.setButtons(List.of(new QuickReplyButton("B1")));

    CarouselCard card2 = createValidCard();
    card2.setButtons(List.of(new QuickReplyButton("B1"), new QuickReplyButton("B2")));

    ArrayList<CarouselCard> cards = new ArrayList<>();
    cards.add(card1);
    cards.add(card2);
    template.setCards(cards);

    assertThrows(
        IllegalStateException.class,
        () -> {
          template.validate();
        },
        "All cards in a carousel must have the same number of buttons");
  }

  @Test
  public void carouselCardsMustHaveSameMediaType() {
    CarouselTemplate template = createValidTemplate();

    CarouselCard card1 = createValidCard();
    card1.setHeaderType(CarouselCard.CarouselCardHeaderType.IMAGE);

    CarouselCard card2 = createValidCard();
    card2.setHeaderType(CarouselCard.CarouselCardHeaderType.VIDEO);

    ArrayList<CarouselCard> cards = new ArrayList<>();
    cards.add(card1);
    cards.add(card2);
    template.setCards(cards);

    assertThrows(
        IllegalStateException.class,
        () -> {
          template.validate();
        },
        "All cards in a carousel must have the same media type");
  }

  @Test
  public void carouselTemplateShouldNotExceedTenCards() {
    CarouselTemplate template = createValidTemplate();
    ArrayList<CarouselCard> cards = new ArrayList<>();
    for (int i = 0; i < 11; i++) {
      cards.add(createValidCard());
    }
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          template.setCards(cards);
        },
        "Carousel must have at most 10 cards");
  }

  @Test
  public void carouselCardShouldRequirePlaceholdersIfVariableExamplesArePresent() {
    CarouselCard card = createValidCard();
    card.setBody("Hello {{1}}!");
    card.setVariableExamples(java.util.Arrays.asList("John", "Doe"));

    assertThrows(
        IllegalStateException.class, card::validate, "Should fail because {{2}} is missing");

    card.setBody("Hello {{1}} and {{2}}!");
    card.validate(); // Should pass
  }

  private CarouselTemplate createValidTemplate() {
    return new CarouselTemplate(
        "test_carousel",
        LanguageCode.ENGLISH,
        "Carousel body",
        TemplateCategory.MARKETING,
        "app-123",
        null,
        TemplateParameterFormat.POSITIONAL);
  }

  private CarouselCard createValidCard() {
    CarouselCard card = new CarouselCard();
    card.setBody("Card body");
    card.setHeaderType(CarouselCard.CarouselCardHeaderType.IMAGE);
    card.setButtons(List.of(new QuickReplyButton("B1")));
    return card;
  }
}
