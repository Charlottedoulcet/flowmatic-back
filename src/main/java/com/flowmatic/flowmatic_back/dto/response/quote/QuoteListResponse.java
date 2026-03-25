package com.flowmatic.flowmatic_back.dto.response.quote;

import java.math.BigDecimal;

import com.flowmatic.flowmatic_back.entity.enums.QuoteStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuoteListResponse {

  private Long id;
  private String referenceNumber;
  private String title;
  private String clientName;
  private String destination;
  private BigDecimal pricePerPerson;
  private QuoteStatus status;
  private Long createdById;
  private String createdBy;

}
