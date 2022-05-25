package com.healthDocs.healthDocs.web;

import com.healthDocs.healthDocs.model.Recept;
import com.healthDocs.healthDocs.model.Termin;
import com.healthDocs.healthDocs.repository.UserRepository;
import com.healthDocs.healthDocs.service.ReceptService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/recepti")
public class ReceptiController {
    private final UserRepository userRepository;
    private final ReceptService receptService;

    public ReceptiController(UserRepository userRepository, ReceptService receptService) {
        this.userRepository = userRepository;
        this.receptService = receptService;
    }
    @GetMapping
    public String getReceptiPage(@RequestParam(required = false) String error, Model model){
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
}
