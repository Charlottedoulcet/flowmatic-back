package com.flowmatic.flowmatic_back.mapper;

import org.springframework.stereotype.Component;

import com.flowmatic.flowmatic_back.dto.response.QuoteListResponse;
import com.flowmatic.flowmatic_back.entity.Quote;

@Component
public class QuoteMapper {

  public QuoteListResponse toListResponse(Quote quote) {
    Long createdById = quote.getCreatedBy().getId();
    String createdByName = quote.getCreatedBy().getFirstName() + " " + quote.getCreatedBy().getLastName();

    return new QuoteListResponse(
        quote.getId(),
        quote.getReferenceNumber(),
        quote.getTitle(),
        quote.getClientName(),
        quote.getDestination(),
        quote.getPricePerPerson(),
        quote.getStatus(),
        createdById,
        createdByName
    );
  }

}
