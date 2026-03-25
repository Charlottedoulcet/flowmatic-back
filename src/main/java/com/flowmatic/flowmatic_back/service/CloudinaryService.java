package com.flowmatic.flowmatic_back.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.flowmatic.flowmatic_back.exception.CloudinaryUploadException;

@Service
public class CloudinaryService {

  @Value("${cloudinary.cloud-name}")
  private String cloudName;

  @Value("${cloudinary.api-key}")
  private String apiKey;

  @Value("${cloudinary.api-secret}")
  private String apiSecret;

  public String uploadImage(MultipartFile file) {
    try {
      Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
          "cloud_name", cloudName,
          "api_key", apiKey,
          "api_secret", apiSecret
      ));

      @SuppressWarnings("unchecked")
      Map<String, Object> result = cloudinary.uploader().upload(
          file.getBytes(),
          ObjectUtils.asMap("folder", "Final Project Flowmatic/logos")
      );

      return (String) result.get("secure_url");

    } catch (Exception e) {
      throw new CloudinaryUploadException("Erreur upload image : " + e.getMessage());
    }
  }

}
