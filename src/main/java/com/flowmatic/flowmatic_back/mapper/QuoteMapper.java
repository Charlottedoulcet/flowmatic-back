package com.flowmatic.flowmatic_back.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.flowmatic.flowmatic_back.dto.request.quote.QuoteCreateRequest;
import com.flowmatic.flowmatic_back.dto.request.quote.QuoteUpdateRequest;
import com.flowmatic.flowmatic_back.dto.response.quote.PaymentConditionResponse;
import com.flowmatic.flowmatic_back.dto.response.quote.QuoteAccomodationResponse;
import com.flowmatic.flowmatic_back.dto.response.quote.QuoteDayResponse;
import com.flowmatic.flowmatic_back.dto.response.quote.QuoteInclusionResponse;
import com.flowmatic.flowmatic_back.dto.response.quote.QuoteListResponse;
import com.flowmatic.flowmatic_back.dto.response.quote.QuoteResponse;
import com.flowmatic.flowmatic_back.dto.response.quote.QuoteSupplementResponse;
import com.flowmatic.flowmatic_back.entity.Agency;
import com.flowmatic.flowmatic_back.entity.PaymentCondition;
import com.flowmatic.flowmatic_back.entity.Quote;
import com.flowmatic.flowmatic_back.entity.QuoteAccommodation;
import com.flowmatic.flowmatic_back.entity.QuoteDay;
import com.flowmatic.flowmatic_back.entity.QuoteInclusion;
import com.flowmatic.flowmatic_back.entity.QuoteSupplement;
import com.flowmatic.flowmatic_back.entity.User;

@Mapper(componentModel = "spring")
public interface QuoteMapper {

  @Mapping(target = "createdById", source = "createdBy.id")
  @Mapping(target = "createdBy", expression = "java(quote.getCreatedBy() != null ? quote.getCreatedBy().getFirstName() + \" \" + quote.getCreatedBy().getLastName() : null)")
  QuoteListResponse toListResponse(Quote quote);

  @Mapping(target = "createdById", source = "createdBy.id")
  @Mapping(target = "createdByName", expression = "java(quote.getCreatedBy() != null ? quote.getCreatedBy().getFirstName() + \" \" + quote.getCreatedBy().getLastName() : null)")
  QuoteResponse toResponse(Quote quote);

  @Mapping(source = "agency", target = "agency")
  @Mapping(source = "createdBy", target = "createdBy")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Quote toEntity(QuoteCreateRequest request, Agency agency, User createdBy);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "agency", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "days", ignore = true)
  @Mapping(target = "inclusions", ignore = true)
  @Mapping(target = "paymentConditions", ignore = true)
  @Mapping(target = "supplements", ignore = true)
  @Mapping(target = "accommodations", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  void updateEntity(@MappingTarget Quote quote, QuoteUpdateRequest request);

  QuoteDayResponse toDayResponse(QuoteDay day);

  QuoteInclusionResponse toInclusionResponse(QuoteInclusion inclusion);

  PaymentConditionResponse toPaymentConditionResponse(PaymentCondition pc);

  QuoteSupplementResponse toSupplementResponse(QuoteSupplement supplement);

  QuoteAccomodationResponse toAccommodationResponse(QuoteAccommodation accommodation);

  @AfterMapping
  default void linkChildren(@MappingTarget Quote quote) {
    if (quote.getDays() != null)
      quote.getDays().forEach(day -> day.setQuote(quote));
    if (quote.getInclusions() != null)
      quote.getInclusions().forEach(i -> i.setQuote(quote));
    if (quote.getPaymentConditions() != null)
      quote.getPaymentConditions().forEach(pc -> pc.setQuote(quote));
    if (quote.getSupplements() != null)
      quote.getSupplements().forEach(s -> s.setQuote(quote));
    if (quote.getAccommodations() != null)
      quote.getAccommodations().forEach(s -> s.setQuote(quote));

  }
}
