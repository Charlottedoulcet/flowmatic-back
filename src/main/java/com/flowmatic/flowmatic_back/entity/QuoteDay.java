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
@Table(name = "quote_days")
@Data
@NoArgsConstructor
public class QuoteDay {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "quote_id", nullable = false)
  private Quote quote;

  @Column(name = "day_number")
  private Integer dayNumber;

  @Column(length = 100)
  private String date;

  @Column(length = 255)
  private String title;

  @Column(length = 500)
  private String summary;

  @Column(name = "night_location", length = 255)
  private String nightLocation;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "transport_duration", length = 100)
  private String transportDuration;

  @Column(name = "accommodation_name", length = 255)
  private String accommodationName;

  @Column(name = "accommodation_location", length = 255)
  private String accommodationLocation;

  @Column(name = "room_type", length = 255)
  private String roomType;

  @Column(length = 500)
  private String meals;

  @Column(name = "included_in_day", columnDefinition = "TEXT")
  private String includedInDay;

}
