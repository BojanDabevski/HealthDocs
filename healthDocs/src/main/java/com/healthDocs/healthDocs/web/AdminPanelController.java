package com.healthDocs.healthDocs.web;

import com.healthDocs.healthDocs.model.Role;
import com.healthDocs.healthDocs.model.User;
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
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/admin")
public class AdminPanelController {

    private final UserRepository userRepository;
    public AdminPanelController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
            return "redirect:/admin/panel";
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
            return "redirect:/admin/panel";
        }
        return "redirect:/admin/panel";
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
