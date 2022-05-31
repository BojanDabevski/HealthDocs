package com.healthDocs.healthDocs.service.impl;

import com.healthDocs.healthDocs.model.Termin;
import com.healthDocs.healthDocs.model.TerminType;
import com.healthDocs.healthDocs.model.User;
import com.healthDocs.healthDocs.repository.TerminRepository;
import com.healthDocs.healthDocs.service.TerminService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TerminServiceImpl implements TerminService {
    private TerminRepository terminRepository;
    public TerminServiceImpl(TerminRepository terminRepository){
        this.terminRepository=terminRepository;
    }
    @Override
    public List<Termin> listAll() {
        return this.terminRepository.findAll();
    }

    @Override
    public void deleteById(Long Id) {
        this.terminRepository.deleteById(Id);
    }

    @Override
    public void createTermin(User doctor, User patient, LocalDateTime dateTime, String location, TerminType type) {
        this.terminRepository.save(new Termin(doctor,patient,dateTime,location,type));
    }

    @Override
    public List<Termin> findBySetByDoctorId(Long doctorId) {
        return this.terminRepository.findAll().stream().filter(x->x.getDoctor().getId()==doctorId).collect(Collectors.toList());
    }

    @Override
    public List<Termin> findBySetForPatientId(Long patientId) {
        return this.terminRepository.findAll().stream().filter(x->x.getPatient().getId()==patientId).collect(Collectors.toList());

    }
	@Override
	public Termin getById(Long Id) {
		return this.terminRepository.getOne(Id);
	}

}