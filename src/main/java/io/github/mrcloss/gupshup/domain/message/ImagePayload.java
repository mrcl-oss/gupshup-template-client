package io.github.mrcloss.gupshup.domain.message;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class ImagePayload extends MediaPayload {

  private final ImageWrapper image;

  public ImagePayload(String link, String id) {
    super(MessageType.IMAGE);

    this.image = new ImageWrapper(link, id);
  }

  @Getter
  @ToString
  public static class ImageWrapper {
    private final String link;
    private final String id;

    public ImageWrapper(String link, String id) {
      this.link = link;
      this.id = id;
    }
  }
}
