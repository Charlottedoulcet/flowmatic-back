package com.flowmatic.flowmatic_back.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowmatic.flowmatic_back.dto.request.quote.StatusUpdateRequest;
import com.flowmatic.flowmatic_back.dto.response.quote.QuoteListResponse;
import com.flowmatic.flowmatic_back.service.QuoteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

  private final QuoteService quoteService;

  public QuoteController(QuoteService quoteService) {
    this.quoteService = quoteService;
  }

  @GetMapping
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<List<QuoteListResponse>> getAllQuotes(@AuthenticationPrincipal String userEmail) {

    List<QuoteListResponse> quotes = quoteService.getAllByAgency(userEmail);
    return ResponseEntity.ok(quotes);
  }

  @PatchMapping("/{id}/status")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<QuoteListResponse> updateStatus(
    @PathVariable Long id,
    @Valid @RequestBody StatusUpdateRequest request,
    @AuthenticationPrincipal String userEmail) {

    QuoteListResponse updated = quoteService.updateStatus(id, request.getStatus(),userEmail);
    return ResponseEntity.ok(updated);
  }

}
