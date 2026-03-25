package com.flowmatic.flowmatic_back.service;

import java.time.Duration;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowmatic.flowmatic_back.dto.response.image.ImageResultResponse;
import com.flowmatic.flowmatic_back.exception.ImageSearchException;

@Service
public class ImageSearchService {

  private final RestClient restClient;
  private final ObjectMapper objectMapper;

  @Value("${unsplash.access-key}")
  private String accessKey;

  public ImageSearchService(ObjectMapper objectMapper) {
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setConnectTimeout(Duration.ofSeconds(10));
    factory.setReadTimeout(Duration.ofSeconds(15));
    this.restClient = RestClient.builder().requestFactory(factory).build();
    this.objectMapper = objectMapper;
  }

  public List<ImageResultResponse> search(String query, int perPage) {
    try {
      String raw = restClient.get()
          .uri(
              "https://api.unsplash.com/search/photos?query={q}&per_page={n}&orientation=landscape&content_filter=high",
              query, perPage)
          .header("Authorization", "Client-ID " + accessKey)
          .retrieve()
          .body(String.class);

      JsonNode results = objectMapper.readTree(raw).path("results");

      return StreamSupport.stream(results.spliterator(), false)
          .map(p -> new ImageResultResponse(
              p.path("id").asText(),
              p.path("urls").path("regular").asText(),
              p.path("urls").path("small").asText(),
              p.path("user").path("name").asText(),
              p.path("user").path("links").path("html").asText() + "?utm_source=flowmatic&utm_medium=referral"))
          .toList();

    } catch (Exception e) {
      throw new ImageSearchException("Erreur lors de la recherche d'images : " + e.getMessage());
    }
  }
}
