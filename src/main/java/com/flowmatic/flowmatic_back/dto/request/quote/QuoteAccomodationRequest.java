package com.flowmatic.flowmatic_back.dto.request.quote;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuoteAccomodationRequest {

  @NotBlank(message = "Le nom de l'hébergement est obligatoire")
  private String name;

  @NotBlank(message = "La localisation est obligatoire")
  private String location;

  private String rating;
  private String description;

  @NotNull
  @Min(value = 1)
  private Integer displayOrder;

}
