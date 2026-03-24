package com.flowmatic.flowmatic_back.dto.response.quote;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConditionResponse {
  private Long id;
  private String description;
  private BigDecimal percentage;
  private String dueDateDescription;
}
