package com.flowmatic.flowmatic_back.service;

import com.flowmatic.flowmatic_back.dto.request.agency.AgencyUpdateRequest;
import com.flowmatic.flowmatic_back.dto.response.agency.AgencyResponse;

public interface AgencyService {

  AgencyResponse getAgency(String userEmail);

  AgencyResponse updateAgency(AgencyUpdateRequest request, String userEmail);
}
