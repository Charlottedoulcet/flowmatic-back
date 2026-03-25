package com.flowmatic.flowmatic_back.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.flowmatic.flowmatic_back.entity.Quote;

import jakarta.persistence.LockModeType;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

  public List<Quote> findByAgencyId(Long agencyId);

  @Query("SELECT q FROM Quote q LEFT JOIN FETCH q.createdBy WHERE q.agency.id = :agencyId")
  List<Quote> findByAgencyIdWithCreatedBy(@Param("agencyId") Long agencyId);

  public List<Quote> findByAgencyIdAndCreatedById(Long agencyId, Long userId);

  public Optional<Quote> findByIdAndAgencyId(Long id, Long agencyId);

  public long countByAgencyId(Long agencyId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT COUNT(q) FROM Quote q WHERE q.agency.id = :agencyId")
  long countByAgencyIdLocked(@Param("agencyId") Long agencyId);

  @Modifying
  @Query("UPDATE Quote q SET q.createdBy = null WHERE q.createdBy.id = :userId")
  void nullifyCreatedBy(@Param("userId") Long userId);

}
