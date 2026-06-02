package io.github.mrcloss.gupshup.domain.template;

import java.util.ArrayList;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;

public class CarouselTemplate extends Template {
    private ArrayList<CarouselCard> cards;

    public CarouselTemplate() {
        super.setTemplateType(TemplateType.CAROUSEL);
    }

    public ArrayList<CarouselCard> getCards() {
        return cards;
    }

    public void setCards(ArrayList<CarouselCard> cards) {
        if (cards != null && (cards.size() < 2 || cards.size() > 10)) {
            throw new IllegalArgumentException("Carousel must have between 2 and 10 cards");
        }
        this.cards = cards;
    }
}
