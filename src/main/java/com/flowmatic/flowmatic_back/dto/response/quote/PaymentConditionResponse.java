package com.flowmatic.flowmatic_back.dto.response.quote;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentConditionResponse {
  private Long id;
  private String description;
  private BigDecimal percentage;
  private String dueDateDescription;
}
