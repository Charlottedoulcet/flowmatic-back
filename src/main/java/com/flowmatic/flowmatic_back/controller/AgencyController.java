package com.flowmatic.flowmatic_back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowmatic.flowmatic_back.dto.request.agency.AgencyUpdateRequest;
import com.flowmatic.flowmatic_back.dto.response.agency.AgencyResponse;
import com.flowmatic.flowmatic_back.service.AgencyService;

@RestController
@RequestMapping("/api/agency")
public class AgencyController {

  private final AgencyService agencyService;

  public AgencyController(AgencyService agencyService) {
    this.agencyService = agencyService;
  }

  @GetMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<AgencyResponse> getAgency(
      @AuthenticationPrincipal String userEmail) {
    return ResponseEntity.ok(agencyService.getAgency(userEmail));
  }

  @PutMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<AgencyResponse> updateAgency(
      @RequestBody AgencyUpdateRequest request,
      @AuthenticationPrincipal String userEmail) {
    return ResponseEntity.ok(agencyService.updateAgency(request, userEmail));
  }
}
