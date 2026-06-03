package io.github.mrcloss.gupshup.domain.template;

import java.util.ArrayList;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;

public class CarouselTemplate extends Template {
    private ArrayList<CarouselCard> cards;

    public CarouselTemplate(String elementName, io.github.mrcloss.gupshup.domain.enums.LanguageCode languageCode, String body, io.github.mrcloss.gupshup.domain.enums.TemplateCategory category, String appId, java.util.List<String> tags, io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat parameterFormat) {
        super(elementName, languageCode, body, category, appId, tags, TemplateType.CAROUSEL, parameterFormat);
    }

    public ArrayList<CarouselCard> getCards() {
        return cards;
    }

    public void setCards(ArrayList<CarouselCard> cards) {
        if (cards != null && (cards.size() < 1 || cards.size() > 10)) {
            throw new IllegalArgumentException("Carousel must have between 1 and 10 cards");
        }
        this.cards = cards;
    }

    @Override
    public void setButtons(java.util.List<io.github.mrcloss.gupshup.domain.button.Button> buttons) {
        if (buttons != null && !buttons.isEmpty()) {
            throw new IllegalArgumentException("Carousel template itself cannot have buttons, only the cards");
        }
        super.setButtons(buttons);
    }

    @Override
    public void addButton(io.github.mrcloss.gupshup.domain.button.Button button) {
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
                throw new IllegalStateException("All cards in a carousel must have the same number of buttons");
            }
            if (card.getHeaderType() != headerType) {
                throw new IllegalStateException("All cards in a carousel must have the same media type");
            }
        }
    }
}
