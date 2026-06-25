package io.github.mrcloss.gupshup.domain.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** Represents the Multi-Product Message (MPM) payload used inside the template parameter. */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Mpm {

  private List<Section> sections;

  /** Represents a section within the Multi-Product Message. */
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Section {
    private String title;
    private List<Product> products;
  }

  /** Represents a product within a section. */
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Product {
    @JsonProperty("product_retailer_id")
    private String productRetailerId;
  }
}
