package com.flowmatic.flowmatic_back.dto.response.quote;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteSupplementResponse {

  private Long id;
  private String description;
  private BigDecimal pricePerPerson;
  private BigDecimal totalPrice;
  private String currency;

}