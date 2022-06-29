package com.healthDocs.healthDocs.web;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.healthDocs.healthDocs.exceptions.InvalidArgumentsException;
import com.healthDocs.healthDocs.exceptions.NoSuchUserException;
import com.healthDocs.healthDocs.model.Termin;
import com.healthDocs.healthDocs.model.TerminType;
import com.healthDocs.healthDocs.model.User;
import com.healthDocs.healthDocs.repository.UserRepository;
import com.healthDocs.healthDocs.service.TerminService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final TerminService terminService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public AdminController(TerminService terminService,UserRepository userRepository,PasswordEncoder passwordEncoder){
        this.terminService=terminService;
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    @GetMapping(value="/login")
    public String getLoginPage(Model model){
        return "najava-admin";
    }

    @PostMapping(value="/login")
    public String postLoginPage(HttpServletRequest request, Model model, Principal principal){
        User user=null;
        String currentlyLoggedIn=principal.getName();
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                throw new InvalidArgumentsException();
            }

            //user = this.userRepository.findByUsernameAndPassword(username, password).orElse(null);
            user=this.userRepository.findByUsername(username).orElse(null);
            if (user != null && passwordEncoder.matches(password,user.getPassword()) &&user.getRole().toString().equals("ROLE_ADMIN")&&currentlyLoggedIn.equals(user.getUsername())) {
                request.getSession().setAttribute("admin", user);
                return "redirect:/admin/termini";
            }else{
                throw new NoSuchUserException();
            }
        }catch(InvalidArgumentsException invalidArgumentsException){

            return "najava-admin";
        }catch (NoSuchUserException noSuchUserException){
            return "najava-admin";
        }

    }


    @GetMapping(value="/termini")

    public String getTerminiPage(Model model, HttpServletRequest request,Principal principal){


        String currentlyLoggedIn=principal.getName();
        User user=(User) request.getSession().getAttribute("admin");
        if(user == null && currentlyLoggedIn.equals(user.getUsername())){
            return "redirect:/admin/login";
        }
        List<Termin> terminList=this.terminService.listAll();
        //List<Termin> terminList=this.terminService.listAll();
        model.addAttribute("terminList",terminList);
        return "listTerminiAdmin";
    }

    @PostMapping(value="/termini/add")
    public String postAddTermini(@RequestParam() String birthdaytime, @RequestParam() Long dropdown,@RequestParam Long dropdown1, HttpServletRequest request, @RequestParam() String description,@RequestParam() String terminType, Principal principal){

        String currentlyLoggedIn=principal.getName();
        User user=(User) request.getSession().getAttribute("admin");
        if(user == null && currentlyLoggedIn.equals(user.getUsername())){
            return "redirect:/admin/login";
        }
        User patient=this.userRepository.findById(dropdown).get();
        User doctor=this.userRepository.findById(dropdown1).get();
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime datum=LocalDateTime.parse(birthdaytime,formatter);
        this.terminService.createTermin(doctor,patient,datum,description,TerminType.valueOf(terminType));
        return "redirect:/admin/termini";
    }

    @GetMapping(value="/termini/add")
    public String getAddTerminiPage( Model model, HttpServletRequest request,Principal principal){


        String currentlyLoggedIn=principal.getName();
        User user=(User) request.getSession().getAttribute("admin");
        if(user == null && currentlyLoggedIn.equals(user.getUsername())){
            return "redirect:/admin/login";
        }
        List<User> patients=this.userRepository.findAll().stream().filter(x->x.getRole().toString().equals("ROLE_PATIENT")).collect(Collectors.toList());
        List<User> doctors=this.userRepository.findAll().stream().filter(x->x.getRole().toString().equals("ROLE_DOCTOR")).collect(Collectors.toList());
        model.addAttribute("patients",patients);
        model.addAttribute("doctors",doctors);


        return "addTerminAdmin";
    }
    @GetMapping(value="/termin/deleteTermin")
    public String deleteTermin(@RequestParam(required = true) Long terminID,HttpServletRequest request,Principal principal){
        String currentlyLoggedIn=principal.getName();
        User user=(User) request.getSession().getAttribute("admin");
        if(user == null && currentlyLoggedIn.equals(user.getUsername())){
            return "redirect:/admin/login";
        }
       /* List<Termin> termins=this.terminService.
                findBySetByDoctorId(user.getId()).stream().filter(x->x.getId()==terminID).
                collect(Collectors.toList()); // check if Termin belongs to doctor
        if(termins.isEmpty()){
            //termin doesn't belong to doctor
        }else{
            this.terminService.deleteById(terminID);
        }
        */
        this.terminService.deleteById(terminID);
        return "redirect:/admin/termini";

    }



    @GetMapping(value="/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/admin/login";
    }

}