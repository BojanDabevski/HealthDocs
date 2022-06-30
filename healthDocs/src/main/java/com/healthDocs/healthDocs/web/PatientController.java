package com.healthDocs.healthDocs.web;


import com.healthDocs.healthDocs.model.Recept;
import com.healthDocs.healthDocs.model.Termin;
import com.healthDocs.healthDocs.repository.UserRepository;
import com.healthDocs.healthDocs.service.ReceptService;
import com.healthDocs.healthDocs.service.TerminService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/patient")
public class PatientController {

private final TerminService terminService;
private final ReceptService receptService;
private final UserRepository userRepository;
    public PatientController(TerminService terminService, ReceptService receptService, UserRepository userRepository) {
        this.terminService=terminService;
        this.receptService = receptService;
        this.userRepository=userRepository;
       
    }

    @GetMapping(value = "/login")
    public String getLoginPage(Model model) {
        model.addAttribute("bodyContent","login");
        return "najava-pacient";
    }
    @GetMapping(value="/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/patient/login";
    }

    @GetMapping(value="/termini")
    public String getTerminiPage(@RequestParam(required = false) String error, Model model){
        if(error != null && !error.isEmpty()){
            model.addAttribute("hasError",true);
            model.addAttribute("error",error);
        }
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Termin> terminList=this.terminService.findBySetForPatientId(userRepository.findByUsername(userDetails.getUsername()).get().getId());
        //List<Termin> terminList=this.terminService.listAll();
        model.addAttribute("terminList",terminList);
        return "listTerminiPacient";
    }



    @GetMapping(value="/termin/deleteTermin")
    public String deleteTermin(@RequestParam(required = true) Long terminID){
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Termin> termins=this.terminService.
                findBySetForPatientId(this.userRepository.findByUsername(userDetails.getUsername()).get().getId()); // check if Termin belongs to patient
        if(termins.isEmpty()){
            //termin doesn't belong to patient
        }else{
            this.terminService.deleteById(terminID);

        }
        return "redirect:/patient/termini";

    }
    @GetMapping(value="/recept")
    public String getReceptPage(@RequestParam(required = false) String error, Model model){
        if(error != null && !error.isEmpty()){
            model.addAttribute("hasError",true);
            model.addAttribute("error",error);
        }
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Recept> receptList=this.receptService.findBySetForPatientId(userRepository.findByUsername(userDetails.getUsername()).get().getId());
        //List<Termin> terminList=this.terminService.listAll();
        model.addAttribute("receptList",receptList);
        return "listReceptPacient";
    }
    @GetMapping(value="/termin/deleteRecept")
    public String deleteRecept(@RequestParam(required = true) Long receptID){
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Recept> recepts=this.receptService.
                findBySetForPatientId(this.userRepository.findByUsername(userDetails.getUsername()).get().getId()); // check if Recept belongs to patient
        if(recepts.isEmpty()){
            //termin doesn't belong to patient
        }else{
            this.receptService.deleteById(receptID);

        }
        return "redirect:/patient/recept";

    }

}
