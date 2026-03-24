package com.flowmatic.flowmatic_back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flowmatic.flowmatic_back.entity.PaymentCondition;

public interface PaymentConditionRepository extends JpaRepository<PaymentCondition, Long> {

  public List<PaymentCondition> findByQuoteId(Long quoteId);

  public void deleteByQuoteId(Long quoteId);
}
