package io.github.mrcloss.gupshup.domain.message;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
/**
 * Represents a WhatsApp looping GIF message payload.
 *
 * <p>WhatsApp represents GIFs under the hood as looping MP4 videos without audio.
 */
public class GifPayload extends MediaPayload {

  private final GifWrapper gif;

  /**
   * Constructs a new GifPayload with a media link and optional ID.
   *
   * @param link the public HTTP/HTTPS URL of the GIF file
   * @param id the optional media ID if the file was pre-uploaded
   */
  public GifPayload(String link, String id) {
    super(MessageType.GIF);
    this.gif = new GifWrapper(link, id);
  }

  /** Wrapper for the GIF media link and ID properties. */
  @Getter
  @ToString
  public static class GifWrapper {
    private final String link;
    private final String id;

    /**
     * Constructs a new GifWrapper.
     *
     * @param link the URL of the GIF
     * @param id the pre-uploaded media ID
     */
    public GifWrapper(String link, String id) {
      this.link = link;
      this.id = id;
    }
  }
}
