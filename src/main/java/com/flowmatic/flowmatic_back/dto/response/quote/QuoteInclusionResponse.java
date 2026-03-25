package com.flowmatic.flowmatic_back.dto.response.quote;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuoteInclusionResponse {
  private Long id;
  private String description;
  private Boolean included;
}
