package io.github.mrcloss.gupshup.domain.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoPayload extends GupshupMessage {
  private final VideoWrapper video;

  public VideoPayload(String link, String id) {
    super(MessageType.VIDEO);

    this.video = new VideoWrapper(link, id);
  }

  @Getter
  public static class VideoWrapper {
    private final String link;
    private final String id;

    public VideoWrapper(String link, String id) {
      this.link = link;
      this.id = id;
    }
  }
}
