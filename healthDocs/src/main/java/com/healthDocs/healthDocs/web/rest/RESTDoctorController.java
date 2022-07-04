package com.healthDocs.healthDocs.web.rest;

import com.healthDocs.healthDocs.exceptions.InvalidArgumentsException;
import com.healthDocs.healthDocs.exceptions.NoSuchUserException;
import com.healthDocs.healthDocs.model.Termin;
import com.healthDocs.healthDocs.model.TerminType;
import com.healthDocs.healthDocs.model.User;
import com.healthDocs.healthDocs.repository.UserRepository;
import com.healthDocs.healthDocs.service.TerminService;
import com.healthDocs.healthDocs.web.rest.responses.DeleteTerminResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/doctor")
public class RESTDoctorController {

    private final TerminService terminService;
    private final UserRepository userRepository;

    public RESTDoctorController(TerminService terminService, UserRepository userRepository) {
        this.terminService = terminService;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/termini/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity <List <Termin>> postAddTermini(@RequestParam() String birthdaytime, @RequestParam() Long dropdown, HttpServletRequest request, @RequestParam() String description, @RequestParam() TerminType terminType) {


        User user = (User) request.getSession().getAttribute("doctor");
        if (user == null) {
            return null;
        }
        User patient = this.userRepository.findById(dropdown).get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime datum = LocalDateTime.parse(birthdaytime, formatter);
        this.terminService.createTermin(user, patient, datum, description, terminType);
        return new ResponseEntity <>(this.terminService.findBySetByDoctorId(user.getId()), HttpStatus.OK);
    }


    @GetMapping(value = "/termin/deleteTermin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity <DeleteTerminResponse> deleteTermin(@RequestParam(required = true) Long terminID, HttpServletRequest request) {
        DeleteTerminResponse deleteTerminResponse = null;
        User user = (User) request.getSession().getAttribute("doctor");
        List <Termin> termins = this.terminService.
                findBySetByDoctorId(user.getId()).stream().filter(x -> x.getId() == terminID).
                collect(Collectors.toList()); // check if Termin belongs to doctor
        if (termins.isEmpty()) {
            //termin doesn't belong to doctor
            deleteTerminResponse = new DeleteTerminResponse(false);
            return new ResponseEntity <>(deleteTerminResponse, HttpStatus.OK);
        } else {
            this.terminService.deleteById(terminID);
            deleteTerminResponse = new DeleteTerminResponse(false);
            return new ResponseEntity <>(deleteTerminResponse, HttpStatus.OK);

        }


    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity <String> postLoginPage(HttpServletRequest request) {
        User user = null;
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                throw new InvalidArgumentsException();
            }

            user = this.userRepository.findByUsernameAndPassword(username, password).orElse(null);
            if (user != null && user.getRole().toString().equals("ROLE_DOCTOR")) {
                request.getSession().setAttribute("doctor", user);
                return new ResponseEntity <>(user.toString(), HttpStatus.OK);
            } else {
                throw new NoSuchUserException();
            }
        } catch (InvalidArgumentsException invalidArgumentsException) {

            return new ResponseEntity <>(invalidArgumentsException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoSuchUserException noSuchUserException) {
            return new ResponseEntity <>(noSuchUserException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
}
