package io.github.mrcloss.gupshup.domain.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class VideoPayload extends MediaPayload {
  private final VideoWrapper video;

  public VideoPayload(String link, String id) {
    super(MessageType.VIDEO);

    this.video = new VideoWrapper(link, id);
  }

  @Getter
  @ToString
  public static class VideoWrapper {
    private final String link;
    private final String id;

    public VideoWrapper(String link, String id) {
      this.link = link;
      this.id = id;
    }
  }
}
