package com.flowmatic.flowmatic_back.dto.response.quote;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.flowmatic.flowmatic_back.entity.enums.QuoteStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuoteResponse {
  private Long id;
  private String referenceNumber;
  private String title;
  private String clientName;
  private String clientEmail;
  private Integer participantCount;
  private String destination;
  private String travelWishes;
  private LocalDate startDate;
  private LocalDate endDate;
  private BigDecimal pricePerPerson;
  private String currency;
  private String coverImageUrl;
  private QuoteStatus status;

  private Long createdById;
  private String createdByName;

  private List<QuoteDayResponse> days;
  private List<QuoteInclusionResponse> inclusions;
  private List<PaymentConditionResponse> paymentConditions;
  private List<QuoteSupplementResponse> supplements;
  private List<QuoteAccomodationResponse> accommodations;
}
