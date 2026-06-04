package io.github.mrcloss.gupshup.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.mrcloss.gupshup.domain.template.Template;
import io.github.mrcloss.gupshup.exception.GupshupApiException;
import io.github.mrcloss.gupshup.infrastructure.dto.request.TemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.response.GupshupResponse;
import io.github.mrcloss.gupshup.infrastructure.mapper.GupshupRequestMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * High-level client for interacting with the Gupshup API.
 */
public class GupshupClient {

    private final String appId;
    private final String apiKey;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    private GupshupClient(Builder builder) {
        this.appId = builder.appId;
        this.apiKey = builder.apiKey;
        this.httpClient = builder.httpClient != null ? builder.httpClient : HttpClient.newHttpClient();
        this.objectMapper = builder.objectMapper != null ? builder.objectMapper : createDefaultObjectMapper();
        this.baseUrl = String.format("https://api.gupshup.io/wa/app/%s/template", appId);
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a new template on Gupshup.
     *
     * @param template The template domain object.
     * @return The response from Gupshup.
     * @throws GupshupApiException if the API returns an error or the request fails.
     */
    public GupshupResponse createTemplate(Template template) {
        template.validate();
        TemplateRequest requestDto = GupshupRequestMapper.map(template);
        
        try {
            String jsonBody = objectMapper.writeValueAsString(requestDto);
            
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl))
                    .header("apikey", apiKey)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            
            return handleResponse(response);
        } catch (GupshupApiException e) {
            throw e;
        } catch (Exception e) {
            throw new GupshupApiException("Failed to create template: " + e.getMessage(), 0, null);
        }
    }

    /**
     * Asynchronously creates a new template on Gupshup.
     *
     * @param template The template domain object.
     * @return A CompletableFuture with the response from Gupshup.
     */
    public CompletableFuture<GupshupResponse> createTemplateAsync(Template template) {
        template.validate();
        TemplateRequest requestDto = GupshupRequestMapper.map(template);

        try {
            String jsonBody = objectMapper.writeValueAsString(requestDto);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl))
                    .header("apikey", apiKey)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(this::handleResponse);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(new GupshupApiException("Failed to start async template creation: " + e.getMessage(), 0, null));
        }
    }

    private GupshupResponse handleResponse(HttpResponse<String> response) {
        try {
            String body = response.body();
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return objectMapper.readValue(body, GupshupResponse.class);
            } else {
                throw new GupshupApiException("Gupshup API error", response.statusCode(), body);
            }
        } catch (GupshupApiException e) {
            throw e;
        } catch (Exception e) {
            throw new GupshupApiException("Failed to parse Gupshup response: " + e.getMessage(), response.statusCode(), response.body());
        }
    }

    private static ObjectMapper createDefaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    public static class Builder {
        private String appId;
        private String apiKey;
        private HttpClient httpClient;
        private ObjectMapper objectMapper;

        public Builder appId(String appId) {
            this.appId = appId;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public Builder objectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }

        public GupshupClient build() {
            if (appId == null || appId.trim().isEmpty()) {
                throw new IllegalStateException("appId is required");
            }
            if (apiKey == null || apiKey.trim().isEmpty()) {
                throw new IllegalStateException("apiKey is required");
            }
            return new GupshupClient(this);
        }
    }
}
