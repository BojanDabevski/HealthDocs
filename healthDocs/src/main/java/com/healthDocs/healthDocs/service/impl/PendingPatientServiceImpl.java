package com.healthDocs.healthDocs.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.healthDocs.healthDocs.model.PendingPatient;
import com.healthDocs.healthDocs.model.Role;
import com.healthDocs.healthDocs.model.User;
import com.healthDocs.healthDocs.repository.PendingPatientRepository;
import com.healthDocs.healthDocs.service.PendingPatientService;
import com.healthDocs.healthDocs.service.UserService;

@Service
public class PendingPatientServiceImpl implements PendingPatientService {
	
	
	private PendingPatientRepository pendingPatientRepository; 
	private UserService userService;
	private PasswordEncoder passwordEncoder;
	
	public PendingPatientServiceImpl(PendingPatientRepository pendingPatientRepository, PasswordEncoder passwordEncoder,UserService userService) {
		this.pendingPatientRepository = pendingPatientRepository;
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
	}

	@Override
	public List<PendingPatient> findAll() {
		return this.pendingPatientRepository.findAll();
	}

	@Override
	public void deleteById(Long Id) {
		this.pendingPatientRepository.deleteById(Id);
		
	}

	@Override
	public void createPendingPatient(String EMBG, String username, String password, String firstName, String lastName,
			Boolean insurance) {
		PendingPatient pendingPatient = new PendingPatient(EMBG,username,this.passwordEncoder.encode(password),firstName,lastName,insurance);
		this.pendingPatientRepository.save(pendingPatient);
		
	}

	@Override
	public User approvePendingPatient(Long Id) {
		PendingPatient existingPendingPatient = this.pendingPatientRepository.findById(Id).orElse(null);
		if(existingPendingPatient.equals(null)) {
			return null;
		}else {
			return this.userService.register(existingPendingPatient.getUsername(), existingPendingPatient.getPassword(), existingPendingPatient.getPassword(), existingPendingPatient.getFirstName(), existingPendingPatient.getLastName(), existingPendingPatient.getEMBG(), Role.ROLE_PATIENT, existingPendingPatient.getInsurance());
		}
	}

}
