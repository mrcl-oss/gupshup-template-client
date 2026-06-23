package io.github.mrcloss.gupshup.infrastructure.dto.client;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.mrcloss.gupshup.domain.button.Button;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.enums.TemplateType;
import io.github.mrcloss.gupshup.domain.template.LTOAttributes;
import io.github.mrcloss.gupshup.domain.template.Template;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "templateType",
    visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = TextTemplateDto.class, name = "TEXT"),
  @JsonSubTypes.Type(value = ImageTemplateDto.class, name = "IMAGE"),
  @JsonSubTypes.Type(value = VideoTemplateDto.class, name = "VIDEO"),
  @JsonSubTypes.Type(value = DocumentTemplateDto.class, name = "DOCUMENT"),
  @JsonSubTypes.Type(value = GIFTemplateDto.class, name = "GIF"),
  @JsonSubTypes.Type(value = LocationTemplateDto.class, name = "LOCATION"),
  @JsonSubTypes.Type(value = CatalogTemplateDto.class, name = "CATALOG"),
  @JsonSubTypes.Type(value = ProductTemplateDto.class, name = "PRODUCT"),
  @JsonSubTypes.Type(value = CarouselTemplateDto.class, name = "CAROUSEL"),
  @JsonSubTypes.Type(value = AuthenticationTemplateDto.class, name = "AUTHENTICATION")
})
public abstract class BaseTemplateDto {
  private String appId;
  private String elementName;
  private LanguageCode languageCode;
  private String body;
  private List<String> variableExamples;
  private TemplateCategory category;
  private TemplateType templateType;
  private TemplateParameterFormat parameterFormat = TemplateParameterFormat.POSITIONAL;
  private List<String> tags;
  private String footer;
  private Integer messageValidity;
  private List<Button> buttons;
  private LTOAttributes ltoAttributes;

  public abstract Template toDomain();
}
