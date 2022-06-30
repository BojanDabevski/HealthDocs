package com.healthDocs.healthDocs.web;
import com.healthDocs.healthDocs.exceptions.InvalidArgumentsException;
import com.healthDocs.healthDocs.exceptions.NoSuchUserException;
import com.healthDocs.healthDocs.model.Recept;
import com.healthDocs.healthDocs.model.Role;
import com.healthDocs.healthDocs.model.Termin;
import com.healthDocs.healthDocs.model.TerminType;
import com.healthDocs.healthDocs.model.User;
import com.healthDocs.healthDocs.repository.UserRepository;
import com.healthDocs.healthDocs.service.ReceptService;
import com.healthDocs.healthDocs.service.TerminService;
import org.dom4j.rule.Mode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/doctor")
public class DoctorController {
    private final TerminService terminService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReceptService receptService;
    private TerminType type;

    public DoctorController(TerminService terminService, UserRepository userRepository, PasswordEncoder passwordEncoder,ReceptService receptService) {
        this.terminService = terminService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.receptService = receptService;
    }

//    @GetMapping(value = "/login")
//    public RedirectView getLoginPage(Model model) {
//        //model.addAttribute("bodyContent","login");
//        //return "najava-doktor";
//        return new RedirectView("/doctor/termini");
//    }

    @GetMapping(value = "/login")
    public String getLoginPage(Model model) {
        return "najava-doktor";
    }

    @PostMapping(value = "/login")
    public String postLoginPage(HttpServletRequest request, Model model) {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            System.out.println(username + " " + password);
            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                throw new InvalidArgumentsException();
            }
            User user = this.userRepository.findByUsername(username).get();
            // if (user != null && user.getRole().toString().equals("ROLE_DOCTOR")) {
            if (    user != null && user.getRole().equals(Role.ROLE_DOCTOR) &&
                    passwordEncoder.matches(password, user.getPassword()))
            {
                request.getSession().setAttribute("doctor", user);
                return "redirect:/doctor/termini";
            } else {
                throw new NoSuchUserException();
            }
        } catch (Exception e) {
            return "najava-doktor";
        }
//        } catch (InvalidArgumentsException invalidArgumentsException) {
//            return "najava-doktor";
//        } catch (NoSuchUserException noSuchUserException) {
//            return "najava-doktor";
//        }
    }


    @GetMapping(value = "/termini")
    public String getTerminiPage(Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("doctor");
        if (user == null) {
            return "redirect:/doctor/login";
        }
        final Long doctorId = userRepository.findByUsername(user.getUsername()).get().getId();
        List<Termin> terminList = this.terminService.findBySetByDoctorId(doctorId);

        //List<Termin> terminList=this.terminService.listAll();
        model.addAttribute("terminList", terminList);
        return "listTermini";
    }

    @GetMapping(value = "/termini/add")
    public String getAddTerminiPage(Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("doctor");
        if (user == null) {
            return "redirect:/doctor/login";
        }
        List<User> patients =
                this.userRepository.findAll()
                .stream()
                .filter(x -> x.getRole().equals(Role.ROLE_PATIENT))
                .collect(Collectors.toList());
                //.filter(x -> x.getRole().toString().equals("ROLE_PATIENT"))
        model.addAttribute("patients", patients);
        model.addAttribute("terminType", TerminType.values());
        model.addAttribute("editMode", false);

        return "addTermin";
    }

    @PostMapping(value = "/termini/add")
    public String postAddTermini(@RequestParam Long patientId,
                                 @RequestParam String date,
                                 @RequestParam String description,
                                 @RequestParam TerminType terminType,
                                 HttpServletRequest request)
    {
        User user = (User) request.getSession().getAttribute("doctor");
        if (user == null) {
            return "redirect:/doctor/login";
        }

        User patient = this.userRepository.findById(patientId).get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateParsed = LocalDateTime.parse(date, formatter);
        this.terminService.createTermin(user, patient, dateParsed, description, terminType);

        return "redirect:/doctor/termini";
    }

    // http://localhost/doctor/termin/editTermin?terminID=5
    @GetMapping("/termin/editTermin")
    public String getEditTermin(@RequestParam Long terminID, HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("doctor");
        if (user == null) {
            return "redirect:/doctor/login";
        }

        Optional<Termin> findTermin = this.terminService.findById(terminID);
        if (!findTermin.isPresent()) {
            return "redirect:/doctor/termini";
        }

        Termin termin = findTermin.get();
        model.addAttribute("editMode", true);
        model.addAttribute("terminType", TerminType.values());
        model.addAttribute("terminData", termin);

        return "addTermin";
    }

    @PostMapping("/termin/editTermin")
    public String postEditTermin(@RequestParam Long terminID,
                                 @RequestParam String date,
                                 @RequestParam String description,
                                 @RequestParam TerminType terminType,
                                 HttpServletRequest request)
    {
        User user = (User) request.getSession().getAttribute("doctor");
        if (user == null) {
            return "redirect:/doctor/login";
        }

        Optional<Termin> findTermin = this.terminService.findById(terminID);
        if (!findTermin.isPresent()) {
            return "redirect:/doctor/termini";
        }

        Termin termin = findTermin.get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateParsed = LocalDateTime.parse(date, formatter);
        termin.setDateAndTime(dateParsed);
        termin.setLocation(description);
        termin.setType(terminType);
        this.terminService.save(termin);

        return "redirect:/doctor/termini";
    }

    @GetMapping(value = "/termin/deleteTermin")
    public String deleteTermin(@RequestParam(required = true) Long terminID, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("doctor");
        List<Termin> termins = this.terminService
                .findBySetByDoctorId(user.getId()).stream().filter(x -> x.getId() == terminID)
                .collect(Collectors.toList()); // check if Termin belongs to doctor
        if (termins.isEmpty()) {
            //termin doesn't belong to doctor
        } else {
            this.terminService.deleteById(terminID);

        }
        return "redirect:/doctor/termini";

    }
    
    @GetMapping(value = "/doctor/recept")
    public String getDoctorRecept(Model model, HttpServletRequest request) {
    	User user = (User) request.getSession().getAttribute("doctor");
        if (user == null) {
            return "redirect:/doctor/login";
        }
        final Long doctorId = userRepository.findByUsername(user.getUsername()).get().getId();
        List<Recept> terminList = this.receptService.findBySetByDoctorId(doctorId);

        //List<Termin> terminList=this.terminService.listAll();
        model.addAttribute("receptList", terminList);
        return "listRecepti";
    }
    
    

    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/doctor/login";
    }
}




