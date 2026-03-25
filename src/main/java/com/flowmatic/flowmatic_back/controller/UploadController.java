package com.flowmatic.flowmatic_back.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.flowmatic.flowmatic_back.service.CloudinaryService;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

  private final CloudinaryService cloudinaryService;

  public UploadController(CloudinaryService cloudinaryService) {
    this.cloudinaryService = cloudinaryService;
  }

  @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Map<String, String>> uploadImage(
      @RequestParam("file") MultipartFile file) {
    String url = cloudinaryService.uploadImage(file);
    return ResponseEntity.ok(Map.of("url", url));

  }
}
