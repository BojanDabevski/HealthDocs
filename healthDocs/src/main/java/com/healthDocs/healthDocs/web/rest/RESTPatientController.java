package com.healthDocs.healthDocs.web.rest;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthDocs.healthDocs.model.PendingPatient;
import com.healthDocs.healthDocs.model.Termin;
import com.healthDocs.healthDocs.repository.UserRepository;
import com.healthDocs.healthDocs.service.PendingPatientService;
import com.healthDocs.healthDocs.service.TerminService;

@RestController
@RequestMapping("api/patient")
public class RESTPatientController {
	
	private final PendingPatientService pendingPatientService;
	private final TerminService terminService;
	private final UserRepository userRepository;
	public RESTPatientController(PendingPatientService pendingPatientService,TerminService terminService,UserRepository userRepository) {
		this.pendingPatientService = pendingPatientService;
		this.terminService = terminService;
		this.userRepository = userRepository;
	}

	@PostMapping("/register")
	public PendingPatient registerPatient(@RequestBody PendingPatient pendingPatient ) {
		this.pendingPatientService.createPendingPatient(pendingPatient.getEMBG(), pendingPatient.getUsername(), pendingPatient.getPassword(), pendingPatient.getFirstName(), pendingPatient.getLastName(), pendingPatient.getInsurance());
		return pendingPatient;
	}
	
	@GetMapping(value="/termin/deleteTermin")
    public Long deleteTermin(@RequestParam(required = true) Long terminID){
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Termin> termins=this.terminService.
                findBySetForPatientId(this.userRepository.findByUsername(userDetails.getUsername()).get().getId()); // check if Termin belongs to patient
        if(termins.isEmpty()){
            //termin doesn't belong to patient
        }else{
            this.terminService.deleteById(terminID);
            return terminID;

        }
        return (long) -1;

    }
	
	@GetMapping(value="/getTermini")
     public List<Termin> getTermini(){
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Termin> terminList=this.terminService.findBySetForPatientId(userRepository.findByUsername(userDetails.getUsername()).get().getId());
        //List<Termin> terminList=this.terminService.listAll();
        return terminList;
    }
	
}
