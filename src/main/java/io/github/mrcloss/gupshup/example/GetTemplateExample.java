package io.github.mrcloss.gupshup.example;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.mrcloss.gupshup.client.GupshupClient;
import io.github.mrcloss.gupshup.infrastructure.dto.response.GetTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.GupshupTemplate;

public class GetTemplateExample {

  public static void main(String[] args) {

    Dotenv dotenv = Dotenv.load();
    String apiKey = dotenv.get("API_KEY");
    String appId = dotenv.get("APP_ID");

    if (apiKey == null || appId == null) {
      System.err.println("API_KEY and APP_ID must be set in the .env file");
      System.exit(1);
    }

    GupshupClient client = GupshupClient.builder().appId(appId).apiKey(apiKey).build();

    // QueryParams queryParams = new QueryParams(0, 10, null, null, null, null, null);

    try {
      System.out.println("Get template...");
      GetTemplateResponse response = client.getTemplate("1c9c1def-96d2-4bb1-b095-e2608627e242");

      System.out.println("Status: " + response.getStatus());

      GupshupTemplate template = response.getTemplate();
      // if (templates != null) {
      //     System.out.println("Templates:");
      //     for (GupshupTemplate template : templates) {
      //         System.out.println("Template: " + template.getId());
      //     }
      // }

      System.out.println(template.getCategory());
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
