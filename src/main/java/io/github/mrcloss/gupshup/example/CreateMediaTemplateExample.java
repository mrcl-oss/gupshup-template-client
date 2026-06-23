package io.github.mrcloss.gupshup.example;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.mrcloss.gupshup.client.GupshupClient;
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.template.ImageTemplate;
import io.github.mrcloss.gupshup.infrastructure.dto.response.CreateTemplateResponse;
import java.io.File;
import java.util.Collections;

/**
 * An example of how to use the GupshupClient to create a WhatsApp template with a media file.
 *
 * <p>The client will automatically upload the media file to Gupshup prior to creating the template.
 */
public class CreateMediaTemplateExample {

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
    GupshupClient client = GupshupClient.builder().appId(appId).apiKey(apiKey).build();

    // Define a local media file to be uploaded (e.g. a sample PNG image)
    File mediaFile =
        new File(
            "/home/ayuda100/Proyectos/gupshup-template-client/src/main/java/io/github/mrcloss/gupshup/example/sample_image.jpeg");
    if (!mediaFile.exists()) {
      System.out.println("Warning: sample_image.jpeg not found locally.");
      System.out.println("Please place a valid file or run this with an existing file path.");
    }

    // Create an ImageTemplate and attach the file
    ImageTemplate template =
        new ImageTemplate(
            "media_image_template_example", // elementName
            LanguageCode.SPANISH_ES, // languageCode
            "Hola! Te enviamos esta imagen de prueba.", // body
            TemplateCategory.MARKETING, // category
            appId, // appId
            Collections.singletonList("MEDIA"), // tags
            TemplateParameterFormat.POSITIONAL // parameterFormat
            );
    template.setMediaFile(mediaFile);

    // Create the template (this automatically uploads the file and populates mediaId and mediaUrl)
    try {
      System.out.println("Creating template with automatic media upload...");
      CreateTemplateResponse response = client.createTemplate(template);

      System.out.println("Status: " + response.getStatus());
      if (response.getTemplate() != null) {
        System.out.println("Template ID: " + response.getTemplate().getId());
        System.out.println("Uploaded Media ID: " + template.getMediaId());
        System.out.println("Uploaded Media URL: " + template.getMediaUrl());
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
