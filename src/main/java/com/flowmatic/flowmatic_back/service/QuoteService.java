package com.flowmatic.flowmatic_back.service;

import java.util.List;

import com.flowmatic.flowmatic_back.dto.response.quote.QuoteListResponse;
import com.flowmatic.flowmatic_back.entity.enums.QuoteStatus;

public interface QuoteService {

  public List<QuoteListResponse> getAllByAgency(String userEmail);

  public QuoteListResponse updateStatus(Long quoteId, QuoteStatus newStatus, String userEmail);

}
