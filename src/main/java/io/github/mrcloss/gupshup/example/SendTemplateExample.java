package io.github.mrcloss.gupshup.example;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.mrcloss.gupshup.client.GupshupClient;
import io.github.mrcloss.gupshup.domain.message.ImagePayload;
import io.github.mrcloss.gupshup.infrastructure.dto.request.SendTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.response.SendTemplateResponse;
import java.util.ArrayList;
import java.util.List;

public class SendTemplateExample {

  public static void main(String[] args) {
    Dotenv dotenv = Dotenv.load();
    String apiKey = dotenv.get("API_KEY");
    String appId = dotenv.get("APP_ID");

    if (apiKey == null || appId == null) {
      System.err.println("API_KEY and APP_ID must be set in the .env file");
      System.exit(1);
    }

    GupshupClient client = GupshupClient.builder().appId(appId).apiKey(apiKey).build();

    List<String> params = new ArrayList<>();

    // CarouselPayload carouselPayload = new
    // CarouselPayload(CarouselPayload.CardHeaderType.IMAGE,
    // List.of(
    // "https://fss.gupshup.io/0/public/0/0/gupshup/34977900141/ceb85f9b-dd26-4ba5-b16a-a9eb883dba52/1745503565625_ceb85f9b-dd26-4ba5-b16a-a9eb883dba52.png",
    // "https://fss.gupshup.io/0/public/0/0/gupshup/34977900141/edea0ce9-f495-4d2a-a78a-baf1bc2feb00/1745503565946_edea0ce9-f495-4d2a-a78a-baf1bc2feb00.png")
    // );

    ImagePayload imagePayload =
        new ImagePayload(
            "https://fss.gupshup.io/0/public/0/0/gupshup/34977900141/49e2713c-1c18-4a2e-b184-65eb021be7cc/1781175474910_49e2713c-1c18-4a2e-b184-65eb021be7cc.png",
            null);

    SendTemplateRequest sendTemplateRequest =
        new SendTemplateRequest(
            "34977900141",
            "34689395507",
            "PruebasManhattan",
            "082da7ff-c2ff-41de-9ba5-0725f29bbbfd",
            params,
            imagePayload);

    try {
      System.out.println("Sending template...");
      SendTemplateResponse response = client.sendTemplate(sendTemplateRequest);

      System.out.println("Status: " + response.getStatus());
      if (response.getError() != null) {
        System.out.println("Error: " + response.getError());
      }
      if (response.getMessageId() != null) {
        System.out.println("Message ID: " + response.getMessageId());
      }
    } catch (io.github.mrcloss.gupshup.exception.GupshupApiException e) {
      System.err.println("Gupshup API Error: " + e.getMessage());
      System.err.println("Status Code: " + e.getStatusCode());
      System.err.println("Response Body: " + e.getResponseBody());
    } catch (Exception e) {
      System.err.println("Error creating template: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
