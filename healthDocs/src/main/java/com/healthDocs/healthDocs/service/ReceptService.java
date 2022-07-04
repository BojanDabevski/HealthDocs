package com.healthDocs.healthDocs.service;

import com.healthDocs.healthDocs.model.Recept;
import com.healthDocs.healthDocs.model.Termin;
import com.healthDocs.healthDocs.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReceptService {
    List <Recept> listAll();

    void deleteById(Long Id);

    Optional <Recept> findById(Long Id);

    Recept save(Recept save);

    void createRecept(User doctor, User patient, LocalDateTime dateTime, String amount, String nameOfDrug, String genericNameOfDrug, String nalog, String upat);

    List <Recept> findBySetByDoctorId(Long doctorId);

    List <Recept> findBySetForPatientId(Long patientId);
}
