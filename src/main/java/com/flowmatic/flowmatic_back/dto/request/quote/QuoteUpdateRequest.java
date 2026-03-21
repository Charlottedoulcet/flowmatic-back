package com.flowmatic.flowmatic_back.dto.request.quote;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuoteUpdateRequest {

  @NotBlank(message = "Le titre est obligatoire")
  private String title;

  @NotBlank(message = "Le nom du client est obligatoire")
  private String clientName;

  private String clientEmail;

  @NotNull
  @Positive
  private Integer participantCount;

  @NotBlank
  private String destination;

  private String travelWishes;

  @NotNull
  private LocalDate startDate;

  @NotNull
  private LocalDate endDate;

  @NotNull
  @Positive
  private BigDecimal pricePerPerson;

  private String currency = "EUR";
  private String coverImageUrl;

  @Valid
  private List<QuoteDayRequest> days;

  @Valid
  private List<QuoteInclusionRequest> inclusions;

  @Valid
  private List<PaymentConditionRequest> paymentConditions;

  @Valid
  private List<QuoteSupplementRequest> supplements;

  @Valid
  private List<QuoteAccomodationRequest> accommodations;
}
