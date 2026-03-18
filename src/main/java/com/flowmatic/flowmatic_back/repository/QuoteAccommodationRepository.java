package com.flowmatic.flowmatic_back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flowmatic.flowmatic_back.entity.QuoteAccommodation;

public interface QuoteAccommodationRepository extends JpaRepository<QuoteAccommodation, Long> {

  public List<QuoteAccommodation> findByQuoteId(Long quoteId);

  public void deleteByQuoteId(Long quoteId);

}
