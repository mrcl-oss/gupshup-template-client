package io.github.mrcloss.gupshup.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.template.TextTemplate;
import io.github.mrcloss.gupshup.infrastructure.dto.response.CreateTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.DeleteTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.GupshupTemplate;
import io.github.mrcloss.gupshup.infrastructure.https.GupshupHttpService;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GupshupClientTest {

  @Mock private GupshupHttpService httpService;

  private GupshupClient client;

  private final String appId = "test-app-id";
  private final String apiKey = "test-api-key";

  @BeforeEach
  void setUp() {
    client =
        DefaultGupshupClient.builder().appId(appId).apiKey(apiKey).httpService(httpService).build();
  }

  @Test
  void testCreateTemplate() {
    // Arrange
    TextTemplate template =
        new TextTemplate(
            "test_element",
            LanguageCode.ENGLISH,
            "Hello {{1}}!",
            Collections.singletonList("World"),
            TemplateCategory.MARKETING,
            appId,
            null,
            null);

    CreateTemplateResponse expectedResponse = new CreateTemplateResponse();
    expectedResponse.setStatus("success");
    GupshupTemplate gupshupTemplate = new GupshupTemplate();
    gupshupTemplate.setId("test-id");
    gupshupTemplate.setElementName("test_element");
    expectedResponse.setTemplate(gupshupTemplate);

    when(httpService.postForm(anyString(), anyMap(), eq(CreateTemplateResponse.class)))
        .thenReturn(expectedResponse);

    // Act
    CreateTemplateResponse response = client.createTemplate(template);

    // Assert
    assertEquals("success", response.getStatus());
    assertEquals("test-id", response.getTemplate().getId());
    assertEquals("test_element", response.getTemplate().getElementName());
    verify(httpService)
        .postForm(
            eq("https://api.gupshup.io/wa/app/test-app-id/template"),
            anyMap(),
            eq(CreateTemplateResponse.class));
  }

  @Test
  void testDeleteTemplate() {
    // Arrange
    String templateName = "test_template";
    DeleteTemplateResponse expectedResponse = new DeleteTemplateResponse();
    expectedResponse.setStatus("success");

    when(httpService.delete(anyString(), eq(DeleteTemplateResponse.class)))
        .thenReturn(expectedResponse);

    // Act
    DeleteTemplateResponse response = client.deleteTemplate(templateName);

    // Assert
    assertEquals("success", response.getStatus());
    verify(httpService)
        .delete(
            eq("https://api.gupshup.io/wa/app/test-app-id/template/" + templateName),
            eq(DeleteTemplateResponse.class));
  }

  @Test
  void testDeleteTemplateAsync() throws Exception {
    // Arrange
    String templateName = "test_template";
    DeleteTemplateResponse expectedResponse = new DeleteTemplateResponse();
    expectedResponse.setStatus("success");

    when(httpService.deleteAsync(anyString(), eq(DeleteTemplateResponse.class)))
        .thenReturn(CompletableFuture.completedFuture(expectedResponse));

    // Act
    CompletableFuture<DeleteTemplateResponse> future = client.deleteTemplateAsync(templateName);
    DeleteTemplateResponse response = future.get();

    // Assert
    assertEquals("success", response.getStatus());
    verify(httpService)
        .deleteAsync(
            eq("https://api.gupshup.io/wa/app/test-app-id/template/" + templateName),
            eq(DeleteTemplateResponse.class));
  }
}
