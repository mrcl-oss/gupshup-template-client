package io.github.mrcloss.gupshup.domain.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationPayload extends GupshupMessage {
  private final LocationWrapper location;
  private final PostBackTexts postbackTexts;

  public LocationPayload(
      double latitude, double longitude, String name, String address, int index, String text) {
    super(MessageType.LOCATION);
    this.location = new LocationWrapper(latitude, longitude, name, address);
    this.postbackTexts = new PostBackTexts(index, text);
  }

  @Getter
  public static class LocationWrapper {
    private final double latitude;
    private final double longitude;
    private final String name;
    private final String address;

    public LocationWrapper(double latitude, double longitude, String name, String address) {
      this.latitude = latitude;
      this.longitude = longitude;
      this.name = name;
      this.address = address;
    }
  }

  @Getter
  public static class PostBackTexts {

    private final int index;
    private final String text;

    public PostBackTexts(int index, String text) {
      this.index = index;
      this.text = text;
    }
  }
}
