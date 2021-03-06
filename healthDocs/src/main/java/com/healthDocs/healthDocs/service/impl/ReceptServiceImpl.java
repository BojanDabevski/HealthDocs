package com.healthDocs.healthDocs.service.impl;

import com.healthDocs.healthDocs.model.Recept;
import com.healthDocs.healthDocs.model.User;
import com.healthDocs.healthDocs.repository.ReceptRepository;
import com.healthDocs.healthDocs.service.ReceptService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReceptServiceImpl implements ReceptService {
    private ReceptRepository receptRepository;

    public ReceptServiceImpl(ReceptRepository receptRepository) {
        this.receptRepository = receptRepository;
    }

    @Override
    public List <Recept> listAll() {
        return this.receptRepository.findAll();
    }

    @Override
    public void deleteById(Long Id) {
        this.receptRepository.deleteById(Id);
    }

    @Override
    public void createRecept(User doctor, User patient, LocalDateTime dateTime, String amount, String nameOfDrug, String genericNameOfDrug, String nalog, String upat) {
        this.receptRepository.save(new Recept(doctor, patient, dateTime, amount, nameOfDrug, genericNameOfDrug, nalog, upat));
    }


    @Override
    public List <Recept> findBySetByDoctorId(Long doctorId) {
        return this.receptRepository.findAll().stream().filter(x -> x.getDoctor().getId() == doctorId).collect(Collectors.toList());
    }

    @Override
    public List <Recept> findBySetForPatientId(Long patientId) {
        return this.receptRepository.findAll().stream().filter(x -> x.getPatient().getId() == patientId).collect(Collectors.toList());
    }

    @Override
    public Optional <Recept> findById(Long Id) {
        return this.receptRepository.findById(Id);
    }

    @Override
    public Recept save(Recept save) {
        return this.receptRepository.save(save);
    }
}
