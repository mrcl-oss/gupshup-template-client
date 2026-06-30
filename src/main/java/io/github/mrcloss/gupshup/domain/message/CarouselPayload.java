package io.github.mrcloss.gupshup.domain.message;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class CarouselPayload extends GupshupMessage {

  private final CardHeaderType cardHeaderType;

  private final List<Card> cards;

  public enum CardHeaderType {
    IMAGE,
    VIDEO;
  }

  public CarouselPayload(CardHeaderType cardHeaderType, List<String> links) {
    super(MessageType.CAROUSEL);
    this.cardHeaderType = cardHeaderType;

    this.cards = links.stream().map(Card::new).collect(Collectors.toList());
  }

  @Getter
  @ToString
  public static class Card {
    private final String link;

    public Card(String link) {
      this.link = link;
    }
  }
}
