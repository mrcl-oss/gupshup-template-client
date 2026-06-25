package io.github.mrcloss.gupshup.infrastructure.dto.client;

import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import io.github.mrcloss.gupshup.domain.template.CarouselCard;
import io.github.mrcloss.gupshup.domain.template.CarouselTemplate;
import java.util.ArrayList;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CarouselTemplateDto extends BaseTemplateDto {
  private ArrayList<CarouselCard> cards;

  /**
   * Default constructor. Explicitly sets the template type to maintain consistency during
   * programmatic instantiation.
   */
  public CarouselTemplateDto() {
    super();
    this.setTemplateType(TemplateType.CAROUSEL);
  }

  @Override
  public CarouselTemplate toDomain() {
    CarouselTemplate template =
        new CarouselTemplate(
            getElementName(),
            getLanguageCode(),
            getBody(),
            getVariableExamples(),
            getCategory(),
            getAppId(),
            getTags(),
            getParameterFormat());
    template.setFooter(getFooter());
    template.setMessageValidity(getMessageValidity());
    if (cards != null) {
      template.setCards(cards);
    }
    return template;
  }
}
