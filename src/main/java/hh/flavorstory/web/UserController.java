package hh.flavorstory.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import hh.flavorstory.domain.UserRepository;
import hh.flavorstory.domain.User;

@Controller
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // User info/profile page (logged-in users only)
    @GetMapping("/profile/{id}")
    public String userProfile(@PathVariable("id") Long id, Model model) {
        var user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        // Initialize collections to avoid lazy-loading issues in the view
        if (user.getCreatedRecipes() != null) user.getCreatedRecipes().size();
        if (user.getFavorites() != null) user.getFavorites().size();
        model.addAttribute("user", user);
        return "profile";
    }

    // Show form to edit user profile (logged-in users only)
    @GetMapping("/profile/{id}/edit")
    public String editUserProfileForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id)));
        return "editProfile";
    }

    // Update user profile (logged-in users only)
    @PostMapping("/profile/{id}/edit")
    public String editUserProfile(@PathVariable("id") Long id, User updatedUser) {
        // Only allow the owner of the profile or an admin to update
        if (!isAdminOrOwner(id)) {
            return "redirect:/login";
        }
        updatedUser.setUserId(id);
        userRepository.save(updatedUser);
        return "redirect:/profile/{id}";
    }

    // Helper: returns true if current user is admin or owner of resource
    private boolean isAdminOrOwner(Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        // Admin check
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        // Owner check by username
        String currentUsername = auth.getName();
        return userRepository.findById(userId).map(u -> u.getUsername().equals(currentUsername)).orElse(false);
    }
}