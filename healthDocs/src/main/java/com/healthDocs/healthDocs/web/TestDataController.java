package com.healthDocs.healthDocs.web;

import com.healthDocs.healthDocs.model.Role;
import com.healthDocs.healthDocs.model.Termin;
import com.healthDocs.healthDocs.model.TerminType;
import com.healthDocs.healthDocs.model.User;
import com.healthDocs.healthDocs.repository.TerminRepository;
import com.healthDocs.healthDocs.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

// Nabrzina da ja popuni bazata so podatoci za testiranje

@Controller
@RequestMapping("/data")
public class TestDataController {

    private final UserRepository userRepository;
    private final TerminRepository terminRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random;

    public TestDataController(UserRepository userRepository,
                              TerminRepository terminRepository,
                              PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.terminRepository = terminRepository;
        this.passwordEncoder = passwordEncoder;
        this.random = new Random();
    }

    @GetMapping("/setUsers")
    public String addUsers() {
        String pass = this.passwordEncoder.encode("123");
        userRepository.save(new User("123", "admin", pass, "admin_1", "admin_2", Role.ROLE_ADMIN, true));
        userRepository.save(new User("123", "patient", pass, "patient_1", "patient_2", Role.ROLE_PATIENT, true));
        userRepository.save(new User("123", "doctor", pass, "doctor_1", "doctor_2", Role.ROLE_DOCTOR, true));

        return "redirect:/";
    }

    @GetMapping("/setUser")
    public String addUser(@RequestParam String user, @RequestParam Role role) {
        String pass = this.passwordEncoder.encode("123");
        userRepository.save(new User("123", user, pass, user + "_1", user + "_2", role, true));
        return "redirect:/";
    }

    @GetMapping("/setTerminiFor/{id}")
    public String addTerminiToDoctorById(@PathVariable Long id) {
        List<User> pacienti = this.userRepository.findAllByRole(Role.ROLE_PATIENT).get();
        User doctor = userRepository.findById(id).get();
        if (pacienti != null && !pacienti.isEmpty()) {
            pacienti.stream().forEach(pacient -> {
                Termin termin = new Termin(doctor, pacient, LocalDateTime.now(), random.nextInt() + " random", TerminType.MEDICAL_PRESCRIPTION);
                terminRepository.save(termin);
            });
        }

        return "redirect:/";
    }
}
