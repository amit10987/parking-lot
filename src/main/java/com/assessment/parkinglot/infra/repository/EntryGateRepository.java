package com.assessment.parkinglot.infra.repository;

import com.assessment.parkinglot.domain.entity.EntryGate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryGateRepository extends JpaRepository<EntryGate, Long> {
}
