package com.flowmatic.flowmatic_back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flowmatic.flowmatic_back.entity.QuoteInclusion;

public interface QuoteInclusionRepository extends JpaRepository<QuoteInclusion, Long> {

  public List<QuoteInclusion> findByQuoteId(Long quoteId);

  public void deleteByQuoteId(Long quoteId);
}
