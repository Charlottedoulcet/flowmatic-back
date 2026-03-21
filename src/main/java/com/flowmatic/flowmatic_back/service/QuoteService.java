package com.flowmatic.flowmatic_back.service;

import java.util.List;

import com.flowmatic.flowmatic_back.dto.request.quote.QuoteCreateRequest;
import com.flowmatic.flowmatic_back.dto.request.quote.QuoteUpdateRequest;
import com.flowmatic.flowmatic_back.dto.response.quote.QuoteListResponse;
import com.flowmatic.flowmatic_back.dto.response.quote.QuoteResponse;
import com.flowmatic.flowmatic_back.entity.enums.QuoteStatus;

public interface QuoteService {

  public List<QuoteListResponse> getAllByAgency(String userEmail);

  public QuoteListResponse updateStatus(Long quoteId, QuoteStatus newStatus, String userEmail);

  public QuoteResponse createQuote(QuoteCreateRequest request, String userEmail);

  public QuoteResponse getQuoteById(Long id, String userEmail);

  public QuoteResponse updateQuote(Long id, QuoteUpdateRequest request, String userEmail);

  public void deleteQuote(Long id, String userEmail);

}
