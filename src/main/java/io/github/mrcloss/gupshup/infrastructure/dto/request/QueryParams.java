package io.github.mrcloss.gupshup.infrastructure.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QueryParams {
  private int pageNo;
  private int pageSize;
  private String templateStatus;
  private String templateCategory;
  private String templateType;
  private String quality;
  private String languageCode;
}
