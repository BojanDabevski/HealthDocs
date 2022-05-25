package com.healthDocs.healthDocs.service;

import com.healthDocs.healthDocs.model.Recept;
import com.healthDocs.healthDocs.model.Termin;
import com.healthDocs.healthDocs.model.User;

import java.util.List;

public interface ReceptService {
    List<Recept> listAll();
    void deleteById(Long Id);
    void createRecept(User doctor, User patient,Termin termin,String amount,String nameOfDrug,String genericNameOfDrug,String nalog, String upat);
    List<Recept> findBySetByDoctorId(Long doctorId);
    List<Recept> findBySetForPatientId(Long patientId);
    List<Recept> findBySetForTerminId(Long terminId);
}
