package io.github.mrcloss.gupshup.domain.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class DocumentPayload extends GupshupMessage {

  private final DocumentWraper document;

  public DocumentPayload(String link, String id, String filename) {
    super(MessageType.DOCUMENT);
    document = new DocumentWraper(link, id, filename);
  }

  @Getter
  @ToString
  public static class DocumentWraper {

    private final String link;
    private final String id;
    private final String filename;

    public DocumentWraper(String link, String id, String filename) {
      this.link = link;
      this.id = id;
      this.filename = filename;
    }
  }
}
