package com.flowmatic.flowmatic_back.dto.request.quote;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuoteSupplementRequest {

  @NotBlank(message = "La description est obligatoire")
  private String description;

  private BigDecimal pricePerPerson;

  private BigDecimal totalPrice;

  private String currency = "EUR";
}
