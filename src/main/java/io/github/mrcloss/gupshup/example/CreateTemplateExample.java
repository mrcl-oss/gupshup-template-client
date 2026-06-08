package io.github.mrcloss.gupshup.example;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.mrcloss.gupshup.client.GupshupClient;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.template.TextTemplate;
import io.github.mrcloss.gupshup.infrastructure.dto.response.CreateTemplateResponse;

import java.util.Collections;
import java.util.List;

/**
 * A simple example of how to use the GupshupClient to create a WhatsApp template.
 */
public class CreateTemplateExample {

    public static void main(String[] args) {
        // Load configuration from .env file
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("API_KEY");
        String appId = dotenv.get("APP_ID");

        if (apiKey == null || appId == null) {
            System.err.println("API_KEY and APP_ID must be set in the .env file");
            System.exit(1);
        }

        // Initialize the client
        GupshupClient client = GupshupClient.builder()
                .appId(appId)
                .apiKey(apiKey)
                .build();

        // Create a simple text template
        TextTemplate template = new TextTemplate(
                "marketing_promo_01",           // elementName
                LanguageCode.ENGLISH,           // languageCode
                "Hello {{1}}, welcome to our service!", // body
                List.of("Marc"),
                TemplateCategory.MARKETING,      // category
                appId,                          // appId
                Collections.singletonList("PROMO"), // tags
                TemplateParameterFormat.POSITIONAL // parameterFormat
        );

        // Add a footer (optional)
        template.setFooter("Reply STOP to opt out.");

        // Create the template
        try {
            System.out.println("Creating template...");
            CreateTemplateResponse response = client.createTemplate(template);
            
            System.out.println("Status: " + response.getStatus());
            System.out.println("Message: " + response.getMessage());
            if (response.getTemplate() != null) {
                System.out.println("Template ID: " + response.getTemplate().getId());
                System.out.println("Namespace: " + response.getTemplate().getNamespace());
            }
            if (response.getError() != null) {
                System.out.println("Error: " + response.getError());
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
