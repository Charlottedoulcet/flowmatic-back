package com.flowmatic.flowmatic_back.dto.response.quote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteInclusionResponse {
  private Long id;
  private String description;
  private Boolean included;
}
