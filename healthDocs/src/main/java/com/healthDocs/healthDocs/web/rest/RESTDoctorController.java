package com.healthDocs.healthDocs.web.rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthDocs.healthDocs.exceptions.InvalidArgumentsException;
import com.healthDocs.healthDocs.exceptions.NoSuchUserException;
import com.healthDocs.healthDocs.model.Termin;
import com.healthDocs.healthDocs.model.TerminType;
import com.healthDocs.healthDocs.model.User;
import com.healthDocs.healthDocs.repository.UserRepository;
import com.healthDocs.healthDocs.service.TerminService;

@RestController
@RequestMapping("api/doctor")
public class RESTDoctorController {
	
	private final TerminService terminService;
	private final UserRepository userRepository;
	
	public RESTDoctorController(TerminService terminService,UserRepository userRepository) {
		this.terminService = terminService;
		this.userRepository = userRepository;
	}

	 @PostMapping(value="/termini/add")
	    public ResponseEntity<List<Termin>> postAddTermini( @RequestParam() String birthdaytime,@RequestParam() Long dropdown, HttpServletRequest request,@RequestParam() String description,@RequestParam() TerminType terminType){


	        User user=(User) request.getSession().getAttribute("doctor");
	        if(user == null){
	            return null;
	        }
	        User patient=this.userRepository.findById(dropdown).get();
	        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
	        LocalDateTime datum=LocalDateTime.parse(birthdaytime,formatter);
	        this.terminService.createTermin(user,patient,datum,description,terminType);
	        return new ResponseEntity<>(this.terminService.findBySetByDoctorId(user.getId()),HttpStatus.OK);
	    }


	   @GetMapping(value="/termin/deleteTermin")
	   public ResponseEntity<String> deleteTermin(@RequestParam(required = true) Long terminID,HttpServletRequest request){
	       User user=(User) request.getSession().getAttribute("doctor");
	       List<Termin> termins=this.terminService.
	               findBySetByDoctorId(user.getId()).stream().filter(x->x.getId()==terminID).
	               collect(Collectors.toList()); // check if Termin belongs to doctor
	       if(termins.isEmpty()){
	            //termin doesn't belong to doctor
	    	   return new ResponseEntity<>("false",HttpStatus.OK);
	       }else{
	           this.terminService.deleteById(terminID);

	       }
	       return new ResponseEntity<>("true",HttpStatus.OK);

	   }
	   
	   @PostMapping(value="/login")
	    public ResponseEntity<String> postLoginPage(HttpServletRequest request){
	        User user=null;
	        try {
	            String username = request.getParameter("username");
	            String password = request.getParameter("password");
	            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
	                throw new InvalidArgumentsException();
	            }

	             user = this.userRepository.findByUsernameAndPassword(username, password).orElse(null);
	            if (user != null && user.getRole().toString().equals("ROLE_DOCTOR")) {
	                request.getSession().setAttribute("doctor", user);
	                return new ResponseEntity<>(user.toString(),HttpStatus.OK);
	            }else{
	                throw new NoSuchUserException();
	            }
	        }catch(InvalidArgumentsException invalidArgumentsException){

	            return new ResponseEntity<>(invalidArgumentsException.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	        }catch (NoSuchUserException noSuchUserException){
	            return new ResponseEntity<>(noSuchUserException.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

	        }

	    }
}
