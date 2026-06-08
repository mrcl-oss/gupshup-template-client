package io.github.mrcloss.gupshup.client;

import io.github.mrcloss.gupshup.domain.template.Template;
import io.github.mrcloss.gupshup.infrastructure.dto.response.CreateTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.DeleteTemplateResponse;

import java.util.concurrent.CompletableFuture;

/**
 * Interface for the Gupshup WhatsApp Template API client.
 */
public interface GupshupClient {

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
     * Builder for GupshupClient.
     *
     * @return A new Builder instance.
     */
    static DefaultGupshupClient.Builder builder() {
        return DefaultGupshupClient.builder();
    }
}
