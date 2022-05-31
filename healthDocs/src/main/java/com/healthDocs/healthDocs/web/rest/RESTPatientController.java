package com.healthDocs.healthDocs.web.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.healthDocs.healthDocs.web.rest.responses.DeleteTerminResponse;
import com.healthDocs.healthDocs.web.rest.responses.GetTerminiResponse;
import com.healthDocs.healthDocs.web.rest.responses.PendingPatientResponse;

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
	public ResponseEntity<PendingPatientResponse> registerPatient(@RequestBody PendingPatient pendingPatient ) {
		this.pendingPatientService.createPendingPatient(pendingPatient.getEMBG(), pendingPatient.getUsername(), pendingPatient.getPassword(), pendingPatient.getFirstName(), pendingPatient.getLastName(), pendingPatient.getInsurance());
		PendingPatientResponse pendingPatientResponse = new PendingPatientResponse(pendingPatient.getEMBG(),pendingPatient.getUsername(),pendingPatient.getPassword(),pendingPatient.getFirstName(),pendingPatient.getLastName(),pendingPatient.getInsurance());
		return new ResponseEntity<>(pendingPatientResponse,HttpStatus.OK);
	}
	
	@GetMapping(value="/termin/deleteTermin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeleteTerminResponse> deleteTermin(@RequestParam(required = true) Long terminID){
		DeleteTerminResponse deleteTerminResponse = null;
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Termin> termins=this.terminService.
                findBySetForPatientId(this.userRepository.findByUsername(userDetails.getUsername()).get().getId()); // check if Termin belongs to patient
        if(termins.isEmpty()){
            //termin doesn't belong to patient
        	deleteTerminResponse = new DeleteTerminResponse(false);
        	return new ResponseEntity<>(deleteTerminResponse,HttpStatus.OK);
        	
        }else{
            this.terminService.deleteById(terminID);
            deleteTerminResponse = new DeleteTerminResponse(true);
        	return new ResponseEntity<>(deleteTerminResponse,HttpStatus.OK);

        }

    }
	
	@GetMapping(value="/getTermini",  produces = MediaType.APPLICATION_JSON_VALUE)
     public ResponseEntity<GetTerminiResponse> getTermini(){
		GetTerminiResponse getTerminiResponse = null;
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Termin> terminList=this.terminService.findBySetForPatientId(userRepository.findByUsername(userDetails.getUsername()).get().getId());
        getTerminiResponse = new GetTerminiResponse(terminList);
        //List<Termin> terminList=this.terminService.listAll();
        return new ResponseEntity<>(getTerminiResponse,HttpStatus.OK);
    }
	
}
