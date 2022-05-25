package com.healthDocs.healthDocs.repository;

import com.healthDocs.healthDocs.model.Recept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceptRepository extends JpaRepository<Recept,Long> {
    List<Recept> findAll();
}
