package com.flowmatic.flowmatic_back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flowmatic.flowmatic_back.entity.QuoteSupplement;

public interface QuoteSupplementRepository extends JpaRepository<QuoteSupplement, Long> {

  public List<QuoteSupplement> findByQuoteId(Long quoteId);

  public void deleteByQuoteId(Long quoteId);
}
