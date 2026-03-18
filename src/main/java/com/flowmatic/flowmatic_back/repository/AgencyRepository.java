package com.flowmatic.flowmatic_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flowmatic.flowmatic_back.entity.Agency;

public interface AgencyRepository extends JpaRepository<Agency, Long> {

}
