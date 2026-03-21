package com.flowmatic.flowmatic_back.dto.request.quote;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuoteDayRequest {

  @NotNull(message = "Le numéro de jour est obligatoire")
  @Min(value = 1, message = "Le numéro de jour doit être >= 1")
  private Integer dayNumber;

  @NotBlank(message = "La date est obligatoire")
  private String date;

  @NotBlank(message = "Le titre du jour est obligatoire")
  private String title;

  private String summary;
  private String nightLocation;
  private String description;
  private String transportDuration;
  private String accommodationName;
  private String accommodationLocation;
  private String roomType;
  private String meals;
  private String includedInDay;

}
