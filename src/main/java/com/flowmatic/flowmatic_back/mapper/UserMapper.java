package com.flowmatic.flowmatic_back.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.flowmatic.flowmatic_back.dto.response.user.UserResponse;
import com.flowmatic.flowmatic_back.entity.User;
import com.flowmatic.flowmatic_back.entity.enums.Role;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStrings")
  UserResponse toResponse(User user);

  @Named("rolesToStrings")
  default List<String> rolesToStrings(Set<Role> roles) {
    if (roles == null) return List.of();
    return roles.stream()
        .map(role -> "ROLE_" + role.name())
        .collect(Collectors.toList());
  }
}
