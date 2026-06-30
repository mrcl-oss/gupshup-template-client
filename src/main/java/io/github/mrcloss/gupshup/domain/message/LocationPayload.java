package io.github.mrcloss.gupshup.domain.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationPayload extends GupshupMessage {
  private final LocationWrapper location;

  @JsonProperty("postbackTexts")
  @lombok.Getter(lombok.AccessLevel.NONE)
  private final Object postbackTexts;

  public LocationPayload(double latitude, double longitude, String name, String address) {
    super(MessageType.LOCATION);
    this.location = new LocationWrapper(latitude, longitude, name, address);
    this.postbackTexts = null;
  }

  public LocationPayload(
      double latitude, double longitude, String name, String address, int index, String text) {
    super(MessageType.LOCATION);
    this.location = new LocationWrapper(latitude, longitude, name, address);
    this.postbackTexts = new PostBackTexts(index, text);
  }

  public LocationPayload(
      double latitude,
      double longitude,
      String name,
      String address,
      List<PostBackTexts> postbackTextsList) {
    super(MessageType.LOCATION);
    this.location = new LocationWrapper(latitude, longitude, name, address);
    this.postbackTexts = postbackTextsList;
  }

  /**
   * Gets the postback texts as a single object. If a list was provided, returns the first element
   * or null.
   *
   * @return the PostBackTexts instance or null
   */
  @JsonIgnore
  public PostBackTexts getPostbackTexts() {
    if (postbackTexts instanceof PostBackTexts) {
      return (PostBackTexts) postbackTexts;
    } else if (postbackTexts instanceof List) {
      List<?> list = (List<?>) postbackTexts;
      if (!list.isEmpty() && list.get(0) instanceof PostBackTexts) {
        return (PostBackTexts) list.get(0);
      }
    }
    return null;
  }

  /**
   * Gets the full list of postback texts.
   *
   * @return a list of PostBackTexts
   */
  @JsonIgnore
  @SuppressWarnings("unchecked")
  public List<PostBackTexts> getPostbackTextsList() {
    if (postbackTexts instanceof List) {
      return (List<PostBackTexts>) postbackTexts;
    } else if (postbackTexts instanceof PostBackTexts) {
      return Collections.singletonList((PostBackTexts) postbackTexts);
    }
    return Collections.emptyList();
  }

  @Getter
  @ToString
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
  @ToString
  public static class PostBackTexts {

    private final int index;
    private final String text;

    public PostBackTexts(int index, String text) {
      this.index = index;
      this.text = text;
    }
  }
}
