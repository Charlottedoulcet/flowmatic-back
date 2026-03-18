package com.flowmatic.flowmatic_back.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flowmatic.flowmatic_back.entity.Quote;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

  public List<Quote> findByAgencyId(Long agencyId);

  public List<Quote> findByAgencyIdAndCreatedById(Long agencyId, Long userId);

  public Optional<Quote> findByIdAndAgencyId(Long id, Long agencyId);

}
