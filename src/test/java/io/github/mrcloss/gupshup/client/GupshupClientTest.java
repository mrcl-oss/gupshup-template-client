package io.github.mrcloss.gupshup.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.template.CarouselCard;
import io.github.mrcloss.gupshup.domain.template.CarouselTemplate;
import io.github.mrcloss.gupshup.domain.template.ImageTemplate;
import io.github.mrcloss.gupshup.domain.template.TextTemplate;
import io.github.mrcloss.gupshup.infrastructure.dto.response.CreateTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.DeleteTemplateResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.GupshupTemplateDetails;
import io.github.mrcloss.gupshup.infrastructure.dto.response.OptInResponse;
import io.github.mrcloss.gupshup.infrastructure.dto.response.UploadMediaResponse;
import io.github.mrcloss.gupshup.infrastructure.http.GupshupHttpService;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    GupshupTemplateDetails gupshupTemplate = new GupshupTemplateDetails();
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

  @Test
  void testCreateTemplateWithMediaFileUpload() throws Exception {
    // Arrange
    ImageTemplate template =
        new ImageTemplate(
            "test_image_template",
            LanguageCode.ENGLISH,
            "Hello",
            TemplateCategory.MARKETING,
            appId,
            null,
            TemplateParameterFormat.POSITIONAL);
    File tempFile = File.createTempFile("test-image", ".png");
    tempFile.deleteOnExit();
    template.setMediaFile(tempFile);

    UploadMediaResponse uploadResponse = new UploadMediaResponse();
    uploadResponse.setStatus("success");
    UploadMediaResponse.MediaDetails mediaDetails = new UploadMediaResponse.MediaDetails();
    mediaDetails.setFileName(tempFile.getName());
    mediaDetails.setId("uploaded-media-id");
    mediaDetails.setUrl("https://example.com/uploaded-image.png");
    uploadResponse.setMedia(mediaDetails);

    when(httpService.uploadMedia(anyString(), any(File.class), eq(UploadMediaResponse.class)))
        .thenReturn(uploadResponse);

    CreateTemplateResponse expectedResponse = new CreateTemplateResponse();
    expectedResponse.setStatus("success");
    GupshupTemplateDetails gupshupTemplate = new GupshupTemplateDetails();
    gupshupTemplate.setId("test-id");
    gupshupTemplate.setElementName("test_image_template");
    expectedResponse.setTemplate(gupshupTemplate);

    when(httpService.postForm(anyString(), anyMap(), eq(CreateTemplateResponse.class)))
        .thenReturn(expectedResponse);

    // Act
    CreateTemplateResponse response = client.createTemplate(template);

    // Assert
    assertEquals("success", response.getStatus());
    assertEquals("uploaded-media-id", template.getMediaId());
    assertEquals("https://example.com/uploaded-image.png", template.getMediaUrl());

    verify(httpService)
        .uploadMedia(
            eq("https://api.gupshup.io/wa/test-app-id/wa/media/v2"),
            eq(tempFile),
            eq(UploadMediaResponse.class));
    verify(httpService)
        .postForm(
            eq("https://api.gupshup.io/wa/app/test-app-id/template"),
            anyMap(),
            eq(CreateTemplateResponse.class));
  }

  @Test
  void testCreateTemplateWithMediaNoUploadWhenAlreadyPopulated() {
    // Arrange
    ImageTemplate template =
        new ImageTemplate(
            "test_image_template",
            LanguageCode.ENGLISH,
            "Hello",
            TemplateCategory.MARKETING,
            appId,
            null,
            TemplateParameterFormat.POSITIONAL);
    template.setMediaId("existing-media-id");
    template.setMediaUrl("https://example.com/existing-image.png");

    File tempFile = new File("dummy.png");
    template.setMediaFile(tempFile);

    CreateTemplateResponse expectedResponse = new CreateTemplateResponse();
    expectedResponse.setStatus("success");
    GupshupTemplateDetails gupshupTemplate = new GupshupTemplateDetails();
    gupshupTemplate.setId("test-id");
    expectedResponse.setTemplate(gupshupTemplate);

    when(httpService.postForm(anyString(), anyMap(), eq(CreateTemplateResponse.class)))
        .thenReturn(expectedResponse);

    // Act
    CreateTemplateResponse response = client.createTemplate(template);

    // Assert
    assertEquals("success", response.getStatus());
    assertEquals("existing-media-id", template.getMediaId());
    assertEquals("https://example.com/existing-image.png", template.getMediaUrl());

    verify(httpService, org.mockito.Mockito.never())
        .uploadMedia(anyString(), any(File.class), any());
  }

  @Test
  void testCreateTemplateAsyncWithMediaFileUpload() throws Exception {
    // Arrange
    ImageTemplate template =
        new ImageTemplate(
            "test_image_template",
            LanguageCode.ENGLISH,
            "Hello",
            TemplateCategory.MARKETING,
            appId,
            null,
            TemplateParameterFormat.POSITIONAL);
    File tempFile = File.createTempFile("test-image", ".png");
    tempFile.deleteOnExit();
    template.setMediaFile(tempFile);

    UploadMediaResponse uploadResponse = new UploadMediaResponse();
    uploadResponse.setStatus("success");
    UploadMediaResponse.MediaDetails mediaDetails = new UploadMediaResponse.MediaDetails();
    mediaDetails.setFileName(tempFile.getName());
    mediaDetails.setId("uploaded-media-id");
    mediaDetails.setUrl("https://example.com/uploaded-image.png");
    uploadResponse.setMedia(mediaDetails);

    when(httpService.uploadMediaAsync(anyString(), any(File.class), eq(UploadMediaResponse.class)))
        .thenReturn(CompletableFuture.completedFuture(uploadResponse));

    CreateTemplateResponse expectedResponse = new CreateTemplateResponse();
    expectedResponse.setStatus("success");
    GupshupTemplateDetails gupshupTemplate = new GupshupTemplateDetails();
    gupshupTemplate.setId("test-id");
    gupshupTemplate.setElementName("test_image_template");
    expectedResponse.setTemplate(gupshupTemplate);

    when(httpService.postFormAsync(anyString(), anyMap(), eq(CreateTemplateResponse.class)))
        .thenReturn(CompletableFuture.completedFuture(expectedResponse));

    // Act
    CompletableFuture<CreateTemplateResponse> future = client.createTemplateAsync(template);
    CreateTemplateResponse response = future.get();

    // Assert
    assertEquals("success", response.getStatus());
    assertEquals("uploaded-media-id", template.getMediaId());
    assertEquals("https://example.com/uploaded-image.png", template.getMediaUrl());

    verify(httpService)
        .uploadMediaAsync(
            eq("https://api.gupshup.io/wa/test-app-id/wa/media/v2"),
            eq(tempFile),
            eq(UploadMediaResponse.class));
    verify(httpService)
        .postFormAsync(
            eq("https://api.gupshup.io/wa/app/test-app-id/template"),
            anyMap(),
            eq(CreateTemplateResponse.class));
  }

  @Test
  void testCreateTemplateWithCarouselMediaUpload() throws Exception {
    // Arrange
    CarouselTemplate template =
        new CarouselTemplate(
            "test_carousel",
            LanguageCode.ENGLISH,
            "Carousel body",
            TemplateCategory.MARKETING,
            appId,
            null,
            TemplateParameterFormat.POSITIONAL);

    CarouselCard card1 = new CarouselCard("Card 1", CarouselCard.CarouselCardHeaderType.IMAGE);
    File tempFile1 = File.createTempFile("test-card-1", ".png");
    tempFile1.deleteOnExit();
    card1.setMediaFile(tempFile1);
    card1.setButtons(
        List.of(new io.github.mrcloss.gupshup.domain.button.QuickReplyButton("Button 1")));

    CarouselCard card2 = new CarouselCard("Card 2", CarouselCard.CarouselCardHeaderType.IMAGE);
    File tempFile2 = File.createTempFile("test-card-2", ".png");
    tempFile2.deleteOnExit();
    card2.setMediaFile(tempFile2);
    card2.setButtons(
        List.of(new io.github.mrcloss.gupshup.domain.button.QuickReplyButton("Button 2")));

    ArrayList<CarouselCard> cards = new ArrayList<>(List.of(card1, card2));
    template.setCards(cards);

    UploadMediaResponse uploadResponse1 = new UploadMediaResponse();
    uploadResponse1.setStatus("success");
    UploadMediaResponse.MediaDetails details1 = new UploadMediaResponse.MediaDetails();
    details1.setId("media-id-1");
    details1.setUrl("https://example.com/media-1.png");
    uploadResponse1.setMedia(details1);

    UploadMediaResponse uploadResponse2 = new UploadMediaResponse();
    uploadResponse2.setStatus("success");
    UploadMediaResponse.MediaDetails details2 = new UploadMediaResponse.MediaDetails();
    details2.setId("media-id-2");
    details2.setUrl("https://example.com/media-2.png");
    uploadResponse2.setMedia(details2);

    when(httpService.uploadMedia(anyString(), eq(tempFile1), eq(UploadMediaResponse.class)))
        .thenReturn(uploadResponse1);
    when(httpService.uploadMedia(anyString(), eq(tempFile2), eq(UploadMediaResponse.class)))
        .thenReturn(uploadResponse2);

    CreateTemplateResponse expectedResponse = new CreateTemplateResponse();
    expectedResponse.setStatus("success");
    GupshupTemplateDetails gupshupTemplate = new GupshupTemplateDetails();
    gupshupTemplate.setId("test-id");
    expectedResponse.setTemplate(gupshupTemplate);

    when(httpService.postForm(anyString(), anyMap(), eq(CreateTemplateResponse.class)))
        .thenReturn(expectedResponse);

    // Act
    CreateTemplateResponse response = client.createTemplate(template);

    // Assert
    assertEquals("success", response.getStatus());
    assertEquals("media-id-1", card1.getMediaId());
    assertEquals("https://example.com/media-1.png", card1.getMediaUrl());
    assertEquals("media-id-2", card2.getMediaId());
    assertEquals("https://example.com/media-2.png", card2.getMediaUrl());

    verify(httpService)
        .uploadMedia(
            eq("https://api.gupshup.io/wa/test-app-id/wa/media/v2"),
            eq(tempFile1),
            eq(UploadMediaResponse.class));
    verify(httpService)
        .uploadMedia(
            eq("https://api.gupshup.io/wa/test-app-id/wa/media/v2"),
            eq(tempFile2),
            eq(UploadMediaResponse.class));
  }

  @Test
  void testOptIn() {
    // Arrange
    String appName = "test-app";
    String phoneNumber = "919876543210";
    OptInResponse expectedResponse = new OptInResponse();
    expectedResponse.setStatus("success");

    when(httpService.postForm(
            eq("https://api.gupshup.io/wa/api/v1/optedin/test-app"),
            eq(Map.of("user", phoneNumber)),
            eq(OptInResponse.class)))
        .thenReturn(expectedResponse);

    // Act
    OptInResponse response = client.optIn(appName, phoneNumber);

    // Assert
    assertEquals("success", response.getStatus());
    verify(httpService)
        .postForm(
            eq("https://api.gupshup.io/wa/api/v1/optedin/test-app"),
            eq(Map.of("user", phoneNumber)),
            eq(OptInResponse.class));
  }

  @Test
  void testOptInAsync() throws Exception {
    // Arrange
    String appName = "test-app";
    String phoneNumber = "919876543210";
    OptInResponse expectedResponse = new OptInResponse();
    expectedResponse.setStatus("success");

    when(httpService.postFormAsync(
            eq("https://api.gupshup.io/wa/api/v1/optedin/test-app"),
            eq(Map.of("user", phoneNumber)),
            eq(OptInResponse.class)))
        .thenReturn(CompletableFuture.completedFuture(expectedResponse));

    // Act
    CompletableFuture<OptInResponse> future = client.optInAsync(appName, phoneNumber);
    OptInResponse response = future.get();

    // Assert
    assertEquals("success", response.getStatus());
    verify(httpService)
        .postFormAsync(
            eq("https://api.gupshup.io/wa/api/v1/optedin/test-app"),
            eq(Map.of("user", phoneNumber)),
            eq(OptInResponse.class));
  }
}
