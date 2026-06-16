package io.github.mrcloss.gupshup.client;

import io.github.mrcloss.gupshup.domain.message.GupshupMessage;
import io.github.mrcloss.gupshup.domain.template.Template;
import io.github.mrcloss.gupshup.infrastructure.dto.request.QueryParams;
import io.github.mrcloss.gupshup.infrastructure.dto.request.SendTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.response.CreateTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.DeleteTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.GetTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.GetTemplatesResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.SendTemplateResponse;

import java.util.concurrent.CompletableFuture;

/**
 * Interface for the Gupshup WhatsApp Template API client.
 */
public interface GupshupClient {


    /**
     * Retrieves all templates from Gupshup.
     *
     * @param template
     * @return
     */
    GetTemplatesResponse getTemplates(QueryParams queryParams);


    /**
     * Retrieve
     * @param template
     * @return
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
     * @param source The registered WhatsApp Business API phone number.
     * @param destination The user's phone number.
     * @param templateId The ID of the template to send.
     * @param parameters The ordered list of variables for the template placeholders.
     * @param mediaMessage The media object representation (null if text-only).
     * @return The response from Gupshup.
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
