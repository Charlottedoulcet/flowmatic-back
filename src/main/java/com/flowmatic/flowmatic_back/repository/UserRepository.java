package com.flowmatic.flowmatic_back.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flowmatic.flowmatic_back.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  public Optional<User> findByEmail(String email);

  public Boolean existsByEmail(String email);

  public List<User> findByAgencyId(Long agencyId);

  public Optional<User> findByIdAndByAgencyId(Long id, Long agencyId);
}
