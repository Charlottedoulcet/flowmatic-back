package com.flowmatic.flowmatic_back.dto.response.image;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageResultResponse {

  private String id;
  private String url;
  private String thumb;
  private String photographer;
  private String photographerUrl;

}