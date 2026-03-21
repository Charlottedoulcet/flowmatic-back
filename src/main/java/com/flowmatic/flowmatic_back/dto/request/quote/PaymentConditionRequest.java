package com.flowmatic.flowmatic_back.dto.request.quote;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentConditionRequest {

  @NotBlank(message = "La desription est obligatoire")
  private String description;

  @NotNull(message = "Le pourcentage est obligatoire")
  @DecimalMin(value = "0.01", message = "Le pourcentage doit être > 0")
  @DecimalMax(value = "100.00", message = "Le pourcentage ne peut pas dépasser 100")
  private BigDecimal percentage;

  @NotBlank(message = "La date d'échéance est obligatoire")
  private String dueDateDescription;

}
