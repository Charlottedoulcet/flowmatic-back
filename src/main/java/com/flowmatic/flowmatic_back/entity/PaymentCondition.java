package com.flowmatic.flowmatic_back.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_conditions")
@Data
@NoArgsConstructor
public class PaymentCondition {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "quote_id", nullable = false)
  private Quote quote;

  @Column(length = 500)
  private String description;

  @Column(precision = 5, scale = 2)
  private BigDecimal percentage;

  @Column(name = "due_date_description", length = 255)
  private String dueDateDescription;
}
