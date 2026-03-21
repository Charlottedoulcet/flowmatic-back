package com.flowmatic.flowmatic_back.dto.response.quote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteAccomodationResponse {
  private Long id;
  private String name;
  private String location;
  private String rating;
  private String description;
  private Integer displayOrder;

}
