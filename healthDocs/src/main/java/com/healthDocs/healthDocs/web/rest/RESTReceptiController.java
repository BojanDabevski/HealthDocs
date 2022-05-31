package com.healthDocs.healthDocs.web.rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthDocs.healthDocs.model.Termin;
import com.healthDocs.healthDocs.model.User;
import com.healthDocs.healthDocs.repository.UserRepository;
import com.healthDocs.healthDocs.service.ReceptService;
import com.healthDocs.healthDocs.service.TerminService;
import com.healthDocs.healthDocs.web.rest.responses.CreateReceptResponse;

@RestController
@RequestMapping("api/recepti")
public class RESTReceptiController {
	private final UserRepository userRepository;
    private final ReceptService receptService;
    private final TerminService terminService;
    
    public RESTReceptiController(UserRepository userRepository,ReceptService receptService,TerminService terminService) {
    	this.userRepository = userRepository;
    	this.receptService = receptService;
    	this.terminService = terminService;
    }
    
    @PostMapping(value="/createRecept",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateReceptResponse> createRecept(@RequestParam() Long patientId, @RequestParam() Long terminId,@RequestParam() String amount,@RequestParam() String nameOfDrug,String genericNameOfDrug,String nalog, String upat ,HttpServletRequest request){

    	CreateReceptResponse createReceptResponse = null;
        User user=(User) request.getSession().getAttribute("doctor");
        if(user == null){
        	createReceptResponse = new CreateReceptResponse("","","","","");
        	createReceptResponse.setError("Doctor not found");
            return new ResponseEntity<>(createReceptResponse,HttpStatus.OK);
        }
        User patient=this.userRepository.findById(patientId).get();
        Termin termin = this.terminService.getById(terminId);
        this.receptService.createRecept(user, patient, termin, amount, nameOfDrug, genericNameOfDrug, nalog, upat);
        createReceptResponse = new CreateReceptResponse(amount,nameOfDrug,genericNameOfDrug,nalog,upat);
        return new ResponseEntity<>(createReceptResponse,HttpStatus.OK);
    }
    
}
