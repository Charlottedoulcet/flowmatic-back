package com.flowmatic.flowmatic_back.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.flowmatic.flowmatic_back.entity.Quote;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

  List<Quote> findByAgencyId(Long agencyId);

  List<Quote> findByAgencyIdAndCreatedById(Long agencyId, Long userId);

  Optional<Quote> findByIdAndAgencyId(Long id, Long agencyId);

  // Utilisé avant de supprimer un user — préserve les devis mais efface le lien
  @Modifying
  @Query("UPDATE Quote q SET q.createdBy = null WHERE q.createdBy.id = :userId")
  void nullifyCreatedBy(@Param("userId") Long userId);

}
