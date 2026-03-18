package com.flowmatic.flowmatic_back.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flowmatic.flowmatic_back.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  public Optional<User> findByUsername(String username);

  public Boolean existsByUsername(String username);

  public List<User> findByAgencyId(Long agencyId);
}
