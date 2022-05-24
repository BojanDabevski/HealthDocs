package com.healthDocs.healthDocs.web;


import com.healthDocs.healthDocs.exceptions.InvalidArgumentsException;
import com.healthDocs.healthDocs.exceptions.NoSuchUserException;
import com.healthDocs.healthDocs.model.Termin;
import com.healthDocs.healthDocs.model.TerminType;
import com.healthDocs.healthDocs.model.User;
import com.healthDocs.healthDocs.repository.UserRepository;
import com.healthDocs.healthDocs.service.TerminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/*@Controller

@RequestMapping("/doctor")

 */

public class DoctorController {
    private final TerminService terminService;
    private final UserRepository userRepository;
    private TerminType type;

    public DoctorController(TerminService terminService,UserRepository userRepository){
        this.terminService=terminService;
        this.userRepository=userRepository;
    }
   /* @GetMapping(value = "/login")
    public RedirectView getLoginPage(Model model) {
        //model.addAttribute("bodyContent","login");
        //return "najava-doktor";
        return new RedirectView("/doctor/termini");
    }
*/
    @GetMapping(value="/login")
    public String getLoginPage(Model model){
        return "najava-doktor";
    }

    @PostMapping(value="/login")
    public String postLoginPage(HttpServletRequest request,Model model){
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
                return "redirect:/doctor/termini";
            }else{
                throw new NoSuchUserException();
            }
        }catch(InvalidArgumentsException invalidArgumentsException){

            return "najava-doktor";
        }catch (NoSuchUserException noSuchUserException){
            return "najava-doktor";
        }

    }


    @GetMapping(value="/termini")

    public String getTerminiPage(Model model, HttpServletRequest request){



        User user=(User) request.getSession().getAttribute("doctor");
        if(user == null){
            return "redirect:/doctor/login";
        }
        List<Termin> terminList=this.terminService.findBySetByDoctorId(userRepository.findByUsername(user.getUsername()).get().getId());
        //List<Termin> terminList=this.terminService.listAll();
        model.addAttribute("terminList",terminList);
        return "listTermini";
    }

    @GetMapping(value="/termini/add")
    public String getAddTerminiPage( Model model, HttpServletRequest request){



        User user=(User) request.getSession().getAttribute("doctor");
        if(user == null){
            return "redirect:/doctor/login";
        }
        List<User> patients=this.userRepository.findAll().stream().filter(x->x.getRole().toString().equals("ROLE_PATIENT")).collect(Collectors.toList());
        model.addAttribute("patients",patients);


        return "addTermin";
    }

    @PostMapping(value="/termini/add")
    public String postAddTermini( @RequestParam() String birthdaytime,@RequestParam() Long dropdown, HttpServletRequest request,@RequestParam() String description){


        User user=(User) request.getSession().getAttribute("doctor");
        if(user == null){
            return "redirect:/doctor/login";
        }
        User patient=this.userRepository.findById(dropdown).get();
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime datum=LocalDateTime.parse(birthdaytime,formatter);
        this.terminService.createTermin(user,patient,datum,description,type);
        return "redirect:/doctor/termini";
    }


   @GetMapping(value="/termin/deleteTermin")
   public String deleteTermin(@RequestParam(required = true) Long terminID,HttpServletRequest request){
       User user=(User) request.getSession().getAttribute("doctor");
       List<Termin> termins=this.terminService.
               findBySetByDoctorId(user.getId()).stream().filter(x->x.getId()==terminID).
               collect(Collectors.toList()); // check if Termin belongs to doctor
       if(termins.isEmpty()){
            //termin doesn't belong to doctor
       }else{
           this.terminService.deleteById(terminID);

       }
       return "redirect:/doctor/termini";

   }



    @GetMapping(value="/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/doctor/login";
    }




    }




