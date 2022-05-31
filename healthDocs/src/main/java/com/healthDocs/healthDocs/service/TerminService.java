package com.healthDocs.healthDocs.service;

import com.healthDocs.healthDocs.model.Termin;
import com.healthDocs.healthDocs.model.TerminType;
import com.healthDocs.healthDocs.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TerminService {
    List<Termin> listAll();
    void deleteById(Long Id);
    void createTermin(User doctor, User patient, LocalDateTime dateTime, String location, TerminType type);
    Termin getById(Long Id);
    List<Termin> findBySetByDoctorId(Long doctorId);
    List<Termin> findBySetForPatientId(Long patientId);

}