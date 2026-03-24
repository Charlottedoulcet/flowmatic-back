package com.flowmatic.flowmatic_back.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.flowmatic.flowmatic_back.dto.request.quote.QuoteCreateRequest;
import com.flowmatic.flowmatic_back.dto.request.quote.QuoteUpdateRequest;
import com.flowmatic.flowmatic_back.dto.request.quote.StatusUpdateRequest;
import com.flowmatic.flowmatic_back.dto.response.quote.ExtractionResponse;
import com.flowmatic.flowmatic_back.dto.response.quote.QuoteListResponse;
import com.flowmatic.flowmatic_back.dto.response.quote.QuoteResponse;
import com.flowmatic.flowmatic_back.service.PdfExtractionService;
import com.flowmatic.flowmatic_back.service.QuoteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

  private final QuoteService quoteService;
  private final PdfExtractionService pdfExtractionService;

  public QuoteController(QuoteService quoteService, PdfExtractionService pdfExtractionService) {
    this.quoteService = quoteService;
    this.pdfExtractionService = pdfExtractionService;
  }

  @GetMapping
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<List<QuoteListResponse>> getAllQuotes(@AuthenticationPrincipal String userEmail) {

    return ResponseEntity.ok(quoteService.getAllByAgency(userEmail));
  }

  @PatchMapping("/{id}/status")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<QuoteListResponse> updateStatus(
      @PathVariable Long id,
      @Valid @RequestBody StatusUpdateRequest request,
      @AuthenticationPrincipal String userEmail) {

    return ResponseEntity.ok(quoteService.updateStatus(id, request.getStatus(), userEmail));
  }

  @PostMapping
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<QuoteResponse> createQuote(
      @Valid @RequestBody QuoteCreateRequest request,
      @AuthenticationPrincipal String userEmail) {

    QuoteResponse created = quoteService.createQuote(request, userEmail);

    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<QuoteResponse> getQuoteById(
      @PathVariable Long id,
      @AuthenticationPrincipal String userEmail) {

    return ResponseEntity.ok(quoteService.getQuoteById(id, userEmail));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<QuoteResponse> updateQuote(
      @PathVariable Long id,
      @Valid @RequestBody QuoteUpdateRequest request,
      @AuthenticationPrincipal String userEmail) {

    return ResponseEntity.ok(quoteService.updateQuote(id, request, userEmail));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<Void> deleteQuote(
      @PathVariable Long id,
      @AuthenticationPrincipal String userEmail) {

    quoteService.deleteQuote(id, userEmail);
    return ResponseEntity.noContent().build();
  }

  @PostMapping(value = "/extract", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ResponseEntity<ExtractionResponse> extractFromPdf(
      @RequestParam("file") MultipartFile file,
      @AuthenticationPrincipal String userEmail) {
    ExtractionResponse extracted = pdfExtractionService.extractFromPdf(file);
    return ResponseEntity.ok(extracted);
  }
}
