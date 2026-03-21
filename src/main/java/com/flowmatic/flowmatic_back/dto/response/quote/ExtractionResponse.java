package com.flowmatic.flowmatic_back.dto.response.quote;

import java.math.BigDecimal;
import java.util.List;

import com.flowmatic.flowmatic_back.dto.request.quote.PaymentConditionRequest;
import com.flowmatic.flowmatic_back.dto.request.quote.QuoteAccomodationRequest;
import com.flowmatic.flowmatic_back.dto.request.quote.QuoteDayRequest;
import com.flowmatic.flowmatic_back.dto.request.quote.QuoteInclusionRequest;
import com.flowmatic.flowmatic_back.dto.request.quote.QuoteSupplementRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtractionResponse {
  private String title;
  private String clientName;
  private String destination;
  private String travelWishes;
  private String startDate;
  private String endDate;
  private BigDecimal pricePerPerson;
  private String currency;

  private List<QuoteDayRequest> days;
  private List<QuoteInclusionRequest> inclusions;
  private List<PaymentConditionRequest> paymentConditions;
  private List<QuoteSupplementRequest> supplements;
  private List<QuoteAccomodationRequest> accommodations;
}
