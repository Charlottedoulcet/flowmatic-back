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
public class QuoteCreateRequest {

  @NotBlank(message = "Le titre est obligatoire")
  private String title;

  @NotBlank(message = "Le nom du client est obligatoire")
  private String clientName;

  private String clientEmail;

  @NotNull(message = "Le nombre de participants est obligatoire")
  @Positive(message = "Le nombre de participants doit être > 0")
  private Integer participantCount;

  @NotBlank(message = "La destination est obligatoire")
  private String destination;

  private String travelWishes;

  @NotNull(message = "La date de départ est obligatoire")
  private LocalDate startDate;

  @NotNull(message = "La date de retour est obligatoire")
  private LocalDate endDate;

  @NotNull(message = "Le prix par personne est obligatoire")
  @Positive(message = "Le prix doit être > 0")
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
  private List<QuoteAccomodationRequest> accomodations;
}
