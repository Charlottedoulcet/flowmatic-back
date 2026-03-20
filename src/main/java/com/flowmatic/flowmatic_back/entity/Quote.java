package com.flowmatic.flowmatic_back.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.flowmatic.flowmatic_back.entity.enums.QuoteStatus;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quotes")
@Data
@NoArgsConstructor
public class Quote {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "agency_id", nullable = false)
  private Agency agency;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", nullable = true)
  private User createdBy;

  @Column(name = "reference_number", length = 50)
  private String referenceNumber;

  @Column(length = 255)
  private String title;

  @Column(name = "client_name", length = 255)
  private String clientName;

  @Column(name = "client_email", length = 255)
  @Email
  private String clientEmail;

  @Column(name = "participant_count")
  private Integer participantCount;

  @Column(length = 255)
  private String destination;

  @Column(name = "travel_wishes", columnDefinition = "TEXT")
  private String travelWishes;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Column(name = "price_per_person", precision = 10, scale = 2)
  private BigDecimal pricePerPerson;

  @Column(length = 3)
  private String currency = "EUR";

  @Column(name = "cover_image_url", length = 500)
  private String coverImageUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private QuoteStatus status = QuoteStatus.PENDING;

  @OneToMany(mappedBy = "quote", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<QuoteDay> days = new ArrayList<>();

  @OneToMany(mappedBy = "quote", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<QuoteInclusion> inclusions = new ArrayList<>();

  @OneToMany(mappedBy = "quote", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PaymentCondition> paymentConditions = new ArrayList<>();

  @OneToMany(mappedBy = "quote", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<QuoteSupplement> supplements = new ArrayList<>();

  @OneToMany(mappedBy = "quote", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<QuoteAccommodation> accommodations = new ArrayList<>();

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

}
