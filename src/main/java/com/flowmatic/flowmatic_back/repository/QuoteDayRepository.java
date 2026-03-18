package com.flowmatic.flowmatic_back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flowmatic.flowmatic_back.entity.QuoteDay;

public interface QuoteDayRepository extends JpaRepository<QuoteDay, Long> {

  public List<QuoteDay> findByQuoteIdOrderByDayNumber(Long quoteId);

  public void deleteByQuoteId(Long quoteId);
}
