package io.github.mrcloss.gupshup.client;

import io.github.mrcloss.gupshup.domain.template.Template;
import io.github.mrcloss.gupshup.infrastructure.dto.request.QueryParams;
import io.github.mrcloss.gupshup.infrastructure.dto.request.SendTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.response.CreateTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.DeleteTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.GetTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.GetTemplatesResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.SendTemplateResponse;
import java.util.concurrent.CompletableFuture;

/** Interface for the Gupshup WhatsApp Template API client. */
public interface GupshupClient {

  /**
   * Retrieves all templates from Gupshup.
   *
   * @param queryParams the query parameters for filtering and pagination
   * @return the response containing the list of templates
   */
  GetTemplatesResponse getTemplates(QueryParams queryParams);

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
   * @param template The template domain object.
   * @return The response from Gupshup.
   */
  CreateTemplateResponse createTemplate(Template template);

  /**
   * Asynchronously creates a new template on Gupshup.
   *
   * @param template The template domain object.
   * @return A CompletableFuture with the response from Gupshup.
   */
  CompletableFuture<CreateTemplateResponse> createTemplateAsync(Template template);

  /**
   * Deletes a template from Gupshup.
   *
   * @param templateName The name of the template to delete.
   * @return The response from Gupshup.
   */
  DeleteTemplateResponse deleteTemplate(String templateName);

  /**
   * Asynchronously deletes a template from Gupshup.
   *
   * @param templateName The name of the template to delete.
   * @return A CompletableFuture with the response from Gupshup.
   */
  CompletableFuture<DeleteTemplateResponse> deleteTemplateAsync(String templateName);

  /**
   * Sends a template message via Gupshup.
   *
   * @param request the request payload containing the message details
   * @return the response from Gupshup.
   */
  SendTemplateResponse sendTemplate(SendTemplateRequest request);

  /**
   * Builder for GupshupClient.
   *
   * @return A new Builder instance.
   */
  static DefaultGupshupClient.Builder builder() {
    return DefaultGupshupClient.builder();
  }
}
