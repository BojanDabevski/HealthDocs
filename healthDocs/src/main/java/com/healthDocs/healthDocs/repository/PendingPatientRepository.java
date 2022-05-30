package com.healthDocs.healthDocs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthDocs.healthDocs.model.PendingPatient;
import com.healthDocs.healthDocs.model.User;

public interface PendingPatientRepository extends JpaRepository<PendingPatient,Long> {
   List<PendingPatient> findAll();
   Optional<PendingPatient> findByUsername(String s);
   Optional<PendingPatient> findById(Long Id);
}
