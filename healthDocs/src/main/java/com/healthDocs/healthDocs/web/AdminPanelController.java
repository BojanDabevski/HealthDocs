package com.healthDocs.healthDocs.web;

import com.healthDocs.healthDocs.model.PendingPatient;
import com.healthDocs.healthDocs.model.PendingStatus;
import com.healthDocs.healthDocs.model.Role;
import com.healthDocs.healthDocs.model.User;
import com.healthDocs.healthDocs.repository.PendingPatientRepository;
import com.healthDocs.healthDocs.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminPanelController {

    private final UserRepository userRepository;
    private final PendingPatientRepository pendingPatientRepository;
    public AdminPanelController(UserRepository userRepository, PendingPatientRepository pendingPatientRepository) {
        this.userRepository = userRepository;
        this.pendingPatientRepository = pendingPatientRepository;
    }

    @GetMapping("/userDetails")
    public String getUserDetails(@RequestParam(required = true) Long id,
                                 HttpServletRequest request,
                                 Model model)
    {
        // Proveri dali e admin za da gi dodade delete opcijata za korisnik
        if (isLoggedInAsRole("admin", request)) {
            model.addAttribute("isAdmin", true);
            model.addAttribute("roles", Role.values());
        } else {
            model.addAttribute("isAdmin", false);
        }

        // Proveri dali korisnikot postoi
        Optional<User> user = this.userRepository.findById(id);
        if (!user.isPresent()) {
            return "redirect:/admin/panel?view=all";
        }

        model.addAttribute("user", user.get());
        return "promeniKorisnik";
    }

    @PostMapping("/saveUser")
    public String saveUser(@RequestParam Long id,
                           @RequestParam String username,
                           @RequestParam String name,
                           @RequestParam String surname,
                           @RequestParam String embg,
                           @RequestParam(required = false) String insurance,
                           @RequestParam Role role)
    {
        Optional<User> findUser = this.userRepository.findById(id);
        if (findUser.isPresent()) {
            Boolean insurance1 = false;
            try {
                if (insurance != null) {
                    insurance1 = true;
                }
            } catch(Exception e) {}
            User newUser = findUser.get();
            newUser.setUsername(username);
            newUser.setFirstName(name);
            newUser.setLastName(surname);
            newUser.setEMBG(embg);
            newUser.setInsurance(insurance1);
            newUser.setRole(role);
            this.userRepository.save(newUser);
            return "redirect:/admin/panel?view=all";
        }
        return "redirect:/admin/panel?view=all";
    }

    @PostMapping("/pendingUser")
    public ResponseEntity<String> managePendingUser(@RequestParam Long id, @RequestParam PendingStatus status) {
        Optional<PendingPatient> findPendingPatient = this.pendingPatientRepository.findById(id);
        if (!findPendingPatient.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User doesn't exist");
        }

        PendingPatient p = findPendingPatient.get();
        if (status == PendingStatus.DENY) {
            this.pendingPatientRepository.delete(p);
            return ResponseEntity.ok("Pending patient denied");
        }

        if (status == PendingStatus.APPROVE) {
            User user = new User(p.getEMBG(), p.getUsername(), p.getPassword(), p.getFirstName(),
                    p.getLastName(), Role.ROLE_PATIENT, p.getInsurance());

            this.userRepository.save(user);
            this.pendingPatientRepository.delete(p);
            return ResponseEntity.ok("Pending patient approved");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
    }

    @PostMapping("/deleteUser")
    public ResponseEntity<String> removeUser(@RequestParam Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (!user.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User doesn't exist");
        }

        this.userRepository.delete(user.get());
        return ResponseEntity.ok("User deleted successfully");
    }

    boolean isLoggedInAsRole(String roleName, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(roleName);
        if (user == null) {
            return false;
        }
        return true;
    }
}
