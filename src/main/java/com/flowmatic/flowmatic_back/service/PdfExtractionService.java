package com.flowmatic.flowmatic_back.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowmatic.flowmatic_back.dto.response.quote.ExtractionResponse;
import com.flowmatic.flowmatic_back.exception.ExtractionFailedException;

@Service
public class PdfExtractionService {

  private final RestClient restClient;
  private final ObjectMapper objectMapper;

  @Value("${ANTHROPIC_API_KEY}")
  private String apiKey;

  private static final String MODEL = "claude-sonnet-4-6";
  private static final String API_URL = "https://api.anthropic.com/v1/messages";

  public PdfExtractionService(ObjectMapper objectMapper) {
    this.restClient = RestClient.create();
    this.objectMapper = objectMapper;
  }

  public ExtractionResponse extractFromPdf(MultipartFile file) {
    try {
      byte[] pdfBytes = file.getBytes();
      String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);

      String prompt = new ClassPathResource("prompts/extraction-prompt.txt")
          .getContentAsString(StandardCharsets.UTF_8);

      Map<String, Object> requestBody = Map.of(
          "model", MODEL,
          "max_tokens", 16000,
          "messages", List.of(
              Map.of("role", "user", "content", List.of(
                  Map.of(
                      "type", "document",
                      "source", Map.of(
                          "type", "base64",
                          "media_type", "application/pdf",
                          "data", base64Pdf
                      )
                  ),
                  Map.of("type", "text", "text", prompt)
              ))
          )
      );

      String rawResponse = restClient.post()
          .uri(API_URL)
          .header("x-api-key", apiKey)
          .header("anthropic-version", "2023-06-01")
          .header("Content-Type", "application/json")
          .body(requestBody)
          .retrieve()
          .body(String.class);

      JsonNode responseNode = objectMapper.readTree(rawResponse);
      String extractedText = responseNode
          .path("content")
          .get(0)
          .path("text")
          .asText();

      String cleanJson = extractedText.trim();
      if (cleanJson.startsWith("```")) {
        cleanJson = cleanJson.replaceAll("^```json\\s*", "").replaceAll("\\s*```$", "");
      }

      return objectMapper.readValue(cleanJson, ExtractionResponse.class);

    } catch (IOException e) {
      throw new ExtractionFailedException("Échec de l'extraction PDF : " + e.getMessage());
    }
  }
}
