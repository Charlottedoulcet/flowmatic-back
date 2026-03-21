package com.flowmatic.flowmatic_back.dto.request.quote;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuoteInclusionRequest {

  @NotBlank(message = "La description est obligatoire")
  private String description;

  @NotNull(message = "Le champ 'inclus' est obligatoire")
  private Boolean included;
}
