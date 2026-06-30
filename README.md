# Gupshup WhatsApp Template Client

[![Build Status](https://github.com/mrcl-oss/gupshup-template-client/actions/workflows/ci.yml/badge.svg)](https://github.com/mrcl-oss/gupshup-template-client/actions/workflows/ci.yml)
[![Publish Status](https://github.com/mrcl-oss/gupshup-template-client/actions/workflows/publish.yml/badge.svg)](https://github.com/mrcl-oss/gupshup-template-client/actions/workflows/publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.mrcl-oss/gupshup-template-client.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.mrcl-oss/gupshup-template-client)
[![License](https://img.shields.io/badge/license-Apache_2.0-blue)](LICENSE)
[![Java Version](https://img.shields.io/badge/Java-17%2B-blue)](https://www.oracle.com/java/technologies/downloads/)

A modern, robust, and type-safe Java client for managing and sending **Gupshup WhatsApp Templates**. Designed following Domain-Driven Design (DDD) principles with built-in validation, synchronous and asynchronous communication, and modern Java features.

---

## 🚀 Key Features

* **Type-Safe Domain Models:** Strong validation rules executed locally before making any network calls.
* **Complete WhatsApp Template Support:**
  * **Text:** Simple and rich text templates with positional variable bindings.
  * **Media:** Image, Video, Document, and GIF templates.
  * **Interactive:** Catalog, Product, and Location templates.
  * **Advanced:** Authentication (OTP) with copy code buttons, and Carousel templates.
* **Flexible Button Types:** Quick Reply, URL (Static & Dynamic), Phone Call, OTP, Copy Code, Pay Now, and MPM.
* **Dual Request API:** Fully supports both synchronous and asynchronous (`CompletableFuture`) network calls.
* **No Heavy Dependencies:** Built using Java 17 HttpClient and Jackson for lightweight and high-performance JSON serialization.

---

## 📦 Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.mrcl-oss</groupId>
    <artifactId>gupshup-template-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## 🛠️ Usage Examples

### 1. Initialize the Client

Configure the `GupshupClient` using its fluent builder:

```java
import io.github.mrcloss.gupshup.client.GupshupClient;

GupshupClient client = GupshupClient.builder()
        .appId("YOUR_APP_ID")
        .apiKey("YOUR_API_KEY")
        .build();
```

### 2. Create a Text Template with Quick Reply Buttons

You can build and submit templates to the Gupshup registry with local validation:

```java
import io.github.mrcloss.gupshup.domain.enums.LanguageCode;
import io.github.mrcloss.gupshup.domain.enums.TemplateCategory;
import io.github.mrcloss.gupshup.domain.enums.TemplateParameterFormat;
import io.github.mrcloss.gupshup.domain.template.TextTemplate;
import io.github.mrcloss.gupshup.domain.button.QuickReplyButton;
import io.github.mrcloss.gupshup.infrastructure.dto.response.CreateTemplateResponse;
import java.util.List;

// Define a quick reply button
QuickReplyButton replyButton = new QuickReplyButton("Opt-out of messages");

// Build the template
TextTemplate template = new TextTemplate(
        "welcome_marketing_template",       // Unique element name
        LanguageCode.ENGLISH,              // Template language
        "Hello {{1}}! Welcome to our store. We have great discounts for you.", // Body text
        TemplateCategory.MARKETING,        // Category
        "YOUR_APP_ID",                     // Gupshup App ID
        List.of("welcome", "promo"),       // Optional tags
        TemplateParameterFormat.POSITIONAL // POSITIONAL or NAMED parameters
);

// Add details (variables example, header, footer, buttons)
template.setVariableExamples(List.of("John"));
template.setHeader("Special Offer!");
template.setFooter("T&C Apply.");
template.setButtons(List.of(replyButton));

// Submit the template
CreateTemplateResponse response = client.createTemplate(template);
if ("success".equals(response.getStatus())) {
    System.out.println("Template submitted successfully! ID: " + response.getTemplate().getId());
} else {
    System.err.println("API error: " + response.getError());
}
```

### 3. Send a WhatsApp Message using a Template

Send messages by mapping parameters and header/body payloads:

```java
import io.github.mrcloss.gupshup.domain.message.ImagePayload;
import io.github.mrcloss.gupshup.infrastructure.dto.request.send.SendTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.response.SendTemplateResponse;
import java.util.List;

// Prepare media headers if required (e.g. image template)
ImagePayload imagePayload = new ImagePayload("https://example.com/banner.png", null);

SendTemplateRequest request = new SendTemplateRequest(
        "34977900141",                      // Sender phone number
        "34689395507",                      // Destination phone number
        "template_welcome_image",           // Template name
        "template-namespace-uuid",          // Template namespace
        List.of("John"),                    // Parameters for placeholders
        imagePayload                        // Media payload (or null if text-only)
);

SendTemplateResponse response = client.sendTemplate(request);
System.out.println("Message ID: " + response.getMessageId());
```

### 4. Send an Authentication (OTP) Template

To send an OTP verification code, use the specialized `SendAuthenticationTemplateRequest` class. This class automatically maps the authentication code to both the template body and the copy-code button:

```java
import io.github.mrcloss.gupshup.infrastructure.dto.request.send.SendAuthenticationTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.response.SendTemplateResponse;

SendAuthenticationTemplateRequest request = new SendAuthenticationTemplateRequest(
        "34977900141",                      // Sender phone number
        "34689395507",                      // Destination phone number
        "PruebasManhattan",                 // App name
        "9f8755b1-05a9-4443-ac68-80e5b749e96c", // Template ID
        "123456"                            // Verification code
);

SendTemplateResponse response = client.sendTemplate(request);
System.out.println("Message ID: " + response.getMessageId());
```

### 5. Send a GIF Template

You can send looping GIF animations (represented as `.mp4` loop videos by WhatsApp) using the specialized `SendGifTemplateRequest` and `GifPayload`:

```java
import io.github.mrcloss.gupshup.domain.message.GifPayload;
import io.github.mrcloss.gupshup.infrastructure.dto.request.send.SendGifTemplateRequest;
import io.github.mrcloss.gupshup.infrastructure.dto.response.SendTemplateResponse;
import java.util.List;

GifPayload gifPayload = new GifPayload("https://example.com/animation.gif", null);

SendGifTemplateRequest request = new SendGifTemplateRequest(
        "34977900141",                      // Sender phone number
        "34689395507",                      // Destination phone number
        "PruebasManhattan",                 // App name
        "gif-template-uuid",                // Template ID
        List.of("bodyParameter"),           // Parameters for body
        gifPayload                          // GIF payload
);

SendTemplateResponse response = client.sendTemplate(request);
System.out.println("Message ID: " + response.getMessageId());
```

---

## 🛠️ Local Development

We enforce high standards of code styling, formatting, and test quality.

### Code Style & Formatting

We use **Google Java Style Guide** formatting via **Spotless** and static checking via **Checkstyle**.

* **Format code automatically:**

    ```bash
    mvn spotless:apply
    ```

* **Verify style and formatting:**

    ```bash
    mvn clean verify
    ```

    *(Both formatting validations and code style compliance are validated automatically during the Maven `validate` lifecycle and checked in the CI/CD pipeline).*

### Test Suite

Run unit tests to verify the domain validation rules and API serialization behavior:

```bash
mvn clean test
```

---

## 📄 License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](LICENSE) file for details.
