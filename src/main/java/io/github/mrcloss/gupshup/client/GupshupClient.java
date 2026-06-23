package io.github.mrcloss.gupshup.client;

import io.github.mrcloss.gupshup.domain.template.Template;
import io.github.mrcloss.gupshup.infrastructure.dto.request.QueryParams;
import io.github.mrcloss.gupshup.infrastructure.dto.request.SendTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.response.CreateTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.DeleteTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.GetTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.GetTemplatesResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.OptInResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.SendTemplateResponse;
import java.util.concurrent.CompletableFuture;

/**
 * Client interface for interacting with the Gupshup WhatsApp Template API.
 *
 * <p>This interface provides synchronous and asynchronous methods to query, create, delete, and
 * send WhatsApp templates.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * GupshupClient client = GupshupClient.builder()
 *     .appId("your-app-id")
 *     .apiKey("your-api-key")
 *     .build();
 *
 * // Create a basic utility text template
 * TextTemplate template = new TextTemplate(
 *     "welcome_user_template",
 *     LanguageCode.ENGLISH,
 *     "Hello {{1}}! Thanks for registering.",
 *     TemplateCategory.UTILITY,
 *     "your-app-id",
 *     List.of("onboarding"),
 *     TemplateParameterFormat.POSITIONAL
 * );
 * template.setVariableExamples(List.of("Alice"));
 *
 * CreateTemplateResponse response = client.createTemplate(template);
 * if ("success".equals(response.getStatus())) {
 *     System.out.println("Created template ID: " + response.getTemplate().getId());
 * }
 * }</pre>
 */
public interface GupshupClient {

  /**
   * Retrieves all templates from Gupshup.
   *
   * @param queryParams the query parameters for filtering and pagination
   * @return the response containing the list of templates
   */
  GetTemplatesResponse getTemplates(QueryParams queryParams);

  /**
   * Retrieves all templates from Gupshup.
   *
   * @return the response containing the list of templates
   */
  GetTemplatesResponse getTemplates();

  /**
   * Retrieves a single template by its ID.
   *
   * @param templateId the unique ID of the template
   * @return the response containing the template details
   */
  GetTemplateResponse getTemplate(String templateId);

  /**
   * Creates a new template on Gupshup.
   *
   * @param template the template domain object to be created
   * @return the response containing creation status and template details
   */
  CreateTemplateResponse createTemplate(Template template);

  /**
   * Asynchronously creates a new template on Gupshup.
   *
   * @param template the template domain object to be created
   * @return a CompletableFuture containing the response with creation status and template details
   */
  CompletableFuture<CreateTemplateResponse> createTemplateAsync(Template template);

  /**
   * Deletes a template from Gupshup.
   *
   * @param templateName the name of the template to delete
   * @return the response indicating success or failure of the deletion
   */
  DeleteTemplateResponse deleteTemplate(String templateName);

  /**
   * Asynchronously deletes a template from Gupshup.
   *
   * @param templateName the name of the template to delete
   * @return a CompletableFuture containing the response indicating success or failure of the
   *     deletion
   */
  CompletableFuture<DeleteTemplateResponse> deleteTemplateAsync(String templateName);

  /**
   * Sends a template message via Gupshup.
   *
   * @param request the request payload containing the message details (recipients, parameters)
   * @return the response containing the message ID
   */
  SendTemplateResponse sendTemplate(SendTemplateRequest request);

  /**
   * Marks a user as opted-in to receive WhatsApp messages.
   *
   * @param appName the registered Gupshup app name
   * @param phoneNumber the user's phone number
   * @return the response indicating success or failure of the opt-in
   */
  OptInResponse optIn(String appName, String phoneNumber);

  /**
   * Asynchronously marks a user as opted-in to receive WhatsApp messages.
   *
   * @param appName the registered Gupshup app name
   * @param phoneNumber the user's phone number
   * @return a CompletableFuture containing the response indicating success or failure
   */
  CompletableFuture<OptInResponse> optInAsync(String appName, String phoneNumber);

  /**
   * Creates a new builder for configuring and instantiating a GupshupClient.
   *
   * @return a new Builder instance
   */
  static DefaultGupshupClient.Builder builder() {
    return DefaultGupshupClient.builder();
  }
}
