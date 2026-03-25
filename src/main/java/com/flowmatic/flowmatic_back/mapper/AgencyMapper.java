package com.flowmatic.flowmatic_back.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.flowmatic.flowmatic_back.dto.request.agency.AgencyUpdateRequest;
import com.flowmatic.flowmatic_back.dto.response.agency.AgencyResponse;
import com.flowmatic.flowmatic_back.entity.Agency;

@Mapper(componentModel = "spring")
public interface AgencyMapper {

  AgencyResponse toResponse(Agency agency);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "name", ignore = true)
  @Mapping(target = "users", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntity(@MappingTarget Agency agency, AgencyUpdateRequest request);
}
