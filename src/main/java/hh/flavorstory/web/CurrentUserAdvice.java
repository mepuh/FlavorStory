package hh.flavorstory.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import hh.flavorstory.domain.UserRepository;
import hh.flavorstory.domain.Recipe;

import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class CurrentUserAdvice {

    private final UserRepository userRepository;

    public CurrentUserAdvice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Adds current user info to the model for all controllers
    @ModelAttribute
    public void addCurrentUserToModel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            userRepository.findByUsername(auth.getName()).ifPresent(user -> {
                model.addAttribute("currentUserId", user.getUserId());
                model.addAttribute("currentUsername", user.getUsername());
                model.addAttribute("currentUser", user);
                // provide a set of favorite recipe ids for fast checks in templates
                Set<Long> favoriteIds = user.getFavorites() == null ? Set.of()
                        : user.getFavorites().stream().map(Recipe::getRecipeId).collect(Collectors.toSet());
                model.addAttribute("favoriteIds", favoriteIds);
            });
        }
    }
}
