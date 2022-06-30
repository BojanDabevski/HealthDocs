package com.healthDocs.healthDocs.web;

import com.healthDocs.healthDocs.model.Role;
import com.healthDocs.healthDocs.model.User;
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
    private final PasswordEncoder passwordEncoder;
    public AdminController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = {"", "/"})
    public String redirect() {
        return "redirect:/admin/login";
    }

    @GetMapping("/login")
    public String loginAdmin(HttpServletRequest request) {
        if (isLoggedInAsRole("admin", request)) {
            return "redirect:/admin/panel";
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
            return "redirect:/admin/panel";
        }
        // else
        return "redirect:/admin/login?status=BAD_CREDENTIALS";
    }

    @GetMapping("/panel")
    public String getAdminPanel(HttpServletRequest request, Model model) {
        if (isLoggedInAsRole("admin", request)) {
            List<User> users = this.userRepository.findAll();
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

