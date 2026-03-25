package com.flowmatic.flowmatic_back.service;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.flowmatic.flowmatic_back.exception.BadRequestException;
import com.flowmatic.flowmatic_back.exception.CloudinaryUploadException;

import jakarta.annotation.PostConstruct;

@Service
public class CloudinaryService {

  private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
  private static final long MAX_IMAGE_SIZE_BYTES = 5 * 1024 * 1024; // 5 MB

  @Value("${cloudinary.cloud-name}")
  private String cloudName;

  @Value("${cloudinary.api-key}")
  private String apiKey;

  @Value("${cloudinary.api-secret}")
  private String apiSecret;

  private Cloudinary cloudinary;

  @PostConstruct
  public void init() {
    this.cloudinary = new Cloudinary(ObjectUtils.asMap(
        "cloud_name", cloudName,
        "api_key", apiKey,
        "api_secret", apiSecret));
  }

  public String uploadImage(MultipartFile file) {
    if (file.getContentType() == null || !ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
      throw new BadRequestException("Format non supporté. Utilisez JPG, PNG ou WebP.");
    }
    if (file.getSize() > MAX_IMAGE_SIZE_BYTES) {
      throw new BadRequestException("L'image ne doit pas dépasser 5 Mo.");
    }

    try {
      @SuppressWarnings("unchecked")
      Map<String, Object> result = cloudinary.uploader().upload(
          file.getBytes(),
          ObjectUtils.asMap("folder", "Final Project Flowmatic/logos"));

      return (String) result.get("secure_url");

    } catch (Exception e) {
      throw new CloudinaryUploadException("Erreur upload image : " + e.getMessage());
    }
  }
}
