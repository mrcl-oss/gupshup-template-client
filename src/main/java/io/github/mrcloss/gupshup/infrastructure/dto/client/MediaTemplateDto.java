package io.github.mrcloss.gupshup.infrastructure.dto.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class MediaTemplateDto extends BaseTemplateDto {
  private String mediaId;
  private String mediaUrl;
}
