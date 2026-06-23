package io.github.mrcloss.gupshup.example;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.mrcloss.gupshup.client.GupshupClient;
import io.github.mrcloss.gupshup.infrastructure.dto.response.OptInResponse;

/** A simple example of how to use the GupshupClient to mark a user as opted-in. */
public class OptInUserExample {

  public static void main(String[] args) {
    // Load configuration from .env file
    Dotenv dotenv = Dotenv.load();
    String apiKey = dotenv.get("API_KEY");
    String appId = dotenv.get("APP_ID");
    // Change this to the registered app name on your Gupshup dashboard
    String appName = dotenv.get("APP_NAME");

    if (apiKey == null || appId == null || appName == null) {
      System.err.println("API_KEY, APP_ID, and APP_NAME must be set in the .env file");
      System.exit(1);
    }

    // Initialize the client
    GupshupClient client = GupshupClient.builder().appId(appId).apiKey(apiKey).build();

    // Define the phone number to be opted-in (must include country code, e.g. 34600123456)
    String phoneNumber = "34600123456";

    // Mark the user as opted-in
    try {
      System.out.println("Marking user as opted-in for app: " + appName + "...");
      OptInResponse response = client.optIn(appName, phoneNumber);

      System.out.println("Status: " + response.getStatus());
      if (response.isSuccess()) {
        System.out.println("User marked as opted-in successfully.");
      } else {
        System.out.println("Failed to opt-in user: " + response.getError());
      }
    } catch (io.github.mrcloss.gupshup.exception.GupshupApiException e) {
      System.err.println("Gupshup API Error: " + e.getMessage());
      System.err.println("Status Code: " + e.getStatusCode());
      System.err.println("Response Body: " + e.getResponseBody());
    } catch (Exception e) {
      System.err.println("Error opting in user: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
