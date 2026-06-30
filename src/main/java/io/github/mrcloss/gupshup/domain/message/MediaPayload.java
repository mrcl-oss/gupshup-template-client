package io.github.mrcloss.gupshup.domain.message;

/** Abstract base class for all media-based message payloads (Image, Video, Document). */
public abstract class MediaPayload extends GupshupMessage {

  protected MediaPayload(MessageType messageType) {
    super(messageType);
  }
}
