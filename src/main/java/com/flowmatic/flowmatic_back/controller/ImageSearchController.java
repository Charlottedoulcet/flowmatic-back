package com.flowmatic.flowmatic_back.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flowmatic.flowmatic_back.dto.response.image.ImageResultResponse;
import com.flowmatic.flowmatic_back.service.ImageSearchService;

@RestController
@RequestMapping("/api/images")
public class ImageSearchController {

  private final ImageSearchService imageSearchService;

  public ImageSearchController(ImageSearchService imageSearchService) {
    this.imageSearchService = imageSearchService;
  }

  @GetMapping("/search")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<List<ImageResultResponse>> search(@RequestParam String query) {
    return ResponseEntity.ok(imageSearchService.search(query, 6));
  }

}
