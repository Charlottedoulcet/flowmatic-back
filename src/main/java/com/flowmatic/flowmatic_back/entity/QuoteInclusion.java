package com.flowmatic.flowmatic_back.entity;

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
@Table(name = "quote_inclusions")
@Data
@NoArgsConstructor
public class QuoteInclusion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "quote_id", nullable = false)
  private Quote quote;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private Boolean included;

}
