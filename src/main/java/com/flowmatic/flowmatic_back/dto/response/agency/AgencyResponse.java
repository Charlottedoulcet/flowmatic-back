package com.flowmatic.flowmatic_back.dto.response.agency;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AgencyResponse {

  private Long id;
  private String name;
  private String logoUrl;
  private String primaryColor;
  private String secondaryColor;
  private String termsAndConditions;

}
