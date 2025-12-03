package hh.flavorstory.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.ui.Model;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import hh.flavorstory.domain.UserRepository;
import hh.flavorstory.domain.User;

@Controller
public class AuthenticationController {

    private UserRepository userRepository;

    public AuthenticationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Show login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Show registration page
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Handle user registration
    @PostMapping("/register")
    public String createUser(User user, BCryptPasswordEncoder passwordEncoder, Model model) {
        // Prevent duplicate usernames
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Username already exists");
            model.addAttribute("user", user);
            return "register";
        }

        // Set default role if not provided
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("ROLE_USER");
        }
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/login";
    }

}
