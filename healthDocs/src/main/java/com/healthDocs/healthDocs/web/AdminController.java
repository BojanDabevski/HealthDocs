package com.healthDocs.healthDocs.web;

import com.healthDocs.healthDocs.model.Role;
import com.healthDocs.healthDocs.model.User;
import com.healthDocs.healthDocs.repository.PendingPatientRepository;
import com.healthDocs.healthDocs.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final PendingPatientRepository pendingPatientRepository;
    private final PasswordEncoder passwordEncoder;
    public AdminController(UserRepository userRepository,
                           PendingPatientRepository patientRepository,
                           PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.pendingPatientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = {"", "/"})
    public String redirect() {
        return "redirect:/admin/login";
    }

    @GetMapping("/login")
    public String loginAdmin(HttpServletRequest request) {
        if (isLoggedInAsRole("admin", request)) {
            return "redirect:/admin/panel?view=all";
        }
        return "najava";
    }

    @PostMapping("/login")
    public String postLoginAdmin(@RequestParam String username,
                                 @RequestParam String password,
                                 HttpServletRequest request)
    {
        Optional<User> user = this.userRepository.findByUsername(username);
        if ( user.isPresent() && user.get().getRole().equals(Role.ROLE_ADMIN) &&
                passwordEncoder.matches(password, user.get().getPassword()) )
        {
            request.getSession().setAttribute("admin", user.get());
            return "redirect:/admin/panel?view=all";
        }
        // else
        return "redirect:/admin/login?status=BAD_CREDENTIALS";
    }

    @GetMapping("/logout")
    public String logoutAdmin(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }

    @GetMapping("/panel")
    public String getAdminPanel(@RequestParam String view, HttpServletRequest request, Model model) {
        if (isLoggedInAsRole("admin", request)) {
            List users;
            model.addAttribute("pending", false);
            switch (view) {
                case "pending":
                    users = this.pendingPatientRepository.findAll();
                    model.addAttribute("pending", true);
                    break;
                case "doctors":
                    users = this.userRepository.findAllByRole(Role.ROLE_DOCTOR).get();
                    break;
                case "patients":
                    users = this.userRepository.findAllByRole(Role.ROLE_PATIENT).get();
                    break;
                default: // all
                    users = this.userRepository.findAll();
                    break;
            }

            model.addAttribute("users", users);
            return "adminPanel";
        }
        return "redirect:/admin/login";
    }


    boolean isLoggedInAsRole(String roleName, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(roleName);
        if (user == null) {
            return false;
        }
        return true;
    }
}

