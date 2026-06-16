package io.github.mrcloss.gupshup.example;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.mrcloss.gupshup.client.GupshupClient;
import io.github.mrcloss.gupshup.infrastructure.dto.response.DeleteTemplateResponse;

/**
 * A simple example of how to use the GupshupClient to delete a WhatsApp template.
 */
public class DeleteTemplateExample {

    public static void main(String[] args) {
        // Load configuration from .env file
        // Dotenv dotenv = Dotenv.load();
        // String apiKey = dotenv.get("API_KEY");
        // String appId = dotenv.get("APP_ID");

        // if (apiKey == null || appId == null) {
        //     System.err.println("API_KEY and APP_ID must be set in the .env file");
        //     System.exit(1);
        // }

        // // Initialize the client
        // GupshupClient client = GupshupClient.builder()
        //         .appId(appId)
        //         .apiKey(apiKey)
        //         .build();

        // String templateName = "borra_esto_porfa_2";

        // // Delete the template
        // try {
        //     System.out.println("Deleting template: " + templateName);
        //     DeleteTemplateResponse response = client.deleteTemplate(templateName);
            
        //     System.out.println("Status: " + response.getStatus());
        //     if (response.getError() != null) {
        //         System.out.println("Error: " + response.getError());
        //     }
        // } catch (io.github.mrcloss.gupshup.exception.GupshupApiException e) {
        //     System.err.println("Gupshup API Error: " + e.getMessage());
        //     System.err.println("Status Code: " + e.getStatusCode());
        //     System.err.println("Response Body: " + e.getResponseBody());
        // } catch (Exception e) {
        //     System.err.println("Error deleting template: " + e.getMessage());
        //     e.printStackTrace();
        // }
    }
}
