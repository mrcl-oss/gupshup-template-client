package io.github.mrcloss.gupshup.infrastructure.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Response returned when a media file is successfully uploaded. */
@Getter
@Setter
@ToString(callSuper = true)
public class UploadMediaResponse extends BaseGupshupResponse {
  private MediaDetails media;

  /** Details of the uploaded media file returned by Gupshup. */
  @Getter
  @Setter
  @ToString
  public static class MediaDetails {
    private String fileName;
    private String id;
    private String url;
  }
}
