package com.flowmatic.flowmatic_back.dto.request;

import com.flowmatic.flowmatic_back.entity.enums.QuoteStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatusUpdateRequest {

  @NotNull(message = "Le statu est obligatoire")
  private QuoteStatus status;

}
