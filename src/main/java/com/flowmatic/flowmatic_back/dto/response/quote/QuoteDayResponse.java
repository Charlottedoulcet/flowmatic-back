package com.flowmatic.flowmatic_back.dto.response.quote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteDayResponse {
  private Long id;
  private Integer dayNumber;
  private String date;
  private String title;
  private String summary;
  private String nightLocation;
  private String description;
  private String transportDuration;
  private String accommodationName;
  private String accommodationLocation;
  private String roomType;
  private String meals;
  private String includedInDay;
}
