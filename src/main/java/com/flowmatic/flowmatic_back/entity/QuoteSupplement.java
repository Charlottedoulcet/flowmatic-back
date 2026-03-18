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
@Table(name = "quote_supplements")
@Data
@NoArgsConstructor
public class QuoteSupplement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "quote_id", nullable = false)
  private Quote quote;

  @Column(length = 500)
  private String description;

  @Column(name = "price_per_person", precision = 10, scale = 2)
  private BigDecimal pricePerPerson;

  @Column(name = "total_price", precision = 10, scale = 2)
  private BigDecimal totalPrice;

  @Column(length = 3)
  private String currency;

}
