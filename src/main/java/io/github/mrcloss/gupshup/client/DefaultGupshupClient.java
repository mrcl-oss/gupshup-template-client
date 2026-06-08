package io.github.mrcloss.gupshup.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.mrcloss.gupshup.domain.template.Template;
import io.github.mrcloss.gupshup.infrastructure.dto.request.TemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.response.CreateTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.DeleteTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.https.GupshupHttpService;
import io.github.mrcloss.gupshup.infrastructure.https.JdkGupshupHttpService;
import io.github.mrcloss.gupshup.infrastructure.mapper.GupshupRequestMapper;

import java.net.http.HttpClient;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Default implementation of GupshupClient.
 */
public class DefaultGupshupClient implements GupshupClient {

    private final GupshupHttpService httpService;
    private final ObjectMapper objectMapper;

    private DefaultGupshupClient(Builder builder) {
        this.objectMapper = builder.objectMapper != null ? builder.objectMapper : createDefaultObjectMapper();
        HttpClient httpClient = builder.httpClient != null ? builder.httpClient : HttpClient.newHttpClient();
        String baseUrl = String.format("https://api.gupshup.io/wa/app/%s/template", builder.appId);
        
        this.httpService = builder.httpService != null ? builder.httpService : 
                new JdkGupshupHttpService(builder.apiKey, baseUrl, httpClient, objectMapper);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public CreateTemplateResponse createTemplate(Template template) {
        template.validate();
        Map<String, Object> body = convertToMap(GupshupRequestMapper.map(template));
        return httpService.postForm("", body, CreateTemplateResponse.class);
    }

    @Override
    public CompletableFuture<CreateTemplateResponse> createTemplateAsync(Template template) {
        template.validate();
        Map<String, Object> body = convertToMap(GupshupRequestMapper.map(template));
        return httpService.postFormAsync("", body, CreateTemplateResponse.class);
    }

    @Override
    public DeleteTemplateResponse deleteTemplate(String templateName) {
        return httpService.delete("/" + templateName, DeleteTemplateResponse.class);
    }

    @Override
    public CompletableFuture<DeleteTemplateResponse> deleteTemplateAsync(String templateName) {
        return httpService.deleteAsync("/" + templateName, DeleteTemplateResponse.class);
    }

    private Map<String, Object> convertToMap(TemplateRequest request) {
        Map<String, Object> map = objectMapper.convertValue(request, new TypeReference<Map<String, Object>>() {});
        // appId is already in the URL
        map.remove("appId");
        return map;
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
        private GupshupHttpService httpService;

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

        public Builder httpService(GupshupHttpService httpService) {
            this.httpService = httpService;
            return this;
        }

        public DefaultGupshupClient build() {
            if (appId == null || appId.trim().isEmpty()) {
                throw new IllegalStateException("appId is required");
            }
            if (apiKey == null || apiKey.trim().isEmpty()) {
                throw new IllegalStateException("apiKey is required");
            }
            return new DefaultGupshupClient(this);
        }
    }
}
