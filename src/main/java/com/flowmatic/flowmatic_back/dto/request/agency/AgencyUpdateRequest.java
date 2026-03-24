package com.flowmatic.flowmatic_back.dto.request.agency;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AgencyUpdateRequest {

  private String logoUrl;
  private String primaryColor;
  private String secondaryColor;
  private String termsAndConditions;

}
