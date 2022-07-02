package com.healthDocs.healthDocs.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.healthDocs.healthDocs.exceptions.InvalidArgumentsException;
import com.healthDocs.healthDocs.service.PendingPatientService;

@Controller
@RequestMapping("/")
public class IndexController {
	
	private final PendingPatientService pendingPatientService;
	public IndexController(PendingPatientService pendingPatientService) {
		this.pendingPatientService = pendingPatientService;
	}
    @GetMapping
    public String getIndex(){
        return "pochetna";
    }

    @GetMapping(value="/about")
    public String about(){
        return "zaNas";
    }
    
    @GetMapping(value="/register")
    public String register(Model model){
        return "registracija";
    }
    
    @PostMapping(value="/register")
    public String register(HttpServletRequest request, Model model) throws InvalidArgumentsException {
    	String EMBG = request.getParameter("EMBG");
    	String username = request.getParameter("username");
    	String password = request.getParameter("password");
    	String firstName = request.getParameter("firstName");
    	String lastName = request.getParameter("lastName");
    	String insurance = request.getParameter("insurance");
    	Boolean insuranceBool = false;
    	
    	if (username == null || username.isEmpty() || password == null || password.isEmpty() || 
    			EMBG == null || 
    			EMBG.isEmpty() || 
    			firstName.isEmpty() 
    			|| firstName == null ||
    			lastName.isEmpty() ||
    			lastName == null ||
    			insurance == null ||
    			insurance.isEmpty()) {
            throw new InvalidArgumentsException();
        }
    	if(insurance.equals("Yes")) {
    		insuranceBool = true;
    	}
    	
    	this.pendingPatientService.createPendingPatient(EMBG, username, password, firstName, lastName, insuranceBool);
    	
    	return "redirect:/";
    	
    	
    }
}